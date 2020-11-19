import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class Viewer extends JFrame implements ActionListener{

    private final Mandelbrot MB;
    private String filepath;

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

       JMenu mnuFilter = new JMenu("Filter");
       JRadioButtonMenuItem btn;
       ButtonGroup grp = new ButtonGroup();
       for (String name: Mandelbrot.COLOR_FILTERS) {
           btn = new JRadioButtonMenuItem(name);
           if (name.equals(MB.getFilter())) {
               btn.setSelected(true);
           }
           btn.addActionListener(this);
           grp.add(btn);
           mnuFilter.add(btn);

       }
       mnuBar.add(mnuFilter);

       return mnuBar;
    }

    private void saveToFile(boolean _saveInfo) {

        JFileChooser fileChooser = new JFileChooser(filepath);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Pictures *.png", "png"));

        int chooserResult = fileChooser.showSaveDialog(null);

        if (chooserResult == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();
            String filename = file.getPath();
            filepath = file.getParent();

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
                    osw.write(Mandelbrot.WIDTH + " x " + Mandelbrot.HEIGHT + " px\n\n");
                    osw.write("Recursion Depth: " + Mandelbrot.DEPTH + "\nLimit: " + Mandelbrot.LIMITER + "\n");
                    osw.write("REAL Range " + MB.getRangeReal() + ", Center: " + MB.getCenterReal() + "\n");
                    osw.write("IMAG Range " + MB.getRangeImag() + ", Center: " + MB.getCenterImag() + "\n\n");
                    osw.write("Duration: " + MB.getDur() + " Sec.\n");
                    osw.write("Color-Filter: " + MB.getFilter());
                    osw.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MB.setFilter(e.getActionCommand());
        MB.getBi().setRGB(0,0,Mandelbrot.WIDTH, Mandelbrot.HEIGHT, MB.calcPixelArray(MB.getMandelArray()), 0,Mandelbrot.WIDTH);
        repaint();
    }
}
