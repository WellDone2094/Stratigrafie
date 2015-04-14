package shiller.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: benfa
 * Date: 28/05/12
 * Time: 9.18
 * To change this template use File | Settings | File Templates.
 */
public class DrawPanel extends JPanel implements MouseListener, ActionListener, Printable{

    private MainPanel parent;
    private Intestazione intestazione;
    private Toolkit desktop;
    private int height, width;
    private double zoom=1;
    private double scala;
    private int fogli;
    private ArrayList<JLabel> arrayLabel;
    private ArrayList<Terreno> arrayTerreno;
    private ArrayList<Riempimento> arrayRiempimento;
    private ArrayList<Filtro> arrayFiltro;
    private ArrayList<ArrayList> arrayRect;
    private ArrayList<MyRectangle> arrayRectIntestazione;
    private ArrayList<MyRectangle> arrayRectTerreno;
    private ArrayList<MyRectangle> arrayRectPozzo;
    private JPopupMenu jPopupMenu;
    private JMenuItem aggiungiTerreno;
    private JMenuItem aggiungiParete;
    private JMenuItem aggiungiFiltro;
    private JMenuItem modificaTerreno;
    private JMenuItem modificaParete;
    private JMenuItem modificaFiltro;
    private JMenuItem rimuoviTerreno;
    private JMenuItem rimuoviParete;
    private JMenuItem rimuoviFiltro;
    private JMenuItem modificaIntestazione;
    private JSeparator sep;
    private double rappProf;
    private Terreno terr;
    private Riempimento riem;
    private Filtro  fil;
    private Image imgScala;
    private Image imgScala2;
    private Image imgTubo;
    private Image imgTappo;
    private Image imgBenfatti;
    private Image imgLvStat;
    private JLabel committente=null;
    private JLabel datiComm=null;
    private JLabel realizzazione=null;
    private JLabel datiTecnici=null;

    public DrawPanel(){

    }

    public DrawPanel(Intestazione intestazione, MainPanel parent){
        super(null);
        MyHasMap.listElement.remove("drawPanel");
        MyHasMap.listElement.put("drawPanel",this);
        this.parent=parent;
        this.intestazione=intestazione;
        this.addMouseListener(this);
        this.setBackground(null);

        desktop = Toolkit.getDefaultToolkit();
        height = desktop.getScreenSize().height-120;
        width = (int)(height*2100.0/2970.0);
        scala = height/2970.0;

        ImageIcon icon = new ImageIcon("immagini/scala8.png");
        imgScala = icon.getImage();
        icon = new ImageIcon("immagini/logo.png");
        imgBenfatti = icon.getImage();
        icon = new ImageIcon("immagini/livelloStatico.png");
        imgLvStat = icon.getImage();
        icon = new ImageIcon("immagini/tappo2.png");
        imgTappo = icon.getImage();

        setIntestazione();

        setArray();
        setPopupMenu();
        setPosition();

        //drawIntestazione();

        writeLabel(10);
    }

    public void setPosition(){
        int top = 10;
        int left;

        int larg = (int) (width * zoom);
        if(larg>parent.getVisibleRect().width-20){
            left = 20;
        }else{
            left = (parent.getVisibleRect().width -larg)/ 2;
        }
        this.setBounds(left, top,(int)(width*zoom), (int)(height*fogli*zoom+500));
        parent.sfondo.setPreferredSize(new Dimension((int)(width*zoom)+30,(int)(height*zoom*fogli)+(10*fogli)+10));
    }

