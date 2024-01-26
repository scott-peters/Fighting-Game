// The head of the stickmen
// Adapted from code from "Drawing a Simple Circle" from stack overflow answers.
// https://stackoverflow.com/questions/19483641/drawing-a-simple-circle

//UTF-8 code for full box: http://www.fileformat.info/info/unicode/char/25fe/index.htm
//UTF-8 code for hollow box: http://www.fileformat.info/info/unicode/char/25fd/index.htm

//Understanding how to set up 2D array code
//Adapted from code from "Multidimensional Arrays in Java" from GeeksforGeeks
//https://www.geeksforgeeks.org/multidimensional-arrays-in-java/

package game_interface;

import the_game.Game;
import the_game.Game.Team;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.lang.Math;

import org.junit.Test;

/**
 * This class draws the screen output of the game to standard out by using a
 * series of arbitrary symbols in sequence to generate a visual representation
 * of the game.
 * 
 * @version 1.2
 */
public class ASCII implements GameInterface {
	/**
	 * This acts like a time limit for each turn.
	 */
	public static final double TIME_INTERVAL = 5000; // This acts more like a time limit for each turn.
	/**
	 * This makes it so when you run each step it's this long.
	 */
	public static final double GAME_TIME = 300; // run each step as being this long.

	// Many variables to coordinate the conversion of a Game object to a visible
	// representation.
	private final int FACE_RADIUS = 5, FACE_X = 5, FACE_Y = 5, WIDTH = 125, HEIGHT_TOP = 7, HEIGHT_BOTTOM = 2;
	private final int HEIGHT_BARS = 12, HEIGHT_MEN = 23, PLAYER_LINE = 5, HEALTH_LINE = 6, FACE_LENGTH = 10;
	private final int FACE_LENGTH2 = 14, BODY_LENGTH = 19, BODY_LENGTH2 = 20, LEG_LENGTH = 23, MAN_WIDTH = 11;

	private final boolean HIT_BOX = false;

	@Override
	/**
	 * 
	 */
	public double getRuntimeInterval() {
		return ASCII.TIME_INTERVAL;
	}

	@Override
	/**
	 * 
	 */
	public double getGametimeInterval() {
		return ASCII.GAME_TIME;
	}

