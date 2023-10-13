#include "Rule.h"
#include <iostream>

Rule::Rule(std::string ruleName, Board& board, GameRules& gameRules, bool instant)
    :board{board},name{ruleName},gameRules{gameRules}, instant{instant}
{}

Rule::~Rule() = default;

std::string Rule::getName() {
    return name;
}

void observable::addObserver(Observer& ob){
    observers.push_back(ob);
}

void observable::notifyWin(){
    for (Observer& ob : observers) {
        ob.updateWin();
    }
}

void observable::notifyDeath(){
    for (Observer& ob : observers) {
        ob.updateDeath();
    }
}

void observable::notifyRuleChange(Position oldPos, Position newPos, Block& ruleBlock){
    for (Observer& ob : observers) {
        ob.updateRuleChange(oldPos, newPos, ruleBlock);
    }
}

void observable::removeObserver(Observer& ob){
    observers.erase(
        std::remove_if(
            observers.begin(),
            observers.end(),
            [&](ObserverRef& observer_ref) {
                return &observer_ref.get() == &ob;
            }),
        observers.end());
}

void observable::addObservers(std::vector<ObserverRef> observers){
   for(ObserverRef ob : observers){
       addObserver(ob);
   }
}

void observable::notifyPlay(){
    for(ObserverRef ob : observers){
        ob.get().updatePlay();
    }
}

void observable::notifyStartSave(){
    for(ObserverRef ob : observers){
        ob.get().updateStartSave();
    }
}

void observable::notifyStartZero(){
    for(ObserverRef ob : observers){
       ob.get().updateStartZero();
    }
}
