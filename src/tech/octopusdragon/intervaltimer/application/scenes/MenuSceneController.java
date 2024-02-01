package tech.octopusdragon.intervaltimer.application.scenes;

import java.util.Optional;

import tech.octopusdragon.intervaltimer.Alert;
import tech.octopusdragon.intervaltimer.Interval;
import tech.octopusdragon.intervaltimer.Preset;
import tech.octopusdragon.intervaltimer.application.IntervalTimerApplication;
import tech.octopusdragon.intervaltimer.application.controls.AlertComboBox;
import tech.octopusdragon.intervaltimer.application.controls.IntervalEditorBox;
import tech.octopusdragon.intervaltimer.application.util.Userdata;
import tech.octopusdragon.intervaltimer.application.util.Values;
import tech.octopusdragon.intervaltimer.application.windows.PresetDialog;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class MenuSceneController {

	@FXML private Parent root;
	@FXML private ColorPicker colorPicker;
	@FXML private MenuButton presetMenuButton;
	@FXML private GridPane intervalTable;
	@FXML private Button addIntervalButton;
	@FXML private Button removeIntervalButton;
	@FXML private Button startButton;
	@FXML private Button continueButton;
	@FXML private CheckBox sharedAlertCheckBox;
	@FXML private AlertComboBox alertComboBox;
	@FXML private CheckBox repeatCheckBox;
	private ObservableList<IntervalEditorBox> intervalEditorBoxes;
	
	
	@FXML
	private void initialize() {
		
		// Initialize variables
		intervalEditorBoxes = FXCollections.observableArrayList();
		
		// Set color picker color to current color
		colorPicker.setValue(Color.web(Userdata.readProperty("ui-color", Values.DEFAULT_UI_COLOR)));
		
		// Populate preset menu
		for (Preset preset : Preset.values()) {
			MenuItem presetMenuItem = new MenuItem(preset.toString());
			presetMenuItem.setOnAction(event -> {
				PresetDialog presetDialog = new PresetDialog(preset);
				// Set style to have appropriate color
				presetDialog.getDialogPane().setStyle(root.getStyle());
				Optional<Interval[]> value = presetDialog.showAndWait();
				if (value != null && value.isPresent()) {
					Interval[] intervals = value.get();
					setIntervals(intervals);
					for (int i = 0; i < intervals.length; i++) {
						Userdata.writeInterval(i, intervals[i]);
					}
				}
			});
			presetMenuButton.getItems().add(presetMenuItem);
		}
		
		// Disable/enable shared alert/individual alerts on checking button
		alertComboBox.disableProperty().bind(sharedAlertCheckBox.selectedProperty().not());
		
		// Fill the alert combo box with alert values and set initial value
		alertComboBox.setValue(Alert.valueOf(Userdata.readProperty("alert", Values.DEFAULT_ALERT.name())));
		// Update userdata on changed
		alertComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
			Userdata.writeProperty("alert", newVal.name());
		});
		
		// Set initial value for shared alert check box
		sharedAlertCheckBox.setSelected(Boolean.parseBoolean(Userdata.readProperty("shared", Boolean.toString(Values.DEFAULT_SHARED_ALERT))));
		// Update userdata on changed
		sharedAlertCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
			Userdata.writeProperty("shared", Boolean.toString(newVal));
		});
		
		// Set initial value for repeat check box
		repeatCheckBox.setSelected(Boolean.parseBoolean(Userdata.readProperty("repeat", Boolean.toString(Values.DEFAULT_REPEAT))));
		// Update userdata on changed
		repeatCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
			Userdata.writeProperty("repeat", Boolean.toString(newVal));
		});
		
		// Disable/enable start button depending on whether any intervals are a
		// duration of zero. Check when the lists change in size.
		ListChangeListener<IntervalEditorBox> parentListener =
				new SpinnerValueZeroListener<IntervalEditorBox>();
		intervalEditorBoxes.addListener(parentListener);
		
		// Disable remove interval button if there is only one interval
		removeIntervalButton.disableProperty().bind(
				Bindings.createBooleanBinding(() -> {
			return intervalEditorBoxes.size() <= 1;
		}, intervalEditorBoxes));
		
		// Disable continue button if a timer has not been started yet
		continueButton.disableProperty().bind(IntervalTimerApplication.timerProperty().isNull());
		
		// Fill initial interval(s)
		Interval[] intervals = Userdata.readIntervals();
		for (Interval inverval : intervals) {
			addInterval(inverval);
		}
		
		// Disable the start button if any intervals are zero
		evaluateButton();
	}
	
	
	/**
	 * @return an array of intervals based on the user's input
	 */
	private Interval[] getIntervals() {
		Interval[] intervals = new Interval[intervalEditorBoxes.size()];
		for (int i = 0; i < intervalEditorBoxes.size(); i++) {
			intervals[i] = intervalEditorBoxes.get(i).getInterval();
		}
		return intervals;
	}
	
	
	/**
	 * Adds a brand new interval with no durations
	 */
	private void addInterval() {
		addInterval(0, 0, 0, null);
		Userdata.writeInterval(intervalEditorBoxes.size() - 1, intervalEditorBoxes.get(intervalEditorBoxes.size() - 1).getInterval());
		Userdata.writeProperty("num-intervals", Integer.toString(intervalEditorBoxes.size()));
	}
	
	
	/**
	 * Adds an interval that already exists
	 * @param interval The interval to add
	 */
	private void addInterval(Interval interval) {
		int h = interval.hours();
		int m = interval.minutes();
		int s = interval.seconds();
		Alert alert = interval.getAlert();
		addInterval(h, m, s, alert);
	}
	
	
	/**
	 * Adds an interval
	 * @param h Number of hours in the interval
	 * @param m Number of minutes in the interval
	 * @param s Number of seconds in the interval
	 * @param alert The alert of the interval
	 */
	private void addInterval(int h, int m, int s, Alert alert) {
		Label newLabel = new Label("Interval " + (intervalEditorBoxes.size() + 1));
		intervalTable.add(
				newLabel,
				0,
				intervalEditorBoxes.size());
		
		IntervalEditorBox newEditorBox = new IntervalEditorBox(new Interval(h, m, s, alert));
		intervalTable.add(newEditorBox, 1, intervalEditorBoxes.size());
		intervalEditorBoxes.add(newEditorBox);
		
		// Evaluate whether start button should be disabled on changed
		newEditorBox.intervalProperty().addListener(new SpinnerValueZeroListener<Interval>());
		
		// Disable interval alert if shared alert is on
		newEditorBox.alertDisabledProperty().bind(sharedAlertCheckBox.selectedProperty());
		
		// Update userdata on changed
		newEditorBox.intervalProperty().addListener((obs, oldVal, newVal) -> {
			Userdata.writeInterval(intervalEditorBoxes.indexOf(newEditorBox), newVal);
		});
	}
	
	
	/**
	 * Removes an interval
	 */
	private void removeInterval() {
		intervalTable.getChildren().remove(
				intervalTable.getChildren().size() - 2,
				intervalTable.getChildren().size());
		intervalEditorBoxes.remove(
				intervalEditorBoxes.size() - 1);
		Userdata.writeProperty("num-intervals", Integer.toString(intervalEditorBoxes.size()));
	}
	
	
	/**
	 * Removes all intervals
	 */
	private void removeAllIntervals() {
		while (!intervalEditorBoxes.isEmpty()) {
			removeInterval();
		}
	}
	
	
	/**
	 * Replaces all intervals with a set of intervals. If there are any
	 * intervals with different alerts, unselects shared alerts. Otherwise,
	 * selects shared alerts
	 * @param intervals The intervals to set
	 */
	private void setIntervals(Interval[] intervals) {
		removeAllIntervals();
		Alert firstAlert = intervals[0].getAlert();
		boolean foundNullAlert = false;
		boolean foundDifferentAlert = false;
		for (Interval interval : intervals) {
			addInterval(interval);
			Userdata.writeInterval(intervalEditorBoxes.size() - 1, interval);
			if (!foundNullAlert && !interval.hasAlert()) {
				foundNullAlert = true;
			}
			if (!foundDifferentAlert && interval.getAlert() != firstAlert) {
				foundDifferentAlert = true;
			}
		}
		if (foundNullAlert) {
			sharedAlertCheckBox.setSelected(true);
		}
		else if (foundDifferentAlert) {
			sharedAlertCheckBox.setSelected(false);
			for (int i = 0; i < intervals.length; i++) {
				intervalEditorBoxes.get(i).setAlert(intervals[i].getAlert());
			}
		}
		else {
			sharedAlertCheckBox.setSelected(true);
			alertComboBox.setValue(firstAlert);
		}
		Userdata.writeProperty("num-intervals", Integer.toString(intervalEditorBoxes.size()));
	}
	
	
	@FXML
	private void addInterval(ActionEvent event) {
		addInterval();
	}
	
	
	@FXML
	private void removeInterval(ActionEvent event) {
		removeInterval();
	}
	
	
	@FXML
	private void startIntervalTimer(ActionEvent event) {
		IntervalTimerApplication.switchToNewTimerScene(
				sharedAlertCheckBox.isSelected() ? alertComboBox.getValue() : null,
				repeatCheckBox.isSelected(),
				getIntervals());
	}
	
	
	@FXML
	private void continueIntervalTimer(ActionEvent event) {
		IntervalTimerApplication.switchToLastTimerScene();
	}
	
	
	@FXML
	private void changeUIColor(ActionEvent event) {
		Color color = colorPicker.getValue();
		String colorStr = 
		String.format("#%02x%02x%02x",
				(int)(Math.round(color.getRed() * 255)),
				(int)(Math.round(color.getGreen() * 255)),
				(int)(Math.round(color.getBlue() * 255)));
		Userdata.writeProperty("ui-color", colorStr);
		root.setStyle("-base: " + colorStr);
	}
	
	
	/**
	 * Prevents the start button from being enabled if a spinner has an interval
	 * duration of zero
	 * @author Alex Gill
	 *
	 */
	private class SpinnerValueZeroListener<T>
			implements ChangeListener<T>, ListChangeListener<T> {
		@Override
		public void changed(ObservableValue<? extends T> observable,
				T oldValue, T newValue) {
			evaluateButton();
		}
	
		@Override
		public void onChanged(Change<? extends T> c) {
			evaluateButton();
		}
	}
	
	
	/**
	 * Enable the start button if all intervals have a duration greater than
	 * zero. Otherwise, disable it.
	 */
	private void evaluateButton() {
		if (intervalEditorBoxes == null)
			return;
		
		boolean zeroFound = false;
		for (int i = 0; i < intervalEditorBoxes.size(); i++) {
			if (intervalEditorBoxes.get(i).getInterval().hours() == 0 &&
				intervalEditorBoxes.get(i).getInterval().minutes() == 0 &&
				intervalEditorBoxes.get(i).getInterval().seconds() == 0) {
				zeroFound = true;
				break;
			}
		}
		if (zeroFound && !startButton.isDisabled()) {
			startButton.setDisable(true);
		}
		else if (!zeroFound && startButton.isDisabled()) {
			startButton.setDisable(false);
		}
	}

}
