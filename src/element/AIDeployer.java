package element;

import game.GameState;

import java.util.ArrayList;

// deploys ships for computer player
public class AIDeployer extends Deployer {	
	private int key;
	private boolean horizontal;
	
	public AIDeployer(GameState aState, ArrayList<Integer> aDeployList) {
		super(aState, aDeployList);
		key = -1;
		horizontal = false;
	}
	
	public void run() {
		
		while (!Thread.interrupted() && deployList.size() != 0) {
			key = (int) (Math.random() * 100);
			horizontal = randomiseH();
			
			ArrayList<Integer> positions = positionsAlgorithm(key, horizontal, deployList.get(0));
				
			if (!deployTest(positions)) {
				
				state.setEnemyShipsSquares(positions); // add ship to enemyShipsSquares (deploy)
				state.setEnemyShips(positions, horizontal); // add ship to enemyShips
				deployList.remove(0); // remove this size from list to deploy
			}		
		}
	}
	
	protected boolean deployTest(ArrayList<Integer> positions) {
		positions = addNeighbours(positions); // neighbouring squares included in the test
		
		for (int element : positions)
			if (state.enemyShipsSquaresContains(element))
				return true;
		return false;
	}
	
	// returns true/false with 0.5 probability
	private boolean randomiseH() {
		return Math.random() >= 0.5;
	}
	
	// adds neighbouring squares to ship position
	private ArrayList<Integer> addNeighbours(ArrayList<Integer> positions) {
		ArrayList<Integer> list = new ArrayList<>();
		list.addAll(positions);
		int size = list.size();
		int fKey = list.get(0);
		int lKey = list.get(list.size() - 1);
		
		int[] digits = numberToDigits(key);
						
		if (!horizontal) {
			list.add(fKey - 10);
			list.add(lKey + 10);
			
			for (int k = 0, l = 0; k < size + 2; k++) {
				if (digits[1] != 0)
					list.add(fKey - 11 + l);
				if (digits[1] != 9)
					list.add(fKey - 9 + l);
				l += 10;
			}
		}
		else {
			if (digits[1] != 0) list.add(fKey - 1);
			if (digits[1] != 9) list.add(lKey + 1);
			
			for (int k = 0; k < size + 2; k++) {
				list.add(fKey - 11 + k);
				list.add(fKey + 9 + k);
			}
		}
		
		return list;
	}

}