    public void setPopupMenu(){
        jPopupMenu = new JPopupMenu();
        aggiungiTerreno = new JMenuItem("Aggiungi strato terreno");
        aggiungiTerreno.addActionListener(this);
        aggiungiTerreno.setVisible(true);
        jPopupMenu.add(aggiungiTerreno);
        rimuoviTerreno = new JMenuItem("Rimuovi strato terreno");
        rimuoviTerreno.addActionListener(this);
        rimuoviTerreno.setVisible(true);
        jPopupMenu.add(rimuoviTerreno);
        modificaTerreno = new JMenuItem("Modifica strato terreno");
        modificaTerreno.addActionListener(this);
        modificaTerreno.setVisible(true);
        jPopupMenu.add(modificaTerreno);

        aggiungiParete = new JMenuItem("Aggiungi strato parete");
        aggiungiParete.addActionListener(this);
        aggiungiParete.setVisible(true);
        jPopupMenu.add(aggiungiParete);
        rimuoviParete = new JMenuItem("Rimuovi strato parete");
        rimuoviParete.addActionListener(this);
        rimuoviParete.setVisible(true);
        jPopupMenu.add(rimuoviParete);
        modificaParete = new JMenuItem("Modifica strato parete");
        modificaParete.addActionListener(this);
        modificaParete.setVisible(true);
        jPopupMenu.add(modificaParete);

        sep = new JSeparator();
        sep.setVisible(false);
        jPopupMenu.add(sep);

        aggiungiFiltro = new JMenuItem("Aggiungi strato filtro");
        aggiungiFiltro.addActionListener(this);
        aggiungiFiltro.setVisible(true);
        jPopupMenu.add(aggiungiFiltro);
        rimuoviFiltro = new JMenuItem("Rimuovi strato filtro");
        rimuoviFiltro.addActionListener(this);
        rimuoviFiltro.setVisible(true);
        jPopupMenu.add(rimuoviFiltro);
        modificaFiltro = new JMenuItem("Modifica strato filtro");
        modificaFiltro.addActionListener(this);
        modificaFiltro.setVisible(true);
        jPopupMenu.add(modificaFiltro);

        modificaIntestazione = new JMenuItem("Modifica intestazione");
        modificaIntestazione.addActionListener(this);
        modificaIntestazione.setVisible(true);
        jPopupMenu.add(modificaIntestazione);

        jPopupMenu.setVisible(false);
    }

    public void setArray(){
        arrayLabel = new ArrayList<JLabel>();
        arrayTerreno = new ArrayList<Terreno>();
        arrayRiempimento = new ArrayList<Riempimento>();
        arrayFiltro = new ArrayList<Filtro>();
        arrayRect = new ArrayList<ArrayList>();
        arrayRectIntestazione = new ArrayList<MyRectangle>();
        arrayRectTerreno = new ArrayList<MyRectangle>();
        arrayRectPozzo = new ArrayList<MyRectangle>();
        arrayRect.add(arrayRectIntestazione);
        arrayRect.add(arrayRectTerreno);
        arrayRect.add(arrayRectPozzo);

        arrayRectTerreno.add(new MyRectangle(new Rectangle(250,750,650,5000),scala*zoom));
        arrayRectPozzo.add(new MyRectangle(new Rectangle(900,750,650,5000), scala*zoom));
        arrayRectIntestazione.add(new MyRectangle(new Rectangle(0,0,2100,750),scala*zoom));
    }

    public void drawIntestazione(){
        if(committente!=null)
            this.remove(committente);
        if(datiComm!=null)
            this.remove(datiComm);
        if(realizzazione!=null)
            this.remove(realizzazione);
        committente = new JLabel();
        datiComm = new JLabel();
        realizzazione = new JLabel();
        committente.setText("<html><u>Committente:</u></html>");
        datiComm.setText("<html>"+intestazione.getCommittente()+"<br>via " +
                                  intestazione.getVia()+"<br>" +
                                  intestazione.getPaese()+"</html>");
        realizzazione.setText("Pozzo realizzato in loc. "+intestazione.getLocalita()+" in data "+intestazione.getData());
        committente.setHorizontalAlignment(SwingConstants.RIGHT);
        committente.setVerticalAlignment(SwingConstants.NORTH);
        committente.setFont(new Font("Serif",Font.BOLD,48));
        datiComm.setFont(new Font("Serif",0,40));
        datiComm.setVerticalAlignment(SwingConstants.NORTH);
        realizzazione.setFont(new Font("Serif",Font.BOLD,48));
        committente.setBounds(800,200,630,300);
        datiComm.setBounds(1450,200,600,300);
        realizzazione.setBounds(150,500,1900,150);
        realizzazione.setVerticalAlignment(SwingConstants.NORTH);

        this.add(realizzazione); this.add(committente); this.add(datiComm);
    }

