package game_interface;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import the_game.Game;
import the_game.Game.Team;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;

import javafx.scene.effect.*;

import game_interface.GameInterface;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ProgressBar;

/**
 * Uses java fx to create graphics to display the Game object using many Images
 * contained withing the sprites and words folder in order to convey the user to
 * initialize the Game with the corresponding settings. Also uses plays sounds
 * corresponding to the different actions performed by the players using media
 * players.
 *
 */
public class GUI implements GameInterface {

	/**
	 * This acts like a time interval for each turn
	 */
	public static final double TIME_INTERVAL = 1000 / 30d; // This acts more like a time limit for each turn.
	/**
	 * This makes it so when you run each step it's this long.
	 */
	public static final double GAME_TIME = TIME_INTERVAL; // run each step as being this long.

	private Team whichAI = null;

	/**
	 * This is the screen that is being drawn at a given time.
	 */
	public static enum Window {
		/**
		 * The main menu
		 */
		START,
		/**
		 * The controls screen
		 */
		CONTROLS,
		/**
		 * The background selection screen
		 */
		BACKGROUND, 
		/**
		 * The character selction screen for player one
		 */
		CHARACTER1, 
		/**
		 * The character selection screen for player two
		 */
		CHARACTER2, 
		/**
		 * The screen that asks if player one is human or an AI
		 */
		HUMAN_AI1, 
		/**
		 * The screen that asks if player two is human or an AI
		 */
		HUMAN_AI2, 
		/**
		 * The fight screen where the game is played
		 */
		FIGHT, 
		/**
		 * The screen that pops up when an AI has been selected for
		 * both player one and player two. This is not allowed.
		 */
		ERROR
	}

	/**
	 * This alows the sounds to be muted.
	 */
	public static final boolean MUTE = false;

	private Window screen;

	private Game aGame;

	private ImageView player1 = new ImageView(), player2 = new ImageView();
	private Image kermit = ModelsAndViewsController.getPlayerIcon("Kermit");
	private Image bubs = ModelsAndViewsController.getPlayerIcon("Bub");

	// theme song main screen
	// https://freesound.org/people/Mrthenoronha/sounds/369920/
	private String theme = "src/sounds/cartoon-game-theme-loop.wav";
	private Media soundTheme = new Media(new File(theme).toURI().toString());
	private MediaPlayer themeSong = new MediaPlayer(soundTheme);

	// for onGameStart
	private MediaPlayer fightCountdown;

	private ImageView three = new ImageView();
	private ImageView two = new ImageView();
	private ImageView one = new ImageView();
	private ImageView fightWord = new ImageView();

	// fight screen song
	// https://freesound.org/people/Vicces1212/sounds/86758/ -- old video game
	private String loop = "src/sounds/oldvideogame.wav";
	private Media backingLoop = new Media(new File(loop).toURI().toString());
	private MediaPlayer backingTrack = new MediaPlayer(backingLoop);

	// for onGameEnd
	private MediaPlayer endCheer;

	// for selection sounds
	private MediaPlayer mediaSelect;
	private MediaPlayer mediaMenu;
	private MediaPlayer backSound;

	private MediaPlayer notSelected;

	private ImageView background = new ImageView();
	private ImageView fightScreen = new ImageView();
	private Image forest = new Image("backgrounds/RedWoods.jpg");
	private Image sunset = new Image("backgrounds/Sunset on the Bay.jpg");
	private Image palmTree = new Image("backgrounds/123 Digital False Color 6.1.jpg");
	private Image purpleAcid = new Image("backgrounds/5 5321 Tartaric Acid.jpg");
	private Image spacey = new Image("backgrounds/Bellagio Lights.jpg");
	private Image sanFran = new Image("backgrounds/San Francisco.jpg");
	private Image bay = new Image("backgrounds/The Bay.jpg");
	private Image yosemite = new Image("backgrounds/Yosemite.jpg");

	private ImageView ok = new ImageView();
	private ImageView ok1 = new ImageView();
	private Image oK = new Image("words/ok.png"); // 107 x 81 pixels

	private ImageView back = new ImageView();
	private Image baCk = new Image("words/back.png"); // 186 x 82 pixels
	private ImageView back1 = new ImageView();
	private ImageView back2 = new ImageView();

	// background screen
	private ImageView sPacEy = new ImageView();
	private ImageView forEst = new ImageView();
	private ImageView sUnSet = new ImageView();
	private ImageView pAlm = new ImageView();
	private ImageView pUrp = new ImageView();
	private ImageView sanFrAn = new ImageView();
	private ImageView bAy = new ImageView();
	private ImageView yoSeMiTe = new ImageView();

	// start screen
	private ImageView debug = new ImageView();
	private ImageView startGame = new ImageView();

	// character selection screen
	private ImageView char1 = new ImageView();
	private ImageView char2 = new ImageView();
	private ImageView chosen1 = new ImageView();
	private ImageView chosen2 = new ImageView();

	private ImageView play1 = new ImageView();
	private Image plAy1 = new Image("words/player one.png"); // 335 x 75 pixels
	private ImageView play2 = new ImageView();
	private Image plAy2 = new Image("words/player two.png"); // 335 x 74 pixels

	// enter
	private ImageView enter = new ImageView();

	// human vs ai screen
	private ImageView ai = new ImageView();
	private ImageView human = new ImageView();
	private ImageView plaYer = new ImageView();
	private ImageView ok3 = new ImageView();
	private ImageView back3 = new ImageView();

	// for isTwoAIs
	private boolean first = false;
	private boolean second = false;

	// for selections of AIs
	private ArrayList<ImageView> choice1 = new ArrayList<ImageView>();
	private ArrayList<ImageView> choice2 = new ArrayList<ImageView>();

	// for selections of characters
	private ArrayList<ImageView> choiceChar1 = new ArrayList<ImageView>();
	private ArrayList<ImageView> choiceChar2 = new ArrayList<ImageView>();

	// for isCharSelected
	private boolean charChosen1 = false;
	private boolean charChosen2 = false;

	// health bars
	private ProgressBar healthP1 = new ProgressBar();
	private ProgressBar healthP2 = new ProgressBar();

	// for end screen
	private ImageView player = new ImageView();
	private ImageView wins = new ImageView();
	private ImageView exclaim1 = new ImageView();
	private ImageView exclaim2 = new ImageView();
	private ImageView exclaim3 = new ImageView();

	private Pane root; // Use this to place ImageViews and other components on the screen.


	/**
	 * * This constructor draws the main menu screen.
	 * 
	 * @param root the root Pane from main
	 * @param game the game
	 *
	 */
	public GUI(Pane root, Game game) {
		this.root = root;
		this.aGame = game;
		this.screen = Window.START;
		this.drawScreen(this.screen);
	}

	/**
	 * This is a getter for the screen that is being drawn.
	 * 
	 * @return root the first Pane we draw which is the main menu screen
	 * 
	 */
	public Pane getRoot() {
		return this.root;
	}

	/**
	 * 
	 */
	public double getRuntimeInterval() {
		return GUI.TIME_INTERVAL;
	}

	/**
	 * 
	 */
	public double getGametimeInterval() {
		return GUI.GAME_TIME;
	}

	@Override

