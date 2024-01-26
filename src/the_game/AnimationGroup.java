//Original drawings for Bub were done by Sythes @ SpritersResource - https://www.spriters-resource.com/custom_edited/bubblebobblecustoms/ 
//additional animations were hand drawn by Jess
//Original drawings for Kermit were ripped by A.J.Nitro - https://www.spriters-resource.com/game_boy_gbc/muppets/sheet/45414/
//additional or editing of animations for game use were manually sorted by Jess
package the_game;

import java.awt.geom.Point2D;
import java.util.Arrays;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import the_game.ObjectLoader;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * A collection of animations associated to the several states that each Player
 * can occupy. Use of these different states is governed by the index of the
 * parallel arrays of the image and the text file in order to create an
 * animation on a given image view.
 * 
 */
public final class AnimationGroup {

	private final Image[] allImages;
	private final AnimationTextLoader[] allTextFiles;
	private final Point2D.Double size;

	private long duration;
	private int numOfImage;
	private int columns;
	private int offsetX;
	private int offsetY;
	private int pWidth;
	private int pHeight;

	/**
	 * Separates the folderPath into assumed-to-exist Image and Text sub-folders
	 * which then load all Image's and AnimationTextLoader's respectively. Then,
	 * using a consistent naming system between AnimationGroup folders, organizes
	 * each Object into its corresponding place within the parallel arrays for later
	 * use.
	 * 
	 * @param folderPath  the folder path of the animation images
	 * @param size  the size of the player
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public AnimationGroup(String folderPath, Point2D.Double size) throws FileNotFoundException, IOException {
		this.size = size;
		Image[] unsortedImages = SmartImageLoader.toImageArray(
				(new ObjectLoader<SmartImageLoader>(folderPath + "//Images", SmartImageLoader.class)).getAllObjects());
		AnimationTextLoader[] unsortedText = (new ObjectLoader<AnimationTextLoader>(folderPath + "//Text",
				AnimationTextLoader.class)).getAllObjects();

		this.allImages = new Image[unsortedImages.length];
		this.allTextFiles = new AnimationTextLoader[unsortedText.length];
		for (int i = 0; i < unsortedImages.length; i++) {
			if (unsortedImages[i].getUrl().contains("Stance")) {
				this.allImages[getStanceIndex()] = unsortedImages[i];
			} else if (unsortedImages[i].getUrl().contains("Walk")) {
				this.allImages[getWalkingIndex()] = unsortedImages[i];
			} else if (unsortedImages[i].getUrl().contains("Jump")) {
				this.allImages[getJumpingIndex()] = unsortedImages[i];
			} else if (unsortedImages[i].getUrl().contains("Punch")) {
				this.allImages[getPunchingIndex()] = unsortedImages[i];
			} else if (unsortedImages[i].getUrl().contains("Death")) {
				this.allImages[getDeathIndex()] = unsortedImages[i];
			} else if (unsortedImages[i].getUrl().contains("Block")) {
				this.allImages[getBlockingIndex()] = unsortedImages[i];
			}


			if (unsortedText[i].getFilePath().contains("Stance")) {
				this.allTextFiles[getStanceIndex()] = unsortedText[i];
			} else if (unsortedText[i].getFilePath().contains("Walk")) {
				this.allTextFiles[getWalkingIndex()] = unsortedText[i];
			} else if (unsortedText[i].getFilePath().contains("Jump")) {
				this.allTextFiles[getJumpingIndex()] = unsortedText[i];
			} else if (unsortedText[i].getFilePath().contains("Punch")) {
				this.allTextFiles[getPunchingIndex()] = unsortedText[i];
			} else if (unsortedText[i].getFilePath().contains("Death")) {
				this.allTextFiles[getDeathIndex()] = unsortedText[i];
			} else if (unsortedText[i].getFilePath().contains("Block")) {
				this.allTextFiles[getBlockingIndex()] = unsortedText[i];
			}
		}
	}

	/**
	 * Copy's every Image and text file into a new instance.
	 * 
	 * @param toCopy the Image to be copied.
	 */
	public AnimationGroup(AnimationGroup toCopy) {
		this.allImages = toCopy.allImages;
		this.allTextFiles = toCopy.allTextFiles;
		this.size = (Point2D.Double) toCopy.size.clone();
	}

