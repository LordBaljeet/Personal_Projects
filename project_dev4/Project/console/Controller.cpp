#include "Controller.h"
#include "../Config.h"

Controller::Controller()
    :game(Config::LEVELS, Config::SAVE), view(game.getBoard())
{

}

void Controller::play(){
    game.consoleStart();
    if(view.displayStartGame())
        game.loadSave();
    else
        game.loadNextLevel();

    gameLoop();

    switch(game.getState()){

    case GameState::WON:
        view.displayInfo("You Won! Thanks for playing Baba Is You!");
        break;
    case GameState::LOST:
        view.displayInfo("You Lost! Thanks for playing Baba Is You!");
        break;
    default:
        return;
    }
}

void Controller::gameLoop(){

    view.displayBoard();

    while(!game.isFinished()){
        std::string command;
        std::cin >> command;
        std::transform(command.begin(), command.end(), command.begin(),
                       [](unsigned char c){ return std::tolower(c); });
        if(commands.count(command)) commands[command]();
        else view.displayInfo("Invalide Command! Type [help]");
        view.displayBoard();
    }
}
