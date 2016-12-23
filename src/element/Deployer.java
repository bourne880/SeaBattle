package element;

import game.GameState;

import java.util.ArrayList;

// base class for deployers
public abstract class Deployer implements Runnable {	
	protected GameState state;
	protected ArrayList<Integer> deployList;
	
	public Deployer(GameState aState, ArrayList<Integer> aDeployList) {
		state = aState;
		deployList = aDeployList;
	}
	
	public abstract void run();
	
	public ArrayList<Integer> getDeployList() {
		return deployList;
	}
	
	// returns true if current deploy position crosses any of previously deployed ships (crossing test)
	protected abstract boolean deployTest(ArrayList<Integer> positions);
	
	// returns corrected ship position (including the borders in calculations) given rollover position, horizontal value, and ship size 
	protected static ArrayList<Integer> positionsAlgorithm(int key, boolean horizontal, int s) {
		ArrayList<Integer> positions = new ArrayList<>();
		
		int[] digits = numberToDigits(key);
		
		int m;
		int n = 0;
		
		if (horizontal)
			m = digits[1]+s-1;
		else
			m = digits[0]+s-1;
		
		while (m > 9) {
			m--;
			n++;
		}
		
		String number;
		
		for (int l = n; l > 0; l--) {
			if (horizontal)
				number = digits[0] + "" + (digits[1]-l);
			else
				number = (digits[0]-l) + "" + digits[1];
			positions.add(Integer.parseInt(number));
		}
		for (int k = 0; k < s-n; k++) {
			if (horizontal)
				number = digits[0] + "" + (digits[1]+k);
			else
				number = (digits[0]+k) + "" + digits[1];
			positions.add(Integer.parseInt(number));
		}
		
		return positions;
	}
	
	// given number of max 2 digits, returns array of 2 digits
	protected static int[] numberToDigits(int number) {
		String ij = String.valueOf(number);
		char[] digits = ij.toCharArray();
		int[] array = new int[2];
		
		if (number > 9) {
			array[0] = Character.getNumericValue(digits[0]);;
			array[1] = Character.getNumericValue(digits[1]);
		}
		else {
			array[0] = 0;
			array[1] = Character.getNumericValue(digits[0]);
		}

		return array;
	}

}
