#include "save.h"

Save::Save(Board& board, GameRules& gameRules)
    :Rule{"save", board, gameRules}
{

}
#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wunused-parameter"
void Save::execute(Block& targetBlock, Block& collidingBlock){
    if(targetBlock.getName() == "start") notifyStartSave();
}
#pragma GCC diagnostic pop
Save::~Save() = default;
