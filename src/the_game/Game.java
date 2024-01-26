package the_game;

import java.awt.geom.Point2D;

import game_interface.GameInterface;
import game_objects.Attack;
import game_objects.Player;

/**
 * This class sets up the variables for the players on either team and acts as a
 * middle-man for getting and restricting player information. Updates positions
 * of each player based on the boundaries of the game and the state of the other
 * player.
 *
 */
public class Game {

	/**
	 * This is the initial output screen before the game starts to explain key
	 * controls to players
	 */
	public static final String GAME_INTRO = "This is a two player battle to the death! \n"
			+ "The left team uses the controls A and D to move, Z to attack and S to block. \n"
			+ "The right team uses the controls J and L to move, M to attack and K to block. \n"
			+ "Both teams will enter their moves at the same time then press enter. \n" + "Press Enter to begin!";

	/**
	 * This is the size of the displayed screen.
	 */
	public static final Point2D.Double SCREEN_SIZE = new Point2D.Double(1250, 700);
	/**
	 * This is the ground level of the players.
	 */
	public static final double GROUND_LEVEL = 680;
	/**
	 * This is the gravity that is put on the players.
	 */
	public static final double GRAVITY = 2000;

	/**
	 * 
	 * This sets up whether a player belongs to the right team or the left team.
	 * LEFT is player one, RIGHT is player two.
	 */
	public static enum Team {
		/**
		 * This is the left side player, also known as Player One
		 */
		LEFT, 
		/**
		 * This is the right side player, also known as Player Two
		 */
		RIGHT
	};

	private Player p1, p2;

	/**
	 * This constructs the game when player one and player two are specified.
	 * 
	 * @param p1 player one
	 * @param p2 player two
	 */
	public Game(Player p1, Player p2) {
		if (p1 != null)
			this.p1 = p1.copy();
		if (p2 != null)
			this.p2 = p2.copy();
		// this.setPlayer(Team.LEFT,p1);
		// this.setPlayer(Team.RIGHT,p2);
	}

	/**
	 * This sets the players to their respective teams.
	 * 
	 * @param t Team: LEFT or RIGHT, that the player will be on
	 * @param p player being set to a team
	 */
	public void setPlayer(Team t, Player p) {
		if (p == null) {
			return;
		}
		if (!t.equals(p.getTeam())) {
			System.err.println("Failed to set player: set team did not match player team.");
			System.err.println("Set: " + t.toString());
			System.err.println("Player: " + p.getTeam().toString());
			return;
		}
		switch (t) {
		case LEFT:
			this.p1 = p.copy();
			break;
		case RIGHT:
			this.p2 = p.copy();
			break;
		}
	}

	/**
	 * This gets the current health of a player.
	 * 
	 * @param t Team the player is on
	 * @return the current health of the player
	 */
	public double getHealth(Team t) {
		switch (t) { // For t
		case LEFT: // if t is LEFT
			return p1.getHealth();
		case RIGHT: // if t is RIGHT
			return p2.getHealth();
		default:
			return -1;
		}
	}

	/**
	 * Uses java's built in class Point to retrieve the position of a player
	 * 
	 * @param t Team the player is on
	 * @return the x and y value of the player
	 */
	public Point2D.Double getPosition(Team t) {
		switch (t) {
		case LEFT:
			return (Point2D.Double) p1.getPosition().clone();
		case RIGHT:
			return (Point2D.Double) p2.getPosition().clone();
		default:
			return null;
		}
	}

	/**
	 * Uses java's built in class Point to retrieve the velocity of a player
	 * 
	 * @param t Team the player is on
	 * @return the x and y velocity of the player
	 */
	public Point2D.Double getVelocity(Team t) {
		switch (t) {
		case LEFT:
			return (Point2D.Double) p1.getVelocity().clone();
		case RIGHT:
			return (Point2D.Double) p2.getVelocity().clone();
		default:
			return null;
		}
	}

