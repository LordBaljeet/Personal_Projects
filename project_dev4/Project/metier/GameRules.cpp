#include "GameRules.h"
#include "Rules/Rule.h"

GameRules::GameRules()
    :rules{initialiseDefaultRules()}
{}

void GameRules::addRule(std::string blockName, std::string ruleName){
    std::string currentBlockRule = getBlockRuleName(blockName, Type::BLOCK);
    if(!std::count(rules[blockName].begin(), rules[blockName].end(), ruleName)){
        if(currentBlockRule == Rule::PLAYER_IDENTIFIER)
            rules[blockName].insert(rules[blockName].end(),ruleName);
        else
            rules[blockName].push_back(ruleName);
    }
}

void GameRules::removeRule(std::string blockName, std::string ruleName){
    if(rules.contains(blockName)){
        std::vector<std::string>& blockRules = rules[blockName];
        blockRules.erase(std::find(blockRules.begin(), blockRules.end(), ruleName));
    }
}

std::string GameRules::getBlockRuleName(std::string blockName, Type blockType){
    if(blockType != Type::BLOCK)
        return rules["rule"].back();
    if(rules.contains(blockName) && !rules[blockName].empty())
        return rules[blockName].back();
    return rules["block"].back();
}

std::map<std::string, std::vector<std::string>> GameRules::initialiseDefaultRules(){
    return {
        {"rule", {"push"}},
        {"block", {"inactive"}}
    };
}

std::vector<std::string> GameRules::getPlayer(){
    std::vector<std::string> player;
    for(const auto& pair : rules){
        if(std::count(pair.second.begin(), pair.second.end(), Rule::PLAYER_IDENTIFIER)){
            player.push_back(pair.first);
        }
    }
    return player;
}

void GameRules::clear(){
    rules.clear();
    rules = initialiseDefaultRules();
}
