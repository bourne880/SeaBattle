package phase;

import element.EnemyMove;
import element.PlayerMoveMP;
import game.Connection;
import game.GameState;
import gui.GUI;

// starts the gameplay and switches between player and enemy moves until it announces a winner
// connection dependent - if connection is closed, terminates
public class MPGameplay extends Gameplay {
	private Connection connection;
	
	public MPGameplay(GUI aGUI, GameState aState, Connection aConnection, ThreadGroup parentTG) {
		super(aGUI, aState, parentTG);
		connection = aConnection;
		gameplay = new ThreadGroup(parentTG, "MPGameplay");
	}
	
	// display communicate, no save game option enabling
	protected void communicate() {
		if (!connection.isClosed()) {
			gui.message(3);
			
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}
	
	// makes a mutual draw to choose a starting player
	// starts the game with enemy move or not, depending on the result
	protected void startingPlayer() {
		
		while (!Thread.currentThread().isInterrupted() && !connection.isClosed()) {		
			int myNumber = (int) (Math.random() * 1000);
			int friendNumber = 0;
			connection.setSend('g', myNumber);
			
			while (!Thread.currentThread().isInterrupted())
				if (connection.getReceived().size() > 0) {
					friendNumber = (int) connection.getReceived().remove();
					break;
				}

			if (myNumber < friendNumber) {
				draw = 0;
				break;
			}
			else if (myNumber > friendNumber) {
				draw = 1;
				break;
			}
		}
		
		if (draw == 0)
			enemyMove();
	}
	
	protected void playerMove() {
		Runnable playerMoveMP = new PlayerMoveMP(gui, state, connection);
		runThread(playerMoveMP); // lets player to make a move
	}
	
	protected void enemyMove() {
		Runnable enemyMove = new EnemyMove(gui, state, connection);
		runThread(enemyMove); // reads move made by a friend and displays effects on board
	}
	
	// no optional action
	protected void optionalAfterPlayerWon() {
		
	}
	
	// interrupts if connection is closed
	protected void runThread(Runnable r) {
		if (connection.isClosed()) {
			Thread.currentThread().interrupt();
			return;
		}
		
		super.runThread(r);
	}
}
