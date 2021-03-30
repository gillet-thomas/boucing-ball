# Bouncing Ball Readme

This Java project is using JavaFX to replicate the behavior of a bouncing ball.

## Run the application
To launch my application, please run the ```mvn clean javafx:run``` command from the root directory.

Once the application is launched a ball will appear in the window and automatically start bouncing on the window&#39;s edges. If the window is resized by the user, the ball will adapt and bounce inside this smaller/bigger window.

## Application behavior
At the first launch of the application, a config file will be automatically created. This config file will be used to store all the application settings in order to keep the same configuration when the app is relaunched.

On the top left one can find a setting button. This button will bring up a settings windows that will allow the user to custom the application. This new window appears as modal which means that the user can&#39;t interact with the main window (with the bouncing ball) until he has closed the setting window.

First the user will be able to change the ball color as well as the background color of the window.
 To do so, 2 color pickers are available. Once the color is picked, the ball/window color is automatically changed in the main window.

It is also possible to use an image for the ball/window. To do so, 2 file explorer buttons are available allowing the user to browse his computer and upload the image wanted. Once again, once the image has been selected it is automatically set as the ball or window texture in the main window.

The user also has the possibility to change the default mouse action. There are only 2 possibilities: change the ball color or change the window background. The meaning of this mouse action is that when the user is in the main window and he clicks on it, either the ball or the window will automatically change its color. Changing the ball / window color using this mouse action in the main window will not update the ball color picker / window color picker value in the settings window.

Finally, it is also possible to change the ball speed and the ball size. To do so, 2 cursors are available. To use these cursors, one should click on them and not drag the cursor. The ball speed can go from 5 to 10 and the ball size can go from 5 to 25.

To save the settings, one should click on the red cross at the top right to close the window.
 This will have for effect to save all the settings in the config file.

When the user closes the main window, the ball color and the window color will be automatically updated in the config file.

## High-level description of the architecture

This program contains 4 classes and 2 FXML files.
 The four classes are App.java, DefaultSettings.java, MainController.java, SettingsController.java.

The **App.java** file is the main class of the application it contains the entry point of the program (the main function) as well as the entry point of the JavaFX application (the start method). The main window (Main.fxml) is loaded, initialized, and parameterized in this file. The bouncing animation is also defined there inside a TimeLine object.

Then there is the **MainController.java** file that is interacting with the Main.fxml file.
 This controller implements all the methods and components used within the main window.
 This class is also loading and creating an instance of the Settings.fxml so that the settings window can be displayed when the setting button is clicked.

It is the same for the **SettingsController.java** file that is interacting with the Settings.fxml file.
 This controller implements all the methods and components used within the settings window.

The **DefaultSettings.java** is an external static class which have the sole purpose of holding the default configuration values used within the application.
