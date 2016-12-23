package phase;

import game.Connection;
import game.GameState;
import gui.GUI;

// deployment of ships by player
public class PlayerDeploymentMP extends PlayerDeployment {
	private Connection connection;

	public PlayerDeploymentMP(GUI aGUI, GameState aState, Integer[] anArray, Connection aConnection, ThreadGroup parentTG) {
		super(aGUI, aState, anArray, parentTG);
		connection = aConnection;
		playerDeploy = new ThreadGroup(parentTG, "PlayerDeploymentMP");
	}
		
	// send data about player's deployment of ships to friend
	protected void optionalAction() {
		connection.setSend('g', state.getPlayerShipsSquaresMap());
		connection.setSend('g', state.getPlayerShips());
		connection.setSend('g', state.getPlayerShipsHorizontal());
	}

}
