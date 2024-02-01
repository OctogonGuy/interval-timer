package tech.octopusdragon.intervaltimer.application.windows;

import java.io.IOException;

import tech.octopusdragon.intervaltimer.Interval;
import tech.octopusdragon.intervaltimer.Preset;
import tech.octopusdragon.intervaltimer.Preset.Value;
import tech.octopusdragon.intervaltimer.application.controls.AlertComboBox;
import tech.octopusdragon.intervaltimer.application.controls.IntervalSpinner;
import tech.octopusdragon.intervaltimer.application.util.Resource;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Presents a preset to the user, allows the user to edit its values, and
 * returns a series of intervals corresponding to these values if the user
 * confirms the selection.
 * @author Alex Gill
 *
 */
public class PresetDialog extends Dialog<Interval[]> {
	
	// --- UI components --
	@FXML private GridPane presetGrid;
	
	/**
	 * Constructor - creates a new dialog
	 * @param preset The preset to show and have the user optionally edit
	 */
	public PresetDialog(Preset preset) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PresetDialogPane.fxml"));
		loader.setController(this);
		try {
			setDialogPane(loader.load());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// Set header text
		((Labeled)getDialogPane().getHeader()).setText(preset.toString());
		
		// Add dialog buttons
		getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
		
		// Populate the content grid with preset values
		for (int i = 0; i < preset.getValues().length; i++) {
			Value intervalType = preset.getValues()[i];
			IntervalSpinner spinner = new IntervalSpinner(intervalType.getNumUnits());
			presetGrid.add(new Label(intervalType.getName()), 0, i);
			presetGrid.add(spinner, 1, i);
			presetGrid.add(new Label(intervalType.getUnit().toString()), 2, i);
			// Add alert combo box only if value has alerts
			if (intervalType.hasAlert()) {
				AlertComboBox alertComboBox = new AlertComboBox();
				alertComboBox.setValue(intervalType.getAlert());
				presetGrid.add(alertComboBox, 3, i);
				// Update interval when data is changed
				alertComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
					intervalType.setAlert(newVal);
				});
			}
			
			// Update interval when data is changed
			spinner.valueProperty().addListener((obs, oldVal, newVal) -> {
				intervalType.setNumUnits(newVal);
			});
			
			// Prevent result from being sent if the data is not valid
			okButton.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (!validData()) {
						event.consume();
					}
				}
				private boolean validData() {
					return spinner.getValue() > 0;
				}
			});
		}
		
		// Result converter
		setResultConverter(new Callback<ButtonType, Interval[]>() {
			@Override
			public Interval[] call(ButtonType param) {
				if (param == ButtonType.OK) {
					return preset.intervals();
				}
				return null;
			}
		});
		
		setTitle("Preset");
		((Stage)getDialogPane().getScene().getWindow()).getIcons().add(
				new Image(Resource.ICON.getResourceAsStream()));
	}
}
