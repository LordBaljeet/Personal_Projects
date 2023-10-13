#include "Stop.h"

Stop::Stop(Board& board, GameRules& gameRules)
    :Rule("stop",board, gameRules)
{}

Stop::~Stop() = default;

#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wunused-parameter"
void Stop::execute(Block& targetBlock, Block& collidingBlock){}
#pragma GCC diagnostic pop
