import { NavigationContainer } from "@react-navigation/native";
import Menu from "./pages/Menu";
import Timer from "./pages/Timer";
import { Stack } from "./utils/Navigation";

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Menu">
        <Stack.Screen name="Menu" component={Menu} />
        <Stack.Screen name="Timer" component={Timer} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
