package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

@SuppressWarnings("serial")

// the menu bar
public class Menu extends JMenuBar implements ActionListener {
	private HashMap<Integer, JMenuItem> items; // map of keys 1-7, every key corresponds to single menu item
	private boolean indicator; // changes value if user makes a choice in game menu
	private int choice; // recent choice in game menu (1-5)
	private boolean mode; // recent choice in opponent menu (true for single)
	private static int messageDisplays; // number of info message displays
	
	public Menu() {
		items = new HashMap<>();
		
		JMenu game = new JMenu("Game");
		
		JMenuItem newGame = new JMenuItem("New game");
		newGame.addActionListener(this);
		items.put(1, newGame);
		
		JMenuItem saveGame = new JMenuItem("Save game");
		saveGame.addActionListener(this);
		items.put(2, saveGame);
		
		JMenuItem loadGame = new JMenuItem("Load game");
		loadGame.addActionListener(this);
		items.put(3, loadGame);
		
		JMenuItem scores = new JMenuItem("Best scores");
		scores.addActionListener(this);
		items.put(4, scores);
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		items.put(5, exit);
		
		game.add(newGame);
		game.addSeparator();
		game.add(saveGame);
		game.add(loadGame);
		game.addSeparator();
		game.add(scores);
		game.addSeparator();
		game.add(exit);
		
		this.add(game);
		
		JMenu mode = new JMenu("Mode");
		
		JRadioButtonMenuItem single = new JRadioButtonMenuItem("Single player");
		single.setSelected(true);
		single.addActionListener(this);
		items.put(6, single);
		
		JRadioButtonMenuItem multi = new JRadioButtonMenuItem("Multi player");
		multi.addActionListener(this);
		items.put(7, multi);
		
		ButtonGroup group = new ButtonGroup();
		group.add(single);
		group.add(multi);
		
		mode.add(single);
		mode.add(multi);
				
		this.add(mode);
		
		this.setVisible(false);
		
		this.mode = true;
	}
	
	// returns key assigned to value (given object) in given map or null if there is no key assigned to this object
	private static Object getKeyFromValue(Map<? extends Object, ? extends Object> m, Object value) {
		for (Object o : m.keySet()) {
			if (m.get(o).equals(value)) {
				return o;
			}
	    }
	    return null;
	}
	
	public boolean getIndicator() {
		return indicator;
	}
	
	public int getChoice() {
		return choice;
	}
	
	public boolean getMode() {
		return mode;
	}
	
	// listener, changes values of choice and mode according to player selections;
	// changes value of indicator when player makes a menu action choice
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		int key = (int) getKeyFromValue(items, source);
		
		if (key >= 1 && key <= 5) {
			choice = key;
			indicator = !indicator;
		}
		else if (key == 6)
			mode = true;
		else if (key == 7)
			mode = false;
		
		if (messageDisplays == 0 && (key == 6 || key == 7)) {
			JOptionPane.showMessageDialog(null, "Changing mode won't have any effect unless you start a new game.");
			messageDisplays++;
		}
	}
	
	public boolean itemEnabled(int key) {
		return items.get(key).isEnabled();
	}
	
	// enables all menu items
	public void enableItems() {
		for (int key : items.keySet())
			items.get(key).setEnabled(true);
	}
	
	// enables/disables menu item given key depending on the given value
	public void enableItem(int key, boolean value) {
		items.get(key).setEnabled(value);
	}

}
