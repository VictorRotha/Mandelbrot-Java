import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;


public class Viewer extends JFrame implements ActionListener{

    private final String MNU_CUSTOM_LIMIT = "Custom Limit ...";
    private final String MNU_CUSTOM_DEPTH = "Custom Depth ...";
    private final String MNU_INFO = "Info";

    private final String[] MNU_DEPTHS = {"600", "800", "1000"};
    private final String[] MNU_LIMITS = {"1.0", "2.0", "5.0"};
    private final String[] MNU_ZOOMS = {"2", "3", "5", "10"};

    private final Mandelbrot MB;
    private final ViewerPanel VP;

    private String filepath;


    public Viewer(Mandelbrot _mandelbrot) {

        MB = _mandelbrot;
        setTitle("Mandelbrot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        VP = new ViewerPanel(_mandelbrot);
        add(new JScrollPane(VP));

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
       JMenuItem itmSize = new JMenuItem("Image Size");

       itmSave.addActionListener(e -> saveToFile(itmChkInfo.getState()));
       itmSize.addActionListener(e -> resizeDialog());

       mnuFile.add(itmSave);
       mnuFile.add(itmChkInfo);
       mnuFile.add(itmSize);
       mnuBar.add(mnuFile);

       JMenu mnuFilter = new JMenu("Filter");
       JRadioButtonMenuItem btn;
       ButtonGroup grp = new ButtonGroup();
       for (String name: MB.COLOR_FILTERS) {
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
       for (String depth: MNU_DEPTHS) {
           btnDepth = new JRadioButtonMenuItem(depth);
           btnDepth.addActionListener(this);
           mnuDepth.add(btnDepth);
           grpDepth.add(btnDepth);
           if (depth.equals(String.valueOf(MB.getDepth()))) {
               btnDepth.setSelected(true);
           }
       }
        btnDepth = new JRadioButtonMenuItem(MNU_CUSTOM_DEPTH);
        btnDepth.addActionListener(this);
        mnuDepth.add(btnDepth);
        grpDepth.add(btnDepth);
        if (!Arrays.asList(MNU_DEPTHS).contains(String.valueOf(MB.getDepth()))) {
            btnDepth.setSelected(true);
        }
       mnuBar.add(mnuDepth);

       JMenu mnuLimit = new JMenu("Limit");
       ButtonGroup grpLimit = new ButtonGroup();
       JRadioButtonMenuItem btnLimit;
       for (String limit: MNU_LIMITS) {
           btnLimit = new JRadioButtonMenuItem(limit);
           if (limit.equals(String.valueOf(MB.getLimiter()))) {
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
       if (!Arrays.asList(MNU_LIMITS).contains(String.valueOf(MB.getLimiter()))) {
           btnLimit.setSelected(true);
       }
       mnuBar.add(mnuLimit);

       JMenu mnuZoom = new JMenu("Zoom");
       JMenuItem btnZoom;
       ButtonGroup grpZoom = new ButtonGroup();
       for (String zoom : MNU_ZOOMS) {
           btnZoom = new JRadioButtonMenuItem(zoom + "x");
           btnZoom.addActionListener(this);
           grpZoom.add(btnZoom);
           mnuZoom.add(btnZoom);
           if (zoom.equals(String.valueOf(MB.getZoom()))) {
               btnZoom.setSelected(true);
           }
       }
       mnuBar.add(mnuZoom);

       JMenuItem itmInfo = new JMenuItem(MNU_INFO);
       itmInfo.addActionListener(this);
       mnuBar.add(itmInfo);

       return mnuBar;
    }

    private void resizeDialog() {
        int newWidth = MB.getWidth();
        int newHeight = MB.getHeight();

        JPanel sizePane = new JPanel();
        JTextField t1 = new JTextField(String.valueOf(newWidth), 6 );
        JTextField t2 = new JTextField(String.valueOf(newHeight), 6);
        sizePane.add(new JLabel("WIDTH"));
        sizePane.add(t1);
        sizePane.add(new JLabel("HEIGHT"));
        sizePane.add(t2);
        int result = JOptionPane.showConfirmDialog(
                this,
                sizePane,
                "Size",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                newWidth = Integer.parseInt(t1.getText());
                newHeight = Integer.parseInt(t2.getText());
            } catch (Exception ex) {
                System.out.printf("Input Not Valid: w = %s, h = %s\n", t1.getText(), t2.getText());
            }

            if (newWidth != MB.getWidth() || newHeight != MB.getHeight()) {
                System.out.println("\nChange Image Size ... ");
                double ratio = (double) newWidth / newHeight;
                MB.setRangeImag(MB.getRangeReal() / ratio);
                MB.setWidth(newWidth);
                MB.setHeight(newHeight);
                VP.setPreferredSize(new Dimension(newWidth, newHeight));
                MB.startMandel();
                pack();
                repaint();
            }
        }

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

        String ac = e.getActionCommand();

        if (Arrays.asList(MNU_DEPTHS).contains(ac) || ac.equals(MNU_CUSTOM_DEPTH)) {
            int newDepth = MB.getDepth();
            if (ac.equals(MNU_CUSTOM_DEPTH)) {
                String result = (String) JOptionPane.showInputDialog(
                        this,
                        "New Recursion Depth:",
                        "DEPTH",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        MB.getDepth());
                try {
                    newDepth = Integer.parseInt(result);
                } catch (NumberFormatException ex) {
                    System.out.println(result + " Not Valid");
                }
            } else {
                newDepth = Integer.parseInt(ac);
            }

            if (newDepth != MB.getDepth()) {
                MB.setDepth(newDepth);
                System.out.println("\nChange Recursion Depth ... ");
                MB.startMandel();
                repaint();
            }
        } else if (Arrays.asList(MNU_LIMITS).contains(ac) || ac.equals(MNU_CUSTOM_LIMIT)) {
            double newLimit = MB.getLimiter();
            if (ac.equals(MNU_CUSTOM_LIMIT)) {
                String result = (String) JOptionPane.showInputDialog(
                        this,
                        "New Limit:",
                        "LIMIT",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        MB.getLimiter());
                try {
                    newLimit = Double.parseDouble(result);
                } catch (Exception ex) {
                    System.out.println(result + " Not Valid");
                }
            } else {
                newLimit = Double.parseDouble(ac);
            }
            if (newLimit != MB.getLimiter()) {
                System.out.println("\nChange Limiter ...");
                MB.setLimiter(newLimit);
                MB.startMandel();
                repaint();
            }
        } else if (Arrays.asList(MB.COLOR_FILTERS).contains(ac)) {
            System.out.println("\nChange Color Filter: " + ac);
            MB.setFilter(ac);
            MB.getBi().setRGB(0, 0, MB.getWidth(), MB.getHeight(), MB.calcPixelArray(MB.getMandelArray()), 0, MB.getWidth());
            repaint();
        } else if (ac.endsWith("x")) {
//            int newZoom = MB.getZoom();
            int newZoom = Integer.parseInt(ac.substring(0, ac.length()-1));
            MB.setZoom(newZoom);


        } else if (e.getActionCommand().equals(MNU_INFO)) {
            JOptionPane.showMessageDialog(
                    this,
                    MB.mandelInfo(),
                    "INFO",
                    JOptionPane.PLAIN_MESSAGE,
                    null);
        }
    }
}
