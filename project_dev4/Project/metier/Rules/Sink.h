#ifndef SINK_H
#define SINK_H
#include "Rule.h"
/*!
 * \brief Kills the non-rule colliding block and commits suicide
 */
struct Sink: public Rule{
    Sink(Board& board, GameRules& gameRules);
    void execute(Block& targetBlock, Block& collidingBlock) override;
    ~Sink() override;
};
#endif // SINK_H
