#include "ConsoleColors.h"

/***************************
 * DARKER FOREBROUND COLORS
 **************************/

std::string ConsoleColors::F_BLACK(const std::string& text){
    return setTextColor(30) + text + resetColor();
}

std::string ConsoleColors::F_RED(const std::string& text){
    return setTextColor(31) + text + resetColor();
}

std::string ConsoleColors::F_GREEN(const std::string& text){
    return setTextColor(32) + text + resetColor();
}

std::string ConsoleColors::F_YELLOW(const std::string& text){
    return setTextColor(33) + text + resetColor();
}

std::string ConsoleColors::F_BLUE(const std::string& text){
    return setTextColor(34) + text + resetColor();
}

std::string ConsoleColors::F_MAGENTA(const std::string& text){
   return  setTextColor(35) + text + resetColor();
}

std::string ConsoleColors::F_CYAN(const std::string& text){
   return  setTextColor(36) + text + resetColor();
}

std::string ConsoleColors::F_WHITE(const std::string& text){
   return  setTextColor(37) + text + resetColor();
}

/****************************
 * LIGHTER FOREBROUND COLORS
 ***************************/

std::string ConsoleColors::F_B_BLACK(const std::string& text){
   return  setTextColor(90) + text + resetColor();
}

std::string ConsoleColors::F_B_RED(const std::string& text){
   return  setTextColor(91) + text + resetColor();
}

std::string ConsoleColors::F_B_GREEN(const std::string& text){
   return  setTextColor(92) + text + resetColor();
}

std::string ConsoleColors::F_B_YELLOW(const std::string& text){
   return  setTextColor(93) + text + resetColor();
}

std::string ConsoleColors::F_B_BLUE(const std::string& text){
   return  setTextColor(94) + text + resetColor();
}

std::string ConsoleColors::F_B_MAGENTA(const std::string& text){
   return  setTextColor(95) + text + resetColor();
}

std::string ConsoleColors::F_B_CYAN(const std::string& text){
   return  setTextColor(96) + text +  resetColor();
}

std::string ConsoleColors::F_B_WHITE(const std::string& text){
   return  setTextColor(97) + text + resetColor();
}

/***************************
 * DARKER BACKGROUND COLORS
 **************************/

std::string ConsoleColors::B_BLACK(const std::string& text){
   return  setTextColor(40) + text + resetColor();
}

std::string ConsoleColors::B_RED(const std::string& text){
   return  setTextColor(41) + text + resetColor();
}

std::string ConsoleColors::B_GREEN(const std::string& text){
   return  setTextColor(42) + text + resetColor();
}

std::string ConsoleColors::B_YELLOW(const std::string& text){
   return  setTextColor(43) + text + resetColor();
}

std::string ConsoleColors::B_BLUE(const std::string& text){
   return  setTextColor(44) + text + resetColor();
}

std::string ConsoleColors::B_MAGENTA(const std::string& text){
   return  setTextColor(45) + text + resetColor();
}

std::string ConsoleColors::B_CYAN(const std::string& text){
   return  setTextColor(46) + text + resetColor();
}
std::string ConsoleColors::B_WHITE(const std::string& text){
   return  setTextColor(47) + text + resetColor();
}

/****************************
 * LIGHTER BACKGROUND COLORS
 ***************************/

std::string ConsoleColors::B_B_RED(const std::string& text){
    return setTextColor(100) + text + resetColor();
}

std::string ConsoleColors::B_B_BLACK(const std::string& text){
    return setTextColor(101) + text + resetColor();
}

std::string ConsoleColors::B_B_GREEN(const std::string& text){
    return setTextColor(102) + text + resetColor();
}

std::string ConsoleColors::B_B_YELLOW(const std::string& text){
    return setTextColor(103) + text + resetColor();
}

std::string ConsoleColors::B_B_BLUE(const std::string& text){
    return setTextColor(104) + text + resetColor();
}

std::string ConsoleColors::B_B_MAGENTA(const std::string& text){
    return setTextColor(105) + text + resetColor();
}

std::string ConsoleColors::B_B_CYAN(const std::string& text){
    return setTextColor(106) + text + resetColor();
}

std::string ConsoleColors::B_B_WHITE(const std::string& text){
    return setTextColor(107) + text + resetColor();
}

std::string setTextColor(int colorCode){
    return "\033[" + std::to_string(colorCode) + "m";
}

std::string ConsoleColors::resetColor() {
    return "\033[0m";
}

std::string ConsoleColors::setBackground(int colorCode) {
    return "\033[" + std::to_string(colorCode) + ";1m"; // adding 1 makes the color bold
}


