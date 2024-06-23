import State from "../constants/State";
import Alert from "../constants/Alert";
import Interval from "./Interval";

const REFRESH_RATE: number = 12;  // Timer will refresh every _ ms

export default class {
  intervals: Interval[];
  index: number;        // Current interval index
  alert?: Alert;
  task: Function;       // A task that updates some variable to trigger UI
  repeat: boolean;
  startTime: number;    // Time when interval started
  stopTime: number;     // Time since last stopped
  stopDuration: number; // How long this interval has been paused
  state: string;
  jsInterval: NodeJS.Timeout | undefined;

  constructor(intervals: Interval[], repeat: boolean, task: Function, alert?: Alert) {
    this.intervals = intervals;
    this.repeat = repeat;
    this.alert = alert;
    this.task = task;
    
    this.index = 0;
    this.startTime = 0;
    this.stopTime = 0;
    this.stopDuration = 0;
    this.state = State.INITIALIZED;
  }

  /**
   * Starts the timer
   */
  start() {
    if (this.state === State.RUNNING) return;
    if (this.stopTime) this.stopDuration += Date.now() - this.stopTime;
    this.jsInterval = setInterval(() => this.execute(), REFRESH_RATE);
    if (this.startTime == 0) this.startTime = Date.now();
    this.state = State.RUNNING;
  }

  /**
   * Stop the timer
   */
  stop() {
		if (this.state === State.STOPPED) return;
    clearInterval(this.jsInterval);
    this.stopTime = Date.now();
    this.state = State.STOPPED;
  }

  /**
   * Freezes the timer (for when timer screen is exited)
   */
  freeze() {
		this.stop();
    this.state = State.FROZEN;
  }

  /**
   * Unfreezes the timer (for when timer screen is resumed)
   */
  unfreeze() {
		if (this.state === State.STOPPED) {
      this.stopDuration += Date.now() - this.stopTime;
      this.stopTime = Date.now();
      return;
    }
    this.start();
  }

  /**
   * Ends the timer
   */
  end() {
		this.stop();
    this.state = State.OVER;
  }

  /**
   * Toggles play/pause
   */
  toggle() {
    if (this.state === State.RUNNING)
      this.stop();
    else if (this.state === State.STOPPED)
      this.start();
  }

  /**
	 * Goes back and starts the previous interval. Does nothing if on the first
	 * interval and repeat is off. If repeat is on, loop around to the last
	 * interval.
	 */
  previous() {
    if (this.state === State.OVER ||
      (!this.repeat && this.index == this.intervals.length - 1)) return;
    if (this.repeat && this.index == 0) {
      this.index = this.intervals.length - 1;
    }
    else {
      this.index--;
    }
    this.startTime = Date.now();
    this.stopTime = 0;
    this.stopDuration = 0;
    if (this.state === State.STOPPED) {
      this.start();
    }
  }

  /**
	 * Skips to the next interval. Does nothing if repeat is off and it has
	 * finished the last interval. If repeat is on, loop back to first interval.
	 */
  next() {
    if (!this.repeat && this.index == this.intervals.length - 1) return;
    if (this.repeat && this.index == this.intervals.length - 1) {
      this.index = 0;
    }
    else {
      this.index++;
    }
    this.startTime = Date.now();
    this.stopTime = 0;
    this.stopDuration = 0;
  }

  /**
	 * Restarts the from the first interval
	 */
  restartTimer() {
    this.index = 0;
    this.startTime = Date.now();
    this.stopTime = 0;
    this.stopDuration = 0;
  }

  /**
	 * Restarts the current interval
	 */
  restartInterval() {
    this.startTime = Date.now();
    this.stopTime = 0;
    this.stopDuration = 0;
  }

  /**
   * @returns The duration of the current interval in milliseconds
   */
  duration() {
    return this.intervals[this.index].duration;
  }

  /**
   * @returns The amount of time passed in milliseconds
   */
  timePassed() {
    if (this.state === State.OVER)
      return this.duration();
    return Date.now() - this.startTime - this.stopDuration;
  }

  /**
   * @returns The amount of time left in milliseconds
   */
  timeLeft() {
    return this.duration() - this.timePassed();
  }

  /**
   * @returns The time left as a string in the format: 'hh:mm:ss'
   */
  timeLeftStr() {
    if (this.state === State.OVER)
      return "Time's up!";
    const timeLeft = this.timeLeft();
    const hoursLeft: number = Math.floor(timeLeft / (1000 * 3600));
    const minutesLeft: number = Math.floor(timeLeft / (1000 * 60 ) - hoursLeft * 60);
    const secondsLeft: number = Math.floor(timeLeft / (1000) - hoursLeft * 3600 - minutesLeft * 60);
    const h: string = String(hoursLeft).padStart(2, '0');
    const m: string = String(minutesLeft).padStart(2, '0');
    const s: string = String(secondsLeft).padStart(2, '0');
    return `${h}:${m}:${s}`;
  }

  /**
   * @returns Proportion of time passed in the current interval (0.0 - 1.0)
   */
  progress() {
    return this.timePassed() / this.duration();
  }
  
  /**
   * The timer action that should execute after every millisecond the timer is
   * active
   */
  private execute() {
    if (this.timePassed() >= this.intervals[this.index].duration) {
      if (this.alert)
        this.alert.play();
      else
        this.intervals[this.index].alert.play();
      this.index++;
      if (this.index >= this.intervals.length) this.index = 0;
      this.startTime = Date.now();
      this.stopTime = 0;
      this.stopDuration = 0;
      if (!this.repeat) this.end();
    }
    // Execute provided task
    this.task(Date.now());
  }
}