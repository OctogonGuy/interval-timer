package tech.octopusdragon.intervaltimer;

import java.io.Serializable;
import java.time.Duration;

/**
 * Represents an interval of time with a duration of hours, minutes, and
 * seconds, and an optional alert.
 * @author Alex Gill
 *
 */
public class Interval implements Serializable {
	private static final long serialVersionUID = -215862572180250661L;
	
	// --- Variables ---
	private Duration duration;		// The duration of the interval
	private Alert alert;			// Optional alert
	
	/**
	 * Constructs a new interval with no duration
	 */
	public Interval() {
		this(Duration.ZERO);
	}
	
	/**
	 * Constructs a new interval
	 * @param h The number of hours in the interval
	 * @param m The number of minutes in the interval
	 * @param s The number of seconds in the interval
	 */
	public Interval(int h, int m, int s) {
		this(Duration.ofHours(h).plusMinutes(m).plusSeconds(s));
	}
	
	/**
	 * Constructs a new interval
	 * @param d The duration of the interval
	 */
	public Interval(Duration d) {
		duration = d;
	}
	
	/**
	 * Constructs a new interval with an alert
	 * @param h The number of hours in the interval
	 * @param m The number of minutes in the interval
	 * @param s The number of seconds in the interval
	 * @param alert The alert of the interval
	 */
	public Interval(int h, int m, int s, Alert alert) {
		this(Duration.ofHours(h).plusMinutes(m).plusSeconds(s), alert);
	}
	
	/**
	 * Constructs a new interval with an alert
	 * @param d The duration of the interval
	 * @param a The alert of the interval
	 */
	public Interval(Duration d, Alert a) {
		duration = d;
		alert = a;
	}
	
	/**
	 * Sets the duration of the interval
	 * @param h The number of hours in the interval
	 * @param m The number of minutes in the interval
	 * @param s The number of seconds in the interval
	 */
	public void setDuration(int h, int m, int s) {
		setDuration(Duration.ofHours(h).plusMinutes(m).plusSeconds(s));
	}
	
	/**
	 * Sets the duration of the interval
	 * @param d The duration of the interval
	 */
	public void setDuration(Duration d) {
		duration = d;
	}
	
	/**
	 * @return The duration of the interval
	 */
	public Duration getDuration() {
		return duration;
	}
	
	/**
	 * Sets an alert for the interval
	 * @param a The alert of the interval
	 */
	public void setAlert(Alert a) {
		alert = a;
	}
	
	/**
	 * @return The alert of the interval
	 */
	public Alert getAlert() {
		return alert;
	}
	
	/**
	 * @return Whether the interval has an alert
	 */
	public boolean hasAlert() {
		return alert != null;
	}

	/**
	 * @return the hours
	 */
	public int hours() {
		return (int)duration.toHours();
	}

	/**
	 * @return the minutes
	 */
	public int minutes() {
		return (int)(duration.toMinutes() % 60);
	}

	/**
	 * @return the seconds
	 */
	public int seconds() {
		return (int)(duration.getSeconds() % 60);
	}
	
	@Override
	public String toString() {
		return 	hours() + " hours\n" +
				minutes() + " minutes\n" +
				seconds() + " seconds";
	}
	
}