	/**
	 * 
	 */
	public void draw(Game game) {

		if (game.getHealth(Team.LEFT) > 0 && game.getHealth(Team.RIGHT) > 0) {
			healthP1.setProgress(game.getHealth(Team.LEFT) / game.getMaxHealth(Team.LEFT));
			healthP2.setProgress(game.getHealth(Team.RIGHT) / game.getMaxHealth(Team.RIGHT));
		} else if (game.getHealth(Team.LEFT) <= 0) {
			healthP1.setProgress(0);
		} else if (game.getHealth(Team.RIGHT) <= 0) {
			healthP2.setProgress(0);
		}

		if (game.isGameOver()) {
			onGameOver(game.winner());
		}

		for (Team t : Team.values()) {
			ImageView playerView = t == Team.LEFT ? this.player1 : this.player2;
			int index = game.getAnimationIndex(t);
			synchronized (playerView) {
				if (index > -1) {
					game.getCharacter(t).getAllAnimations().setAnimation(playerView, index);
					if (game.getVelocity(t).x > 0) {
						playerView.setScaleX(1);
					} else if (game.getVelocity(t).x < 0) {
						playerView.setScaleX(-1);
					}
				}
				Point2D.Double p = game.getPosition(t);
				playerView.relocate(p.getX(), p.getY());
			}
		}

	}

	/**
	 * This getter is for the Pane we are currently drawing
	 * 
	 * @return the specific Pane we are drawing at the time. (main menu, controls,
	 *         or fight screens)
	 * 
	 */
	public Window getWindow() {
		return this.screen;
	}

	/**
	 * This getter is for the player ImageView based off of the team they are on.
	 * 
	 * @param t the team the player is on (Team.LEFT or Team.RIGHT)
	 * @return the ImageView of either player one or player two
	 *
	 */
	public ImageView getPlayerImage(Team t) {
		switch (t) {
		case LEFT:
			return this.player1;
		case RIGHT:
			return this.player2;
		default:
			return null;
		}
	}

	/**
	 * This puts the clickable ImageViews of current screen into an ArrayList so
	 * that we know what was selected.
	 * 
	 * @param screen the screen we are currently drawing
	 * @return the ArrayList of clickable ImageViews of that screen
	 */
	public ArrayList<ImageView> getEventImage(Window screen) {
		this.screen = screen;
		ArrayList<ImageView> list = new ArrayList<ImageView>();
		switch (this.screen) {
		case START:
			list.add(debug);
			list.add(startGame);
			return list;
		case BACKGROUND:
			list.add(sPacEy);
			list.add(forEst);
			list.add(sUnSet);
			list.add(pAlm);
			list.add(bAy);
			list.add(sanFrAn);
			list.add(pUrp);
			list.add(yoSeMiTe);
			list.add(back1);
			list.add(ok);
			return list;
		case CHARACTER1:
			list.add(char1);
			list.add(char2);
			list.add(ok1);
			list.add(back2);
			return list;
		case HUMAN_AI1:
			list.add(human);
			list.add(ai);
			list.add(ok3);
			list.add(back3);
		case CHARACTER2:
			list.add(char1);
			list.add(char2);
			list.add(ok1);
			list.add(back2);
		case HUMAN_AI2:
			list.add(human);
			list.add(ai);
			list.add(ok3);
			list.add(back3);
		case CONTROLS:
			list.add(enter);
			list.add(back);
			return list;
		case FIGHT:
			list.add(back);
		}
		return list;
	}

	/**
	 * This adds sounds to selected ImageViews to let the user know what they
	 * selected.
	 * 
	 * @param screen   the screen that is currently being drawn
	 * @param selected the selected item to get the sound
	 */
	public void soundSelected(Window screen, ImageView selected) {

		// select sound
		// https://freesound.org/people/MATTIX/sounds/404151/
		String selectGranted = "src/sounds/select granted.wav";
		Media soundSelect = new Media(new File(selectGranted).toURI().toString());
		mediaSelect = new MediaPlayer(soundSelect);

		// enter / ok sound
		// https://freesound.org/people/RunnerPack/sounds/87035/
		String menuSelect = "src/sounds/menu select.wav";
		Media soundMenu = new Media(new File(menuSelect).toURI().toString());
		mediaMenu = new MediaPlayer(soundMenu);

		// back sound
		// https://freesound.org/people/TheEvilSocks/sounds/193943/ -- back
		String selectBack = "src/sounds/back.wav";
		Media soundBack = new Media(new File(selectBack).toURI().toString());
		backSound = new MediaPlayer(soundBack);

		if (MUTE) {
			mediaSelect.setMute(true);
			mediaMenu.setMute(true);
			backSound.setMute(true);
		} else {
			mediaSelect.volumeProperty();

			mediaSelect.setVolume(0.07);
			mediaMenu.setVolume(0.3);
			backSound.setVolume(0.3);
		}

		ArrayList<ImageView> clickable = new ArrayList<ImageView>();
		if (!selected.equals(null)) {
			clickable = getEventImage(screen);
		}
		for (int i = 0; i < clickable.size(); i++) {

			if (screen.equals(Window.START)) {
				if (clickable.get(i).equals(selected)) {
					mediaMenu.play();
				}
			} else if (screen.equals(Window.BACKGROUND)) {
				if (clickable.get(i).equals(selected)) {
					if (clickable.get(i).equals(ok)) {
						mediaMenu.play();
					} else if (clickable.get(i).equals(back1)) {
						backSound.play();
					} else {
						mediaSelect.play();
					}
				}
			}

			else if (screen.equals(Window.CHARACTER1) || screen.equals(Window.CHARACTER2)) {

				if (clickable.get(i).equals(selected)) {
					if (clickable.get(i).equals(ok1)) {
						mediaMenu.play();
					} else if (clickable.get(i).equals(back2)) {
						backSound.play();
					} else {
						mediaSelect.play();
					}
				}
			} else if (screen.equals(Window.HUMAN_AI1) || screen.equals(Window.HUMAN_AI2)) {
				if (clickable.get(i).equals(selected)) {
					if (clickable.get(i).equals(ok3)) {
						mediaMenu.play();
					} else if (clickable.get(i).equals(back3)) {
						backSound.play();
					} else {
						mediaSelect.play();
					}
				}
			} else if (screen.equals(Window.CONTROLS)) {
				if (clickable.get(i).equals(selected)) {
					if (clickable.get(i).equals(enter)) {
						mediaMenu.play();
					} else {
						backSound.play();
					}
				}

			}

		}

	}

	/**
	 * This adds a drop shadow to selected images to show the user what they
	 * selected. It also keeps track of what is being selected specifically for
	 * screens HUMAN_AI1 and HUMAN_AI2, as well as CHARACTER1 and 
	 * CHARACTER2 screens.
	 * 
	 * @param screen   the screen that is currently being drawn
	 * @param selected the selected item to get the drop shadow
	 */
	public void showSelected(Window screen, ImageView selected) {

		DropShadow ds = new DropShadow(40, Color.MEDIUMSPRINGGREEN.brighter().brighter());
		DropShadow ds2 = new DropShadow(60, Color.MEDIUMPURPLE.brighter().brighter().brighter().brighter().brighter());

		ArrayList<ImageView> clickable = new ArrayList<ImageView>();

		if (!selected.equals(null)) {
			clickable = getEventImage(screen);
		}

		if (screen.equals(Window.HUMAN_AI1)) {
			choice1.add(0, selected);
			if (selected.equals(back3)) {
				choice1.clear();
				resetIsTwoAIs();
			} else if (selected.equals(ok3)) {
				isAISelected(Window.HUMAN_AI1, choice1);

			}
		} else if (screen.equals(Window.HUMAN_AI2)) {
			choice2.add(0, selected);
			if (selected.equals(back3)) {
				choice2.clear();
			} else if (selected.equals(ok3)) {
				isAISelected(Window.HUMAN_AI2, choice2);
			}
		} else if (screen.equals(Window.CHARACTER1)) {
			choiceChar1.add(0, selected);
			if (selected.equals(ok1)) {
				isCharSelected(Window.CHARACTER1, choiceChar1);
			}
		} else if (screen.equals(Window.CHARACTER2)) {
			choiceChar2.add(0, selected);
			if (selected.equals(ok1)) {
				isCharSelected(Window.CHARACTER2, choiceChar2);
			}
		}

		for (int i = 0; i < clickable.size(); i++) {

			if (screen.equals(Window.CHARACTER2) || screen.equals(Window.HUMAN_AI2)) {

				if (clickable.get(i).equals(selected) && !clickable.get(i).equals(back3)
						&& !clickable.get(i).equals(ok3) && !clickable.get(i).equals(ok1)) {
					selected.setEffect(ds2);
				} else {
					clickable.get(i).setEffect(null);
				}
			} else {

				if (clickable.get(i).equals(selected) && !clickable.get(i).equals(back3)
						&& !clickable.get(i).equals(ok3) && !clickable.get(i).equals(ok1)) {
					selected.setEffect(ds);
				} else {
					clickable.get(i).setEffect(null);

				}
			}

		}
	}

