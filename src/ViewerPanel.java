import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ViewerPanel extends JPanel {

    private BufferedImage bi;

    public ViewerPanel(BufferedImage _bi) {
        bi = _bi;
        setPreferredSize(new Dimension(bi.getWidth(), bi.getHeight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(bi, 0,0,null);
    }
}
