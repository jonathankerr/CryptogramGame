import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
		System.out.println("\nHello player, welcome to our cryptogram program!");
		System.out.println("You can type the following commands:\n- \"undo\"\n- \"clear\"\n- \"exit\"\n");
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
			currentPlayer.incrementTotalGuesses();
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
				currentCharIndex++;
			}
		}
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
		else if (userInput.equals("exit"))
		{
			exit();
		}
		else if (userInput.equals("showStats"))
		{
			showStats();
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

	//Function to fetch the players data from a file and store it in objext currrent player
	private static void fetchPlayers(String fileName) 
	{
		
		try 
		{
			ArrayList<String> playerData = new ArrayList(Files.readAllLines(Paths.get(fileName)));
			
			//Converts Strings read from file to ints 
			int x = Integer.parseInt(playerData.get(1));
			int x1 = Integer.parseInt(playerData.get(2));
			int x2 = Integer.parseInt(playerData.get(3));
			int x3 = Integer.parseInt(playerData.get(4));
			
			//Constructs the current player object with information from file
			currentPlayer = new Player(playerData.get(0),x,x1,x2,x3); 
		
		}
		catch (IOException e)
		
		{
			System.out.println("\nFile does not exist, or invalid file name.");
			System.exit(0);
		}
	}
	//Function to print functions to the code
	private static void showStats() {

		System.out.println("Your username is " + currentPlayer.getUsername());
		System.out.println("Your accuracy is " + currentPlayer.getAccuracy() );
		System.out.println("Your total guesses is " + currentPlayer.getTotalGuesses());
		System.out.println("Your total attempted cryptograms is " + currentPlayer.getPlayedCryptograms());
		System.out.println("Your total correctly completed cryptograms is "+ currentPlayer.getCompletedCryptograms());
		
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
	//Fileto clear the file of previous data
	public void clearFile(String fileName) throws IOException {
	    FileOutputStream outputStream = new FileOutputStream(fileName);
	    byte[] strToBytes = "".getBytes();
	    outputStream.write(strToBytes);

	}
	//Function to write Strings to the file
	public void writeToFile(String fileName,String message) throws IOException {
			    String str = message;
			    FileOutputStream outputStream = new FileOutputStream(fileName, true);
			    byte[] strToBytes = str.getBytes();
			    outputStream.write(strToBytes);
			    outputStream.write(System.getProperty("line.separator").getBytes());
			    outputStream.close();
			}
	//Function to write ints to the file
	public void writeToFileInt(String fileName,int message) throws IOException {
	    int str = message;
	    FileOutputStream outputStream = new FileOutputStream(fileName, true);
	    outputStream.write(String.valueOf(message).getBytes());
	    outputStream.write(System.getProperty("line.separator").getBytes());
	    outputStream.close();
	}	
	//Funnction run when program ends
	private void exit() {
		//Clears the file of any data then writes the players current data into file
		try 
		{
			clearFile("Players/players.txt");
			writeToFile("Players/players.txt",currentPlayer.getUsername());
			writeToFileInt("Players/players.txt",currentPlayer.getTotalGuesses());
			writeToFileInt("Players/players.txt",currentPlayer.getTotalGuesses());
			writeToFileInt("Players/players.txt",currentPlayer.getPlayedCryptograms());
			writeToFileInt("Players/players.txt",currentPlayer.getCompletedCryptograms());
		}
		catch(IOException e)
		//If file not found
		{
			System.out.println("\nCannot find players files to save data lost all current data is lost.");
			System.exit(0);
		}
		
		
		System.exit(0);
	}
	
	
	/**
	 * Main method.
	 * 
	 * @param args command-line arguents.
	 */
	public static void main(String [] args)
	{
		Game game = new Game();
		String phrase = game.fetchPhrase("Phrases/phrases.txt");
		fetchPlayers("Players/players.txt");
		cryptogram = new NumberCryptogram(phrase);

		cryptogram.createMapping();
		game.start();
	}
}
