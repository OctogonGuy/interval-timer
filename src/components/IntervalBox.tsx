import { Text, Pressable, View, TouchableOpacity } from "react-native";
import Styles from "../utils/Styles";
import Alert from "../constants/Alert";
import { Picker } from "@react-native-picker/picker";
import Interval from "../utils/Interval";
import { useState } from "react";
import { Dropdown } from "react-native-element-dropdown";

export default (props: {
  hours?: number;
  minutes?: number;
  seconds?: number;
  alert?: Alert;
  showAlert: boolean;
  onPress?: () => void;
  changeIntervalAlert: (newValue: any) => void;
}) => {
  return (
    <View style={Styles.intervalBoxContainer}>
      <TouchableOpacity style={Styles.intervalBox} onPress={props.onPress}>
        <View style={Styles.intervalBoxComponent}>
          <Text style={Styles.intervalBoxNumber}>{props.hours}</Text>
          <Text>h</Text>
        </View>
        <View style={Styles.intervalBoxComponent}>
          <Text style={Styles.intervalBoxNumber}>{props.minutes}</Text>
          <Text>m</Text>
        </View>
        <View style={Styles.intervalBoxComponent}>
          <Text style={Styles.intervalBoxNumber}>{props.seconds}</Text>
          <Text>s</Text>
        </View>
      </TouchableOpacity>
      {props.showAlert && (
        <Dropdown
          style={[Styles.dropdown, Styles.alertDropdown]}
          value={props.alert}
          onChange={props.changeIntervalAlert}
          placeholder={props.alert?.name}
          data={Alert.values.map(function (alert: Alert) {
            return { label: alert.name, value: alert };
          })}
          labelField="label"
          valueField="value"
          maxHeight={450}
        />
      )}
    </View>
  );
};
