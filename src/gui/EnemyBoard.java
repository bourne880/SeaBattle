package gui;

import java.awt.event.MouseEvent;


@SuppressWarnings("serial")

// enemy's board
public class EnemyBoard extends Board {
	
	public EnemyBoard(String aLText) {
		super(aLText);
	}
	
	public void setKeyC(int key) {
		keyC = key;
	}
	
	// no button listener
	protected void buttonClicked(MouseEvent event) { }

}
