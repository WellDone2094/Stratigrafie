package shiller.app;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: benfa
 * Date: 14/05/12
 * Time: 8.51
 * To change this template use File | Settings | File Templates.
 */

public class MyRectangle extends Rectangle {
    private Rectangle original, mod;
    private double  scala;

    public MyRectangle(Rectangle r, double scala){
        original = r;
        this.scala = scala;
        mod = new Rectangle((int)(r.x*scala), (int)(r.y*scala), (int)(r.width*scala), (int)(r.height*scala));
    }

    public void setScala(double scala){
        scala = scala;
        mod.setBounds((int)(original.x*scala), (int)(original.y*scala), (int)(original.width*scala), (int)(original.height*scala));
    }

    public boolean contains(Point p){
        if (mod.contains(p)) return true;
        else return false;
    }

    public void reset(Rectangle r, double scala){
        original = r;
        this.scala = scala;
        mod = new Rectangle((int)(r.x*scala), (int)(r.y*scala), (int)(r.width*scala), (int)(r.height*scala));
    }
}