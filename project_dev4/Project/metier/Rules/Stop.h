#ifndef STOP_H
#define STOP_H
#include "Rule.h"
/*!
 * \brief Prevents any block from moving in to its position
 */
struct Stop: public Rule{
    Stop(Board& board, GameRules& gameRules);
    void execute(Block& targetBlock, Block& collidingBlock) override;
    ~Stop() override;
};
#endif // STOP_H