	/**
	 * This gets the maximum health of a player.
	 * 
	 * @param t Team the player is on
	 * @return the maximum health of the player
	 */
	public double getMaxHealth(Team t) {
		switch (t) {
		case LEFT:
			return p1.getMaxHealth();
		case RIGHT:
			return p2.getMaxHealth();
		default:
			return 0;
		}
	}

	/**
	 * This gets the Attack that player is performing.
	 * 
	 * @param t Team the player is on
	 * @return Attack the player performed
	 */
	public Attack getAttack(Team t) {
		switch (t) {
		case LEFT:
			return this.p1.getAttack().copy();
		case RIGHT:
			return this.p2.getAttack().copy();
		default:
			return null;
		}
	}

	/**
	 * This gets the animation of the index corresponding to the player on either
	 * team.
	 * 
	 * @param t Team the player is on
	 * @return index of the animation change.
	 */
	public int getAnimationIndex(Team t) {
		switch (t) {
		case LEFT:
			return this.p1.getAnimationIndex();
		case RIGHT:
			return this.p2.getAnimationIndex();
		default:
			return -1;
		}
	}

	/**
	 * Returns the size of the player of the corresponding team as point where x is
	 * the width and y is the height.
	 * 
	 * @param t the team of the player.
	 * @return a point representation of the characters size.
	 */
	public Point2D.Double getSize(Team t) {
		switch (t) {
		case LEFT:
			return this.p1.getSize();
		case RIGHT:
			return this.p2.getSize();
		default:
			return null;
		}
	}

	/**
	 * Gives the character of the player on the corresponding team.
	 * 
	 * @param t Team the player is on
	 * @return character of the team.
	 */
	public PlayableCharacter getCharacter(Team t) {
		switch (t) {
		case LEFT:
			return this.p1.getCharacter();
		case RIGHT:
			return this.p2.getCharacter();
		default:
			return null;
		}
	}

	/**
	 * This checks to see if player is attacking.
	 * 
	 * @param t Team the player is on
	 * @return if the player is attacking returns true, otherwise returns false
	 */
	public boolean isAttacking(Team t) {
		switch (t) {
		case LEFT:
			return this.p1.isAttacking();
		case RIGHT:
			return this.p2.isAttacking();
		default:
			return false;
		}
	}

	/**
	 * This checks to see if a player is blocking.
	 * 
	 * @param t Team the player is on
	 * @return if the player is blocking returns true, otherwise returns false
	 */
	public boolean isBlocking(Team t) {
		switch (t) {
		case LEFT:
			return this.p1.isBlocking();
		case RIGHT:
			return this.p2.isBlocking();
		default:
			return false;
		}
	}

	/**
	 * This checks to see if a player is jumping.
	 * 
	 * @param t Team the player is on
	 * @return if the player is jumping returns true, otherwise returns false
	 */
	public boolean isJumping(Team t) {
		switch (t) {
		case LEFT:
			return this.p1.isJumping();
		case RIGHT:
			return this.p2.isJumping();
		default:
			return false;
		}
	}

	/**
	 * This checks is to see if a player has zero health left to end the game
	 * 
	 * @return if either player has zero health returns true, otherwise returns
	 *         false
	 */
	public boolean isGameOver() {
		return this.p1.isDead() || this.p2.isDead();
	}

	/**
	 * This returns the winner of the game
	 * 
	 * @return the winning team of the game, null if the game has not ended.
	 */
	public Team winner() {
		if (this.p1.isDead()) {
			return p2.getTeam();
		} else if (this.p2.isDead()) {
			return p1.getTeam();
		} else {
			return null;
		}
	}

	/**
	 * Reset the game state to be playable.
	 */
	public void reset() {
		// TODO: make game re-playable through restarting of the game.
	}

