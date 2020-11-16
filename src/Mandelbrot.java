import java.awt.*;
import java.awt.image.BufferedImage;

public class Mandelbrot {

    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final double START_REAL = 0.75;
    private final double START_IMAG = 0.125;
    private final double RANGE_REAL = 0.125;
    private final double RANGE_IMAG = 0.125;

    private final double LIMITER = 2.0;
    private final int DEPTH = 100;


    public Mandelbrot() {


        int[] pixelArray = getPixelArray();

        BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        bi.setRGB(0,0,WIDTH, HEIGHT, pixelArray, 0,WIDTH);

        new Viewer(bi);

    }

    private int[] getPixelArray() {
        int[] pixels = new int[WIDTH * HEIGHT];

        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            pixels[i] = getMandel(i);
        }
        return pixels;

    }


    private int getMandel(int _index) {

        int result;

        int row = _index / WIDTH;
        int col = _index % WIDTH;

        double real = (RANGE_REAL / WIDTH) * col + START_REAL;
        double imag = (RANGE_IMAG / HEIGHT) * row + START_IMAG;

        Complex c = new Complex(real, imag);
        Complex z = new Complex(0,0);

        int d = DEPTH;
        for (int i = 0; i < DEPTH; i ++) {
            z.mult(z);
            z.sub(c);
            if (z.abs() > LIMITER) {
                d = i;
                break;
            }
        }

//        if (d == DEPTH) {
//            result = Color.BLACK.getRGB();
//        } else {
//            result = Color.WHITE.getRGB();
//        }

        int color  = (int) (255 - (255.0 / DEPTH) * d);
        result = new Color(color, color, color).getRGB();

        return result;
    }

    public static void main(String[] args) {
        new Mandelbrot();
    }

}
