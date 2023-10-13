#include "inactive.h"

Inactive::Inactive(Board& board, GameRules& gameRules)
    :Rule("inactive",board, gameRules)
{}

Inactive::~Inactive() = default;

void Inactive::execute(Block& targetBlock, Block& collidingBlock){
    board.moveBlock(collidingBlock, targetBlock.getPosition());
}
