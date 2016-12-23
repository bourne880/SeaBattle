package phase;

import element.AIMove;
import element.PlayerMove;
import game.Files;
import game.GameState;
import gui.GUI;

import java.util.ArrayList;


// starts the gameplay and switches between player and computer moves until it announces a winner
public class Gameplay implements Runnable {
	protected GUI gui;
	protected GameState state;
	protected static int draw; // result of a draw, based on which a starting player is chosen
	private static int winner; // 0 for player winner, 1 for computer winner
	private static int score; // result of the game (number of moves)
	protected ThreadGroup gameplay;
		
	public Gameplay(GUI aGUI, GameState aState, ThreadGroup parentTG) {
		gui = aGUI;
		state = aState;
		gameplay = new ThreadGroup(parentTG, "Gameplay");
	}
	
	public void run() {				
		communicate(); // communicate about beginning of the game
		
		startingPlayer(); // randomly starts the game with computer player move or not
		
		while (!Thread.interrupted()) { // a loop is being continued until there is a winner or thread is interrupted
			playerMove();
			
			// after player made a move, checks if he won the game
			if (state.getPlayerMoves().containsAll(state.getEnemyShipsSquares())) {
				winner = 0;
				score = state.getPlayerMoves().size();
				gui.message(9);
				optionalAfterPlayerWon();
				
				break;
			}
				
			enemyMove();
			
			// after enemy (computer) made a move, checks if it won the game
			if (state.getEnemyMoves().containsAll(state.getPlayerShipsSquares())) {
				winner = 1;
				score = state.getEnemyMoves().size();
				gui.message(9);
				
				if (!Thread.interrupted()) // display enemy ships when he is the winner
					for (ArrayList<Integer> list : state.getEnemyShips())
						gui.getEBoard().displayShip(list, state.getEnemyShipHorizontal(list));
				
				break;
			}
		}
			
	}
	
	public static int getDraw() {
		return draw;
	}
	
	// displays communicate about beginning of the game and waits 1 sec
	protected void communicate() {
		gui.message(3);
		
		try {
			Thread.sleep(1000);
		} catch (Exception ex) {
			Thread.currentThread().interrupt();
			return;
		}
		
		gui.getMenu().enableItem(2, true); // enable to save game in gameplay
	}
	
	// makes a draw and sets the draw value to 0 or 1
	// starts the game with enemy (computer) move or not based on the draw value
	protected void startingPlayer() {
		draw = (int) (Math.random() * 2);
		
		if (draw == 0) // if 0 is drawn, computer player starts the game
			enemyMove();
	}
	
	protected void playerMove() {
		Runnable playerMove = new PlayerMove(gui, state);
		runThread(playerMove); // lets player to make a move
	}
	
	protected void enemyMove() {
		Runnable aiMove = new AIMove(gui, state);
		runThread(aiMove); // makes a move for a computer
	}
	
	// optional action after player won
	protected void optionalAfterPlayerWon() {
		Files.updateScores(score); // adds the score to best scores list
		if (!gui.getMenu().itemEnabled(4)) gui.getMenu().enableItem(4, true); // if best scores was disabled in menu, enable
	}
	
	// starts thread with task given runnable, and doesn't move forward until thread is terminated
	protected void runThread(Runnable r) {
		if (Thread.interrupted()) {
			Thread.currentThread().interrupt();
			return;
		}
		
		Thread thread = new Thread(gameplay, r);
		thread.start();
		
		try {
			thread.join();
		} catch (Exception e) { 
			Thread.currentThread().interrupt();
			return;
		}
	}
	
	// returns communicate (for MessageBox) after the gameplay is finished based on winner and score
	public static String winMessage() {
		String message = null;
		
		if (winner == 0)
			message = "Congratulations! You've won. Your score is " + score;
		else if (winner == 1)
			message = "End of the game. Opponent has won with the score of " + score;
		
		return message;
	}

}
