package main;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import game_interface.ASCII;
import game_objects.HumanPlayerTest;
import the_game.GameTest;

@RunWith(Suite.class)
@SuiteClasses({ GameTest.class, ASCII.class, HumanPlayerTest.class })

/**
 * Literally exists to only run all the tests simultaneously.
 *
 */
public class AllTests {

}
