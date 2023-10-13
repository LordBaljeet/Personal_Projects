#include "zero.h"

Zero::Zero(Board& board, GameRules& gameRules)
    :Rule{"zero", board, gameRules}
{

}
#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wunused-parameter"
void Zero::execute(Block& targetBlock, Block& collidingBlock){
    if(targetBlock.getName() == "start") notifyStartZero();
}
#pragma GCC diagnostic pop
Zero::~Zero() = default;
