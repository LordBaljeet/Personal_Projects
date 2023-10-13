#include "play.h"
#include "Rules/RuleFactory.h"

Play::Play(Board& board, GameRules& gameRules)
    :Rule{"play", board, gameRules, true}
{
}
#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wunused-parameter"
void Play::execute(Block& targetBlock, Block& collidingBlock){
    std::string startConfiguration = gameRules.getBlockRuleName("start", Type::BLOCK);
    if(targetBlock.getName() == "game" && startConfiguration != "inactive")
    {
        RuleFactory rf(board, gameRules);
        Rule* rule = rf.getRulePointer(startConfiguration);
        rule->addObservers(observers);
        Block gameBlock("start", Type::SUBJECT, {0,0}, "dwda");
        rule->execute(gameBlock, gameBlock);
    }
}
#pragma GCC diagnostic pop
Play::~Play() = default;
