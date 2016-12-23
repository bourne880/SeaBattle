package phase;

import element.AIDeployer;
import game.GameState;
import gui.EnemyBoard;
import gui.GUI;

import java.util.ArrayList;
import java.util.Arrays;

// deployment of ships by computer enemy
public class AIDeployment implements Runnable {
	private GUI gui;
	private EnemyBoard eBoard;
	private GameState state;
	private static Integer[] deployArray;
	private ThreadGroup aiDeploy;
	
	public AIDeployment(GUI aGUI, GameState aState, Integer[] anArray, ThreadGroup parentTG) {
		gui = aGUI;
		eBoard = gui.getEBoard();
		state = aState;
		deployArray = anArray;
		aiDeploy = new ThreadGroup(parentTG, "AIDeployment");
	}
	
	public void run() {	
		gui.message(2);
		
		ArrayList<Integer> deployList = new ArrayList<>(Arrays.asList(deployArray));
		state.constructEnemyShips(deployList.size());
		
		eBoard.enableBoard(true); // enable enemy board
		
		Thread thread = new Thread(aiDeploy, new AIDeployer(state, deployList));
		thread.start(); // deploys computer ships
		
		try {
			thread.join();
		} catch (Exception e) { 
			Thread.currentThread().interrupt();
			return;
		}
		
		// wait 3 sec to give an impression that computer takes time deploying
		try {
			Thread.sleep(3000);
		} catch (Exception ex) {
			Thread.currentThread().interrupt();
			return;
		}
		
		eBoard.enableBoard(false); // disable enemy board
			
	}

}
