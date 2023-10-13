#include "Push.h"
#include "Rules/RuleFactory.h"

Direction getDirection(Position  start, Position  end){
    int dx = end.getX() - start.getX();
    int dy = end.getY() - start.getY();
    if(dx != 0){
        return dx > 0 ? Direction::E : Direction::W;
    }else{
        return dy > 0 ? Direction::S : Direction::N;
    }
}

Push::Push(Board& board, GameRules& gameRules)
    :Rule("push",board, gameRules)
{}

Push::~Push() = default;

void Push::execute(Block& targetBlock, Block& collidingBlock){

    Direction coords = getDirection(collidingBlock.getPosition(), targetBlock.getPosition());
    Position newPos = targetBlock.getPosition()+coords;
    Position currentPos = targetBlock.getPosition();

    if(board.isInsideBoard(newPos)){
        //Executing destination block rule
        Block newTargetBlock = board.getBlock(newPos);
        RuleFactory rf(board, gameRules);
        std::string ruleName = gameRules.getBlockRuleName(newTargetBlock.getName(), newTargetBlock.getType());
        Rule* blockRule = rf.getRulePointer(ruleName);
        blockRule->addObserver(observers.at(0));
        blockRule->execute(newTargetBlock, targetBlock);

        //If the targetBlock was pushed, execute the block currently at its old position
        if(targetBlock.getPosition() == newPos){
            Block block = board.getBlock(currentPos);
            Rule* rule = rf.getRulePointer(gameRules.getBlockRuleName(block.getName(), block.getType()));
            rule->addObservers(observers);
            rule->execute(block, collidingBlock);

            //If the block that was pushed is a rule Block: notify change
            if(targetBlock.getType() != Type::BLOCK){
                notifyRuleChange(currentPos, newPos, targetBlock);
            }
        }
    }

}
