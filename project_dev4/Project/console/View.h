#ifndef VIEW_H
#define VIEW_H
#include "Board.h"
#include <iostream>
class View
{
    Board& board;
public:
    View(Board& board);

    void displayBoard();

    void displayHelp();

    void displayInfo(std::string message);

    bool displayStartGame();

    bool getStartingChoice();

};

#endif // VIEW_H
