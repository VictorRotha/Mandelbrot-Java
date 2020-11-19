import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;


public class Viewer extends JFrame implements ActionListener{

    private final Mandelbrot MB;
    private String filepath;
    private final String[] MNU_DEPTHS = {"Recursion Depth 600", "Recursion Depth 800", "Recursion Depth 1000", "Custom ..."};

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

       JMenu mnuDepth = new JMenu("Depth");

       JMenuItem item;
       for (String name: MNU_DEPTHS) {
           item = new JMenuItem(name);
           item.addActionListener(this);
           mnuDepth.add(item);
       }

       mnuBar.add(mnuDepth);

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
                System.out.println("\nSave Image: " + file.getPath());
                if  (_saveInfo) {
                    String fileInfoPath = file.getAbsolutePath() + ".info";
                    FileOutputStream fos = new FileOutputStream(fileInfoPath);
                    OutputStreamWriter osw = new OutputStreamWriter(fos);
                    osw.write(file.getAbsolutePath() + "\n\n");
                    osw.write(MB.mandelInfo());
                    osw.write("\nDuration: " + MB.getDur() + " Sec.\n");

                    osw.close();
                    System.out.println("Save Info:  " + fileInfoPath);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        System.out.println(e.getActionCommand());
        if (Arrays.asList(MNU_DEPTHS).contains(e.getActionCommand())) {
            int newDepth = MB.getDepth();
            switch (Arrays.asList(MNU_DEPTHS).indexOf(e.getActionCommand())) {
                case 0:
                    newDepth = 600;
                    break;
                case 1:
                    newDepth = 800;
                    break;
                case 2:
                    newDepth = 1000;
                    break;
                case 3:
                    String result = JOptionPane.showInputDialog("DEPTH");
                    try {
                        newDepth = Integer.parseInt(result);
                    } catch (NumberFormatException numberFormatExceptione) {
                        System.out.println("\nPlease Enter Integer Number!!!");
                    }
                    break;
            }

            if (newDepth != MB.getDepth()) {
                MB.setDepth(newDepth);
                System.out.println("\nChange Recursion Depth ... ");
                MB.startMandel();
                repaint();
            }
        } else {
            System.out.println("\nChange Color Filter: " + e.getActionCommand());
            MB.setFilter(e.getActionCommand());
            MB.getBi().setRGB(0, 0, Mandelbrot.WIDTH, Mandelbrot.HEIGHT, MB.calcPixelArray(MB.getMandelArray()), 0, Mandelbrot.WIDTH);
            repaint();
        }
    }
}
