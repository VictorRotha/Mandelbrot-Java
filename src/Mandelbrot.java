import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Mandelbrot {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final int ZOOM = 2;

    public static final double LIMITER = 2.0;
    public static final int DEPTH = 600;

    public static final String[] COLOR_FILTERS =
            {"Black&White", "Grayscale", "RGB One", "RGB Two", "RGB Red", "HSB One", "HSB Two", "HSB Log", "HSB sin"};

    private String filter;
    private double dur;
    private BufferedImage bi;

    private double centerReal, centerImag, rangeReal, rangeImag;
    private int[] mandelArray;


    public Mandelbrot() {
        centerReal = 0.5;
        centerImag = 0;
        rangeReal = 2.5;
        rangeImag = 2.5;

        filter = COLOR_FILTERS[4];

        bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        startMandel();
        new Viewer(this);

    }

    public void startMandel() {
        System.out.println("CENTER: " + centerReal + ", " + centerImag);
        System.out.println("RANGE REAL: " + rangeReal + ",  RANGE IMAG: " + rangeImag);
        System.out.println("WIDTH: " + WIDTH + " HEIGHT: " + HEIGHT + " DEPTH: " + DEPTH + " LIMIT: " + LIMITER);
        mandelArray = calcMandelArray();
        int[] pixelArray = calcPixelArray(mandelArray);
        bi.setRGB(0,0,WIDTH, HEIGHT, pixelArray, 0,WIDTH);

    }

    private int[] calcMandelArray() {
        long start = System.nanoTime();
        int m;
        int[] pixels = new int[WIDTH * HEIGHT];
        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            m = calcMandel(i);
            pixels[i] = m;
        }
        long end = System.nanoTime();
        dur = (end - start) / 1_000_000_000.0;
        System.out.println(dur + " Sec.");

        return pixels;
    }

    private int calcMandel(int _index) {

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

    public int[] calcPixelArray(int[] _mandelArray) {
        int d;
        int r, g, b;
        float hue;
        int[] result = new int[_mandelArray.length];
        for (int i = 0; i < _mandelArray.length; i++) {
            d = _mandelArray[i];
            switch (Arrays.asList(COLOR_FILTERS).indexOf(filter)) {
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
                    //HSB 1
                    hue = 1.0f * d / DEPTH;
                    result[i] = Color.getHSBColor(hue, 0.9f, 0.9f).getRGB();
                    break;
                case 6:
                    //HSB 2
                    hue = 0.05f + 0.4f * d / DEPTH;
                    result[i] = Color.getHSBColor(hue, 0.9f, 0.9f).getRGB();
                    break;
                case 7:
                    //HSB log
                    hue = 0.4f + 0.4f * (float) (Math.log(d)/Math.log(DEPTH));
                    result[i] = Color.getHSBColor(hue, 0.9f, 0.9f).getRGB();
                    break;
                case 8:
                    //HSB sin
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

    public BufferedImage getBi() {
        return bi;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int[] getMandelArray() {
        return mandelArray;
    }

    public double getDur() {
        return dur;
    }

    public static void main(String[] args) {
        new Mandelbrot();
    }



}
