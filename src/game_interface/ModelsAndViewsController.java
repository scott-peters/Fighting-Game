package game_interface;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import game_interface.GUI.Window;
import game_objects.AIPlayer;
import game_objects.HumanPlayer;
import javafx.event.EventHandler;
import the_game.ObjectLoader;
import the_game.Game;
import the_game.PlayableCharacter;
import the_game.Game.Team;

/**
 * 
 * An intermediate stage between the back end and front end, controlling player
 * key strokes as well as mouse clicks and gui interactions such as button
 * clicks. All shared information is stored, distributed, and manipulated
 * through this class.
 *
 *
 */
public final class ModelsAndViewsController {

	/**
	 * This is the PlayableCharacter array for all PlayableCharacters.
	 */
	public static final PlayableCharacter[] ALL_PLAYABLE_CHARACTERS;
	private final Game theGame;
	private final ASCII ASCII_interface;
	private final GUI GUI_interface;

	/**
	 * This is the keyboard used to listen for key presses.
	 */
	public static Keyboard keyboard;

	private final Timer gameTimer;

	/**
	 * Load all playable characters and store them for public use.
	 */
	static {
		String filePath = (new File("")).getAbsolutePath()
				+ "/src/game_objects/CharacterSelection".replace("/", System.getProperty("file.separator"));
		ALL_PLAYABLE_CHARACTERS = (new ObjectLoader<PlayableCharacter>(filePath, PlayableCharacter.class))
				.getAllObjects();
	}

	/**
	 * This constructs a controller for the game and the game interface. Sets the
	 * game, game interface, timer and starts the threads for the timer tasks and
	 * RuntimeInterval
	 * 
	 * @param aGame          the game
	 * @param aGameInterface the game interface
	 */
	public ModelsAndViewsController(Game aGame, GameInterface aGameInterface) {
		this.theGame = aGame;
		if (GameInterface.IS_ASCII) {
			PlayableCharacter pc = this.getPCFromIcon(getPlayerIcon("Kermit")); // Default Player
			this.theGame.setPlayer(Team.LEFT, new HumanPlayer(Team.LEFT, pc));
			this.theGame.setPlayer(Team.RIGHT, new HumanPlayer(Team.RIGHT, pc));
		}
		// Convert aGameInterface into either an ASCII or GUI.
		this.ASCII_interface = aGameInterface instanceof ASCII ? (ASCII) aGameInterface : null;
		this.GUI_interface = aGameInterface instanceof GUI ? (GUI) aGameInterface : null;
		ModelsAndViewsController.keyboard = new Keyboard(); // Create the keyboard.
		// Cretae the timer and start it immediately if it's ascii.
		this.gameTimer = new Timer(true);
		if (GameInterface.IS_ASCII) {
			this.gameTimer.schedule(new asciiTimerTask(), (long) this.ASCII_interface.getRuntimeInterval());
		} else {
			this.initController();
		}
	}

	/**
	 * 
	 * This updates the GUI version of the game until the game is over.
	 *
	 */
	private class guiTimerTask implements Runnable {

		@Override
		/**
		 * Update, display, repeat.
		 */
		public void run() {
			theGame.updateGame(GUI_interface.getGametimeInterval() / 1000d);
			GUI_interface.draw(theGame);
			if (theGame.isGameOver()) {
				gameTimer.stop();
			}
		}

	}

	/**
	 * 
	 * This class sets up the timer to print out the game at specific intervals.
	 *
	 */
	private class asciiTimerTask implements Runnable {

		/**
		 * This takes the input from the players after enter has been pressed.
		 */
		public asciiTimerTask() {
			System.out.println(Game.GAME_INTRO);
			while (!ModelsAndViewsController.keyboard.isKeyPressed(KeyCode.ENTER))
				; // Wait for enter to be pressed. this will freeze pretty much everything else.

			ModelsAndViewsController.keyboard.manualRelease(KeyCode.ENTER); // Release Enter
		}

		@Override
		/**
		 * Update, display, repeat.
		 */
		public void run() {
			theGame.updateGame(ASCII_interface.getGametimeInterval() / 1000d); // Update the game.
			if (theGame.isGameOver()) { // Stop if the game is over.
				gameTimer.stop();
			}
			ASCII_interface.draw(theGame); // Draw the game on screen/command line
			keyboard.manualReleaseAll(); // Release all keys
			while (!keyboard.isKeyPressed(KeyCode.ENTER) && !Thread.interrupted()) {
			} // while the timer has not re-called this method and if enter has not been
			// pressed, i.e. the next move has not been initiated.
			if (!Thread.interrupted()) {
				gameTimer.reset(); // Reset the timer early.
			}
		}

	}

