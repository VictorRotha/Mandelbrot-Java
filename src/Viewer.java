import javax.swing.*;
import java.awt.image.BufferedImage;

public class Viewer {

    JFrame frame;

    public Viewer(BufferedImage _bi) {
        frame = new JFrame("Mandelbrot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ViewerPanel(_bi));
        frame.pack();
        frame.setVisible(true);



    }

}
