import { NavigationContainer } from "@react-navigation/native";
import Menu from "./pages/Menu";
import Timer from "./pages/Timer";
import { Stack } from "./utils/Navigation";
import { useFonts } from 'expo-font';
import * as SplashScreen from 'expo-splash-screen'
import {useEffect} from "react";
import {useKeepAwake} from "expo-keep-awake";

SplashScreen.preventAutoHideAsync();

export default function App() {
    const [loaded, error] = useFonts({
        'Barlow-Regular': require('./assets/fonts/Barlow-Regular.ttf'),
        'Barlow-Italic': require('./assets/fonts/Barlow-Italic.ttf'),
        'Barlow-Bold': require('./assets/fonts/Barlow-Bold.ttf'),
    });

    useEffect(() => {
        if (loaded || error) {
            SplashScreen.hideAsync();
        }
    }, [loaded, error]);

    if (!loaded && !error) {
        return null;
    }

  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Menu">
        <Stack.Screen name="Menu" component={Menu} />
        <Stack.Screen name="Timer" component={Timer} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
