package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

// includes static methods to operate on files
public class Files {
	private static File dir; // the directory in which game files will be stored
	private static File save; // path to file with save game
	private static File scores;  // path to txt file with best scores
	private static File cData; // path to txt file with connection data;
	
	static {
		dir = new File("files");
		save = new File(dir, "save.sb");
		scores = new File(dir, "scores.txt");
		
		if (!dir.exists()) // create directory if doesn't exist
			try {
				dir.mkdir(); 
			} catch (Exception ex) { }
	}
	
	// given GameState and draw of gameplay, writes gameplay to save
	public static void saveGame(GameState state, int draw) {
		try {
			FileOutputStream connect = new FileOutputStream(save);
			ObjectOutputStream chain = new ObjectOutputStream(connect);	
			chain.writeObject(state);
			chain.writeObject(draw);
			chain.close();
		} catch (Exception ex) { }
	}
	
	// reads gameplay from save, returns array of objects with GameState and draw respectively
	public static Object[] loadGame() {
		Object[] load = new Object[2];
		
		try {
			FileInputStream connect = new FileInputStream(save);
			ObjectInputStream chain = new ObjectInputStream(connect);						
			load[0] = chain.readObject();
			load[1] = chain.readObject();
			chain.close();
		} catch (Exception ex) { }
		
		return load;
	}
	
	// returns true/false whether save file exists
	public static boolean saveExists() {
		return save.exists();
	}
	
	// reads best scores list from scores, than if needed updates list with given score and writes it to scores
	public static void updateScores(int score) {
		ArrayList<Integer> scoreList = new ArrayList<>();
		
		// read scores from file and add them to score list
		try {
			FileReader connect = new FileReader(scores);
			BufferedReader chain = new BufferedReader(connect);
			
			String line = null;
			while ((line = chain.readLine()) != null)
				scoreList.add(Integer.parseInt(line));
			
			chain.close();
		} catch (Exception ex) { }
		
		Collections.sort(scoreList);	
		if (scoreList.size() == 10 && score >= scoreList.get(scoreList.size() - 1)) // if score is not lower than the 10th score return
			return;
		
		scoreList.add(score);
		Collections.sort(scoreList); // add and sort
		
		if (scoreList.size() > 10) // if there are 11 scores now, return the last (highest) score
			scoreList.remove(scoreList.size() - 1);
		
		try { // write scores to file from updated score list
			FileWriter connect = new FileWriter(scores);
			BufferedWriter chain = new BufferedWriter(connect);
			
			for (Integer s : scoreList) {
				chain.write(s.toString());
				chain.newLine();
			}
			
			chain.close();
		} catch (Exception ex) { }
	}
	
	// returns string representation of best scores read from scores
	public static String listScores() {
		StringBuilder sb = new StringBuilder();
		sb.append("Scores listed from the best one:");
		
		int i = 1;
		
		// read scores from file and add them to score list
		try {
			FileReader connect = new FileReader(scores);
			BufferedReader chain = new BufferedReader(connect);
			
			String line = null;
			while ((line = chain.readLine()) != null)
				sb.append("\n" + (i++) + ". " + line);
			
			chain.close();
		} catch (Exception ex) { }
		
		String scoreList = sb.toString();;
		
		return scoreList;
	}
	
	// returns true/false whether scores file exists
	public static boolean scoresExists() {
		return scores.exists();
	}
	
	// writes to temp file given connection data
	public static void saveConnectionData(String playerTCP, String friendIP, String friendTCP) {
		if (cData == null) // create temp file if doesn't exist
			try {
				cData = File.createTempFile("seaBattle-", "-connectionData");
				cData.deleteOnExit(); // to delete temp file on exit
			} catch (Exception ex) { }
		
		try { // write data to file
			FileWriter connect = new FileWriter(cData);
			BufferedWriter chain = new BufferedWriter(connect);
			
			chain.write(playerTCP);
			chain.newLine();
			chain.write(friendIP);
			chain.newLine();
			chain.write(friendTCP);
			chain.newLine();
			
			chain.close();
		} catch (Exception ex) { }
	}
	
	// returns array with connection data, or null if temp file doesn't exist
	public static String[] readConnectionData() {
		if (cData == null)
			return null;
		
		String[] cArray = new String[3];
		
		// read connection data from temp file and add them to array
		try {
			FileReader connect = new FileReader(cData);
			BufferedReader chain = new BufferedReader(connect);
			
			for (int i = 0; i < 3; i++)
				cArray[i] = chain.readLine();
			
			chain.close();
		} catch (Exception ex) { }
		
		return cArray;
	}
	
}
