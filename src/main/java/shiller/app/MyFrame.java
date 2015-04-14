package shiller.app;



import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: benfa
 * Date: 06/04/12
 * Time: 10.22
 * To change this template use File | Settings | File Templates.
 */
public class MyFrame extends JFrame{

    MainPanel mainPanel;
    MyMenuBar menuBar;

    public MyFrame(String title){
        super(title);
        this.setBounds(0,0,500,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(100, 100));

        MyHasMap.listElement.put("myFrame",this);
        mainPanel = new MainPanel(this);
        MyHasMap.listElement.put("mainPanel", mainPanel);
        menuBar = new MyMenuBar(this);
        MyHasMap.listElement.put("menuBar", menuBar);

        this.setJMenuBar(menuBar);
        this.add(mainPanel);
    }

    public void myRepaint(){
        Rectangle r = this.getBounds();
        this.setBounds(r.x, r.y, 2, 2);
        this.setBounds(r.x, r.y, r.width, r.height);
        this.repaint();
    }
}
