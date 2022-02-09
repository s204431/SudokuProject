package sudoku;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class View {

    public View(){
        JTextField searchTermTextField = new JTextField(26);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JButton button = new JButton("Button");
        JLabel label = new JLabel("This is a sudoku solver");
        // Display it all in a scrolling window and make the window appear
        JFrame frame = new JFrame("Sudoku-Solver");
        panel.add(label);
        panel.add(button);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.add(panel);
        frame.setSize(500,500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
