import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class LetterCryptogram {
	
	static String quote;
	//Creating an arraylist containing all possible letters in the cryptogram
	char[] alphabetForGram = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	
	
	static //Adds each letter to an arraylist, alphabetList
	List<Character> alphabetList = new ArrayList<Character>(); {
		for (char c : alphabetForGram) {
			alphabetList.add(c);
		}
	}
	
	
	//The following are only declared for testing purposes
	static Map<Character, Character> encryptedKeyMap = new HashMap<Character, Character>();;
	static List<Character> encryptedQuote = new ArrayList<Character>();
	static String finalEncryptedQuote = "";
	static String finalUserProgress = "";
	static List<String> userProgress = new ArrayList<String>();
	
	
	
	//Constructor
	public LetterCryptogram(String Phrase) {
		
		quote = Phrase;
		createMapping();
		Tests();
	}
	
	
	public static void createMapping() {
		
		Set<Character> uniqueLetters = new HashSet<Character>();
		
		
		//Finding each unique letter in the quote
		Set<Character> uniqueQuoteLetters = new HashSet<Character>();
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
	
	
	
	
	
	
}