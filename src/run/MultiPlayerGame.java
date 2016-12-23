package run;

import javax.swing.JOptionPane;

import game.Communication;
import game.Connect;
import game.Connection;
import game.GameState;
import gui.GUI;
import phase.EnemyDeployment;
import phase.MPGameplay;
import phase.PlayerDeploymentMP;

// tries to establish connection, if unsuccessful returns
// then enables communication and runs through phases that make up multi player game
public class MultiPlayerGame implements Runnable {
	private GUI gui;
	private GameState state;
	private Integer[] deployArray;
	private Connection connection;
	private ThreadGroup multiPlayer;
	
	public MultiPlayerGame(GUI aGUI, GameState aState, Integer[] aDeployArray, ThreadGroup parentTG) {
		gui = aGUI;
		state = aState;
		deployArray = aDeployArray;
		multiPlayer = new ThreadGroup(parentTG, "MultiPlayer");
	}
	
	public void run() {
		
		establishConnection(); // player establishes connection or return
		
		if (connection == null || !connection.connected()) // if there is no connection return
			return;
		
		// enables Communication
		Thread communication = new Thread(multiPlayer, new Communication(gui, connection));
		communication.start();
		
		 // player and enemy deployment
		deployment();
		
		// Gameplay
		Runnable mpGameplay = new MPGameplay(gui, state, connection, multiPlayer);
		runThread(mpGameplay);
		
		connectionBrokenMessage(); // display message if connection was broken
	}
	
	// starts thread with task given runnable, and doesn't move forward until thread is terminated
	private void runThread(Runnable r) {
		if (Thread.interrupted() || (connection != null && connection.isClosed())) {
			Thread.currentThread().interrupt();
			return;
		}
		
		Thread thread = new Thread(multiPlayer, r);
		thread.start();
		
		try {
			thread.join();
		} catch (Exception e) { 
			Thread.currentThread().interrupt();
			return;
		}
	}
	
	// player establishes connection or return
	private void establishConnection() {
		gui.message(10); // if user exits

		// Connect
		Runnable connect = new Connect(gui, multiPlayer);
		runThread(connect);
		
		connection = Connect.getConnection(); // after connect has finished
	}
	
	// player and enemy deployment, the order dependent on result of mutualDraw
	private void deployment() {
		Runnable playerDeploymentMP = new PlayerDeploymentMP(gui, state, deployArray, connection, multiPlayer);
		Runnable enemyDeployment = new EnemyDeployment(gui, state, connection);
		
		if (mutualDraw()) {
			runThread(enemyDeployment);
			runThread(playerDeploymentMP);
		}
		else {
			runThread(playerDeploymentMP);
			runThread(enemyDeployment);
		}
	}
	
	// player and friend send random number to themselves
	// based on relation between these numbers, the boolean is returned
	private boolean mutualDraw() {
		boolean result = false;
		int iteration = 0;
		
		while (!Thread.currentThread().isInterrupted()) {
			// detecting if connected to the same computer
			if (++iteration == 10) {
				JOptionPane.showMessageDialog(null, "You can't connect to the same computer!");
				connection.close();
			}
			
			int myNumber = (int) (Math.random() * 1000);
			int friendNumber = 0;
			connection.setSend('g', myNumber);
			
			while (!Thread.currentThread().isInterrupted())
				if (connection.getReceived().size() > 0) {
					friendNumber = (int) connection.getReceived().remove();
					break;
				}
			
			if (myNumber < friendNumber) {
				result = false;
				break;
			}
			else if (myNumber > friendNumber) {
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	// displays message to player if connection was broken
	private void connectionBrokenMessage() {
		boolean error = false;
		
		 // if connection error, display player message
		if (connection.isClosed()) {
			gui.message(11);
			error = true;
		}
		
		// if there was no error
		if (!error) {
			 // wait until player or friend starts new game/exits
			while (!Thread.interrupted() && !connection.isClosed()) { }
			
			 // if connection is broken (friend's action), display player message
			if (connection.isClosed())
				gui.message(12);
		}
	}
}
