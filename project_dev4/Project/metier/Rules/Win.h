#ifndef WIN_H
#define WIN_H
#include "Rule.h"
/*!
 * \brief wins the level
 */
struct Win: public Rule{
    Win(Board& board, GameRules& gameRules);
    void execute(Block& targetBlock, Block& collidingBlock) override;
    ~Win() override;
};
#endif // WIN_H
