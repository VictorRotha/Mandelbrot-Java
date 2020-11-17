import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Mandelbrot {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    public static final int ZOOM = 2;

    private JFrame frame;
    private BufferedImage bi;

    private final double LIMITER = 2.0;
    private final int DEPTH = 100;

    private double centerReal, centerImag, rangeReal, rangeImag;



    public Mandelbrot() {

        centerReal = 0.5;
        centerImag = 0;
        rangeReal = 2.5;
        rangeImag = 2.5;

        bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        startMandel();

        frame = new JFrame("Mandelbrot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ViewerPanel(bi, this));
        frame.pack();
        frame.setVisible(true);

    }

    public void startMandel() {
        System.out.println("CENTER: " + centerReal + ", " + centerImag);
        System.out.println("RANGE REAL: " + rangeReal + ",  RANGE IMAG: " + rangeImag);
        int[] mandelArray = getMandelArray();
        int[] pixelArray = getPixelArray(mandelArray, 1);
        bi.setRGB(0,0,WIDTH, HEIGHT, pixelArray, 0,WIDTH);

    }

    private int[] getMandelArray() {
        int[] pixels = new int[WIDTH * HEIGHT];
        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            pixels[i] = getMandel(i);
        }
        return pixels;
    }

    private int getMandel(int _index) {

        int row = _index / WIDTH;
        int col = _index % WIDTH;

        double real = (rangeReal / WIDTH) * col + centerReal - rangeReal / 2;
        double imag = (rangeImag / HEIGHT) * row + centerImag - rangeImag / 2;

        Complex c = new Complex(real, imag);
        Complex z = new Complex(0, 0);

        int d = DEPTH;
        for (int i = 0; i < DEPTH; i++) {
            z.mult(z);
            z.sub(c);
            if (z.abs() > LIMITER) {
                d = i;
                break;
            }
        }
        return d;
    }

    public int[] getPixelArray(int[] _mandelArray, int type) {
        int d;
        int[] result = new int[_mandelArray.length];
        for (int i = 0; i < _mandelArray.length; i++) {
            d = _mandelArray[i];
            switch (type) {
                case 0:
                    result[i] = (d == DEPTH) ? Color.BLACK.getRGB() : Color.WHITE.getRGB();
                    break;
                case 1:
                    int color  = (int) (255 - (255.0 / DEPTH) * d);
                    result[i] = new Color(color, color, color).getRGB();
                    break;
            }
          }
        return result;
    }

    public double getCenterReal() {
        return centerReal;
    }

    public void setCenterReal(double centerReal) {
        this.centerReal = centerReal;
    }

    public double getCenterImag() {
        return centerImag;
    }

    public void setCenterImag(double centerImag) {
        this.centerImag = centerImag;
    }

    public double getRangeReal() {
        return rangeReal;
    }

    public void setRangeReal(double rangeReal) {
        this.rangeReal = rangeReal;
    }

    public double getRangeImag() {
        return rangeImag;
    }

    public void setRangeImag(double rangeImag) {
        this.rangeImag = rangeImag;
    }




    public static void main(String[] args) {
        new Mandelbrot();
    }



}
