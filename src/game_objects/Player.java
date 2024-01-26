package game_objects;

import java.awt.geom.Point2D;
import java.io.File;

import game_interface.GUI;
import game_interface.GameInterface;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import the_game.AnimationGroup;
import the_game.Game;
import the_game.PlayableCharacter;
import the_game.Game.Team;

/**
 * Setting up a player class that will establish each players
 * movements/attacks/health and other information related to the given player.
 * 
 * @author scottpeters
 *
 */
public abstract class Player implements Copyable<Player> {

	private boolean jumping;
	private boolean attacking;
	private boolean blocking;
	private Point2D.Double pos, velocity, acceleration;
	private Team team;
	private double health;
	private boolean hasChanged;
	private final PlayableCharacter pc;

	private long nextAttack, lastAttack;
	private MediaPlayer attackSound;
	private MediaPlayer bounceSound;
	private MediaPlayer blockSound;

	/**
	 * Setting the starting configuration of each character based on the given
	 * playable character and team.
	 * 
	 * @param team what team the player is on
	 * @param pc   establish that this is a playable character
	 */
	public Player(Team team, PlayableCharacter pc) {
		this.team = team;
		switch (this.team) {
		case LEFT:
			this.pos = new Point2D.Double(300, Game.GROUND_LEVEL - pc.getHeight());
			break;
		case RIGHT:
			this.pos = new Point2D.Double(800, Game.GROUND_LEVEL - pc.getHeight());
			break;
		}

		this.nextAttack = this.lastAttack = 0;
		this.velocity = new Point2D.Double(0, 0);
		this.acceleration = new Point2D.Double(0, 0);

		this.pc = pc;
		this.health = this.pc == null ? 0 : pc.getHealth();
	}

	/**
	 * Setting up all copy constructors for the Player class
	 * 
	 * @param toCopy copy all variables
	 */
	public Player(Player toCopy) {
		this.hasChanged = this.attacking = this.blocking = this.jumping = false;
		this.team = toCopy.team;
		this.pos = (Point2D.Double) toCopy.pos.clone();
		this.velocity = (Point2D.Double) toCopy.velocity.clone();
		this.acceleration = (Point2D.Double) toCopy.acceleration.clone();
		this.health = toCopy.health;
		this.pc = toCopy.pc;
		this.lastAttack = toCopy.lastAttack;
	}

	/**
	 * Gives the character of the player.
	 * 
	 * @return the character of the player.
	 */
	public PlayableCharacter getCharacter() {
		return this.pc;
	}

	/**
	 * Gives max health of the players for a starting point.
	 * 
	 * @return max health of the player
	 */
	public double getMaxHealth() {
		return this.pc.getHealth();
	}

	/**
	 * Gets the player health at any moment.
	 * 
	 * @return health of the player at a given time
	 */
	public double getHealth() {
		return this.health;
	}

	/**
	 * Hurt the player based on the attack and state of the player.
	 * 
	 * @param a  an Attack
	 * @return damage based on whether someone's blocking or not
	 */
	public double hurt(Attack a) {

		long time = System.currentTimeMillis();
		if (time > lastAttack + a.getDuration()) {
			lastAttack = time;
			// Make sure the player takes the reduced damage if blocking.
			if (blocking == true) {
				if (!GameInterface.IS_ASCII) {
					moveSound("block");
				}
				return health -= a.getBlockDamage();
			} else if (blocking == false) {
				return health -= a.getDamage();
			} else {
				return health;
			}
		} else {
			return 0;
		}
	}

	/**
	 * Gives us the attack the player is doing.
	 * 
	 * @return the attack
	 */
	public Attack getAttack() {
		if (!jumping) {
			return this.pc.getAttacks().getPunch();
		} else {
			return this.pc.getAttacks().getJumpKick();
		}
	}

	/**
	 * Gives us the position of each player at a given time.
	 * 
	 * @return the position of the player
	 */
	public Point2D.Double getPosition() {
		return (Point2D.Double) this.pos.clone();
	}

	/**
	 * Gives us the position of each player at a given time.
	 * 
	 * @return the position of the player
	 */
	public Point2D.Double getPositionReference() {
		return this.pos;
	}