    public void myPaint(Graphics g){

        drawSchema(g);
        for(Terreno t: arrayTerreno){
            t.draw(g);
        }

        for(Riempimento t: arrayRiempimento){
            t.draw(g);
        }

        if(fogli==1){
            int ltubo = (int)((intestazione.getProfonditaPozzo()+0.5)*rappProf);
            g.drawImage(imgTubo, 1350, 790, 150, ltubo, this);
            g.drawImage(imgBenfatti, 150, 150, 750, 250, this);
            g.drawImage(imgTappo, 1350, 780+ltubo, 150, 50, this);

        }
        if(fogli==2){
            int ltubo = (int)((intestazione.getProfonditaPozzo()-80)*rappProf);
            g.drawImage(imgTubo, 1350, 790, 150, 1920, this);
            g.drawImage(imgBenfatti, 150, 150, 750, 250, this);
            g.drawImage(imgTubo,1350,3290,150,ltubo,this);
            g.drawImage(imgTappo, 1350, 3280+ltubo, 150, 50, this);
        }

        for(Filtro t: arrayFiltro){
            t.draw(g);
        }

        writeDatiTecnici();

        int lvStat = (int)(intestazione.getLivelloStatico()*rappProf);
        if(lvStat<0){
            lvStat=Math.abs(lvStat);
            lvStat+=800-44;
            g.drawImage(imgLvStat,1350,lvStat,150,80, this);
        }

        super.paintComponents(g);

    }

    public void paint(Graphics g){
        setPosition();
        Graphics2D g2 = (Graphics2D)g;
        g2.scale(scala*zoom, scala*zoom);

        myPaint(g);
    }

    public void setZoom(int zoom){
        this.zoom = zoom/100.0;
        for(Terreno t: arrayTerreno){
            t.setScala(scala*this.zoom);
        }

        for(ArrayList a: arrayRect){
            ArrayList<MyRectangle> b = (ArrayList<MyRectangle>)a;
            for(MyRectangle r: b){
                r.setScala(scala*this.zoom);
            }
        }

        repaint();
    }

    private void drawSchema(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRoundRect(0,0,2100,2970,0,0);
        if(fogli==2){
            g.fillRoundRect(0,2990,2100,2970,0,0);
            drawSchema2(g);
        }

        g.setFont(new Font("Serif",0, 30));
        g.setColor(Color.BLACK);

        g.drawLine(250,800,550,800);
        g.drawLine(650,800,900,800);
        g.drawString("0.0",575,807);


        g.drawLine(900,800,1200,800);
        g.drawString("0.0",1225,807);
        g.drawLine(1300,800,1550,800);

        g.drawLine(50, 50, 2050, 50);
        g.drawLine(50, 50, 50, 2900);
        g.drawLine(2050, 50, 2050, 2900);
        g.drawLine(50, 2900, 2050, 2900);
        g.drawLine(150, 650, 1950, 650);
        g.drawLine(150, 750, 1950, 750);
        g.drawLine(150, 2750, 1950, 2750);
        g.drawLine(150, 650, 150, 2750);
        g.drawLine(900, 650, 900, 2750);
        g.drawLine(1550, 650, 1550, 2750);
        g.drawLine(1950, 650, 1950, 2750);
        g.drawLine(250, 750, 250, 2750);
        g.drawLine(640, 750, 640, 2750);
        g.drawLine(1300, 750, 1300, 2750);
        g.drawImage(imgScala,175,800,30,1900,this);
        g.setFont(new Font("Serif",0,25));
        double passo= 1900/16.0;
        int npassi = 16;

        if(intestazione.getProfonditaPozzo()<=80){
            passo = 1900/16.0;
            npassi = 16;
        }else if(intestazione.getProfonditaPozzo()<=90){
            passo = 1900/18.0;
            npassi = 18;
        }else if(intestazione.getProfonditaPozzo()>90){
            passo = 1900/16.0;
            npassi = 16;
        }


        for(int i=0; i<=npassi; i++){
            int y=(int)(passo*i+810);
            g.drawString(String.valueOf(i*5), 210, y);
        }
    }

    private void drawSchema2(Graphics g){
        g.setColor(Color.BLACK);
        g.drawLine(50,3040,50, 5920);
        g.drawLine(50,3040,2050, 3040);
        g.drawLine(50,5920,2050, 5920);
        g.drawLine(2050,3040,2050, 5920);
        g.drawLine(150,3140, 150, 5715);
        g.drawLine(1950,3140, 1950, 5715);
        g.drawLine(150,3140, 1950, 3140);
        g.drawLine(250,3240, 250, 5715);
        g.drawLine(150,3240, 1950, 3240);

        g.drawLine(900,3140, 900, 5715);
        g.drawLine(1550,3140, 1550, 5715);
        g.drawLine(1300,3240, 1300, 5715);
        g.drawLine(640,3240, 640, 5715);
        g.drawLine(150, 5715, 1950, 5715);

        g.drawImage(imgScala2,175,3290,30,2375,this);

        g.setFont(new Font("Serif",0,25));

        double passo = 1900/16.0;
        for(int i=0; i<=20; i++){
            int y=(int)(passo*i+3300);
            if(i>3){
                g.drawString(String.valueOf((i*5)+80), 200, y);
            }else{
                g.drawString(String.valueOf((i*5)+80), 210, y);
            }
        }
    }

