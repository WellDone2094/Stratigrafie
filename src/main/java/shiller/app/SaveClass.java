package shiller.app;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: mirkolonardi
 * Date: 29/06/12
 * Time: 13.24
 * To change this template use File | Settings | File Templates.
 */
public class SaveClass {
    private ArrayList<ArrayList> array;
    private Intestazione i;
    public SaveClass(Intestazione i, ArrayList<ArrayList> array){
        this.i = i;
        this.array = array;
    }

    public ArrayList<ArrayList> getArray(){
        return array;
    }

    public Intestazione getIntestazione(){
        return i;
    }
}
