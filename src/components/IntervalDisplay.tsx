import { Text } from "react-native";
import Styles from "../utils/Styles";

export default (props: { intervalIndex: number; numIntervals: number }) => {
  return (
    <Text style={Styles.text}>
      {props.intervalIndex}/{props.numIntervals}
    </Text>
  );
};
