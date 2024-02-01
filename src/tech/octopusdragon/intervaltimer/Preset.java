package tech.octopusdragon.intervaltimer;

import java.time.Duration;

/**
 * A pre-made set of intervals
 * @author Alex Gill
 */
public enum Preset {
	
	POMODORO(
		new Value[] {
			new Value("Work", Unit.MINUTES, 25, Alert.DONG_DING),
			new Value("Short Break", Unit.MINUTES, 5, Alert.DING_DONG),
			new Value("Long Break", Unit.MINUTES, 15, Alert.DING_DONG) },
		new int[] {
			0,
			1,
			0,
			1,
			0,
			1,
			0,
			2 }),
	
	BOX_BREATHING(
		new Value[] {
			new Value("Inhale", Unit.SECONDS, 4, Alert.CHIMES_UP),
			new Value("Hold", Unit.SECONDS, 4, Alert.SILENT),
			new Value("Exhale", Unit.SECONDS, 4, Alert.CHIMES_DOWN),
			new Value("Hold", Unit.SECONDS, 4, Alert.SILENT) },
		new int[] {
			0,
			1,
			2,
			3 }),
	
	INTERVAL_TRAINING(
		new Value[] {
			new Value("Exercise", Unit.SECONDS, 20, Alert.DONG_DING),
			new Value("Rest", Unit.SECONDS, 40, Alert.DING_DONG) },
		new int[] {
			0,
			1 });
	
	
	private Value[] values;				// Names and durations of values 
	private int[] intervalPattern;		// Represents intervals based on values
	
	
	/**
	 * Constructor
	 * @param values Names and durations of types of intervals
	 * @param intervalPattern Represents intervals based on values. The integer
	 * for each interval pattern should correspond to the index of the value
	 * starting at 1.
	 */
	private Preset(Value[] values, int[] intervalPattern) {
		this.values = values;
		this.intervalPattern = intervalPattern;
	}
	
	
	/**
	 * @return The names and durations of interval durations
	 */
	public Value[] getValues() {
		return values;
	}
	
	
	/**
	 * @return A list of intervals representing the preset
	 */
	public Interval[] intervals() {
		Interval[] intervals = new Interval[intervalPattern.length];
		for (int i = 0; i < intervals.length; i++) {
			if (values[intervalPattern[i]].hasAlert()) {
				intervals[i] = new Interval(values[intervalPattern[i]].getDuration(),
						values[intervalPattern[i]].getAlert());
			}
			else {
				intervals[i] = new Interval(values[intervalPattern[i]].getDuration());
			}
		}
		return intervals;
	}
	
	
	/**
	 * A part of the preset that can be used to create any number of intervals
	 * based on a set number of durations
	 * @author Alex Gill
	 */
	public static class Value {
		private String name;	// The name of the value
		private Unit unit;		// The unit of time
		private int numUnits;	// The number of units in the duration
		private Alert alert;	// The optional alert
		/**
		 * Constructor without alert
		 * @param name The name of the value
		 * @param unit The unit of time
		 * @param initialDuration The initial units in the duration
		 */
		public Value(String name, Unit unit, int initialNumUnits) {
			this(name, unit, initialNumUnits, null);
		}
		/**
		 * Constructor with alert
		 * @param name The name of the value
		 * @param unit The unit of time
		 * @param initialDuration The initial units in the duration
		 * @param alert The alert of the intervals with this value
		 */
		public Value(String name, Unit unit, int initialNumUnits, Alert alert) {
			this.name = name;
			this.unit = unit;
			numUnits = initialNumUnits;
			this.alert = alert;
		}
		/**
		 * @return The name of the value
		 */
		public String getName() {
			return name;
		}
		/**
		 * @return The unit of time
		 */
		public Unit getUnit() {
			return unit;
		}
		/**
		 * @return The number of units in the duration
		 */
		public int getNumUnits() {
			return numUnits;
		}
		/**
		 * Sets the number of units in the duration
		 * @param duration The number of units to set
		 */
		public void setNumUnits(int numUnits) {
			this.numUnits = numUnits;
		}
		/**
		 * @return The alert of the intervals with this value
		 */
		public Alert getAlert() {
			return alert;
		}
		/**
		 * Sets the alert
		 * @param alert The alert of the intervals with this value
		 */
		public void setAlert(Alert alert) {
			this.alert = alert;
		}
		/**
		 * @return Whether this value has an alert
		 */
		public boolean hasAlert() {
			return alert != null;
		}
		/**
		 * @return A duration based on the unit of time and the number of units
		 */
		public Duration getDuration() {
			Duration duration;
			switch (unit) {
			case SECONDS:
				duration = Duration.ofSeconds(numUnits);
				break;
			case MINUTES:
				duration = Duration.ofMinutes(numUnits);
				break;
			case HOURS:
				duration = Duration.ofHours(numUnits);
				break;
			default:
				duration = null;
			}
			return duration;
		}
	}
	
	
	/**
	 * The possible units of time the values can represent
	 * @author Alex Gill
	 */
	public static enum Unit {
		SECONDS,
		MINUTES,
		HOURS;

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(this.name().toLowerCase());
			// Replace underscores with spaces
			for (int i = 0; i < sb.length(); i++) {
				if (sb.charAt(i) == '_') {
					sb.replace(i, i + 1, " ");
				}
			}
			// Capitalize
			sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
			for (int i = 0; i < sb.length(); i++) {
				if (i >= 1 && sb.charAt(i - 1) == ' ') {
					sb.replace(i, i + 1, sb.substring(i, i + 1).toUpperCase());
				}
			}
			return sb.toString();
		}
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.name().toLowerCase());
		// Replace underscores with spaces
		for (int i = 0; i < sb.length(); i++) {
			if (sb.charAt(i) == '_') {
				sb.replace(i, i + 1, " ");
			}
		}
		// Capitalize
		sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
		for (int i = 0; i < sb.length(); i++) {
			if (i >= 1 && sb.charAt(i - 1) == ' ') {
				sb.replace(i, i + 1, sb.substring(i, i + 1).toUpperCase());
			}
		}
		return sb.toString();
	}
}
