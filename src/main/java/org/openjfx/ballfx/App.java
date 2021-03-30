package org.openjfx.ballfx;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class App extends Application {


	/**
	 * Anchor Pane representing the main window (Main.fxml).
	 */
	private AnchorPane root;
	
	/**
	 * Value by which the ball will be moved on the X axis in the Timeline.
	 */
	public static double deltaX = DefaultSettings.DEFAULT_BALL_DELTAX;
	
	/**
	 * Value by which the ball will be moved on the Y axis in the Timeline.
	 */
	public static double deltaY = DefaultSettings.DEFAULT_BALL_DELTAY;

	/**
	 * Entry point for all JavaFX applications.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			
			/**
			 * FXMLLoader used to load the main window's FXML file (Main.fxml).
			 */
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
			root = loader.load();
			
			/**
			 * Controller instance from the main window.
			 */
			MainController controller = loader.getController();
			
			/**
			 * Main scene of the application.
			 */
			Scene scene = new Scene(root);
			
			/**
			 * Main stage of the application.
			 */
			Stage stage = new Stage();
			
			stage.setScene(scene);
			stage.setOnHidden(e -> controller.shutdown());
			stage.setTitle(DefaultSettings.MAIN_CONTROLLER_TITLE);
			stage.sizeToScene();
			stage.show();
			stage.setMinWidth(DefaultSettings.MAIN_CONTROLLER_MIN_SIZE);
			stage.setMinHeight(DefaultSettings.MAIN_CONTROLLER_MIN_SIZE);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.jfif")));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		/**
		 * Ball from the Main.fxml.
		 */
		Circle ball = (Circle) root.lookup("#ball");
		
		/**
		 * Pane from the Main.fxml inside which the ball is bouncing.
		 */
		Pane ballPane = (Pane) root.lookup("#ballPane");
		
		/**
		 * Timeline looping indefinite time.
		 */
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(DefaultSettings.DEFAULT_TIMELINE_DURATION), new EventHandler<ActionEvent>() {

			/**
			 * Contains the action to perform when each frame is started.
			 */
			@Override
			public void handle(ActionEvent t) {
				
				/**
				 * Bounds of the plane the ball is bouncing in.
				 */
				Bounds bounds = ballPane.getLayoutBounds();

				// Move the ball on the X/Y axis by the value of deltaX/deltaY
				ball.setLayoutX(ball.getLayoutX() + deltaX);
				ball.setLayoutY(ball.getLayoutY() + deltaY);

				// If ball touches the right/left edge the deltaX is inverted
				if (ball.getLayoutX() < (bounds.getMinX() + ball.getRadius())
						|| ball.getLayoutX() > (bounds.getMaxX() - ball.getRadius())) {
					deltaX = -deltaX;
				}

				// If ball touches the top/bottom edge the deltaY is inverted
				if (ball.getLayoutY() >= (bounds.getMaxY() - ball.getRadius())
						|| ball.getLayoutY() <= (bounds.getMinY() + ball.getRadius())) {
					deltaY = -deltaY;
				}
				
				// If the ball is outside the pane, move it inside
				if (ball.getLayoutX() > (bounds.getMaxX())) {
					ball.setLayoutX(bounds.getMaxX() - ball.getRadius());
				}
				
				// If the ball is outside the pane, move it inside
				if (ball.getLayoutY() > (bounds.getMaxY())) {
					ball.setLayoutY(bounds.getMaxY() - ball.getRadius());
				}

			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	
	/**
	 * Entry point for all Java applications.
	 */
	public static void main(String[] args) {
		
		//If the file doesn't exist create it with the default configuration values
		if(!Files.exists(Path.of(DefaultSettings.CONFIG_FILE_NAME))) {
			Properties prop = new Properties();
			try(BufferedWriter writer = Files.newBufferedWriter(Path.of(DefaultSettings.CONFIG_FILE_NAME))){
				 prop.setProperty("BallColor", DefaultSettings.DEFAULT_BALL_COLOR);
			     prop.setProperty("WindowColor", DefaultSettings.DEFAULT_WINDOW_COLOR);
			     prop.setProperty("BallColorPicker", DefaultSettings.DEFAULT_BALL_COLOR_PICKER_VALUE);
			     prop.setProperty("WindowColorPicker", DefaultSettings.DEFAULT_WINDOW_COLOR_PICKER_VALUE);
			     prop.setProperty("MouseAction", DefaultSettings.DEFAULT_MOUSE_ACTION);
			     prop.setProperty("BallSpeed", DefaultSettings.DEFAULT_BALL_SPEED);
			     prop.setProperty("BallSize", DefaultSettings.DEFAULT_BALL_SIZE);
			     prop.store(new FileOutputStream(DefaultSettings.CONFIG_FILE_NAME), null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		launch(args);
	}

}