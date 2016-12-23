package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")

// base class for boards
public abstract class Board extends JPanel {
	protected static Color black = new Color(51, 51, 51);
	protected String lText; // text of label appearing above board
	
	protected HashMap<Integer, JToggleButton> squares; // map of keys 0-99, every key corresponds to single button
	protected int keyR; // key of a button recently rollovered by a player
	protected int keyC; // key of a button recently selected (clicked) by a player
	protected boolean cEnabled; // if value set to true, clicking on squares is enabled; otherwise there is counter selection effect
	protected boolean[] selects; // indexes correspond to keys (buttons), true if it is to be selected
	
	public Board(String aLText) {
		lText = aLText;
		keyR = -1;
		keyC = -1;
		selects = new boolean[100];
	
		Box boardBox = new Box(BoxLayout.Y_AXIS);
		Box bottomBox = new Box(BoxLayout.X_AXIS);
		
		GridLayout grid1 = new GridLayout(1, 11);
		grid1.setHgap(0);
		grid1.setVgap(0);
		JPanel numberArea = new JPanel(grid1);
		GridLayout grid2 = new GridLayout(10, 1);
		JPanel letterArea = new JPanel(grid2);
		GridLayout grid3 = new GridLayout(10,10);		
		JPanel squareArea = new JPanel(grid3);
		
		JLabel label = new JLabel(lText);
		label.setFont(new Font("Dialog", Font.BOLD, 17));
		label.setAlignmentX(CENTER_ALIGNMENT);
		
		String[] letterArray = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
		int[] numberArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		
		numberArea.add(new Label(" "));
		for (int i = 0; i < 10; i++) {
			String j = Integer.toString(numberArray[i]);
			numberArea.add(new Label(j));
		}
		
		for (int i = 0; i < 10; i++) {
			letterArea.add(new Label(letterArray[i]));
		}		
		
		squares = new HashMap<>();
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				JToggleButton b = new JToggleButton("");
				b.setMargin(new Insets(0, 0, 0, 0));
				b.setPreferredSize(new Dimension(30, 30));
				b.setEnabled(false);
				
				b.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent event) {
						buttonRollover(b);
					}
				});
				
				b.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent event) {
						buttonSelected(b);
					}
				});
				
				b.addMouseListener(new MouseListener() {
					public void mouseClicked(MouseEvent event) {
						buttonClicked(event);
					}

					public void mouseEntered(MouseEvent event) { }

					public void mouseExited(MouseEvent event) { }

					public void mousePressed(MouseEvent event) { }
					
					public void mouseReleased(MouseEvent event) { }
					
				});
				
				String number = i + "" + j;
				int count = Integer.parseInt(number);
				squares.put(count, b);
				squareArea.add(b);
			}
		}
		
		bottomBox.add(letterArea);
		bottomBox.add(squareArea);
		boardBox.add(label);
		boardBox.add(numberArea);
		boardBox.add(bottomBox);
		
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(BorderLayout.CENTER, boardBox);
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
	
	public int getKeyR() {
		return keyR;
	}
	
	public int getKeyC() {
		return keyC;
	}
	
	// action listener for button rollover; updates keyR value
	private void buttonRollover(JToggleButton b) {
		ButtonModel model = b.getModel();
		if (model.isRollover())
			keyR = (int) getKeyFromValue(squares, (Object) b);
	}
	
	// action listener for button click (selection), updates keyC
	// if not cEnabled selects/deselects button according to selects value
	private void buttonSelected (JToggleButton b) {
		keyC = (int) getKeyFromValue(squares, (Object) b);
		if (!cEnabled)
			if (selects[keyC])
				squares.get(keyC).setSelected(true);
			else
				squares.get(keyC).setSelected(false);
	}
	
	protected abstract void buttonClicked (MouseEvent event);
	
	public void enableRollover(boolean value) {
		for(int key : squares.keySet())
			squares.get(key).setRolloverEnabled(value);	
	}
	
	public void enableBoard(boolean value) {
		for(int key : squares.keySet())
			squares.get(key).setEnabled(value);	
	}
	
	// selects square (button) given key and updates selects
	public void selectSquare(int key) {
		selects[key] = true;
		cEnabled = true;
		squares.get(key).setSelected(true);
		cEnabled = false;
	}
	
	// returns display symbol for a square given horizontal value
	private static String symbol(boolean horizontal) {
		if (horizontal)
			return "\u2501";
		else
			return "\u2502";
	}
	
	// erases symbols from buttons given key-list
	public void erasePositions(ArrayList<Integer> positions) {
		for (int element : positions)
			squares.get(element).setText("");
	}
	
	// displays ship, with position given list of keys and horizontal value, on board with given color
	public void displayPositions(ArrayList<Integer> positions, boolean horizontal, Color color) {
		for (int element : positions) {
			squares.get(element).setForeground(color);
			squares.get(element).setText(symbol(horizontal));
		}
	}
	
	// displays ship, with position given list of keys and horizontal value, with default color
	public void displayShip(ArrayList<Integer> positions, boolean horizontal) {		
		for (int element : positions) {
			squares.get(element).setForeground(black);
			squares.get(element).setText(symbol(horizontal));
		}
	}
	
	// displays hit on a position given key with orange color if given true
	public void displayHit(int key, boolean color) {
		if (color)
			squares.get(key).setForeground(Color.ORANGE);
		else
			squares.get(key).setForeground(black);
		squares.get(key).setText("X");
	}
	
	// erases symbols and sets default color for squares given eList, deselects squares given dList
	public void eraseBoard(ArrayList<Integer> eList, ArrayList<Integer> dList) {
		for (int key : eList) {
			JToggleButton b = squares.get(key);
			b.setText("");
			b.setForeground(black);
		}			
		
		cEnabled = true;	
		for(int key : dList)
			squares.get(key).setSelected(false);
		cEnabled = false;

		selects = new boolean[100];
		keyR = -1;
		keyC = -1;
	}
		
}
