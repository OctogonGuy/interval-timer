import {Pressable, PressableProps, Text, View} from "react-native";

export const Button = (props: {pressableProps: PressableProps,
    text: string, color: string, textColor: string, lightColor: string, darkColor: string}) => {
    return (
        <View style={{
            borderColor: props.pressableProps.disabled ? "#808080" : props.darkColor,
            borderWidth: 1,
        }}>
            <Pressable {...props.pressableProps} style={({pressed}) => [
                {
                    backgroundColor: props.pressableProps.disabled ? "#B8B8B8" : pressed ? props.darkColor : props.color,
                    borderColor: props.pressableProps.disabled ? "#D0D0D0" : pressed ? props.color : props.lightColor,
                },
                {
                    borderTopWidth: 1,
                    paddingTop: 3,
                    paddingBottom: 5,
                    paddingHorizontal: 14,
                }
            ]}>
                    <Text style={{
                        fontFamily: 'Barlow-Regular',
                        color: props.pressableProps.disabled ? "#808080" : props.textColor,
                        fontSize: 18,
                    }}>{props.text}</Text>
            </Pressable>
        </View>
    );
};