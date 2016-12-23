package element;

import game.Connection;
import game.GameState;
import gui.GUI;

// lets player to make a move, processes only a proper one, then displays effects on board
// connection dependent - if connection is closed, terminates
public class PlayerMoveMP extends PlayerMove {
	private Connection connection;

	public PlayerMoveMP(GUI aGUI, GameState aState, Connection aConnection) {
		super(aGUI, aState);
		connection = aConnection;
	}
	
	// sleep thread if connection is not closed
	protected void sleep() {
		if (!connection.isClosed())
			super.sleep();
	}
	
	// connection can't be closed
	protected boolean optionalCondition() {
		return !connection.isClosed();
	}
	
	// sends player's move to friend's game
	protected void optionalAction() {
		connection.setSend('g', state.getPlayerMoves().get(state.getPlayerMoves().size() - 1));
	}

}
