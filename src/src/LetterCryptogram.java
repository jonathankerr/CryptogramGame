import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class LetterCryptogram 
{
	private char[] alphabet = 
	{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' 
	};
	
	// Adds each letter to an arraylist, alphabetList
	private List<Character> alphabetList = new ArrayList<Character>(); 
	{
		for (char c : alphabet) 
		{
			alphabetList.add(c);
		}
	}
	
	private Set<Character> uniqueQuoteLetters = new HashSet<Character>(); //Finding each unique letter in the quote
	private Map<Character, Character> encryptedKeyMap = new HashMap<Character, Character>();

	private List<Character> encryptedQuote = new ArrayList<Character>();
	private ArrayList<Character> userLetters = new ArrayList<>();
	private ArrayList <Character> uniqueLetters = new ArrayList<>();

	//private  List<String> userProgress = new ArrayList<String>();

	private String quote;
	private String finalEncryptedQuote = "";
	//private String finalUserProgress = "";

	int currentLetter = 0;	 

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

		displayCryptogram();

		if (currentLetter == uniqueLetters.size())
		{
			if (complete()) 
			{
				System.out.println("You have successfully completed the cryptogram.\n");
				System.exit(0);
			} 
			else 
			{
				System.out.println("You have failed to complete the crytogram. All values will be now be reset...\n");
				currentLetter = 0;
				userLetters.clear();

				return;
			}
		}

		System.out.println("Please enter a singular letter value for cryptogram letter \'" + uniqueLetters.get(currentLetter)  + "\'...\n");
		value = readInput().toLowerCase(); //Setting to lower case so switch doesn't break.

		while (value.length() > 1 | value.length() == 0) 
		{
			switch (value) 
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

			System.out.println("Invalid input, please enter a singular letter value for cryptogram letter \'" + uniqueLetters.get(currentLetter)  + "\'...\n");
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
	 * Prints out the generated cryptogram.
	 */
	private void userCryptogram() 
	{
		if (currentLetter == 0)
		{
			return;
		}

		System.out.println("\nYou current mapping is: ");

		for (int i = 0; i <= currentLetter-1; i++) 
		{
			System.out.print(uniqueLetters.get(i) + " = " + userLetters.get(i) + " ");
		}

		System.out.println("\n");
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

	/**
	 * Undoes an action.
	 */
	private void undo () 
	{
		if (currentLetter == 0)
		{
			System.out.println("You cannot undo anymore letters.\n");

			return;
		}

		userLetters.remove(currentLetter-1);
		currentLetter --;
		System.out.println("You have undone your last action.\n");
		userCryptogram();
	}

	/**
	 * Undoes all actions.
	 */
	private void clear() 
	{
		System.out.println("Clearing all mappings...\n");
		currentLetter = 0;
		userLetters.clear();
	}

	/**
	 * Prints welcome-message and waits for user to start game.
	 */
	private void userWelcome() 
	{
		System.out.println("\nHello player, welcome to our cryptogram program!\n");
		System.out.println("Please type \"generate\" to start...\n");
		String generationCheck = readInput();

		while (!generationCheck.equalsIgnoreCase("generate")) 
		{
			System.out.println("Invalid input, please type \"generate\" to start...\n");
			generationCheck = readInput();
		}

		System.out.println("Your cryptogram is: \n");
	}

	/**
	 * Displays cryptogram in terminal.
	 */
	private void displayCryptogram() 
	{
		System.out.println(finalEncryptedQuote);
	}

		/*
	private  void Tests() {
		
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

	public static void getInput() 
	{
		Scanner input = new Scanner(System.in);
		String s = input.nextLine();
		char FirstChar = s.charAt(0);
		char SecondChar = s.charAt(2);
	}
	*/
}
