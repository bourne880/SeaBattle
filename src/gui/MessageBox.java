package gui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import phase.Gameplay;

@SuppressWarnings("serial")

// panel displaying communicates to player
public class MessageBox extends JPanel {
	private DCardLayout bPanelLayout;
	private JPanel bPanel;
	
	public MessageBox() {
		Border firstB = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		Border secondB = BorderFactory.createEtchedBorder();
		Border compoundB = BorderFactory.createCompoundBorder(firstB, secondB);
		setBorder(compoundB);
		
		bPanelLayout = new DCardLayout();
		bPanel = new JPanel(bPanelLayout);
		bPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JLabel label1 = new JLabel("Deploy your ships on the board. Use left mouse button to deploy and right mouse button to change position.");
		bPanel.add(label1, "1");
		
		JLabel label2 = new JLabel("Wait until opponent's ships are deployed.");
		bPanel.add(label2, "2");
		
		JLabel label3 = new JLabel("The game begins.");
		bPanel.add(label3, "3");
		
		JLabel label4 = new JLabel("Your move.");
		bPanel.add(label4, "4");
		
		JLabel label5 = new JLabel("Opponent's move.");
		bPanel.add(label5, "5");
		
		JLabel label6 = new JLabel("Missed.");
		bPanel.add(label6, "6");
		
		JLabel label7 = new JLabel("Hit!");
		bPanel.add(label7, "7");
		
		JLabel label8 = new JLabel("Hit and sunk!");
		bPanel.add(label8, "8");
		
		JLabel label10 = new JLabel("Choose action from the menu bar.");
		bPanel.add(label10, "10");
		
		JLabel label11 = new JLabel("There was a connection error. Choose action from the menu bar.");
		bPanel.add(label11, "11");
		
		JLabel label12 = new JLabel("Friend starts a new game or exited game.");
		bPanel.add(label12, "12");
			
		this.add(bPanel);		
	}
	
	// switches to communicate given number
	public void message(int i) {
		if (i == 9) {
			JLabel label9 = new JLabel(Gameplay.winMessage());
			bPanel.add(label9, "9");
		}
				
		bPanelLayout.show(bPanel, Integer.toString(i));
	}

}
