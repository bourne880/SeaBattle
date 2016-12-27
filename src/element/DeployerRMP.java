package element;

import game.Connection;
import game.GameState;
import gui.PlayerBoard;

import java.util.ArrayList;

// listens to player's rollover moves and displays ship position accordingly
// connection dependent - if connection is closed, terminates
public class DeployerRMP extends DeployerR {
	private Connection connection;

	public DeployerRMP(PlayerBoard aBoard, GameState aState, ArrayList<Integer> aDeployList, Connection aConnection) {
		super(aBoard, aState, aDeployList);
		connection = aConnection;
	}
	
	// check if connection is closed, if it is, interrupt
	protected void optionalAction() {
		if (connection.isClosed())
			Thread.currentThread().interrupt();
	}

}
