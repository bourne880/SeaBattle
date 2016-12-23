package element;

import game.GameState;
import gui.Board;
import gui.PlayerBoard;

import java.util.ArrayList;

// base class for player deployers
public abstract class PlayerDeployer extends Deployer {
	
	protected static PlayerBoard pBoard;
	
	public PlayerDeployer(Board aBoard, GameState aState, ArrayList<Integer> aDeployList) {
		super(aState, aDeployList);
		pBoard = (PlayerBoard) aBoard;
	}
	
	public abstract void run();
	
	protected boolean deployTest(ArrayList<Integer> positions) {
		for (int element : positions)
			if (state.playerShipsSquaresContains(element))
				return true;
		return false;
	}
	
}