	/**
	 * This removes the drop shadows from the screen passed to it.
	 * 
	 * @param screen1 the screen that the drop shadows are to be removed from
	 */
	public void resetSelected(Window screen1) {
		ArrayList<ImageView> toReset = new ArrayList<ImageView>();
		toReset = getEventImage(screen1);
		for (int i = 0; i < toReset.size(); i++) {
			toReset.get(i).setEffect(null);
		}
	}

	/**
	 * This sets up the countdown to fight, plays the fade transitions that 
	 * correspond to the countdown to fight, and starts the background music.
	 */
	private void onGameStart() {

		// https://freesound.org/people/Nakamurasensei/sounds/472853/
		String fight = "src/sounds/countdown-to-fight.wav";
		Media soundSelect = new Media(new File(fight).toURI().toString());
		fightCountdown = new MediaPlayer(soundSelect);

		// fade transition for 3
		FadeTransition forThree = new FadeTransition(Duration.seconds(0.5), three);
		forThree.setFromValue(0);
		forThree.setToValue(1);
		forThree.setCycleCount(1);
		forThree.setAutoReverse(false);

		// fade transition for 3
		FadeTransition forThree2 = new FadeTransition(Duration.seconds(0.5), three);
		forThree2.setFromValue(1.0);
		forThree2.setToValue(0);
		forThree2.setCycleCount(1);
		forThree2.setAutoReverse(false);

		// fade transition for 2 in
		FadeTransition forTwo = new FadeTransition(Duration.seconds(0.5), two);
		forTwo.setFromValue(0);
		forTwo.setToValue(1);
		forTwo.setCycleCount(1);
		forTwo.setAutoReverse(false);

		// fade transition for 2 out
		FadeTransition forTwo2 = new FadeTransition(Duration.seconds(0.5), two);
		forTwo2.setFromValue(1);
		forTwo2.setToValue(0);
		forTwo2.setCycleCount(1);
		forTwo2.setAutoReverse(false);

		// fade transition for 1 in
		FadeTransition forOne = new FadeTransition(Duration.seconds(0.5), one);
		forOne.setFromValue(0);
		forOne.setToValue(1);
		forOne.setCycleCount(1);
		forOne.setAutoReverse(false);

		// fade transition for 1 out
		FadeTransition forOne2 = new FadeTransition(Duration.seconds(0.5), one);
		forOne2.setFromValue(1);
		forOne2.setToValue(0);
		forOne2.setCycleCount(1);
		forOne2.setAutoReverse(false);

		// fade transition fight in
		FadeTransition forFight = new FadeTransition(Duration.seconds(0.5), fightWord);
		forFight.setFromValue(0);
		forFight.setToValue(1);
		forFight.setCycleCount(1);
		forFight.setAutoReverse(false);

		// fade transition fight out
		FadeTransition forFight2 = new FadeTransition(Duration.seconds(0.5), fightWord);
		forFight2.setFromValue(1);
		forFight2.setToValue(0);
		forFight2.setCycleCount(1);
		forFight2.setAutoReverse(false);

		// sequential transition set up to play all in order
		SequentialTransition st = new SequentialTransition();
		st.getChildren().addAll(forThree, forThree2, forTwo, forTwo2, forOne, forOne2, forFight, forFight2);
		st.setCycleCount(1);

		themeSong.stop();
		themeSong.dispose();

		if (MUTE) {
			fightCountdown.setMute(true);
			backingTrack.setMute(true);
		} else {
			fightCountdown.setVolume(0.5);
			backingTrack.setVolume(0.05);
		}

		fightCountdown.setOnReady(new Runnable() {
			public void run() {
				fightCountdown.play();
				st.play();
			}
		});
		fightCountdown.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				fightCountdown.stop();
				backingTrack.setCycleCount(MediaPlayer.INDEFINITE);
				backingTrack.play();
			}
		});
	}

	/**
	 * This creates the end screen for once a player dies. This includes playing
	 * the cheer.
	 * 
	 * @param team the team of the winning player
	 */
	private void onGameOver(Team team) {

		// https://freesound.org/people/tim.kahn/sounds/337000/ -- cheering
		String cheer = "src/sounds/cheer-01.wav";
		Media cheerEnd = new Media(new File(cheer).toURI().toString());
		endCheer = new MediaPlayer(cheerEnd);

		Image bigP1 = new Image("words/player one big.png"); // 946 x 154 pixels
		Image bigP2 = new Image("words/player two big.png"); // 937 x 149 pixels
		Image wiNs = new Image("words/wins.png"); // 419 x 163 pixels
		Image exclaim = new Image("words/exclamation.png"); // 66 x 183 pixels

		backingTrack.stop();
		backingTrack.dispose();

		if (MUTE) {
			endCheer.setMute(true);
		} else {
			endCheer.setVolume(0.5);
		}
		endCheer.play();

		if (team.equals(Team.LEFT)) {
			player.setImage(bigP1);

		} else if (team.equals(Team.RIGHT)) {
			player.setImage(bigP2);
		}

		wins.setImage(wiNs);
		exclaim1.setImage(exclaim);
		exclaim2.setImage(exclaim);
		exclaim3.setImage(exclaim);
	}

	/**
	 * This uses the ImageView selected by the user to set the background of the
	 * fight screen.
	 * 
	 * @param fromModels the ImageView selected by the user
	 */
	public void setFightBackground(ImageView fromModels) {

		if (fromModels.equals(sPacEy)) {
			fightScreen.setImage(spacey);
		} else if (fromModels.equals(forEst)) {
			fightScreen.setImage(forest);
		} else if (fromModels.equals(sUnSet)) {
			fightScreen.setImage(sunset);
		} else if (fromModels.equals(pAlm) || fromModels.equals(debug)) {
			fightScreen.setImage(palmTree);
		} else if (fromModels.equals(bAy)) {
			fightScreen.setImage(bay);
		} else if (fromModels.equals(sanFrAn)) {
			fightScreen.setImage(sanFran);
		} else if (fromModels.equals(pUrp)) {
			fightScreen.setImage(purpleAcid);
		} else if (fromModels.equals(yoSeMiTe)) {
			fightScreen.setImage(yosemite);
		}
	}

	/**
	 * This uses the selected character to add to player one's rectangle and sets
	 * the image of player one to the selected image.
	 * 
	 * @param selected the character image selected by the user
	 */
	public void setPlayerOne(ImageView selected) {
		if (selected.equals(char1)) {
			chosen1.setImage(kermit);
		} else if (selected.equals(char2)) {
			chosen1.setImage(bubs);

		}
	}

	/**
	 * This uses the selected character to add to player two's rectangle and sets
	 * the image of player two to the selected image.
	 * 
	 * @param selected2 the character image selected by the user
	 */
	public void setPlayerTwo(ImageView selected2) {
		if (selected2.equals(char1)) {
			chosen2.setImage(kermit);
		} else if (selected2.equals(char2)) {
			chosen2.setImage(bubs);

		}
	}

	/**
	 * This resets the chosen characters to null after the error screen pops up.
	 * It resets the booleans for player one's chosen character and player
	 * two's chosen character. It also resets their respective ArrayLists.
	 */
	public void resetChosen() {
		chosen1.setImage(null);
		chosen2.setImage(null);

		charChosen1 = false;
		charChosen2 = false;

		choiceChar1.clear();
		choiceChar2.clear();
	}

	/**
	 * This plays a sound that is feedback for the user when no character
	 * has been selected.
	 */
	public void playUnselectedSound() {

		//https://freesound.org/people/MATTIX/sounds/459956/
		String noSelect = "src/sounds/select-denied-03.wav";
		Media noneSelected = new Media(new File(noSelect).toURI().toString());
		notSelected = new MediaPlayer(noneSelected);

		if(MUTE) {
			notSelected.setMute(true);
		} else {
			notSelected.setVolume(0.125);
		}

		notSelected.play();
	}

	/**
	 * This gets the player image chosen by the user.
	 * 
	 * @param t  the team the player is being chosen for
	 * @return  the image of the chosen player
	 */
	public Image getChosenIcon(Team t) {
		switch (t) {
		case LEFT:
			return this.chosen1.getImage();
		case RIGHT:
			return this.chosen2.getImage();
		default:
			return null;
		}
	}

	/**
	 * This checks to make sure the user has selected a character for player one 
	 * and player two. If so, it sets booleans respectively so that we know a 
	 * character selection has been made.
	 * 
	 * @param screen  the screen being drawn (Only concerned with CHARACTER1 and
	 * 					CHARACTER2)
	 * @param chosen  the ArrayList of ImageViews that the user selected on the 
	 * 					specific screen
	 */
	private void isCharSelected(Window screen, ArrayList<ImageView> chosen) {

		if (!(chosen.size() == 1)) {

			if (screen.equals(Window.CHARACTER1)) {
				if (chosen.get(1).equals(char1) || chosen.get(1).equals(char2)) {
					charChosen1 = true;
				}
			} else if (screen.equals(Window.CHARACTER2)) {
				if (chosen.get(1).equals(char1) || chosen.get(1).equals(char2)) {
					charChosen2 = true;
				}
			}
		}
	}

	/**
	 * This returns if player one has been assigned a character.
	 * 
	 * @return  if player one is assigned a character
	 */
	public boolean isCharChosen1() {	
		return charChosen1;	
	}

	/**
	 * This returns if player two has been assigned a character.
	 * 
	 * @return  if player two is assigned a character
	 */
	public boolean isCharChosen2() {
		return charChosen2;
	}

	/**
	 * This checks to see if the actual selection of player one or player two is an
	 * AI. If so, it sets booleans to true is they are in fact selected as AIs.
	 * 
	 * @param screen the screen being drawn (Only concerned with HUMAN_AI1 and
	 *               HUMAN_AI2)
	 * @param chosen the ArrayList of ImageViews that the user selected on the
	 *               specific screen
	 */
	private void isAISelected(Window screen, ArrayList<ImageView> chosen) {
		if (!(chosen.size() == 1)) {
			if (screen.equals(Window.HUMAN_AI1)) {
				if (chosen.get(1).equals(ai)) {
					first = true;
				}
			} else if (screen.equals(Window.HUMAN_AI2)) {
				if (chosen.get(1).equals(ai)) {
					second = true;
				}
			}
			isTwoAIs();
		}
	}

	/**
	 * This checks to see if an AI player will be used during the fight.
	 * 
	 * @return  if there is an AI player in the fight
	 */
	public boolean isAIin() {

		if (first || second) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This sets the variable whichAI to the left team or the right team
	 * depending on which side the AI was chosen for.
	 */
	private void setWhichAI() {
		if (first) {
			whichAI = Team.LEFT;
		} else if (second) {
			whichAI = Team.RIGHT;
		}
	}

	/**
	 * This gets which team the AI player is on.
	 * 
	 * @return  the team the AI player is on
	 */
	public Team whichAI() {

		setWhichAI();

		if (first || second) {
			return whichAI;
		} else {
			return null;
		}
	}

	/**
	 * This checks to see if both player one and player two have been selected as
	 * AIs.
	 * 
	 * @return if both player one and player two have been selected as AIs
	 */
	public boolean isTwoAIs() {

		boolean one = getFirst();
		boolean two = getSecond();

		if (one && two) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This resets the booleans for player one and player two being selected as an
	 * AI, either from using the back button or once the error screen has been
	 * drawn.
	 */
	public void resetIsTwoAIs() {
		first = false;
		second = false;
	}

	/**
	 * This a getter for the boolean that says if player one is an AI
	 * 
	 * @return first boolean that says if player one is selected as an AI
	 */
	private boolean getFirst() {
		return first;
	}

	/**
	 * This is a getter for the boolean that says if player two is an AI
	 * 
	 * @return second boolean that says if player two is selected as an AI
	 */
	private boolean getSecond() {
		return second;
	}

	/**
	 * This draws the pane we are currently drawing.
	 *
	 * @param screen lets us know which screen we want to draw based on the enum
	 *
	 */
	public void drawScreen(Window screen) {
		this.screen = screen;
		// clear all
		this.root.getChildren().clear();

		switch (this.screen) {
		case START:
			drawMain();
			break;
		case BACKGROUND:
			drawBackground();
			break;
		case CHARACTER1:
			drawCharacter(1);
			break;
		case CHARACTER2:
			drawCharacter(2);
			break;
		case HUMAN_AI1:
			drawHuman_AI(1);
			break;
		case HUMAN_AI2:
			drawHuman_AI(2);
			break;
		case CONTROLS:
			drawControls();
			break;
		case ERROR:
			drawError();
			break;
		case FIGHT:
			drawFight();
			break;
		}
	}

	/**
	 * Draws the start screen that displays the game name and allows the user to
	 * select if they want to "start game" or "debug". Start game allows for
	 * choosing characters, backgrounds, gives controls, the whole selection and
	 * intro process. Debug sends them directly to the fight screen.
	 */
	private void drawMain() {

		Image colorAcid = new Image("backgrounds/3 5372 Salicylic Acid C7H6O3.jpg");

		ImageView title1 = new ImageView();
		Image firstTitle = new Image("words/title1 outline.png"); // 867 x 99 pixels
		ImageView title2 = new ImageView();
		Image secondTitle = new Image("words/title2 outline.png"); // 347 x 93 pixels

		Image deBug = new Image("words/debug.png"); // 251 x 111 pixels
		Image start = new Image("words/start game.png"); // 405 x 100 pixels

		int titleHeight = 100;
		int titleWidth1 = 900;
		int titleWidth2 = 350;
		int titleStart = 10;
		int titleStartX1 = -10;
		int titleStartX2 = 880;

		int wordWidth = 405;
		int wordWidth1 = 250;
		int wordHeight = 100;
		int wordStart = 423;
		int wordStart2 = 500;
		int row1 = 310;
		int row2 = 430;

		// background
		background.setImage(colorAcid);
		background.setFitWidth(Game.SCREEN_SIZE.x);
		background.setFitHeight(Game.SCREEN_SIZE.y);

		// game name
		title1.setImage(firstTitle);
		Pane.positionInArea(title1, titleStartX1, titleStart, titleWidth1, titleHeight, 0, null, HPos.RIGHT,
				VPos.BOTTOM, true);
		title2.setImage(secondTitle);
		title2.setFitHeight(titleHeight);
		Pane.positionInArea(title2, titleStartX2, titleStart, titleWidth2, titleHeight, 0, null, HPos.LEFT, VPos.BOTTOM,
				true);

		// select debug - as a fast track to fight screen
		debug.setImage(deBug);
		debug.setPreserveRatio(true);
		debug.setFitHeight(wordHeight);
		Pane.positionInArea(debug, wordStart, row1, wordWidth, wordHeight, 0, null, HPos.CENTER, VPos.CENTER, true);

		// select start the game
		startGame.setImage(start);
		startGame.setPreserveRatio(true);
		startGame.setFitHeight(wordHeight);
		Pane.positionInArea(startGame, wordStart2, row2, wordWidth1, wordHeight, 0, null, HPos.CENTER, VPos.CENTER,
				true);

		// play theme song
		if (MUTE) {
			themeSong.setMute(true);
		} else {
			themeSong.setVolume(0.05);
		}

		themeSong.setCycleCount(MediaPlayer.INDEFINITE);
		themeSong.play();

		root.getChildren().addAll(background, title1, title2, debug, startGame);

	}

	/**
	 * Draws the controls screen which lets the users know what the controls are to
	 * play the game.
	 */
	private void drawControls() {

		// top sentence of controls screen
		ImageView intro1 = new ImageView();
		Image inTro1 = new Image("words/offset intro line 1 1.png"); // 586 x 91 pixels
		ImageView intro1a = new ImageView();
		Image inTro1a = new Image("words/offset intro line 1 2.png"); // 597 x 90 pixels

		// player one controls
		ImageView intro2 = new ImageView();
		Image inTro2 = new Image("words/intro line 2.png"); // 622 x 75 pixels - player one controls
		ImageView intro3 = new ImageView();
		Image inTro3 = new Image("words/intro line 3.png"); // 401 x 75 pixels - a - left
		ImageView intro4 = new ImageView();
		Image inTro4 = new Image("words/intro line 4.png"); // 432 x 76 pixels - d - right
		ImageView intro5 = new ImageView();
		Image inTro5 = new Image("words/intro line 5.png"); // 283 x 76 pixels - s - block
		ImageView intro6 = new ImageView();
		Image inTro6 = new Image("words/intro line 6.png"); // 311 x 73 pixels - z - attack
		ImageView jump1 = new ImageView();
		Image juMp1 = new Image("words/player 1 jump.png"); // 285 x 79 pixels - w - jump

		// player two controls
		ImageView intro7 = new ImageView();
		Image inTro7 = new Image("words/intro line 7.png"); // 615 x 74 pixels - player two controls
		ImageView intro8 = new ImageView();
		Image inTro8 = new Image("words/intro line 8.png"); // 383 x 74 pixels - j - left
		ImageView intro9 = new ImageView();
		Image inTro9 = new Image("words/intro line 9.png"); // 413 x 75 pixels - l - right
		ImageView intro10 = new ImageView();
		Image inTro10 = new Image("words/intro line 10.png"); // 278 x 75 pixels - k - block
		ImageView intro11 = new ImageView();
		Image inTro11 = new Image("words/intro line 11.png"); // 326 x 75 pixels - m - attack
		ImageView jump2 = new ImageView();
		Image juMp2 = new Image("words/player 2 jump.png"); // 252 x 78 pixels - i - jump

		Image entEr = new Image("words/intro line 12.png"); // 186 x 73 pixels - enter

		int titleStart1 = 33;
		int titleStart2 = 618;
		int titleStartY = 10;
		int wordWidth1 = 600;
		int wordHeight1 = 95;
		int p1ControlsStart = 110;
		int p2ControlsStart = 735;
		int row1 = 210;
		int row2 = 280;
		int row3 = 350;
		int row4 = 420;
		int row5 = 500;
		int wordWidth = 450;
		int wordHeight = 80;
		int controlsHeight = 125;
		int controlsWidth = 625;
		int controls2Start = 625;
		int buttonsWidth = 200;
		int buttonsHeight = 600;
		int enterStart = 800;
		int backStart = 165;

		// background set to black
		root.setStyle("-fx-background-color: black;");

		// the top sentence of the screen
		intro1.setImage(inTro1);
		Pane.positionInArea(intro1, titleStart1, titleStartY, wordWidth1 - 10, wordHeight1, 0, null, HPos.RIGHT,
				VPos.BOTTOM, true);
		intro1a.setImage(inTro1a);
		Pane.positionInArea(intro1a, titleStart2, titleStartY, wordWidth1, wordHeight1, 0, null, HPos.LEFT, VPos.BOTTOM,
				true);

		// player one controls
		intro2.setImage(inTro2);
		Pane.positionInArea(intro2, 0, controlsHeight, controlsWidth, wordHeight, 0, null, HPos.CENTER, VPos.BOTTOM,
				true);
		intro3.setImage(inTro3);
		Pane.positionInArea(intro3, p1ControlsStart, row1, wordWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM,
				true);
		intro4.setImage(inTro4);
		Pane.positionInArea(intro4, p1ControlsStart, row2, wordWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM,
				true);
		intro5.setImage(inTro5);
		Pane.positionInArea(intro5, p1ControlsStart, row3, wordWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM,
				true);
		intro6.setImage(inTro6);
		Pane.positionInArea(intro6, p1ControlsStart, row4, wordWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM,
				true);
		jump1.setImage(juMp1);
		Pane.positionInArea(jump1, p1ControlsStart, row5, wordWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM, true);

		// player two controls
		intro7.setImage(inTro7);
		Pane.positionInArea(intro7, controls2Start, controlsHeight, controlsWidth, wordHeight, 0, null, HPos.CENTER,
				VPos.BOTTOM, true);
		intro8.setImage(inTro8);
		Pane.positionInArea(intro8, p2ControlsStart, row1, wordWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM,
				true);
		intro9.setImage(inTro9);
		Pane.positionInArea(intro9, p2ControlsStart, row2, wordWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM,
				true);
		intro10.setImage(inTro10);
		Pane.positionInArea(intro10, p2ControlsStart, row3, wordWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM,
				true);
		intro11.setImage(inTro11);
		Pane.positionInArea(intro11, p2ControlsStart, row4, wordWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM,
				true);
		jump2.setImage(juMp2);
		Pane.positionInArea(jump2, p2ControlsStart, row5, wordWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM, true);

		// enter
		enter.setImage(entEr);
		enter.setFitHeight(wordHeight);
		enter.setPreserveRatio(true);
		Pane.positionInArea(enter, enterStart, buttonsHeight, buttonsWidth, wordHeight, 0, null, HPos.CENTER,
				VPos.BOTTOM, true);

		// back
		back.setImage(baCk);
		back.setFitHeight(wordHeight);
		back.setPreserveRatio(true);
		Pane.positionInArea(back, backStart, buttonsHeight, buttonsWidth, wordHeight, 0, null, HPos.CENTER, VPos.BOTTOM,
				true);

		root.getChildren().addAll(intro1, intro1a, intro2, intro3, intro4, intro5, intro6, intro7, intro8, intro9,
				intro10, intro11, enter, back, jump1, jump2);

	}

	/**
	 * Draws the background selection screen
	 */
	private void drawBackground() {

		ImageView selectBackground = new ImageView();
		Image bGround = new Image("words/background.png");

		int col1 = 50;
		int col2 = 350;
		int col3 = 650;
		int col4 = 950;
		int row1 = 150;
		int row2 = 360;
		int widthPic = 250;
		int heightPic = 150;
		int buttonsWidth = 200;
		int buttonsHeight = 560;
		int okStart = 800;
		int backStart = 165;
		int wordHeight = 80;
		int wordHeight1 = 73;
		int titleStart = 300;
		int titleStartY = 30;
		int titleWidth = 650;

		root.setStyle("-fx-background-color: black;");
		selectBackground.setImage(bGround);
		selectBackground.setFitHeight(wordHeight);
		Pane.positionInArea(selectBackground, titleStart, titleStartY, titleWidth, wordHeight, 0, null, HPos.CENTER,
				VPos.BOTTOM, true);

		sPacEy.setImage(spacey);
		sPacEy.setFitWidth(widthPic);
		sPacEy.setFitHeight(heightPic);
		Pane.positionInArea(sPacEy, col1, row1, widthPic, heightPic, 0, null, HPos.CENTER, VPos.BOTTOM, true);

		forEst.setImage(forest);
		forEst.setFitWidth(widthPic);
		forEst.setFitHeight(heightPic);
		Pane.positionInArea(forEst, col2, row1, widthPic, heightPic, 0, null, HPos.CENTER, VPos.BOTTOM, true);

		sUnSet.setImage(sunset);
		sUnSet.setFitWidth(widthPic);
		sUnSet.setFitHeight(heightPic);
		Pane.positionInArea(sUnSet, col3, row1, widthPic, heightPic, 0, null, HPos.CENTER, VPos.BOTTOM, true);

		pAlm.setImage(palmTree);
		pAlm.setFitWidth(widthPic);
		pAlm.setFitHeight(heightPic);
		Pane.positionInArea(pAlm, col4, row1, widthPic, heightPic, 0, null, HPos.CENTER, VPos.BOTTOM, true);

		bAy.setImage(bay);
		bAy.setFitWidth(widthPic);
		bAy.setFitHeight(heightPic);
		Pane.positionInArea(bAy, col1, row2, widthPic, heightPic, 0, null, HPos.CENTER, VPos.BOTTOM, true);

		sanFrAn.setImage(sanFran);
		sanFrAn.setFitWidth(widthPic);
		sanFrAn.setFitHeight(heightPic);
		Pane.positionInArea(sanFrAn, col2, row2, widthPic, heightPic, 0, null, HPos.CENTER, VPos.BOTTOM, true);

		pUrp.setImage(purpleAcid);
		pUrp.setFitWidth(widthPic);
		pUrp.setFitHeight(heightPic);
		Pane.positionInArea(pUrp, col3, row2, widthPic, heightPic, 0, null, HPos.CENTER, VPos.BOTTOM, true);

		yoSeMiTe.setImage(yosemite);
		yoSeMiTe.setFitWidth(widthPic);
		yoSeMiTe.setFitHeight(heightPic);
		Pane.positionInArea(yoSeMiTe, col4, row2, widthPic, heightPic, 0, null, HPos.CENTER, VPos.BOTTOM, true);

		// back
		back1.setImage(baCk);
		back1.setFitHeight(wordHeight1);
		Pane.positionInArea(back1, backStart, buttonsHeight, buttonsWidth, wordHeight, 0, null, HPos.CENTER,
				VPos.BOTTOM, true);

		// ok
		ok.setImage(oK);
		ok.setFitHeight(wordHeight1);
		Pane.positionInArea(ok, okStart, buttonsHeight, buttonsWidth, wordHeight, 0, null, HPos.CENTER, VPos.BOTTOM,
				true);

		themeSong.stop();

		root.getChildren().addAll(selectBackground, sPacEy, forEst, sUnSet, pAlm, bAy, sanFrAn, pUrp, yoSeMiTe, back1,
				ok);

	}

	/**
	 * This draws the character selection screen for both player one and player two.
	 * 
	 * @param num dependent upon if choosing for player one or player two.
	 */
	private void drawCharacter(int num) {

		ImageView fighter = new ImageView();
		Image fiGhter = new Image("words/fighter.png"); // 612 x 77 pixels
		ImageView for4 = new ImageView();
		Image foR = new Image("words/for.png"); // 128 x 74 pixels
		ImageView playersTurn = new ImageView();

		int titleStart1 = 305;
		int titleStart2 = 555;
		int titleStart3 = 445;
		int titleHeight = 80;
		int titleRow1 = 10;
		int titleRow2 = 90;
		int titleRow3 = 170;
		int titleWidth1 = 625;
		int titleWidth2 = 130;
		int titleWidth3 = 335;

		int imageWidth = 100;
		int imageHeight = 150;
		int imageY = 250;
		int imageCol1 = 400;
		int imageCol2 = 750;

		int wordWidth = 200;
		int wordHeight = 100;

		int rectWidth = 200;
		int rectHeight = 150;
		int radius = 20;
		int rectCol1 = 75;
		int rectCol2 = 975;
		int rectRow = 400;
		int playerRow = 300;

		int wordHeight1 = 80;
		int wordHeight2 = 73;
		int wordStart1 = 165;
		int wordStart2 = 800;
		int wordStartY = 560;

		// background
		root.setStyle("-fx-background-color: black;");
		fighter.setImage(fiGhter);
		fighter.setPreserveRatio(true);
		fighter.setFitHeight(titleHeight);
		Pane.positionInArea(fighter, titleStart1, titleRow1, titleWidth1, titleHeight, 0, null, HPos.LEFT, VPos.BOTTOM,
				true);

		for4.setImage(foR);
		for4.setPreserveRatio(true);
		for4.setFitHeight(titleHeight);
		Pane.positionInArea(for4, titleStart2, titleRow2, titleWidth2, titleHeight, 0, null, HPos.LEFT, VPos.BOTTOM,
				true);

		if (num == 1) {
			playersTurn.setImage(plAy1);
		} else if (num == 2) {
			playersTurn.setImage(plAy2);
		}
		playersTurn.setPreserveRatio(true);
		playersTurn.setFitHeight(titleHeight);
		Pane.positionInArea(playersTurn, titleStart3, titleRow3, titleWidth3, titleHeight, 0, null, HPos.LEFT,
				VPos.BOTTOM, true);

		char1.setImage(kermit);
		Pane.positionInArea(char1, imageCol1, imageY, imageWidth, imageHeight, 0, null, HPos.CENTER, VPos.CENTER, true);

		char2.setImage(bubs);
		Pane.positionInArea(char2, imageCol2, imageY, imageWidth, imageHeight, 0, null, HPos.CENTER, VPos.CENTER, true);

		play1.setImage(plAy1);
		play1.setFitWidth(wordWidth);
		Pane.positionInArea(play1, rectCol1, playerRow, wordWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM, true);

		Rectangle r1 = new Rectangle();
		r1.setStroke(Color.MEDIUMSPRINGGREEN);
		r1.setFill(Color.INDIGO);
		r1.setWidth(rectWidth);
		r1.setHeight(rectHeight);
		r1.setArcWidth(radius);
		r1.setArcHeight(radius);
		Pane.positionInArea(r1, rectCol1, rectRow, rectWidth, rectHeight, 0, null, HPos.CENTER, VPos.CENTER, true);

		play2.setImage(plAy2);
		play2.setFitWidth(wordWidth);
		Pane.positionInArea(play2, rectCol2, playerRow, wordWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM, true);

		Rectangle r2 = new Rectangle();
		r2.setStroke(Color.MEDIUMSPRINGGREEN);
		r2.setFill(Color.INDIGO);
		r2.setWidth(rectWidth);
		r2.setHeight(rectHeight);
		r2.setArcWidth(radius);
		r2.setArcHeight(radius);
		Pane.positionInArea(r2, rectCol2, rectRow, rectWidth, rectHeight, 0, null, HPos.CENTER, VPos.CENTER, true);

		Pane.positionInArea(chosen1, rectCol1, rectRow, rectWidth, rectHeight, 0, null, HPos.CENTER, VPos.CENTER, true);

		Pane.positionInArea(chosen2, rectCol2, rectRow, rectWidth, rectHeight, 0, null, HPos.CENTER, VPos.CENTER, true);

		// back
		back2.setImage(baCk);
		back2.setFitHeight(wordHeight2);
		Pane.positionInArea(back2, wordStart1, wordStartY, wordWidth, wordHeight1, 0, null, HPos.CENTER, VPos.BOTTOM,
				true);

		// ok
		ok1.setImage(oK);
		ok1.setFitHeight(wordHeight2);
		Pane.positionInArea(ok1, wordStart2, wordStartY, wordWidth, wordHeight1, 0, null, HPos.CENTER, VPos.BOTTOM,
				true);

		root.getChildren().addAll(r1, r2, chosen1, chosen2, char1, char2, back2, ok1, fighter, for4, playersTurn, play1,
				play2);
	}

	/**
	 * This draws the screen that asks the user if the character selected will be a
	 * human or an ai player for both player one and player two.
	 * 
	 * @param num dependent upon if for player one or player two
	 */
	private void drawHuman_AI(int num) {

		ImageView is = new ImageView();
		Image iS = new Image("words/is.png"); // 80 x 75 pixels
		ImageView select = new ImageView();
		Image seLect = new Image("words/please select.png"); // 421 x 75 pixels
		Image aI = new Image("words/ai.png"); // 86 x 76 pixels
		Image humAn = new Image("words/human.png"); // 234 x 82 pixels

		int wordHeight = 75;
		int row1 = 80;
		int row2 = 200;
		int row3 = 300;
		int row4 = 410;
		int row5 = 560;
		int selectStart = 415;
		int selectWidth = 425;
		int playerStart = 458;
		int playerWidth = 335;
		int isStart = 585;
		int isWidth = 80;
		int humanStart = 310;
		int humanWidth = 235;
		int aiStart = 715;
		int okStart = 970;
		int okWidth = 110;
		int backStart = 165;
		int backWidth = 190;

		// background
		root.setStyle("-fx-background-color: black;");

		// please select
		select.setImage(seLect);
		Pane.positionInArea(select, selectStart, row1, selectWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM, true);

		// player one or player two
		if (num == 1) {
			plaYer.setImage(plAy1);
		} else if (num == 2) {
			plaYer.setImage(plAy2);
		}
		Pane.positionInArea(plaYer, playerStart, row2, playerWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM, true);

		// is
		is.setImage(iS);
		Pane.positionInArea(is, isStart, row3, isWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM, true);

		// human
		human.setImage(humAn);
		human.setFitHeight(wordHeight);
		Pane.positionInArea(human, humanStart, row4, humanWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM, true);

		// ai
		ai.setImage(aI);
		ai.setFitHeight(wordHeight);
		Pane.positionInArea(ai, aiStart, row4, humanWidth, wordHeight, 0, null, HPos.CENTER, VPos.BOTTOM, true);

		// ok
		ok3.setImage(oK);
		ok3.setFitHeight(wordHeight);
		Pane.positionInArea(ok3, okStart, row5, okWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM, true);

		// back
		back3.setImage(baCk);
		back3.setFitHeight(wordHeight);
		Pane.positionInArea(back3, backStart, row5, backWidth, wordHeight, 0, null, HPos.LEFT, VPos.BOTTOM, true);

		root.getChildren().addAll(select, plaYer, is, human, ai, ok3, back3);

	}

	/**
	 * Draws the fight screen where the game is actually played.
	 */
	private void drawFight() {

		ImageView hP1 = new ImageView();
		Image hp1 = new Image("words/player one.png");
		ImageView hP2 = new ImageView();
		Image hp2 = new Image("words/player two.png");

		Image thrEe = new Image("words/3.png");
		Image tWo = new Image("words/2.png");
		Image oNe = new Image("words/1.png");
		Image fiGht = new Image("words/fight.png");

		int healthWidth = 275;
		int healthWidth1 = 100;
		int healthWidth2 = 200;
		int healthHeight = 40;
		int healthBarY = 50;
		int healthBarX1 = 28;
		int healthBarX2 = 800;
		int titleWidth = 180;
		int titleHeight = 30;
		int titleY = 20;
		int titleX1 = 125;
		int titleX2 = 945;

		int endWordHeight = 150;
		int playerWordWidth = 950;
		int winsWidth = 420;
		int exclaimWidth = 65;
		int firstRow = 120;
		int secondRow = firstRow + endWordHeight;
		int firstRowStartX = 150;
		int secondRowX1 = 320;
		int secondRowX2 = 740;
		int secondRowX3 = 810;
		int secondRowX4 = 880;

		int fadeHeight = 200;
		int fadeWidthNum = 125;
		int fadeWidthFight = 450;
		int fadeY = 150;
		int fadeXNum = 560;
		int fadeXFight = 400;

		// background
		root.setStyle("-fx-background-color: black;");

		fightScreen.setFitWidth(Game.SCREEN_SIZE.x);
		fightScreen.setFitHeight(Game.SCREEN_SIZE.y);

		// player one
		Pane.positionInArea(player1, aGame.getPosition(Team.LEFT).x, aGame.getPosition(Team.LEFT).y,
				aGame.getSize(Team.LEFT).x, aGame.getSize(Team.LEFT).y, 0, null, HPos.CENTER, VPos.CENTER, true);

		// player two
		this.player2.setScaleX(-1);
		Pane.positionInArea(player2, aGame.getPosition(Team.RIGHT).x, aGame.getPosition(Team.RIGHT).y,
				aGame.getSize(Team.RIGHT).x, aGame.getSize(Team.RIGHT).y, 0, null, HPos.CENTER, VPos.CENTER, true);

		// player one health bar title
		hP1.setImage(hp1);
		Pane.positionInArea(hP1, titleX1, titleY, titleWidth, titleHeight, 0, null, HPos.CENTER, VPos.CENTER, true);

		// player one health bar
		healthP1.setStyle(
				"-fx-accent: indigo; -fx-control-inner-background: mediumspringgreen; -fx-text-box-border: black;");
		healthP1.setPrefSize(healthWidth, healthHeight);
		healthP1.setScaleX(-1);
		healthP1.setProgress(1);
		Pane.positionInArea(healthP1, healthBarX1, healthBarY, healthWidth1, healthHeight, 0, null, HPos.CENTER,
				VPos.CENTER, true);

		// player two health bar title
		hP2.setImage(hp2);
		Pane.positionInArea(hP2, titleX2, titleY, titleWidth, titleHeight, 0, null, HPos.CENTER, VPos.CENTER, true);

		// player two health bar
		healthP2.setStyle(
				"-fx-accent: indigo; -fx-control-inner-background: mediumspringgreen; -fx-text-box-border: black;");
		healthP2.setPrefSize(healthWidth, healthHeight);
		healthP2.setScaleX(-1);
		healthP2.setProgress(1);
		Pane.positionInArea(healthP2, healthBarX2, healthBarY, healthWidth2, healthHeight, 0, null, HPos.CENTER,
				VPos.CENTER, true);

		player.setPreserveRatio(true);
		player.setFitHeight(endWordHeight);
		Pane.positionInArea(player, firstRowStartX, firstRow, playerWordWidth, endWordHeight, 0, null, HPos.LEFT,
				VPos.BOTTOM, true);

		wins.setPreserveRatio(true);
		wins.setFitHeight(endWordHeight);
		Pane.positionInArea(wins, secondRowX1, secondRow, winsWidth, endWordHeight, 0, null, HPos.LEFT, VPos.BOTTOM,
				true);

		exclaim1.setPreserveRatio(true);
		exclaim1.setFitHeight(endWordHeight);
		Pane.positionInArea(exclaim1, secondRowX2, secondRow, exclaimWidth, endWordHeight, 0, null, HPos.LEFT,
				VPos.BOTTOM, true);

		exclaim2.setPreserveRatio(true);
		exclaim2.setFitHeight(endWordHeight);
		Pane.positionInArea(exclaim2, secondRowX3, secondRow, exclaimWidth, endWordHeight, 0, null, HPos.LEFT,
				VPos.BOTTOM, true);

		exclaim3.setPreserveRatio(true);
		exclaim3.setFitHeight(endWordHeight);
		Pane.positionInArea(exclaim3, secondRowX4, secondRow, exclaimWidth, endWordHeight, 0, null, HPos.LEFT,
				VPos.BOTTOM, true);

		three.setImage(thrEe); // 126 x 209
		three.setPreserveRatio(true);
		three.setFitHeight(fadeHeight);
		three.setOpacity(0);
		Pane.positionInArea(three, fadeXNum, fadeY, fadeWidthNum, fadeHeight, 0, null, HPos.CENTER, VPos.BOTTOM, true);

		two.setImage(tWo); // 125 x 201
		two.setPreserveRatio(true);
		two.setFitHeight(fadeHeight);
		two.setOpacity(0);
		Pane.positionInArea(two, fadeXNum, fadeY, fadeWidthNum, fadeHeight, 0, null, HPos.CENTER, VPos.BOTTOM, true);

		one.setImage(oNe); // 77 x 282
		one.setPreserveRatio(true);
		one.setFitHeight(fadeHeight);
		one.setOpacity(0);
		Pane.positionInArea(one, fadeXNum, fadeY, fadeWidthNum, fadeHeight, 0, null, HPos.CENTER, VPos.BOTTOM, true);

		fightWord.setImage(fiGht); // 421 x 143
		fightWord.setPreserveRatio(true);
		fightWord.setFitHeight(fadeHeight);
		fightWord.setOpacity(0);
		Pane.positionInArea(fightWord, fadeXFight, fadeY, fadeWidthFight, fadeHeight, 0, null, HPos.CENTER, VPos.BOTTOM,
				true);

		onGameStart();

		root.getChildren().addAll(fightScreen, healthP1, healthP2, hP1, hP2, player1, player2, three, two, one,
				fightWord, player, wins, exclaim1, exclaim2, exclaim3);

	}

	/**
	 * Draws the error screen for when two AI's are chosen. That is not allowed to
	 * happen. Once clicked, sends user back to first character selection screen.
	 */

	private void drawError() {
		// uh-oh sound
		// https://freesound.org/people/JapanYoshiTheGamer/sounds/361255/
		String uhOh = "src/sounds/8-bit-uh-oh-sound.wav";
		Media soundSelect = new Media(new File(uhOh).toURI().toString());
		MediaPlayer uhOhSound = new MediaPlayer(soundSelect);

		ImageView shock = new ImageView();

		// https://commons.wikimedia.org/wiki/File:Robot-clip-art-book-covers-feJCV3-clipart.png
		Image robot = new Image("sprites/robot.png"); // 504 x 599 pixels
		ImageView uh = new ImageView();

		Image uH = new Image("words/uh.png"); // 242 x 169 pixels
		ImageView oh = new ImageView();
		Image oH = new Image("words/oh.png"); // 242 x 169 pixels
		ImageView exclamation = new ImageView();
		Image excl = new Image("words/exclamation.png"); // 66 x 183 pixels
		ImageView choose = new ImageView();
		Image choOse = new Image("words/choose.png"); // 820 x 79 pixels
		ImageView oneHuman = new ImageView();
		Image oneHuMan = new Image("words/one human.png"); // 612 x 82 pixels

		int robotStart = 373;
		int robotY = 0;

		int robotWidth = 504;
		int robotHeight = 599;
		int changeHeight = 500;

		int topLeft = 120;
		int topY = 10;
		int topWidth = 245;
		int topHeight = 170;
		int topHeight2 = 169;
		int topRight = 855;
		int topRight2 = 1100;
		int topRight2Width = 65;

		int bottomHeight = 80;
		int bottomStart = 580;
		int bottomLeftWidth = 700;
		int bottomLeftWidth2 = 697;
		int bottomLeftStart = 20;
		int bottomRightWidth = 520;
		int bottomRightStart = 710;

		// background
		root.setStyle("-fx-background-color: black;");

		shock.setImage(robot);
		shock.setPreserveRatio(true);
		shock.setFitHeight(changeHeight);
		Pane.positionInArea(shock, robotStart, robotY, robotWidth, robotHeight, 0, null, HPos.CENTER, VPos.CENTER,
				true);

		uh.setImage(uH);
		Pane.positionInArea(uh, topLeft, topY, topWidth, topHeight, 0, null, HPos.CENTER, VPos.CENTER, true);

		oh.setImage(oH);
		Pane.positionInArea(oh, topRight, topY, topWidth, topHeight, 0, null, HPos.CENTER, VPos.CENTER, true);

		exclamation.setImage(excl);
		exclamation.setFitHeight(topHeight2);
		Pane.positionInArea(exclamation, topRight2, topY, topRight2Width, topHeight, 0, null, HPos.CENTER, VPos.CENTER,
				true);

		choose.setImage(choOse);
		choose.setFitWidth(bottomLeftWidth2);
		choose.setFitHeight(bottomHeight);
		Pane.positionInArea(choose, bottomLeftStart, bottomStart, bottomLeftWidth, bottomHeight, 0, null, HPos.LEFT,
				VPos.BOTTOM, true);
		oneHuman.setImage(oneHuMan);
		oneHuman.setFitWidth(bottomRightWidth);
		oneHuman.setFitHeight(bottomHeight);
		Pane.positionInArea(oneHuman, bottomRightStart, bottomStart, bottomRightWidth, bottomHeight, 0, null, HPos.LEFT,
				VPos.BOTTOM, true);

		if (MUTE) {
			uhOhSound.setMute(true);
		} else {
			uhOhSound.setVolume(0.2);
		}

		uhOhSound.play();
		root.getChildren().addAll(shock, oh, uh, exclamation, choose, oneHuman);
	}

}