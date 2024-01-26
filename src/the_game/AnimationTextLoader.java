package the_game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Stores all the information within the given text file as an immutable
 * representation of the information used by it's associated animation file.
 */
public final class AnimationTextLoader {
	private long duration;
	private int numOfImage;
	private int columns;
	private int offsetX;
	private int offsetY;
	private int PWidth;
	private int PHeight;
	private String filePath;

	/**
	 * Unloads every value into it's corresponding variable.
	 * 
	 * @param filePath the location of the file being loaded.
	 */
	public AnimationTextLoader(String filePath) {
		this.filePath = filePath;
		String text = "";
		int lineNumber;
		try {
			FileReader readFile = new FileReader(filePath);
			BufferedReader br = new BufferedReader(readFile);

			for (lineNumber = 1; lineNumber < 15; lineNumber++) {
				if (lineNumber == 3) {
					text = br.readLine().substring(10);
					this.duration = Long.parseLong(text);
				} else if (lineNumber == 5) {
					text = br.readLine().substring(18);
					this.numOfImage = Integer.parseInt(text);
				} else if (lineNumber == 7) {
					text = br.readLine().substring(19);
					this.columns = Integer.parseInt(text);
				} else if (lineNumber == 9) {
					text = br.readLine().substring(9);
					this.offsetX = Integer.parseInt(text);
				} else if (lineNumber == 11) {
					text = br.readLine().substring(9);
					this.offsetY = Integer.parseInt(text);
				} else if (lineNumber == 14) {
					text = br.readLine().substring(7);
					this.PWidth = Integer.parseInt(text);
				} else if (lineNumber == 16) {
					text = br.readLine().substring(8);
					this.PHeight = Integer.parseInt(text);
				} else
					br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gives the duration of the animation
	 * 
	 * @return duration of the animation
	 */
	public long getDuration() {
		return this.duration;
	}

	/**
	 * This is a getter for the number of images for the animation cycle.
	 * 
	 * @return number of images in the animation cycle
	 */
	public int getNumOfImage() {
		return this.numOfImage;
	}

	/**
	 * This gets the number of columns in the animation image.
	 * 
	 * @return number of columns in the set for the animation.
	 */
	public int getColumns() {
		return this.columns;
	}

	/**
	 * This gets the x offset of the images to be used for animations.
	 * 
	 * @return the x offset needed to get the picture loaded properly for animation
	 */
	public int getOffsetX() {
		return this.offsetX;
	}

	/**
	 * This gets the y offset of the images to be used for animations.
	 * 
	 * @return the y offset needed to get the picture loaded properly for animation
	 */
	public int getOffsetY() {
		return this.offsetY;
	}

	/**
	 * This gets the width of the pictures on the animation image.
	 * 
	 * @return the picture width.
	 */
	public int getPWidth() {
		return this.PWidth;
	}

	/**
	 * This gets the height of the pictures on the animation image.
	 * 
	 * @return the picture height.
	 */
	public int getPHeight() {
		return this.PHeight;
	}

	/**
	 * This gets the absolute path of the file. This is equal to the path given 
	 * in the constructor of the object.
	 * 
	 * @return the absolute path of the file. Equal to the path given in the
	 *         constructor of the object.
	 */
	public String getFilePath() {
		return this.filePath;
	}

	@Override
	/**
	 * This tests for equality of an object and an AnimationText.
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (this == obj) {
			return true;
		} else if (!(obj instanceof AnimationTextLoader)) {
			return false;
		} else {
			AnimationTextLoader atl = (AnimationTextLoader) obj;
			return this.columns == atl.columns && this.filePath.equals(atl.filePath)
					&& this.numOfImage == atl.numOfImage && this.offsetX == atl.offsetX && this.offsetY == atl.offsetY
					&& this.PHeight == atl.PHeight && this.PWidth == atl.PWidth && this.duration == atl.duration;
		}
	}
}
