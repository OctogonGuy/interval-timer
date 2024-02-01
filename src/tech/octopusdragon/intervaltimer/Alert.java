package tech.octopusdragon.intervaltimer;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

/**
 * The Alert class is used to encapsulate the alerts that can be used.
 * @author Alex Gill
 *
 */

public enum Alert {
	
	TELEPHONE("telephone.mid"),
	CHIMES_UP("chimes_up.mid"),
	CHIMES_DOWN("chimes_down.mid"),
	WHISTLE("whistle.mid"),
	WOODBLOCK("woodblock.mid"),
	XYLOPHONE("xylophone.mid"),
	OCARINA("ocarina.mid"),
	GRANDFATHER_1("grandfather_clock1.mid"),
	GRANDFATHER_2("grandfather_clock2.mid"),
	DING("ding.mid"),
	DING_DONG("ding_dong.mid"),
	DONG_DING("dong_ding.mid"),
	CYMBAL("cymbal.mid"),
	APITO("apito.mid"),
	CHORDS("chords.mid"),
	OBATALA("obatala.mid"),
	BRAVE_WIND("brave_wind.mid"),
	OMOMUKI("omomuki.mid"),
	SILENT();
	
	// --- Constants ---
	private final static String ALERT_DIR = "resources/alerts/";
	
	// --- Variables ---
	private Sequencer player;		// The Sequencer for the Alert
	private String filename;		// The filename of the alert audio file
	
	/**
	 * Instantiates a new silent Alert
	 */
	private Alert() {
	}
	
	
	/**
	 * Instantiates a new Alert
	 * @param The filename of the alert
	 */
	private Alert(String filename) {
		this.filename = ALERT_DIR + filename;
	}
	
	
	/**
	 * Plays the alert sound
	 */
	public void play() {
		if (filename == null) return;
		
		// Create a new Sequencer and set its Sequence to the Alert.
		try {
			if (player != null)
				player.close();
			player = MidiSystem.getSequencer();
			player.addMetaEventListener(new EndOfSequenceListener());
			player.open();
			player.setSequence(getClass().getClassLoader().getResourceAsStream(
					filename));
			player.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Listener for the end of the Sequence
	 * @author Alex Gill
	 *
	 */
	class EndOfSequenceListener implements MetaEventListener {
		@Override
		public void meta(MetaMessage message) {
			
			// This code will execute once the end of the Sequence is reached
			if (message.getType() == 47) {
				player.close();
			}
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
