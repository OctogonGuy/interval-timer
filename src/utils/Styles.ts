import { StyleSheet } from "react-native";

export default StyleSheet.create({
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "space-between",
    paddingVertical: 12,
    gap: 10,
  },
  controlGroup: {
    flex: 0,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
    paddingHorizontal: 12,
    gap: 12,
  },
  spaced: {
    justifyContent: "space-between",
  },
  intervalList: {
    width: "100%",
  },
  separator: {
    height: 24
  },
  intervalBoxContainer: {
    flex: 0,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    gap: 10,
  },
  intervalBox: {
    flex: 0,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    gap: 8,
    padding: 5,
    borderRadius: 3,
    backgroundColor: "#00000005",
  },
  intervalBoxComponent: {
    flex: 0,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    gap: 3,
  },
  intervalBoxNumber: {
    backgroundColor: "#ffffff",
    paddingVertical: 5,
    width: 35,
    textAlign: "center",
    borderRadius: 5,
  },
  intervalEditorBoxContainer: {
    flex: 0,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
    gap: 15,
  },
  intervalEditorBoxModal: {
    flex: 0,
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "space-between",
    width: "80%",
    borderRadius: 5,
    alignSelf: "center",
    marginVertical: "auto",
    gap: 15,
    padding: 15
  },
  presetModal: {
    flex: 0,
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
  },
  presetItem: {
    flex: 0,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    gap: 15,
  },
  presetEditorBoxModal: {
    flex: 0,
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "space-between",
    width: "90%",
    borderRadius: 5,
    alignSelf: "center",
    marginVertical: "auto",
    gap: 15,
    padding: 15
  },
  dropdown: {
    backgroundColor: "#ffffff",
    borderColor: "#575757",
    borderWidth: 1,
    paddingVertical: 6,
    paddingHorizontal: 10,
  },
  alertDropdown: {
    width: 150
  },
  presetDropdown: {
    width: 180
  },
  modalityDropdown: {
    width: 120
  },
  dropdownDisabled: {
    backgroundColor: "#bbbbbb",
    borderColor: "#969696",
  },
  dropdownTextDisabled: {
    color: "#969696",
  },
  picker: {
    width: 70
  },
  checkBoxContainer: {
    flex: 0,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    gap: 5,
  },
  analogDisplay: {
    width: "75%",
    height: "75%",
  },
  colorPicker: {
    flex: 1,
    width: "100%",
  },
  colorPickerContainer: {
    flex: 0,
    alignItems: "center",
    justifyContent: "center",
    width: "80%",
    height: "60%",
    borderRadius: 5,
    alignSelf: "center",
    marginVertical: "auto",
    gap: 40,
    padding: 15
  },
  modal: {
    backgroundColor: "#f8f9fa",
    borderColor: "#454545",
    borderWidth: 3,
  },
  text: {
    fontFamily: "Barlow-Regular",
    fontSize: 18,
  }
});
