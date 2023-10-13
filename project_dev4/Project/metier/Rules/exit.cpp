#include "exit.h"

Exit::Exit(Board& board, GameRules& gameRules)
    :Rule{"exit", board, gameRules, true}
{

}

#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wunused-parameter"
void Exit::execute(Block& targetBlock, Block& collidingBlock)
{
    if(targetBlock.getName() == "game") exit(0);
}
#pragma GCC diagnostic pop
Exit::~Exit() = default;