	/**
	 * This draws the first seven rows of the screen output.
	 * 
	 * @param screen       the char[][] of the first part of screen to be filled in
	 * @param healthP1     the current health of player one
	 * @param healthP2     the current health of player two
	 * @param fullHealthP1 the maximum health of player one
	 * @param fullHealthP2 the maximum health of player two
	 */
	private void drawBackground(char[][] screen, double healthP1, double healthP2, double fullHealthP1,
			double fullHealthP2) {

		// for background
		int healthStart1 = 10;
		int healthEnd1 = 23;
		int healthStart2 = WIDTH - 24;
		int healthEnd2 = WIDTH - 11;

		// for health bars
		int fullHealth = 10; // can't be dependent on variable full health as wouldn't print out consistently
		int realHealthP1 = healthToInt(healthP1, fullHealthP1);
		int realHealthP2 = healthToInt(healthP2, fullHealthP2);
		int healthBoxStart1 = 12;
		int healthBoxEnd1 = 21;
		int healthBoxStart2 = WIDTH - 22;
		int healthBoxEnd2 = WIDTH - 13;

		// char[][] screen = new char[HEIGHT_MEN][WIDTH];

		for (int row = 0; row < HEIGHT_TOP; row++) {
			for (int column = 0; column < WIDTH; column++) {

				// top bars
				if (row == 0 || row == 1) {
					screen[row][column] = '%';
				}
				// right and left bars
				else if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2) {
					screen[row][column] = '%';
				}

				// player one print out
				else if (row == PLAYER_LINE && column >= 12 && column <= 21) {
					String one = "Player One";

					for (int index = 0; index < one.length(); index++) {
						screen[PLAYER_LINE][index + 12] = one.charAt(index);
					}
				}

				// player one health bars
				else if (row == HEALTH_LINE && column == healthStart1 || row == HEALTH_LINE && column == healthEnd1) {
					screen[HEALTH_LINE][healthStart1] = '|';
					screen[HEALTH_LINE][healthEnd1] = '|';
				}

				// player one health
				else if (row == HEALTH_LINE && column >= healthBoxStart1 && column <= healthBoxEnd1) {

					for (int h = 0; h <= fullHealth; h++) {
						if (column == healthBoxEnd1 - h) {
							if (realHealthP1 > h) {
								screen[HEALTH_LINE][column] = '\u25FE'; // see comments at top UTF-8 code full box
							} else {
								screen[HEALTH_LINE][column] = '\u25FD'; // see comments at top UTF-8 code empty box
							}
						}
					}
				}

				// player two print out
				else if (row == PLAYER_LINE && column >= WIDTH - 22 && column <= WIDTH - 13) {
					String two = "Player Two";

					for (int index = 0; index < two.length(); index++) {
						screen[PLAYER_LINE][index + (WIDTH - 22)] = two.charAt(index);
					}
				}
				// player two health bars
				else if (row == HEALTH_LINE && column == healthStart2 || row == HEALTH_LINE && column == healthEnd2) {
					screen[HEALTH_LINE][healthStart2] = '|';
					screen[HEALTH_LINE][healthEnd2] = '|';
				}

				// player two health
				else if (row == HEALTH_LINE && column >= healthBoxStart2 && column <= healthBoxEnd2) {

					for (int h = 0; h <= fullHealth; h++) {
						if (column == healthBoxEnd2 - h) {
							if (realHealthP2 > h) {
								screen[HEALTH_LINE][column] = '\u25FE'; // see comments at top UTF-8 code full box
							} else {
								screen[HEALTH_LINE][column] = '\u25FD'; // see comments at top UTF-8 code empty box
							}
						}
					}
				}

				// white space - screen
				else {
					screen[row][column] = ' ';
				}

				System.out.print(screen[row][column]);
			}
			System.out.println();
		}

	}

	/**
	 * This calculates the current health of the player, scaled to their maximum
	 * health and turns it into an integer.
	 * 
	 * @param health           the current health of the player
	 * @param playerFullHealth the maximum health of the player
	 * @return the current health of the player as a number out of 10
	 */
	private int healthToInt(double health, double playerFullHealth) {

		if (health > playerFullHealth) {
			health = playerFullHealth;
		}

		double checkHealth = health / playerFullHealth;
		double scale = checkHealth * 10;
		int callHealth = (int) Math.ceil(scale);
		return callHealth;

	}

	/**
	 * This draws the side bars from the end of the health bar row to the player
	 * names row.
	 */
	private void drawSideBars() {
		char[][] sides = new char[HEIGHT_BARS][WIDTH];

		for (int row = 0; row < HEIGHT_BARS; row++) {
			for (int column = 0; column < WIDTH; column++) {

				// right and left bars
				if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2) {
					sides[row][column] = '%';
				} else {
					sides[row][column] = ' ';
				}
				System.out.print(sides[row][column]);
			}
			System.out.println();
		}
	}

	/**
	 * This draws the bottom two rows of the screen.
	 */
	private void drawBottomBars() {

		char[][] bottom = new char[HEIGHT_BOTTOM][WIDTH];

		for (int row = 0; row < HEIGHT_BOTTOM; row++) {
			for (int column = 0; column < WIDTH; column++) {
				bottom[row][column] = '%';
				System.out.print(bottom[row][column]);
			}
			System.out.println();
		}
	}

	/**
	 * This draws the player name above each player's head.
	 * 
	 * @param player1Pos    the position of player one
	 * @param player2Pos    the position of player two
	 * @param player1PosEnd the position of player one at its total width
	 * @param player2PosEnd the position of plater two at its total width
	 */
	private void drawPlayerNames(int player1Pos, int player2Pos, int player1PosEnd, int player2PosEnd) {

		char[][] names = new char[HEIGHT_BOTTOM][WIDTH];

		for (int row = 0; row < HEIGHT_BOTTOM; row++) {
			for (int column = 0; column < WIDTH; column++) {

				if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2) {
					names[row][column] = '%';
				}

				else if (row == 0 && column >= player1Pos + 1 && column < player1PosEnd) {
					String p1 = "Player One";
					for (int index = 0; index < p1.length(); index++) {
						names[row][index + player1Pos + 1] = p1.charAt(index);
					}
				} else if (row == 0 && column >= player2Pos + 1 && column < player2PosEnd) {
					String p2 = "Player Two";
					for (int index2 = 0; index2 < p2.length(); index2++) {
						names[row][index2 + player2Pos + 1] = p2.charAt(index2);
					}
				} else {
					names[row][column] = ' ';
				}

				System.out.print(names[row][column]);
			}
			System.out.println();
		}
	}

	/**
	 * This draws the print out of which player won the game in ASCII Art.
	 * 
	 * @param team the winning team
	 */
	private void drawPlayerWins(Team team) {

		// code adapted from "Learn to Create an ASCII Art Service in Java with Eclipse"
		// tutorial on YouTube by Sylvain Saurel
		// https://www.youtube.com/watch?v=B5dN9-9bEeg

		BufferedImage image = new BufferedImage(WIDTH, HEIGHT_BARS, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setFont(new Font("Lucida Sans", Font.PLAIN, 11));

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		if (team.equals(Team.LEFT)) {
			g2.drawString("PLAYER ONE WINS!!", 12, 10); // string, x, y
		} else if (team.equals(Team.RIGHT)) {
			g2.drawString("PLAYER TWO WINS!!", 11, 10); // string, x, y
		}

		for (int row = 0; row < HEIGHT_BARS; row++) {
			StringBuilder builder = new StringBuilder();
			for (int col = 0; col < WIDTH; col++) {

				if (col == 0 || col == 1 || col == WIDTH - 1 || col == WIDTH - 2) {
					builder.append('%');
				} else {
					builder.append(image.getRGB(col, row) == -16777216 ? " " : "@");
				}
			}
			System.out.println(builder);
		}

	}

	/**
	 * This draws the whole section of the output screen where the players are when
	 * both are normal stickmen. Also able to draw just the hit boxes of the players
	 * when hit box flag is true.
	 * 
	 * @param player1Pos    the position of player one
	 * @param player2Pos    the position of player two
	 * @param player1PosEnd the position of player one at its total width
	 * @param player2PosEnd the position of player two at its total width
	 */
	private void drawStickmanVsStickman(int player1Pos, int player2Pos, int player1PosEnd, 
			int player2PosEnd) {

		char[][] stickmanArray = new char[HEIGHT_MEN][WIDTH];

		for (int row = 0; row < HEIGHT_MEN; row++) {
			for (int column = 0; column < WIDTH; column++) {

				// side bars
				if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2)
					stickmanArray[row][column] = '%';

				// player one
				else if (column >= player1Pos && column < player1PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '#';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared =  (column - player1Pos - FACE_X) * (column - player1Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;
							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// eyes
							else if (column == 3 + player1Pos && row == 3 || column == 7 + player1Pos && row == 3) {
								stickmanArray[row][column] = '*';
							}
							// next 2 elifs for mouth
							else if (column == 3 + player1Pos && row == 6 || column == 7 + player1Pos && row == 6) {
								stickmanArray[row][column] = '*';
							} else if (column == 4 + player1Pos && row == 7 || column == 6 + player1Pos && row == 7) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {
							// left arm
							if (column == 1 + player1Pos && row == 13 || column == 3 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player1Pos && row == 13 || column == 7 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player1Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {
							// left leg
							if (column == 3 + player1Pos && row == 19 || column == 2 + player1Pos && row == 20
									|| column == 1 + player1Pos && row == 21 || column == 0 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player1Pos && row == 19 || column == 8 + player1Pos && row == 20
									|| column == 9 + player1Pos && row == 21
									|| column == 10 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// player two
				else if (column >= player2Pos && column <= player2PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '@';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player2Pos - FACE_X) * (column - player2Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// eyes
							else if (column == 3 + player2Pos && row == 3 || column == 7 + player2Pos && row == 3) {
								stickmanArray[row][column] = '*';
							}
							// next 2 elifs for mouth
							else if (column == 3 + player2Pos && row == 6 || column == 7 + player2Pos && row == 6) {
								stickmanArray[row][column] = '*';
							} else if (column == 4 + player2Pos && row == 7 || column == 6 + player2Pos && row == 7) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player2Pos && row == 13 || column == 3 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player2Pos && row == 13 || column == 7 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player2Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player2Pos && row == 19 || column == 2 + player2Pos && row == 20
									|| column == 1 + player2Pos && row == 21 || column == 0 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player2Pos && row == 19 || column == 8 + player2Pos && row == 20
									|| column == 9 + player2Pos && row == 21
									|| column == 10 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// white space
				else {
					stickmanArray[row][column] = ' ';
				}
				System.out.print(stickmanArray[row][column]);
			}
			System.out.println();

		}

	}

	/**
	 * This draws the whole section of the output screen where the players are when
	 * player one has won the game. Also able to draw just the hit boxes of the
	 * player when hit box flag is true.
	 * 
	 * @param player1Pos    the position of player one
	 * @param player2Pos    the position of player two
	 * @param player1PosEnd the position of player one at its total width
	 * @param player2PosEnd the position of player two at its total width
	 */
	private void drawStickmanVsDead(int player1Pos, int player2Pos, int player1PosEnd, 
			int player2PosEnd) {

		char[][] stickmanArray = new char[HEIGHT_MEN][WIDTH];

		for (int row = 0; row < HEIGHT_MEN; row++) {
			for (int column = 0; column < WIDTH; column++) {

				// side bars
				if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2)
					stickmanArray[row][column] = '%';

				// player one
				else if (column >= player1Pos && column < player1PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '#';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player1Pos - FACE_X) * (column - player1Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;
							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// eyes
							else if (column == 3 + player1Pos && row == 3 || column == 7 + player1Pos && row == 3) {
								stickmanArray[row][column] = '*';
							}
							// next 2 elifs for mouth
							else if (column == 3 + player1Pos && row == 6 || column == 7 + player1Pos && row == 6) {
								stickmanArray[row][column] = '*';
							} else if (column == 4 + player1Pos && row == 7 || column == 6 + player1Pos && row == 7) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < BODY_LENGTH) {
							// left arm
							if (column == 1 + player1Pos && row == 13 || column == 3 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player1Pos && row == 13 || column == 7 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player1Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {
							// left leg
							if (column == 3 + player1Pos && row == 19 || column == 2 + player1Pos && row == 20
									|| column == 1 + player1Pos && row == 21 || column == 0 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player1Pos && row == 19 || column == 8 + player1Pos && row == 20
									|| column == 9 + player1Pos && row == 21
									|| column == 10 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// player two
				else if (column >= player2Pos && column <= player2PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '@';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player2Pos - FACE_X) * (column - player2Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// eyes
							else if (column == 3 + player2Pos && row == 3 || column == 7 + player2Pos && row == 3) {
								stickmanArray[row][column] = 'X';
							}
							// mouth
							else if (column >= 3 + player2Pos && row == 6 && column <= 7 + player2Pos) {
								stickmanArray[row][column] = '_';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player2Pos && row == 15 || column == 3 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player2Pos && row == 15 || column == 7 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player2Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player2Pos && row == 19 || column == 2 + player2Pos && row == 20
									|| column == 1 + player2Pos && row == 21 || column == 0 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player2Pos && row == 19 || column == 8 + player2Pos && row == 20
									|| column == 9 + player2Pos && row == 21
									|| column == 10 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// white space
				else {
					stickmanArray[row][column] = ' ';
				}
				System.out.print(stickmanArray[row][column]);
			}
			System.out.println();

		}

	}

	/**
	 * This draws the whole section of the output screen where the players are when
	 * player two has won the game. Also able to draw just the hit boxes of the
	 * players when hit box flag is true.
	 * 
	 * @param player1Pos    the position of player one
	 * @param player2Pos    the position of player two
	 * @param player1PosEnd the position of player one at its total width
	 * @param player2PosEnd the position of plater two at its total width
	 */
	private void drawDeadVsStickman(int player1Pos, int player2Pos, int player1PosEnd, 
			int player2PosEnd) {

		char[][] stickmanArray = new char[HEIGHT_MEN][WIDTH];

		for (int row = 0; row < HEIGHT_MEN; row++) {
			for (int column = 0; column < WIDTH; column++) {

				// side bars
				if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2)
					stickmanArray[row][column] = '%';

				// player one
				else if (column >= player1Pos && column < player1PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '#';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player1Pos - FACE_X) * (column - player1Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// eyes
							else if (column == 3 + player1Pos && row == 3 || column == 7 + player1Pos && row == 3) {
								stickmanArray[row][column] = 'X';
							}
							// mouth
							else if (column >= 3 + player1Pos && row == 6 && column <= 7 + player1Pos) {
								stickmanArray[row][column] = '_';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player1Pos && row == 15 || column == 3 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player1Pos && row == 15 || column == 7 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player1Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player1Pos && row == 19 || column == 2 + player1Pos && row == 20
									|| column == 1 + player1Pos && row == 21 || column == 0 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player1Pos && row == 19 || column == 8 + player1Pos && row == 20
									|| column == 9 + player1Pos && row == 21
									|| column == 10 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// player two
				else if (column >= player2Pos && column < player2PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '@';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player2Pos - FACE_X) * (column - player2Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// eyes
							else if (column == 3 + player2Pos && row == 3 || column == 7 + player2Pos && row == 3) {
								stickmanArray[row][column] = '*';
							}
							// next 2 elifs for mouth
							else if (column == 3 + player2Pos && row == 6 || column == 7 + player2Pos && row == 6) {
								stickmanArray[row][column] = '*';
							} else if (column == 4 + player2Pos && row == 7 || column == 6 + player2Pos && row == 7) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {
							// left arm
							if (column == 1 + player2Pos && row == 13 || column == 3 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player2Pos && row == 13 || column == 7 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player2Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {
							// left leg
							if (column == 3 + player2Pos && row == 19 || column == 2 + player2Pos && row == 20
									|| column == 1 + player2Pos && row == 21 || column == 0 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player2Pos && row == 19 || column == 8 + player2Pos && row == 20
									|| column == 9 + player2Pos && row == 21
									|| column == 10 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// white space
				else {
					stickmanArray[row][column] = ' ';
				}
				System.out.print(stickmanArray[row][column]);
			}
			System.out.println();

		}

	}

	/**
	 * This draws the whole section of the output screen where the players are when
	 * player one is being attacked by player two within range. Also able to draw
	 * just the hit boxes of the players when hit box flag is true.
	 * 
	 * @param player1Pos    the position of player one
	 * @param player2Pos    the position of player two
	 * @param player1PosEnd the position of player one at its total width
	 * @param player2PosEnd the position of player two at its total width
	 */
	private void drawAttackedVsAttacking(int player1Pos, int player2Pos, int player1PosEnd, 
			int player2PosEnd) {

		char[][] stickmanArray = new char[HEIGHT_MEN][WIDTH];

		for (int row = 0; row < HEIGHT_MEN; row++) {
			for (int column = 0; column < WIDTH; column++) {

				// side bars
				if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2)
					stickmanArray[row][column] = '%';

				// player one
				else if (column >= player1Pos && column < player1PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '#';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player1Pos - FACE_X) * (column - player1Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// eyebrows
							else if (column == 3 + player1Pos && row == 3 || column == 7 + player1Pos && row == 3) {
								stickmanArray[row][column] = '^';
							}
							// eyes
							else if (column == 3 + player1Pos && row == 4 || column == 7 + player1Pos && row == 4) {
								stickmanArray[row][column] = 'O';
							}
							// mouth
							else if (column >= 4 + player1Pos && row == 7 && column <= 6 + player1Pos) {
								stickmanArray[row][column] = '~';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player1Pos && row == 12 || column == 1 + player1Pos && row == 13
									|| column == 1 + player1Pos && row == 14 || column == 3 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player1Pos && row == 12 || column == 9 + player1Pos && row == 13
									|| column == 7 + player1Pos && row == 14 || column == 9 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player1Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player1Pos && row == 19 || column == 2 + player1Pos && row == 20
									|| column == 1 + player1Pos && row == 21 || column == 0 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player1Pos && row == 19 || column == 8 + player1Pos && row == 20
									|| column == 9 + player1Pos && row == 21
									|| column == 10 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// player two
				else if (column >= player2Pos && column < player2PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '@';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player2Pos - FACE_X) * (column - player2Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// right eyebrow
							else if (column == 7 + player2Pos && row == 3) {
								stickmanArray[row][column] = '/';
							}
							// left eyebrow
							else if (column == 3 + player2Pos && row == 3) {
								stickmanArray[row][column] = '\\';
							}
							// eyes
							else if (column == 3 + player2Pos && row == 4 || column == 7 + player2Pos && row == 4) {
								stickmanArray[row][column] = '*';
							}
							// mouth
							else if (column >= 4 + player2Pos && row == 7 && column <= 6 + player2Pos) {
								stickmanArray[row][column] = '#';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player2Pos && row == 13 || column == 3 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player2Pos && row == 13 || column == 7 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player2Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player2Pos && row == 19 || column == 2 + player2Pos && row == 20
									|| column == 1 + player2Pos && row == 21 || column == 0 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player2Pos && row == 19 || column == 8 + player2Pos && row == 20
									|| column == 9 + player2Pos && row == 21
									|| column == 10 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// white space
				else {
					stickmanArray[row][column] = ' ';
				}
				System.out.print(stickmanArray[row][column]);
			}
			System.out.println();

		}

	}

	/**
	 * This draws the whole section of the output screen where the players are when
	 * player two is being attacked by player one within range. Also able to draw
	 * just the hit boxes of the players when hit box flag is true.
	 * 
	 * @param player1Pos    the position of player one
	 * @param player2Pos    the position of player two
	 * @param player1PosEnd the position of player one at its total width
	 * @param player2PosEnd the position of player two at its total width
	 */
	private void drawAttackingVsAttacked(int player1Pos, int player2Pos, int player1PosEnd, 
			int player2PosEnd) {

		char[][] stickmanArray = new char[HEIGHT_MEN][WIDTH];

		for (int row = 0; row < HEIGHT_MEN; row++) {
			for (int column = 0; column < WIDTH; column++) {

				// side bars
				if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2)
					stickmanArray[row][column] = '%';

				// player one
				else if (column >= player1Pos && column < player1PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '#';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player1Pos - FACE_X) * (column - player1Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// right eyebrow
							else if (column == 7 + player1Pos && row == 3) {
								stickmanArray[row][column] = '/';
							}
							// left eyebrow
							else if (column == 3 + player1Pos && row == 3) {
								stickmanArray[row][column] = '\\';
							}
							// eyes
							else if (column == 3 + player1Pos && row == 4 || column == 7 + player1Pos && row == 4) {
								stickmanArray[row][column] = '*';
							}
							// mouth
							else if (column >= 4 + player1Pos && row == 7 && column <= 6 + player1Pos) {
								stickmanArray[row][column] = '#';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player1Pos && row == 13 || column == 3 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player1Pos && row == 13 || column == 7 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player1Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player1Pos && row == 19 || column == 2 + player1Pos && row == 20
									|| column == 1 + player1Pos && row == 21 || column == 0 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player1Pos && row == 19 || column == 8 + player1Pos && row == 20
									|| column == 9 + player1Pos && row == 21
									|| column == 10 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

					}
				}

				// player two
				else if (column >= player2Pos && column < player2PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '@';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {

							int xSquared = (column - player2Pos - FACE_X) * (column - player2Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// eyebrows
							else if (column == 3 + player2Pos && row == 3 || column == 7 + player2Pos && row == 3) {
								stickmanArray[row][column] = '^';
							}
							// eyes
							else if (column == 3 + player2Pos && row == 4 || column == 7 + player2Pos && row == 4) {
								stickmanArray[row][column] = 'O';
							}
							// mouth
							else if (column >= 4 + player2Pos && row == 7 && column <= 6 + player2Pos) {
								stickmanArray[row][column] = '~';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player2Pos && row == 12 || column == 1 + player2Pos && row == 13
									|| column == 1 + player2Pos && row == 14 || column == 3 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player2Pos && row == 12 || column == 9 + player2Pos && row == 13
									|| column == 7 + player2Pos && row == 14 || column == 9 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player2Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player2Pos && row == 19 || column == 2 + player2Pos && row == 20
									|| column == 1 + player2Pos && row == 21 || column == 0 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player2Pos && row == 19 || column == 8 + player2Pos && row == 20
									|| column == 9 + player2Pos && row == 21
									|| column == 10 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// white space
				else {
					stickmanArray[row][column] = ' ';
				}
				System.out.print(stickmanArray[row][column]);
			}
			System.out.println();

		}

	}

	/**
	 * This draws the whole section of the output screen where the players are when
	 * player one is attacking and player two is blocking. Also able to draw just
	 * the hit boxes of the players when hit box flag is true.
	 * 
	 * @param player1Pos    the position of player one
	 * @param player2Pos    the position of player two
	 * @param player1PosEnd the position of player one at its total width
	 * @param player2PosEnd the position of player two at its total width
	 */
	private void drawAttackingVsBlocking(int player1Pos, int player2Pos, int player1PosEnd, 
			int player2PosEnd) {

		char[][] stickmanArray = new char[HEIGHT_MEN][WIDTH];

		for (int row = 0; row < HEIGHT_MEN; row++) {
			for (int column = 0; column < WIDTH; column++) {

				// side bars
				if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2)
					stickmanArray[row][column] = '%';

				// player one
				else if (column >= player1Pos && column < player1PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '#';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player1Pos - FACE_X) * (column - player1Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// right eyebrow
							else if (column == 7 + player1Pos && row == 3) {
								stickmanArray[row][column] = '/';
							}
							// left eyebrow
							else if (column == 3 + player1Pos && row == 3) {
								stickmanArray[row][column] = '\\';
							}
							// eyes
							else if (column == 3 + player1Pos && row == 4 || column == 7 + player1Pos && row == 4) {
								stickmanArray[row][column] = '*';
							}
							// mouth
							else if (column >= 4 + player1Pos && row == 7 && column <= 6 + player1Pos) {
								stickmanArray[row][column] = '#';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player1Pos && row == 13 || column == 3 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player1Pos && row == 13 || column == 7 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player1Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player1Pos && row == 19 || column == 2 + player1Pos && row == 20
									|| column == 1 + player1Pos && row == 21 || column == 0 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player1Pos && row == 19 || column == 8 + player1Pos && row == 20
									|| column == 9 + player1Pos && row == 21
									|| column == 10 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

					}
				}

				// player two
				else if (column >= player2Pos && column < player2PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '@';
					}

					else {

						// face
						if (row <= FACE_LENGTH2) {
							int xSquared = (column - player2Pos - FACE_X) * (column - player2Pos - FACE_X);
							int ySquared = (row - 4 - FACE_Y) * (row - 4 - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// right eyebrow
							else if (column == 7 + player2Pos && row == 7) {
								stickmanArray[row][column] = '\\';
							}
							// left eyebrow
							else if (column == 3 + player2Pos && row == 7) {
								stickmanArray[row][column] = '/';
							}
							// eyes
							else if (column == 3 + player2Pos && row == 8 || column == 7 + player2Pos && row == 8) {
								stickmanArray[row][column] = '@';
							}
							// mouth
							else if (column >= 4 + player2Pos && row == 11 && column <= 6 + player2Pos) {
								stickmanArray[row][column] = '=';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH2) {

							// left arm
							if (column == 1 + player2Pos && row == 16 || column == 3 + player2Pos && row == 17) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player2Pos && row == 16 || column == 7 + player2Pos && row == 17) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player2Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player2Pos && row == 20 || column == 2 + player2Pos && row == 21
									|| column == 1 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player2Pos && row == 20 || column == 8 + player2Pos && row == 21
									|| column == 9 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// white space
				else {
					stickmanArray[row][column] = ' ';
				}
				System.out.print(stickmanArray[row][column]);
			}
			System.out.println();

		}

	}

	/**
	 * This draws the whole section of the output screen where the players are when
	 * player one is blocking and player two is attacking. Also able to draw just
	 * the hit boxes of the players when hit box flag is true.
	 * 
	 * @param player1Pos    the position of player one
	 * @param player2Pos    the position of player two
	 * @param player1PosEnd the position of player one at its total width
	 * @param player2PosEnd the position of player two at its total width
	 */
	private void drawBlockingVsAttacking(int player1Pos, int player2Pos, int player1PosEnd, 
			int player2PosEnd) {

		char[][] stickmanArray = new char[HEIGHT_MEN][WIDTH];

		for (int row = 0; row < HEIGHT_MEN; row++) {
			for (int column = 0; column < WIDTH; column++) {

				// side bars
				if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2)
					stickmanArray[row][column] = '%';

				// player one
				else if (column >= player1Pos && column < player1PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '#';
					}

					else {

						// face
						if (row <= FACE_LENGTH2) {
							int xSquared = (column - player1Pos - FACE_X) * (column - player1Pos - FACE_X);
							int ySquared = (row - 4 - FACE_Y) * (row - 4 - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// right eyebrow
							else if (column == 7 + player1Pos && row == 7) {
								stickmanArray[row][column] = '\\';
							}
							// left eyebrow
							else if (column == 3 + player1Pos && row == 7) {
								stickmanArray[row][column] = '/';
							}
							// eyes
							else if (column == 3 + player1Pos && row == 8 || column == 7 + player1Pos && row == 8) {
								stickmanArray[row][column] = '@';
							}
							// mouth
							else if (column >= 4 + player1Pos && row == 11 && column <= 6 + player1Pos) {
								stickmanArray[row][column] = '=';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH2) {

							// left arm
							if (column == 1 + player1Pos && row == 16 || column == 3 + player1Pos && row == 17) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player1Pos && row == 16 || column == 7 + player1Pos && row == 17) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player1Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player1Pos && row == 20 || column == 2 + player1Pos && row == 21
									|| column == 1 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player1Pos && row == 20 || column == 8 + player1Pos && row == 21
									|| column == 9 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// player two
				else if (column >= player2Pos && column <= player2PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '@';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player2Pos - FACE_X) * (column - player2Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// right eyebrow
							else if (column == 7 + player2Pos && row == 3) {
								stickmanArray[row][column] = '/';
							}
							// left eyebrow
							else if (column == 3 + player2Pos && row == 3) {
								stickmanArray[row][column] = '\\';
							}
							// eyes
							else if (column == 3 + player2Pos && row == 4 || column == 7 + player2Pos && row == 4) {
								stickmanArray[row][column] = '*';
							}
							// mouth
							else if (column >= 4 + player2Pos && row == 7 && column <= 6 + player2Pos) {
								stickmanArray[row][column] = '#';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player2Pos && row == 13 || column == 3 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player2Pos && row == 13 || column == 7 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player2Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player2Pos && row == 19 || column == 2 + player2Pos && row == 20
									|| column == 1 + player2Pos && row == 21 || column == 0 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player2Pos && row == 19 || column == 8 + player2Pos && row == 20
									|| column == 9 + player2Pos && row == 21
									|| column == 10 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

					}
				}

				// white space
				else {
					stickmanArray[row][column] = ' ';
				}
				System.out.print(stickmanArray[row][column]);
			}
			System.out.println();

		}

	}

	/**
	 * This draws the whole section of the output screen where the players are when
	 * both players are attacking. Also able to draw just the hit boxes of the players 
	 * when hit box flag is true.
	 * 
	 * @param player1Pos    the position of player one
	 * @param player2Pos    the position of player two
	 * @param player1PosEnd the position of player one at its total width
	 * @param player2PosEnd the position of player two at its total width
	 */
	private void drawAttackingVsAttacking(int player1Pos, int player2Pos, int player1PosEnd,
			int player2PosEnd) {

		char[][] stickmanArray = new char[HEIGHT_MEN][WIDTH];

		for (int row = 0; row < HEIGHT_MEN; row++) {
			for (int column = 0; column < WIDTH; column++) {

				// side bars
				if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2)
					stickmanArray[row][column] = '%';

				// player one
				else if (column >= player1Pos && column < player1PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '#';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player1Pos - FACE_X) * (column - player1Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// right eyebrow
							else if (column == 7 + player1Pos && row == 3) {
								stickmanArray[row][column] = '/';
							}
							// left eyebrow
							else if (column == 3 + player1Pos && row == 3) {
								stickmanArray[row][column] = '\\';
							}
							// eyes
							else if (column == 3 + player1Pos && row == 4 || column == 7 + player1Pos && row == 4) {
								stickmanArray[row][column] = '*';
							}
							// mouth
							else if (column >= 4 + player1Pos && row == 7 && column <= 6 + player1Pos) {
								stickmanArray[row][column] = '#';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player1Pos && row == 13 || column == 3 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player1Pos && row == 13 || column == 7 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player1Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player1Pos && row == 19 || column == 2 + player1Pos && row == 20
									|| column == 1 + player1Pos && row == 21 || column == 0 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player1Pos && row == 19 || column == 8 + player1Pos && row == 20
									|| column == 9 + player1Pos && row == 21
									|| column == 10 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

					}
				}

				// player two
				else if (column >= player2Pos && column < player2PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '@';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player2Pos - FACE_X) * (column - player2Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// right eyebrow
							else if (column == 7 + player2Pos && row == 3) {
								stickmanArray[row][column] = '/';
							}
							// left eyebrow
							else if (column == 3 + player2Pos && row == 3) {
								stickmanArray[row][column] = '\\';
							}
							// eyes
							else if (column == 3 + player2Pos && row == 4 || column == 7 + player2Pos && row == 4) {
								stickmanArray[row][column] = '*';
							}
							// mouth
							else if (column >= 4 + player2Pos && row == 7 && column <= 6 + player2Pos) {
								stickmanArray[row][column] = '#';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player2Pos && row == 13 || column == 3 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player2Pos && row == 13 || column == 7 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player2Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player2Pos && row == 19 || column == 2 + player2Pos && row == 20
									|| column == 1 + player2Pos && row == 21 || column == 0 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player2Pos && row == 19 || column == 8 + player2Pos && row == 20
									|| column == 9 + player2Pos && row == 21
									|| column == 10 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

					}
				}

				// white space
				else {
					stickmanArray[row][column] = ' ';
				}
				System.out.print(stickmanArray[row][column]);
			}
			System.out.println();

		}

	}

	/**
	 * This draws the whole section of the output screen where the players are when
	 * player one is attacking player two out of range. Also able to draw just the
	 * hit boxes of the players when hit box flag is true.
	 * 
	 * @param player1Pos    the position of player one
	 * @param player2Pos    the position of player two
	 * @param player1PosEnd the position of player one at its total width
	 * @param player2PosEnd the position of player two at its total width
	 */
	private void drawAttackingVsStickman(int player1Pos, int player2Pos, int player1PosEnd,
			int player2PosEnd) {

		char[][] stickmanArray = new char[HEIGHT_MEN][WIDTH];

		for (int row = 0; row < HEIGHT_MEN; row++) {
			for (int column = 0; column < WIDTH; column++) {

				// side bars
				if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2)
					stickmanArray[row][column] = '%';

				// player one
				else if (column >= player1Pos && column < player1PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '#';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player1Pos - FACE_X) * (column - player1Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// right eyebrow
							else if (column == 7 + player1Pos && row == 3) {
								stickmanArray[row][column] = '/';
							}
							// left eyebrow
							else if (column == 3 + player1Pos && row == 3) {
								stickmanArray[row][column] = '\\';
							}
							// eyes
							else if (column == 3 + player1Pos && row == 4 || column == 7 + player1Pos && row == 4) {
								stickmanArray[row][column] = '*';
							}
							// mouth
							else if (column >= 4 + player1Pos && row == 7 && column <= 6 + player1Pos) {
								stickmanArray[row][column] = '#';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player1Pos && row == 13 || column == 3 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player1Pos && row == 13 || column == 7 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player1Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player1Pos && row == 19 || column == 2 + player1Pos && row == 20
									|| column == 1 + player1Pos && row == 21 || column == 0 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player1Pos && row == 19 || column == 8 + player1Pos && row == 20
									|| column == 9 + player1Pos && row == 21
									|| column == 10 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

					}
				}

				// player two
				else if (column >= player2Pos && column < player2PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '@';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player2Pos - FACE_X) * (column - player2Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// eyes
							else if (column == 3 + player2Pos && row == 3 || column == 7 + player2Pos && row == 3) {
								stickmanArray[row][column] = '*';
							}
							// next 2 elifs for mouth
							else if (column == 3 + player2Pos && row == 6 || column == 7 + player2Pos && row == 6) {
								stickmanArray[row][column] = '*';
							} else if (column == 4 + player2Pos && row == 7 || column == 6 + player2Pos && row == 7) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player2Pos && row == 13 || column == 3 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player2Pos && row == 13 || column == 7 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player2Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player2Pos && row == 19 || column == 2 + player2Pos && row == 20
									|| column == 1 + player2Pos && row == 21 || column == 0 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player2Pos && row == 19 || column == 8 + player2Pos && row == 20
									|| column == 9 + player2Pos && row == 21
									|| column == 10 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// white space
				else {
					stickmanArray[row][column] = ' ';
				}
				System.out.print(stickmanArray[row][column]);
			}
			System.out.println();

		}

	}

	/**
	 * This draws the whole section of the output screen where the players are when
	 * player two is attacking player one out of range. Also able to draw just the
	 * hit boxes of the players when hit box flag is true.
	 * 
	 * @param player1Pos    the position of player one
	 * @param player2Pos    the position of player two
	 * @param player1PosEnd the position of player one at its total width
	 * @param player2PosEnd the position of player two at its total width
	 */
	private void drawStickmanVsAttacking(int player1Pos, int player2Pos, int player1PosEnd,
			int player2PosEnd) {

		char[][] stickmanArray = new char[HEIGHT_MEN][WIDTH];

		for (int row = 0; row < HEIGHT_MEN; row++) {
			for (int column = 0; column < WIDTH; column++) {

				// side bars
				if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2)
					stickmanArray[row][column] = '%';

				// player one
				else if (column >= player1Pos && column < player1PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '#';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player1Pos - FACE_X) * (column - player1Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// eyes
							else if (column == 3 + player1Pos && row == 3 || column == 7 + player1Pos && row == 3) {
								stickmanArray[row][column] = '*';
							}
							// next 2 elifs for mouth
							else if (column == 3 + player1Pos && row == 6 || column == 7 + player1Pos && row == 6) {
								stickmanArray[row][column] = '*';
							} else if (column == 4 + player1Pos && row == 7 || column == 6 + player1Pos && row == 7) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player1Pos && row == 13 || column == 3 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player1Pos && row == 13 || column == 7 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player1Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player1Pos && row == 19 || column == 2 + player1Pos && row == 20
									|| column == 1 + player1Pos && row == 21 || column == 0 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player1Pos && row == 19 || column == 8 + player1Pos && row == 20
									|| column == 9 + player1Pos && row == 21
									|| column == 10 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// player two
				else if (column >= player2Pos && column < player2PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '@';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player2Pos - FACE_X) * (column - player2Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// right eyebrow
							else if (column == 7 + player2Pos && row == 3) {
								stickmanArray[row][column] = '/';
							}
							// left eyebrow
							else if (column == 3 + player2Pos && row == 3) {
								stickmanArray[row][column] = '\\';
							}
							// eyes
							else if (column == 3 + player2Pos && row == 4 || column == 7 + player2Pos && row == 4) {
								stickmanArray[row][column] = '*';
							}
							// mouth
							else if (column >= 4 + player2Pos && row == 7 && column <= 6 + player2Pos) {
								stickmanArray[row][column] = '#';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player2Pos && row == 13 || column == 3 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player2Pos && row == 13 || column == 7 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player2Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player2Pos && row == 19 || column == 2 + player2Pos && row == 20
									|| column == 1 + player2Pos && row == 21 || column == 0 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player2Pos && row == 19 || column == 8 + player2Pos && row == 20
									|| column == 9 + player2Pos && row == 21
									|| column == 10 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

					}
				}

				// white space
				else {
					stickmanArray[row][column] = ' ';
				}
				System.out.print(stickmanArray[row][column]);
			}
			System.out.println();

		}

	}

	/**
	 * This draws the whole section of the output screen where the players are when
	 * player two is blocking and player one is not attacking. Also able to draw
	 * just the hit boxes of the players when hit box flag is true.
	 * 
	 * @param player1Pos    the position of player one
	 * @param player2Pos    the position of player two
	 * @param player1PosEnd the position of player one at its total width
	 * @param player2PosEnd the position of player two at its total width
	 */
	private void drawStickmanVsBlocking(int player1Pos, int player2Pos, int player1PosEnd,
			int player2PosEnd) {

		char[][] stickmanArray = new char[HEIGHT_MEN][WIDTH];

		for (int row = 0; row < HEIGHT_MEN; row++) {
			for (int column = 0; column < WIDTH; column++) {

				// side bars
				if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2)
					stickmanArray[row][column] = '%';

				// player one
				else if (column >= player1Pos && column < player1PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '#';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player1Pos - FACE_X) * (column - player1Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// eyes
							else if (column == 3 + player1Pos && row == 3 || column == 7 + player1Pos && row == 3) {
								stickmanArray[row][column] = '*';
							}
							// next 2 elifs for mouth
							else if (column == 3 + player1Pos && row == 6 || column == 7 + player1Pos && row == 6) {
								stickmanArray[row][column] = '*';
							} else if (column == 4 + player1Pos && row == 7 || column == 6 + player1Pos && row == 7) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player1Pos && row == 13 || column == 3 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player1Pos && row == 13 || column == 7 + player1Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player1Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player1Pos && row == 19 || column == 2 + player1Pos && row == 20
									|| column == 1 + player1Pos && row == 21 || column == 0 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player1Pos && row == 19 || column == 8 + player1Pos && row == 20
									|| column == 9 + player1Pos && row == 21
									|| column == 10 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// player two
				else if (column >= player2Pos && column < player2PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '@';
					}

					else {

						// face
						if (row <= FACE_LENGTH2) {
							int xSquared = (column - player2Pos - FACE_X) * (column - player2Pos - FACE_X);
							int ySquared = (row - 4 - FACE_Y) * (row - 4 - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// right eyebrow
							else if (column == 7 + player2Pos && row == 7) {
								stickmanArray[row][column] = '\\';
							}
							// left eyebrow
							else if (column == 3 + player2Pos && row == 7) {
								stickmanArray[row][column] = '/';
							}
							// eyes
							else if (column == 3 + player2Pos && row == 8 || column == 7 + player2Pos && row == 8) {
								stickmanArray[row][column] = '@';
							}
							// mouth
							else if (column >= 4 + player2Pos && row == 11 && column <= 6 + player2Pos) {
								stickmanArray[row][column] = '=';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH2) {

							// left arm
							if (column == 1 + player2Pos && row == 16 || column == 3 + player2Pos && row == 17) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player2Pos && row == 16 || column == 7 + player2Pos && row == 17) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player2Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player2Pos && row == 20 || column == 2 + player2Pos && row == 21
									|| column == 1 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player2Pos && row == 20 || column == 8 + player2Pos && row == 21
									|| column == 9 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// white space
				else {
					stickmanArray[row][column] = ' ';
				}
				System.out.print(stickmanArray[row][column]);
			}
			System.out.println();

		}

	}

	/**
	 * This draws the whole section of the output screen where the players are when
	 * player one is blocking and player two is not attacking. Also able to draw
	 * just the hit boxes of the players when hit box flag is true.
	 * 
	 * @param player1Pos    the position of player one
	 * @param player2Pos    the position of player two
	 * @param player1PosEnd the position of player one at its total width
	 * @param player2PosEnd the position of player two at its total width
	 */
	private void drawBlockingVsStickman(int player1Pos, int player2Pos, int player1PosEnd,
			int player2PosEnd) {

		char[][] stickmanArray = new char[HEIGHT_MEN][WIDTH];

		for (int row = 0; row < HEIGHT_MEN; row++) {
			for (int column = 0; column < WIDTH; column++) {

				// side bars
				if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2)
					stickmanArray[row][column] = '%';

				// player one
				else if (column >= player1Pos && column < player1PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '#';
					}

					else {

						// face
						if (row <= FACE_LENGTH2) {
							int xSquared = (column - player1Pos - FACE_X) * (column - player1Pos - FACE_X);
							int ySquared = (row - 4 - FACE_Y) * (row - 4 - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// right eyebrow
							else if (column == 7 + player1Pos && row == 7) {
								stickmanArray[row][column] = '\\';
							}
							// left eyebrow
							else if (column == 3 + player1Pos && row == 7) {
								stickmanArray[row][column] = '/';
							}
							// eyes
							else if (column == 3 + player1Pos && row == 8 || column == 7 + player1Pos && row == 8) {
								stickmanArray[row][column] = '@';
							}
							// mouth
							else if (column >= 4 + player1Pos && row == 11 && column <= 6 + player1Pos) {
								stickmanArray[row][column] = '=';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH2) {

							// left arm
							if (column == 1 + player1Pos && row == 16 || column == 3 + player1Pos && row == 17) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player1Pos && row == 16 || column == 7 + player1Pos && row == 17) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player1Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player1Pos && row == 20 || column == 2 + player1Pos && row == 21
									|| column == 1 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player1Pos && row == 20 || column == 8 + player1Pos && row == 21
									|| column == 9 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// player two
				else if (column >= player2Pos && column < player2PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '@';
					}

					else {

						// face
						if (row <= FACE_LENGTH) {
							int xSquared = (column - player2Pos - FACE_X) * (column - player2Pos - FACE_X);
							int ySquared = (row - FACE_Y) * (row - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// eyes
							else if (column == 3 + player2Pos && row == 3 || column == 7 + player2Pos && row == 3) {
								stickmanArray[row][column] = '*';
							}
							// next 2 elifs for mouth
							else if (column == 3 + player2Pos && row == 6 || column == 7 + player2Pos && row == 6) {
								stickmanArray[row][column] = '*';
							} else if (column == 4 + player2Pos && row == 7 || column == 6 + player2Pos && row == 7) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH) {

							// left arm
							if (column == 1 + player2Pos && row == 13 || column == 3 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player2Pos && row == 13 || column == 7 + player2Pos && row == 14) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player2Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player2Pos && row == 19 || column == 2 + player2Pos && row == 20
									|| column == 1 + player2Pos && row == 21 || column == 0 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player2Pos && row == 19 || column == 8 + player2Pos && row == 20
									|| column == 9 + player2Pos && row == 21
									|| column == 10 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// white space
				else {
					stickmanArray[row][column] = ' ';
				}
				System.out.print(stickmanArray[row][column]);
			}
			System.out.println();

		}

	}

	/**
	 * This draws the whole section of the output screen where the players are when
	 * both players are blocking. Also able to draw just the hit boxes of the players
	 * when hit box flag is true.
	 * 
	 * @param player1Pos    the position of player one
	 * @param player2Pos    the position of player two
	 * @param player1PosEnd the position of player one at its total width
	 * @param player2PosEnd the position of player two at its total width
	 */
	private void drawBlockingVsBlocking(int player1Pos, int player2Pos, int player1PosEnd,
			int player2PosEnd) {

		char[][] stickmanArray = new char[HEIGHT_MEN][WIDTH];

		for (int row = 0; row < HEIGHT_MEN; row++) {
			for (int column = 0; column < WIDTH; column++) {

				// side bars
				if (column == 0 || column == 1 || column == WIDTH - 1 || column == WIDTH - 2)
					stickmanArray[row][column] = '%';

				// player one
				else if (column >= player1Pos && column < player1PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '#';
					}

					else {

						// face
						if (row <= FACE_LENGTH2) {
							int xSquared = (column - player1Pos - FACE_X) * (column - player1Pos - FACE_X);
							int ySquared = (row - 4 - FACE_Y) * (row - 4 - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// right eyebrow
							else if (column == 7 + player1Pos && row == 7) {
								stickmanArray[row][column] = '\\';
							}
							// left eyebrow
							else if (column == 3 + player1Pos && row == 7) {
								stickmanArray[row][column] = '/';
							}
							// eyes
							else if (column == 3 + player1Pos && row == 8 || column == 7 + player1Pos && row == 8) {
								stickmanArray[row][column] = '@';
							}
							// mouth
							else if (column >= 4 + player1Pos && row == 11 && column <= 6 + player1Pos) {
								stickmanArray[row][column] = '=';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH2) {

							// left arm
							if (column == 1 + player1Pos && row == 16 || column == 3 + player1Pos && row == 17) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player1Pos && row == 16 || column == 7 + player1Pos && row == 17) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player1Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player1Pos && row == 20 || column == 2 + player1Pos && row == 21
									|| column == 1 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player1Pos && row == 20 || column == 8 + player1Pos && row == 21
									|| column == 9 + player1Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}

				// player two
				else if (column >= player2Pos && column <= player2PosEnd) {

					if (HIT_BOX) {
						stickmanArray[row][column] = '@';
					}

					else {

						// face
						if (row <= FACE_LENGTH2) {
							int xSquared = (column - player2Pos - FACE_X) * (column - player2Pos - FACE_X);
							int ySquared = (row - 4 - FACE_Y) * (row - 4 - FACE_Y);
							int radiusSquared = FACE_RADIUS * FACE_RADIUS;

							// head
							if (Math.abs((xSquared + ySquared) - radiusSquared) < FACE_RADIUS) {
								stickmanArray[row][column] = '*';
							}
							// right eyebrow
							else if (column == 7 + player2Pos && row == 7) {
								stickmanArray[row][column] = '\\';
							}
							// left eyebrow
							else if (column == 3 + player2Pos && row == 7) {
								stickmanArray[row][column] = '/';
							}
							// eyes
							else if (column == 3 + player2Pos && row == 8 || column == 7 + player2Pos && row == 8) {
								stickmanArray[row][column] = '@';
							}
							// mouth
							else if (column >= 4 + player2Pos && row == 11 && column <= 6 + player2Pos) {
								stickmanArray[row][column] = '=';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// body
						else if (row < BODY_LENGTH2) {

							// left arm
							if (column == 1 + player2Pos && row == 16 || column == 3 + player2Pos && row == 17) {
								stickmanArray[row][column] = '*';
							}
							// right arm
							else if (column == 9 + player2Pos && row == 16 || column == 7 + player2Pos && row == 17) {
								stickmanArray[row][column] = '*';
							}
							// middle line
							else if (column == 5 + player2Pos) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}

						// legs
						else if (row < LEG_LENGTH) {

							// left leg
							if (column == 3 + player2Pos && row == 20 || column == 2 + player2Pos && row == 21
									|| column == 1 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// right leg
							else if (column == 7 + player2Pos && row == 20 || column == 8 + player2Pos && row == 21
									|| column == 9 + player2Pos && row == 22) {
								stickmanArray[row][column] = '*';
							}
							// white space
							else {
								stickmanArray[row][column] = ' ';
							}
						}
					}
				}
				// white space
				else {
					stickmanArray[row][column] = ' ';
				}
				System.out.print(stickmanArray[row][column]);
			}
			System.out.println();

		}

	}

	@Override
	/**
	 * 
	 */
	public void draw(Game game) {
		// ** don't know if i'm supposed to put this here but this is how i'm calling it
		// in main

		char[][] screen = new char[HEIGHT_TOP][WIDTH];

		double scale = WIDTH / Game.SCREEN_SIZE.x;

		// getPlayer1Pos() can be between 3 and 100
		// getPlayer2Pos() can be between 16 and 113 ** always be 13 away
		int player1Pos = (int) (game.getPosition(Team.LEFT).x * scale);
		int player2Pos = (int) (game.getPosition(Team.RIGHT).x * scale);

		int player1PosEnd = (int) (game.getPosition(Team.LEFT).x * scale + MAN_WIDTH); // end of player one's position
		int player2PosEnd = (int) (game.getPosition(Team.RIGHT).x * scale + MAN_WIDTH); // end of player two's position

		// this.drawSideBars();
		if (game.isGameOver()) {
			// end screen player one wins
			this.drawBackground(screen, game.getHealth(Team.LEFT), game.getHealth(Team.RIGHT),
					game.getMaxHealth(Team.LEFT), game.getMaxHealth(Team.RIGHT));
			if (game.winner().equals(Team.LEFT)) {
				this.drawPlayerWins(Team.LEFT);
				this.drawStickmanVsDead(player1Pos, player2Pos, player1PosEnd, player2PosEnd);
			}
			// end screen player two wins
			else if (game.winner().equals(Team.RIGHT)) {
				this.drawPlayerWins(Team.RIGHT);
				this.drawDeadVsStickman(player1Pos, player2Pos, player1PosEnd, player2PosEnd);
			}
		} else {
			this.drawBackground(screen, game.getHealth(Team.LEFT), game.getHealth(Team.RIGHT),
					game.getMaxHealth(Team.LEFT), game.getMaxHealth(Team.RIGHT));

			this.drawSideBars();

			this.drawPlayerNames(player1Pos, player2Pos, player1PosEnd, player2PosEnd);

			// if both players are attacking
			if (game.isAttacking(Team.LEFT) && game.isAttacking(Team.RIGHT)) {
				this.drawAttackingVsAttacking(player1Pos, player2Pos, player1PosEnd, player2PosEnd);
			}

			// if player one is attacking and player two is blocking
			else if (game.isAttacking(Team.LEFT) && game.isBlocking(Team.RIGHT)) {
				drawAttackingVsBlocking(player1Pos, player2Pos, player1PosEnd, player2PosEnd);
			}

			// if player one is blocking and player two is attacking
			else if (game.isBlocking(Team.LEFT) && game.isAttacking(Team.RIGHT)) {
				this.drawBlockingVsAttacking(player1Pos, player2Pos, player1PosEnd, player2PosEnd);
			}

			// if player one is attacking player two
			else if (game.isAttacking(Team.LEFT)
					&& (((player1PosEnd - player2Pos) >= -5) && ((player1PosEnd - player2Pos) <= 0))) {
				this.drawAttackingVsAttacked(player1Pos, player2Pos, player1PosEnd, player2PosEnd);
			}

			// if player two is attacking player one
			else if (game.isAttacking(Team.RIGHT)
					&& (((player2Pos - player1PosEnd) <= 5) && ((player2Pos - player1PosEnd) >= 0))) {
				this.drawAttackedVsAttacking(player1Pos, player2Pos, player1PosEnd, player2PosEnd);
			}

			// if player one attacks out of range
			else if (game.isAttacking(Team.LEFT)) {
				this.drawAttackingVsStickman(player1Pos, player2Pos, player1PosEnd, player2PosEnd);
			}

			// if player two attacks out of range
			else if (game.isAttacking(Team.RIGHT)) {
				this.drawStickmanVsAttacking(player1Pos, player2Pos, player1PosEnd, player2PosEnd);
				// out of range
			}

			// both players blocking
			else if (game.isBlocking(Team.LEFT) && game.isBlocking(Team.RIGHT)) {
				this.drawBlockingVsBlocking(player1Pos, player2Pos, player1PosEnd, player2PosEnd);
			}

			// player one blocks but player two not attacking
			else if (game.isBlocking(Team.LEFT)) {
				this.drawBlockingVsStickman(player1Pos, player2Pos, player1PosEnd, player2PosEnd);
			}

			// player two block put player one not attacking
			else if (game.isBlocking(Team.RIGHT)) {
				this.drawStickmanVsBlocking(player1Pos, player2Pos, player1PosEnd, player2PosEnd);
			}
			// left = p1 3, p2 16; right = p1 100, p2 113; ** always be 13 away
			else {
				this.drawStickmanVsStickman(player1Pos, player2Pos, player1PosEnd, player2PosEnd);
			}
		}

		this.drawBottomBars();

	}

	@Test
	/**
	 * This tests drawBackground method which is drawing the first seven rows of the
	 * output screen.
	 */
	public void testBackground() {
		char mty = '\u25FD';
		char ful = '\u25FE';

		char[][] expected = {
				{ '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%',
						'%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%',
						'%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%',
						'%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%',
						'%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%',
						'%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%',
						'%', '%', '%', '%', '%', '%', '%', '%', '%', '%' },
				{ '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%',
						'%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%',
						'%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%',
						'%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%',
						'%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%',
						'%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%', '%',
						'%', '%', '%', '%', '%', '%', '%', '%', '%', '%' },
				{ '%', '%', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '%', '%' },
				{ '%', '%', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '%', '%' },
				{ '%', '%', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '%', '%' },
				{ '%', '%', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'P', 'l', 'a', 'y', 'e', 'r', ' ', 'O',
						'n', 'e', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', 'P', 'l', 'a', 'y', 'e', 'r', ' ', 'T', 'w', 'o', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '%', '%' },
				{ '%', '%', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', mty, mty, mty, mty, mty, mty, mty, mty,
						ful, ful, ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', '|', ' ', mty, mty, mty, mty, mty, ful, ful, ful, ful, ful, ' ', '|',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '%', '%' } };

		int height = 7;
		int width = 125;

		char[][] screen = new char[height][width];

		ASCII theInterface = new ASCII();

		theInterface.drawBackground(screen, 2, 5, 10, 10);

		TestTools.assertDoubleArrayEquals(expected, screen);
	}

	@Test
	public void testFullHealthNoScale() {
		ASCII theInterface = new ASCII();
		assertEquals(10, theInterface.healthToInt(10.0, 10.0));
	}

	@Test
	public void testFullHealthScale() {
		ASCII theInterface = new ASCII();
		assertEquals(10, theInterface.healthToInt(200.0, 200.0));
	}

	@Test
	public void testOverFullHealth() {
		ASCII theInterface = new ASCII();
		assertEquals(10, theInterface.healthToInt(20.0, 10.0));
	}

	@Test
	public void testNoHealth() {
		ASCII theInterface = new ASCII();
		assertEquals(0, theInterface.healthToInt(0.0, 10.0));
	}

}
