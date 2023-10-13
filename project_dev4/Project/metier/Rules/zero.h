#ifndef ZERO_H
#define ZERO_H

#include "Rule.h"

struct Zero : public Rule
{
    Zero(Board& board, GameRules& gameRules);
    void execute(Block& targetBlock, Block& collidingBlock) override;
    ~Zero() override;
};

#endif // ZERO_H
