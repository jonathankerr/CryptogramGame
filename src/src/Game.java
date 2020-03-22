import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Main driver class to run game.
 */
public class Game
{ 
	private static Player currentPlayer;
	private static Cryptogram cryptogram;

	private static HashMap<Character, Character> userGuesses;

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
	 */
	private void start() 
	{
		System.out.println("\nHello " + currentPlayer.getUsername() + ", welcome to our cryptogram program!");
		System.out.println("You can type the following commands:\n- \"undo\"\n- \"clear\"\n- \"stats\"\n- \"exit\"\n- \"name\"\n");
		System.out.println("Please type \"generate\" to start...\n");

		Scanner input = new Scanner(System.in);
		String userInput = input.nextLine();
		//input.close();

		if (userInput.equals("exit"))
		{
			exit();
		}

		while (!userInput.equals("generate"))
		{
			System.out.println("Invalid input, please type \"generate\" to start...\n");
			userInput = input.nextLine();

			if (userInput.equals("exit"))
			{
				exit();
			}
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
			boolean inputRead = readInput(userInput);
			
			if (!inputRead && (userInput.length() > 1 || userInput.length() == 0)) 
			{
				System.out.println("Invalid input. Please enter a singular character value for cryptogram character \'" + cryptogram.getEncryptedChar(currentCharIndex) + "\'...\n");
			}
			else if (!inputRead)
			{
				userGuess = userInput.charAt(0);
				userGuesses.put(cryptogram.getEncryptedChar(currentCharIndex), userGuess);
				char x6 = Character.toLowerCase(cryptogram.getChar(currentCharIndex));
				if(userGuess == x6) {
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
	 */
	private boolean readInput(String userInput) 
	{
		if (userInput.equals("undo"))
		{
			undo();
			return true;
		}
		else if (userInput.equals("clear"))
		{
			clear();
			return true;
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
			System.out.println("Last action undone");
		}
	}
	
	/**
	 * Undoes all actions.
	 */
	
	private void clear() 
	{
		System.out.println("All actions cleared");
		currentCharIndex = 0;
		userGuesses.clear();
	}

	/**
	 * Shows the current player's statistics.
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
		System.out.format("| %-20s | %-4s |", "Sessions:", player.getPlayedCryptograms()); System.out.println();
		System.out.format("| %-20s | %-4s |", "Completed sessions:", player.getTotalGuesses()); System.out.println();
		System.out.println("+----------------------+------+");
	}

	/**
	 * Changes username of user depending on input.
	 */
	private void changeUserName() 
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Your current user name is " + currentPlayer.getUsername());
		System.out.println("Would you like to change it? [Y/N]");
		String userChoice = input.nextLine();

		if (userChoice.toUpperCase().equals("Y")) 
		{
			System.out.println("Please enter a new username...\n");
			String userName = input.nextLine();
			currentPlayer.setUsername(userName);
			System.out.println("Username changed to: \"" + currentPlayer.getUsername() + "\".\n");
		}
		else
		{
			System.out.println("Username has not beem changed.\n");
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
				exit();
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

	/**
	 * Runs when the game ends. Writes data of one player to a file.
	 */
	private void exit() 
	{
		// Clears the file of any data then writes the players current data into file
		try 
		{
			clearFile("Players/players");
			writeToFile("Players/players", currentPlayer.getUsername());
			writeToFileInt("Players/players", currentPlayer.getCorrectGuesses());
			writeToFileInt("Players/players", currentPlayer.getTotalGuesses());
			writeToFileInt("Players/players", currentPlayer.getPlayedCryptograms());
			writeToFileInt("Players/players", currentPlayer.getCompletedCryptograms());
			
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
		}
		catch (IOException e) // If file not found
		{
			System.out.println("\nCannot find players files to save data lost all current data is lost.");
			System.exit(0);
		}
		
		System.exit(0);
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

	/**
	 * Fetches a one player's data from a file.
	 * 
	 * @param fileName name of the file.
	 * @return phrase to be encrypted and solved by user.
	 * @throws IOException 
	 */
	private static Player fetchPlayers(String fileName) throws IOException 
	{
		Player player = null;

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
				System.out.println("Insifficent Data in player file pls restart exit and relaunch ");
				
				clearFile("Players/players");
				writeToFile("Players/players", "User");
				writeToFileInt("Players/players", 0);
				writeToFileInt("Players/players", 0);
				writeToFileInt("Players/players", 0);
				writeToFileInt("Players/players", 0);
				System.exit(0);
			
		}
		catch (IOException e)
		{
			System.out.println("\nFile does not exist, or invalid file name.");
			clearFile("Players/players");
			System.exit(0);
		}

		return player;
	}

	private static ArrayList<String> fetchPrevGame(String fileName) throws IOException 
	{
		try
		{
			ArrayList<String> prevData = new ArrayList(Files.readAllLines(Paths.get(fileName)));
			return prevData;
		}
		catch (java.lang.IndexOutOfBoundsException e) {
			
			System.out.println("No Previous sessions.");
			clearFile("Players/prevG");
			System.exit(0);
		}
		catch (IOException e)
		{
			System.out.println("No Previous sessions.");
			clearFile("Players/prevGame");
			System.exit(0);	
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
		Scanner input = new Scanner(System.in);
		System.out.println("Would you like to load previous game?");
		String userChoice = input.nextLine();
		if(userChoice.equals("Y")) {
			
			try 
			{
				ArrayList <String> x = fetchPrevGame("Players/prevGame");
				cryptogram.setPhrase(x.get(0));
				cryptogram.setEncryptedPhrase(x.get(1),x.get(0));

				for (int z = 0;z<x.get(2).length();z++)
				{
					userGuesses.put(x.get(2).charAt(z), x.get(3).charAt(z));
					currentCharIndex++;
				}
			}
			catch (java.lang.IndexOutOfBoundsException e) 
			{
				System.out.println("No previous session.");
				System.exit(0);
			}
		}
	}

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
		currentPlayer = fetchPlayers("Players/players");
		cryptogram = new NumberCryptogram(phrase);
		cryptogram.createMapping();
		loadGame(userGuesses);
		game.start();
	}
}