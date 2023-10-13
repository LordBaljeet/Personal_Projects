#include "Best.h"

Best::Best(Board& board, GameRules& gameRules)
    :Rule("best",board, gameRules)
{}

Best::~Best() = default;

#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wunused-parameter"
void Best::execute(Block& targetBlock, Block& collidingBlock){}
#pragma GCC diagnostic pop
