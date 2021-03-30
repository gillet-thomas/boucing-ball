package org.openjfx.ballfx;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class MainController implements Initializable {

	/**
	 * AnchorPane from the Main.fxml file.
	 */
	@FXML
	public AnchorPane anchorPane;
	
	/**
	 * Pane from the Main.fxml file.
	 */
	@FXML
	public Pane ballPane;
	
	/**
	 * Static Pane attribute holding the same Pane instance as ballPane.
	 */
	public static Pane static_ballPane;
	
	/**
	 * Ball (Circle object) from the Main.fxml file.
	 */
	@FXML
	private Circle ball;
	
	/**
	 * Static Ball (Circle object) variable holding the same Circle instance as ball.
	 */
	public static Circle static_ball;
	
	/**
	 * Settings Button from the Main.fxml file.
	 */
	@FXML
	public Button settings;

	/**
	 * Color of the bouncing ball.
	 */
	public static String ballColor;
	
	/**
	 * Color of the main window.
	 */
	public static String windowColor;
	
	/**
	 * Mouse action when the main window is clicked.
	 */
	public static String mouseAction;
	
	/**
	 * Index of the current color in the arraColors array.
	 */
	public int colorIndex = 0;
	
	/**
	 * Color array holding the default window colors displayed on mouse click.
	 */
	public Color[] arrayColors={Color.GREEN, Color.BLUE, Color.RED, Color.ANTIQUEWHITE, Color.YELLOW, Color.CHOCOLATE};
	
	/**
	 * Loads the settings from the configuration file and set them in the application.
	 * Sets the static variables' value.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ball.relocate(DefaultSettings.DEFAULT_BALL_LOCATION, DefaultSettings.DEFAULT_BALL_LOCATION);
		static_ball = ball;
		static_ballPane = ballPane;
		mouseAction = DefaultSettings.DEFAULT_MOUSE_ACTION;
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(DefaultSettings.CONFIG_FILE_NAME));
			
			static_ball.setRadius(Double.valueOf(prop.getProperty("BallSize")));
			App.deltaX = Double.valueOf(prop.getProperty("BallSpeed"));
			App.deltaY = App.deltaX - 3;
			
			mouseAction = prop.getProperty("MouseAction");
			
			if(Files.exists(Path.of(prop.getProperty("BallColor")))) {
				changeBallBackgroundImage(prop.getProperty("BallColor"));
			} else {
				ball.setFill(Color.web(prop.getProperty("BallColor")));
				ballColor = prop.getProperty("BallColor");
			}
			
			if(Files.exists(Path.of(prop.getProperty("WindowColor")))) {
				changeWindowBackgroundImage(prop.getProperty("WindowColor"));
			} else {
				changeWindowBackgroundColor(Color.web(prop.getProperty("WindowColor")));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * Method called when the Pane is clicked.
	 * It either changes the ball of the window color depending on the mouse action.
	 */
	@FXML
	public void changeColor() {
		if(mouseAction.equals("Change ball color")) {
			ball.setFill(arrayColors[colorIndex]);
			ballColor = arrayColors[colorIndex].toString();
		} else {
			changeWindowBackgroundColor(arrayColors[colorIndex]);
		}
		colorIndex = (colorIndex == arrayColors.length - 1) ? 0 : colorIndex + 1;
	}

	/**
	 * Method called when the setting button is clicked.
	 * It loads and displays the setting window.
	 */
	@FXML
	public void openSettingsWindow() {
		AnchorPane root = new AnchorPane();
	    Stage stage = new Stage();
	    AnchorPane frame;
	    
		try {
			frame = FXMLLoader.load(getClass().getResource("Settings.fxml"));
			root.getChildren().add(frame);
			Scene scene = new Scene(root);
			
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle(DefaultSettings.SETTINGS_CONTROLLER_TITLE);
			stage.initModality(Modality.APPLICATION_MODAL); 
			stage.initStyle(StageStyle.UTILITY);
			stage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
			      public void handle(WindowEvent we) {
			    	  SettingsController.writeSettings();
			      }
			  }); 
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method called by the Setting window to change the main window color.
	 * @param c The color to be applied as window background
	 */
	public static void changeWindowBackgroundColor(Color c) {
		BackgroundFill backgroundFill = new BackgroundFill(c, CornerRadii.EMPTY, Insets.EMPTY);
		Background background = new Background(backgroundFill);
		static_ballPane.setBackground(background);
		windowColor = c.toString();
	}
	
	/**
	 * Method called by the Setting window to change the main window background image.
	 * @param image path to set as the window background
	 */
	public static void changeWindowBackgroundImage(String path) {
		FileInputStream inputstream;
		try {
			inputstream = new FileInputStream(path);
			Image image = new Image(inputstream); 
			BackgroundImage bgi = new BackgroundImage(image, null, null, null, null);
			Background bg = new Background(bgi);
			static_ballPane.setBackground(bg);
			windowColor = path;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method called by the Setting window to change the bouncing ball background image.
	 * @param image path to set as the ball background
	 */
	public static void changeBallBackgroundImage(String path) {
		FileInputStream inputstream;
		try {
			inputstream = new FileInputStream(path);
			Image image = new Image(inputstream); 
			ImagePattern imagePattern = new ImagePattern(image);
			static_ball.setFill(imagePattern);
			ballColor = path;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Method called when the main window is closed.
	 * It saves the ball color and window color values in the configuration file.
	 */
	public void shutdown() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(DefaultSettings.CONFIG_FILE_NAME));
			prop.setProperty("BallColor", MainController.ballColor);
			prop.setProperty("WindowColor", MainController.windowColor);
			prop.store(new FileOutputStream(DefaultSettings.CONFIG_FILE_NAME), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
