import { Audio } from "expo-av";

const dir = "../assets/alerts/";

const telephone_file = require(dir + "telephone.mp3");
const chimes_up_file = require(dir + "chimes_up.mp3");
const chimes_down_file = require(dir + "chimes_down.mp3");
const whistle_file = require(dir + "whistle.mp3");
const woodblock_file = require(dir + "woodblock.mp3");
const xylophone_file = require(dir + "xylophone.mp3");
const ocarina_file = require(dir + "ocarina.mp3");
const grandfather_1_file = require(dir + "grandfather_clock1.mp3");
const grandfather_2_file = require(dir + "grandfather_clock2.mp3");
const ding_file = require(dir + "ding.mp3");
const ding_dong_file = require(dir + "ding_dong.mp3");
const dong_ding_file = require(dir + "dong_ding.mp3");
const cymbal_file = require(dir + "cymbal.mp3");
const apito_file = require(dir + "apito.mp3");
const chords_file = require(dir + "chords.mp3");
const obatala_file = require(dir + "obatala.mp3");
const brave_wind_file = require(dir + "brave_wind.mp3");
const omomuki_file = require(dir + "omomuki.mp3");

export default class Alert {

	static values: Alert[] = [];
	static map: {[id: string]: Alert} = {};

	static TELEPHONE = new Alert("Telephone", telephone_file);
	static CHIMES_UP = new Alert("Chime Up", chimes_up_file);
	static CHIMES_DOWN = new Alert("Chime Down", chimes_down_file);
	static WHISTLE = new Alert("Whistle", whistle_file);
	static WOODBLOCK = new Alert("Woodblock", woodblock_file);
	static XYLOPHONE = new Alert("Xylophone", xylophone_file);
	static OCARINA = new Alert("Ocarina", ocarina_file);
	static GRANDFATHER_1 = new Alert("Grandf. 1", grandfather_1_file);
	static GRANDFATHER_2 = new Alert("Grandf. 2", grandfather_2_file);
	static DING = new Alert("Ding", ding_file);
	static DING_DONG = new Alert("Ding Dong", ding_dong_file);
	static DONG_DING = new Alert("Dong Ding", dong_ding_file);
	static CYMBAL = new Alert("Cymbal", cymbal_file);
	static APITO = new Alert("Apito", apito_file);
	static CHORDS = new Alert("Chords", chords_file);
	static OBATALA = new Alert("Obatala", obatala_file);
	static BRAVE_WIND = new Alert("Brave Wind", brave_wind_file);
	static OMOMUKI = new Alert("Omomuki", omomuki_file);
	static SILENT = new Alert("Silent");

	static sound: Audio.Sound = new Audio.Sound();

  name: string;
  file: any;

  private constructor(name: string, file?: any) {
    this.name = name;
    this.file = file ? file : undefined;
		Alert.values.push(this);
		Alert.map[name] = this;
  }
	
  async play() {
    if (!this.file) return;
    await Alert.sound.unloadAsync();
    await Alert.sound.loadAsync(this.file);
    await Alert.sound.playAsync();
  }
}