	/**
	 * Gives us the velocity of each player at a given time.
	 * 
	 * @return the velocity of the player
	 */
	public Point2D.Double getVelocity() {
		return (Point2D.Double) this.velocity.clone();
	}

	/**
	 * Places the Player at the given x-location.
	 * 
	 * @param x the x-coordinate of the player.
	 */
	public void setX(double x) {
		this.pos.x = x;
	}

	/**
	 * Places the Player at the given y-location.
	 * 
	 * @param y the y-coordinate of the player.
	 */
	public void setY(double y) {
		this.pos.y = y;
	}

	/**
	 * Lets us know if a player is attacking.
	 * 
	 * @return if player is attacking
	 */
	public boolean isAttacking() {
		return attacking;
	}

	/**
	 * Lets us know if a player is blocking.
	 * 
	 * @return if player is blocking
	 */
	public boolean isBlocking() {
		return blocking;
	}

	/**
	 * Lets us know if a player is jumping.
	 * 
	 * @return if a player is jumping
	 */
	public boolean isJumping() {
		return jumping;
	}

	/**
	 * The state of the character's absence of life.
	 * 
	 * @return if a player is dead.
	 */
	public boolean isDead() {
		return this.health <= 0;
	}

	/**
	 * Establishes a velocity for the player in order to move them left or right.
	 * 
	 * @param velX the velocity of the player left or right
	 */
	public void setVelX(double velX) {
		if (this.velocity.x != velX) {
			this.velocity.x = velX;
			hasChanged = true;
		}
	}

	/**
	 * Establishes a velocity for the player in order to move them up or down.
	 * 
	 * @param velY the velocity of the player up or down
	 */
	public void setVelY(double velY) {
		if (this.velocity.y != velY) {
			this.velocity.y = velY;
			hasChanged = true;
		}
	}

	/**
	 * Establishes an acceleration for the player in the y direction.
	 * 
	 * @param accelY the y-acceleration
	 */
	public void setAccelY(double accelY) {
		if (this.acceleration.y != accelY) {
			this.acceleration.y = accelY;
			hasChanged = true;
		}
	}

	/**
	 * Get max speed that the player can run
	 * 
	 * @return speed from the playable character class
	 */
	public double getMaxSpeed() {
		return this.pc.getSpeed();
	}

	/**
	 * Get horizontal size of the player to help establish a hitbox.
	 * 
	 * @return sizeX horizontal size of the character.
	 */
	public double getHeight() {
		return this.pc.getHeight();
	}

	/**
	 * Get vertical size of the player to help establish a hitbox.
	 * 
	 * @return sizeY vertical size of the character.
	 */
	public double getWidth() {
		return this.pc.getWidth();
	}

	/**
	 * Get full size of the player.
	 * 
	 * @return size of the player.
	 */
	public Point2D.Double getSize() {
		return (Point2D.Double) this.pc.getSize().clone();
	}

	/**
	 * Get an attack that the player makes.
	 * 
	 * @return defaultAttack
	 */
	public Attack getDefaultAttack() {
		return this.getDefaultAttack();
	}

	/**
	 * Gets the team the player is on in order to know who player 1 and 2 are.
	 * 
	 * @return which team the player is on
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Sets the hasChanged variable to true if the player is attacking in order to
	 * control animations
	 * 
	 * @param attacking if the player is attacking
	 */
	protected void setAttacking(boolean attacking) {
		if (!this.blocking) {
			if (attacking && this.attacking != attacking) {
				this.nextAttack = System.currentTimeMillis() + this.getAttack().getDuration();
				if (!GameInterface.IS_ASCII) {
					moveSound("attack");
				}
				hasChanged = true;
			}
			this.attacking = attacking;
		}
	}

	/**
	 * Sets the hasChanged variable to true if the player is blocking in order to
	 * control animations
	 * 
	 * @param blocking if the player is blocking
	 */
	protected void setBlocking(boolean blocking) {
		if (this.blocking != blocking && !this.attacking) {
			this.blocking = blocking;
			hasChanged = true;
		}
	}

