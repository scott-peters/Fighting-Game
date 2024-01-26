// Start screen adapted from stack overflow answers for stacking images.
// https://stackoverflow.com/questions/29459369/how-do-i-stack-an-image-on-to-an-existing-background-in-javafx

// Position enums from oracle to help position wording
// https://docs.oracle.com/javase/8/javafx/api/javafx/geometry/Pos.html

// Image tutorial youtube
// https://www.youtube.com/watch?v=F8bY18QAj6U

// Creating multiple panes adapted from stack overflow answer for multiple panes in one scene.
// https://stackoverflow.com/questions/33339427/javafx-have-multiple-panes-in-one-scene

// Mouse event for ImageView adapted from stack overflow answer for adding event handler.
// https://stackoverflow.com/questions/25550518/add-eventhandler-to-imageview-contained-in-tilepane-contained-in-vbox

package main;

import game_interface.ASCII;
import game_interface.GUI;
import game_interface.GameInterface;
import game_interface.ModelsAndViewsController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import the_game.Game;

/**
 * Initializes all required objects to start the program.
 *
 */
public class Main extends Application {

	private static final Game aGame = new Game(null, null);

	/**
	 * This is the main of our program that decides which game to run.. GUI or ASCII
	 * 
	 * @param args this is the terminal input
	 */
	public static void main(String[] args) {
		// The program will automatically terminate in ascii mode due to no JFrame
		// visible so this will keep it running.
		if (GameInterface.IS_ASCII) {
			new ModelsAndViewsController(aGame, new ASCII());
			while (!aGame.isGameOver() && !Thread.interrupted()) // Wait until the game has ended to terminate.
				;
		} else {
			launch(args);
		}
	}

	@Override
	/**
	 * Initialize the GUI.
	 */
	public void start(Stage primaryStage) {
		// setting up all screens
		Pane root = new Pane();

		Scene scene = new Scene(root, Game.SCREEN_SIZE.x, Game.SCREEN_SIZE.y, Color.BLACK);

		// Ensures that the scene's key presses direct to the root's.
		scene.onKeyPressedProperty().bind(root.onKeyPressedProperty());
		scene.onKeyReleasedProperty().bind(root.onKeyReleasedProperty());

		// Attach the game and interface to the model
		new ModelsAndViewsController(aGame, new GUI(root, aGame));

		primaryStage.setTitle("Most Epic Sick Nasty Game of All Time");

		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
