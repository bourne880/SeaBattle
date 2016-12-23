package gui;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.*;

// creates graphical user interface
public class GUI {
	private JFrame mainFrame;
	private DCardLayout bPanelLayout; // custom CardLayout for mainFrame
	private JPanel bPanel;
	private Intro intro; // intro panel
	private MessageBox mBox; // panel displaying communicates to player
	private PlayerBoard pBoard;
	private EnemyBoard eBoard;
	private Component emptySpace; // empty space between boards and messenger in multi player mode
	private Messenger messenger; // messenger in multi player mode
	private Menu menu; // menu bar
	
	public GUI() {
		mainFrame = new JFrame("Sea Battle");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		bPanelLayout = new DCardLayout();
		bPanel = new JPanel(bPanelLayout);
		bPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));		
		
		intro = new Intro();
		bPanel.add(intro, "1");
		
		JPanel gamePanel = new JPanel();
		gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
		
		mBox = new MessageBox();
		gamePanel.add(mBox);
		gamePanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.X_AXIS));
		pBoard = new PlayerBoard("My Ships");
		eBoard = new EnemyBoard("Opponent's Ships");
		boardPanel.add(pBoard);
		boardPanel.add(eBoard);
		gamePanel.add(boardPanel);
		
		emptySpace = Box.createRigidArea(new Dimension(0, 20));
		emptySpace.setVisible(false);
		messenger = new Messenger();
		gamePanel.add(emptySpace);
		gamePanel.add(messenger);
		
		bPanel.add(gamePanel, "2");
		
		menu = new Menu();
				
		mainFrame.setContentPane(bPanel);
		mainFrame.setJMenuBar(menu);
		mainFrame.setBounds(50,50,512,736);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);	
	}
	
	public PlayerBoard getPBoard() {
		return pBoard;
	}
	
	public EnemyBoard getEBoard() {
		return eBoard;
	}
	public Menu getMenu() {
		return menu;
	}
	
	// displays new window with user interface enabling connection
	public ConnectionWindow getCWindow() {
		return new ConnectionWindow(mainFrame);
	}
	
	public Messenger getMessenger() {
		return messenger;
	}
	
	// given true/false value displays/hides Messenger
	public void setMessengerVisible(boolean value) {
		emptySpace.setVisible(value);
		messenger.setVisible(value);
		mainFrame.pack();
	}
		
	// switches to next panel in CardLayout
	public void nextCard() {
		bPanelLayout.next(bPanel);
		menu.setVisible(true);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null); // centers the window on the screen
	}
	
	// switches to communicate given number
	public void message(int i) {
		mBox.message(i);
	}
					
}