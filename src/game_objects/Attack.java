package game_objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class sets up everything to do with attacking in the game.
 * 
 * @author JessicaChurcher
 */
public class Attack implements Copyable<Attack> {

	/**
	 * Contains all recognized forms of attack by each player, creates an easily
	 * expandable framework.
	 */
	public static enum AttackType {
		PUNCH, JUMPKICK;
	}

	// Attack-related information variables.
	private double damage, range, blockDamage;
	private long duration;
	private AttackType attackType;

	/**
	 * This constructs an Attack with a file name as the parameter. Sets damage
	 * range duration and blockDamage for use in code by reading off a file that is
	 * separately placed in the project.
	 * 
	 * @param FileName the specific file name we want to read the values from
	 */
	public Attack(String FileName) {

		String text = "";
		int lineNumber;

		try {
			FileReader readfile = new FileReader(FileName);
			BufferedReader br = new BufferedReader(readfile);

			for (lineNumber = 1; lineNumber < 10; lineNumber += 2) {
				text = br.readLine().split(": ")[1];
				switch (lineNumber) {
				case 1:
					this.attackType = AttackType.valueOf(text);
					break;
				case 3:
					this.damage = Double.valueOf(text);
					break;
				case 5:
					this.range = Double.valueOf(text);
					break;
				case 7:
					this.duration = Long.valueOf(text);
					break;
				case 9:
					this.blockDamage = Double.valueOf(text);
					break;
				}
				br.readLine(); // Skip blank line.
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This is a getter for the type of attack
	 * 
	 * @return the type of attack, of type AttackType
	 */
	public AttackType getAttackType() {
		return this.attackType;
	}

	/**
	 * This constructs an Attack with an Attack as a parameter. Sets damage range
	 * duration and blockDamage for use in code
	 * 
	 * @param a Attack used to construct attack
	 */
	public Attack(Attack a) {
		this.damage = a.damage;
		this.range = a.range;
		this.duration = a.duration;
		this.blockDamage = a.blockDamage;
	}

	/**
	 * Get the damage done per attack.
	 * 
	 * @return damage amount of damage done per attack
	 */
	public double getDamage() {
		return this.damage;

	}

	/**
	 * Get the range in which a player attack can hit.
	 * 
	 * @return range  the range of the attack
	 */
	public double getRange() {
		return this.range *= 10;
	}

	/**
	 * Give the time it takes for an attack to finish before you can attack again.
	 * 
	 * @return duration the amount of time an attack takes to complete
	 */
	public long getDuration() {
		return this.duration;
	}

	/**
	 * Gives the amount of damage done while blocking as opposed to while not
	 * blocking.
	 * 
	 * @return blockDamage the amount of damage done while blocking
	 */
	public double getBlockDamage() {
		return this.blockDamage;

	}

	/**
	 * Create a copy constructor for Attack
	 */
	@Override
	public Attack copy() {
		return new Attack(this);
	}

	@Override
	/**
	 * Used to check equality of an object and Attack.
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (this == obj) {
			return true;
		} else if (!(obj instanceof Attack)) {
			return false;
		} else {
			Attack a = (Attack) obj;
			return this.attackType == a.attackType && this.blockDamage == a.blockDamage && this.damage == a.damage
					&& this.duration == a.duration && this.range == a.range;
		}
	}

}
