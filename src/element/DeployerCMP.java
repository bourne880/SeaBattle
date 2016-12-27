package element;

import game.Connection;
import game.GameState;
import gui.PlayerBoard;

import java.util.ArrayList;

// listens to player's click moves and deploys accordingly
// connection dependent - if connection is closed, terminates
public class DeployerCMP extends DeployerC {
	private Connection connection;

	public DeployerCMP(PlayerBoard aBoard, GameState aState, ArrayList<Integer> aDeployList, Connection aConnection) {
		super(aBoard, aState, aDeployList);
		connection = aConnection;
	}
	
	// check if connection is closed, if it is, interrupt
	protected void optionalAction() {
		if (connection.isClosed())
			Thread.currentThread().interrupt();
	}

}
