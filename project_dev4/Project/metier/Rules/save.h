#ifndef SAVE_H
#define SAVE_H

#include "Rule.h"

struct Save : public Rule
{
    Save(Board& board, GameRules& gameRules);
    void execute(Block& targetBlock, Block& collidingBlock) override;
    ~Save() override;
};

#endif // SAVE_H
