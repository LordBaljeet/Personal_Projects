#ifndef BEST_H
#define BEST_H
#include "Rule.h"

/*!
 * \brief Adds a glowing effect the block
 */
struct Best: public Rule{
    Best(Board& board, GameRules& gameRules);
    void execute(Block& targetBlock, Block& collidingBlock) override;
    ~Best() override;
};

#endif // BEST_H
