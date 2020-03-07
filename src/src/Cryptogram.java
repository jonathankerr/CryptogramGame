/**
 * Base class for cryptograms.
 */
public class Cryptogram 
{	
	String phrase = "HelloWorld";  //The phrase in which we will encrypt
	LetterCryptogram crypto = new LetterCryptogram(phrase);
}