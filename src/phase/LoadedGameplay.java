package phase;

import game.GameState;
import gui.GUI;

// continues the gameplay based on GameState and draw of loaded game
public class LoadedGameplay extends Gameplay {
		
	public LoadedGameplay(GUI aGUI, GameState aState, int aDraw, ThreadGroup parentTG) {
		super(aGUI, aState, parentTG);
		draw = aDraw; // draw value from loaded gameplay
		gameplay = new ThreadGroup(parentTG, "LoadedGameplay");
	}
	
	 // no communicate, just enable save game option in menu
	protected void communicate() {
		gui.getMenu().enableItem(2, true);
	}
	
	// starts the game with computer player move or not based on the GameState and draw of loaded gameplay
	protected void startingPlayer() {
		
		if ((draw == 0 && state.getEnemyMoves().size() == state.getPlayerMoves().size()) || 
				(draw == 1 && state.getPlayerMoves().size() > state.getEnemyMoves().size())) {
				enemyMove();
			}
	}
	
}
