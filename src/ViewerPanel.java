import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ViewerPanel extends JPanel implements MouseListener{

    private final Mandelbrot MB;

    public ViewerPanel(Mandelbrot _mandelbrot) {
        MB = _mandelbrot;
        setPreferredSize(new Dimension(MB.getWidth(), MB.getHeight()));
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(MB.getBi(), 0,0,null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        double real = (MB.getRangeReal() / MB.getWidth()) * e.getX()
                + MB.getCenterReal() - MB.getRangeReal() / 2;
        double imag = (MB.getRangeImag() / MB.getHeight()) * e.getY()
                + MB.getCenterImag() - MB.getRangeImag() / 2;

        MB.setCenterReal(real);
        MB.setCenterImag(imag);
        if (e.getButton() == MouseEvent.BUTTON1) {
            MB.setRangeReal(MB.getRangeReal() / MB.getZoom());
            MB.setRangeImag(MB.getRangeImag() / MB.getZoom());
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            MB.setRangeReal(MB.getRangeReal() * MB.getZoom());
            MB.setRangeImag(MB.getRangeImag() * MB.getZoom());
        }

        MB.startMandel();
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
