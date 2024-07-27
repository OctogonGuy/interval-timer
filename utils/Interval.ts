import Alert from "../constants/Alert";

export default class {
  duration: number;
  alert: Alert;

  constructor(hours=0, minutes=0, seconds=0, duration=0, alert=Alert.CHORDS) {
    // Treat first parameter as duration
    if (duration) {
      this.duration = duration;
    }
    // Treat parameters as h:m:s
    else if (hours || minutes || seconds) {
      this.duration = (hours * 3600 + minutes * 60 + seconds) * 1000;
    }
    // Default
    else {
      this.duration = 0;
    }
    this.alert = alert;
  }
  
  hours() {
    return Math.floor(this.duration / (1000 * 3600));
  }
  
  minutes() {
    return Math.floor(this.duration / (1000 * 60) - this.hours() * 60);
  }
  
  seconds() {
    return Math.floor(this.duration / (1000) - this.hours() * 3600 - this.minutes() * 60);
  }
}