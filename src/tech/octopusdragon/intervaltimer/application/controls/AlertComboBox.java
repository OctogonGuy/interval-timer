package tech.octopusdragon.intervaltimer.application.controls;

import java.util.Collection;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;

import tech.octopusdragon.intervaltimer.Alert;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * A combo box with alert options as well as a preview button that can play an
 * alert
 * @author Alex Gill
 *
 */
@SuppressWarnings("restriction")
public class AlertComboBox extends ComboBox<Alert> {
	
	/**
	 * Creates an alert combo box with all the alerts
	 */
	public AlertComboBox() {
		this(Alert.values());
	}
	
	
	/**
	 * Creates an alert combo box with the specified items
	 * @param items The items to have in the combo box
	 */
	public AlertComboBox(Collection<? extends Alert> items) {
		this(items.toArray(new Alert[items.size()]));
	}
	
	
	/**
	 * Creates an alert combo box with the specified items
	 * @param items The items to have in the combo box
	 */
	public AlertComboBox(Alert... items) {
		super(FXCollections.observableArrayList(items));
		
		setCellFactory(new Callback<ListView<Alert>, ListCell<Alert>>() {
			@Override
			public ListCell<Alert> call(ListView<Alert> list) {
				AlertCell alertCell = new AlertCell();
				return alertCell;
			}
		});
		
		setSkin(new AlertListSkin(this));
	}
	
	
	/**
	 * Allows the alert list items to have a preview button beside them
	 * @author Alex Gill
	 *
	 */
	private class AlertCell extends ListCell<Alert> {
		@Override
		public void updateItem(Alert alert, boolean empty) {
			super.updateItem(alert, empty);
			if (alert != null && !empty) {
				setText(alert.toString());
				Button previewButton = new Button("🔔");
				previewButton.getStyleClass().add("alert-button");
				previewButton.setOnAction(event -> {
					alert.play();
				});
				setGraphic(previewButton);
			}
		}
	}
	
	
	/**
	 * Prevents the combo box list view from closing when clicking on the
	 * preview button
	 * @author Alex Gill
	 *
	 */
	private class AlertListSkin extends ComboBoxListViewSkin<Alert> {
		public AlertListSkin(ComboBox<Alert> comboBox) {
			super(comboBox);
			comboBox.getSelectionModel().selectedItemProperty().addListener(
					(obs, oldVal, newVal) -> {
				comboBox.hide();
			});
			comboBox.setOnMouseClicked(event -> {
				comboBox.hide();
			});
		}
	}
}
