import { StatusBar } from "expo-status-bar";
import { useEffect, useState } from "react";
import {
  FlatList,
  Modal,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from "react-native";
import Checkbox from "expo-checkbox";
import { Dropdown } from "react-native-element-dropdown";
import IntervalEditorBox from "../components/IntervalEditorBox";
import IntervalBox from "../components/IntervalBox";
import {
  storeIntervals,
  getIntervals,
  storeAlert,
  storeRepeat,
  storeSharedAlert,
  getAlert,
  getRepeat,
  getSharedAlert, getIntervalTimer, storeIntervalTimer, getColor, getModality, storeColor,
} from "../utils/AppStorage";
import Interval from "../utils/Interval";
import Styles from "../utils/Styles";
import { MenuProps } from "../utils/Navigation";
import Alert from "../constants/Alert";
import { Picker } from "@react-native-picker/picker";
import PresetEditorBox from "../components/PresetEditorBox";
import Preset from "../constants/Preset";
import CheckBox from "../components/CheckBox";
import IntervalTimer from "../utils/IntervalTimer";
import ColorPicker from "react-native-wheel-color-picker";
import {lightenColor, darkenColor, textColor} from "../utils/Utils";
import {Button} from "../components/Button";

export default ({ route, navigation }: MenuProps) => {
  // --- Modal ---
  const [intervalModalVisible, setIntervalModalVisible] = useState(false);
  const [presetModalVisible, setPresetModalVisible] = useState(false);
  const [showColorPickerModal, setShowColorPickerModal] = useState(false);
  const [tempColor, setTempColor] = useState("");
  // --- Interval info ---
  const [intervals, setIntervals] = useState(new Array<Interval>());
  const [modalInterval, setModalInterval] = useState(new Interval());
  const [modalIntervalIndex, setModalIntervalIndex] = useState(0);
  const [modalPreset, setModalPreset] = useState(Preset.POMODORO);
  const [removeButtonDisabled, setRemoveButtonDisabled] = useState(false);
  const [startButtonDisabled, setStartButtonDisabled] = useState(false);
  // --- Form ---
  const [alert, setAlert] = useState<Alert>(Alert.SILENT);
  const [repeat, setRepeat] = useState<boolean>(false);
  const [sharedAlert, setSharedAlert] = useState<boolean>(false);
  // --- GUI ---
  const [color, setColor] = useState("");
  const [lightColor, setLightColor] = useState("");
  const [darkColor, setDarkColor] = useState("");
  const [fontColor, setFontColor] = useState("");

  // Get data from storage upon first load
  useEffect(() => {
    async function load() {
      setIntervals(await getIntervals());
      setAlert(await getAlert());
      setRepeat(await getRepeat());
      setSharedAlert(await getSharedAlert());
    }
    load();
    async function loadColor() {
      const color = await getColor();
      setColor(color);
    }
    loadColor();
    navigation.addListener('focus', () => {
      loadColor();
    })
  }, []);

  useEffect(() => {
    // Disable remove button if there is only one interval
    setRemoveButtonDisabled(intervals.length == 1);
    // Disable start button any intervals' length is 0
    let emptyIntervalFound = false;
    for (const interval of intervals) {
      if (interval.duration == 0) {
        emptyIntervalFound = true;
        break;
      }
    }
    setStartButtonDisabled(emptyIntervalFound);
  }, [intervals]);

  // Add derived colors
  useEffect(() => {
      setLightColor(lightenColor(color));
      setDarkColor(darkenColor(color));
      setFontColor(textColor(color));
  }, [color]);

  function addInterval() {
    const tempIntervals = [...intervals];
    tempIntervals.push(new Interval());
    storeIntervals(tempIntervals);
    setIntervals(tempIntervals);
  }

  function removeInterval() {
    if (intervals.length == 1) return;
    const tempIntervals = [...intervals];
    tempIntervals.pop();
    storeIntervals(tempIntervals);
    setIntervals(tempIntervals);
  }

  function changeInterval(index: number, interval: Interval) {
    const tempIntervals = [...intervals];
    tempIntervals[index] = interval;
    storeIntervals(tempIntervals);
    setIntervals(tempIntervals);
  }

  function usePreset(preset: Preset) {
    setIntervals(preset.intervals());
    setSharedAlert(!modalPreset.presetValues[0].alert);
    storeIntervals(preset.intervals());
  }

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

      <Modal
        transparent={true}
        visible={intervalModalVisible}
        onRequestClose={() => setIntervalModalVisible(false)}
      >
        <View style={[Styles.intervalEditorBoxModal, , Styles.modal]}>
          <IntervalEditorBox
            interval={modalInterval}
            changeInterval={(
              hours: number,
              minutes: number,
              seconds: number
            ) => {
              const newValue: Interval = new Interval(
                hours,
                minutes,
                seconds,
                0,
                modalInterval.alert
              );
              setModalInterval(newValue);
            }}
          />

          <View style={Styles.controlGroup}>
            <Button
              text="Confirm"
              color={color}
              lightColor={lightColor}
              darkColor={darkColor}
              textColor={fontColor}
              pressableProps={{onPress: () => {
                changeInterval(modalIntervalIndex, modalInterval);
                setIntervalModalVisible(false);
              }}}
            />
            <Button
              text="Cancel"
              color={color}
          lightColor={lightColor}
          darkColor={darkColor}
          textColor={fontColor}
              pressableProps={{onPress: () => setIntervalModalVisible(false)}}
            />
          </View>
        </View>
      </Modal>

      <Modal
        transparent={true}
        visible={presetModalVisible}
        onRequestClose={() => setPresetModalVisible(false)}
      >
        <View style={[Styles.presetEditorBoxModal, Styles.modal]}>
          <PresetEditorBox preset={modalPreset} submitPreset={setModalPreset} />
          <View style={Styles.controlGroup}>
            <Button
              text="Confirm"
              color={color}
              lightColor={lightColor}
              darkColor={darkColor}
              textColor={fontColor}
              pressableProps={{onPress: () => {
                usePreset(modalPreset);
                setPresetModalVisible(false);
              }}}
            />
            <Button
              text="Cancel"
              color={color}
          lightColor={lightColor}
          darkColor={darkColor}
          textColor={fontColor}
              pressableProps={{onPress: () => setPresetModalVisible(false)}}
            />
          </View>
        </View>
      </Modal>

      <View style={[Styles.controlGroup, Styles.spaced, {paddingBottom: 10}]}>
        <Button
          text="Color Picker"
          color={color}
          lightColor={lightColor}
          darkColor={darkColor}
          textColor={fontColor}
          pressableProps={{onPress: () => setShowColorPickerModal(true)}}
        />
        <Dropdown /* Preset select */
          style={[Styles.dropdown, Styles.presetDropdown]}
          data={Preset.values.map(function (preset: Preset) {
            return { label: preset.name, value: preset };
          })}
          labelField="label"
          valueField="value"
          placeholder="Preset"
          onChange={(item) => {
            setModalPreset(item.value);
            setPresetModalVisible(true);
          }}
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

      <FlatList /* Interval list */
        style={Styles.intervalList}
        ItemSeparatorComponent={() => {
          return <View style={Styles.separator} />;
        }}
        data={intervals}
        renderItem={({ item, index }) => {
          return (
            <View style={Styles.intervalBoxContainer}>
              <Text style={Styles.text}>{index + 1}.</Text>
              <IntervalBox
                hours={item.hours()}
                minutes={item.minutes()}
                seconds={item.seconds()}
                showAlert={!sharedAlert}
                alert={item.alert}
                onPress={() => {
                  setModalIntervalIndex(intervals.indexOf(item));
                  setModalInterval(intervals[intervals.indexOf(item)]);
                  setIntervalModalVisible(true);
                }}
                changeIntervalAlert={(alertItem: any) => {
                  const newValue: Alert = alertItem.value;
                  const index: number = intervals.indexOf(item);
                  const newInterval = item;
                  newInterval.alert = newValue;
                  changeInterval(index, newInterval);
                }}
              />
            </View>
          );
        }}
      />

      <View style={[Styles.controlGroup, {paddingTop: 10}]}>
        <Button
          text="Add"
          color={color}
          lightColor={lightColor}
          darkColor={darkColor}
          textColor={fontColor}
          pressableProps={{onPress: addInterval}} />
        <Button
          color={color}
          lightColor={lightColor}
          darkColor={darkColor}
          textColor={fontColor}
          text="Remove"
          pressableProps={{
              onPress: removeInterval,
              disabled: removeButtonDisabled
          }}
        />
      </View>

      <View style={Styles.controlGroup}>
        <Dropdown
          style={[
            Styles.dropdown,
            Styles.alertDropdown,
            !sharedAlert ? Styles.dropdownDisabled : null,
          ]}
          placeholderStyle={!sharedAlert ? [Styles.dropdownTextDisabled, Styles.text] : Styles.text}
          value={alert.name}
          onChange={(item) => {
            setAlert(item.value);
            storeAlert(item.value);
          }}
          placeholder={alert.name}
          maxHeight={450}
          data={Alert.values.map(function (alert: Alert) {
            return { label: alert.name, value: alert };
          })}
          labelField="label"
          valueField="value"
          disable={!sharedAlert}
            renderItem={item => {
              return (
                  <View style={{padding: 10}}>
                    <Text style={Styles.text}>{item.label}</Text>
                  </View>
              )
            }}
        />
        <CheckBox
          value={sharedAlert}
          color={color}
          onValueChange={(value) => {
            setSharedAlert(value);
            storeSharedAlert(value);
          }}
          text="Shared"
        />
        <CheckBox
          value={repeat}
          color={color}
          onValueChange={(value) => {
            setRepeat(value);
            storeRepeat(value);
          }}
          text="Repeat"
        />
      </View>

      <View style={Styles.controlGroup}>
        <Button
          text="Start"
          color={color}
          lightColor={lightColor}
          darkColor={darkColor}
          textColor={fontColor}
          pressableProps={{
              onPress: () => {
                navigation.navigate("Timer", {
                  intervals: intervals,
                  repeat: repeat,
                  alertName: sharedAlert ? alert.name : "",
                })
              },
              disabled: startButtonDisabled
          }}
        />
        <Button
          text="Continue"
          color={color}
          lightColor={lightColor}
          darkColor={darkColor}
          textColor={fontColor}
          pressableProps={{
              onPress: () => navigation.navigate("Timer"),
              disabled: startButtonDisabled
          }}
        />
      </View>
    </View>
  );
};
