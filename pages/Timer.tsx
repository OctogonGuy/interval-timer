import { View, Text, Modal } from "react-native";
import { TimerProps } from "../utils/Navigation";
import IntervalDisplay from "../components/IntervalDisplay";
import DigitalTimerDisplay from "../components/DigitalTimerDisplay";
import AnalogTimerDisplay from "../components/AnalogTimerDisplay";
import { useEffect, useState } from "react";
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
import ColorPicker from "react-native-wheel-color-picker";
import Styles from "../utils/Styles";
import { Dropdown } from "react-native-element-dropdown";
import Modality from "../constants/Modality";
import {lightenColor, darkenColor, textColor} from "../utils/Utils";
import {Button} from "../components/Button";

export default ({ route, navigation }: TimerProps) => {
  // --- Modal ---
  const [showColorPickerModal, setShowColorPickerModal] = useState(false);
  const [tempColor, setTempColor] = useState("");
  // --- Interval ---
  const [refreshDisplay, setRefreshDisplay] = useState<any>(); // Use to change UI
  const [intervalTimer, setIntervalTimer] = useState<IntervalTimer>();
  // --- GUI ---
  const [modality, setModality] = useState("");
  const [color, setColor] = useState("");
  const [lightColor, setLightColor] = useState("");
  const [darkColor, setDarkColor] = useState("");
  const [fontColor, setFontColor] = useState("");
  // --- Timer info ---
  const [intervalIndex, setIntervalIndex] = useState(0);
  const [numIntervals, setNumIntervals] = useState(0);
  const [timeLeftStr, setTimeLeftStr] = useState("");
  const [progress, setProgress] = useState(0.0);
  const [timerState, setTimerState] = useState("");

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
      <Modal
        visible={showColorPickerModal}
        transparent={true}
        animationType="slide"
      >
        <View style={[Styles.colorPickerContainer, Styles.modal]}>
          <View style={Styles.colorPicker}>
            <ColorPicker
              color={color}
              onColorChangeComplete={setTempColor}
              swatches={false}
            />
          </View>
          <View style={Styles.controlGroup}>
            <Button
              text="Ok"
              color={color}
              lightColor={lightColor}
              darkColor={darkColor}
              textColor={fontColor}
              pressableProps={{onPress: () => {
                setColor(tempColor);
                storeColor(tempColor);
                setShowColorPickerModal(false);
              }}}
            />
            <Button
              text="Cancel"
              color={color}
              lightColor={lightColor}
              darkColor={darkColor}
              textColor={fontColor}
              pressableProps={{onPress: () => setShowColorPickerModal(false)}}
            />
          </View>
        </View>
      </Modal>

      <View>
        <View style={[Styles.controlGroup]}>
          <View style={{flex: 1, alignItems: 'flex-start'}}>
            <Button
              text="Color"
              color={color}
              lightColor={lightColor}
              darkColor={darkColor}
              textColor={fontColor}
              pressableProps={{onPress: () => setShowColorPickerModal(true)}}
            />
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
            pressableProps={{onPress: () => intervalTimer?.toggle()}}
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
