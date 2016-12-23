package element;

import game.GameState;
import gui.PlayerBoard;

import java.awt.Color;
import java.util.ArrayList;

// listens to player's rollover moves and displays ship position accordingly
public class DeployerR extends PlayerDeployer {
	private int keyRTemp; // recent rollover position
	private boolean horizontalTemp; // recent horizontal value
	private static ArrayList<Integer> positionsTemp; // recent display positions
	
	public DeployerR(PlayerBoard aBoard, GameState aState, ArrayList<Integer> aDeployList) {
		super(aBoard, aState, aDeployList);
		keyRTemp = -1;
		horizontalTemp = false;
		positionsTemp = new ArrayList<Integer>();
	}

	public void run() {

		while (!Thread.currentThread().isInterrupted() && deployList.size() != 0) { // until all ships are deployed
			if (!keyRTempTest() || !horizontalTempTest()) {
				ArrayList<Integer> positions = positionsAlgorithm(pBoard.getKeyR(), pBoard.getHorizontal(), deployList.get(0));
				
				Color color;
				if (deployTest(positions))
					color = Color.red; // ship color displayed set to red if crosses already deployed
				else
					color = Color.blue; // ship color displayed set to blue otherwise
				
				for (ArrayList<Integer> list : state.getPlayerShips())  // display player ships (deployed) if there are any
					if (!list.isEmpty())
						pBoard.displayShip(list, state.getPlayerShipHorizontal(list));
				
				positionsTemp.removeAll(state.getPlayerShipsSquares()); // make sure to not erase deployed ships positions
				pBoard.erasePositions(positionsTemp); // erase positions of previously displayed ship
				pBoard.displayPositions(positions, pBoard.getHorizontal(), color); // display positions of ship to deploy
				positionsTemp.clear();
				positionsTemp.addAll(positions);
				keyRTemp = pBoard.getKeyR(); // update keyRTemp
				horizontalTemp = pBoard.getHorizontal(); // update horizontalTemp
			}
			
			// sleep thread to make room for another
			try {
				Thread.sleep(50);
			} catch (Exception ex) {
				Thread.currentThread().interrupt();
				return;
			}
			
		}
		
		 // erase positions of last displayed ship if there was a display after last deployment
		if (!state.getPlayerShipsSquares().containsAll(DeployerR.getPositionsTemp()))
			pBoard.erasePositions(DeployerR.getPositionsTemp());	
	}
	
	// returns true/false whether rollover position differs from recent (did player made a rollover move)
	private boolean keyRTempTest() {
		return pBoard.getKeyR() == keyRTemp;
	}
	// returns true/false whether horizontal value differs from recent (did player made a right click move)
	private boolean horizontalTempTest() {
		return pBoard.getHorizontal() == horizontalTemp;
	}
	// returns recent display positions
	public static ArrayList<Integer> getPositionsTemp() {
		return positionsTemp;
	}

}
