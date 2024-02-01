package tech.octopusdragon.intervaltimer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A timer that goes off repeatedly after one or more specified intervals.
 * @author Alex Gill
 *
 */
public class IntervalTimer extends Timer {
	
	// --- Global constants ---
	public static final Duration UPDATE_RATE = Duration.ofMillis(10);
	
	
	// --- Instance variables ---
	private Interval[] intervals;	// The intervals in the timer
	private int curIntervalIndex;	// The index of the current interval
	private Alert alert;			// The alert
	private boolean repeat;			// Whether the timer repeats
	private IntervalTimerTask curTask;	// The currently running main task
	private List<TimerTask> tasks;	// Additional tasks to execute on count down
	private long startNanoTime;		// Last start time of interval in nanos
	private Duration timePassedSinceStop;	// Time passed since last stop
	private boolean running;		// Whether the timer is running (not paused)
	private boolean frozen;			// Whether the timer is temporarily paused
	private boolean canceled;		// Whether the timer is canceled
	
	
	/**
	 * The constructor initializes a new interval timer with a task to do after
	 * each second. There is no alert in this one and therefore uses the alerts
	 * of each interval instead
	 * @param repeat Whether the timer repeats after all intervals are finished
	 * @param intervals The intervals
	 */
	public IntervalTimer(boolean repeat, Interval... intervals) {
		this(null, repeat, intervals);
	}
	
	
	/**
	 * The constructor initializes a new interval timer with a task to do after
	 * each second.
	 * @param alert The alert to sound after each interval
	 * @param repeat Whether the timer repeats after all intervals are finished
	 * @param intervals The intervals
	 */
	public IntervalTimer(Alert alert, boolean repeat, Interval... intervals) {
		super(true);
		
		tasks = new ArrayList<TimerTask>();
		canceled = false;
		running = false;
		
		this.alert = alert;
		this.repeat = repeat;
		
		// Initialize the interval delays.
		this.intervals = new Interval[intervals.length];
		for (int i = 0; i < intervals.length; i++) {
			this.intervals[i] = intervals[i];
		}
		curIntervalIndex = 0;
		timePassedSinceStop = Duration.ZERO;
		
		// Create a new Timer that executes its task after every second.
		start();
	}
	
	
	/**
	 * Starts the timer
	 */
	public void start() {
		if (canceled || (running && !frozen)) return;
		startNanoTime = System.nanoTime();
		curTask = new IntervalTimerTask();
		this.schedule(curTask, UPDATE_RATE.toMillis(), UPDATE_RATE.toMillis());
		running = true;
		frozen = false;
	}
	
	
	/**
	 * Pauses the timer
	 */
	public void stop() {
		if (canceled || !running || frozen) return;
		timePassedSinceStop = timePassed();
		curTask.cancel();
		running = false;
	}
	
	
	/**
	 * Pauses the timer without updating the running variable
	 */
	public void freeze() {
		stop();
		running = true;
		frozen = true;
	}
	
	
	@Override
	public void cancel() {
		stop();
		super.cancel();
		canceled = true;
	}
	
	
	/**
	 * Pauses the timer if running. Resumes the timer if paused.
	 */
	public void togglePause() {
		if (running) {
			stop();
		}
		else {
			start();
		}
	}
	
	
	/**
	 * Goes back and starts the previous interval. Does nothing if on the first
	 * interval and repeat is off. If repeat is on, loop around to the last
	 * interval.
	 */
	public void previous() {
		if (canceled) return;
		if (!repeat && curIntervalIndex == 0) {
			return;
		}
		else if (repeat && curIntervalIndex == 0) {
			curIntervalIndex = intervals.length - 1;
		}
		else {
			curIntervalIndex--;
		}
		timePassedSinceStop = Duration.ZERO;
		startNanoTime = System.nanoTime();
	}
	
	
	/**
	 * Skips to the next interval. Does nothing if repeat is off and it has
	 * finished the last interval. If repeat is on, loop back to first interval.
	 */
	public void next() {
		if (canceled) return;
		if (!repeat && curIntervalIndex == intervals.length - 1) {
			return;
		}
		else if (repeat && curIntervalIndex == intervals.length - 1) {
			curIntervalIndex = 0;
		}
		else {
			curIntervalIndex++;
		}
		timePassedSinceStop = Duration.ZERO;
		startNanoTime = System.nanoTime();
	}
	
	
	/**
	 * Restarts the from the first interval
	 */
	public void restart() {
		if (canceled) return;
		curIntervalIndex = 0;
		timePassedSinceStop = Duration.ZERO;
		startNanoTime = System.nanoTime();
	}
	
	
	/**
	 * Restarts the current interval
	 */
	public void restartInterval() {
		if (canceled) return;
		timePassedSinceStop = Duration.ZERO;
		startNanoTime = System.nanoTime();
	}
	
	
	/**
	 * Task to execute each second
	 * @author Alex Gill
	 *
	 */
	private void countdown() {
		
		// Get the time left
		Duration timeLeft = timeLeft();
		
		// If zero seconds are left, increment the current interval
		// index, reset the time left to the corresponding delay, and
		// play the alert.
		if (timeLeft.isZero() || timeLeft.isNegative()) {
			Alert intervalAlert = curInterval().getAlert();
			
			if (!repeat && curIntervalIndex == intervals.length - 1) {
				cancel();
			}
			else {
				next();
			}
			
			// Play alert on a new thread because it otherwise causes a short
			// freeze
			new Thread(() -> {
				if (alert != null) {
					alert.play();
				}
				else if (intervalAlert != null) {
					intervalAlert.play();
				}
			}).start();
		}
	}
	
	
	/**
	 * Adds a new timer task to complete each time the timer counts down
	 * @param newTask The timer task
	 */
	public void addTask(TimerTask newTask) {
		tasks.add(newTask);
	}
	
	
	/**
	 * @return the place of the current interval
	 */
	public Interval[] getIntervals() {
		return intervals;
	}
	
	
	/**
	 * @return the place of the current interval
	 */
	public Interval curInterval() {
		return intervals[curIntervalIndex];
	}
	
	
	/**
	 * @return the place of the current interval
	 */
	public int curIntervalIndex() {
		return curIntervalIndex;
	}
	
	
	/**
	 * @return the total number of intervals
	 */
	public int numIntervals() {
		return intervals.length;
	}
	
	
	/**
	 * @return the current interval number over total number of intervals
	 */
	public String intervalNumberString() {
		return (curIntervalIndex + 1) + "/" + intervals.length;
	}
	
	
	/**
	 * @return the alert
	 */
	public Alert getAlert() {
		return alert;
	}	
	
