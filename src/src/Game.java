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
	//initlaise the current player 
	private static Player currentPlayer;
	private static Cryptogram cryptogram;

	private HashMap<Character, Character> userGuesses;

	private int currentCharIndex;

	private Game()
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
		System.out.println("You can type the following commands:\n- \"undo\"\n- \"clear\"\n- \"stats\"\n- \"exit\"\n");
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

	private void changeUserName() {
		Scanner input = new Scanner(System.in);
		System.out.println("Your current user name is " + currentPlayer.getUsername());
		System.out.println("Would you like to change it [Y/N]");
		String userChoice = input.nextLine();
		if(userChoice.equals("Y")) {
			System.out.println("Please enter a new username");
			String userName = input.nextLine();
			currentPlayer.setUsername(userName);
			}
			else
				System.out.println("Username has not be changed");
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
			showStats();
			return true; 
		}	
		else if (userInput.equals("exit"))
		{
			exit();
		}
		else if (userInput.equals("name"))
		{
			changeUserName();
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
	private static void showStats() 
	{
		System.out.println("Your username is " + currentPlayer.getUsername());
		System.out.printf("Your accuracy is %f\n",currentPlayer.getAccuracy());
		System.out.println("Your total correct guesses is " + currentPlayer.getCorrectGuesses());
		System.out.println("Your total guesses is " + currentPlayer.getTotalGuesses());
		System.out.println("Your total attempted cryptograms is " + currentPlayer.getPlayedCryptograms());
		System.out.println("Your total correctly completed cryptograms is "+ currentPlayer.getCompletedCryptograms());
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
	 * Writes a string in a file.
	 */
	public static void writeToFile(String fileName,String message) throws IOException 
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
	public static void writeToFileInt(String fileName,int message) throws IOException 
	{
		FileOutputStream outputStream = new FileOutputStream(fileName, true);
		
	    outputStream.write(String.valueOf(message).getBytes());
	    outputStream.write(System.getProperty("line.separator").getBytes());
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
			System.exit(0);
		}

		return player;
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
		game.start();
	}
}
