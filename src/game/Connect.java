package game;

import gui.ConnectionWindow;
import gui.GUI;

// opens ConnectionWindow and listens to player's actions from it
// enables player to establish connection by initializing Connection;
// if connection is established, interrupts itself
// if not, closes connection
// if window is closed, interrupts itself and closes connection
public class Connect implements Runnable {
	private static long time;
	private static Connection connection;
	private ConnectionWindow window;
	private boolean actionTemp; // recent action value

	public Connect(GUI gui) {
		time = 0;
		connection = null;
		window = gui.getCWindow();
	}
	
	public void run() {		
		while (!Thread.interrupted()) {
			if (connection != null && connection.connected()) { // connection established				
				Files.saveConnectionData(Integer.toString(window.getPlayerTCP()),
						window.getFriendIP(), Integer.toString(window.getFriendTCP())); // save connection data to temp file
				window.dispose();
				window = null;
				Thread.currentThread().interrupt();
			}
			else if (!actionTempTest() && window.getAction()) { // player clicks connect, after validation
				connection = new Connection(window.getPlayerTCP(), window.getFriendIP(), window.getFriendTCP());
				time = System.currentTimeMillis();
				actionTemp = window.getAction();
			}
			else if (!actionTempTest() && !window.getAction()) { // player clicks cancel
				connection.close();
				time = 0;
				connection = null;
				actionTemp = window.getAction();
			}
			else if (connection != null && connection.isClosed()) // if connection error during try, auto cancel
				window.clickCancel();
			else if (time != 0 && System.currentTimeMillis() - time >= 60000) { // 60 sec limit for trying, auto cancel
				window.clickCancel();
			}
			else if (window.isClosed()) { // player closes window
				if (connection != null) connection.close();
				connection = null;
				window = null;
				Thread.currentThread().interrupt();
			}
			
			try {
				Thread.sleep(50);
			} catch (Exception ex) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	// returns false if action differs from recent action (did player try to connect / cancel)
	private boolean actionTempTest() {
		return actionTemp == window.getAction();
	}
	
	public static Connection getConnection() {
		return connection;
	}
}
