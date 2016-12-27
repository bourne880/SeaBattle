package element;

import game.GameState;
import gui.PlayerBoard;

import java.util.ArrayList;

// listens to player's click moves and deploys accordingly
public class DeployerC extends PlayerDeployer {
	private int keyCTemp; // recent click position
	
	public DeployerC(PlayerBoard aBoard, GameState aState, ArrayList<Integer> aDeployList) {
		super(aBoard, aState, aDeployList);
		keyCTemp = -1;
	}
	
	public void run() {
		
		while (!Thread.currentThread().isInterrupted() && deployList.size() != 0) { // until all ships are deployed
			if (!keyCTempTest()) {
				ArrayList<Integer> positions = positionsAlgorithm(pBoard.getKeyC(), pBoard.getHorizontal(), deployList.get(0));
				
				if (!deployTest(positions)) {
					state.setPlayerShipsSquares(positions); // add ship to playerShipsSquares (deploy)
					state.setPlayerShips(positions, pBoard.getHorizontal()); // add ship to playerShips
					deployList.remove(0); // remove this size from list to deploy
					keyCTemp = pBoard.getKeyC(); // update keyCTemp
					pBoard.displayShip(positions, pBoard.getHorizontal());
				}
			}
			
			optionalAction();
			
			// sleep thread to make room for another
			try {
				Thread.sleep(50);
			} catch (Exception ex) { 
				Thread.currentThread().interrupt();
				return;
			}
		
		}
		
	}
	
	// returns true/false whether click position differs from recent (did player made a click move)
	private boolean keyCTempTest() {
		return pBoard.getKeyC() == keyCTemp;
	}
	
	// no optional action
	protected void optionalAction() {

	}
		
}