    private void writeDatiTecnici(){
        String s ="<html><p style=\"line-height:0.6;font-size:30pt;letter-spacing:-2px\">" +
                "<b>Sistema di perforazione:</b><br>" + intestazione.getSistemaPerforazione()+"<br>"+
                "<b>Diametro di perforazione:</b><br>"+intestazione.getDiametroPerforazione()+"mm<br>"+
                "<b>Profonditaà di perforazione:</b><br>"+intestazione.getProfonditaPeroforazione()+"m<br>"+
                "<b>Profondità pozzo:</b><br>"+intestazione.getProfonditaPozzo()+"m<br>"+
                "<b>Colonna di rivestimento:</b><br>"+intestazione.getColonnaRivesetimento()+"<br>"+
                "<b>Diametro colonna di rivestimento:</b><br>"+intestazione.getDiametroRivestimento()+"mm<br>"+
                getFiltriString()+
                "<b>Livello statico:</b><br>";
        double lvStat = intestazione.getLivelloStatico();
        if(lvStat>0)
            s+="+"+intestazione.getLivelloStatico()+"m<br>";
        else
            s+=intestazione.getLivelloStatico()+"m<br>";
        if(removeSpace(intestazione.getLivelloDinamico())!=""){
            s=s+"<b>Livello dinamico:</b><br>-"+intestazione.getLivelloDinamico()+"m<br>";
        }
        if(removeSpace(intestazione.getPortataDinamico())!=""){
            s=s+"con portata di (l/min) "+intestazione.getPortataDinamico()+"<br>";
        }
        if(removeSpace(intestazione.getProfonditaRestrizione())!=""){
            s=s+"<b>Restrizione a:</b><br>"+intestazione.getProfonditaRestrizione()+"m<br<";
        }
        if(removeSpace(intestazione.getInfoAggiuntive())!=""){
            s=s+"<b>Informazioni aggiuntive:</b><br>"+intestazione.getInfoAggiuntive();
        }
        s=s+"</p></html>";


        datiTecnici.setText(s);
    }

    private String getFiltriString(){
        if(arrayFiltro.size()==0)
            return "";
        String s = "<b>Filtri:</b><br>";
        for(int i=0; i<arrayFiltro.size(); i++){
            Filtro f = arrayFiltro.get(i);
            if(i!=arrayFiltro.size()-1){
                if(f.type==arrayFiltro.get(i+1).type && (f.minizio+f.mprofondita)==arrayFiltro.get(i+1).minizio){
                    s=s+"-Filtro "+f.getType()+" da "+f.minizio+" a "+(arrayFiltro.get(i+1).minizio+arrayFiltro.get(i+1).mprofondita)+"<br>";
                    i++;
                    continue;
                }
            }
            s=s+"Filtro "+f.getType()+" da "+f.minizio+" a "+(f.minizio+f.mprofondita)+"<br>";
        }
        return s;
    }

    private String removeSpace(String s){
        boolean b=true;
        while(b){
            if(s.length()>0){
                if(s.charAt(0)==' '){
                    s = s.substring(1);
                }else{
                    b=false;
                }
            }else{
                s="";
                b=false;
            }
        }
        return s;
    }

    private void writeLabel(int profondita){
        datiTecnici = new JLabel();
        datiTecnici.setBounds(1570,800,360, 1900);
        datiTecnici.setFont(new Font("Serif", 0, 18));
        datiTecnici.setVerticalAlignment(SwingConstants.TOP);
        this.add(datiTecnici);

        Font font = new Font("Serif", Font.BOLD, 45);

        JLabel l = new JLabel("Litostratigrafia");
        l.setBounds(150, 3140, 700, 100);
            arrayLabel.add(l);
        l = new JLabel("Schema pozzo");
        l.setBounds(900, 3140, 650, 100);
            arrayLabel.add(l);
        l = new JLabel("Dati tecnici");
        l.setBounds(1550, 3140, 400, 100);
            arrayLabel.add(l);

        if(fogli==2){
            l = new JLabel("Litostratigrafia");
                l.setBounds(150, 650, 700, 100);
                arrayLabel.add(l);
            l = new JLabel("Schema pozzo");
            l.setBounds(900, 650, 650, 100);
                arrayLabel.add(l);
            l = new JLabel("Dati tecnici");
            l.setBounds(1550, 650, 400, 100);
                arrayLabel.add(l);
        }

        for(JLabel la: arrayLabel){
            la.setHorizontalAlignment(SwingConstants.CENTER);
            la.setVerticalAlignment(SwingConstants.CENTER);
            la.setFont(font);
            this.add(la);
        }


    }

