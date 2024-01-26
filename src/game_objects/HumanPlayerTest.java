package game_objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Test;

import game_interface.ModelsAndViewsController;
import the_game.PlayableCharacter;
import the_game.Game.Team;

/**
 * Assorted tests for ensuring the HumanPlayer class is working properly.
 * 
 */
public class HumanPlayerTest {

	private final PlayableCharacter pc;

	/**
	 * This tests HumanPlayer
	 */
	public HumanPlayerTest() {
		this.pc = ModelsAndViewsController.ALL_PLAYABLE_CHARACTERS[0]; // Select a random pc, doesn't matter which.
	}

	@Test
	/**
	 * This tests constructors
	 */
	public void testConstructor() {
		HumanPlayer hp = new HumanPlayer(Team.LEFT, this.pc);
		assertEquals(Team.LEFT, hp.getTeam());
		assertEquals(this.pc.getHealth(), hp.getMaxHealth());
		assertEquals(this.pc.getHeight(), hp.getHeight());
		assertEquals(this.pc.getWidth(), hp.getWidth());
		assertEquals(this.pc.getSize(), hp.getSize());
		assertTrue(this.pc.getSize() != hp.getSize()); // Should contain the same information, should not reference the
														// same object.
		assertEquals(this.pc.getSpeed(), hp.getMaxSpeed());
	}

	@Test
	/**
	 * This tests copy contructors.
	 */
	public void testCopyConstructor() {
		HumanPlayer hp1 = new HumanPlayer(Team.LEFT, this.pc);
		HumanPlayer hp2 = new HumanPlayer(hp1);
		assertTrue(hp1 != hp2);
		assertEquals(hp1, hp2);
	}

	@Test
	/**
	 * This tests the copy method.
	 */
	public void testCopyMethod() {
		HumanPlayer hp1 = new HumanPlayer(Team.LEFT, this.pc);
		HumanPlayer hp2 = hp1.copy();
		assertTrue(hp1 != hp2);
		assertEquals(hp1, hp2);
	}

	@Test
	/**
	 * This tests to see that there are no constants.
	 */
	public void testNoConstants() {
		boolean hasConstant = false;
		Scanner sc = null;
		try {
			sc = new Scanner(new File(new File("").getAbsolutePath() + "/src/game_objects/HumanPlayer.java"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String nextLine;
		while (sc.hasNext() && !hasConstant) {
			nextLine = sc.nextLine();
			hasConstant = nextLine.matches("(.)*(\\d)(.)*") && !nextLine.contains("0");
			if(hasConstant) {
				System.out.println(nextLine);
			}
		}
		assertTrue(!hasConstant);
	}
}