	/**
	 * This gets the duration of the animation
	 * 
	 * @param index the index of the animation image
	 * @return  the duration of time the animation is
	 */
	public long getDuration(int index) {
		return this.allTextFiles[index].getDuration();
	}

	/**
	 * Sets standing still animation for the characters
	 * 
	 * @param iv  ImageView of the Player
	 */
	public void setStance(ImageView iv) {
		this.setAnimation(iv, getStanceIndex());
	}

	/**
	 * Sets walking animations for the characters
	 * 
	 * @param iv  ImageView of the Player
	 */
	public void setWalking(ImageView iv) {
		this.setAnimation(iv, getWalkingIndex());
	}

	/**
	 * Sets jumping animations for the characters
	 * 
	 * @param iv  ImageView of the Player
	 */
	public void setJumping(ImageView iv) {
		this.setAnimation(iv, getJumpingIndex());
	}

	/**
	 * Sets punching animations for the characters
	 * 
	 * @param iv  ImageView of the Player
	 */
	public void setPunching(ImageView iv) {
		this.setAnimation(iv, getPunchingIndex());
	}

	/**
	 * Index's used for assigning and selecting corresponding animations
	 * 
	 * @return 0  the index of the stance image
	 */
	public static int getStanceIndex() {
		return 0;
	}

	/**
	 * Index's used for assigning and selecting corresponding animations
	 * 
	 * @return 1  the index of the walking image
	 */
	public static int getWalkingIndex() {
		return 1;
	}

	/**
	 * Index's used for assigning and selecting corresponding animations
	 * 
	 * @return 2  the index of the jumping umage
	 */
	public static int getJumpingIndex() {
		return 2;
	}

	/**
	 * Index's used for assigning and selecting corresponding animations
	 * 
	 * @return 3  the index of the punching image
	 */
	public static int getPunchingIndex() {
		return 3;
	}

	/**
	 * Index's used for assigning and selecting corresponding animations
	 * 
	 * @return 4  the index of the blocking image
	 */
	public static int getBlockingIndex() {
		return 4;
	}

	/**
	 * Index's used for assigning and selecting corresponding animations
	 * 
	 * @return 5  the index of the death image
	 */
	public static int getDeathIndex() {
		return 5;
	}

	/**
	 * Sets up all of the necessary components to get the animations working
	 * properly, such as offset x and y, picture width and height etc.
	 * 
	 * @param iv    corresponding image view to the animation needed
	 * @param index which animation that needs to be used
	 */
	public void setAnimation(ImageView iv, int index) {
		this.duration = this.allTextFiles[index].getDuration();
		this.numOfImage = this.allTextFiles[index].getNumOfImage();
		this.columns = this.allTextFiles[index].getColumns();
		this.offsetX = this.allTextFiles[index].getOffsetX();
		this.offsetY = this.allTextFiles[index].getOffsetY();
		this.pWidth = this.allTextFiles[index].getPWidth();
		this.pHeight = this.allTextFiles[index].getPHeight();
		iv.setImage(this.allImages[index]);

		iv.setFitWidth(this.size.x + pWidth);

		iv.setImage(this.allImages[index]);

		Platform.runLater(() -> {

			Animation animation = new SpriteAnimation(iv, Duration.millis(duration), numOfImage, columns, offsetX,
					offsetY, (int) this.size.x + pWidth, (int) (this.size.y + pHeight));
			animation.setCycleCount(Animation.INDEFINITE);
			animation.play();
		});

	}

	@Override
	/**
	 * This checks for equality of an object and an AnimationGroup.
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (this == obj) {
			return true;
		} else if (!(obj instanceof AnimationGroup)) {
			return false;
		} else {
			AnimationGroup ag = (AnimationGroup) obj;
			return Arrays.equals(this.allImages, ag.allImages) && Arrays.equals(this.allTextFiles, ag.allTextFiles)
					&& this.size.equals(ag.size);
		}
	}
}