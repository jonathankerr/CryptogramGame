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



}
