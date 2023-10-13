#ifndef RULEFACTORY_H
#define RULEFACTORY_H
#include "../GameRules.h"
#include "../Observer.h"
#include "Push.h"
#include "Kill.h"
#include "exit.h"
#include "play.h"
#include "save.h"
#include "zero.h"
#include "You.h"
#include "inactive.h"
#include "Stop.h"
#include "Win.h"
#include "Best.h"
#include "Sink.h"
#include <stdexcept>
#include <string.h>
#include <memory>

/*!
 * \brief The RuleFactory class is responsible of creating Rule's instances on demand
 */
class RuleFactory{
private:
    Board& board;
    GameRules& gameRules;
    std::map<std::string, std::shared_ptr<Rule>> rules{
        {"win", std::make_shared<Win>(Win(board,gameRules))},
        {"stop", std::make_shared<Stop>(Stop(board,gameRules))},
        {"push", std::make_shared<Push>(Push(board,gameRules))},
        {"best", std::make_shared<Best>(Best(board,gameRules))},
        {"sink", std::make_shared<Sink>(Sink(board,gameRules))},
        {"kill", std::make_shared<Kill>(Kill(board,gameRules))},
        {"you", std::make_shared<You>(You(board,gameRules))},
        {"inactive", std::make_shared<Inactive>(Inactive(board,gameRules))},
        {"play", std::make_shared<Play>(Play(board,gameRules))},
        {"exit", std::make_shared<Exit>(Exit(board,gameRules))},
        {"save", std::make_shared<Save>(Save(board,gameRules))},
        {"zero", std::make_shared<Zero>(Zero(board,gameRules))},
    };
public:
    RuleFactory(Board& board, GameRules&gameRule);

    /*!
     * \brief observeRules adds an observer to all rules.
     * \param ob the observer to add
     */
    void observeRules(Observer& ob);

    /*!
     * \brief getRulePointer returns the appropriate rule pointer based on its given name.
     * \param name the name of the rule to return
     * \return a pointer to the specified rule if it exists.
     * \throws std::invalid_argument if given name does not match a registered rule.
     */
    Rule* getRulePointer(std::string name);
};
#endif // RULEFACTORY_H
