public class LetterCryptogram extends Cryptogram 
{	
	private char[] alphabet = 
	{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' 
	};

	/**
	 * Class constructor.
	 * Overrides base constructor.
	 * 
	 * @param phrase phrase that is encrypted.
	 */
	public LetterCryptogram(String phrase) 
	{
		super(phrase);

		for (char c : alphabet) 
		{
			alphabetList.add(c);
		}
	}
}	