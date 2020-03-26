import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Map;

/**
 * Main driver class to run game.
 */
public class Game
{ 
	private static Player currentPlayer;
	private static Cryptogram cryptogram;
	private static Boolean ifSave = false;

	private static HashMap<Character, Character> userGuesses;
	private static String userName;
	private static int currentCharIndex;

	/** 
	 * Class constructor.
	 */
	public Game()
	{
		userGuesses = new HashMap<Character, Character>();
		currentCharIndex = 0;
	}

	/**
	 * Starts the game and gets input from terminal.
	 * @throws IOException 
	 */
	private void start() throws IOException 
	{
		System.out.println("\nHello " + currentPlayer.getUsername() + ", welcome to our cryptogram program!");
		System.out.println("You can type the following commands:\n- \"undo\"\n- \"clear\"\n- \"answer\"\n- \"top10\"\n- \"stats\"\n- \"name\"\n- \"save\"\n- \"load\"\n- \"exit\"\n ");
		System.out.println("Please type \"generate\" to start...\n");

		Scanner input = new Scanner(System.in);
		String userInput = input.nextLine();
		//input.close();

		readInput(userInput, false);

		while (!userInput.equals("generate"))
		{
			System.out.println("");
			System.out.println("Please type \"generate\" to start...\n");
			userInput = input.nextLine();

			readInput(userInput, false);
		}	

		System.out.println();

		while (!completed())
		{
			System.out.print("\nYour cryptogram is:");
			System.out.println("\n" + cryptogram.getEncryptedPhrase());
			getMapping();

			System.out.println("Please enter a singular character value for cryptogram character \'" + cryptogram.getEncryptedChar(currentCharIndex) + "\'...\n");

			//input = new Scanner(System.in);
			userInput = input.nextLine();
			
			char userGuess = ' ';
			boolean inputRead = readInput(userInput, true);
			
			if (!inputRead && (userInput.length() > 1 || userInput.length() == 0)) 
			{
				System.out.println("Invalid input. Please enter a singular character value for cryptogram character \'" + cryptogram.getEncryptedChar(currentCharIndex) + "\'...\n");
			}
			else if (!inputRead)
			{
				userGuess = userInput.charAt(0);
				userGuesses.put(cryptogram.getEncryptedChar(currentCharIndex), userGuess);
				char x6 = Character.toLowerCase(cryptogram.getChar(currentCharIndex));
				
				if (userGuess == x6) 
				{
					currentPlayer.incrementCorrectGuesses();
				}

				currentCharIndex++;
				currentPlayer.incrementTotalGuesses();
				
			}
		}
		input.close();
	}
	
	/**
	 * Gets user input from terminal.
	 * @throws IOException 
	 */
	private boolean readInput(String userInput, boolean inGame) throws IOException 
	{
		if (userInput.equals("undo"))
		{
			if (inGame)
			{
				undo();
				return true;
			}
			else
			{
				System.out.println("You must be solving a cryptogram in order to use this command.");
			}
		}
		else if (userInput.equals("clear"))
		{
			if (inGame)
			{
				clear();
				return true;
			}
			else
			{
				System.out.println("You must be solving a cryptogram in order to use this command.");
			}
		}
		else if (userInput.equals("answer"))
		{
			if (inGame)
			{
				answer();
				return true;
			}
			else
			{
				System.out.println("You must be solving a cryptogram in order to use this command.");
			}
		}
		else if (userInput.equals("stats"))
		{
			showStats(currentPlayer);
			return true; 
		}	
		else if (userInput.equals("name"))
		{
			changeUserName();
			return true;
		}
		else if (userInput.equals("exit"))
		{
			exit();
		}
		else if (userInput.equals("save"))
		{
			save();
			return true;
		}
		else if (userInput.equals("load"))
		{
			loadGame(userGuesses);
			return true;
		}
		else if (userInput.equals("top10"))
		{
			getTop10();
			return true;
		}
		return false;
	}