    public void addTerreno(HashMap hm){

        System.out.println("entrato in addTerreno");

        double minizio=(Double)hm.get("metriInizio");
        double mfine = (Double)hm.get("metriFine");
        int lim = 80;
        if(fogli==1){
            lim = 90;
        }
        if(minizio<=lim && mfine<=lim){
            double mprofondita=mfine-minizio;
            int inizio = (int)(minizio*rappProf);
            double fine =(mfine*rappProf);
            int profondita=(int)(fine-inizio);
            inizio+=800;

            int type=(Integer)hm.get("type");
            String infoAgg = (String)hm.get("infoAgg");

            Terreno t = new Terreno(inizio, profondita, minizio, mprofondita, infoAgg, type, scala*zoom);
            arrayTerreno.add(t);
            this.add(t.getLabel());
        }else if(minizio>=lim && mfine>=lim){
            double mprofondita=(mfine)-minizio;
            int inizio = (int)(minizio*rappProf);
            double fine =(mfine*rappProf);
            int profondita=(int)(fine-inizio);
            inizio+=590+800;

            int type=(Integer)hm.get("type");
            String infoAgg = (String)hm.get("infoAgg");

            Terreno t = new Terreno(inizio, profondita, minizio, mprofondita, infoAgg, type, scala*zoom);
            arrayTerreno.add(t);
            this.add(t.getLabel());
        }else if(minizio<lim && mfine>lim ){
            double mprofondita=(mfine)-minizio;
            int inizio = (int)(minizio*rappProf);
            double fine1 =(lim*rappProf);
            int profondita1=(int)(fine1-inizio);
            inizio+=800;

            int minizio2=lim;
            int inizio2 = (int)(lim*rappProf);
            double fine2 = (mfine*rappProf);
            int profondita2 = (int)(fine2-inizio2);
            inizio2+=590+800;

            int type=(Integer)hm.get("type");
            String infoAgg = (String)hm.get("infoAgg");

            Terreno t = new Terreno(inizio, profondita1, minizio, lim-minizio, infoAgg, type, scala*zoom);
            Terreno t2 = new Terreno(inizio2, profondita2, minizio2, mfine-minizio2, infoAgg, type, scala*zoom);

            arrayTerreno.add(t);
            arrayTerreno.add(t2);
            this.add(t.getLabel());
            this.add(t2.getLabel());

        }
        MyFrame m = (MyFrame)MyHasMap.listElement.get("myFrame");
        m.myRepaint();
        m.myRepaint();
    }

    public void removeTerreno(){
        arrayTerreno.remove(terr);
        this.remove(terr.getLabel());
        this.repaint();
    }

    public void modificaTerreno(){
        NewTerreno t = new NewTerreno((JFrame)MyHasMap.listElement.get("myFrame"),true);
        t.modifica(terr);
        t.setVisible(true);
        if(t.getSucc()){
            removeTerreno();
            HashMap hm =t.getHm();
            addTerreno(hm);
        }
    }

    public void addRiempimento(HashMap hm){

        double minizio=(Double)hm.get("metriInizio");
        double mfine = (Double)hm.get("metriFine");
        int lim = 80;
        if(fogli==1){
            lim = 90;
        }

        if(minizio<=lim && mfine<=lim){
            double mprofondita=(mfine)-minizio;
            int inizio = (int)(minizio*rappProf);
            double fine =(mfine*rappProf);
            int profondita=(int)(fine-inizio);
            inizio+=800;

            int type=(Integer)hm.get("type");
            String infoAgg = (String)hm.get("infoAgg");

            Riempimento t = new Riempimento(inizio, profondita, minizio, mprofondita, infoAgg, type, scala*zoom);
            arrayRiempimento.add(t);
            this.add(t.getLabel());
        }else if(minizio>=lim && mfine>=lim){
            double mprofondita=(mfine)-minizio;
            int inizio = (int)(minizio*rappProf);
            double fine =(mfine*rappProf);
            int profondita=(int)(fine-inizio);
            inizio+=590+800;

            int type=(Integer)hm.get("type");
            String infoAgg = (String)hm.get("infoAgg");

            Riempimento t = new Riempimento(inizio, profondita, minizio, mprofondita, infoAgg, type, scala*zoom);
            arrayRiempimento.add(t);
            this.add(t.getLabel());
        }else if(minizio<lim && mfine>lim ){
            double mprofondita=(mfine)-minizio;
            int inizio = (int)(minizio*rappProf);
            double fine1 =(lim*rappProf);
            int profondita1=(int)(fine1-inizio);
            inizio+=800;

            int minizio2=lim;
            int inizio2 = (int)(lim*rappProf);
            double fine2 = (mfine*rappProf);
            int profondita2 = (int)(fine2-inizio2);
            inizio2+=590+800;

            int type=(Integer)hm.get("type");
            String infoAgg = (String)hm.get("infoAgg");

            Riempimento t = new Riempimento(inizio, profondita1, minizio, lim-minizio, infoAgg, type, scala*zoom);
            Riempimento t2 = new Riempimento(inizio2, profondita2, minizio2, mfine-minizio2, infoAgg, type, scala*zoom);

            arrayRiempimento.add(t);
            arrayRiempimento.add(t2);
            this.add(t.getLabel());
            this.add(t2.getLabel());

        }

        MyFrame m = (MyFrame)MyHasMap.listElement.get("myFrame");
        m.myRepaint();
        m.myRepaint();
    }

