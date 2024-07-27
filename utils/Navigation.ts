import { createNativeStackNavigator, NativeStackScreenProps } from "@react-navigation/native-stack";
import Interval from "./Interval";

export type RootStackParamList = {
  Menu: undefined;
  Timer: {
    intervals: Interval[],
    repeat: boolean,
    alertName: string
   } | undefined;
};

export const Stack = createNativeStackNavigator<RootStackParamList>();
export type MenuProps = NativeStackScreenProps<RootStackParamList, "Menu">;
export type TimerProps = NativeStackScreenProps<RootStackParamList, "Timer">;