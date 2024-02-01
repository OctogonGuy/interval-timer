package tech.octopusdragon.intervaltimer.application.controls;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;

/**
 * A spinner for a duration that contributes to the interval length
 * @author Alex Gill
 *
 */
public class IntervalSpinner extends Spinner<Integer> {
	
	private static final int MAX_VALUE = 99;	// Maximum value of spinners
	
	/**
	 * Constructs a new spinner with no initial value
	 */
	public IntervalSpinner() {
		this(0);
	}
	
	/**
	 * Constructs a new spinner
	 * @param initialValue The initial value
	 */
	public IntervalSpinner(int initialValue) {
		// Create a formatted spinner that prevents the timer being started
		// when all durations in the row are 0, prevents values from being
		// changed to non-numbers, and commits values when focus is lost.
		super(new IntervalSpinnerValueFactory(0, MAX_VALUE, initialValue));
		this.setEditable(true);
		this.getStyleClass().add(STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
		this.getEditor().textProperty().addListener(
				new EditorAlphaListener(this));
		this.focusedProperty().addListener(
				new SpinnerFocusLostListener(this));
	}
	

	/**
	 * Sets the minimum, maximum, and initial value for the spinner as well as
	 * formatting the spinner to display two digits at all times
	 * @author Alex Gill
	 */
	private static class IntervalSpinnerValueFactory
			extends SpinnerValueFactory.IntegerSpinnerValueFactory {
		public IntervalSpinnerValueFactory(int min, int max, int initialValue) {
			super(min, max, initialValue);
			setConverter(new StringConverter<Integer>() {
				@Override
				public String toString(Integer object) {
					return String.format("%02d", object);
				}
				@Override
				public Integer fromString(String string) {
					return Integer.parseInt(string);
				}
			});
		}
	}

	
	/**
	 * Commits a spinner's typed in value upon focus being lost. This is to help
	 * bypass Enter having to be pressed.
	 * @author Alex Gill
	 */
	private static class SpinnerFocusLostListener implements ChangeListener<Boolean> {
		private IntervalSpinner spinner;	// Spinner this is being applied to
		public SpinnerFocusLostListener(IntervalSpinner spinner) {
			this.spinner = spinner;
		}
		@Override
		public void changed(ObservableValue<? extends Boolean> observable,
				Boolean oldValue, Boolean newValue) {
			if (newValue == false) {
			    if (!spinner.isEditable()) return;
			    String text = spinner.getEditor().getText();
			    SpinnerValueFactory<Integer> valueFactory = spinner.getValueFactory();
			    if (valueFactory != null) {
			        StringConverter<Integer> converter = valueFactory.getConverter();
			        if (converter != null) {
			        	if (text.isEmpty()) {
			        		text = ((IntervalSpinnerValueFactory)spinner.getValueFactory()).
			        				getConverter().toString(0);
			        		spinner.getEditor().setText(text);
			        	}
				        Integer value = converter.fromString(text);
			            valueFactory.setValue(value);
			        }
			    }
			}
		}
	}
	
	
	/**
	 * Prevents a spinner's value from being changed if it does not contain a
	 * parsable integer or an integer that is too long.
	 * @author Alex Gill
	 */
	private static class EditorAlphaListener implements ChangeListener<String> {
		private Spinner<Integer> spinner;	// Spinner this is being applied to
		public EditorAlphaListener(Spinner<Integer> spinner) {
			this.spinner = spinner;
		}
		@Override
		public void changed(
				ObservableValue<? extends String> observable,
				String oldValue,
				String newValue) {
			try {
				if (!newValue.isEmpty()) {
					Integer.parseInt(newValue);
				}
				if (newValue.length() > Integer.toString(MAX_VALUE).length()) {
					newValue = newValue.substring(1);
				}
				spinner.getEditor().setText(newValue);
			} catch (NumberFormatException e) {
				spinner.getEditor().setText(oldValue);
			}
		}
	}
}