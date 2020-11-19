import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ViewerPanel extends JPanel implements MouseListener{

    private Mandelbrot mandelbrot;

    public ViewerPanel(Mandelbrot _mandelbrot) {
        mandelbrot = _mandelbrot;
        setPreferredSize(new Dimension(Mandelbrot.WIDTH, Mandelbrot.HEIGHT));
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(mandelbrot.getBi(), 0,0,null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        double real = (mandelbrot.getRangeReal() / Mandelbrot.WIDTH) * e.getX()
                + mandelbrot.getCenterReal() - mandelbrot.getRangeReal() / 2;
        double imag = (mandelbrot.getRangeImag() / Mandelbrot.HEIGHT) * e.getY()
                + mandelbrot.getCenterImag() - mandelbrot.getRangeImag() / 2;

        mandelbrot.setCenterReal(real);
        mandelbrot.setCenterImag(imag);
        if (e.getButton() == MouseEvent.BUTTON1) {
            mandelbrot.setRangeReal(mandelbrot.getRangeReal() / Mandelbrot.ZOOM);
            mandelbrot.setRangeImag(mandelbrot.getRangeImag() / Mandelbrot.ZOOM);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            mandelbrot.setRangeReal(mandelbrot.getRangeReal() * Mandelbrot.ZOOM);
            mandelbrot.setRangeImag(mandelbrot.getRangeImag() * Mandelbrot.ZOOM);
        }

        mandelbrot.startMandel();
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
