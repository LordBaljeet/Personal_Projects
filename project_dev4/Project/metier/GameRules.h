#ifndef GAMERULES_H
#define GAMERULES_H


#include "Enumeration.h"
#include <map>
#include <set>
#include <string>
#include <vector>

/*!
 * \brief The GameRules class represents a list of game rules. It associates a block with its rules
 * using the block name
 */
class GameRules
{
    std::map<std::string, std::vector<std::string>> rules;

    std::map<std::string, std::vector<std::string>> initialiseDefaultRules();

public:
    GameRules();

    void addRule(std::string blockName, std::string ruleName);

    void removeRule(std::string blockName, std::string ruleName);

    std::string getBlockRuleName(std::string blockName, Type blockType);

    std::vector<std::string> getPlayer();

    void clear();

};

#endif // GAMERULES_H
