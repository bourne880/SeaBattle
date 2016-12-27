package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")

// panel with user interface enabling sending messages and in which messages are displayed
public class Messenger extends JPanel {
	private JTextArea messageArea;
	private JTextField messageField;
	private JButton sendButton;
	private String message; // message sent by player
	
	public Messenger() {
		message = "";
		
		Border firstM = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border secondM = BorderFactory.createEtchedBorder();
		Border compoundM = BorderFactory.createCompoundBorder(firstM, secondM);
		Border mBorder = BorderFactory.createTitledBorder(compoundM, "Messenger", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION);
		setBorder(mBorder);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		messageArea = new JTextArea(5, 10);
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
		messageArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret) messageArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); // automatic scroll down
		
		JScrollPane scrollPane = new JScrollPane(messageArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		Border firstS = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border secondS = UIManager.getBorder("ScrollPane.border");
		Border compoundS = BorderFactory.createCompoundBorder(firstS, secondS);
		scrollPane.setBorder(compoundS);
		
		JPanel messageFieldPanel = new JPanel();
		messageFieldPanel.setLayout(new BoxLayout(messageFieldPanel, BoxLayout.X_AXIS));
		messageFieldPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		messageField = new JTextField();
		messageField.addKeyListener(new EnterPressed());
		
		sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButton());
		
		messageFieldPanel.add(messageField);
		messageFieldPanel.add(sendButton);
		
		add(scrollPane);
		add(messageFieldPanel);
		
		setVisible(false);
	}
	
	// Enter button listener - when released, click sendButton
	// inactive when not visible
	private class EnterPressed implements KeyListener {

		public void keyPressed(KeyEvent event) { }
	
		public void keyReleased(KeyEvent event) {
			if (isVisible() && event.getKeyCode() == KeyEvent.VK_ENTER)
				sendButton.doClick();
		}
	
		public void keyTyped(KeyEvent event) { }
	
	}
	
	// sendButton listener - when clicked, erase messageField and update message
	// inactive when not visible
	private class SendButton implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {
			if (isVisible() && !messageField.getText().isEmpty()) {
				message = messageField.getText();
				messageField.setText("");
			}
		}
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String text) {
		message = text;
	}
	
	// given boolean (true for opponent, false for player) and message, displays message in messageArea
	public void displayMessage(boolean opponent, String message) {
		String author;
		if (opponent)
			author = "Opponent";
		else
			author = "Me";
		
		messageArea.append(author + ": " + message + "\n");
	}

	public void clearMessageField() {
		messageField.setText("");
	}

}
