import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class gameTest {
	protected Player testPlayer  = new Player("Test",2,4,8,1);
	protected Cryptogram testNCryptogram = new NumberCryptogram("Test");
	protected Cryptogram testLCryptogram = new LetterCryptogram("Test");
	protected Game testgame = new Game();

	@BeforeEach
	void setUp(){
		//Tests to see if any errors occur when calling createMapping()
		testNCryptogram.createMapping();
		testLCryptogram.createMapping();
	}
	
	@Test
	void testPlayer() {
		//Testing player getters
		assertTrue(testPlayer.getUsername() == "Test");
		assertTrue(testPlayer.getCorrectGuesses() == 2);
		assertTrue(testPlayer.getTotalGuesses() == 4);
		assertTrue(testPlayer.getCompletedCryptograms() == 1);
		assertTrue(testPlayer.getPlayedCryptograms() == 8);
		//Uses setters/Incrementors for player
		int temp = testPlayer.getCorrectGuesses();
		testPlayer.incrementCorrectGuesses();;

		int temp3 = testPlayer.getTotalGuesses();
		testPlayer.incrementTotalGuesses();
		
		int temp4 = testPlayer.getCompletedCryptograms();
		testPlayer.incrementCompletedCryptograms();
		
		int temp5 = testPlayer.getPlayedCryptograms();
		testPlayer.incrementPlayedCryptograms();
		
		double temp2 = testPlayer.getAccuracy();	
		testPlayer.setUsername("Test2");
		//Tests Setters for players
		assertTrue(testPlayer.getUsername() == "Test2");
		assertTrue(temp == (testPlayer.getCorrectGuesses())-1);
		assertTrue(temp2 == ((double)3/(double)5)*100);
		assertTrue(temp3 == (testPlayer.getTotalGuesses())-1);
		assertTrue(temp4 ==  (testPlayer.getCompletedCryptograms())-1);
		assertTrue(temp5 ==  (testPlayer.getPlayedCryptograms())-1);
		
		}
		
	
	@Test
	void testNcryptogram() {
		//Tests getChar by comparing it to the phrase used for test
		assertTrue(testNCryptogram.getChar(0) == 'T');
		assertTrue(testNCryptogram.getChar(1) == 'E');
		assertTrue(testNCryptogram.getChar(2) == 'S');
		assertTrue(testNCryptogram.getChar(3) == 'T');
		//Tests getEncryptedChar by getting encrypted message then putting it into char array and comparing with getgetEncryptedChar result
		char[] z = new char[15];
		String EncryptedPharse = testNCryptogram.getEncryptedPhrase();
		EncryptedPharse.getChars(0,EncryptedPharse.length(), z, 0);
		for(int t2 =0;t2<4;t2++) {
		assertTrue(testNCryptogram.getEncryptedChar(t2) == z[t2*2]);
		}
		//Test getUniqueChars by comparing it with a array that has all of the unique chars from phrase Test
		Character[] x = {'T', 'e', 's'};
		for(int t = 0;t<3;t++) {
			assertTrue(testNCryptogram.getUniqueChars().get(t) == x[t]);
		}
		
	}
	@Test
	void testLcryptogram() {
		//Tests getChar by comparing it to the phrase used for test
		assertTrue(testLCryptogram.getChar(0) == 'T');
		assertTrue(testLCryptogram.getChar(1) == 'E');
		assertTrue(testLCryptogram.getChar(2) == 'S');
		assertTrue(testLCryptogram.getChar(3) == 'T');
		//Tests getEncryptedChar by getting encrypted message then putting it into char array and comparing with getgetEncryptedChar result
		char[] z = new char[15];
		String EncryptedPharse = testLCryptogram.getEncryptedPhrase();
		EncryptedPharse.getChars(0,EncryptedPharse.length(), z, 0);
		for(int t2 =0;t2<4;t2++) {
		assertTrue(testLCryptogram.getEncryptedChar(t2) == z[t2*2]);
		}
		//Test getUniqueChars by comparing it with a array that has all of the unique chars from phrase Test
		Character[] x = {'T', 'e', 's'};
		for(int t = 0;t<3;t++) {
			assertTrue(testLCryptogram.getUniqueChars().get(t) == x[t]);
			
		}
	}

@Test
void testGame() throws IOException {
	testgame.clearFile("Players/test");
	testgame.writeToFile("Players/test", "Test");
	testgame.writeToFileInt("Players/test", 123);
	ArrayList<String> testData = new ArrayList(Files.readAllLines(Paths.get("Players/test")));
	assertTrue(testData.get(0).equals("Test"));
	assertTrue(testData.get(1).equals("123"));
	
}
}
