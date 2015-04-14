package shiller.app;

//import org.jdom2.*;
//import org.jdom2.output.Format;
//import org.jdom2.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.print.attribute.standard.MediaSize;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created with IntelliJ IDEA.
 * User: benfa
 * Date: 28/05/12
 * Time: 8.25
 * To change this template use File | Settings | File Templates.
 */
public class MainPanel extends JScrollPane{
    JFrame parent;
    JPanel sfondo;
    DrawPanel drawPanel;
    File path= null;

    public MainPanel(JFrame parent){
        super();
        this.parent = parent;
        System.out.println(this);
        this.getVerticalScrollBar().setUnitIncrement(20);
        this.getHorizontalScrollBar().setUnitIncrement(20);

        sfondo = new JPanel(null);
        sfondo.setPreferredSize(parent.getSize());
        sfondo.setBackground(new Color(90, 90, 90));

        this.setViewportView(sfondo);
        drawPanel=null;
    }

    public void nuovo(){
        if(drawPanel!=null){
            int confirm = JOptionPane.showConfirmDialog(this, "C'è un progetto ancora aperto,\n sei sicuro di voler cominciare un nuovo progetto?\n " +
                    "Tutti i dati non salvati verranno persi");
            if (confirm==0){
                MyDialog dialog = new MyDialog(parent, true, "Nuova stratigrafia", 0);
                dialog.setVisible(true);
                if(dialog.getSucc()){
                    sfondo.remove(drawPanel);
                    drawPanel = new DrawPanel(dialog.getIntestazione(),this);
                    sfondo.add(drawPanel);
                    MyMenuBar m = (MyMenuBar)MyHasMap.listElement.get("menuBar");
                    m.setZoom(100);
                    path = null;
                }
            }
        }else{
            MyDialog dialog = new MyDialog(parent, true, "Nuova stratigrafia", 0);
            dialog.setVisible(true);
            if(dialog.getSucc()){
                drawPanel = new DrawPanel(dialog.getIntestazione(),this);
                sfondo.add(drawPanel);
                MyMenuBar m = (MyMenuBar)MyHasMap.listElement.get("menuBar");
                m.setZoom(100);
                path = null;
            }
        }
        ((MyFrame)parent).myRepaint();
    }

    public void salvaConNome(){
        JFileChooser fs = new JFileChooser();
        fs.setApproveButtonText("Salva");
        fs.setDialogTitle("Salva con nome");
        int res = fs.showOpenDialog(this);
        if(res==JFileChooser.APPROVE_OPTION){
            path = fs.getSelectedFile();
            salvaAll(path);


        }
    }

    public void setZoom(int zoom){
        if(drawPanel!=null)
            drawPanel.setZoom(zoom);
        MyFrame f = (MyFrame)MyHasMap.listElement.get("myFrame");
        f.myRepaint();
    }

    public void carica(){
        JFileChooser fs = new JFileChooser();
        fs.setApproveButtonText("Carica");
        fs.setDialogTitle("Carica progetto");
        int res = fs.showOpenDialog(this);
        if(res==JFileChooser.APPROVE_OPTION){
            path = fs.getSelectedFile();
            caricaAll(path);
        }
        ((MyFrame)parent).myRepaint();
    }

