import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Main driver class to run game.
 */
public class Game
{ 
	//private Player currentPlayer;
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
			System.exit(0);
		}

		while (!userInput.equals("generate"))
		{
			System.out.println("Invalid input, please type \"generate\" to start...\n");
			userInput = input.nextLine();

			if (userInput.equals("exit"))
			{
				System.exit(0);
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
				System.exit(0);
			}
			else 
			{
				System.out.println("\nYou have failed to complete the cryptogram. All values will be now be reset...");
				currentCharIndex = 0;
				userGuesses.clear();
			}
		}

		return false;
	}
	
	/**
	 * Main method.
	 * 
	 * @param args command-line arguents.
	 */
	public static void main(String [] args)
	{
		cryptogram = new NumberCryptogram("helloworld");
		Game game = new Game();

		cryptogram.createMapping();
		game.start();
	}
}