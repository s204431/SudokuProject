package sudoku;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class View extends JPanel {

    public View() {
        setPreferredSize(new Dimension(Field.HEIGHT * 9, Field.WIDTH * 9));

        // Create frame
        JFrame frame = new JFrame("Sudoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2;

        //draw fields
        Color black = Color.BLACK;
        Color lightGray = Color.LIGHT_GRAY;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Field field = new Field(-1, true);
                g.setColor(lightGray);
                g.fillRect(i * Field.HEIGHT, j * Field.WIDTH, Field.WIDTH, Field.HEIGHT);
                g.setColor(black);
                g.drawRect(i * Field.HEIGHT, j * Field.WIDTH, Field.WIDTH, Field.HEIGHT);
            }
        }
        
    }
}
