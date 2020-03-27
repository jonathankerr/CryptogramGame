import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents all users who play the game.
 */
public class Players
{
    ArrayList<Player> players;
    
    public Players()
    {
        players = new ArrayList<Player>();
    }
	
    public void addPlayer(Player player)
    {
        players.add(player);
    }

    public Player getPlayer(String username)
    {
        Player player = (Player)Arrays.stream(players.toArray()).filter(x -> ((Player)x).getUsername().equals(username)).findFirst().orElse(null);

        return player;
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public boolean fetchPlayers()
    {
		File[] files = new File("Players/Users").listFiles(); 
    
        if (files.length > 0)
        {
            for (File file : files) 
            {
                ArrayList<String> playerData = new ArrayList<String>();

                try (Scanner fileReader = new Scanner(file)) 
                {
                    while (fileReader.hasNextLine())
                    {
                        playerData.add(fileReader.nextLine());
                    }

                    fileReader.close();
                }
                catch (FileNotFoundException e)
                { }

                Player player = new Player(playerData.get(0).toString(), Integer.parseInt(playerData.get(1)), Integer.parseInt(playerData.get(2)), Integer.parseInt(playerData.get(3)), Integer.parseInt(playerData.get(4)));
                players.add(player);
            }

            return true;
        }
        
        return false;
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
