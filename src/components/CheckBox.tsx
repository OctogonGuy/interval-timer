import { Checkbox } from "expo-checkbox";
import { View, Text } from "react-native";
import Styles from "../utils/Styles";
import {useState} from "react";

export default (props: {
  value: boolean;
  onValueChange: (value: boolean) => void;
  color: string
  text: string;
}) => {
  const [isChecked, setChecked] = useState(false);

  return (
    <View style={Styles.checkBoxContainer}>
      <Checkbox
        value={props.value}
        onValueChange={e => {
          props.onValueChange(e);
          setChecked(e);
        }}
        color={props.color}/>
      <Text>{props.text}</Text>
    </View>
  );
};
