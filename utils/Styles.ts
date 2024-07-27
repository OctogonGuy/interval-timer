import { StyleSheet } from "react-native";

export default StyleSheet.create({
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "space-between",
    paddingVertical: '6%',
    gap: 10,
  },
  controlGroup: {
    flex: 0,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
    paddingHorizontal: '6%',
    gap: 12,
  },
  spaced: {
    justifyContent: "space-between",
  },
  intervalList: {
    width: "80%",
  },
  separator: {
    height: 24
  },
  intervalBoxContainer: {
    flex: 1,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    gap: 10,
  },
  intervalBox: {
    flex: 3,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    gap: 2,
    padding: '2%',
    borderRadius: 3,
    backgroundColor: "#00000005",
  },
  intervalBoxComponent: {
    flex: 1,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    gap: 3,
  },
  intervalBoxNumber: {
    backgroundColor: "#ffffff",
    textAlign: "center",
    borderRadius: 5,
    width: '100%',
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
    alignItems: "flex-end",
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
    paddingVertical: 4,
    paddingHorizontal: 8,
  },
  presetDropdown: {
    width: '50%'
  },
  modalityDropdown: {
    width: '30%'
  },
  dropdownDisabled: {
    backgroundColor: "#bbbbbb",
    borderColor: "#969696",
  },
  dropdownTextDisabled: {
    color: "#969696",
  },
  checkBoxContainer: {
    flex: 1,
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
    paddingTop: -2,
    paddingBottom: 2,
  }
});
