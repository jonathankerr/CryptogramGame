import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Main driver class to run game.
 */
public class Game
{ 
	private Players players;
	private static Player currentPlayer;
	private static Cryptogram cryptogram;

	private static HashMap<Character, Character> userGuesses;
	private static int currentCharIndex;

	private String[] commands = 
	{ "commands", "undo", "clear", "name", "save", "load", "top", "frequency", "hint", "answer","exit" };

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
	 */
	private void start(String phrase) 
	{
		System.out.println("\nPlease enter your username...\n");

		Scanner input = new Scanner(System.in);
		String userInput = input.nextLine();

		getCurrentUser(userInput);
		commands();
		
		System.out.println("Please type \"generate\", followed by either \"numbers\" or \"letters\" to start...\n");
		
		userInput = input.nextLine();
		readInput(userInput, false);

		while (!userInput.toLowerCase().equals("generate numbers") && !userInput.toLowerCase().equals("generate letters"))
		{
			System.out.println("\nPlease type \"generate\", followed by either \"numbers\" or \"letters\" to start...\n");
			userInput = input.nextLine();

			readInput(userInput, false);
		}	

		System.out.println();

		if (userInput.toLowerCase().contains("numbers"))
		{
			cryptogram = new NumberCryptogram(phrase);
		}
		else if (userInput.toLowerCase().contains("letters"))
		{
			cryptogram = new LetterCryptogram(phrase);
		}

		cryptogram.createMapping();

		while (!completed())
		{
			System.out.print("\nCryptogram:");
			System.out.println("\n" + cryptogram.getEncryptedPhrase());
			getMapping();

			System.out.println("Enter a singular character value for cryptogram character \'" + cryptogram.getEncryptedChar(currentCharIndex) + "\'...\n");

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

	private void getCurrentUser(String userInput)
	{
		boolean hasPlayers = players.fetchPlayers();

		if (hasPlayers)
		{
			if (players.getPlayer(userInput) != null && players.getPlayer(userInput).getSession().size() != 0)
			{
				System.out.println("\nWelcome back, " + players.getPlayer(userInput).getUsername());
				load(players.getPlayer(userInput));
				System.out.println();
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
	}
	
	/**
	 * Gets user input from terminal.
	 */
	private boolean readInput(String userInput, boolean inGame)
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
		else if (userInput.equals("hint"))
		{
			if (inGame)
			{
				hint();
				return true;
			}
			else
			{
				System.out.println("You must be solving a cryptogram in order to use this command.");
			}
		}
		else if (userInput.equals("frequency"))
		{
			if (inGame)
			{
				frequency();
				return true;
			}
			else
			{
				System.out.println("You must be solving a cryptogram in order to use this command.");
			}
		}
		else if (userInput.equals("stats"))
		{
			stats(currentPlayer);
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
		else if (userInput.equals("top"))
		{
			top();
			return true;
		}
		else if (userInput.equals("commands"))
		{
			commands();
			return true;
		}

		return false;
	}

	private void commands()
	{
		System.out.println("+-----------------------------+");
		System.out.println("| Commands                    |");
		System.out.println("+-----------------------------+");
		for (int i = 1; i < commands.length; i++)
		{
			System.out.format("| %-27s |", commands[i]); System.out.println();
		}
		
		System.out.println("+-----------------------------+");
		System.out.println("Type \"commands\" to see this list again.\n");
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
	 * Shows the letter frequencies.
	 */
	
	private void frequency() 
	{
		int length = cryptogram.getPhrase().length();  //gets the real length
		String str = cryptogram.getEncryptedPhrase(); //gets the actual encrypted phrase the user sees
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		str.trim();
		
		char[] chars = str.toCharArray();	
		for(char c: chars) {   //loops through all chars
			if(c == ' ') { //if blank then pass over
				//do nothing
			}
			else {
				if(map.containsKey(c)) { //if its already been mapped
					map.replace(c, map.get(c), map.get(c) + 1);
				}
				else { //first time its been mapped
					map.put(c, 1);
				}
			}
		}
		System.out.println("Frequencies in the cryprogram:");
		
		
		for(Character c: map.keySet()) {  //loops through the hashmap
			double percent = map.get(c);
			percent = (percent / length) * 100; //converts the frequency to a percentage
			percent = Double.parseDouble(new DecimalFormat("##.#").format(percent));  //limits the double to only 1 dp
			System.out.println(c + " " + percent +"%");
		}
		
		/*
		 * English language frequencies taken from https://www3.nd.edu/~busiforc/handouts/cryptography/letterfrequencies.html
		 */
		
		System.out.println("Frequencies in the english Language:");
		System.out.println("E 11.6%");
		System.out.println("A 8.4%");
		System.out.println("R 7.5%");
		System.out.println("I 7.5%");
		System.out.println("O 7.1%");
		System.out.println("T 6.9%");
		System.out.println("N 6.6%");
		System.out.println("S 5.7%");
		System.out.println("L 5.4%");
		System.out.println("C 4.5%");
        
		
	}
	
	/**
	 * Gives the user a hint..
	 */
	
	private void hint() 
	{
		boolean alreadyHinted = false;
		for(int i = 0; i< userGuesses.size(); i++) {
			//char x6 = Character.toUpperCase(cryptogram.getChar(i));
			char x6 = userGuesses.get(cryptogram.getEncryptedChar(i));
			if(cryptogram.getChar(i) != x6) {// if an existing guess by the user is incorrect we  will change it.
				alreadyHinted = true;
				char answer = cryptogram.getChar(i);
				userGuesses.replace(cryptogram.getEncryptedChar(i), answer);
				System.out.println("You have guessed that: " + cryptogram.getEncryptedChar(i) + " was equal to: " + x6);
				System.out.println("This is incorrect. " + cryptogram.getEncryptedChar(i) + " is actually equal to: " + cryptogram.getChar(i));
				System.out.println("So we have changed that for you.");
				currentPlayer.incrementTotalGuesses();
			}
		}
		
		if(!alreadyHinted) {
			System.out.println("You are still to guess a character for " + cryptogram.getEncryptedChar(currentCharIndex));
			System.out.println("We know that " + cryptogram.getEncryptedChar(currentCharIndex) + " is equal to " + cryptogram.getChar(currentCharIndex));
			System.out.println("So we have sorted that for you.");
			userGuesses.put(cryptogram.getEncryptedChar(currentCharIndex), cryptogram.getChar(currentCharIndex));
			currentPlayer.incrementTotalGuesses();
			currentCharIndex++;
		}
		
		
		//currentPlayer.incrementCorrectGuesses();
		//currentCharIndex++;
		//currentPlayer.incrementTotalGuesses();
	}
	
	/**
	 * Gives the answer to the cryptogram.
	 */
	
	private void answer() 
	{
		System.out.println("Answer.");
		System.out.println("\n" + cryptogram.getEncryptedPhrase());
		for (int i = 0; i < cryptogram.getEncryptedPhrase().length(); i++) 
		{
			System.out.print('-');
		}

		System.out.println();

		for (int i = 0; i < cryptogram.getEncryptedPhrase().toCharArray().length; i++) 
		{
			char output = cryptogram.getPhrase().replaceAll(".(?!$)", "$0 ").toCharArray()[i];
			System.out.print(Character.toUpperCase(output));
		}

		System.out.println();
	
		currentPlayer.incrementPlayedCryptograms();	
		System.exit(0);
	}

	/**
	 * Loads the player's previous game, if it exists.
	 *
	 * @param userGuesses map representing player's guesses.
	 */
	private static void load(Player player)
	{
		Scanner input = new Scanner(System.in);

		System.out.println("Would you like to load the previous game? [Y/N]\n");

		String userInput = input.nextLine();
		
		if (userInput.toUpperCase().equals("Y")) 
		{
			ArrayList<String> session = player.getSession();

			if (session.size() > 0)
			{
				currentCharIndex = 0;
				userGuesses.clear();
				cryptogram.setPhrase(session.get(0));
				cryptogram.setEncryptedPhrase(session.get(1), userInput);

				for (int i = 0; i < session.get(2).length(); i++)
				{
					userGuesses.put(session.get(2).charAt(i), session.get(3).charAt(i));
					currentCharIndex++;
				}
			}
		}
	}
	
	/**
	 * Prints top 10 scores to console.
	 */
	private void top() 
	{
		ArrayList<Player> players = this.players.getPlayers();
		players = new ArrayList<Player>(players.stream().filter(x -> ((Player)x).getCompletedCryptograms() > 0).collect(Collectors.toList()));
		Collections.sort(players);

		if (players.size() > 0)
		{
			System.out.println("\nTop 10:");
			System.out.println("+---+-------------+-------+");
			System.out.println("| # | Player name | Score |");
			System.out.println("+---+-------------+-------+");
			for (int i = 0; i < 10; i++)
			{
				System.out.format("| %-1s | %-11s | %-5s |", Integer.toString(i + 1), players.get(i).getUsername(), players.get(i).getCompletedCryptograms()); System.out.println();
			}
			System.out.println("+---+-------------+-------+");
		}
		else
		{
			System.out.println("No one has completed any cryptograms yet.");
		}
	}

	/**
	 * Shows the current player's statistics in tabular form.
	 */
	private static void stats(Player player) 
	{
		System.out.println("\nStats for " + currentPlayer.getUsername() + ":");
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

		System.out.println("\nYour current username is: " + currentPlayer.getUsername() + "\n");
		System.out.println("Would you like to change it? [Y/N]\n");

		String userInput = input.nextLine();

		if (userInput.toUpperCase().equals("Y")) 
		{
			System.out.println("\nPlease enter a new username...\n");

			String username = input.nextLine();
			
			if (players.getPlayer(username) == null)
			{
				if (currentPlayer.getSession().size() > 0)
				{
					try 
					{
						File session = new File("Players/Sessions/" + currentPlayer.getUsername());
						File data = new File("Players/Users/" + currentPlayer.getUsername()); 
						File newName = new File("Players/Sessions/" + username);
						
						session.renameTo(newName);
						data.delete();
					}
					catch (Exception e)
					{ }
				}

				currentPlayer.setUsername(username);

				System.out.println("\nUsername changed to: \"" + username + "\".\n");
			}
			else
			{
				System.out.println("\nUser with name \"" + username + "\" already exists. Username has not been changed.");
			}
		}
		else
		{
			System.out.println("\nUsername has not been changed.\n");
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
				currentPlayer.saveData();
				System.exit(0);
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
			
		System.out.println("Would you like to save your current session? This will override any previous session you might have saved. [Y/N]\n");
			
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
		save();
		currentPlayer.saveData();

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
	 * Main method.
	 * 
	 * @param args command-line arguents.
	 */
	public static void main(String [] args) throws IOException
	{
		Game game = new Game();
		String phrase = game.fetchPhrase("Phrases/phrases.txt");
		
		game.start(phrase);
	}
}
