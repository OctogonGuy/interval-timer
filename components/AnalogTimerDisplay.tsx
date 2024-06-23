import Svg, { Circle, Path } from "react-native-svg";
import Styles from "../utils/Styles";

const innerRadius = 40;
const outerWidth = 5;

export default (props: { progress: number; color: string }) => {
  return (
    <Svg viewBox="0 0 100 100" style={Styles.analogDisplay}>
      <Path
        fill={props.color}
        d={`M 50 ${50 - innerRadius} A ${innerRadius} ${innerRadius}, 0, ${
          props.progress < 0.5 ? 1 : 0
        } 0, ${
          50 -
          Math.cos(2 * Math.PI * props.progress + Math.PI / 2) * innerRadius
        } ${
          50 -
          Math.sin(2 * Math.PI * props.progress + Math.PI / 2) * innerRadius
        } L 50 50`}
      ></Path>
      <Circle
        cx="50"
        cy="50"
        r={50 - outerWidth / 2}
        stroke={props.color}
        fill="transparent"
        strokeWidth={outerWidth}
      />
    </Svg>
  );
};
