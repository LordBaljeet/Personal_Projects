#ifndef CONTROLLER_H
#define CONTROLLER_H

#include <iostream>
#include <string>
#include <Game.h>
#include <View.h>
#include <cctype>
#ifdef _WIN32
#include <synchapi.h>
#endif
/*!
 * \brief The Controller class
 */
class Controller
{
    Game game;
    View view;

    std::map<std::string, std::function<void()>> commands = {
    {"z", [this](){game.move(Direction::N);}},
    {"w", [this](){game.move(Direction::N);}},
    {"q", [this](){game.move(Direction::W);}},
    {"a", [this](){game.move(Direction::W);}},
    {"s", [this](){game.move(Direction::S);}},
    {"d", [this](){game.move(Direction::E);}},
    {"save", [this](){game.saveGame(); view.displayInfo("game saved successfully");}},
    {"exit", [](){exit(0);}},
    {"help", [this](){view.displayHelp();}},
    {"r", [this](){game.restartLevel();}},

};

    /*!
     * \brief gameLoop the game loop
     */
    void gameLoop();

public:
    /*!
     * \brief Controller constructor
     */
    Controller();
    /*!
     * \brief play allows the player to play the game
     */
    void play();
};
#endif // CONTROLLER_H
