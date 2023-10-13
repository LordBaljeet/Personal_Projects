#ifndef PUSH_H
#define PUSH_H
#include "Rule.h"
/*!
 * \brief Makes the block pushable
 */
struct Push: public Rule{
    Push(Board& board, GameRules& gameRules);
    void execute(Block& targetBlock, Block& collidingBlock) override;
    ~Push()override;

};

#endif // PUSH_H