	/**
	 * Prints out the generated cryptogram.
	 */
	private void getMapping() 
	{
		if (currentCharIndex == 0) 
		{
			System.out.println();
		}
		else
		{
			for (int i = 0; i < cryptogram.getEncryptedPhrase().length(); i++) 
			{
				System.out.print('-');
			}

			System.out.println();

			for (char c : cryptogram.getEncryptedPhrase().toCharArray()) 
			{
				char output = userGuesses.containsKey(c) ? userGuesses.get(c) : ' ';
				System.out.print(Character.toUpperCase(output));
			}
		
			System.out.println("\n");
		}
	}

	/**
	 * Undoes an action.
	 */
	private void undo() 
	{
		if (currentCharIndex == 0)
		{
			System.out.println("You cannot undo any more actions.");
		}
		else
		{
			currentCharIndex--;
			userGuesses.remove(cryptogram.getEncryptedChar(currentCharIndex));
			System.out.println("Last action undone.");
		}
	}
	
	/**
	 * Undoes all actions.
	 */
	
	private void clear() 
	{
		System.out.println("All actions cleared.");
		currentCharIndex = 0;
		userGuesses.clear();
	}
	
	/**
	 * Gives the answer to the cryptogram.
	 */
	
	private void answer() 
	{
		System.out.println("The answer was: " + cryptogram.getPhrase());
		currentPlayer.incrementPlayedCryptograms();	
		completesave();
		
	}
	
	/**
	 * Gives the player a hint by giving them one letters mapping.
	 */
	
	@SuppressWarnings("null")
	private void getTop10() 
	{
        try { 
        	String[] Names = null;
        	Integer[] Scores = null;
            File f = new File("Players"); 
            Player player = null;
            ArrayList<Player> allPlayers = new ArrayList();
            
            Map<String, Integer> map = new HashMap<String, Integer>();
  
            // Get all the names of the files present 
            // in the directory 
            File[] files = f.listFiles(); 
  
            System.out.println("Files are:"); 
  
            for (int i = 0; i < files.length; i++) { 
            	if(files[i].getName().equals("prevGame")) {
            		//do nothing with this file
            	}
            	else {
	            		System.out.println(files[i].getName()); 
	            		String fileName = "Players/" + files[i].getName();
	                	ArrayList<String> playerData = new ArrayList(Files.readAllLines(Paths.get(fileName)));
	                	String userName = playerData.get(0);
	                	int completedCryptos = Integer.parseInt(playerData.get(4));
	                	System.out.println(userName + " has completed: " + completedCryptos + " cryptograms");
                	
                	
	
            			//Converts Strings read from file to ints 
            			int x = Integer.parseInt(playerData.get(1));
            			int x1 = Integer.parseInt(playerData.get(2));
            			int x2 = Integer.parseInt(playerData.get(3));
            			int x3 = Integer.parseInt(playerData.get(4));
            			
            			// Constructs the current player object with information from file
            			player = new Player(playerData.get(0),x,x1,x2,x3); 
            			allPlayers.add(player);
            		}
           } 
        System.out.println("USERNAME IS: " + allPlayers.get(0).getUsername());
        System.out.println("USERNAME IS: " + allPlayers.get(1).getUsername());
        Collections.sort(allPlayers);
        
        for(Player str: allPlayers) {
        	System.out.println(str);
        }
        
        } 
        catch (Exception e) { 
            System.err.println(e.getMessage()); 
        } 
		
		
	}

