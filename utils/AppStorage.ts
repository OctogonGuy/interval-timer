import AsyncStorage from "@react-native-async-storage/async-storage";
import Interval from "./Interval";
import IntervalTimer from "./IntervalTimer";
import Alert from "../constants/Alert";
import Modality from "../constants/Modality";

export const storeData = async (key: string, value: any) => {
  try {
    const jsonValue = JSON.stringify(value);
    await AsyncStorage.setItem(key, jsonValue);
  } catch (e: any) {
    console.error(e, e.stack);
  }
}

export const getData = async (key: string) => {
  try {
    const jsonValue = await AsyncStorage.getItem(key);
    return jsonValue != null ? JSON.parse(jsonValue) : null;
  } catch (e: any) {
    console.error(e, e.stack);
  }
}

// --- App-related data ---

export const storeIntervals = async (intervals: Interval[]) => {
  storeData("menu-intervals", JSON.stringify(intervals));
}

export const getIntervals = async () => {
  const loadedData = await getData("menu-intervals");
  const intervals: Interval[] = [];
  // Add empty starting interval if not no data was loaded
  if (!loadedData) {
    intervals.push(new Interval());
  }
  else {
    // Create new instances of intervals by parsing JSON data
    const intervalsData = JSON.parse(loadedData);
    for (const intervalsDatum of intervalsData) {
      intervals.push(new Interval(0, 0, 0, intervalsDatum.duration, Alert.map[intervalsDatum.alert?.name]));
    }
  }
  return intervals;
}

export const storeIntervalTimer = async (intervalTimer: IntervalTimer) => {
  const intervals = await getIntervals();
  storeData("intervals", JSON.stringify(intervals));
  storeData("intervalTimer", JSON.stringify(intervalTimer));
}

export const getIntervalTimer = async (task: Function) => {
  const loadedIntervals = await getData("intervals");
  const intervals = [];
  const intervalsData = JSON.parse(loadedIntervals);
  for (const intervalsDatum of intervalsData) {
    intervals.push(new Interval(0, 0, 0, intervalsDatum.duration, Alert.map[intervalsDatum.alert?.name]));
  }

  const loadedData = await getData("intervalTimer");
  const intervalTimerData = JSON.parse(loadedData);
  const intervalTimer: IntervalTimer = new IntervalTimer(
    intervals,
    intervalTimerData.repeat,
    task,
    intervalTimerData.alert ? Alert.map[intervalTimerData.alert.name] : undefined,
  );
  intervalTimer.index = intervalTimerData.index;
  intervalTimer.startTime = intervalTimerData.startTime;
  intervalTimer.stopTime = intervalTimerData.stopTime;
  intervalTimer.stopDuration = intervalTimerData.stopDuration;
  intervalTimer.state = intervalTimerData.state;
  return intervalTimer;
}

export const storeAlert = async(alert: Alert) => {
  await storeData("alertName", alert.name);
}

export const getAlert = async() => {
  const alertName: string = await getData("alertName");
  if (alertName)
    return Alert.map[alertName];
  return Alert.CHORDS;
}

export const storeRepeat = async(repeat: boolean) => {
  await storeData("repeat", JSON.stringify(repeat));
}

export const getRepeat = async() => {
  const repeatStr: string = await getData("repeat");
  if (repeatStr)
    return JSON.parse(repeatStr);
  else
    return false;
}

export const storeSharedAlert = async(sharedAlert: boolean) => {
  storeData("sharedAlert", JSON.stringify(sharedAlert));
}

export const getSharedAlert = async() => {
  const sharedAlertStr: string = await getData("sharedAlert");
  if (sharedAlertStr)
    return JSON.parse(sharedAlertStr);
  else
    return true;
}

export const storeColor = async(color: string) => {
  storeData("color", color);
}

export const getColor = async() => {
  const color: string = await getData("color");
  if (color)
    return color;
  else
    return "lightcoral";
}

export const storeModality = async(modality: string) => {
  storeData("modality", modality);
}

export const getModality = async() => {
  const modality: string = await getData("modality");
  if (modality)
    return modality;
  else
    return Modality.BOTH;
}