#ifndef YOU_H
#define YOU_H
#include "Rule.h"
/*!
 * \brief Indicates the blocks that the player controls. Works exactly like the inactive rule when executed.
 */
struct You: public Rule{
    You(Board& board, GameRules& gameRules);
    void execute(Block& targetBlock, Block& collidingBlock) override;
    ~You() override;
};

#endif // YOU_H
