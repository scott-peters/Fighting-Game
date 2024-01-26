package the_game;

import java.io.File;

import javafx.scene.image.Image;

/**
 * A way to load images based off of an relative path from a give absolute path.
 * 
 * @author Lucas Brown
 */
public final class SmartImageLoader {

	private final Image img;

	/**
	 * Load an image from the given absolute file.
	 * 
	 * @param filePath the absolute file path of the Image attempting to be loaded.
	 * @throws IllegalArgumentException if the given file failed to load.
	 */
	public SmartImageLoader(String filePath) throws IllegalArgumentException {
		String base = (new File("")).getAbsolutePath() + System.getProperty("file.separator") + "src";
		String imgPath = filePath.replace(base, "");
		this.img = new Image(imgPath);
		if (this.img.isError()) {
			throw new IllegalArgumentException("filePath does not reference an image file.");
		}
	}

	/**
	 * This gets the Image if successfully loaded.
	 * 
	 * @return The Image if successfully loaded, else null.
	 */
	public Image getImage() {
		return this.img;
	}

	/**
	 * Converts every SmartImageLoader to an Image.
	 * 
	 * @param silArr an array of SmartImageLoader's.
	 * @return an array of corresponding images.
	 */
	public static Image[] toImageArray(SmartImageLoader[] silArr) {
		Image[] imgArr = new Image[silArr.length];
		for (int i = 0; i < silArr.length; i++) {
			imgArr[i] = silArr[i].getImage();
		}
		return imgArr;
	}
}
