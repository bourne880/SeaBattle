package gui;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")

// panel with graphics and game info
public class Intro extends JPanel {
	private JLabel bshipL;
	private JLabel line1;
	private JLabel line2;
	
	public Intro() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		ImageIcon bship = new ImageIcon(this.getClass().getResource("/battleship.jpg"));
		bshipL = new JLabel(bship, JLabel.CENTER);
		
		String html = "<html><body style='width='502'; text-align: center'>";
		line1 = new JLabel(html + "Sea Battle");
		line1.setFont(new Font("Dialog", Font.BOLD, 24));
		line1.setHorizontalAlignment(JLabel.CENTER);
		
		String html1 = "<html><table width='495' cellspacing='0' cellpadding='0'><tr><td align='left'>";
		String html2 = "</td><td align='right'>";
		String html3 = "</td></tr></table></html>";
		line2 = new JLabel(html1 + "Version: 1.0" + html2 + "\u00a9 2016 Adam Pierz" + html3);
		
		add(line1);
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(bshipL);
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(line2);
	}
	
}
