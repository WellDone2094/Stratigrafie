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
public class Terreno extends JPanel{
    int inizio, profondita, type;
    double minizio,mprofondita;
    MyRectangle rect;
    JLabel label;
    Font font = new Font("Serif", Font.BOLD, 30);
    Image img;
    BufferedImage bimg;
    String infoAgg;

    public Terreno(int inizio, int profondita, double minizio, double mprofondita, String infoAgg, int type, double scala ){
        this.inizio = inizio;
        this.profondita = profondita;
        this.minizio = minizio;
        this.mprofondita = mprofondita;
        this.type = type;
        rect = new MyRectangle(new Rectangle(250, inizio, 650, profondita), scala);
        this.infoAgg = infoAgg;

        Element e = getFiltroType(type);

        label = new JLabel(" ",SwingConstants.CENTER);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBounds(250, inizio, 400, profondita);
        label.setFont(font);
        label.setText("<html><center>"+this.infoAgg.replaceAll("\n","<br>") +"</center></html>");
        System.out.println(label.getText());

        ImageIcon icon = new ImageIcon(e.getAttribute("img"));
        img = icon.getImage();
        img = createImage(new FilteredImageSource(img.getSource(),
                new CropImageFilter(0, 0, 259, profondita)));

    }

    public void setScala(double scala){
        rect.setScala(scala);
    }

    public boolean contains(Point p){
        if (rect.contains(p)) return true;
        else return false;
    }

    public JLabel getLabel(){
        return label;
    }

    public void draw(Graphics g){
        DrawPanel d = (DrawPanel)MyHasMap.listElement.get("drawPanel");
        g.drawImage(img, 642, inizio, d);
        g.setFont(new Font("Serif",0, 30));
        g.drawLine(650,inizio,900,inizio);
        g.drawLine(650,inizio+profondita,900,inizio+profondita);

        g.drawLine(250,inizio,550,inizio);
        g.drawString(String.valueOf(minizio),575,inizio+7);
        g.drawLine(250,inizio+profondita,550,inizio+profondita);
        g.drawString(String.valueOf(minizio+mprofondita), 575, inizio+profondita+7);
    }

    public void reset(int inizio, int profondita, int minizio, int mprofondita, String infoAgg, int type, double scala, DrawPanel drawPanel){
        this.inizio = inizio;
        this.profondita = profondita;
        this.minizio = minizio;
        this.mprofondita = mprofondita;
        this.type = type;
        rect = new MyRectangle(new Rectangle(250, inizio, 650, profondita), scala);
        this.infoAgg = infoAgg;

        Element e = getFiltroType(type);

        ImageIcon icon = new ImageIcon(e.getAttribute("img"));
        img = icon.getImage();
        img = createImage(new FilteredImageSource(img.getSource(),
                new CropImageFilter(0, 0, 259, profondita)));

        label.setBounds(250, inizio, 400, profondita);
        label.setText("<html><center>"+this.infoAgg.replaceAll("\n","<br>")+"</center></html>");
        System.out.println(label.getText());
    }

    public String getInfoAgg(){
        return infoAgg;
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
        NodeList list = doc.getElementsByTagName("terreno");
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

}
