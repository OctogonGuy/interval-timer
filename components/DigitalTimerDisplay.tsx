import { Text } from "react-native";
import Styles from "../utils/Styles";

export default (props: { timeLeft: string }) => {
  return <Text style={Styles.text}>{props.timeLeft}</Text>;
};
