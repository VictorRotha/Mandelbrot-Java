import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;

public class Mandelbrot {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final int ZOOM = 2;
    public static final int TYPE = 7;

    private JFrame frame;
    private BufferedImage bi;

    private final double LIMITER = 2.0;
    private final int DEPTH = 600;

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
        System.out.println("WIDTH: " + WIDTH + " HEIGHT: " + HEIGHT + " DEPTH: " + DEPTH + " LIMIT: " + LIMITER);
        long now = new Date().getTime();

        int[] mandelArray = getMandelArray();
        long t = new Date().getTime() - now;

        System.out.println("MandelArray: " + t/1000.0 + " sec");
        int[] pixelArray = getPixelArray(mandelArray);
        System.out.println("PixelArray:  " + (new Date().getTime() - now - t)/1000.0 + " sec");
        bi.setRGB(0,0,WIDTH, HEIGHT, pixelArray, 0,WIDTH);

    }

    private int[] getMandelArray() {
        int m;
        int[] pixels = new int[WIDTH * HEIGHT];
        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            m = getMandel(i);
            pixels[i] = m;
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

    public int[] getPixelArray(int[] _mandelArray) {
        int d;
        int r, g, b;
        float hue;
        int[] result = new int[_mandelArray.length];
        for (int i = 0; i < _mandelArray.length; i++) {
            d = _mandelArray[i];
            switch (TYPE) {
                case 0:
                    //BLACK & WHITE
                    result[i] = (d == DEPTH) ? Color.BLACK.getRGB() : Color.WHITE.getRGB();
                    break;
                case 1:
                    //GRAYSCALE
                    float brightness = 0.8f - (0.8f * d/DEPTH);
                    result[i] = Color.getHSBColor(0.0f, 0.0f, brightness).getRGB();
                    break;
                case 2:
                    //RGB 1
                    r = (int) ((255.0 / DEPTH) * d);
                    g = (int) (255 - (255.0 / DEPTH) * d);
                    b = (int) ((255.0 / DEPTH) * d);
                    result[i] = new Color(r, g, b).getRGB();
                    break;
                case 3:
                    //RGB 2
                    if (d < 2 * DEPTH / 3) {
                        r = 0;
                    } else {
                        r = (int) ((255.0 / (DEPTH / 3.0)) * d/3.0);
                    }
                    if (d < DEPTH / 3) {
                        g = 0;
                    } else {
                        g = (int) ((255.0 / (DEPTH / 3.0)) * d/3.0);
                    }

                    b = 255 - (int) ((255.0 / DEPTH) * d);
                    result[i] = new Color(r, g, b).getRGB();
                    break;
                case 4:
                    //RGB Red
                    r = (int) ((255.0 / DEPTH) * d);
                    g = 0;
                    b = 0;
                    result[i] = new Color(r, g, b).getRGB();
                    break;
                case 5:
                    hue = 1.0f * d / DEPTH;
                    result[i] = Color.getHSBColor(hue, 0.9f, 0.9f).getRGB();
                    break;
                case 6:
                    hue = 0.05f + 0.4f * d / DEPTH;
                    result[i] = Color.getHSBColor(hue, 0.9f, 0.9f).getRGB();
                    break;
                case 7:
                    hue = 0.4f + 0.4f * (float) (Math.log(d)/Math.log(DEPTH));
                    result[i] = Color.getHSBColor(hue, 0.9f, 0.9f).getRGB();
                    break;
                case 8:
                    hue = 0.4f + 0.4f * (float) (Math.sin(d)/Math.sin(DEPTH));
                    result[i] = Color.getHSBColor(hue, 0.9f, 0.9f).getRGB();
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
