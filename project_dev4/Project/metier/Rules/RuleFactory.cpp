#include "RuleFactory.h"
RuleFactory::RuleFactory(Board& board, GameRules& gameRules)
    :board{board}, gameRules{gameRules}
{}

Rule* RuleFactory::getRulePointer(std::string name){
    if(rules.count(name)){
        return rules.at(name).get();
    }else{
        throw std::invalid_argument("This rule doesn't exist : " + name);
    }
}

void RuleFactory::observeRules(Observer& ob){
    for (const auto& key : rules) {
        Rule* rule = key.second.get();
        rule->addObserver(ob);
    }
}
