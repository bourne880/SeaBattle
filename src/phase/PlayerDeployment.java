package phase;

import element.DeployerC;
import element.DeployerR;
import game.GameState;
import gui.GUI;
import gui.PlayerBoard;

import java.util.ArrayList;
import java.util.Arrays;

// deployment of ships by player
public class PlayerDeployment implements Runnable {
	private GUI gui;
	private PlayerBoard pBoard;
	protected GameState state;
	private Integer[] deployArray;
	protected ThreadGroup playerDeploy;
	
	public PlayerDeployment(GUI aGUI, GameState aState, Integer[] anArray, ThreadGroup parentTG) {
		gui = aGUI;
		pBoard = gui.getPBoard();
		state = aState;
		deployArray = anArray;
		playerDeploy = new ThreadGroup(parentTG, "PlayerDeployment");
	}
	
	public void run() {				
		gui.message(1);
		
		ArrayList<Integer> deployList = new ArrayList<>(Arrays.asList(deployArray));
		state.constructPlayerShips(deployList.size());
			
		pBoard.enableRollover(true); // needs rollover listener
		pBoard.enableBoard(true); // enable player board
		
		Thread thread1 = new Thread(playerDeploy, new DeployerR(pBoard, state, deployList));
		Thread thread2 = new Thread(playerDeploy, new DeployerC(pBoard, state, deployList));
			
		thread1.start(); // listens to player's rollover moves and displays ship position accordingly
		thread2.start(); // listens to player's click moves and deploys
			
		try {
			thread2.join();
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			return;
		}
		
		optionalAction();
		
		// wait 1 sec to show deployment results
		try { 
			Thread.sleep(1000);
		} catch (Exception ex) {
			Thread.currentThread().interrupt();
			return;
		}
			
		pBoard.enableRollover(false); // turns off rollover listener
		pBoard.enableBoard(false); // disable player board
			
	}
	
	// no optional action
	protected void optionalAction() {
		
	}
	
}