	/**
	 * 
	 * A basic implementation of a KeyListener. Use the isKeyPressed method in order
	 * to determine if a given key is pressed at any time.
	 *
	 */
	public class Keyboard {

		private final boolean[] keysPressed;

		private final TerminalKeyThread terminalKeyThread;

		/**
		 * This takes input from the keyboard. This also creates the thread to always
		 * ensure the checking of the input stream asynchronously. Daemon forces the
		 * thread to close if the main program closes.
		 */
		public Keyboard() {
			if (!GameInterface.IS_ASCII) {
				this.initKeyEventHandlers();
			}
			this.keysPressed = new boolean[KeyCode.values().length];
			Arrays.fill(this.keysPressed, false);
			this.terminalKeyThread = new TerminalKeyThread(); // Create the thread to always ensure the checking of the
			// input stream asynchronously.
			this.terminalKeyThread.setDaemon(true); // IMPORTANT! Forces the thread to close if the main program closes
			this.terminalKeyThread.start();
		}

		/**
		 * Create all relevant key event handlers.
		 */
		private void initKeyEventHandlers() {

			GUI_interface.getRoot().setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					keysPressed[event.getCode().getCode()] = true;
				}
			});

			GUI_interface.getRoot().setOnKeyReleased(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					keysPressed[event.getCode().getCode()] = false;
				}
			});

		}

		/**
		 * This sets the key that was pressed.
		 * 
		 * @param key   the key that was pressed
		 * @param state returns true is pressed, otherwise false
		 */
		private void setKey(KeyCode key, boolean state) {
			synchronized (this.keysPressed) {
				this.keysPressed[key.getCode()] = state;
			}
		}

		/**
		 * This gets what key was pressed.
		 * 
		 * @return a copy of the key that pressed
		 */
		public boolean[] getPressedKeys() {
			synchronized (this.keysPressed) {
				return this.keysPressed.clone();
			}

		}

		/**
		 * This checks to see if a key was pressed
		 * 
		 * @param key the key that was pressed
		 * @return if the key was pressed returns true, otherwise false
		 */
		public boolean isKeyPressed(KeyCode key) {
			synchronized (this.keysPressed) {
				return this.keysPressed[key.getCode()];
			}
		}

		/**
		 * Allows for the manual release of a given key IF running in ascii mode.
		 * 
		 * @param key the key to be released.
		 * @return whether the key release was accepted or not.
		 */
		public boolean manualRelease(KeyCode key) {
			if (ASCII.IS_ASCII) {
				this.setKey(key, false);
				return true;
			} else {
				return false;
			}
		}

		/**
		 * Allows for the manual release of a all keys IF running in ascii mode.
		 * 
		 * @return whether the action was completed or not.
		 */
		public boolean manualReleaseAll() {
			if (ASCII.IS_ASCII) {
				synchronized (this.keysPressed) {
					Arrays.fill(this.keysPressed, false);
				}
				return true;
			} else {
				return false;
			}
		}

		/**
		 * This thread continually reads System in for what keys have been pressed, it
		 * cannot read for releases unfortunately thus we allow for manual releasing of
		 * keys under controlled circumstances.
		 */
		private class TerminalKeyThread extends Thread {

			@Override
			/**
			 * Continually checks the System.in stream for a key pressed to convert into
			 * KeyCode's.
			 */
			public void run() {
				try {
					while (!Thread.interrupted()) { // Continue until the thread is interrupted/terminated.
						setKey(intToKeyCode(System.in.read()), true);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Takes an int that represents a utf-8 character and returns its corresponding
	 * KeyCode.
	 * 
	 * @param The int to convert.
	 * 
	 * @return A KeyCode representation of the given int. If none was found then
	 *         null is returned.
	 */
	private static KeyCode intToKeyCode(int toConvert) {
		// if()
		if (toConvert == 13) { // 13 is enter
			return KeyCode.ENTER;
		} else {
			KeyCode[] codes = KeyCode.values(); // Get all possible values
			String reference = ("" + ((char) toConvert)).toUpperCase(); // Convert utf-8 int to a string.
			for (KeyCode code : codes) {
				if (code.getChar().equals(reference)) {
					return code;
				}
			}
		}
		return null; // If no representation was found.
	}

	/**
	 * Control logic around changing screens based off of mouse click.
	 */
	public class MouseClick implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			Window screen = GUI_interface.getWindow();
			switch (screen) {
			case START:
				if (event.getTarget().equals(GUI_interface.getEventImage(Window.START).get(0))) {
					GUI_interface.soundSelected(Window.START, GUI_interface.getEventImage(Window.START).get(0));
					GUI_interface.setFightBackground(GUI_interface.getEventImage(Window.START).get(0));
					theGame.setPlayer(Team.RIGHT, new HumanPlayer(Team.RIGHT, ALL_PLAYABLE_CHARACTERS[1]));
					theGame.getCharacter(Team.RIGHT).getAllAnimations()
					.setAnimation(GUI_interface.getPlayerImage(Team.RIGHT), 0);
					theGame.setPlayer(Team.LEFT, new AIPlayer(Team.LEFT, ALL_PLAYABLE_CHARACTERS[0],
							theGame.getPlayer(Team.RIGHT)));
					theGame.getCharacter(Team.LEFT).getAllAnimations()
					.setAnimation(GUI_interface.getPlayerImage(Team.LEFT), 0);
					gameTimer.schedule(new guiTimerTask(), (long) GUI_interface.getRuntimeInterval(), 4500);
					GUI_interface.drawScreen(Window.FIGHT);
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.START).get(1))) {
					GUI_interface.soundSelected(Window.START, GUI_interface.getEventImage(Window.START).get(1));
					GUI_interface.drawScreen(Window.BACKGROUND);
				} else {
					GUI_interface.drawScreen(Window.START);
				}
				break;
			case BACKGROUND:
				if (event.getTarget().equals(GUI_interface.getEventImage(Window.BACKGROUND).get(0))) {
					GUI_interface.setFightBackground(GUI_interface.getEventImage(Window.BACKGROUND).get(0));
					GUI_interface.showSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(0));
					GUI_interface.soundSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(0));
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.BACKGROUND).get(1))) {
					GUI_interface.setFightBackground(GUI_interface.getEventImage(Window.BACKGROUND).get(1));
					GUI_interface.showSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(1));
					GUI_interface.soundSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(1));
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.BACKGROUND).get(2))) {
					GUI_interface.setFightBackground(GUI_interface.getEventImage(Window.BACKGROUND).get(2));
					GUI_interface.showSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(2));
					GUI_interface.soundSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(2));
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.BACKGROUND).get(3))) {
					GUI_interface.setFightBackground(GUI_interface.getEventImage(Window.BACKGROUND).get(3));
					GUI_interface.showSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(3));
					GUI_interface.soundSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(3));
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.BACKGROUND).get(4))) {
					GUI_interface.setFightBackground(GUI_interface.getEventImage(Window.BACKGROUND).get(4));
					GUI_interface.showSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(4));
					GUI_interface.soundSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(4));
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.BACKGROUND).get(5))) {
					GUI_interface.setFightBackground(GUI_interface.getEventImage(Window.BACKGROUND).get(5));
					GUI_interface.showSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(5));
					GUI_interface.soundSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(5));
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.BACKGROUND).get(6))) {
					GUI_interface.setFightBackground(GUI_interface.getEventImage(Window.BACKGROUND).get(6));
					GUI_interface.showSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(6));
					GUI_interface.soundSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(6));
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.BACKGROUND).get(7))) {
					GUI_interface.setFightBackground(GUI_interface.getEventImage(Window.BACKGROUND).get(7));
					GUI_interface.showSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(7));
					GUI_interface.soundSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(7));
				}

				if (event.getTarget().equals(GUI_interface.getEventImage(Window.BACKGROUND).get(9))) {
					GUI_interface.soundSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(9));
					GUI_interface.drawScreen(Window.CHARACTER1);
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.BACKGROUND).get(8))) {
					GUI_interface.soundSelected(Window.BACKGROUND,
							GUI_interface.getEventImage(Window.BACKGROUND).get(8));
					GUI_interface.drawScreen(Window.START);
				} else {
					GUI_interface.drawScreen(Window.BACKGROUND);
				}
				break;
			case CHARACTER1:
				if (event.getTarget().equals(GUI_interface.getEventImage(Window.CHARACTER1).get(0))) {
					GUI_interface.setPlayerOne(GUI_interface.getEventImage(Window.CHARACTER1).get(0));
					GUI_interface.showSelected(Window.CHARACTER1,
							GUI_interface.getEventImage(Window.CHARACTER1).get(0));
					GUI_interface.soundSelected(Window.CHARACTER1,
							GUI_interface.getEventImage(Window.CHARACTER1).get(0));
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.CHARACTER1).get(1))) {
					GUI_interface.setPlayerOne(GUI_interface.getEventImage(Window.CHARACTER1).get(1));
					GUI_interface.showSelected(Window.CHARACTER1,
							GUI_interface.getEventImage(Window.CHARACTER1).get(1));
					GUI_interface.soundSelected(Window.CHARACTER1,
							GUI_interface.getEventImage(Window.CHARACTER1).get(1));
				}
				if (event.getTarget().equals(GUI_interface.getEventImage(Window.CHARACTER1).get(2))) {
					GUI_interface.showSelected(Window.CHARACTER1, 
							GUI_interface.getEventImage(Window.CHARACTER1).get(2));
					if (!GUI_interface.isCharChosen1()) {
						GUI_interface.playUnselectedSound();
					}
					if(GUI_interface.isCharChosen1()) {
						GUI_interface.soundSelected(Window.CHARACTER1,
								GUI_interface.getEventImage(Window.CHARACTER1).get(2));
						GUI_interface.drawScreen(Window.HUMAN_AI1);
					}  
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.CHARACTER1).get(3))) {
					GUI_interface.soundSelected(Window.CHARACTER1,
							GUI_interface.getEventImage(Window.CHARACTER1).get(3));
					GUI_interface.drawScreen(Window.BACKGROUND);
				} else {
					GUI_interface.drawScreen(Window.CHARACTER1);
				}
				break;
			case HUMAN_AI1:
				if (event.getTarget().equals(GUI_interface.getEventImage(Window.HUMAN_AI1).get(0))) {
					GUI_interface.showSelected(Window.HUMAN_AI1, GUI_interface.getEventImage(Window.HUMAN_AI1).get(0));
					GUI_interface.soundSelected(Window.HUMAN_AI1, GUI_interface.getEventImage(Window.HUMAN_AI1).get(0));
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.HUMAN_AI1).get(1))) {
					GUI_interface.showSelected(Window.HUMAN_AI1, GUI_interface.getEventImage(Window.HUMAN_AI1).get(1));
					GUI_interface.soundSelected(Window.HUMAN_AI1, GUI_interface.getEventImage(Window.HUMAN_AI1).get(1));
				}
				if (event.getTarget().equals(GUI_interface.getEventImage(Window.HUMAN_AI1).get(2))) {
					GUI_interface.showSelected(Window.HUMAN_AI1, GUI_interface.getEventImage(Window.HUMAN_AI1).get(2));
					GUI_interface.soundSelected(Window.HUMAN_AI1, GUI_interface.getEventImage(Window.HUMAN_AI1).get(2));
					GUI_interface.drawScreen(Window.CHARACTER2);
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.HUMAN_AI1).get(3))) {
					GUI_interface.showSelected(Window.HUMAN_AI1, GUI_interface.getEventImage(Window.HUMAN_AI1).get(3));
					GUI_interface.soundSelected(Window.HUMAN_AI1, GUI_interface.getEventImage(Window.HUMAN_AI1).get(3));
					GUI_interface.drawScreen(Window.CHARACTER1);
				} else {
					GUI_interface.drawScreen(Window.HUMAN_AI1);
				}
				break;
			case CHARACTER2:
				if (event.getTarget().equals(GUI_interface.getEventImage(Window.CHARACTER2).get(0))) {
					GUI_interface.setPlayerTwo(GUI_interface.getEventImage(Window.CHARACTER2).get(0));
					GUI_interface.showSelected(Window.CHARACTER2,
							GUI_interface.getEventImage(Window.CHARACTER2).get(0));
					GUI_interface.soundSelected(Window.CHARACTER2,
							GUI_interface.getEventImage(Window.CHARACTER2).get(0));
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.CHARACTER2).get(1))) {
					GUI_interface.setPlayerTwo(GUI_interface.getEventImage(Window.CHARACTER2).get(1));
					GUI_interface.showSelected(Window.CHARACTER2,
							GUI_interface.getEventImage(Window.CHARACTER2).get(1));
					GUI_interface.soundSelected(Window.CHARACTER2,
							GUI_interface.getEventImage(Window.CHARACTER2).get(1));
				}
				if (event.getTarget().equals(GUI_interface.getEventImage(Window.CHARACTER2).get(2))) {
					GUI_interface.showSelected(Window.CHARACTER2,
							GUI_interface.getEventImage(Window.CHARACTER2).get(2));
					if (!GUI_interface.isCharChosen2()) {
						GUI_interface.playUnselectedSound();
					}
					if(GUI_interface.isCharChosen2()) {
						GUI_interface.soundSelected(Window.CHARACTER2,
								GUI_interface.getEventImage(Window.CHARACTER2).get(2));
						GUI_interface.drawScreen(Window.HUMAN_AI2);
					} 
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.CHARACTER2).get(3))) {
					GUI_interface.soundSelected(Window.CHARACTER2,
							GUI_interface.getEventImage(Window.CHARACTER2).get(3));
					GUI_interface.drawScreen(Window.CHARACTER1);
				} else {
					GUI_interface.drawScreen(Window.CHARACTER2);
				}
				break;
			case HUMAN_AI2:
				if (event.getTarget().equals(GUI_interface.getEventImage(Window.HUMAN_AI2).get(0))) {
					GUI_interface.showSelected(Window.HUMAN_AI2, GUI_interface.getEventImage(Window.HUMAN_AI2).get(0));
					GUI_interface.soundSelected(Window.HUMAN_AI2, GUI_interface.getEventImage(Window.HUMAN_AI2).get(0));
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.HUMAN_AI2).get(1))) {
					GUI_interface.showSelected(Window.HUMAN_AI2, GUI_interface.getEventImage(Window.HUMAN_AI2).get(1));
					GUI_interface.soundSelected(Window.HUMAN_AI2, GUI_interface.getEventImage(Window.HUMAN_AI2).get(1));
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.HUMAN_AI2).get(2))) {
					GUI_interface.showSelected(Window.HUMAN_AI2, GUI_interface.getEventImage(Window.HUMAN_AI2).get(2));
					if (!GUI_interface.isTwoAIs()) {
						GUI_interface.soundSelected(Window.HUMAN_AI2, GUI_interface.getEventImage(Window.HUMAN_AI2).get(2));
					}
					if (GUI_interface.isTwoAIs()) {
						GUI_interface.drawScreen(Window.ERROR);
						GUI_interface.resetIsTwoAIs();
						GUI_interface.resetChosen();
					} else {
						if (!GUI_interface.isTwoAIs() && GUI_interface.isAIin()) {
							if (GUI_interface.whichAI().equals(Team.LEFT)) {
								theGame.setPlayer(Team.RIGHT,
										new HumanPlayer(Team.RIGHT, getSelectedCharacter(Team.RIGHT)));
								theGame.getCharacter(Team.RIGHT).getAllAnimations()
								.setAnimation(GUI_interface.getPlayerImage(Team.RIGHT), 0);
								GUI_interface.getChosenIcon(Team.RIGHT);
								theGame.setPlayer(Team.LEFT, new AIPlayer(Team.LEFT, getSelectedCharacter(Team.LEFT),
										theGame.getPlayer(Team.RIGHT)));
								theGame.getCharacter(Team.LEFT).getAllAnimations()
								.setAnimation(GUI_interface.getPlayerImage(Team.LEFT), 0);
								GUI_interface.getChosenIcon(Team.LEFT);
								GUI_interface.drawScreen(Window.CONTROLS);
							} else if (GUI_interface.whichAI().equals(Team.RIGHT)) {
								theGame.setPlayer(Team.LEFT,
										new HumanPlayer(Team.LEFT, getSelectedCharacter(Team.LEFT)));
								theGame.getCharacter(Team.LEFT).getAllAnimations()
								.setAnimation(GUI_interface.getPlayerImage(Team.LEFT), 0);
								GUI_interface.getChosenIcon(Team.LEFT);
								theGame.setPlayer(Team.RIGHT, new AIPlayer(Team.RIGHT, getSelectedCharacter(Team.RIGHT),
										theGame.getPlayer(Team.LEFT)));
								theGame.getCharacter(Team.RIGHT).getAllAnimations()
								.setAnimation(GUI_interface.getPlayerImage(Team.RIGHT), 0);
								GUI_interface.getChosenIcon(Team.RIGHT);
								GUI_interface.drawScreen(Window.CONTROLS);
							
							}
						} else {
							theGame.setPlayer(Team.LEFT,
									new HumanPlayer(Team.LEFT, getPCFromIcon(GUI_interface.getChosenIcon(Team.LEFT))));
							theGame.getCharacter(Team.LEFT).getAllAnimations()
							.setAnimation(GUI_interface.getPlayerImage(Team.LEFT), 0);
							theGame.setPlayer(Team.RIGHT, new HumanPlayer(Team.RIGHT,
									getPCFromIcon(GUI_interface.getChosenIcon(Team.RIGHT))));
							theGame.getCharacter(Team.RIGHT).getAllAnimations()
							.setAnimation(GUI_interface.getPlayerImage(Team.RIGHT), 0);
							GUI_interface.drawScreen(Window.CONTROLS);

						}
					}
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.HUMAN_AI2).get(3))) {
					GUI_interface.showSelected(Window.HUMAN_AI2, GUI_interface.getEventImage(Window.HUMAN_AI2).get(3));
					GUI_interface.soundSelected(Window.HUMAN_AI2, GUI_interface.getEventImage(Window.HUMAN_AI2).get(3));
					GUI_interface.drawScreen(Window.CHARACTER2);
				} else {
					GUI_interface.drawScreen(Window.HUMAN_AI2);
				}
				break;
			case CONTROLS:
				if (event.getTarget().equals(GUI_interface.getEventImage(Window.CONTROLS).get(0))) {
					GUI_interface.soundSelected(Window.CONTROLS, GUI_interface.getEventImage(Window.CONTROLS).get(0));
					GUI_interface.drawScreen(Window.FIGHT);
				} else if (event.getTarget().equals(GUI_interface.getEventImage(Window.CONTROLS).get(1))) {
					GUI_interface.soundSelected(Window.CONTROLS, GUI_interface.getEventImage(Window.CONTROLS).get(1));
					GUI_interface.drawScreen(Window.CHARACTER2);
				} else {
					GUI_interface.drawScreen(Window.CONTROLS);
				}

				gameTimer.schedule(new guiTimerTask(), (long) GUI_interface.getRuntimeInterval(), 4500);
				break;
			case ERROR:
				GUI_interface.resetSelected(Window.CHARACTER1);
				GUI_interface.resetSelected(Window.CHARACTER2);
				GUI_interface.drawScreen(Window.CHARACTER1);
				break;

			}
		}

	}

	/**
	 * Returns the playable character that corresponds to the selected icon.
	 * 
	 * @param icon the icon of the player.
	 * @return the corresponding playable character.
	 */
	private PlayableCharacter getPCFromIcon(Image icon) {
		for (PlayableCharacter pc : ALL_PLAYABLE_CHARACTERS) {
			if (pc.getIcon().equals(icon)) {
				return pc;
			}
		}
		return null;
	}

	/**
	 * Directly equivalent to calling getPCFromIcon with the argument of
	 * GUI_interface.getChosenIcon at the given team.
	 * 
	 * @param t The team being selected.
	 * @return the playable character of the team.
	 */
	private PlayableCharacter getSelectedCharacter(Team t) {
		return this.getPCFromIcon(GUI_interface.getChosenIcon(t));
	}

	
	private void initController() {
		if (!GameInterface.IS_ASCII) {
			this.GUI_interface.getRoot().setOnMouseClicked(new MouseClick());
		}
	}

	/**
	 * Returns the image of the icons for all PlayableCharacters.
	 * 
	 * @param name  name of PlayableCharacter
	 * @return  icon for PlayableCharacter
	 */
	static final Image getPlayerIcon(String name) {
		for (PlayableCharacter pc : ModelsAndViewsController.ALL_PLAYABLE_CHARACTERS) {
			if (pc.getName().equals(name)) {
				return pc.getIcon();
			}
		}
		return null;
	}

}
