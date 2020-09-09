package play.Scrap;

import java.awt.*;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
public class JScrollDemo {
		public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout(8, 6));

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JTextArea tArea = new JTextArea(20,20);
		JLabel labelColumn = new JLabel("Column Header");
		JLabel labelRow = new JLabel("Row Header");
		JLabel label1 = new JLabel("UL");
		JLabel label2 = new JLabel("UR");
		JLabel label3 = new JLabel("LL");
		JLabel label4 = new JLabel("LR");
		JScrollPane scrollPane = new JScrollPane(tArea);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setColumnHeaderView(labelColumn);
		scrollPane.setRowHeaderView(labelRow);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER ,label1);
		scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER ,label2);
		scrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER ,label3);
		scrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER ,label4);
		panel.add(scrollPane);
		frame.add(panel, BorderLayout.WEST);
		frame.setSize(500, 500);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		}
}