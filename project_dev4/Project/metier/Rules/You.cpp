#include "You.h"
#include "Rules/inactive.h"

You::You(Board& board, GameRules& gameRules)
    :Rule("you",board, gameRules)
{}

You::~You() = default;

void You::execute(Block& targetBlock, Block& collidingBlock){
    Inactive inactive(board, gameRules);
    inactive.execute(targetBlock, collidingBlock);
}
