import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Represents a user who plays the game.
 */
public class Player implements Comparable
{
	private String username;
	private int correctGuesses;
	private int totalGuesses;
	private int playedCryptograms;
	private int completedCryptograms;
	
	public Player(String string, int i, int j, int k, int l) {
		
	
	  username = string;
	  correctGuesses = i;
	  totalGuesses = j;
	  playedCryptograms = k;
	  completedCryptograms = l;
	}
	
	@Override
	public int compareTo(Object compPlayer) {
		int compareScore =((Player)compPlayer).getCompletedCryptograms();
		//return this.completedCryptograms-compareScore;
		return compareScore-this.completedCryptograms;
	}
	
    @Override
    public String toString() {
        return "Username: " + username + ", Completed Cryptograms: " + completedCryptograms + "";
    }

	
	
	public double getAccuracy()
	{
		if (totalGuesses == 0)
		{
			return 0;
		}
		else 
		{
			return ((double) correctGuesses/(double)totalGuesses * 100);
		}
	}
	
	public void  incrementCorrectGuesses()
	{
		correctGuesses++;
	}
	
	public void  incrementTotalGuesses()
	{
		totalGuesses++;
	}
	
	public int getTotalGuesses()
	{
		return totalGuesses;
	}
	
	public int getCorrectGuesses()
	{
		return correctGuesses;
	}

	/**
	 * Gets number of cryptograms played.
	 * 
	 * @return number of cryptograms played.
	 */
	public int getPlayedCryptograms()
	{
		return playedCryptograms;
	}
	
	/**
	 * Increments the number of cryptograms played.
	 */
	public void incrementPlayedCryptograms()
	{
		playedCryptograms++;
	}
	
	/**
	 * Gets the number of cryptograms completed.
	 * 
	 * @param completedCryptograms the number of cryptograms completed.
	 */
	public int getCompletedCryptograms()
	{
		return completedCryptograms;
	}
	
	/**
	 * Increments the number of cryptograms completed.
	 */
	public void incrementCompletedCryptograms()
	{
		completedCryptograms++;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void saveSession(String phrase, String encryptedPhrase, HashMap<Character, Character> userGuesses)
	{
		try 
		{
			String s = "";
			String t = "";

			for (Character c : userGuesses.keySet())
			{
				s += c;
				t += userGuesses.get(c);
			}

			FileOutputStream outputStream;
			try {
				outputStream = new FileOutputStream("Players/Sessions/" + username, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			outputStream.write(System.getProperty("line.separator").getBytes());
			outputStream.write(encryptedPhrase.getBytes());
			outputStream.write(System.getProperty("line.separator").getBytes());
			outputStream.write(s.getBytes());
			outputStream.write(System.getProperty("line.separator").getBytes());
			outputStream.write(t.getBytes());
			outputStream.write(System.getProperty("line.separator").getBytes());
			outputStream.close();
		}
		catch (FileNotFoundException e) // If file not found
		{
			System.out.println("Error.");
		}
	}

	public ArrayList<String> getSession()
	{
		try
		{
			ArrayList<String> session = new ArrayList(Files.readAllLines(Paths.get("Players/Session/" + username)));

			return session;
		}
		catch (java.lang.IndexOutOfBoundsException e) 
		{ }
		
		return null;
	}

	public void saveData()
	{
		try 
		{
			String s = "";
			String t = "";

			for (char c : userGuesses.keySet()) 
			{
				s += c;
				t += userGuesses.get(c);
			}

			FileOutputStream outputStream = new FileOutputStream("Players/Users/" + username, false);
	
			outputStream.write(username.getBytes());
			outputStream.write(System.getProperty("line.separator").getBytes());
			outputStream.write(correctGuesses.getBytes());
			outputStream.write(System.getProperty("line.separator").getBytes());
			outputStream.write(totalGuesses.getBytes());
			outputStream.write(System.getProperty("line.separator").getBytes());
			outputStream.write(playedCryptograms.getBytes());
			outputStream.write(System.getProperty("line.separator").getBytes());
			outputStream.write(completedCryptograms.getBytes());
			outputStream.write(System.getProperty("line.separator").getBytes());
			outputStream.close();
		}
		catch (IOException e) // If file not found
		{
			System.out.println(

@Override
		public int compareTo(Object o) {
			// TODO Auto-generated method stub
			return 0;
		}"Error.");
		}
	}
}
