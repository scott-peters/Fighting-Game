package game_interface;

import the_game.Game;

/**
 * Implementations of this interface are meant to be visual representations of
 * the Game object. They should not attempt to modify the Game.
 */
public interface GameInterface {

	// A flag to control the use of ascii or gui.
	/**
	 * Flag used to control the use of ASCII or GUI
	 */
	public static final boolean IS_ASCII = false;

	/**
	 * Draw the game at a given instant.
	 * 
	 * @param game an instance of the game to be drawn.
	 */
	public abstract void draw(Game game);

	/**
	 * Get the time in milliseconds it should take each round to occur.
	 * 
	 * @return the time in milliseconds it should take each round to occur.
	 */
	public abstract double getRuntimeInterval();

	/**
	 * Get the time in milliseconds each time step should record. for example, at
	 * 30fps this should return 1/30 seconds.
	 * 
	 * @return the time in milliseconds each time step should record.
	 */
	public abstract double getGametimeInterval();

}