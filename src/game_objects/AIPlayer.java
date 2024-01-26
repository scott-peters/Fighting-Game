package game_objects;

import the_game.Game.Team;

import java.util.Random;

import the_game.Game;
import the_game.PlayableCharacter;

/**
 * This class creates an AI player to allow the user to play against the
 * computer.
 * 
 * @author scottpeters
 */
public final class AIPlayer extends Player {

	private Player enemyPlayer;

	private final Random r = new Random();

	/**
	 * This is the copy constructor for the AI player.
	 * 
	 * @param toCopy copy all variables
	 */
	public AIPlayer(AIPlayer toCopy) {
		super(toCopy);
		this.enemyPlayer = toCopy.enemyPlayer;
	}

	/**
	 * This is the constructor for the AI player. It also sets the enemy human
	 * player of the AI player.
	 * 
	 * @param team              the team the AI player is on
	 * @param playableCharacter the playble character the AI player is
	 * @param enemy             the opposing human player
	 */
	public AIPlayer(Team team, PlayableCharacter playableCharacter, Player enemy) {
		super(team, playableCharacter);
		setEnemy(enemy);
	}

	/**
	 * This sets the enemy of the AI player.
	 * 
	 * @param p the human player that is the enemy of the AI player
	 */
	private void setEnemy(Player p) {
		this.enemyPlayer = p;
	}

	/**
	 * Establishes the different actions an enemy AI can do based on proximity to
	 * the player and randomness
	 */

	@Override
	/**
	 * This sets up the AI's moves based on what it's opponent is doing.
	 */
	protected void controlPlayer() {

		if ((Math.abs(enemyPlayer.getPosition().x - super.getPosition().x)) >= 150) {
			if ((enemyPlayer.getPosition().x - super.getPosition().x) >= 0) {
				super.setVelX(super.getMaxSpeed());
			} else if ((enemyPlayer.getPosition().x - super.getPosition().x) < 0) {
				super.setVelX(-super.getMaxSpeed());
			}
		} else if (Math.abs(enemyPlayer.getPosition().x - super.getPosition().x) < 150) {
			super.setAttacking(true);
			if (enemyPlayer.isAttacking()) {
				switch (r.nextInt(3)) {
				case 0:
					setBlocking(true);
					setJumping(false);
					setAttacking(false);
					super.setVelX(0);
					break;
				case 1:
					if (super.getPosition().y == Game.GROUND_LEVEL - super.getHeight()) {
						setJumping(true);
						setBlocking(false);
						setAttacking(false);
						super.setVelX(0);
					}
					break;
				case 2:
					setAttacking(true);
					setJumping(false);
					setBlocking(false);
					super.setVelX(0);
					break;
				}
			}
		}
	}

	@Override
	/**
	 * This makes a copy of the AI player.
	 */
	public AIPlayer copy() {
		return new AIPlayer(this);
	}

}


