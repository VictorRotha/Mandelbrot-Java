import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Mandelbrot {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final int ZOOM = 2;

    public final String[] COLOR_FILTERS = {
            "Black&White", "Grayscale", "RGB One", "RGB Two", "RGB Red",
            "HSB One", "HSB Two", "HSB Log", "HSB sin", "HSB Log1p", "HSB 3"};

    private String filter;
    public int depth;
    public double limiter;
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
        depth = 600;
        limiter = 2.0;

        bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        startMandel();
        new Viewer(this);

    }

    public void startMandel() {
        System.out.println("\nCalculate New Image:");
        System.out.println(mandelInfo());

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
        System.out.println("Duration: " + dur + " Sec.");

        return pixels;
    }

    private int calcMandel(int _index) {

        int row = _index / WIDTH;
        int col = _index % WIDTH;

        double real = (rangeReal / WIDTH) * col + centerReal - rangeReal / 2;
        double imag = (rangeImag / HEIGHT) * row + centerImag - rangeImag / 2;

        Complex c = new Complex(real, imag);
        Complex z = new Complex(0, 0);

        int d = depth;
        for (int i = 0; i < depth; i++) {
            z.mult(z);
            z.sub(c);
            if (z.abs() > limiter) {
                d = i;
                break;
            }
        }
        return d;
    }

    public int[] calcPixelArray(int[] _mandelArray) {
        //TODO filter options log, ln, sin, recursive, ...
        int d;
        int r, g, b;
        float hue;
        int[] result = new int[_mandelArray.length];
        for (int i = 0; i < _mandelArray.length; i++) {
            d = _mandelArray[i];
            switch (Arrays.asList(COLOR_FILTERS).indexOf(filter)) {
                case 0:
                    //BLACK & WHITE
                    result[i] = (d == depth) ? Color.BLACK.getRGB() : Color.WHITE.getRGB();
                    break;
                case 1:
                    //GRAYSCALE
                    float brightness = 0.8f - (0.8f * d/ depth);
                    result[i] = Color.getHSBColor(0.0f, 0.0f, brightness).getRGB();
                    break;
                case 2:
                    //RGB 1
                    r = (int) ((255.0 / depth) * d);
                    g = (int) (255 - (255.0 / depth) * d);
                    b = (int) ((255.0 / depth) * d);
                    result[i] = new Color(r, g, b).getRGB();
                    break;
                case 3:
                    //RGB 2
                    if (d < 2 * depth / 3) {
                        r = 0;
                    } else {
                        r = (int) ((255.0 / (depth / 3.0)) * d/3.0);
                    }
                    if (d < depth / 3) {
                        g = 0;
                    } else {
                        g = (int) ((255.0 / (depth / 3.0)) * d/3.0);
                    }

                    b = 255 - (int) ((255.0 / depth) * d);
                    result[i] = new Color(r, g, b).getRGB();
                    break;
                case 4:
                    //RGB Red
                    r = (int) ((255.0 / depth) * d);
                    g = 0;
                    b = 0;
                    result[i] = new Color(r, g, b).getRGB();
                    break;
                case 5:
                    //HSB 1
                    hue = 1.0f * d / depth;
                    result[i] = Color.getHSBColor(hue, 0.9f, 0.9f).getRGB();
                    break;
                case 6:
                    //HSB 2
                    hue = 0.05f + 0.4f * d / depth;
                    result[i] = Color.getHSBColor(hue, 0.9f, 0.9f).getRGB();
                    break;
                case 7:
                    //HSB log
                    hue = 0.4f + 0.4f * (float) (Math.log(d)/Math.log(depth));
                    result[i] = Color.getHSBColor(hue, 0.9f, 0.9f).getRGB();
                    break;
                case 8:
                    //HSB sin
                    hue = 0.4f + 0.4f * (float) (Math.sin(d)/Math.sin(depth));
                    result[i] = Color.getHSBColor(hue, 0.9f, 0.9f).getRGB();
                    break;
                case 9:
                    //HSB log10
                    hue = 0.4f + 0.4f * (float) (Math.log1p(d)/Math.log1p(depth));
                    result[i] = Color.getHSBColor(hue, 0.9f, 0.9f).getRGB();
                    break;
                case 10:
                    //HSB 3
                    hue = 0.16f * d / depth;
                    brightness = 0.3f + 0.7f * d/depth;
                    result[i] = Color.getHSBColor(hue, 0.9f, brightness).getRGB();
                    break;

            }
          }
        return result;
    }

    public String mandelInfo() {

        String formatString =
                "Image (w x h): %s x %s px \n" +
                "Recursion Depth: %s, Limit %s\n" +
                "REAL Range: %s, Center %s\n" +
                "IMAG Range: %s, Center %s\n" +
                "Color Filter %s\n";

        return String.format(formatString, Mandelbrot.WIDTH, Mandelbrot.HEIGHT, depth, limiter, rangeReal, centerReal,
                rangeImag, centerImag, filter);

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

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public double getLimiter() {
        return limiter;
    }

    public void setLimiter(double limiter) {
        this.limiter = limiter;
    }

    public static void main(String[] args) {
        new Mandelbrot();
    }



}
