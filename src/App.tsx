import { NavigationContainer } from "@react-navigation/native";
import Menu from "./pages/Menu";
import Timer from "./pages/Timer";
import { Stack } from "./utils/Navigation";
import {useEffect, useState} from "react";
import {Button, Modal, View} from "react-native";
import Styles from "./utils/Styles";
import ColorPicker from "react-native-wheel-color-picker";
import {getAlert, getColor, getIntervals, getRepeat, getSharedAlert, storeColor} from "./utils/AppStorage";
import { ColorContext } from "./contexts/ColorContext"
import {lightenColor} from "./utils/Utils";

export default function App() {
  // --- GUI ---
  const [showColorPickerModal, setShowColorPickerModal] = useState(false);
  const [tempColor, setTempColor] = useState("");
  const [state, setState] = useState({
      color: "#808080",
      lightColor: lightenColor("#808080"),
      setColor: (newColor: string) => {
        state.color = newColor;
        state.lightColor = lightenColor(newColor)
        storeColor(newColor);
        setShowColorPickerModal(false);
      }},);

    /*const state = {
      color: "#808080",
      lightColor: lightenColor("#808080"),
      setColor: (newColor: string) => {
        state.color = newColor;
        state.lightColor = lightenColor(newColor)
        storeColor(tempColor);
        setShowColorPickerModal(false);
      },
    };*/

        /*setState(state => ({
            color: newColor,
            lightColor: lightenColor(newColor),
        }));*/

  // Get data from storage upon first load
  useEffect(() => {
    async function loadColor() {
      const color = await getColor();
      state.setColor(color);
    }
    loadColor();
  }, []);


  return (
    <ColorContext.Provider value={state}>
        <NavigationContainer>
          <Stack.Navigator initialRouteName="Menu">
              <Stack.Screen
                  name="Menu"
                  component={Menu}
                  options={{
                      headerRight: () => (
                          <View>
                              <Modal
                                visible={showColorPickerModal}
                                transparent={true}
                                animationType="slide"

                              >
                                <View style={[Styles.colorPickerContainer, Styles.modal]}>
                                  <View style={Styles.colorPicker}>
                                    <ColorPicker
                                      color={state.color}
                                      onColorChangeComplete={setTempColor}
                                      swatches={false}
                                    />
                                  </View>
                                  <View style={Styles.controlGroup}>
                                    <Button
                                      title="Ok"
                                      color={state.color}
                                      onPress={() => {
                                        state.setColor(tempColor);
                                        setShowColorPickerModal(false);
                                      }}
                                    />
                                    <Button
                                      title="Cancel"
                                      color={state.color}
                                      onPress={() => setShowColorPickerModal(false)}
                                    />
                                  </View>
                                </View>
                              </Modal>
                              <Button
                                  onPress={() => setShowColorPickerModal(true)}
                                  title="Color picker"
                                  color={state.color}
                              />
                          </View>
                      ),
                  }} />
              <Stack.Screen name="Timer" component={Timer} />
          </Stack.Navigator>
        </NavigationContainer>
    </ColorContext.Provider>
  );
}
