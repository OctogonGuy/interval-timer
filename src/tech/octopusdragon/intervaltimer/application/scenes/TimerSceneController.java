package tech.octopusdragon.intervaltimer.application.scenes;

import java.util.TimerTask;

import tech.octopusdragon.intervaltimer.IntervalTimer;
import tech.octopusdragon.intervaltimer.Modality;
import tech.octopusdragon.intervaltimer.application.IntervalTimerApplication;
import tech.octopusdragon.intervaltimer.application.controls.CountdownPieChart;
import tech.octopusdragon.intervaltimer.application.util.Userdata;
import tech.octopusdragon.intervaltimer.application.util.Values;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class TimerSceneController {
	
	private IntervalTimer intervalTimer;
	
	@FXML private Label intervalLabel;
	@FXML private ComboBox<Modality> modalityComboBox;
	@FXML private Label timerLabel;
	@FXML private CountdownPieChart timerPieChart;
	@FXML private Button backButton;
	@FXML private Button pauseResumeButton;
	@FXML private Button previousButton;
	@FXML private Button nextButton;
	@FXML private Button restartButton;
	@FXML private Button restartIntervalButton;
	
	/**
	 * Sets the interval timer and updates the label to show its countdown.
	 * @param timer The interval timer
	 */
	public void setTimer(IntervalTimer timer) {
		intervalTimer = timer;
		intervalTimer.addTask(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					updateDisplay();
				});
			}
		});
		updateDisplay();
		
		// Populate modality combo box
		for (Modality modality : Modality.values()) {
			modalityComboBox.getItems().add(modality);
		}
		// Save modality to userdata
		modalityComboBox.setValue(
				Modality.valueOf(Userdata.readProperty("modality", Values.DEFAULT_MODALITY.name())));
		modalityComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
			setModality(modalityComboBox.getValue());
			Userdata.writeProperty("modality", newVal.name());
		});
		
		// Remove/add label and pie chart when visibility is changed
		timerLabel.managedProperty().bind(timerLabel.visibleProperty());
		timerPieChart.managedProperty().bind(timerPieChart.visibleProperty());
	}
	
	
	/**
	 * Sets the modality of the timer graphic, adding/removing the appropriate
	 * nodes from the scene. Also sets the modality setting in the userdata
	 * @param modality The new modality
	 */
	private void setModality(Modality modality) {
		switch (modality) {
		case DIGITAL:
			timerLabel.setVisible(true);
			timerPieChart.setVisible(false);
			break;
		case ANALOG:
			timerLabel.setVisible(false);
			timerPieChart.setVisible(true);
			break;
		case BOTH:
			timerLabel.setVisible(true);
			timerPieChart.setVisible(true);
		}
	}
	
	
	/**
	 * Updates the timer's display
	 */
	private void updateDisplay() {
		intervalLabel.setText(intervalTimer.intervalNumberString());
		timerLabel.setText(intervalTimer.timeLeftString());
		timerPieChart.displayElapsedTime(
				intervalTimer.timeLeft(), intervalTimer.curInterval().getDuration());
		
		// Enable/disable appropriate buttons
		if (!previousButton.isDisabled() &&
				!intervalTimer.isRepeat() &&
				intervalTimer.curIntervalIndex() == 0) {
			previousButton.setDisable(true);
		}
		else if (previousButton.isDisabled() &&
				intervalTimer.isRepeat() ||
				intervalTimer.curIntervalIndex() != 0) {
			previousButton.setDisable(false);
		}
		if (!nextButton.isDisabled() &&
				!intervalTimer.isRepeat() &&
				intervalTimer.curIntervalIndex() == intervalTimer.numIntervals() - 1) {
			nextButton.setDisable(true);
		}
		else if (nextButton.isDisabled() &&
				intervalTimer.isRepeat()||
				intervalTimer.curIntervalIndex() != intervalTimer.numIntervals() - 1) {
			nextButton.setDisable(false);
		}
		if (intervalTimer.isCanceled()) {
			pauseResumeButton.setDisable(true);
			previousButton.setDisable(true);
			nextButton.setDisable(true);
			restartButton.setDisable(true);
			restartIntervalButton.setDisable(true);
		}
		
		// Update pause/resume button text
		if (intervalTimer.isRunning()) {
			pauseResumeButton.setText("Pause");
		}
		else {
			pauseResumeButton.setText("Resume");
		}
	}
	
	
	@FXML
	private void backToMenu(ActionEvent event) {
		if (intervalTimer.isRunning()) {
			intervalTimer.freeze();
		}
		updateDisplay();
		IntervalTimerApplication.switchToMenuScene();
	}
	
	
	@FXML
	private void togglePause(ActionEvent event) {
		intervalTimer.togglePause();
		updateDisplay();
	}
	
	
	@FXML
	private void previous(ActionEvent event) {
		intervalTimer.previous();
		updateDisplay();
	}
	
	
	@FXML
	private void next(ActionEvent event) {
		intervalTimer.next();
		updateDisplay();
	}
	
	
	@FXML
	private void restart(ActionEvent event) {
		intervalTimer.restart();
		updateDisplay();
	}
	
	
	@FXML
	private void restartInterval(ActionEvent event) {
		intervalTimer.restartInterval();
		updateDisplay();
	}
}