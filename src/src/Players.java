import java.util.ArrayList;

/**
 * Represents all users who play the game.
 */
public class Players
{
	ArrayList<Player> players;
	
    public void addPlayer(Player player)
    {
        players.add(player);
    }

    public void savePlayers()
    {
        /*
        private void completesave() {
            String fileName  = "Players/" + userName;
            try 
            {
                clearFile(fileName);
                writeToFile(fileName, currentPlayer.getUsername());
                writeToFileInt(fileName, currentPlayer.getCorrectGuesses());
                writeToFileInt(fileName, currentPlayer.getTotalGuesses());
                writeToFileInt(fileName, currentPlayer.getPlayedCryptograms());
                writeToFileInt(fileName, currentPlayer.getCompletedCryptograms());
                System.exit(0);
            }
            catch (IOException e) // If file not found
            {
                System.out.println("\nCannot find players files to save data lost all current data is lost.");
                System.exit(0);
            }
            
        } 
        */
    }

    public Player findPlayer(String userName)
    {
        return null;
    }

    public int getTotalAccuracies()
    {
        int accuracy = 0;

        for (Player player : players)
        {
            accuracy += player.getAccuracy();
        }

        return accuracy;
    }

    public int getTotalPlayedCryptograms()
    {
        int playedCryptograms = 0;

        for (Player player : players)
        {
            playedCryptograms += player.getPlayedCryptograms();
        }

        return playedCryptograms;
    }

    public int getTotalCompletedCryptograms()
    {
        int completedCryptograms = 0;

        for (Player player : players)
        {
            completedCryptograms += player.getCompletedCryptograms();
        }

        return completedCryptograms;
    }
}
