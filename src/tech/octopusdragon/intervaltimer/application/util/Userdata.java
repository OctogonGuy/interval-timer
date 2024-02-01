package tech.octopusdragon.intervaltimer.application.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import tech.octopusdragon.intervaltimer.Alert;
import tech.octopusdragon.intervaltimer.Interval;

/**
 * Loads and saves user data for the interval timer application
 * @author Alex Gill
 *
 */
public class Userdata {
	
	// --- Constants ---
	// Userdata folder name
	private static final String USERDATA_FOLDERNAME = "userdata";
	// Userdata filename
	private static final String FILENAME = "interval_timer_userdata.ser";
	
	// --- Variables ---
	private static Properties properties;	// The userdata
	
	
	static {
		properties = loadProperties();
	}

	
	/**
	 * Tries to load userdata from the userdata file. If successful, it returns
	 * a new object with the userdata loaded. If unsuccessful, it returns a new
	 * object with default values.
	 * @return the loaded userdata
	 */
	private static Properties loadProperties() {
		Properties object = null;
		try {
			FileInputStream fileIn = new FileInputStream(USERDATA_FOLDERNAME + "/" + FILENAME);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			object = (Properties) objectIn.readObject();
			objectIn.close();
		} catch (IOException | ClassNotFoundException e) {
			object = new Properties();
		}
		
		return object;
	}
	
	
	/**
	 * Saves this userdata to the userdata file
	 */
	private static void saveProperties() {
		// Make sure the folder exists
		File folder = new File(USERDATA_FOLDERNAME);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		try {
			FileOutputStream fileOut = new FileOutputStream(USERDATA_FOLDERNAME + "/" + FILENAME);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(properties);
			objectOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Reads and returns a property value
	 * @param key The property key
	 * @return The property value or null if not found
	 */
	public static String readProperty(String key) {
		return properties.getProperty(key);
	}
	
	
	/**
	 * Reads and returns a property value or a default value if not found
	 * @param key The property key
	 * @param defaultValue A property value to write if the file is not found or
	 * 	the property is not found
	 * @return The property value or a default value if not found
	 */
	public static String readProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	
	
	/**
	 * Reads and returns a list of intervals saved to the userdata
	 * @return
	 */
	public static Interval[] readIntervals() {
		int numIntervals = Integer.parseInt(readProperty("num-intervals", Integer.toString(0)));
		if (numIntervals == 0) {
			return new Interval[] {new Interval()};
		}
		List<Interval> intervals = new ArrayList<>();
		for (int i = 0; i < numIntervals; i++) {
			String intervalId = String.format("interval-%d", i);
			String intervalHours = String.format("%s-hours", intervalId);
			String intervalMinutes = String.format("%s-minutes", intervalId);
			String intervalSeconds = String.format("%s-seconds", intervalId);
			String intervalAlert = String.format("%s-alert", intervalId);
			int h = Integer.parseInt(readProperty(intervalHours));
			int m = Integer.parseInt(readProperty(intervalMinutes));
			int s = Integer.parseInt(readProperty(intervalSeconds));
			try {
				Alert alert = Alert.valueOf(readProperty(intervalAlert));
				intervals.add(new Interval(h, m, s, alert));
			} catch (NullPointerException e) {
				intervals.add(new Interval(h, m, s));
			}
		}
		return intervals.toArray(new Interval[numIntervals]);
	}
	
	
	/**
	 * Writes a property
	 * @param key The property key
	 * @param value The property value
	 */
	public static void writeProperty(String key, String value) {
		properties.setProperty(key, value);
		saveProperties();
	}
	
	
	/**
	 * Writes an interval as a property
	 * @param index The index of the interval
	 * @param interval The interval
	 */
	public static void writeInterval(int index, Interval interval) {
		String intervalId = String.format("interval-%d", index);
		String intervalHours = String.format("%s-hours", intervalId);
		String intervalMinutes = String.format("%s-minutes", intervalId);
		String intervalSeconds = String.format("%s-seconds", intervalId);
		String intervalAlert = String.format("%s-alert", intervalId);
		writeProperty(intervalHours, Integer.toString(interval.hours()));
		writeProperty(intervalMinutes, Integer.toString(interval.minutes()));
		writeProperty(intervalSeconds, Integer.toString(interval.seconds()));
		if (interval.hasAlert()) {
			writeProperty(intervalAlert, interval.getAlert().name());
		}
	}

}
