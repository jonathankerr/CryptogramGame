import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

/**
 */
public class Cryptogram 
{	
	protected char[] alphabet =
	{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
		'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' 
	};

	private ArrayList<Character> alphabetList;
	private ArrayList<Character> uniqueChars; // List of unique character in the phrase.
	private ArrayList<Character> uniqueEncryptedChars; // List of unique character in the phrase.
	private HashMap<Character, Character> charMap;

	protected String phrase;
	private String encryptedPhrase;

	public Cryptogram(String phrase)
	{
		alphabetList = new ArrayList<Character>();
		uniqueChars = new ArrayList<Character>();
		uniqueEncryptedChars = new ArrayList<Character>();
		charMap = new HashMap<Character, Character>();

		this.phrase = phrase;

		for (char c : alphabet) 
		{
			alphabetList.add(c);
		}
	}

	public boolean getCompletionStatus(int currentCharIndex, HashMap<Character, Character> userGuesses)
	{
		boolean isCompleted = true;

		for (Character c : charMap.keySet())
		{
			char value = Character.toLowerCase(userGuesses.get(charMap.get(c)));  

			if (c != value)
			{
				isCompleted = false;
	
				break;
			}
		} 
		

		return isCompleted;
	}

	public char getChar(int currentCharIndex)
	{
		return Character.toUpperCase(uniqueChars.get(currentCharIndex));
	}

	public char getEncryptedChar(int currentCharIndex)
	{
		return Character.toUpperCase(uniqueEncryptedChars.get(currentCharIndex));
	}

	public ArrayList<Character> getUniqueChars()
	{
		return uniqueChars;
	}

	public String getEncryptedPhrase()
	{
		return encryptedPhrase.replaceAll(".(?!$)", "$0 ");
	}

	/**
	 * Maps each character in the phrase to a letter from the alphabet.
	 */
	public void createMapping() 
	{
		StringBuilder builder = new StringBuilder(phrase.length());

		for (char c : phrase.toCharArray()) 
		{
			if (!uniqueChars.contains(c))
			{
				uniqueChars.add(c);
			}
		}
		
		Collections.shuffle(alphabetList);
		
		// For each of the unique letters, assign a random letter as it's encypted pair.
		for (char c : uniqueChars) 
		{  
			charMap.put(c, alphabetList.get(0));
			alphabetList.remove(0);
		}
		
		// Prevents any letters being mapped to themselves.
		for (char c : charMap.keySet()) 
		{
			char currentValue = charMap.get(c);
			
			if (c == currentValue) 
			{
				charMap.put(c, alphabetList.get(0));
				alphabetList.remove(0);
				alphabetList.add(currentValue);
			}
		}
	
		// Construct string from encrypted characters.
		for (char c : phrase.toCharArray()) 
		{
			if (!uniqueEncryptedChars.contains(charMap.get(c)))
			{
				uniqueEncryptedChars.add(charMap.get(c));
			}

			builder.append(charMap.get(c));
		}

		encryptedPhrase = builder.toString();
	}

}