    private void removeRiempimento(){
        arrayRiempimento.remove(riem);
        this.remove(riem.getLabel());
        this.repaint();
    }

    private void modificaRiempimento(){
        NewRiempimento t = new NewRiempimento((JFrame)MyHasMap.listElement.get("myFrame"),true);
        t.modifica(riem);
        t.setVisible(true);
        if(t.getSucc()){
            removeRiempimento();
            HashMap hm =t.getHm();
            this.addRiempimento(hm);
        }
    }

    public void addFiltro(HashMap hm){

        double minizio=(Double)hm.get("metriInizio");
        double mfine = (Double)hm.get("metriFine");
        int lim = 80;
        if(fogli==1){
            lim = 90;
        }
        
        if(minizio<=lim && mfine<=lim){
            double mprofondita=(mfine)-minizio;
            int inizio = (int)(minizio*rappProf);
            double fine =(mfine*rappProf);
            int profondita=(int)(fine-inizio);
            inizio+=800;

            int type=(Integer)hm.get("type");
            String infoAgg = (String)hm.get("infoAgg");

            Filtro t = new Filtro(inizio, profondita, minizio, mprofondita, infoAgg, type, scala*zoom);
            arrayFiltro.add(t);
        }else if(minizio>=lim && mfine>=lim){
            double mprofondita=(mfine)-minizio;
            int inizio = (int)(minizio*rappProf);
            double fine =(mfine*rappProf);
            int profondita=(int)(fine-inizio);
            inizio+=590+800;

            int type=(Integer)hm.get("type");
            String infoAgg = (String)hm.get("infoAgg");

            Filtro t = new Filtro(inizio, profondita, minizio, mprofondita, infoAgg, type, scala*zoom);
            arrayFiltro.add(t);
        }else if(minizio<lim && mfine>lim ){
            double mprofondita=(mfine)-minizio;
            int inizio = (int)(minizio*rappProf);
            double fine1 =(lim*rappProf);
            int profondita1=(int)(fine1-inizio);
            inizio+=800;

            int minizio2=lim;
            int inizio2 = (int)(lim*rappProf);
            double fine2 = (mfine*rappProf);
            int profondita2 = (int)(fine2-inizio2);
            inizio2+=590+800;

            int type=(Integer)hm.get("type");
            String infoAgg = (String)hm.get("infoAgg");

            Filtro t = new Filtro(inizio, profondita1, minizio, lim-minizio, infoAgg, type, scala*zoom);
            Filtro t2 = new Filtro(inizio2, profondita2, minizio2, mfine-minizio2, infoAgg, type, scala*zoom);

            arrayFiltro.add(t);
            arrayFiltro.add(t2);

        }

        Collections.sort(arrayFiltro);
        writeDatiTecnici();
        MyFrame m = (MyFrame)MyHasMap.listElement.get("myFrame");
        m.myRepaint();
        m.myRepaint();
    }

    private void removeFiltro(){
        System.out.println("Asdfasdfasdfasdfasdfasdfasfdadfasdf");
        arrayFiltro.remove(fil);
        System.out.println(fil);
        this.repaint();
    }

