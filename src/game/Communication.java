package game;

import gui.GUI;
import gui.Messenger;

import java.util.LinkedList;

// enables communication during multi player game
// displays Messenger at start, hides when finished;
// listens to player's send button actions from Messenger and checks if new messages were received in Connection
// displays messages in Messenger and updates receivedM/send queues in Connection accordingly;
// connection dependent - if connection is closed, terminates
public class Communication implements Runnable {
	private GUI gui;
	private Connection connection;
	private LinkedList<String> receivedMTemp; // recent copy of received messages queue
	private Messenger messenger;
	private String messageTemp; // recent message sent by player
	
	public Communication(GUI aGUI, Connection aConnection) {
		gui = aGUI;
		connection = aConnection;
		receivedMTemp = new LinkedList<>();
		messenger = gui.getMessenger();
		messageTemp = messenger.getMessage();
	}
	
	public void run() {		
		gui.setMessengerVisible(true); // display messenger in GUI at start
		
		while (!Thread.interrupted() && !connection.isClosed()) {
			if (!messageTempTest()) { // handling of sent messages
				messageTemp = messenger.getMessage(); // update messageTemp
				connection.setSend('m', messageTemp); // add message to send queue
				messenger.displayMessage(false, messageTemp); // display message
			}
			else if (!receivedMTempTest()) { // handling of received messages
				LinkedList<String> receivedM = connection.getReceivedM();
				receivedMTemp.addAll(receivedM); // update receivedMTemp
				receivedM.removeAll(receivedMTemp); // update receivedM
				
				while (!receivedMTemp.isEmpty()) // display messages copied from received queue
					messenger.displayMessage(true, receivedMTemp.remove());
			}
			
			try {
				Thread.sleep(50);
			} catch (Exception ex) {
				Thread.currentThread().interrupt();
			}
			
		}
		
		// when finished
		messenger.clearMessageField(); // clear messageField
		gui.setMessengerVisible(false); // hide Messenger	
	}
	
	// returns true/false whether sent message differs from recent( did player send a message)
	private boolean messageTempTest() {
		return messageTemp.equals(messenger.getMessage());
	}
	
	// returns true/false whether received messages queue differs from recent (were new messages received)
	private boolean receivedMTempTest() {
		return receivedMTemp.size() == connection.getReceivedM().size();
	}
	
}
