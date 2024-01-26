package the_game;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import game_objects.AttackGroup;
import javafx.scene.image.Image;

/**
 * A collection of information that represents a unique character that a player
 * object is expected to use to use but not alter.
 * 
 * @author scottpeters
 * @author Lucas
 */
public final class PlayableCharacter {

	// Various attributes associated to a character.
	private String characterName;
	private double maxHealth, speed, jumpSpeed;
	private Point2D.Double size;
	private Image icon;
	private final AttackGroup allAttacks;
	private final AnimationGroup allAnimations;

	/**
	 * Setting up the playable character by reading all wanted values from a file.
	 * 
	 * @param folderPath name of the file we want to read
	 * @throws IOException
	 */
	public PlayableCharacter(String folderPath) throws IOException {
		String text = "";
		int lineNumber;
		int x = -1, y = -1;

		FileReader readfile = new FileReader(folderPath + "/CharacterAttributes.txt/");
		BufferedReader br = new BufferedReader(readfile);

		for (lineNumber = 1; lineNumber < 15; lineNumber++) {
			if (lineNumber == 1) {
				text = br.readLine().substring(16);
				this.characterName = text;
			} else if (lineNumber == 3) {
				text = br.readLine().substring(12);
				maxHealth = Double.valueOf(text);
			} else if (lineNumber == 5) {
				text = br.readLine().substring(15);
				speed = Double.valueOf(text);
			} else if (lineNumber == 7) {
				text = br.readLine().substring(12);
				jumpSpeed = Double.valueOf(text);
			} else if (lineNumber == 9) {
				text = br.readLine().substring(7);
				x = Integer.parseInt(text);
			} else if (lineNumber == 11) {
				text = br.readLine().substring(8);
				y = Integer.parseInt(text);
			} else {
				br.readLine();
			}
			this.size = new Point2D.Double(x, y);

		}
		br.close();

		try {
			this.icon = (new SmartImageLoader(folderPath + "/Icon.png")).getImage();
		} catch (RuntimeException re) {
			re.printStackTrace(); // Should only occur when trying to create an Image object during JUnit runtime.
		}
		this.allAttacks = new AttackGroup(folderPath + "/attacks");
		this.allAnimations = new AnimationGroup(folderPath + "/animations", this.size);
	}

	/**
	 * This gets the name of the character.
	 * 
	 * @return the name of the character.
	 */
	public String getName() {
		return this.characterName;
	}

	/**
	 * Health of a player at full.
	 * 
	 * @return health of players
	 */
	public double getHealth() {
		return this.maxHealth;
	}

	/**
	 * Maximum speed of the players movement.
	 * 
	 * @return speed of players
	 */
	public double getSpeed() {
		return this.speed;
	}

	/**
	 * Initial speed the player leaves the ground at.
	 * 
	 * @return speed of players
	 */
	public double getJumpSpeed() {
		return this.jumpSpeed;
	}

	/**
	 * Get all attacks that the player makes.
	 * 
	 * @return defaultAttack
	 */
	public AttackGroup getAttacks() {
		return new AttackGroup(this.allAttacks);
	}

	/**
	 * Get horizontal size of the player to help establish a hitbox.
	 * 
	 * @return sizeX horizontal size of the character.
	 */
	public double getHeight() {// might not need these anymore?
		return this.size.getY();
	}

	/**
	 * Get vertical size of the player to help establish a hitbox.
	 * 
	 * @return sizeY vertical size of the character.
	 */
	public double getWidth() {
		return this.size.getX();
	}

	/**
	 * Get full size of the player (hitbox).
	 * 
	 * @return size of the player.
	 */
	public Point2D.Double getSize() {
		return (Point2D.Double) this.size.clone();
	}

	/**
	 * Get all animations for the Playable Character.
	 * 
	 * @return ImageFile of all animations for the Playable Character
	 */
	public AnimationGroup getAllAnimations() {
		return new AnimationGroup(this.allAnimations);
	}

	/**
	 * Get's the Image for the icon used in character selection.
	 * 
	 * @return the Image of character.
	 */
	public Image getIcon() {
		return this.icon;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (this == obj) {
			return true;
		} else if (!(obj instanceof PlayableCharacter)) {
			return false;
		} else {
			PlayableCharacter pc = (PlayableCharacter) obj;
			return this.allAnimations.equals(pc.allAnimations) && this.allAttacks.equals(pc.allAttacks)
					&& this.characterName.equals(pc.characterName) && this.icon.equals(pc.icon)
					&& this.maxHealth == pc.maxHealth && this.size.equals(pc.size) && this.speed == pc.speed;
		}
	}
}
