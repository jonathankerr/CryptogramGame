import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Generates a letter cryptogram.
 */
public class LetterCryptogram 
{	
	private String quote;

	private char[] alphabet = 
	{ 
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' 
	};
	
	// Creating an arraylist containing all possible characters in the cryptogram.
	private List<Character> alphabetList = new ArrayList<Character>(); 
	{
		for (char c : alphabet) 
		{
			alphabetList.add(c);
		}
	}
	
	private Set<Character> uniqueQuoteLetters = new HashSet<Character>();
	private Map<Character, Character> encryptedKeyMap = new HashMap<Character, Character>();;
	private List<Character> encryptedQuote = new ArrayList<Character>();
	private ArrayList<Character> userLetters = new ArrayList<>();
	private ArrayList <Character> uniqueLetters = new ArrayList<>();
	//private List<String> userProgress = new ArrayList<String>();
	private String finalEncryptedQuote = "";
	//private String finalUserProgress = "";
	private int currentLetter = 0;

	/**
	 * Class constructor.
	 * 
	 * @param phrase phrase that is encrypted.
	 */
	public LetterCryptogram(String phrase) 
	{	
		quote = phrase.toLowerCase();

		createMapping();
		userWelcome();

		while (true) 
		{
			gameStart();
			userCryptogram();
		}
	}

	/**
	 * Starts the game and gets input from terminal.
	 */
	private void gameStart() 
	{
		char userGuess;
		String value;

		displayCryptoGram();

		if (currentLetter == uniqueLetters.size())
		{
			if (complete() == true) 
			{
				System.out.println("You have completed the cyrptogram good job!");
				System.exit(0);
			} 
			else 
			{
				System.out.println("You have failed to complete the crytogram, Resetting all the values and try again.");
				currentLetter = 0;
				userLetters.clear();
				return;
			}
		}

		System.out.println("Please enter a singular letter value for cyrptogram letter " + uniqueLetters.get(currentLetter));
		value = readInput();
		value.toLowerCase(); // Setting to lower case so switch doesn't break.

		while (value.length() > 1 | value.length() == 0) 
		{
			switch(value) 
			{
				case ("exit"):
					System.exit(0);
					break;
				case("undo"):
					undo();
					return;
				case("clear"):
					clear();
					return;
				default:
					break;
			}

			System.out.println("Invalid input, Please enter a singular letter value for cyrptogram letter " + uniqueLetters.get(currentLetter));
			value = readInput();
		}

		userGuess = value.charAt(0);
		userLetters.add(userGuess);
		currentLetter++;
	}
	
	/**
	 * Maps each character in the phrase to a letter from the alphabet.
	 */
	private void createMapping() 
	{
		for (char c : quote.toCharArray()) 
		{
			if (Character.isLetter(c))
			{
				uniqueQuoteLetters.add(c);
			}
		}

		Collections.shuffle(alphabetList);  // Randomly order the alphabet arraylist.
		
		// For each of the unique letters we will assign a random letter as it's encypted pair
		for (Character C : uniqueQuoteLetters) 
		{  
			encryptedKeyMap.put(C, alphabetList.get(0));
			alphabetList.remove(0);
		}

		// Prevents any letters being mapped to themselves.
		for (Character C : encryptedKeyMap.keySet()) 
		{
			Character currentValue = encryptedKeyMap.get(C);

			if (C.equals(currentValue)) 
			{
				encryptedKeyMap.put(C, alphabetList.get(0));
				alphabetList.remove(0);
				alphabetList.add(currentValue);
			}
		}

		for (char c : quote.toCharArray()) 
		{
			if (Character.isLetter(c))
			{
				encryptedQuote.add(encryptedKeyMap.get(c));
			}

			// Adding to uniqueletters arraylist ot be used.
			if (!uniqueLetters.contains(encryptedKeyMap.get(c))) 
			{
				uniqueLetters.add(encryptedKeyMap.get(c));
			}
		}

		for (Character s : encryptedQuote) 
		{
			finalEncryptedQuote += s + " ";
		}
	}
	
	/*
	public static void Tests() {
		
		for (char c : quote.toCharArray()) {
			if (Character.isLetter(c))
				encryptedQuote.add(encryptedKeyMap.get(c));
				//System.out.println(encryptedKeyMap.get(c));
		}
		
		for(Character s : encryptedQuote) {
			finalEncryptedQuote += s + " ";
		}
		
		for(int i = 0; i < quote.length(); i++) {
			userProgress.add("_");
		}
		
		for(String s : userProgress) {
			finalUserProgress += s + " ";
		}
		
		System.out.println("Here is the quote:\n" + quote);
		System.out.println("Here is the random encryption of the quote:\n" + finalEncryptedQuote);
		//System.out.println("Here is the users progress:\n" + finalUserProgress);
		System.out.println("Here are the mappings:\n" + encryptedKeyMap);
		//System.out.println(uniqueQuoteLetters);
	}
	*/
	
	
	/**
	 * Gets user input from terminal.
	 */
	/*
	private void getInput() 
	{
		Scanner input = new Scanner(System.in);
		String s = input.nextLine();
		char FirstChar = s.charAt(0);
		char SecondChar = s.charAt(2);
	}
	*/

	/**
	 * Prints welcome-message and waits for user to start game.
	 */
	private void userWelcome() 
	{
		String generationCheck = readInput();
		System.out.println("Hello Player welcome to our cryptogram program!\n");
		System.out.println("Please Type generate to start! \n");

		while (!generationCheck.equalsIgnoreCase("generate")) 
		{
			System.out.println("Invalid input, please type generate to start.");
			generationCheck = readInput();
		}
		
		System.out.println("Your cyrptogram is \n");
	}


	/**
	 * Waits for user to end game, otherwise returns input.
	 * 
	 * @return user's input.
	 */
	private String readInput() 
	{
		Scanner input = new Scanner(System.in);
		String userinput = input.nextLine();

		if (userinput.equalsIgnoreCase("exit"))
		{
			System.exit(0);
		}

		return userinput;
	}

	/**
	 * Displays cryptogram in terminal.
	 */
	private void displayCryptoGram() 
	{
		System.out.println(finalEncryptedQuote);
	}

	/**
	 * Prints out the generated cryptogram.
	 */
	private void userCryptogram() 
	{
		if (currentLetter == 0)
		{
			return;
		}

		System.out.println("You current mapping is");

		for (int i = 0; i <= currentLetter-1; i++) 
		{
			System.out.print(uniqueLetters.get(i) + " = " + userLetters.get(i) + " ");
		}

		System.out.println("");
	}

	/**
	 * Undoes an action.
	 */
	private void undo() 
	{
		if (currentLetter == 0)
		{
			System.out.println("You cannot undo anymore letters");
			return;
		}

		userLetters.remove(currentLetter-1);
		currentLetter --;
		System.out.println("You have undone your previous letter for the cryptogram.");
		userCryptogram();
	}

	/**
	 * Undoes all actions.
	 */
	private void clear() 
	{
		System.out.println("Clearing all mappings");
		currentLetter = 0;
		userLetters.clear();
	}

	/**
	 * Determines whether user has completed the game.
	 * 
	 * @return boolean value determining whether game has finished.
	 */
	private boolean complete() 
	{
		boolean finished = true;

		for (int i = 0; i <= currentLetter-1; i++) 
		{
			if (!uniqueQuoteLetters.contains(userLetters.get(i)))
			{
				finished = false;
			}
		}

		return finished;
	}
}