	/**
	 * Shows the current player's statistics in tabular form.
	 */
	private static void showStats(Player player) 
	{
		System.out.println("\nHello, " + currentPlayer.getUsername() + ".");
		System.out.println("+----------------------+------+");
		System.out.println("| Stats                | Val. |");
		System.out.println("+----------------------+------+");
		System.out.println("+ This game: ----------+------+");
		System.out.format("| %-20s | %-4s |", "Guesses:", player.getTotalGuesses()); System.out.println();
		System.out.format("| %-20s | %-4s |", "Correct guesses", player.getCorrectGuesses()); System.out.println();
		System.out.format("| %-20s | %-4s |", "Accuracy:", (Integer.toString((int)player.getAccuracy()) + "%")); System.out.println();
		System.out.println("+ All games: ----------+------+");
		System.out.format("| %-20s | %-4s |", "Correct sessions:", player.getCompletedCryptograms()); System.out.println();
		System.out.format("| %-20s | %-4s |", "Completed sessions:", player.getPlayedCryptograms()); System.out.println();
		System.out.println("+----------------------+------+");
	}

	/**
	 * Changes username of user depending on input.
	 */
	private void changeUserName() 
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Your name is " + currentPlayer.getUsername());
		System.out.println("Would you like to change it? [Y/N]");
		String userChoice = input.nextLine();