    private void modificaFiltro(){
        System.out.println("Asdfasdfasdfasdfasdfasdfasfdadfasdf");
        NewFiltro t = new NewFiltro((JFrame)MyHasMap.listElement.get("myFrame"),true);
        t.modifica(fil);
        t.setVisible(true);
        if(t.getSucc()){
            removeFiltro();
            HashMap hm =t.getHm();
            this.addFiltro(hm);
        }
    }

    private void setIntestazione(){
        if(intestazione.getProfonditaPozzo()>90){
            fogli = 2;
            rappProf = 1900.0/80.0;
            ImageIcon i = new ImageIcon("immagini/scala8.png");
            imgScala = i.getImage();
        }else{
            fogli=1;
            if(intestazione.getProfonditaPozzo()>80){
                ImageIcon i = new ImageIcon("immagini/scala9.png");
                imgScala = i.getImage();
                rappProf= 1900.0/90.0;
            }else{
                rappProf = 1900.0/80.0;
                ImageIcon i = new ImageIcon("immagini/scala8.png");
                imgScala = i.getImage();
            }
        }
        ImageIcon icon = new ImageIcon();
        System.out.println(intestazione.getColonnaRivesetimento());
        if(intestazione.getColonnaRivesetimento().equalsIgnoreCase("pvc")){
            icon = new ImageIcon("immagini/Tubo.png");
        }
        if(intestazione.getColonnaRivesetimento().equalsIgnoreCase("acciaio al carbonio")){
            icon = new ImageIcon("immagini/Tubocarbonio.png");
        }
        if(intestazione.getColonnaRivesetimento().equalsIgnoreCase("acciaio inox")){
            icon = new ImageIcon("immagini/TuboAcciaio.png");
        }
        imgTubo = icon.getImage();
        imgScala2 = new ImageIcon("immagini/scala10.jpg").getImage();
        drawIntestazione();
    }

