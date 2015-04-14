package shiller.app;

/**
 * Created with IntelliJ IDEA.
 * User: benfa
 * Date: 28/05/12
 * Time: 8.21
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) {
        for(int i=0; i<args.length; i++){
            System.out.println("args = " + args[i]);
        }

        MyFrame frame = new MyFrame("Stratigrafie");
        frame.setVisible(true);
    }
}
