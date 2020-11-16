import java.awt.*;
import java.awt.image.BufferedImage;

public class Mandelbrot {

    private final int WIDTH = 200;
    private final int HEIGHT = 200;

    public Mandelbrot() {


        int[] pixelArray = getPixelArray();

        BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        bi.setRGB(0,0,WIDTH, HEIGHT, pixelArray, 0,0);

        new Viewer(bi);


    }

    private int[] getPixelArray() {
        int[] pixels = new int[WIDTH * HEIGHT];

        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                int index = row * WIDTH + col;
                pixels[index] = new Color(col, col, col).getRGB();
            }
        }

        return pixels;

    }







    public static void main(String[] args) {
        new Mandelbrot();
    }

}
