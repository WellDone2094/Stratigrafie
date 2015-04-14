package shiller.app;

import javax.swing.*;
import java.awt.*;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

/**
 * Created with IntelliJ IDEA.
 * User: ShilleR
 * Date: 30.09.13
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */
public class ImgComboBox  extends  JLabel{

    String[] imgList;

    public ImgComboBox(String[] imgList){
        this.imgList = imgList;
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        int selectedIndex = ((Integer)value).intValue();

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        //Set the icon and text.  If icon was null, say so.
        ImageIcon icon = null;
        icon = new ImageIcon(imgList[index]);

        Image img = icon.getImage();
        img = createImage(new FilteredImageSource(img.getSource(),
                new CropImageFilter(0, 0, 270, 100 )));
        img = img.getScaledInstance(100,50,0);
        ImageIcon i = new ImageIcon(img);
        setIcon(i);
        //setText(pet);
        //setFont(list.getFont());


        return this;
    }

}
