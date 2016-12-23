package game;

import element.DeployerR;
import gui.Board;
import gui.GUI;
import gui.Menu;
import phase.Gameplay;
import run.LoadedGame;
import run.MultiPlayerGame;
import run.SinglePlayerGame;

import java.util.ArrayList;
import javax.swing.JOptionPane;



// initializes gui and state, starts the game with single player game
// listens to player's action from Menu and performs tasks according to player's choices:
// - executes runs (before that, interrupts previous run and brings back board and menu to default state)
// - saves or loads game
// - displays best scores
public class GameExecutor implements Runnable {
	private GUI gui;
	private Menu menu;
	private GameState state;
	private static final Integer[] DEPLOY_ARRAY = { 5, 4, 3, 2, 1 }; //size and order of ships for players to deploy
	private boolean indicatorTemp; // recent value of indicator (Menu)
	private ThreadGroup run;
	
	public GameExecutor() {
		gui = new GUI();
		menu = gui.getMenu();
		state = new GameState();
		run = new ThreadGroup("Run");
		
		pastIntro();
		setupMenu();
		sRun(); // default run start
	}
	
	public void run() {
		while (true) {	
			if (!indicatorTempTest()) {
				int choice = menu.getChoice();
				boolean mode = menu.getMode();

				switch (choice) {
					case 1:
						if (mode) {
							interruptRun();
							clearBoards();
							setupMenu();
							
							state = new GameState();
							sRun();
						}
						else {
							interruptRun();
							clearBoards();
							setupMenu();
							
							state = new GameState();
							mRun();
						}
						break;
					case 2:
						// save game to file
						Files.saveGame(state, Gameplay.getDraw());
						// enable load in menu, if previously disabled
						if (!menu.itemEnabled(3)) menu.enableItem(3, true);
						break;
					case 3:
						interruptRun();
						clearBoards();
						
						// load game from file
						Object[] load = Files.loadGame();
						state = (GameState) load[0];
						int draw = (int) load[1];
						
						lRun(draw);
						break;
					case 4:
						// display best scores
						JOptionPane.showMessageDialog(null, Files.listScores(), "Best scores", JOptionPane.PLAIN_MESSAGE);
						break;
					case 5:
						System.exit(0);
						break;		
				}
				
				indicatorTemp = menu.getIndicator();
			}
			
			try {
				Thread.sleep(50);
			} catch (Exception ex) { }
		}
	}
	
	// switches to main card after display of Intro
	private void pastIntro() {
		// 3 seconds for Intro (first card) to appear
		try {
			Thread.sleep(3000);
		} catch (Exception ex) { }
		
		gui.nextCard(); // switches to second card (past Intro)
	}
	
	// returns true/false whether indicator value differs from recent (did player made a menu choice)
	private boolean indicatorTempTest() {
		return menu.getIndicator() == indicatorTemp;
	}
	
	// interrupts current run of the game, waits until all threads are terminated
	// closes connection if there is an open one
	private void interruptRun() {
		run.interrupt();

		while (run.activeCount() != 0)
			try {
				Thread.sleep(5);
			} catch (Exception ex) { }
		
		// close connection if not closed
		Connection connection = Connect.getConnection();
		if (connection != null && !connection.isClosed())
			connection.close();
	}
	
	// brings back boards to default state
	private void clearBoards() {
		ArrayList<Integer> eList = new ArrayList<>();
		ArrayList<Integer> dList = new ArrayList<>();
		
		Board pBoard = gui.getPBoard();		
		pBoard.enableBoard(true);
		
		eList.addAll(DeployerR.getPositionsTemp());
		eList.addAll(state.getPlayerShipsSquares());
		dList.addAll(state.getEnemyMoves());
		pBoard.eraseBoard(eList, dList);
		pBoard.enableBoard(false);
		
		eList.clear();
		dList.clear();
		
		Board eBoard = gui.getEBoard();
		eBoard.enableBoard(true);
		
		eList.addAll(state.getEnemyShipsSquares());
		dList.addAll(state.getPlayerMoves());
		eBoard.eraseBoard(eList, dList);
		eBoard.enableBoard(false);
	}
	
	// enables and disables menu items for a default game (single player)
	private void setupMenu() {
		menu.enableItems();
		
		menu.enableItem(2, false);
		
		// if multi player game, disable save, load, scores
		if (!menu.getMode()) {
			menu.enableItem(2, false);
			menu.enableItem(3, false);
			menu.enableItem(4, false);
			return;
		}
		
		// if no save file, disable save game
		if (!Files.saveExists())
			menu.enableItem(3, false);
		
		// if no scores files, disable best scores
		if (!Files.scoresExists())
			menu.enableItem(4, false);
	}
	
	// run of single player game
	private void sRun() {
		Thread thread = new Thread(run, new SinglePlayerGame(gui, state, DEPLOY_ARRAY, run));
		thread.start();
	}
	
	// run of loaded game
	private void lRun(int draw) {		
		Thread thread = new Thread(run, new LoadedGame(gui, state, draw, run));
		thread.start();
	}
	
	// run of multi player game
	private void mRun() {
		Thread thread = new Thread(run, new MultiPlayerGame(gui, state, DEPLOY_ARRAY, run));
		thread.start();
	}
	
}
