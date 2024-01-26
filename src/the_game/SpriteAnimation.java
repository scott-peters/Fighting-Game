//copied from CPSC Dropbox - @author NathalyVerwaal CPSC 219
package the_game;

import javafx.geometry.Rectangle2D;
import javafx.animation.*;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Based on:
 * https://netopyr.com/2012/03/09/creating-a-sprite-animation-with-javafx/
 */
public final class SpriteAnimation extends Transition {
	private ImageView imageView; // contains the image sprite
	private int numOfImages; // number of images on the image sprite
	private int columns; // number of columns the image sprite contains
	private int offsetX; // xcoord of topleft point of first image on image sprite
	private int offsetY; // ycoord of top left point of the first image on image sprite
	private int width; // width of each image on the image sprite
	private int height; // height of each image on the image sprite

	/**
	 * This is the constructor for SpriteAnimation.
	 * 
	 * @param imageView  the ImageView of the Player
	 * @param duration  the duration of the animation
	 * @param numOfImages  the number of images on the animation image
	 * @param columns  the number of columns on the amination image
	 * @param offsetX  the x offset of the images on the animation image
	 * @param offsetY  the y offset of the images on the animation image
	 * @param width  the width of the images on the animation image
	 * @param height  the height of the images ont he animation image
	 */
	public SpriteAnimation(ImageView imageView, Duration duration, int numOfImages, int columns, int offsetX,
			int offsetY, int width, int height) {

		this.imageView = imageView;
		this.numOfImages = numOfImages;
		this.columns = columns;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.width = width;
		this.height = height;
		setCycleDuration(duration);
		setInterpolator(Interpolator.LINEAR);
	}

	/**
	 * This allows us to cycle through animation images.
	 * 
	 * @param fractionOfCycle indicates how far we are currently in this cycle of
	 *                        animation. This is a number between 0 and 1 and
	 *                        indicates a percentage.
	 */
	protected void interpolate(double fractionOfCycle) {

		/*
		 * Compute the index of the nearest index based on where we are in the cycle.
		 * Note that this means that the first and last image are displayed for a
		 * shorter time than the other images.
		 */
		int index = (int) Math.round(fractionOfCycle * (numOfImages - 1));

		// check which column this index falls into based on the number of columns in
		// the image sprite
		int column = (index % columns);

		// find the x-coordinate based on the width of each column with offsetX as the
		// x- coordinate for the first column
		int x = column * width + offsetX;

		// Similar computation for row and y coordinate.
		int row = (index / columns);
		int y = row * height + offsetY;

		// indicate which part of the image should be displayed
		imageView.setViewport(new Rectangle2D(x, y, width, height));
	}

}
