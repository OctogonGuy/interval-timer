import { useEffect, useState } from "react";
import { Text, View } from "react-native";
import WheelPicker from "react-native-wheely";
import Interval from "../utils/Interval";
import Styles from "../utils/Styles";

const numbers = Array.from(Array(60).keys());
const items = numbers.reverse().map((n) => String(n));

export default (props: {
  interval: Interval;
  changeInterval: (hours: number, minutes: number, seconds: number) => void;
}) => {
  const [hours, setHours] = useState(props.interval.hours());
  const [minutes, setMinutes] = useState(props.interval.minutes());
  const [seconds, setSeconds] = useState(props.interval.seconds());

  useEffect(() => {
    // Update interval
    props.changeInterval(hours, minutes, seconds);
  }, [hours, minutes, seconds]);

  return (
    <View style={Styles.intervalEditorBoxContainer}>
      <View style={Styles.intervalBoxComponent}>
        <WheelPicker
          containerStyle={Styles.picker}
          selectedIndex={numbers.indexOf(hours)}
          options={items}
          onChange={(index) => setHours(parseInt(items[index]))}
          visibleRest={1}
        />
        <Text>h</Text>
      </View>

      <View style={Styles.intervalBoxComponent}>
        <WheelPicker
          containerStyle={Styles.picker}
          selectedIndex={numbers.indexOf(minutes)}
          options={items}
          onChange={(index) => setMinutes(parseInt(items[index]))}
          visibleRest={1}
        />
        <Text>m</Text>
      </View>

      <View style={Styles.intervalBoxComponent}>
        <WheelPicker
          containerStyle={Styles.picker}
          selectedIndex={numbers.indexOf(seconds)}
          options={items}
          onChange={(index) => setSeconds(parseInt(items[index]))}
          visibleRest={1}
        />
        <Text>s</Text>
      </View>
    </View>
  );
};
