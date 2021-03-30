package org.openjfx.ballfx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

public class SettingsController extends MainController implements Initializable {

	/**
	 * ColorPicker from the Settings.fxml used to pick the main window's ball color.
	 */
	@FXML
	private ColorPicker ballPicker;
	
	/**
	 * Static ColorPicker attribute holding same value as ballPicker.
	 */
	public static ColorPicker static_ballPicker;

	/**
	 * ColorPicker from the Settings.fxml used to pick the main window's background color.
	 */
	@FXML
	private ColorPicker windowPicker;
	
	/**
	 * Static ColorPicker attribute holding same value as windowPicker.
	 */
	public static ColorPicker static_windowPicker;
	
	/**
	 * ColorPicker from the Settings.fxml used to pick the main window's default mouse action.
	 */
	@FXML
	private ComboBox<String> mouseActionCombobox;
	
	/**
	 * Static ComboBox attribute holding same value as mouseActionCombobox.
	 */
	private static ComboBox<String> static_mouseActionCombobox;
	
	/**
	 * ColorPicker from the Settings.fxml used to choose the main window's ball's size.
	 */
	@FXML
	private Slider ballSizeSlider;
	
	/**
	 * Static Slider attribute holding same value as ballSizeSlider.
	 */
	private static Slider static_ballSizeSlider;
	
	/**
	 * ColorPicker from the Settings.fxml used to pick the main window's ball's speed.
	 */
	@FXML
	private Slider ballSpeedSlider;
	
	/**
	 * Static Slider attribute holding same value as ballSpeedSlider.
	 */
	private static Slider static_ballSpeedSlider;
	
	/**
	 * Loads the settings from the configuration file and set the settings controls value accordingly.
	 * Sets the static variables' value.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mouseActionCombobox.setItems(FXCollections.observableArrayList("Change ball color","Change window background"));
		
		static_ballPicker = ballPicker;
		static_windowPicker = windowPicker;
		static_mouseActionCombobox = mouseActionCombobox;
		static_ballSizeSlider = ballSizeSlider;
		static_ballSpeedSlider = ballSpeedSlider;
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(DefaultSettings.CONFIG_FILE_NAME));
			ballPicker.setValue(Color.web(prop.getProperty("BallColorPicker")));
			windowPicker.setValue(Color.web(prop.getProperty("WindowColorPicker")));
			mouseActionCombobox.setValue(prop.getProperty("MouseAction"));
			ballSpeedSlider.setValue(Double.parseDouble(prop.getProperty("BallSpeed")));
			ballSizeSlider.setValue(Double.parseDouble(prop.getProperty("BallSize")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Method called when the ballPicker value is changed.
	 * It changes the main windows ball color and set the ballColor variable value accordingly.
	 */
	@FXML
	void changeBallColor() {
		static_ball.setFill(ballPicker.getValue());
		ballColor = ballPicker.getValue().toString();
	}
	
	/**
	 * Method called when the windowPicker value is changed.
	 * It changes the main windows color.
	 */
	@FXML
	void changeWindowColor() {
		changeWindowBackgroundColor(windowPicker.getValue());
	}
	
	/**
	 * Method called when the "Ball background" file browser button is clicked.
	 * It changes the main windows ball background image.
	 */
	@FXML
	void changeBallBackground() {
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);
		if(selectedFile != null) {
			changeBallBackgroundImage(selectedFile.getPath());
		}
	}
	
	/**
	 * Method called when the "Window background" file browser button is clicked.
	 * It changes the main windows background image.
	 */
	@FXML
	void changeWindowBackground() {
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);
		if(selectedFile != null) {
			changeWindowBackgroundImage(selectedFile.getPath());
		}
	}
	
	/**
	 * Method called when the mouseActionCombobox value is changed.
	 * It changes the mouseAction variable value accordingly.
	 */
	@FXML
	void changeMouseAction() {
		mouseAction = mouseActionCombobox.getValue();
	}
	
	/**
	 * Method called when the ballSizeSlider value is changed.
	 * It changes the main window ball size accordingly.
	 */
	@FXML
	void changeBallSize() {
		static_ball.setRadius(ballSizeSlider.getValue());
	}
	
	/**
	 * Method called when the ballSpeedSlider value is changed.
	 * It changes the main window ball speed accordingly.
	 */
	@FXML
	void changeBallSpeed() {
		App.deltaX = ballSpeedSlider.getValue();
		App.deltaY = App.deltaX - 3;
	}
	
	/**
	 * Method called when the setting window is closed.
	 * It saves all the settings controls value in the configuration file.
	 */
	public static void writeSettings() {
		Properties prop = new Properties();
		try(BufferedWriter writer = Files.newBufferedWriter(Path.of(DefaultSettings.CONFIG_FILE_NAME))){
			 prop.setProperty("BallColor", MainController.ballColor);
		     prop.setProperty("WindowColor", MainController.windowColor);
		     prop.setProperty("BallColorPicker", static_ballPicker.getValue().toString());
		     prop.setProperty("WindowColorPicker", static_windowPicker.getValue().toString());
		     prop.setProperty("MouseAction", static_mouseActionCombobox.getValue());
		     prop.setProperty("BallSpeed", String.valueOf(static_ballSpeedSlider.getValue()));
		     prop.setProperty("BallSize", String.valueOf(static_ballSizeSlider.getValue()));
		     prop.store(new FileOutputStream(DefaultSettings.CONFIG_FILE_NAME), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
