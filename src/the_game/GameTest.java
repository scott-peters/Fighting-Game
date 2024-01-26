package the_game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import game_objects.HumanPlayer;
import game_objects.Player;
import the_game.Game.Team;

/**
 * Test's several characteristics of the Game class.
 * 
 * @author Lucas Brown
 */
public class GameTest {

	private PlayableCharacter testCharacter;

	/**
	 * This is a test of Game.
	 */
	public GameTest() {
		try {
			// Doesn't matter which one we choose.
			String filePath = (new File("")).getAbsolutePath()
					+ "/src/game_objects/CharacterSelection/Kermit".replace("/", System.getProperty("file.separator"));
			testCharacter = new PlayableCharacter(filePath);
		} catch (IOException e) {
		}
	}

	@Test
	/**
	 * Test that the default constructor does not create its own players.
	 */
	public void testNullConstructor() {
		Game game = new Game(null, null);
		assertEquals(game.getPlayer(Team.LEFT), null);
		assertEquals(game.getPlayer(Team.RIGHT), null);
	}

	@Test
	/**
	 * Test the constructor encapsulation.
	 */
	public void testConstructorEncapsulation() {
		Player p1 = new HumanPlayer(Team.LEFT, testCharacter);
		Player p2 = new HumanPlayer(Team.RIGHT, testCharacter);
		Game game = new Game(p1, p2);
		assertEquals(game.getPlayer(Team.LEFT), p1);
		assertEquals(game.getPlayer(Team.RIGHT), p2);
		assertTrue(game.getPlayer(Team.LEFT) != p1);
		assertTrue(game.getPlayer(Team.RIGHT) != p2);
	}

	@Test
	/**
	 * Tests the encapsulation of the player setter method.
	 */
	public void testPlayerSetterEncapsulation() {
		Player p1 = new HumanPlayer(Team.LEFT, testCharacter);
		Player p2 = new HumanPlayer(Team.RIGHT, testCharacter);
		Game game = new Game(null, null);
		game.setPlayer(Team.LEFT, p1);
		game.setPlayer(Team.RIGHT, p2);
		assertTrue(game.getPlayer(Team.LEFT) != p1);
		assertTrue(game.getPlayer(Team.RIGHT) != p2);
	}

	@Test
	/**
	 * Test to make sure a player can't be assigned to the wrong team.
	 */
	public void testPlayerSetterIncorrectTeam() {
		Player p1 = new HumanPlayer(Team.LEFT, testCharacter);
		Game game = new Game(null, null);
		game.setPlayer(Team.RIGHT, p1);
		assertTrue(game.getPlayer(Team.LEFT) == null);

		Player p2 = new HumanPlayer(Team.RIGHT, testCharacter);
		game = new Game(null, null);
		game.setPlayer(Team.LEFT, p2);
		assertTrue(game.getPlayer(Team.RIGHT) == null);
	}
}
