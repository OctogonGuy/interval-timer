import Interval from "../utils/Interval";
import Alert from "./Alert";
import Unit from "./Unit";

export class PresetValue {
  name: string;
  unit: string;
  numUnits: number;
  alert?: Alert;

  constructor(name: string, unit: string, initialNumUnits: number, alert?: Alert) {
    this.name = name;
    this.unit = unit;
    this.numUnits = initialNumUnits;
    this.alert = alert;
  }

  duration() {
    let duration = 0;
    switch (this.unit) {
      case Unit.HOURS:
        duration = this.numUnits * 3600;
        break;
      case Unit.MINUTES:
        duration = this.numUnits * 60;
        break;
      case Unit.SECONDS:
        duration = this.numUnits;
        break;
    }
    duration *= 1000;
    return duration;
  }
}

export default class Preset {

	static values: Preset[] = [];
	static map: {[id: string]: Preset} = {};
	
	static POMODORO = new Preset("Pomodoro",
		[
			new PresetValue("Work", Unit.MINUTES, 25, Alert.DONG_DING),
			new PresetValue("Short Break", Unit.MINUTES, 5, Alert.DING_DONG),
			new PresetValue("Long Break", Unit.MINUTES, 15, Alert.DING_DONG) ],
		[
			0,
			1,
			0,
			1,
			0,
			1,
			0,
			2 ]);
	
	static BOX_BREATHING = new Preset("Box Breathing",
		[
			new PresetValue("Inhale", Unit.SECONDS, 4, Alert.CHIMES_UP),
			new PresetValue("Hold", Unit.SECONDS, 4, Alert.SILENT),
			new PresetValue("Exhale", Unit.SECONDS, 4, Alert.CHIMES_DOWN),
			new PresetValue("Hold", Unit.SECONDS, 4, Alert.SILENT) ],
		[
			0,
			1,
			2,
			3 ]);
	
	static INTERVAL_TRAINING = new Preset("Interval Training",
		[
			new PresetValue("Exercise", Unit.SECONDS, 20, Alert.DONG_DING),
			new PresetValue("Rest", Unit.SECONDS, 40, Alert.DING_DONG) ],
		[
			0,
			1 ]);
  
  name: string;
	presetValues: PresetValue[];				// Names and durations of values 
	intervalPattern: number[];		// Represents intervals based on values

  private constructor(name: string, presetValues: PresetValue[], intervalPattern: number[]) {
    this.name = name;
		this.presetValues = presetValues;
		this.intervalPattern = intervalPattern;
		Preset.values.push(this);
		Preset.map[name] = this;
  }
	
	
	/**
	 * @returns A list of intervals representing the preset
	 */
	intervals() {
		const intervals: Interval[] = [];
		for (let i = 0; i < this.intervalPattern.length; i++) {
			if (this.presetValues[this.intervalPattern[i]].alert) {
				intervals[i] = new Interval(0, 0, 0, this.presetValues[this.intervalPattern[i]].duration(),
        this.presetValues[this.intervalPattern[i]].alert);
			}
			else {
				intervals[i] = new Interval(0, 0, 0, this.presetValues[this.intervalPattern[i]].duration());
			}
		}
		return intervals;
	}
}