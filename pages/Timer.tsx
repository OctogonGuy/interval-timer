import { View, Text, Modal, AppStateStatus } from "react-native";
import { TimerProps } from "../utils/Navigation";
import IntervalDisplay from "../components/IntervalDisplay";
import DigitalTimerDisplay from "../components/DigitalTimerDisplay";
import AnalogTimerDisplay from "../components/AnalogTimerDisplay";
import { useEffect, useState, useRef } from "react";
import { Audio } from "expo-av";
import IntervalTimer from "../utils/IntervalTimer";
import Alert from "../constants/Alert";
import {
  getColor,
  getIntervalTimer,
  getModality,
  storeColor,
  storeIntervalTimer,
  storeModality,
} from "../utils/AppStorage";
import State from "../constants/State";
import Styles from "../utils/Styles";
import { Dropdown } from "react-native-element-dropdown";
import Modality from "../constants/Modality";
import {lightenColor, darkenColor, textColor} from "../utils/Utils";
import {Button} from "../components/Button";
import {useKeepAwake} from "expo-keep-awake";
import ColorPicker, { Panel1, HueCircular, Preview } from 'reanimated-color-picker';
import {AppState} from 'react-native';

export default ({ route, navigation }: TimerProps) => {
  // --- Modal ---
  const [showColorPickerModal, setShowColorPickerModal] = useState(false);
  const [tempColor, setTempColor] = useState("");
  // --- Interval ---
  const [refreshDisplay, setRefreshDisplay] = useState<any>(); // Use to change UI
  const [intervalTimer, setIntervalTimer] = useState<IntervalTimer>();
  // --- GUI ---
  const [modality, setModality] = useState("");
  const [color, setColor] = useState("#800000");
  const [lightColor, setLightColor] = useState("#ffdbdb");
  const [darkColor, setDarkColor] = useState("#660000");
  const [fontColor, setFontColor] = useState("#ffffff");
  // --- Timer info ---
  const [intervalIndex, setIntervalIndex] = useState(0);
  const [numIntervals, setNumIntervals] = useState(0);
  const [timeLeftStr, setTimeLeftStr] = useState("");
  const [progress, setProgress] = useState(0.0);
  const [timerState, setTimerState] = useState("");
  // --- App state ---
  const appState: React.MutableRefObject<AppStateStatus> = useRef(AppState.currentState);
  const [appStateVisible, setAppStateVisible] = useState(appState.current);

  // App state (active, background, inactive)
  useEffect(() => {
    const subscription = AppState.addEventListener('change', nextAppState => {
      appState.current = nextAppState;
      setAppStateVisible(appState.current);
      console.log('AppState', appState.current);
    });

    return () => {
      subscription.remove();
    };
  }, []);

  useEffect(() => {
      if (appStateVisible === 'inactive' || appStateVisible === 'background') {
        intervalTimer?.freeze();
      }
      else {
        intervalTimer?.unfreeze();
      }
  }, [appStateVisible]);

  useEffect(() => {
    async function loadIntervalTimer() {
      const intervalTimer: IntervalTimer = await getIntervalTimer(
        setRefreshDisplay
      );
      setIntervalTimer(intervalTimer);
    }
    if (route.params) {
      const intervalTimer: IntervalTimer = new IntervalTimer(
        route.params.intervals,
        route.params.repeat,
        setRefreshDisplay,
        route.params.alertName ? Alert.map[route.params.alertName] : undefined
      );
      setIntervalTimer(intervalTimer);
      storeIntervalTimer(intervalTimer);
    } else {
      loadIntervalTimer();
    }
    async function loadColor() {
      const color = await getColor();
      setColor(color);
      setLightColor(lightenColor(color));
      setDarkColor(darkenColor(color));
      setFontColor(textColor(color));
    }
    loadColor();
    async function loadModality() {
      const modality = await getModality();
      setModality(modality);
    }
    loadModality();
  }, [route]);

  // Keep screen awake
  useKeepAwake();

  useEffect(() => {
    if (!intervalTimer) return;
    setIntervalIndex(intervalTimer.index + 1);
    setNumIntervals(intervalTimer.intervals.length);
    setTimeLeftStr(intervalTimer.timeLeftStr());
    setProgress(intervalTimer.progress());
    setTimerState(intervalTimer.state);
  }, [refreshDisplay]);

  useEffect(() => {
    if (!intervalTimer) return;
    intervalTimer.unfreeze();
    navigation.addListener("blur", () => {
      intervalTimer.freeze();
      storeIntervalTimer(intervalTimer);
    });
    setRefreshDisplay(0);
  }, [intervalTimer]);

  // Add derived colors
  useEffect(() => {
      setLightColor(lightenColor(color));
      setDarkColor(darkenColor(color));
      setFontColor(textColor(color));
  }, [color]);

  return (
    <View style={[Styles.container, {backgroundColor: lightColor}]}>
      <View>
        <View style={[Styles.controlGroup]}>
          <View style={{flex: 1, alignItems: 'flex-start'}}>
          </View>
          <View style={{flex: 1}}>
            <IntervalDisplay
              intervalIndex={intervalIndex}
              numIntervals={numIntervals}
            />
          </View>
          <View style={{flex: 1}}>
            <Dropdown /* Modality select */
              style={[Styles.dropdown, Styles.modalityDropdown, {width: '100%'}]}
              data={[Modality.ANALOG, Modality.DIGITAL, Modality.BOTH].map(
                function (modality: string) {
                  return { label: modality, value: modality };
                }
              )}
              labelField="label"
              valueField="value"
              value={modality}
              placeholder={modality}
              onChange={(item) => {
                setModality(item.value);
                storeModality(item.value);
              }}
              selectedTextStyle={Styles.text}
              placeholderStyle={Styles.text}
              renderItem={item => {
                return (
                    <View style={{padding: 10}}>
                      <Text style={Styles.text}>{item.label}</Text>
                    </View>
                )
              }}
            />
          </View>
        </View>
      </View>

      <View style={{flexDirection: "column", alignItems: "center", justifyContent: "space-evenly", width: "100%"}}>
        {(modality === Modality.DIGITAL || modality === Modality.BOTH) && (
          <DigitalTimerDisplay timeLeft={timeLeftStr} />
        )}

        {(modality === Modality.ANALOG || modality === Modality.BOTH) && (
          <AnalogTimerDisplay progress={progress} color={color} />
        )}
      </View>

      <View style={[Styles.container, { flex: 0, width: "100%" }]}>
        <View style={Styles.controlGroup}>
          <Button
            text="Previous"
            color={color}
            lightColor={lightColor}
            darkColor={darkColor}
            textColor={fontColor}
            pressableProps={{onPress: () => intervalTimer?.previous()}} />
          <Button
            text={timerState === State.STOPPED ? "Resume" : "Pause"}
            color={color}
            lightColor={lightColor}
            darkColor={darkColor}
            textColor={fontColor}
            pressableProps={{onPress: () => {
              intervalTimer?.toggle();
              setTimerState(intervalTimer?.state??"");
            }}}
          />
          <Button
            text="Next"
            color={color}
            lightColor={lightColor}
            darkColor={darkColor}
            textColor={fontColor}
            pressableProps={{onPress: () => intervalTimer?.next()}} />
        </View>

        <View style={Styles.controlGroup}>
          <Button
            text="Restart Timer"
            color={color}
            lightColor={lightColor}
            darkColor={darkColor}
            textColor={fontColor}
            pressableProps={{onPress: () => intervalTimer?.restartTimer()}}
          />
          <Button
            text="Restart Interval"
            color={color}
            lightColor={lightColor}
            darkColor={darkColor}
            textColor={fontColor}
            pressableProps={{onPress: () => intervalTimer?.restartInterval()}}
          />
        </View>
      </View>
    </View>
  );
};
