import React from "react";
import { getColor } from "../utils/AppStorage"
import {lightenColor} from "../utils/Utils";

export const ColorContext = React.createContext({
    color: "",
    lightColor: "",
    setColor: (newColor: string) => { }
});