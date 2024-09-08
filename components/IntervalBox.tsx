import { Text, Pressable, View, TouchableOpacity } from "react-native";
import Styles from "../utils/Styles";
import Alert from "../constants/Alert";
import { Picker } from "@react-native-picker/picker";
import Interval from "../utils/Interval";
import { useState } from "react";
import { Dropdown } from "react-native-element-dropdown";

export default (props: {
  intervalNum: number;
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
      <Text style={Styles.text}>{props.intervalNum}.</Text>
      <TouchableOpacity style={Styles.intervalBox} onPress={props.onPress}>
        <View style={Styles.intervalBoxComponent}>
          <Text style={[Styles.intervalBoxNumber, Styles.text]}>{props.hours?.toString().padStart(2, '0')}</Text>
        </View>
        <Text style={Styles.text}>:</Text>
        <View style={Styles.intervalBoxComponent}>
          <Text style={[Styles.intervalBoxNumber, Styles.text]}>{props.minutes?.toString().padStart(2, '0')}</Text>
        </View>
        <Text style={Styles.text}>:</Text>
        <View style={Styles.intervalBoxComponent}>
          <Text style={[Styles.intervalBoxNumber, Styles.text]}>{props.seconds?.toString().padStart(2, '0')}</Text>
        </View>
      </TouchableOpacity>
      {props.showAlert && (
        <Dropdown
          style={[Styles.dropdown, {flex: 2}]}
          value={props.alert}
          onChange={props.changeIntervalAlert}
          placeholder={props.alert?.name}
          data={Alert.values.map(function (alert: Alert) {
            return { label: alert.name, value: alert };
          })}
          labelField="label"
          valueField="value"
          maxHeight={450}
          placeholderStyle={Styles.text}
          selectedTextProps={{numberOfLines:1}}
          renderItem={item => {
            return (
                <View style={{padding: 10}}>
                  <Text style={Styles.text} numberOfLines={1}>{item.label}</Text>
                </View>
            )
          }}
        />
      )}
    </View>
  );
};
