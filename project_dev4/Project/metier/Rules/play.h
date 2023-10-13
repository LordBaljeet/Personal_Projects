#ifndef PLAY_H
#define PLAY_H

#include "Rule.h"

struct Play : public Rule
{
    Play(Board& board, GameRules& gameRules);
    void execute(Block& targetBlock, Block& collidingBlock) override;
    ~Play() override;
};

#endif // PLAY_H
