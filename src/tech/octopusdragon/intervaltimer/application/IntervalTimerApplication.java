package tech.octopusdragon.intervaltimer.application;

import java.io.IOException;

import tech.octopusdragon.intervaltimer.Alert;
import tech.octopusdragon.intervaltimer.Interval;
import tech.octopusdragon.intervaltimer.IntervalTimer;
import tech.octopusdragon.intervaltimer.application.scenes.TimerSceneController;
import tech.octopusdragon.intervaltimer.application.util.Resource;
import tech.octopusdragon.intervaltimer.application.util.Userdata;
import tech.octopusdragon.intervaltimer.application.util.Values;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * This JavaFX application allows the user to create a timer that goes off
 * repeatedly after one or more specified intervals.
 * @author Alex Gill
 *
 */
public class IntervalTimerApplication extends Application {
	
	// --- Constants ---
	// Properties
	private static ObjectProperty<IntervalTimer> timerProperty;
	
	// FXML file paths
	public static final String MENU_SCENE_FXML =
			"scenes/MenuScene.fxml";
	public static final String TIMER_SCENE_FXML =
			"scenes/TimerScene.fxml";
	
	// --- Variables ---
	// GUI components
	private static Stage stage;
	// Last timer scene to be able to switch back
	private static Scene lastTimerScene;
	
	
	@Override
	public void init() {
		timerProperty = new SimpleObjectProperty<IntervalTimer>();
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		
		// Set the stage
		primaryStage.setTitle("Interval Timer");
		primaryStage.getIcons().add(new Image(Resource.ICON.getResourceAsStream()));
		primaryStage.setWidth(Values.DEFAULT_WIDTH);
		primaryStage.setHeight(Values.DEFAULT_WIDTH);
		switchToMenuScene();
		primaryStage.show();
	}
	
	
	/**
	 * @return The current interval timer property
	 */
	public static ObjectProperty<IntervalTimer> timerProperty() {
		return timerProperty;
	}
	
	
	/**
	 * @return The current interval timer
	 */
	public static IntervalTimer getTimer() {
		return timerProperty.get();
	}
	
	
	/**
	 * @param timer The interval timer to set
	 */
	public static void setTimer(IntervalTimer timer) {
		timerProperty.set(timer);
	}
	
	
	/**
	 * Sets the stage's current scene to a new scene with a root loaded with an
	 * FXML file
	 * @param fxmlPath The path of the FXML file
	 * @return The FXMLLoader associated with the new scene
	 */
	private static FXMLLoader switchToScene(String fxmlPath) {
		FXMLLoader loader = new FXMLLoader(
					IntervalTimerApplication.class.getResource(fxmlPath));
		try {
			Parent root = loader.load();
			root.setStyle("-base: " + Userdata.readProperty("ui-color", Values.DEFAULT_UI_COLOR));
			if (stage.getScene() == null) {
				stage.setScene(new Scene(root));
			}
			else {
				stage.getScene().setRoot(root);
			}
		} catch (IOException e) {
			System.out.println("Error loading FXML file");
			e.printStackTrace();
		}
		return loader;
	}
	
	
	/**
	 * Switches to the menu scene
	 */
	public static void switchToMenuScene() {
		switchToScene(MENU_SCENE_FXML);
	}
	
	
	/**
	 * Switches to a new timer scene and starts the timer
	 * @param alert The alert for the interval timer
	 * @param repeat Whether to repeat the interval timer
	 * @param Intervals The intervals
	 */
	public static void switchToNewTimerScene(Alert alert, boolean repeat, Interval...intervals) {
		setTimer(new IntervalTimer(alert, repeat, intervals));
		FXMLLoader loader = switchToScene(TIMER_SCENE_FXML);
		lastTimerScene = stage.getScene();
		TimerSceneController controller = loader.getController();
		controller.setTimer(getTimer());
	}
	
	
	/**
	 * Switches to the last loaded timer scene and starts the timer
	 */
	public static void switchToLastTimerScene() {
		stage.setScene(lastTimerScene);
		lastTimerScene.getRoot().setStyle("-base: " + Userdata.readProperty("ui-color", Values.DEFAULT_UI_COLOR));
		if (getTimer().isRunning()) {
			getTimer().start();
		}
	}

	
	public static void main(String[] args) {
		launch(args);
	}
}
