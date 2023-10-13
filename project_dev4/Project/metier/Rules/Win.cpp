#include "Win.h"

Win::Win(Board& board, GameRules& gameRules)
    :Rule("win",board, gameRules)
{}

Win::~Win() = default;

#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wunused-parameter"

void Win::execute(Block& targetBlock, Block& collidingBlock){
    std::vector<std::string> player = gameRules.getPlayer();
    if(std::count(player.begin(), player.end(), collidingBlock.getName())){
        notifyWin();
    }else{
        board.moveBlock(collidingBlock, targetBlock.getPosition());
    }
}

#pragma GCC diagnostic pop
