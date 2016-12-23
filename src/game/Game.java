package game;

// includes main method, starts GameExecutor thread
public class Game {

	public static void main (String[] args) {
		Game game = new Game();
		game.start();
	}
	
	private void start() {
		Thread executor = new Thread(new GameExecutor());
		executor.start();	
	}
		
}
