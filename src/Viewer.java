import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;


public class Viewer extends JFrame implements ActionListener{

    private final String MNU_CUSTOM_LIMIT = "Custom Limit ...";

    private final Mandelbrot MB;
    private String filepath;
    private final String[] MNU_DEPTHS = {"Recursion Depth 600", "Recursion Depth 800", "Recursion Depth 1000", "Custom ..."};

    public Viewer(Mandelbrot _mandelbrot) {

        MB = _mandelbrot;
        setTitle("Mandelbrot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JScrollPane(new ViewerPanel(_mandelbrot)));
//        add(new ViewerPanel(_mandelbrot));

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
       JRadioButtonMenuItem btnDepth;
       ButtonGroup grpDepth = new ButtonGroup();
       for (int i = 0; i < MNU_DEPTHS.length; i++) {
           String name = MNU_DEPTHS[i];
           btnDepth = new JRadioButtonMenuItem(name);
           btnDepth.addActionListener(this);
           mnuDepth.add(btnDepth);
           grpDepth.add(btnDepth);
           switch (MB.getDepth()) {
              case 600:
                  if (i == 0) {btnDepth.setSelected(true);}
                  break;
              case 800:
                  if (i == 1) {btnDepth.setSelected(true);}
                  break;
              case 1000:
                  if (i == 2) {btnDepth.setSelected(true);}
                  break;
               default:
                   if ( i == 3) {btnDepth.setSelected(true);}
                   break;

          }

       }
       mnuBar.add(mnuDepth);

       JMenu mnuLimit = new JMenu("Limit");
       Double[] limits = {1.0, 2.0, 5.0};

       ButtonGroup grpLimit = new ButtonGroup();
       JRadioButtonMenuItem btnLimit;
       for (double limit: limits) {

           String name = String.valueOf(limit);
           btnLimit = new JRadioButtonMenuItem(name);
           if (limit == MB.getLimiter()) {
               btnLimit.setSelected(true);
           }
           btnLimit.addActionListener(this);
           grpLimit.add(btnLimit);
           mnuLimit.add(btnLimit);
       }
       btnLimit = new JRadioButtonMenuItem(MNU_CUSTOM_LIMIT);
       btnLimit.addActionListener(this);
       grpLimit.add(btnLimit);
       mnuLimit.add(btnLimit);
       if (!Arrays.asList(limits).contains(MB.getLimiter())) {
           btnLimit.setSelected(true);
       }

       mnuBar.add(mnuLimit);

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
//        System.out.println(e.getActionCommand() + " " + e.getSource().toString());

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
                    String result = (String) JOptionPane.showInputDialog(
                            null,
                            "New Recursion Depth:",
                            "DEPTH",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            MB.getDepth());
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
        } else  if (Arrays.asList(Mandelbrot.COLOR_FILTERS).contains(e.getActionCommand())) {
            System.out.println("\nChange Color Filter: " + e.getActionCommand());
            MB.setFilter(e.getActionCommand());
            MB.getBi().setRGB(0, 0, Mandelbrot.WIDTH, Mandelbrot.HEIGHT, MB.calcPixelArray(MB.getMandelArray()), 0, Mandelbrot.WIDTH);
            repaint();
        } else {
            double newLimit = MB.getLimiter();
            if (e.getActionCommand().equals(MNU_CUSTOM_LIMIT)) {
                String result = (String) JOptionPane.showInputDialog(
                        null,
                        "New Limit:",
                        "LIMIT",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        MB.getLimiter());
                try {
                    newLimit = Double.parseDouble(result);
                } catch (NumberFormatException ex) {
                    System.out.println("Please Enter Number!");
                }
            } else {
                newLimit = Double.parseDouble(e.getActionCommand());
            }
                if (newLimit != MB.getLimiter()) {
                    System.out.println("\nChange Limiter ...");
                    MB.setLimiter(newLimit);
                    MB.startMandel();
                    repaint();
                }
            }
    }
}