	/**
	 * This updates the game based off of time. Detects if a player is being
	 * attacked, and hurts players if they are being attacked. Also detects
	 * collisions between players and restricts movement.
	 * 
	 * @param time the amount of time that has passed in game time.
	 */
	public void updateGame(double time) {
		p1.updatePlayer(time);
		p2.updatePlayer(time);

		Point2D.Double player1Pos = p1.getPositionReference();
		Point2D.Double player2Pos = p2.getPositionReference();
		
		

		// Player boundary restrictions.
		if (player1Pos.y > Game.GROUND_LEVEL - this.p1.getHeight()) {
			player1Pos.y = Game.GROUND_LEVEL - this.p1.getHeight();
			this.p1.setVelY(0);
			this.p1.setAccelY(0);
		}
		if (player2Pos.y > Game.GROUND_LEVEL - this.p2.getHeight()) {
			player2Pos.y = Game.GROUND_LEVEL - this.p2.getHeight();
			this.p2.setVelY(0);
			this.p2.setAccelY(0);
		}

		if (GameInterface.IS_ASCII) {
			if (player1Pos.x < 20) {
				player1Pos.x = 20;
			} else if (player1Pos.x > Game.SCREEN_SIZE.x - (p1.getSize().x + 20)) {
				player1Pos.x = Game.SCREEN_SIZE.x - (p1.getSize().x + 20);
			}
		} else {
			if (player1Pos.x < -40) {
				player1Pos.x = -40;
			} else if (player1Pos.x > Game.SCREEN_SIZE.x - this.p1.getSize().x) {
				player1Pos.x = Game.SCREEN_SIZE.x - this.p1.getSize().x;
			}
		}

		if (GameInterface.IS_ASCII) {
			if (player2Pos.x < 20) {
				player2Pos.x = 20;
			} else if (player2Pos.x > Game.SCREEN_SIZE.x - (p2.getSize().x + 20)) {
				player2Pos.x = Game.SCREEN_SIZE.x - (p2.getSize().x + 20);
			}
		} else {
			if (player2Pos.x < -40) {
				player2Pos.x = -40;
			} else if (player2Pos.x > Game.SCREEN_SIZE.x - this.p2.getSize().x) {
				player2Pos.x = Game.SCREEN_SIZE.x - this.p2.getSize().x;
			}
		}

		// Player collision detection
		if (!(player1Pos.y + this.p1.getHeight() < player2Pos.y || player2Pos.y + this.p2.getHeight() < player1Pos.y)) {
			if (player1Pos.x + p1.getWidth() > player2Pos.x
					&& player1Pos.x + p1.getWidth() < player2Pos.x + p2.getWidth()) {
				double delta = player1Pos.x + p1.getWidth() - player2Pos.x;
				player1Pos.x -= delta / 2;
				player2Pos.x += delta / 2;
			}
			if (player2Pos.x + p2.getWidth() > player1Pos.x
					&& player2Pos.x + p2.getWidth() < player1Pos.x + p1.getWidth()) {
				double delta = player2Pos.x + p2.getWidth() - player1Pos.x;
				player2Pos.x -= delta / 2;
				player1Pos.x += delta / 2;
			}

			if (p1.isAttacking() && player1Pos.x + p1.getWidth() + p1.getAttack().getRange() >= player2Pos.x
					&& player1Pos.x - p1.getAttack().getRange() <= player2Pos.x + p2.getWidth()) {
				p2.hurt(p1.getAttack());
			}

			if (p2.isAttacking() && player2Pos.x - p2.getAttack().getRange() <= player1Pos.x + p1.getWidth()
					&& player2Pos.x + p2.getWidth() + p2.getAttack().getRange() >= player1Pos.x) {
				p1.hurt(p2.getAttack());
			}
			
			if(p1.getHealth() < 0.1 || p2.getHealth() < 0.1) {
				player1Pos.y = GROUND_LEVEL - p1.getHeight();
				player2Pos.y = GROUND_LEVEL - p2.getHeight();
			}
		}
	}

	/**
	 * Returns a copy of the player on the given team.
	 * 
	 * @param t the team of the player being returned.
	 * @return a copy of the respective player.
	 */
	public Player getPlayer(Team t) {
		switch (t) {
		case LEFT:
			return this.p1;
		case RIGHT:
			return this.p2;
		default:
			return null;
		}
	}
}