	/**
	 * Sets the hasChanged variable to true if the player is jumping in order to
	 * control animations. Also sets up some restrictions for jumping.
	 * 
	 * @param jumping if the player is jumping
	 */
	protected void setJumping(boolean jumping) {
		this.jumping = jumping;
		if (this.jumping) {
			hasChanged = true;
			this.setVelY(-this.pc.getJumpSpeed());
			if (!GameInterface.IS_ASCII) {
				moveSound("jump");
			}
			this.setAccelY(Game.GRAVITY);
		}
	}

	/**
	 * This sets the sounds for different moves the player makes. ie attacking,
	 * blocking and jumping
	 * 
	 * @param move the move the player is making that requires sound
	 */
	private void moveSound(String move) {

		// https://freesound.org/people/Robinhood76/sounds/341492/ -- bubble sound

		// https://www.zapsplat.com/page/4/?s=cartoon+fight&post_type=music&sound-effect-category-id
		String ya = "src/sounds/ya.mp3";
		Media attacking = new Media(new File(ya).toURI().toString());
		attackSound = new MediaPlayer(attacking);

		// https://www.zapsplat.com/page/4/?s=cartoon+fight&post_type=music&sound-effect-category-id
		String huh = "src/sounds/huh.mp3";
		Media blocking = new Media(new File(huh).toURI().toString());
		blockSound = new MediaPlayer(blocking);

		// https://freesound.org/people/Lefty_Studios/sounds/369515/
		String bounce = "src/sounds/jumping-sfx.wav";
		Media bounceLoop = new Media(new File(bounce).toURI().toString());
		bounceSound = new MediaPlayer(bounceLoop);

		if (GUI.MUTE) {
			attackSound.setMute(true);
			blockSound.setMute(true);
			bounceSound.setMute(true);
		} else {
			attackSound.setVolume(0.1);
			blockSound.setVolume(0.1);
			bounceSound.setVolume(0.1);
		}

		if (move.equals("attack")) {
			attackSound.play();
		}
		if (move.equals("block")) {
			blockSound.play();
		}
		if (move.equals("jump")) {
			bounceSound.play();
		}
	}

	/**
	 * Changes the position of the player based on the velocity of the player and
	 * how much time has gone by.
	 * 
	 * @param time elapsed
	 */
	public void updatePlayer(double time) {
		if (this.attacking && System.currentTimeMillis() >= this.nextAttack) {
			this.attacking = false;
			this.hasChanged = true;
		}
		this.controlPlayer(); // Set states based off player/AI input.
		this.velocity.x += this.acceleration.x * time;
		this.velocity.y += this.acceleration.y * time;
		this.pos.x += time * this.velocity.x;
		this.pos.y += time * this.velocity.y;
	}

	/**
	 * Decides what animation change should occur and returns the int representation
	 * of said change.
	 * 
	 * @return the int of the animation change. -1 if no change should occur.
	 */
	public int getAnimationIndex() {
		// Death does not care for you're frivolous events.
		if (this.isDead()) {
			return AnimationGroup.getDeathIndex();
		}
		if (this.hasChanged) {
			this.hasChanged = false;
			// if-statements in order of priority
			if (this.jumping) {
				return AnimationGroup.getJumpingIndex();
			}
			if (this.blocking) {
				return AnimationGroup.getBlockingIndex();
			}
			if (this.attacking) {
				return AnimationGroup.getPunchingIndex();
			}
			if (this.velocity.x == 0 && this.velocity.y == 0) {
				return AnimationGroup.getStanceIndex();
			} else {
				return AnimationGroup.getWalkingIndex();
			}
		}
		return -1;
	}

	@Override
	/**
	 * This checks for equality between an object and a Player.
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (this == obj) {
			return true;
		} else if (!(obj instanceof Player)) {
			return false;
		} else {
			Player p = (Player) obj;
			boolean pcEqual = this.pc == null ? p.pc == null : this.pc.equals(p.pc);
			boolean posEqual = this.pos == null ? p.pos == null : this.pos.equals(p.pos);
			boolean velEqual = this.velocity == null ? p.velocity == null : this.velocity.equals(p.velocity);
			return this.health == p.health && pcEqual && posEqual && velEqual && this.team == p.team;
		}
	}

	/**
	 * Forces the implementation of this class to manipulate the state of the player
	 * using the provided methods.
	 */
	protected abstract void controlPlayer();

}