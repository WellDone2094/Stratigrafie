package shiller.app;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: benfa
 * Date: 29/05/12
 * Time: 19.54
 * To change this template use File | Settings | File Templates.
 */
public class Filtro extends JPanel implements Comparable{
    int inizio, profondita, type;
    double minizio,mprofondita;
    String tipo="";
    MyRectangle rect;
    Font font = new Font("Serif", Font.BOLD, 30);
    Image img;
    BufferedImage bimg;
    String infoAgg;

    public Filtro(int inizio, int profondita, double minizio, double mprofondita, String infoAgg,int type, double scala){
        this.inizio = inizio;
        this.profondita = profondita;
        this.minizio = minizio;
        this.mprofondita = mprofondita;
        this.type = type;
        rect = new MyRectangle(new Rectangle(900, inizio, 1550, profondita), scala);
        this.infoAgg = infoAgg;

        Element e = getFiltroType(type);
        tipo = e.getTextContent();

        ImageIcon icon = new ImageIcon(e.getAttribute("img"));
        img = icon.getImage();
        img = createImage(new FilteredImageSource(img.getSource(),
                new CropImageFilter(0, 0, 150, profondita)));

    }

    public void setScala(double scala){
        rect.setScala(scala);
    }

    public boolean contains(Point p){
        if (rect.contains(p)) return true;
        else return false;
    }

    public void draw(Graphics g){
        DrawPanel d = (DrawPanel)MyHasMap.listElement.get("drawPanel");
        g.drawImage(img, 1350, inizio, d);
    }

    public void reset(int inizio, int profondita, int minizio, int mprofondita, String infoAgg, int type, double scala, DrawPanel drawPanel){
        this.inizio = inizio;
        this.profondita = profondita;
        this.minizio = minizio;
        this.mprofondita = mprofondita;
        this.type = type;
        rect = new MyRectangle(new Rectangle(900, inizio, 1550, profondita), scala);
        this.infoAgg = infoAgg;

        Element e = getFiltroType(type);
        tipo = e.getTextContent();

        ImageIcon icon = new ImageIcon(e.getAttribute("img"));
        img = icon.getImage();
        img = createImage(new FilteredImageSource(img.getSource(),
                new CropImageFilter(0, 0, 150, profondita)));
    }

    public String getInfoAgg(){
        return infoAgg;
    }


    @Override
    public int compareTo(Object o) {
        if (o!=null){
            Filtro objOfMyClass = (Filtro)o;
            // qui ci arriva solo se i titoli sono uguali o un titolo è nullo quindi confronto i codici edizione
            if (minizio>objOfMyClass.minizio){
                return 1;
            }else{
                if (minizio==objOfMyClass.minizio){
                    return 0;
                }
            }
        }
        return -1;
    }

    public Element getFiltroType(int type){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace());  //To change body of catch statement use File | Settings | File Templates.
        }
        Document doc = null;
        try {
            doc = builder.parse("strati.xml");
        } catch (SAXException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace());  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace());  //To change body of catch statement use File | Settings | File Templates.
        }
        NodeList list = doc.getElementsByTagName("filtro");
        Element e=null;
        for(int i=0; i<list.getLength(); i++){
            Element f = (Element)list.item(i);
            if(String.valueOf(type).equals(f.getAttribute("id"))){
                e = (Element)list.item(i);
                break;
            }
        }
        return e;
    }

    public String getType(){
        return tipo;
    }
}
