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
	protected PlayerBoard pBoard;
	protected GameState state;
	private Integer[] deployArray;
	protected Thread rolloverT; // listens to player's rollover moves and displays ship position accordingly
	protected Thread clickT; // listens to player's click moves and deploys
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
		
		threadsDeclaration(deployList);
			
		rolloverT.start();
		clickT.start();
			
		try {
			clickT.join();
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
	
	protected void threadsDeclaration(ArrayList<Integer> deployList) {
		rolloverT = new Thread(playerDeploy, new DeployerR(pBoard, state, deployList));
		clickT = new Thread(playerDeploy, new DeployerC(pBoard, state, deployList));
	}
	
	// no optional action
	protected void optionalAction() {
		
	}
	
}
