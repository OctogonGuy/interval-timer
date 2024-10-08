import { useEffect, useState } from "react";
import { FlatList, Text, View } from "react-native";
import WheelPicker from "react-native-wheely";
import Interval from "../utils/Interval";
import Styles from "../utils/Styles";
import Preset, { PresetValue } from "../constants/Preset";
import { Picker } from "@react-native-picker/picker";
import Alert from "../constants/Alert";
import { Dropdown } from "react-native-element-dropdown";

const numbers = Array.from(Array(60).keys());
const items = numbers.reverse().map((n) => String(n));

export default (props: {
  preset: Preset;
  submitPreset: (newValue: Preset) => void;
}) => {
  return (
    <View style={Styles.presetModal}>
      {props.preset.presetValues.map(function (value: PresetValue) {
        return (
          <View
            style={Styles.presetItem}
            key={props.preset.presetValues.indexOf(value)}
          >
            <Text style={[Styles.text, {flex: 4}]}>{value.name}</Text>
            <View style={{alignItems: "center", flexDirection: "row", flex: 4, gap: 5}}>
              <WheelPicker
                containerStyle={{flex: 1}}
                selectedIndex={numbers.indexOf(value.numUnits)}
                options={items}
                onChange={(index) => {
                  value.numUnits = parseInt(items[index]);
                }}
                visibleRest={1}
                scaleFunction={(x) => 1/(1.5*x)}
                itemHeight={25}
                itemTextStyle={Styles.text}
              />
              <Text style={[Styles.text, {flex: 0, textAlign: "left"}]}>{value.unit.charAt(0).toLowerCase()}</Text>
            </View>
            {value.alert && (
              <Dropdown
                style={[Styles.dropdown, {flex: 6}]}
                value={value.alert}
                onChange={(item: any) => {
                  value.alert = item.value;
                }}
                placeholder={value.alert.name}
                data={Alert.values.map(function (alert: Alert) {
                  return { label: alert.name, value: alert };
                })}
                labelField="label"
                valueField="value"
                maxHeight={350}
                placeholderStyle={Styles.text}
                selectedTextProps={{numberOfLines: 1}}
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
      })}
    </View>
  );
};