		if (userChoice.toUpperCase().equals("Y")) 
		{
			System.out.println("Please enter a new name...\n");
			String userName = input.nextLine();
			currentPlayer.setUsername(userName);
			System.out.println("Name changed to: \"" + currentPlayer.getUsername() + "\".\n");
		}
		else
		{
			System.out.println("Name has not beem changed.\n");
		}
	}	

	/**
	 * Determines whether user has completed the game.
	 */
	private boolean completed()
	{
		if (currentCharIndex == cryptogram.getUniqueChars().size())
		{
			if (cryptogram.getCompletionStatus(userGuesses))
			{
				System.out.println("\n" + cryptogram.getEncryptedPhrase());
				getMapping();
				System.out.println("You have successfully completed the cryptogram!");
				currentPlayer.incrementPlayedCryptograms();
				currentPlayer.incrementCompletedCryptograms();	
				completesave();
			}
			else 
			{
				System.out.println("\nYou have failed to complete the cryptogram. All values will be now be reset...");
				currentCharIndex = 0;
				userGuesses.clear();
				currentPlayer.incrementPlayedCryptograms();	
			}
		}

		return false;
	}

	private void completesave() {
		String fileName  = "Players/" + userName;
		try 
		{
			clearFile(fileName);
			writeToFile(fileName, currentPlayer.getUsername());
			writeToFileInt(fileName, currentPlayer.getCorrectGuesses());
			writeToFileInt(fileName, currentPlayer.getTotalGuesses());
			writeToFileInt(fileName, currentPlayer.getPlayedCryptograms());
			writeToFileInt(fileName, currentPlayer.getCompletedCryptograms());
			System.exit(0);
		}
		catch (IOException e) // If file not found
		{
			System.out.println("\nCannot find players files to save data lost all current data is lost.");
			System.exit(0);
		}
		
	} 
	
	private void save() {
		Scanner input = new Scanner(System.in);
		String userChoice = "";
		if (ifSave) {
		System.out.println("Would u like to save your game before exiting it will overwrite previous save [Y/N]");
		userChoice = input.nextLine();
		}
		if (userChoice.toUpperCase().equals("Y") || ifSave == false) {
		try 
		{
			String s = "";
			String t = "";
			for(Character c : userGuesses.keySet()) {
			s = s +c;
			t = t + userGuesses.get(c);
			}
			String ecrypt =cryptogram.getEncryptedPhrase();
			String ecryptNoWhiteSpace = ecrypt.replaceAll("\\s","");
			clearFile("Players/prevGame");
			writeToFile("Players/prevGame",cryptogram.getPhrase());
			writeToFile("Players/prevGame",ecryptNoWhiteSpace);
			writeToFile("Players/prevGame",s);
			writeToFile("Players/prevGame",t);
			ifSave = true;
		}
		catch (IOException e) // If file not found
		{
			System.out.println("\nCannot find previous game file");
		}
		}
		
	}

	/**
	 * Runs when the game ends. Writes data of one player to a file.
	 */
	private void exit() 
	{
		String filenName = "Players/" + userName;
		// Clears the file of any data then writes the players current data into file
		Scanner input = new Scanner(System.in);
		System.out.println("Would u like to save your game before exiting it will overwrite previous save [Y/N]");
		String userChoice = input.nextLine();
		if (userChoice.toUpperCase().equals("Y") ) {
		try 
		{
			clearFile(filenName);
			writeToFile(filenName, currentPlayer.getUsername());
			writeToFileInt(filenName, currentPlayer.getCorrectGuesses());
			writeToFileInt(filenName, currentPlayer.getTotalGuesses());
			writeToFileInt(filenName, currentPlayer.getPlayedCryptograms());
			writeToFileInt(filenName, currentPlayer.getCompletedCryptograms());
			
			String s = "";
			String t = "";
			for(Character c : userGuesses.keySet()) {
			s = s +c;
			t = t + userGuesses.get(c);
			}
			String ecrypt =cryptogram.getEncryptedPhrase();
			String ecryptNoWhiteSpace = ecrypt.replaceAll("\\s","");
			clearFile("Players/prevGame");
			writeToFile("Players/prevGame",cryptogram.getPhrase());
			writeToFile("Players/prevGame",ecryptNoWhiteSpace);
			writeToFile("Players/prevGame",s);
			writeToFile("Players/prevGame",t);
			System.exit(0);
		}
		catch (IOException e) // If file not found
		{
			System.out.println("\nCannot find players files to save data lost all current data is lost.");
			System.exit(0);
		}
		
		System.exit(0);
		}else {
			try {
			clearFile(filenName);
			writeToFile(filenName, currentPlayer.getUsername());
			writeToFileInt(filenName, currentPlayer.getCorrectGuesses());
			writeToFileInt(filenName, currentPlayer.getTotalGuesses());
			writeToFileInt(filenName, currentPlayer.getPlayedCryptograms());
			writeToFileInt(filenName, currentPlayer.getCompletedCryptograms());
			System.exit(0);
			}
			catch (IOException e) // If file not found
			{
				System.out.println("\nCannot find players files to save data lost all current data is lost.");
				System.exit(0);
			}
		}
	}

	/**
	 * Clears all data in file.
	 */
	public static void clearFile(String fileName) throws IOException 
	{
	    FileOutputStream outputStream = new FileOutputStream(fileName);
		byte[] strToBytes = "".getBytes();
		
	    outputStream.write(strToBytes);
	    outputStream.close();
	}

	/**
	 * Fetches a random phrase from a file.
	 * 
	 * @param fileName name of the file.
	 * @return phrase to be encrypted and solved by user.
	 */
	private String fetchPhrase(String fileName)
	{
		String phrase = null;

		try 
		{
			Random random = new Random();
			ArrayList<String> result = new ArrayList(Files.readAllLines(Paths.get(fileName)));

			phrase = result.get(random.nextInt(result.size())).replaceAll("\\s+","").toLowerCase();
		}
		catch (IOException e)
		{
			System.out.println("\nFile does not exist, or invalid file name.");
			exit();
		}

		return phrase;
	}

	
	
	private static String getUsernameFromUser(){
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter your username");
		String username  = input.nextLine();
		username = username.replaceAll("\\s+","");
		return username;
		
	
	}
	
	/**
	 * Fetches a one player's data from a file.
	 * 
	 * @param fileName name of the file.
	 * @return phrase to be encrypted and solved by user.
	 * @throws IOException 
	 */
	private static Player fetchPlayers() throws IOException 
	{
		Player player = null;
		String fileName = "Players/" + userName;
		try 
		{
			ArrayList<String> playerData = new ArrayList(Files.readAllLines(Paths.get(fileName)));
			
			//Converts Strings read from file to ints 
			int x = Integer.parseInt(playerData.get(1));
			int x1 = Integer.parseInt(playerData.get(2));
			int x2 = Integer.parseInt(playerData.get(3));
			int x3 = Integer.parseInt(playerData.get(4));
			
			// Constructs the current player object with information from file
			player = new Player(playerData.get(0),x,x1,x2,x3); 
			}
			catch(java.lang.IndexOutOfBoundsException e) {
				System.out.println("File corrupt player stats are reset");
				
				clearFile(fileName);
				writeToFile(fileName, userName);
				writeToFileInt(fileName, 0);
				writeToFileInt(fileName, 0);
				writeToFileInt(fileName, 0);
				writeToFileInt(fileName, 0);
				player = new Player(userName,0,0,0,0);
			
		}
		catch (IOException e)
		{
			System.out.println("\nPlayer file does not exist new file created please relaunch game and try again.");
			clearFile(fileName);
			System.exit(0);
		}

		return player;
	}

	private static ArrayList<String> fetchPrevGame(String fileName) throws IOException 
	{
		try
		{
			ArrayList<String> prevData = new ArrayList(Files.readAllLines(Paths.get(fileName)));
			ifSave = true;
			return prevData;
		}
		catch (java.lang.IndexOutOfBoundsException e) {
			
			System.out.println("Corrupt file previous save lost.");
			clearFile("Players/prevGame");
			ifSave = false;
		}
		catch (IOException e)
		{
			System.out.println("No Previous sessions.");
			clearFile("Players/prevGame");
			ifSave = false;
		}

		return null;
	}
	
	/**
	 * Loads the player's previous game, if it exists.
	 *
	 * @param userGuesses map representing player's guesses.
	 * @throws IOException
	 */
	private static void loadGame(HashMap<Character, Character> userGuesses) throws IOException 
	{
		currentCharIndex = 0;
		userGuesses.clear();
		Scanner input = new Scanner(System.in);
		System.out.println("Would you like to load the previous game? [Y/N]");
		String userChoice = input.nextLine();
		if (userChoice.toUpperCase().equals("Y")) 
		{
			try 
			{
				ArrayList <String> x = fetchPrevGame("Players/prevGame");
				cryptogram.setPhrase(x.get(0));
				cryptogram.setEncryptedPhrase(x.get(1), userChoice);
				currentCharIndex = 0;
				for (int z = 0;z<x.get(2).length();z++)
				{
					userGuesses.put(x.get(2).charAt(z), x.get(3).charAt(z));
					currentCharIndex++;
				}
			}
			catch (java.lang.IndexOutOfBoundsException e) 
			{
				System.out.println("Corrupt file previous save lost.");
				ifSave = false;
				clearFile("Players/prevGame");
			}
			catch (java.lang.NullPointerException e) 
			{
				ifSave = false;
				clearFile("Players/prevGame");
			}
	}}

	/**
	 * Writes a string in a file.
	 */
	public static void writeToFile(String fileName, String message) throws IOException 
	{
		String str = message;
		FileOutputStream outputStream = new FileOutputStream(fileName, true);
		byte[] strToBytes = str.getBytes();

		outputStream.write(strToBytes);
		outputStream.write(System.getProperty("line.separator").getBytes());
		outputStream.close();
	}

	/**
	 * Writes an integer to a file.
	 */
	public static void writeToFileInt(String fileName, int message) throws IOException 
	{
		FileOutputStream outputStream = new FileOutputStream(fileName, true);
		
	    outputStream.write(String.valueOf(message).getBytes());
	    outputStream.write(System.getProperty("line.separator").getBytes());
	    outputStream.close();
	}
	
	/**
	 * Main method.
	 * 
	 * @param args command-line arguents.
	 * @throws IOException 
	 */
	public static void main(String [] args) throws IOException
	{
		Game game = new Game();
		String phrase = game.fetchPhrase("Phrases/phrases.txt");
		userName = getUsernameFromUser();
		currentPlayer = fetchPlayers();
		cryptogram = new NumberCryptogram(phrase);
		cryptogram.createMapping();
		
		game.start();
	}
}
