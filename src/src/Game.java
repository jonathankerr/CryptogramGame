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
	private Players players;
	private static Player currentPlayer;
	private static Cryptogram cryptogram;

	private static HashMap<Character, Character> userGuesses;
	private static String userName;
	private static int currentCharIndex;

	/** 
	 * Class constructor.
	 */
	public Game()
	{
		userGuesses = new HashMap<Character, Character>();
		players = new Players();
		currentCharIndex = 0;
	}

	/**
	 * Starts the game and gets input from terminal.
	 * @throws IOException 
	 */
	private void start() throws IOException 
	{
		System.out.println("\nPlease enter your username...\n");

		Scanner input = new Scanner(System.in);
		String userInput = input.nextLine();

		boolean hasPlayers = players.fetchPlayers();

		if (hasPlayers)
		{
			if (players.getPlayer(userInput) != null)
			{
				loadGame(players.getPlayer(userInput));
			}
			else
			{
				players.addPlayer(new Player(userInput, 0, 0, 0, 0));
			}
		}
		else
		{
			players.addPlayer(new Player(userInput, 0, 0, 0, 0));
		}

		currentPlayer = players.getPlayer(userInput);

		System.out.println("\nHello " + currentPlayer.getUsername() + ", welcome to our cryptogram program!");
		System.out.println("You can type the following commands:\n- \"undo\"\n- \"clear\"\n- \"answer\"\n- \"top10\"\n- \"stats\"\n- \"name\"\n- \"save\"\n- \"load\"\n- \"exit\"\n ");
		System.out.println("Please type \"generate\" to start...\n");
		
		userInput = input.nextLine();
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
		completed();
	}
	
	/**
	 * Prints top 10 scores to console.
	 */
	@SuppressWarnings("null")
	private void getTop10() 
	{
		/*
		try 
		{ 
        	String[] Names = null;
        	Integer[] Scores = null;
            File f = new File("Players"); 
            Player player = null;
            ArrayList<Player> allPlayers = new ArrayList();
            
            Map<String, Integer> map = new HashMap<String, Integer>();
  
            // Get all the names of the files present 
            // in the directory 
            File[] files = f.listFiles(); 
  
            System.out.println("Top 10 Scores Are:"); 
  
			for (int i = 0; i < files.length; i++) 
			{ 
				if (files[i].getName().equals("prevGame")) 
				{
            		//do nothing with this file
            	}
				else 
				{
	           		String fileName = "Players/" + files[i].getName();
	               	ArrayList<String> playerData = new ArrayList(Files.readAllLines(Paths.get(fileName)));
	               	String userName = playerData.get(0);
	               	int completedCryptos = Integer.parseInt(playerData.get(4));
	
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
			
        	Collections.sort(allPlayers);
        
        	int count = 0;
        	loop:
	        	for(Player str: allPlayers) {
	        		if (count == 9) {
	        			break loop;
	        		}
	        		else {
		        		System.out.println(str);
		        		count++;
	        		}
				}
				
			System.out.println("+----------------------+------+");
			System.out.println("| Player name          | Sco. |");
			System.out.println("+----------------------+------+");
			System.out.println("+ This game: ----------+------+");
			System.out.format("| %-20s | %-4s |", "Guesses:", player.getTotalGuesses()); System.out.println();
			System.out.println("+----------------------+------+");
        } 
		catch (Exception e) 
		{ 
            System.err.println(e.getMessage()); 
		}
		*/
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
		System.out.println("Your username is: " + currentPlayer.getUsername());
		System.out.println("Would you like to change it? [Y/N]");
		String userChoice = input.nextLine();

		if (userChoice.toUpperCase().equals("Y")) 
		{
			System.out.println("Please enter a new name...\n");
			String userName = input.nextLine();
			currentPlayer.setUsername(userName);
			System.out.println("Username changed to: \"" + currentPlayer.getUsername() + "\".\n");
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
				completed();
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
	
	private void save() 
	{
		Scanner input = new Scanner(System.in);
			
		System.out.println("Would you like to save your current session? This will override any previous session you might have saved. [Y/N]");
			
		if (input.nextLine().toUpperCase().equals("Y"))
		{
			currentPlayer.saveSession(cryptogram.getPhrase(), cryptogram.getEncryptedPhrase().replaceAll("\\s",""), userGuesses);
		}
	}

	/**
	 * Runs when the game ends. Writes data of one player to a file.
	 */
	private void exit() 
	{
		Scanner input = new Scanner(System.in);

		System.out.println("Would you like to save your current session? This will override any previous session you might have saved. [Y/N]");

		if (input.nextLine().toUpperCase().equals("Y") ) 
		{
			currentPlayer.saveSession(cryptogram.getPhrase(), cryptogram.getEncryptedPhrase().replaceAll("\\s",""), userGuesses);
			currentPlayer.saveData();
		}
		else 
		{
			currentPlayer.saveData();
		}

		System.exit(0);
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
	 * Loads the player's previous game, if it exists.
	 *
	 * @param userGuesses map representing player's guesses.
	 * @throws IOException
	 */
	private static void loadGame(Player player) throws IOException 
	{
		Scanner input = new Scanner(System.in);

		System.out.println("Would you like to load the previous game? [Y/N]");
		
		if (input.nextLine().toUpperCase().equals("Y")) 
		{
			ArrayList<String> session = player.getSession();

			if (session.size() > 0)
			{
				currentCharIndex = 0;
				userGuesses.clear();
				cryptogram.setPhrase(session.get(0));
				cryptogram.setEncryptedPhrase(session.get(1), input.nextLine());

				for (int i = 0; i < session.get(2).length(); i++)
				{
					userGuesses.put(session.get(2).charAt(i), session.get(3).charAt(i));
					currentCharIndex++;
				}
			}
		}
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
		
		cryptogram = new NumberCryptogram(phrase);
		cryptogram.createMapping();
		
		game.start();
	}
}
