import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class Viewer extends JFrame {

    private final Mandelbrot MB;

    public Viewer(Mandelbrot _mandelbrot) {

        MB = _mandelbrot;
        setTitle("Mandelbrot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new ViewerPanel(_mandelbrot));

        JMenuBar menuBar = createMainMenu();
        setJMenuBar(menuBar);

        pack();
        setVisible(true);

    }

    private JMenuBar createMainMenu() {
       JMenuBar mnuBar = new JMenuBar();

       JMenu mnuFile = new JMenu("File");
       JMenuItem itmSave = new JMenuItem("Save Image");
       JCheckBoxMenuItem itmChkInfo = new JCheckBoxMenuItem("Save Info");
       itmChkInfo.setState(true);

       itmSave.addActionListener(e -> saveToFile(itmChkInfo.getState()));

       mnuFile.add(itmSave);
       mnuFile.add(itmChkInfo);
       mnuBar.add(mnuFile);

       return mnuBar;
    }

    private void saveToFile(boolean _saveInfo) {

        //TODO Remember FilePath

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Pictures *.png", "png"));
        int chooserResult = fileChooser.showSaveDialog(null);

        if (chooserResult == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();
            String filename = file.getPath();

            if (fileChooser.getFileFilter().getDescription().endsWith(".png")) {
                if (!filename.endsWith(".png") && !filename.endsWith(".PNG")) {
                    file = new File(filename + ".png");
                }
            }
            try {
                ImageIO.write(MB.getBi(), "png", file);
                if  (_saveInfo) {
                    String fileInfoPath = file.getAbsolutePath() + ".info";
                    FileOutputStream fos = new FileOutputStream(fileInfoPath);
                    OutputStreamWriter osw = new OutputStreamWriter(fos);
                    osw.write(file.getAbsolutePath() + "\n");
                    osw.write(Mandelbrot.WIDTH + " x " + Mandelbrot.HEIGHT + "\n");
                    osw.write("REAL Range " + MB.getRangeReal() + ", Center: " + MB.getCenterReal() + "\n");
                    osw.write("IMAG Range " + MB.getRangeImag() + ", Center: " + MB.getCenterImag() + "\n");
                    osw.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

}
