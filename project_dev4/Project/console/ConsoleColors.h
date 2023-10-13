#ifndef CONSOLECOLORS_H
#define CONSOLECOLORS_H


#include <iostream>

class ConsoleColors {
public:
    /***************************
     * DARKER FOREBROUND COLORS
     **************************/

    static std::string F_RED(const std::string& text);

    static std::string F_BLACK(const std::string& text);

    static std::string F_GREEN(const std::string& text);

    static std::string F_YELLOW(const std::string& text);

    static std::string F_BLUE(const std::string& text);

    static std::string F_MAGENTA(const std::string& text);

    static std::string F_CYAN(const std::string& text);

    static std::string F_WHITE(const std::string& text);

    /****************************
     * LIGHTER FOREBROUND COLORS
     ***************************/

    static std::string F_B_RED(const std::string& text);

    static std::string F_B_BLACK(const std::string& text);

    static std::string F_B_GREEN(const std::string& text);

    static std::string F_B_YELLOW(const std::string& text);

    static std::string F_B_BLUE(const std::string& text);

    static std::string F_B_MAGENTA(const std::string& text);

    static std::string F_B_CYAN(const std::string& text);

    static std::string F_B_WHITE(const std::string& text);

    /***************************
     * DARKER BACKGROUND COLORS
     **************************/

    static std::string B_RED(const std::string& text);

    static std::string B_BLACK(const std::string& text);

    static std::string B_GREEN(const std::string& text);

    static std::string B_YELLOW(const std::string& text);

    static std::string B_BLUE(const std::string& text);

    static std::string B_MAGENTA(const std::string& text);

    static std::string B_CYAN(const std::string& text);

    static std::string B_WHITE(const std::string& text);

    /****************************
     * LIGHTER BACKGROUND COLORS
     ***************************/

    static std::string B_B_RED(const std::string& text);

    static std::string B_B_BLACK(const std::string& text);

    static std::string B_B_GREEN(const std::string& text);

    static std::string B_B_YELLOW(const std::string& text);

    static std::string B_B_BLUE(const std::string& text);

    static std::string B_B_MAGENTA(const std::string& text);

    static std::string B_B_CYAN(const std::string& text);

    static std::string B_B_WHITE(const std::string& text);

private:
    // Helper method to set the text color
    static std::string setTextColor(int colorCode);

    // Helper method to reset the text color
    static std::string resetColor();

    // Helper method to set the background color
    static std::string setBackground(int colorCode);
};


#endif // CONSOLECOLORS_H
