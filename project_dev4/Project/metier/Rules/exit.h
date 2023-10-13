#ifndef EXIT_H
#define EXIT_H

#include "Rule.h"

struct Exit : public Rule
{
    Exit(Board& board, GameRules& gameRules);
    void execute(Block& targetBlock, Block& collidingBlock) override;
    ~Exit() override;
};

#endif // EXIT_H