    public void modificaIntestazione(){
        MyDialog m = new MyDialog(parent.parent,true, "Modifica intestazione", 1);
        m.preset(intestazione);
        m.setVisible(true);
        if(m.getSucc()){
            intestazione = m.getIntestazione();
            setIntestazione();
            MyFrame mf = (MyFrame)MyHasMap.listElement.get("myFrame");
            mf.myRepaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        System.out.println(mouseEvent.getPoint());

        Terreno terr = null;
        Riempimento riem = null;
        Filtro fil = null;

        if(mouseEvent.getButton()==3){
            boolean contains=false;
            for(MyRectangle r: arrayRectTerreno){
                if(r.contains(mouseEvent.getPoint())){
                    aggiungiTerreno.setVisible(true);
                    modificaTerreno.setVisible(true);
                    rimuoviTerreno.setVisible(true);
                    contains = true;
                }else{
                    aggiungiTerreno.setVisible(false);
                    modificaTerreno.setVisible(false);
                    rimuoviTerreno.setVisible(false);
                }
            }

            for(MyRectangle r: arrayRectPozzo){
                if(r.contains(mouseEvent.getPoint())){
                    aggiungiParete.setVisible(true);
                    modificaParete.setVisible(true);
                    rimuoviParete.setVisible(true);
                    sep.setVisible(true);
                    aggiungiFiltro.setVisible(true);
                    modificaFiltro.setVisible(true);
                    rimuoviFiltro.setVisible(true);
                    contains = true;
                }else{
                    aggiungiParete.setVisible(false);
                    modificaParete.setVisible(false);
                    rimuoviParete.setVisible(false);
                    sep.setVisible(false);
                    aggiungiFiltro.setVisible(false);
                    modificaFiltro.setVisible(false);
                    rimuoviFiltro.setVisible(false);
                }
            }

            for(MyRectangle r: arrayRectIntestazione){
                if(r.contains(mouseEvent.getPoint())){
                    modificaIntestazione.setVisible(true);
                    contains = true;
                }else{
                    modificaIntestazione.setVisible(false);
                }
            }

            for(Terreno t: arrayTerreno){
                if(t.contains(mouseEvent.getPoint())){
                    modificaTerreno.setEnabled(true);
                    rimuoviTerreno.setEnabled(true);
                    contains = true;
                    terr=t;
                }
            }

            for(Riempimento t: arrayRiempimento){
                if(t.contains(mouseEvent.getPoint())){
                    modificaParete.setEnabled(true);
                    rimuoviParete.setEnabled(true);
                    contains = true;
                    riem=t;
                }
            }

            for(Filtro t: arrayFiltro){
                if(t.contains(mouseEvent.getPoint())){
                    modificaFiltro.setEnabled(true);
                    rimuoviFiltro.setEnabled(true);
                    contains = true;
                    fil=t;
                    System.out.println(fil);
                }
            }

            if(riem==null){
                modificaParete.setEnabled(false);
                rimuoviParete.setEnabled(false);
            }

            if(terr==null){
                modificaTerreno.setEnabled(false);
                rimuoviTerreno.setEnabled(false);
            }

            if(fil==null){
                modificaFiltro.setEnabled(false);
                rimuoviFiltro.setEnabled(false);
            }

            this.riem = riem;
            this.terr = terr;
            this.fil = fil;
            if (contains){
                jPopupMenu.show(this, mouseEvent.getX(), mouseEvent.getY());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource()==aggiungiTerreno){
            System.out.println("cliccato inserisci livello");
            NewTerreno nt = new NewTerreno(((JFrame)MyHasMap.listElement.get("myFrame")), true);
            nt.setVisible(true);
            if(nt.getSucc()){
                System.out.println("succes");
                HashMap hm = nt.getHm();
                addTerreno(hm);
            }else{
                System.out.println("no success");
            }
        }else if(actionEvent.getSource()==rimuoviTerreno){
            int d = JOptionPane.showConfirmDialog((JFrame)MyHasMap.listElement.get("myFrame"), "Sei sicuro di voler eliminare il livello?");
            if (d==0){
                removeTerreno();
            }
        }else if(actionEvent.getSource()==modificaTerreno){
            modificaTerreno();
        }else if(actionEvent.getSource()==aggiungiParete){
            NewRiempimento nt = new NewRiempimento(((JFrame)MyHasMap.listElement.get("myFrame")), true);
            nt.setVisible(true);
            if(nt.getSucc()){
                HashMap hm = nt.getHm();
                addRiempimento(hm);
            }
        }else if(actionEvent.getSource()==rimuoviParete){
            int d = JOptionPane.showConfirmDialog((JFrame)MyHasMap.listElement.get("myFrame"), "Sei sicuro di voler eliminare il livello?");
            if (d==0){
                removeRiempimento();
            }
        }else if(actionEvent.getSource()==modificaParete){
            modificaRiempimento();
        }else if(actionEvent.getSource()==aggiungiFiltro){
            NewFiltro nt = new NewFiltro(((JFrame)MyHasMap.listElement.get("myFrame")), true);
            nt.setVisible(true);
            if(nt.getSucc()){
                HashMap hm = nt.getHm();
                addFiltro(hm);
            }
        }else if(actionEvent.getSource()==rimuoviFiltro){
            int d = JOptionPane.showConfirmDialog((JFrame)MyHasMap.listElement.get("myFrame"), "Sei sicuro di voler eliminare questo filtro?");
            if (d==0){
                System.out.println(fil);
                removeFiltro();
            }
        }else if(actionEvent.getSource()==modificaFiltro){
            modificaFiltro();
        }else if(actionEvent.getSource()==modificaIntestazione){
            modificaIntestazione();
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setParent(MainPanel m){
        parent = m;
    }

    public Intestazione getIntestazione(){
        return intestazione;
    }

    public ArrayList<ArrayList> getStrati(){
        ArrayList<ArrayList> array = new  ArrayList<ArrayList>();
        array.add(arrayTerreno);
        array.add(arrayFiltro);
        array.add(arrayRiempimento);
        return array;
    }

    public void setStrati(ArrayList<ArrayList> array){
        arrayTerreno = (ArrayList<Terreno>)array.get(0);
        arrayFiltro = (ArrayList<Filtro>)array.get(1);
        arrayRiempimento = (ArrayList<Riempimento>)array.get(2);
    }

    public SaveClass getSave(){
       return new SaveClass(getIntestazione(), getStrati());
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int i) throws PrinterException {
        System.out.println("i = " + i);
        if(i<fogli){
            double height = pageFormat.getImageableHeight();
            double width = pageFormat.getImageableWidth();
            double scalax = width/2100.0;
            double scalay = height/2970.0;
            Graphics2D g2 = (Graphics2D)graphics;
            g2.scale(scalax, scalay);
            g2.translate(0, -2990*i);
            myPaint(graphics);
            return PAGE_EXISTS;
        }else{
            return NO_SUCH_PAGE;
        }
    }

    public ArrayList<Terreno> getTerreno(){
        return arrayTerreno;
    }

    public ArrayList<Riempimento> getRiempimento(){
        return arrayRiempimento;
    }

    public ArrayList<Filtro> getFiltro(){
        return arrayFiltro;
    }
}
