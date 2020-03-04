public class Player 
{
	private String username;
	private float accuracy;
	private int totalGuesses;
	private int playedCryptograms;
	private int completedCryptograms;
	
	public float getAccuracy()
	{
		return accuracy;
	}
	
	public void setAccuracy(int accuracy)
	{
		this.accuracy = accuracy;
	}
	
	public int getPlayedCryptograms()
	{
		return playedCryptograms;
	}
	
	public void incrementPlayedCryptograms()
	{
		playedCryptograms++;
	}
	
	public int getCompletedCryptograms()
	{
		return completedCryptograms;
	}
	
	public void incrementCompletedCryptograms()
	{
		completedCryptograms++;
	}
}