	/**
	 * @return whether the timer repeats
	 */
	public boolean isRepeat() {
		return repeat;
	}
	
	
	/**
	 * @return the time left in the current interval
	 */
	public Duration timeLeft() {
		return curInterval().getDuration().
				minus(timePassedSinceStop).
				minusNanos(running ? System.nanoTime() - startNanoTime : 0);
	}
	
	
	/**
	 * @return the number of hours left
	 */
	public int hoursLeft() {
		return (int)Math.floor(timeLeft().toHours());
	}
	
	
	/**
	 * @return the number of minutes left excluding hours
	 */
	public int minutesLeft() {
		return (int)Math.floor(timeLeft().toMinutes() % 60);
	}
	
	
	/**
	 * @return the number of seconds left excluding hours and minutes
	 */
	public int secondsLeft() {
		return (int)Math.floor(timeLeft().getSeconds() % 60);
	}
	
	
	/**
	 * Returns a formatted string representation of the time left in the current
	 * interval.
	 * @return The time left
	 */
	public String timeLeftString() {
		
		// If the time is up, indicate that
		if (canceled) {
			return "Time's up!";
		}
		
		StringBuilder sb = new StringBuilder();
		
		// If there is more than an hour left, add the hour as well as minutes
		// and seconds
		if (hoursLeft() >= 1) {
			sb.append(String.format("%d:", hoursLeft()));
			// Add the minutes and seconds both with two zeros
			sb.append(String.format("%02d:%02d", minutesLeft(), secondsLeft()));
		}
		
		// Otherwise, just add minutes and seconds
		else {
			// Add the minutes with one zero and seconds with two zeros
			sb.append(String.format("%01d:%02d", minutesLeft(), secondsLeft()));
		}
		
		return sb.toString();
	}
	
	
	/**
	 * @return the time passed in the current interval
	 */
	public Duration timePassed() {
		return timePassedSinceStop.
				plusNanos(running ? System.nanoTime() - startNanoTime : 0);
	}
	
	
	/**
	 * @return the number of hours passed
	 */
	public int hoursPassed() {
		return (int)Math.floor(timePassed().toHours());
	}
	
	
	/**
	 * @return the number of minutes passed excluding hours
	 */
	public int minutesPassed() {
		return (int)Math.floor(timePassed().toMinutes() % 60);
	}
	
	
	/**
	 * @return the number of seconds passed excluding hours and minutes
	 */
	public int secondsPassed() {
		return (int)Math.floor(timePassed().getSeconds() % 60);
	}


	/**
	 * @return whether the timer is running (not paused)
	 */
	public boolean isRunning() {
		return running;
	}


	/**
	 * @return whether the timer is frozen
	 */
	public boolean isFrozen() {
		return running;
	}


	/**
	 * @return whether the timer has been canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	
	
	/**
	 * Task to run every second
	 * @author Alex Gill
	 *
	 */
	private class IntervalTimerTask extends TimerTask {
		@Override
		public void run() {
			countdown();
			for (TimerTask task : tasks) {
				task.run();
			}
		}
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Alert: " + alert);
		sb.append("\n");
		sb.append("Repeat: " + repeat);
		sb.append("\n");
		sb.append("Intervals: " + intervals.toString());
		sb.append("\n");
		sb.append("Current interval index: " + curIntervalIndex);
		sb.append("\n");
		sb.append("Time left: " + timeLeft() + " " + UPDATE_RATE);
		sb.append("\n");
		sb.append("Start time in nanos: " + startNanoTime);
		sb.append("\n");
		sb.append("Is canceled: " + canceled);
		sb.append("\n");
		sb.append("Attached tasks: " + tasks);
		return sb.toString();
	}
}
