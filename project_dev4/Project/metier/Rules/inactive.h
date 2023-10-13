#ifndef INACTIVE_H
#define INACTIVE_H
#include "Rule.h"

/*!
 * \brief Makes the block translucent
 */
struct Inactive: public Rule{
    Inactive(Board& board, GameRules& gameRules);
    void execute(Block& targetBlock, Block& collidingBlock) override;
    ~Inactive() override;
};

#endif // INACTIVE_H
