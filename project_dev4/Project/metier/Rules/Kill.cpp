#include "Kill.h"

Kill::Kill(Board& board, GameRules& gameRules)
    :Rule("kill",board, gameRules)
{}

Kill::~Kill() = default;

void Kill::execute(Block& targetBlock, Block& collidingBlock){
    if(collidingBlock.getType() == Type::BLOCK){
        board.pop(collidingBlock.getPosition());
        collidingBlock.setPosition(targetBlock.getPosition());
        notifyDeath();
    }
}
