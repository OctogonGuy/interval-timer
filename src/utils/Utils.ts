/**
 * Returns a lighter shade of the given color
 * @param color The color to use
 */
export function lightenColor(color: string) {
    color = color.substring(1);
    let red: number = parseInt(color.substring(0, 2), 16);
    let green: number = parseInt(color.substring(2, 4), 16);
    let blue: number = parseInt(color.substring(4, 6), 16);
    red = Math.round(.1 * red + .9 * 255);
    green = Math.round(.1 * green + .9 * 255);
    blue = Math.round(.1 * blue + .9 * 255);
    return "#" + red.toString(16) + green.toString(16) + blue.toString(16);
}