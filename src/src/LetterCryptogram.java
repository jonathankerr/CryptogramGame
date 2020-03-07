import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Scanner;

public class LetterCryptogram {
	
	private static String quote;
	//Creating an arraylist containing all possible letters in the cryptogram
	private char[] alphabetForGram = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	
	
	private static //Adds each letter to an arraylist, alphabetList
	List<Character> alphabetList = new ArrayList<Character>(); {
		for (char c : alphabetForGram) {
			alphabetList.add(c);
		}
	}
	
	
	//The following are only declared for testing purposes
	private static Map<Character, Character> encryptedKeyMap = new HashMap<Character, Character>();;
	private static List<Character> encryptedQuote = new ArrayList<Character>();
	private static String finalEncryptedQuote = "";
	private static String finalUserProgress = "";
	private static List<String> userProgress = new ArrayList<String>();
	private static ArrayList<Character> userLetters = new ArrayList<>();
	private static ArrayList <Character> uniqueLetters = new ArrayList<>();
	int currentLetter = 0;
	//Finding each unique letter in the quote
	static Set<Character> uniqueQuoteLetters = new HashSet<Character>();

	//Constructor
	public LetterCryptogram(String Phrase) {
		
		quote = Phrase.toLowerCase();
		createMapping();
		//Tests();
		userWelcome();
	  while(true) {
			gameStart();
			userCryptogram();
		}
	}
	
	
	public static void createMapping() {


		for (char c : quote.toCharArray()) {
			if (Character.isLetter(c))
				uniqueQuoteLetters.add(c);
		}
		Collections.shuffle(alphabetList);  //randomly order the alphabet arraylist
		
		for (Character C : uniqueQuoteLetters) {  //for each of the unique letters we will assign a random letter as it's encypted pair
			encryptedKeyMap.put(C, alphabetList.get(0));
			alphabetList.remove(0);
		}
		// Prevents any letters being mapped to themselves
		for (Character C : encryptedKeyMap.keySet()) {
			Character currentValue = encryptedKeyMap.get(C);
			if (C.equals(currentValue)) {
				encryptedKeyMap.put(C, alphabetList.get(0));
				alphabetList.remove(0);
				alphabetList.add(currentValue);
			}
		}

		for (char c : quote.toCharArray()) {
			if (Character.isLetter(c))
				encryptedQuote.add(encryptedKeyMap.get(c));
			//Adding to uniqueletters arraylist ot be used.
			if (!uniqueLetters.contains(encryptedKeyMap.get(c))) {
				uniqueLetters.add(encryptedKeyMap.get(c));
			}
			//System.out.println(encryptedKeyMap.get(c));
		}

		for(Character s : encryptedQuote) {
			finalEncryptedQuote += s + " ";
		}
	}
	
	//THESE WILL BE DELETED JUST FOR TESTING
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
	
	
	//Will be used to get input for tests
	public static void getInput() {
		Scanner input = new Scanner(System.in);
		String s = input.nextLine();
		char FirstChar = s.charAt(0);
		char SecondChar = s.charAt(2);
	}


	public void userWelcome() {
		System.out.println("Hello Player welcome to our cryptogram program!\n");
		System.out.println("Please Type generate to start! \n");
		String generationCheck = readInput();
		Boolean generateStart = false;

		while(!generationCheck.equalsIgnoreCase("generate")) {
			System.out.println("Invalid input, please type generate to start.");
			generationCheck = readInput();
			}
		System.out.println("Your cyrptogram is \n");
	}



	public String readInput() {
		Scanner input = new Scanner(System.in);
		String userinput = input.nextLine();
		if(userinput.equalsIgnoreCase("exit"))
			System.exit(0);
		return userinput;
	}



	public void DisplayCryptoGram() {
		System.out.println(finalEncryptedQuote);
	}

	public void gameStart() {
		String value;
		char userGuess;
		DisplayCryptoGram();
		if(currentLetter == uniqueLetters.size())
			if(complete() == true) {
				System.out.println("You have completed the cyrptogram good job!");
				System.exit(0);
			} else {
				System.out.println("You have failed to complete the crytogram, Resetting all the values and try again.");
				currentLetter = 0;
				userLetters.clear();
				return;
			}
		System.out.println("Please enter a singular letter value for cyrptogram letter " + uniqueLetters.get(currentLetter));
		value = readInput();
		//Setting to lower case so switch doesn't break.
		value.toLowerCase();
		while(value.length() > 1 | value.length() == 0) {
			switch(value) {
				case ("exit"):
					System.exit(0);
					break;
				case("undo"):
					undo();
					return;
				case("clear"):
					clear();
					return;
			}
			System.out.println("Invalid input, Please enter a singular letter value for cyrptogram letter " + uniqueLetters.get(currentLetter));
			value = readInput();
		}
		userGuess = value.charAt(0);
		userLetters.add(userGuess);
		currentLetter++;
	}

	public void userCryptogram() {
		if(currentLetter == 0)
			return;
		System.out.println("You current mapping is");
		for(int i = 0; i <= currentLetter-1; i++) {
			System.out.print(uniqueLetters.get(i) + " = " + userLetters.get(i) + " ");
		}
		System.out.println("");
	}

	public void undo () {
		if (currentLetter == 0){
			System.out.println("You cannot undo anymore letters");
			return;
	}
		userLetters.remove(currentLetter-1);
		currentLetter --;
		System.out.println("You have undone your previous letter for the cryptogram.");
		userCryptogram();

	}

	public void clear() {
		System.out.println("Clearing all mappings");
		currentLetter = 0;
		userLetters.clear();
	}

	public boolean complete() {
		boolean finished = true;
		for(int i = 0; i <= currentLetter-1; i++) {
			if(!uniqueQuoteLetters.contains(userLetters.get(i)))
				finished = false;
		}
		return finished;
	}

}
