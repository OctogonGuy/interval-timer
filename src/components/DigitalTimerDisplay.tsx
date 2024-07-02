import { Text } from "react-native";
import Styles from "../utils/Styles";

export default (props: { timeLeft: string }) => {
  return <Text>{props.timeLeft}</Text>;
};
