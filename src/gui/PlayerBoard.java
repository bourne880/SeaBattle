package gui;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

@SuppressWarnings("serial")

// player's board
public class PlayerBoard extends Board {
	
	private boolean horizontal = false; // false if ship position chosen vertical, true for horizontal
	
	public PlayerBoard(String aLText) {
		super(aLText);
	}
	
	// listener, changes horizontal value when users makes a right click on any button
	protected void buttonClicked (MouseEvent event) {
		if(SwingUtilities.isRightMouseButton(event))
			if(horizontal)
				horizontal=false;
			else
				horizontal=true;
	}
	
	public boolean getHorizontal() {
		return horizontal;
	}
	
	public void eraseBoard(ArrayList<Integer> eList, ArrayList<Integer> dList) {
		super.eraseBoard(eList, dList);
		horizontal = false;
	}
	
}
