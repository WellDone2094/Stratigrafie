package shiller.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: benfa
 * Date: 06/04/12
 * Time: 10.13
 * To change this template use File | Settings | File Templates.
 */
public class MyMenuBar extends JMenuBar implements  ActionListener{

    private final MyFrame parent;

    private JMenu file;
        private JMenuItem nuovo;
        private JMenuItem carica;
        private JMenuItem salva;
        private JMenuItem salvaConNome;
        private JMenuItem stampa;
        private JMenuItem esci;

    private JMenu zoom;
        private ButtonGroup zoomGroup;
            private JRadioButtonMenuItem z25;
            private JRadioButtonMenuItem z50;
            private JRadioButtonMenuItem z75;
            private JRadioButtonMenuItem z100;
            private JRadioButtonMenuItem z125;
            private JRadioButtonMenuItem z150;
            private JRadioButtonMenuItem z175;
            private JRadioButtonMenuItem z200;

    public MyMenuBar(final MyFrame parent){
        super();

        this.parent=parent;
        
        file = new JMenu("File");
            nuovo = new JMenuItem("Crea Nuovo");
            carica = new JMenuItem("Apri ...");
            salva = new JMenuItem("Salva");
            salvaConNome = new JMenuItem("Salva con nome");
            stampa = new JMenuItem("Stampa");
            esci = new JMenuItem("Esci");

        file.add(nuovo); file.add(carica); file.add(salva); file.add(salvaConNome); file.add(stampa); file.addSeparator(); file.add(esci);
        this.add(file);

        zoom = new JMenu("Zoom");
            zoomGroup = new ButtonGroup();
                z25 = new JRadioButtonMenuItem("25%");
                    z25.addActionListener(this);
                z50 = new JRadioButtonMenuItem("50%");
                    z50.addActionListener(this);
                z75 = new JRadioButtonMenuItem("75%");
                    z75.addActionListener(this);
                z100 = new JRadioButtonMenuItem("100%");
                    z100.setSelected(true);
                    z100.addActionListener(this);
                z125 = new JRadioButtonMenuItem("125%");
                    z125.addActionListener(this);
                z150 = new JRadioButtonMenuItem("150%");
                    z150.addActionListener(this);
                z175 = new JRadioButtonMenuItem("175%");
                    z175.addActionListener(this);
                z200 = new JRadioButtonMenuItem("200%");
                    z200.addActionListener(this);
            zoomGroup.add(z25); zoomGroup.add(z50); zoomGroup.add(z75); zoomGroup.add(z100); zoomGroup.add(z125); zoomGroup.add(z150); zoomGroup.add(z175); zoomGroup.add(z200);
        zoom.add(z25); zoom.add(z50); zoom.add(z75); zoom.add(z100); zoom.add(z125); zoom.add(z150); zoom.add(z175); zoom.add(z200);
        this.add(zoom);
        
        
        salva.setEnabled(false);
        salva.setVisible(false);
        salvaConNome.setEnabled(false);
        stampa.setEnabled(false);
        zoom.setEnabled(false);





        //imposto le azioni dei menu
        nuovo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MainPanel m = (MainPanel)MyHasMap.listElement.get("mainPanel");
                m.nuovo();
                abilitaVoci();
            }
        });

        carica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MainPanel m = (MainPanel)MyHasMap.listElement.get("mainPanel");
                m.carica();
                abilitaVoci();
            }
        });

        salva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        salvaConNome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MainPanel m = (MainPanel)MyHasMap.listElement.get("mainPanel");
                m.salvaConNome();
            }
        });

        stampa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MainPanel m = (MainPanel)MyHasMap.listElement.get("mainPanel");
                m.stampa();
            }
        });

        esci.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                parent.dispose();
            }
        });
    }


    public void actionPerformed(ActionEvent ae){
        int zoom=0;

        if (ae.getSource() == z25)
            zoom=25;
        if (ae.getSource() == z50)
            zoom=50;
        if (ae.getSource() == z75)
            zoom=75;
        if (ae.getSource() == z100)
            zoom=100;
        if (ae.getSource() == z125)
            zoom=125;
        if (ae.getSource() == z150)
            zoom=150;
        if (ae.getSource() == z175)
            zoom=175;
        if (ae.getSource() == z200)
            zoom=200;

            if(zoom!=0){
                MainPanel m = (MainPanel)MyHasMap.listElement.get("mainPanel");
                m.setZoom(zoom);
                MyFrame f = (MyFrame)MyHasMap.listElement.get("myFrame");
            }
    }
    
    public void paint(Graphics g){
        super.paint(g);
    }

    public void abilitaVoci(){
        salva.setEnabled(false);
        salvaConNome.setEnabled(true);
        stampa.setEnabled(true);
        zoom.setEnabled(true);
    }

    public void setZoom(int z){
        z25.setSelected(false);
        z50.setSelected(false);
        z75.setSelected(false);
        z100.setSelected(false);
        z125.setSelected(false);
        z150.setSelected(false);
        z175.setSelected(false);
        z200.setSelected(false);


        if (z==25)
            z25.setSelected(true);
        if (z==50)
            z50.setSelected(true);
        if (z==75)
            z75.setSelected(true);
        if (z==100)
            z100.setSelected(true);
        if (z==125)
            z125.setSelected(true);
        if (z==150)
            z150.setSelected(true);
        if (z==175)
            z175.setSelected(true);
        if (z==200)
            z200.setSelected(true);

    }
}
