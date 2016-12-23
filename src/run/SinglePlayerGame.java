package run;

import game.GameState;
import gui.GUI;
import phase.AIDeployment;
import phase.Gameplay;
import phase.PlayerDeployment;

// runs through phases that make up single player game
public class SinglePlayerGame implements Runnable {
	private GUI gui;
	private GameState state;
	private Integer[] deployArray;
	private ThreadGroup singlePlayer;
	
	public SinglePlayerGame(GUI aGUI, GameState aState, Integer[] aDeployArray, ThreadGroup parentTG) {
		gui = aGUI;
		state = aState;
		deployArray = aDeployArray;
		singlePlayer = new ThreadGroup(parentTG, "SinglePlayer");
	}
	
	public void run() {
		
		// Player deployment
		Runnable playerDeployment = new PlayerDeployment(gui, state, deployArray, singlePlayer);
		runThread(playerDeployment);
		
		// AI deployment
		Runnable aiDeployment = new AIDeployment(gui, state, deployArray, singlePlayer);
		runThread(aiDeployment);
		
		// Gameplay
		Runnable gameplay = new Gameplay(gui, state, singlePlayer);
		runThread(gameplay);		

	}
	
	// starts thread with task given runnable, and doesn't move forward until thread is terminated
	private void runThread(Runnable r) {
		if (Thread.interrupted()) {
			Thread.currentThread().interrupt();
			return;
		}
		
		Thread thread = new Thread(singlePlayer, r);
		thread.start();
		
		try {
			thread.join();
		} catch (Exception e) { 
			Thread.currentThread().interrupt();
			return;
		}
	}
}