    public void stampa(){
        PrinterJob pj = null;
        try {
            pj = PrinterJob.getPrinterJob();
            PageFormat format = pj.defaultPage();
            float pageWidth = MediaSize.ISO.A4.getX(MediaSize.INCH) * 72;
            float pageHeight = MediaSize.ISO.A4.getY(MediaSize.INCH) * 72;
            float margin = 0;
            Paper paper = new Paper();
            paper.setSize(pageWidth, pageHeight);
            paper.setImageableArea(margin, margin, pageWidth,
                    pageHeight);
            format.setPaper(paper);
            format = pj.validatePage(format);
            if(pj.printDialog()) {
                pj.setPrintable(drawPanel, format);
                pj.print();
            }
        } catch(Exception ex) {
            if(pj != null) {
                pj.cancel();
            }
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }


    public void salvaAll(File file){
        try{
            OutputStreamWriter bw = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");

            Intestazione i = drawPanel.getIntestazione();
            String xml = "<root prog=\"stratigrafie_1.0\">\n<intestazione "
                    +"committente=\""+parseToSave(i.getCommittente())+"\" "
                    +"via=\""+parseToSave(i.getVia())+"\" "
                    +"paese=\""+parseToSave(i.getPaese())+"\" "
                    +"localita=\""+parseToSave(i.getLocalita())+"\" "
                    +"data=\""+parseToSave(i.getData())+"\" "
                    +"sistemaPerforazione=\""+parseToSave(i.getSistemaPerforazione())+"\" "
                    +"diametroPerforazione=\""+parseToSave(i.getDiametroPerforazione())+"\" "
                    +"profonditaPerforazione=\""+parseToSave(i.getProfonditaPeroforazione())+"\" "
                    +"profonditaPozzo=\""+i.getProfonditaPozzo()+"\" "
                    +"colonnaRivestimento=\""+parseToSave(i.getColonnaRivesetimento())+"\" "
                    +"diametroRivestimento=\""+parseToSave(i.getDiametroRivestimento())+"\" "
                    +"livelloStatico=\""+i.getLivelloStatico()+"\" "
                    +"livelloDinamico=\""+parseToSave(i.getLivelloDinamico())+"\" "
                    +"portataDinamico=\""+parseToSave(i.getPortataDinamico())+"\" "
                    +"profonditaRestrizione=\""+parseToSave(i.getProfonditaRestrizione())+"\">"
                    +parseToSave(i.getInfoAggiuntive())+"</intestazione>";
            bw.write(xml);

            ArrayList<Terreno> ter = drawPanel.getTerreno();
            ArrayList<Riempimento> rie = drawPanel.getRiempimento();
            ArrayList<Filtro> fil = drawPanel.getFiltro();

            for(Terreno t: ter){

                xml="<terreno inizio=\""+t.minizio+"\" fine=\""+(t.minizio+t.mprofondita)+"\" type=\""+t.type+"\" infoAgg=\""+parseToSave(t.infoAgg)+"\"></terreno>\n";
                bw.write(xml);
            }
            for(Riempimento t: rie){

                xml="<riempimento inizio=\""+t.minizio+"\" fine=\""+(t.minizio+t.mprofondita)+"\" type=\""+t.type+"\" infoAgg=\""+parseToSave(t.infoAgg)+"\"></riempimento>\n";
                bw.write(xml);
            }
            for(Filtro t: fil){

                xml="<filtro inizio=\""+t.minizio+"\" fine=\""+(t.minizio+t.mprofondita)+"\" type=\""+t.type+"\" infoAgg=\""+parseToSave(t.infoAgg)+"\"></filtro>\n";
                bw.write(xml);
            }
            bw.write("</root>");
            bw.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    public void caricaAll(File file){
        boolean correctFile=false;
        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String s = br.readLine();
            correctFile = s.equalsIgnoreCase("<root prog=\"stratigrafie_1.0\">");
        }catch (Exception e){
            e.getMessage();
        }
        if(correctFile){
            NodeList terreno = getNodeList("terreno", file.getAbsolutePath());
            NodeList filtro = getNodeList("filtro", file.getAbsolutePath());
            NodeList riempimento = getNodeList("riempimento", file.getAbsolutePath());
            NodeList intestazione = getNodeList("intestazione", file.getAbsolutePath());

            if(drawPanel!=null)
                sfondo.remove(drawPanel);
            Intestazione i = new Intestazione();
            Element intes = (Element)intestazione.item(0);
            i.setCommittente(parseToLoad(intes.getAttribute("committente")));
            i.setVia(parseToLoad(intes.getAttribute("via")));
            i.setPaese(parseToLoad(intes.getAttribute("paese")));
            i.setLocalita(parseToLoad(intes.getAttribute("localita")));
            i.setData(parseToLoad(intes.getAttribute("data")));
            i.setSistemaPerforazione(parseToLoad(intes.getAttribute("sistemaPerforazione")));
            i.setDiametroPerforazione(parseToLoad(intes.getAttribute("diametroPerforazione")));
            i.setProfonditaPeroforazione(parseToLoad(intes.getAttribute("profonditaPerforazione")));
            i.setProfonditaPozzo(Integer.parseInt(parseToLoad(intes.getAttribute("profonditaPozzo"))));
            i.setColonnaRivesetimento(parseToLoad(intes.getAttribute("colonnaRivestimento")));
            i.setDiametroRivestimento(parseToLoad(intes.getAttribute("diametroRivestimento")));
            i.setLivelloStatico(Double.parseDouble(parseToLoad(intes.getAttribute("livelloStatico"))));
            i.setLivelloDinamico(parseToLoad(intes.getAttribute("livelloDinamico")));
            i.setProfonditaRestrizione(parseToLoad(intes.getAttribute("profonditaRestrizione")));
            i.setPortataDinamico(parseToLoad(intes.getAttribute("portataDinamico")));
            i.setInfoAggiuntive(parseToLoad(intes.getTextContent()));
            drawPanel = new DrawPanel(i,this);

            for(int j=0; j<terreno.getLength(); j++){
                Element e = (Element)terreno.item(j);
                HashMap hm = new HashMap();

                System.out.println(Double.parseDouble(e.getAttribute("inizio")));
                System.out.println(Double.parseDouble(e.getAttribute("fine")));
                hm.put("metriInizio", Double.parseDouble(e.getAttribute("inizio")));
                hm.put("metriFine", Double.parseDouble(e.getAttribute("fine")));
                hm.put("type",Integer.parseInt(e.getAttribute("type")));
                hm.put("infoAgg",parseToLoad(e.getAttribute("infoAgg")));
                drawPanel.addTerreno(hm);
            }

            for(int j=0; j<riempimento.getLength(); j++){
                Element e = (Element)riempimento.item(j);
                HashMap hm = new HashMap();
                hm.put("metriInizio",Double.parseDouble(e.getAttribute("inizio")));
                hm.put("metriFine", Double.parseDouble(e.getAttribute("fine")));
                hm.put("type",Integer.parseInt(e.getAttribute("type")));
                hm.put("infoAgg",parseToLoad(e.getAttribute("infoAgg")));
                drawPanel.addRiempimento(hm);
            }

            for(int j=0; j<filtro.getLength(); j++){
                Element e = (Element)filtro.item(j);
                HashMap hm = new HashMap();
                hm.put("metriInizio",Double.parseDouble(e.getAttribute("inizio")));
                hm.put("metriFine", Double.parseDouble(e.getAttribute("fine")));
                hm.put("type",Integer.parseInt(e.getAttribute("type")));
                hm.put("infoAgg",parseToLoad(e.getAttribute("infoAgg")));
                drawPanel.addFiltro(hm);
            }


            sfondo.add(drawPanel);
        }else{
            JOptionPane.showMessageDialog(this, "Il file selezionato non è un file di salvataggio di questo programma", "Errore", JOptionPane.WARNING_MESSAGE);
        }
    }


    public NodeList getNodeList(String tipo, String file){
        System.out.println("file = " + file);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace());  //To change body of catch statement use File | Settings | File Templates.
        }
        Document doc = null;
        try {
            doc = builder.parse(file);
        } catch (SAXException e) {
            System.out.println("errore 1");
            JOptionPane.showMessageDialog(null, e.getStackTrace());  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            System.out.println("errore 2");
            System.out.println(e.getMessage());
        }
        NodeList list = doc.getElementsByTagName(tipo);
        return list;
    }


    public String parseToSave(String str){
        str = str.replaceAll("#","#000");
        str = str.replaceAll("&","#001");
        str = str.replaceAll("\n","#002");
        str = str.replaceAll("<","#003");
        str = str.replaceAll(">","#004");
        str = str.replaceAll("\'","#006");
        str = str.replaceAll("\"","#007");
        return str;
    }

    public String parseToLoad(String str){
        str = str.replaceAll("#000","#");
        str = str.replaceAll("#001","&");
        str = str.replaceAll("#002","\n");
        str = str.replaceAll("#003","<");
        str = str.replaceAll("#004",">");
        str = str.replaceAll("#005","\\");
        str = str.replaceAll("#006","\'");
        str = str.replaceAll("#007","\"");
        return str;
    }
}
