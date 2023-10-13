#ifndef KILL_H
#define KILL_H
#include "Rule.h"

/*!
 * \brief Kills any non-rule block that collides with it.
 */
struct Kill: public Rule{
    Kill(Board& board, GameRules& gameRules);
    void execute(Block& targetBlock, Block& collidingBlock) override;
    ~Kill() override;
};
#endif // KILL_H
