public class NumberCryptogram extends Cryptogram 
{	
	private char[] alphabet = 
	{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	/**
	 * Class constructor.
	 * Overrides base constructor.
	 * 
	 * @param phrase phrase that is encrypted.
	 */
	public NumberCryptogram(String phrase) 
	{
		super(phrase);

		for (char c : alphabet) 
		{
			alphabetList.add(c);
		}
	}
}	