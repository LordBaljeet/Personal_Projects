#include "../Config.h"
#include "View.h"

#ifdef _WIN32
#include <conio.h>
#include <synchapi.h>
#define CLEAR_SCREEN "cls"

#else
#define CLEAR_SCREEN "clear"
#endif

#include <time.h>
#include <iomanip>
#include <fstream>
#include <sstream>
#include <chrono>
#include <thread>

bool hasSave(){
    std::ifstream file(Config::SAVE+"/save.txt");
    file.seekg(0,std::ios::end);
    return file.tellg();
}

View::View(Board& board)
    :board{board}
{}

void View::displayBoard(){
    system(CLEAR_SCREEN);
    std::cout << board << std::endl;
}

void View::displayHelp(){
    system(CLEAR_SCREEN);
    std::cout<<"-------------------------------------------"<<std::endl;
    std::cout<<"|               ***HELP!***               |"<<std::endl;
    std::cout<<"-------------------------------------------"<<std::endl;
    std::cout<<"|                                         |"<<std::endl;
    std::cout<<"|       [ Z Q S D  ]    |  Move           |"<<std::endl;
    std::cout<<"|       [ W A S D  ]    |  Move           |"<<std::endl;
    std::cout<<"|       [   save   ]    |  save the game  |"<<std::endl;
    std::cout<<"|       [     R    ]    |  restart        |"<<std::endl;
    std::cout<<"|       [   exit   ]    |  exit the game  |"<<std::endl;
    std::cout<<"|                                         |"<<std::endl;
    std::cout<<"-------------------------------------------"<<std::endl;
    getchar();
    std::cout << "\nPress Enter to continue..." << std::endl;
    getchar();
}

bool View::displayStartGame(){
    std::string command;
    do{
        system(CLEAR_SCREEN);
        std::cout<<"----------------------------------------"<<std::endl;
        std::cout<<"|         WELCOME TO BABA IS YOU       |"<<std::endl;
        std::cout<<"----------------------------------------"<<std::endl;
        std::cout<<"|                                      |"<<std::endl;
        std::cout<<"|   type [ play ] to start the game    |"<<std::endl;
        std::cout<<"|   type [ help ] to get help          |"<<std::endl;
        std::cout<<"|                                      |"<<std::endl;
        std::cout<<"----------------------------------------"<<std::endl;

        std::cin >> command;
        std::transform(command.begin(), command.end(), command.begin(),
                       [](unsigned char c){ return std::tolower(c); });
    }while(command != "play" && command != "help" && command != "exit");
    if(command == "exit") exit(0);
    if(command == "play"){
        return getStartingChoice();
    }
    displayHelp();
    return displayStartGame();
}

bool View::getStartingChoice(){
    if(!hasSave()) return 0;
    std::string command;
    do{
        system(CLEAR_SCREEN);
        std::cout<<"----------------------------------------"<<std::endl;
        std::cout<<"|         WELCOME TO BABA IS YOU       |"<<std::endl;
        std::cout<<"----------------------------------------"<<std::endl;
        std::cout<<"|                                      |"<<std::endl;
        std::cout<<"|   type [ 0 ] to start from level 0   |"<<std::endl;
        std::cout<<"|   type [ 1 ] to start from last save |"<<std::endl;
        std::cout<<"|                                      |"<<std::endl;
        std::cout<<"----------------------------------------"<<std::endl;

        std::cin >> command;
        std::transform(command.begin(), command.end(), command.begin(),
                       [](unsigned char c){ return std::tolower(c); });

    }while(command != "0" && command != "1" && command != "exit");
    if(command == "exit") exit(0);
    return stoi(command);
}

void View::displayInfo(std::string message){
    std::cout<< std::string(message.size()+11, '-')<<std::endl;
    std::cout<< std::setw(0) <<"|  [!]  "<< message << "  |"<<std::endl;
    std::cout<< std::string(message.size()+11, '-')<<std::endl;
    std::this_thread::sleep_for(std::chrono::seconds(1));
}
