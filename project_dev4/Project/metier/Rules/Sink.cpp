#include "Sink.h"
#include "Kill.h"

Sink::Sink(Board& board, GameRules& gameRules)
    :Rule("sink",board, gameRules)
{}

Sink::~Sink() = default;

void Sink::execute(Block& targetBlock, Block& collidingBlock){
    if(collidingBlock.getType() == Type::BLOCK){
        Kill kill(board, gameRules);
        kill.addObservers(observers);
        kill.execute(targetBlock, collidingBlock);
        kill.execute(targetBlock, targetBlock);
    }else {
        board.moveBlock(collidingBlock, targetBlock.getPosition());
    }
}
