import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class CustomMenu extends JMenu {

    private ButtonGroup BG;
    private final String[] VALUES;
    private final ActionListener LISTENER;

    private String selected;

    public CustomMenu(String _title, String[] _values, String _selected, ActionListener _listener) {
        super(_title);
        BG = new ButtonGroup();
        VALUES = _values;
        LISTENER = _listener;
        selected = _selected;

        radioButtons();

    }

    private void radioButtons() {
        JRadioButtonMenuItem btn;
        if (!Arrays.asList(VALUES).contains(selected)) {
            selected = VALUES[VALUES.length-1];
        }
        for (String value : VALUES) {
            btn = new JRadioButtonMenuItem(value);
            btn.addActionListener(LISTENER);
            BG.add(btn);
            add(btn);
            if (value.equals(selected)) {
                btn.setSelected(true);
            }
        }
    }
}
