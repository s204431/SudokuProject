package sudoku;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class View extends JFrame {

    public View() {
        // Create panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9,9));

        // Add fields
        JTextField field;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                field = new JTextField(1);
                field.setHorizontalAlignment(JTextField.CENTER);
                panel.add(field);
            }
        }

        // Add frame
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        add(panel);
        setSize(500,500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
