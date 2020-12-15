import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;


public class Viewer extends JFrame {

    private final String MNU_INFO = "Info";
    private final String MNU_CUSTOM_DEPTH = "Custom Depth ...";
    private final String MNU_CUSTOM_LIMIT = "Custom Limit ...";

    private final String[] MNU_DEPTHS = {"600", "800", "1000", MNU_CUSTOM_DEPTH};
    private final String[] MNU_LIMITS = {"1.0", "2.0", "5.0", MNU_CUSTOM_LIMIT};
    private final String[] MNU_ZOOMS = {"2x", "3x", "5x", "10x"};

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
        JCheckBoxMenuItem itmChkInfo = new JCheckBoxMenuItem("Save Info");
        itmChkInfo.setState(true);
        JMenuItem itmSave = new JMenuItem("Save Image");
        itmSave.addActionListener(e -> saveToFile(itmChkInfo.getState()));
        JMenuItem itmSize = new JMenuItem("Image Size");
        itmSize.addActionListener(e -> resizeDialog());

        mnuFile.add(itmSave);
        mnuFile.add(itmChkInfo);
        mnuFile.add(new JSeparator());
        mnuFile.add(itmSize);
        mnuBar.add(mnuFile);

        CustomMenu mnuFilter = new CustomMenu("Filter", MB.COLOR_FILTERS, MB.getFilter(),
                e -> {
                    String ac = e.getActionCommand();
                    System.out.println("\nChange Color Filter: " + ac);
                    MB.setFilter(ac);
                    MB.applyFilter();
                    repaint();
                });
        mnuBar.add(mnuFilter);

        CustomMenu mnuDepth = new CustomMenu("Depth", MNU_DEPTHS, MB.getDepth() + "", this::changeDepth);
        mnuBar.add(mnuDepth);
        CustomMenu mnuLimit = new CustomMenu("Limit", MNU_LIMITS, MB.getLimiter() + "", this::changeLimit);
        mnuBar.add(mnuLimit);
        CustomMenu mnuZoom = new CustomMenu("Zoom", MNU_ZOOMS,  MB.getZoom() + "x", this::changeZoom);
        mnuBar.add(mnuZoom);
        JMenuItem itmInfo = new JMenuItem(MNU_INFO);
        itmInfo.addActionListener(e -> JOptionPane.showMessageDialog(
                       this, MB.mandelInfo(), "INFO", JOptionPane.PLAIN_MESSAGE,null));
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

    private void changeDepth(ActionEvent e) {
        String result = e.getActionCommand();
        int newDepth = MB.getDepth();
        if (result.equals(MNU_CUSTOM_DEPTH)) {
            result = (String) JOptionPane.showInputDialog(
                    this,
                    "New Recursion Depth:",
                    "DEPTH",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "600"
            );
        }
        try {
            newDepth = Integer.parseInt(result);
        } catch (NumberFormatException ex) {
            System.out.println(result + " Not Valid");
        }

        if (newDepth != MB.getDepth()) {
            MB.setDepth(newDepth);
            System.out.println("\nChange Recursion Depth ... ");
            MB.startMandel();
            repaint();
        }
    }

    private void changeLimit(ActionEvent e) {
        String result = e.getActionCommand();
        double newLimit = MB.getLimiter();
        if (result.equals(MNU_CUSTOM_LIMIT)) {
            result = (String) JOptionPane.showInputDialog(
                    this,
                    "New Limit:",
                    "LIMIT",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    MB.getLimiter());
        }
        try {
            newLimit = Double.parseDouble(result);
        } catch (Exception ex) {
            System.out.println(result + " Not Valid");
        }

        if (newLimit != MB.getLimiter()) {
            System.out.println("\nChange Limiter ...");
            MB.setLimiter(newLimit);
            MB.startMandel();
            repaint();
        }
    }

    private void changeZoom(ActionEvent e) {
        String result = e.getActionCommand();
        int newZoom = Integer.parseInt(result.substring(0, result.length()-1));
        MB.setZoom(newZoom);
    }

}
