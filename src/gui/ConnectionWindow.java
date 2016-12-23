package gui;

import game.Files;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.Inet4Address;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

// window with user interface enabling player connection with friend's game
public class ConnectionWindow {
	private JFrame parentFrame; // frame from which this frame (window) has been opened
	private JTextField field2;
	private JTextField field3;
	private JTextField field4;
	private JFrame frame;
	private JButton connect; // its text is changed to try depending on action
	private JButton exit; // its text is changed to cancel depending on action
	
	private boolean closed; // indicates if window is closed
	private boolean action; // action indicator - true if trying to connect, false otherwise
	// data attributes
	private int playerTCP;
	private String friendIP;
	private int friendTCP;
	
	public ConnectionWindow(JFrame aParentFrame) {
		parentFrame = aParentFrame;
		parentFrame.setEnabled(false);
		
		frame = new JFrame("Connection");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowClosed());
				
		JPanel background = new JPanel();
		background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		BoxLayout bLayout = new BoxLayout(background, BoxLayout.Y_AXIS);
		background.setLayout(bLayout);
		
		JPanel top = new JPanel();
		GridLayout tLayout = new GridLayout(2, 1);
		tLayout.setVgap(20);
		top.setLayout(tLayout);
		
		JPanel info = new JPanel();
		Border firstB = BorderFactory.createEtchedBorder();
		Border secondB = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border compound = BorderFactory.createCompoundBorder(firstB, secondB);
		info.setBorder(compound);
		info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        String sen1 = "In order to connect you and your friend should put your ip addresses in text fields and try to connect.";
        String sen2 = "TCP port values are default and if you don't need to, don't change them. If you change them, you need to exchange these values between you too.";
        String html1 = "<html><body style='width: 150px; text-align: justify'>";
        String html2 = "<br>";

        JLabel text = new JLabel(html1 + sen1 + html2 + sen2);
        info.add(text);
		top.add(info);
		
		JPanel data = new JPanel();
		GridLayout dataLayout = new GridLayout(4, 2);
		dataLayout.setVgap(10);
		dataLayout.setHgap(5);
		data.setLayout(dataLayout);
		
		String[] cArray = Files.readConnectionData(); // reads connection data from file
		String put;
		
		JLabel label1 = new JLabel("My IP: ");
		data.add(label1);
		
		JTextField field1 = new JTextField();
		
	    String myIP = null;
	    try {
			myIP = Inet4Address.getLocalHost().getHostAddress();
		} catch (Exception ex) { }
	    
	    field1.setText(myIP);
	    field1.setHorizontalAlignment(JTextField.RIGHT);
	    field1.setEditable(false);
	    data.add(field1);
		
		JLabel label2 = new JLabel("My TCP port: ");
		data.add(label2);
		
		if (cArray != null)
			put = cArray[0];
		else
			put = "4411";
	    field2 = new JTextField(put);
	    field2.setHorizontalAlignment(JTextField.RIGHT);
	    data.add(field2);
		
		JLabel label3 = new JLabel("Friend's IP: ");
		data.add(label3);
		
		if (cArray != null)
			put = cArray[1];
		else
			put = "";
	    field3 = new JTextField(put);
	    field3.setHorizontalAlignment(JTextField.RIGHT);
	    data.add(field3);
		
		JLabel label4 = new JLabel("Friend's TCP port: ");
		data.add(label4);
	    
		if (cArray != null)
			put = cArray[2];
		else
			put = "4411";
	    field4 = new JTextField(put);
	    field4.setHorizontalAlignment(JTextField.RIGHT);
	    data.add(field4);
	    
	    top.add(data);
	    background.add(top);
	    
	    
	    JPanel buttons = new JPanel();
	    
	    ButtonListener bListener = new ButtonListener();
	    connect = new JButton("Connect");
	    connect.addActionListener(bListener);
	    exit = new JButton("Exit");
	    exit.addActionListener(bListener);
	    
	    buttons.add(connect);
	    buttons.add(exit);
	    background.add(Box.createRigidArea(new Dimension(0, 15)));
	    background.add(buttons);
	    
	    frame.setContentPane(background);
		frame.setBounds(50,50,100,100);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
	
	// window listener - when closed update closed, position on top and enable parentFrame
	private class WindowClosed implements WindowListener {

		public void windowActivated(WindowEvent event) { }

		public void windowClosed(WindowEvent event) {
			closed = true;
			parentFrame.setEnabled(true);
			parentFrame.setAlwaysOnTop(true);
			parentFrame.setAlwaysOnTop(false);
		}

		public void windowClosing(WindowEvent event) { }

		public void windowDeactivated(WindowEvent event) { }

		public void windowDeiconified(WindowEvent event) { }

		public void windowIconified(WindowEvent event) { }

		public void windowOpened(WindowEvent event) { }
		
	}
	
	// button listener - according to action, which button was clicked, and what values were put in textfields, performs actions
	private class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			JButton source = (JButton) event.getSource();
			
			if (!action && source == connect) { // user clicks connect
				String aPlayerTCP = field2.getText();
				friendIP = field3.getText();
				String aFriendTCP = field4.getText();
				
				if (aPlayerTCP.isEmpty() || friendIP.isEmpty() || aFriendTCP.isEmpty()) { // check if textfields are empty
					JOptionPane.showMessageDialog(frame, "None of the fields can be left empty.");
					return;
				}
				if (!validIP(friendIP)) { // validate ip
					JOptionPane.showMessageDialog(frame, "IP address is not valid.");
					return;
				}
				
				try { // check if tcp values are numbers
					playerTCP = Integer.parseInt(aPlayerTCP);
					friendTCP = Integer.parseInt(aFriendTCP);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(frame, "TCP values must be an integer number.");
					return;
				}
					field2.setEditable(false);
					field3.setEditable(false);
					field4.setEditable(false);
				
				action = !action; // update action
				connect.setText("Trying");
				connect.setEnabled(false);
				exit.setText("Cancel");
			}
			else if (!action) // user clicks exit
				frame.dispose();
			else { // user clicks cancel
				field2.setEditable(true);
				field3.setEditable(true);
				field4.setEditable(true);
				
				action = !action;
				connect.setText("Connect");
				connect.setEnabled(true);
				exit.setText("Exit");
			}
			
		}
		
	}
	
	// given string returns true if it is valid ip number
	public static boolean validIP(String ip) {
	    try {
	        if (ip == null || ip.isEmpty())
	            return false;

	        String[] parts = ip.split("\\.");
	        if (parts.length != 4)
	            return false;

	        for ( String s : parts ) {
	            int i = Integer.parseInt( s );
	            if ( (i < 0) || (i > 255) )
	                return false;
	        }
	        if ( ip.endsWith(".") )
	            return false;

	        return true;
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	}
	
	// returns true/false whether window is closed
	public boolean isClosed() {
		return closed;
	}
	
	public boolean getAction() {
		return action;
	}
	
	public int getPlayerTCP() {
		return playerTCP;
	}
	
	public String getFriendIP() {
		return friendIP;
	}
	
	public int getFriendTCP() {
		return friendTCP;
	}
	
	public void clickCancel() {
		exit.doClick();
	}
	
	// disposes window
	public void dispose() {
		frame.dispose();
	}

}
