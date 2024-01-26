package game_objects;

import game_interface.ModelsAndViewsController;
import javafx.scene.input.KeyCode;
import the_game.PlayableCharacter;
import the_game.Game;
import the_game.Game.Team;

/**
 * This class creates all the keybinds for the players and matches them to
 * specific actions.
 * 
 * @author scottpeters
 */
public final class HumanPlayer extends Player {
	private final KeyCode moveLeftKey, moveRightKey, blockKey, attackKey, jumpKey;

	/**
	 * Sets up both player keybinds in order to control the player.
	 * 
	 * @param team what team the player is on
	 * @param pc   establish that this is a playable character
	 */
	public HumanPlayer(Team team, PlayableCharacter pc) {
		super(team, pc);
		// Set up player key binds
		switch (team) { // if-else equivalent for each possible value of Team.
		case LEFT: // All left team key binds
			this.moveLeftKey = KeyCode.A;
			this.moveRightKey = KeyCode.D;
			this.attackKey = KeyCode.Z;
			this.blockKey = KeyCode.S;
			this.jumpKey = KeyCode.W;
			break;
		case RIGHT: // All right team key binds
			this.moveLeftKey = KeyCode.J;
			this.moveRightKey = KeyCode.L;
			this.attackKey = KeyCode.M;
			this.blockKey = KeyCode.K;
			this.jumpKey = KeyCode.I;
			break;
		default: // All keys should be set to null in the event Team is null
			this.moveLeftKey = null;
			this.moveRightKey = null;
			this.blockKey = null;
			this.attackKey = null;
			this.jumpKey = null;
		}
	}

	/**
	 * Matches the keybinds to specific actions the player can do.
	 */
	@Override
	public void controlPlayer() {
		if (ModelsAndViewsController.keyboard.isKeyPressed(this.moveLeftKey)) {
			super.setVelX(-super.getMaxSpeed());
		}

		if (ModelsAndViewsController.keyboard.isKeyPressed(this.moveRightKey)) { // check if player is moving right
			super.setVelX(super.getMaxSpeed());
		}

		if (ModelsAndViewsController.keyboard.isKeyPressed(this.jumpKey)
				&& super.getPosition().y == Game.GROUND_LEVEL - super.getHeight()) {
			super.setJumping(true);
		}

		if (ModelsAndViewsController.keyboard.isKeyPressed(this.attackKey)) { // check if player is attacking
			super.setAttacking(true); // set attack to true
		}

		if (ModelsAndViewsController.keyboard.isKeyPressed(this.blockKey)) { // check if player is blocking
			super.setBlocking(true); // set block to true
		}

		if (!ModelsAndViewsController.keyboard.isKeyPressed(this.moveLeftKey)
				&& !ModelsAndViewsController.keyboard.isKeyPressed(this.moveRightKey)) {
			super.setVelX(0);
		}

		if (ModelsAndViewsController.keyboard.isKeyPressed(this.moveLeftKey)
				&& ModelsAndViewsController.keyboard.isKeyPressed(this.moveRightKey)) {
			super.setVelX(0);
		}

		if (!ModelsAndViewsController.keyboard.isKeyPressed(this.jumpKey)) {
			super.setJumping(false);
		}

		if (!ModelsAndViewsController.keyboard.isKeyPressed(this.blockKey)) {
			super.setBlocking(false);
		}
	}

	/**
	 * Creates copy constructors for the human player class
	 * 
	 * @param toCopy
	 */
	public HumanPlayer(HumanPlayer toCopy) {
		super(toCopy);
		moveLeftKey = toCopy.moveLeftKey;
		moveRightKey = toCopy.moveRightKey;
		attackKey = toCopy.attackKey;
		blockKey = toCopy.blockKey;
		jumpKey = toCopy.jumpKey;
	}

	@Override
	/**
	 * This checks for equality of the human player.
	 */
	public boolean equals(Object obj) {
		boolean se = super.equals(obj);
		if (!se) { // If the super of the object is not equals, the child cannot be equal.
			return false;
		} else if (!(obj instanceof HumanPlayer)) {
			return false;
		} else {
			HumanPlayer hp = (HumanPlayer) obj;
			return this.attackKey == hp.attackKey; // Only need to check one key as all the key's are grouped.
		}
	}

	/**
	 * Equivalent to using a copy constructor.
	 */
	@Override
	public HumanPlayer copy() {
		return new HumanPlayer(this);
	}

}