import java.awt.*;
import java.awt.image.BufferedImage;

public class Mandelbrot {

    private final int WIDTH = 600;
    private final int HEIGHT = 600;

    private final double LIMITER = 2.0;
    private final int DEPTH = 100;

    private double centerReal, centerImag, rangeReal, rangeImag;


    public Mandelbrot() {

        centerReal = 0.5;
        centerImag = 0;
        rangeReal = 2.5;
        rangeImag = 2.5;

        int[] mandelArray = getMandelArray();
        int[] pixelArray = getPixelArray(mandelArray, 1);

        BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        bi.setRGB(0,0,WIDTH, HEIGHT, pixelArray, 0,WIDTH);

        new Viewer(bi);
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



    public static void main(String[] args) {
        new Mandelbrot();
    }

}
