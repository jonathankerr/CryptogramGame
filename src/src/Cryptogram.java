import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

/**
 */
public class Cryptogram 
{
	protected ArrayList<Character> alphabetList;
	private ArrayList<Character> uniqueChars; // List of unique character in the phrase.
	private ArrayList<Character> uniqueEncryptedChars; // List of unique character in the phrase.
	private HashMap<Character, Character> charMap; // Hash map of each character in the phrase mapped to a random character from the "alphabetList".

	protected String phrase; // Unencrypted phrase.
	private String encryptedPhrase; // Encrypted phrase.

	/**
	 * Class constructor.
	 * 
	 * @param phrase phrase that should be encrypted and solved by the user.
	 */
	public Cryptogram(String phrase)
	{
		alphabetList = new ArrayList<Character>();
		uniqueChars = new ArrayList<Character>();
		uniqueEncryptedChars = new ArrayList<Character>();
		charMap = new HashMap<Character, Character>();

		this.phrase = phrase;
	}

	/**
	 * Gets unique character in phrase by index.
	 * 
	 * @param currentCharIndex index of character user is currently guessing.
	 * @return unique character.
	 */
	public char getChar(int currentCharIndex)
	{
		return Character.toUpperCase(uniqueChars.get(currentCharIndex));
	}
	public String getPhrase()
	{
		return phrase;
	}
	public void setPhrase(String Phrase)
	{
		this.phrase = Phrase;
		
	}
	/**
	 * Gets unique character in encrypted phrase by index.
	 * 
	 * @param currentCharIndex index of character user is currently guessing.
	 * @return unique encrypted character.
	 */
	public char getEncryptedChar(int currentCharIndex)
	{
		return Character.toUpperCase(uniqueEncryptedChars.get(currentCharIndex));
	}

	/**
	 * Gets ecrypted phrase, with spaces in between every character.
	 * 
	 * @return encrypted phrase.
	 */
	public String getEncryptedPhrase()
	{
		return encryptedPhrase.replaceAll(".(?!$)", "$0 ");
	}
	public void setEncryptedPhrase(String Phase,String nValues)
	{
		ArrayList<Character> newUChars = makeUChars(Phase) ;
		this.uniqueEncryptedChars = newUChars;
		this.encryptedPhrase = Phase;
		charMap.clear();
		this.uniqueChars = makeUChars(this.phrase);
		for(int i = 0;i<uniqueChars.size();i++) {
			charMap.put(uniqueChars.get(i),uniqueEncryptedChars.get(i));
		}
	}

	/**
	 * Gets list of unique characters in phrase.
	 * 
	 * @return list of unique character.
	 */
	
	
	public ArrayList<Character>  makeUChars(String Phrase) { 
	ArrayList<Character> knownChars = new ArrayList<Character>();
	for (char c : Phrase.toCharArray()) 
	{
		if (!knownChars.contains(c))
		{
			knownChars.add(c);
		}
	}
	return knownChars;
	
	}
	public ArrayList<Character> getUniqueChars()
	{
		return uniqueChars;
	}

	/**
	 * Determines whether cryptogram is completed
	 * 
	 * @param userGuesses Hash map of guesses for each character by the user.
	 * @return true if cryptogram is completed, false if it is not.
	 */
	public boolean getCompletionStatus(HashMap<Character, Character> userGuesses)
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
