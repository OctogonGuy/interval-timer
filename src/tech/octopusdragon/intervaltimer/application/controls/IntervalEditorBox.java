package tech.octopusdragon.intervaltimer.application.controls;

import java.io.IOException;

import tech.octopusdragon.intervaltimer.Alert;
import tech.octopusdragon.intervaltimer.Interval;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

/**
 * A graphic that allows the user to create an interval for the interval timer
 * @author Alex Gill
 *
 */
public class IntervalEditorBox extends HBox {
	
	// --- UI components ---
	@FXML private IntervalSpinner hourSpinner;
	@FXML private IntervalSpinner minuteSpinner;
	@FXML private IntervalSpinner secondSpinner;
	@FXML private AlertComboBox alertComboBox;
	
	// --- Variables ---
	// The interval this editor represents
	private ObjectProperty<Interval> intervalProperty;
	private BooleanProperty alertDisabledProperty;
	
	/**
	 * Constructor that starts with an empty interval and alert enabled
	 */
	public IntervalEditorBox() {
		this(new Interval(), false);
	}
	
	
	/**
	 * Constructor with an interval and alert enabled
	 */
	public IntervalEditorBox(Interval interval) {
		this(interval, false);
	}
	
	
	/**
	 * Constructor that starts with an empty interval and whether or not to
	 * disable the alert
	 */
	public IntervalEditorBox(boolean alertDisabled) {
		this(new Interval(), alertDisabled);
	}
	
	
	/**
	 * Constructor with an interval and whether or not to disable the alert
	 */
	public IntervalEditorBox(Interval interval, boolean alertDisabled) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("IntervalEditorBox.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		intervalProperty = new SimpleObjectProperty<>(interval);
		alertDisabledProperty = new SimpleBooleanProperty(alertDisabled);
		
		build();
	}
	
	
	/**
	 * Builds the UI
	 */
	public void build() {
		hourSpinner.getValueFactory().setValue(getInterval().hours());
		hourSpinner.valueProperty().addListener(new ValueChangeListener());
		minuteSpinner.getValueFactory().setValue(getInterval().minutes());
		minuteSpinner.valueProperty().addListener(new ValueChangeListener());
		secondSpinner.getValueFactory().setValue(getInterval().seconds());
		secondSpinner.valueProperty().addListener(new ValueChangeListener());
		alertComboBox.setValue(getInterval().getAlert());
		alertComboBox.valueProperty().addListener(new ValueChangeListener());
		alertComboBox.visibleProperty().bind(alertDisabledProperty.not());
		alertComboBox.visibleProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				this.getChildren().add(alertComboBox);
			}
			else {
				this.getChildren().remove(alertComboBox);
			}
		});
	}
	
	
	/**
	 * Sets the value of the hour spinner
	 * @param h The hour to set
	 */
	public void setHours(int h) {
		hourSpinner.getValueFactory().setValue(h);
	}
	
	
	/**
	 * @return The value of the hour spinner
	 */
	public int getHours() {
		return hourSpinner.getValue();
	}
	
	
	/**
	 * Sets the value of the minute spinner
	 * @param m The minute to set
	 */
	public void setMinutes(int m) {
		minuteSpinner.getValueFactory().setValue(m);
	}
	
	
	/**
	 * @return The value of the minute spinner
	 */
	public int getMinutes() {
		return minuteSpinner.getValue();
	}
	
	
	/**
	 * Sets the value of the second spinner
	 * @param s The second to set
	 */
	public void setSeconds(int s) {
		secondSpinner.getValueFactory().setValue(s);
	}
	
	
	/**
	 * @return The value of the second spinner
	 */
	public int getSeconds() {
		return secondSpinner.getValue();
	}
	
	
	/**
	 * Sets the value of the alert combo box
	 * @param alert The alert to set
	 */
	public void setAlert(Alert alert) {
		alertComboBox.setValue(alert);
	}
	
	
	/**
	 * @return The value of the alert combo box
	 */
	public Alert getAlert() {
		return alertComboBox.getValue();
	}
	
	
	/**
	 * @return The property of the interval this editor represents
	 */
	public ObjectProperty<Interval> intervalProperty() {
		return intervalProperty;
	}
	
	/**
	 * @return The interval this editor represents
	 */
	public Interval getInterval() {
		return intervalProperty.get();
	}
	
	/**
	 * Sets the value for the interval property
	 * @param interval The interval this editor is supposed to represent
	 */
	public void setInterval(Interval interval) {
		intervalProperty.set(interval);
	}
	
	
	/**
	 * @return The property of whether the interval's alert is disabled
	 */
	public BooleanProperty alertDisabledProperty() {
		return alertDisabledProperty;
	}
	
	/**
	 * @return Whether or not the alert is disabled
	 */
	public boolean alertIsDisabled() {
		return alertDisabledProperty.get();
	}
	
	/**
	 * Sets the value for the alert disabled property
	 * @param alertDisabled Whether or not to disable the alert
	 */
	public void disableAlert(boolean disableAlert) {
		alertDisabledProperty.set(disableAlert);
	}
	
	
	/**
	 * Listens to changes to the values of the interval spinners
	 * @author Alex Gill
	 */
	private class ValueChangeListener implements ChangeListener<Object> {
		@Override
		public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
			// Update the interval
			setInterval(new Interval(
					hourSpinner.getValue(),
					minuteSpinner.getValue(),
					secondSpinner.getValue(),
					!alertIsDisabled() ? alertComboBox.getValue() : null));
		}
	}
}
