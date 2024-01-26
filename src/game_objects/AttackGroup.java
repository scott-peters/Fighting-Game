package game_objects;

import java.util.Arrays;

import game_objects.Attack.AttackType;
import the_game.ObjectLoader;

/**
 * A collection of one of every attack-type used for storing the attack set of a
 * playable character and to coordinate attacks between different players.
 *
 */
public final class AttackGroup {
	private final Attack[] allAttacks;

	/**
	 * Loads every possible attack object within the folder given by the folderPath
	 * and sorts them into the allAttacks array using the AttackType as the basis
	 * for sorting.
	 * 
	 * @param folderPath the folder containing multiple attack-files
	 */
	public AttackGroup(String folderPath) {
		Attack[] attackType = (new ObjectLoader<Attack>(folderPath, Attack.class)).getAllObjects();
		this.allAttacks = new Attack[AttackType.values().length];
		for (int t = 0; t < AttackType.values().length; t++) {
			for (int a = 0; a < attackType.length; a++) {
				if (attackType[a].getAttackType() == AttackType.values()[t]) {
					this.allAttacks[t] = attackType[a];
				}
			}
		}
	}

	/**
	 * Copy's every Attack into a new instance.
	 * 
	 * @param toCopy the AttackGroup to be copied.
	 */
	public AttackGroup(AttackGroup toCopy) {
		this.allAttacks = new Attack[toCopy.allAttacks.length];
		for (int i = 0; i < this.allAttacks.length; i++) {
			this.allAttacks[i] = toCopy.allAttacks[i].copy();
		}
	}

	/**
	 * The "default" attack of a character.
	 * @return the attack object associated with a punch.
	 */
	public Attack getPunch() {
		return this.allAttacks[AttackType.PUNCH.ordinal()].copy();
	}

	/**
	 * An attack for if the player is in the air.
	 * @return the attack associated with a mid-air kick.
	 */
	public Attack getJumpKick() {
		return this.allAttacks[AttackType.JUMPKICK.ordinal()].copy();
	}

	@Override
	/**
	 * This checks for equality between and object and AttackGroup.
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (this == obj) {
			return true;
		} else if (!(obj instanceof AttackGroup)) {
			return false;
		} else {
			AttackGroup ag = (AttackGroup) obj;
			return Arrays.equals(this.allAttacks, ag.allAttacks);
		}
	}

}
