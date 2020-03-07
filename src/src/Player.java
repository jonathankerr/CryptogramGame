/**
 * Represents a user who plays the game.
 */
public class Player 
{
	private String username;
	private float accuracy;
	private int totalGuesses;
	private int playedCryptograms;
	private int completedCryptograms;
	
	/**
	 * Gets player's accuracy.
	 * 
	 * @return player's accuracy.
	 */
	public float getAccuracy()
	{
		return accuracy;
	}
	
	/**
	 * Sets player's accuracy.
	 * 
	 * @param accuracy target accuracy.
	 */
	public void setAccuracy(int accuracy)
	{
		this.accuracy = accuracy;
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
}
