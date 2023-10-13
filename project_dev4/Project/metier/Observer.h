#ifndef OBSERVER_H
#define OBSERVER_H

/*!
 * \brief The Observer class represents an observer on an observable
 */
#include "Block.h"
#include "Position.h"
class Observer{

public:

    /*!
     * \brief updateWin Treats the win event
     */
    virtual void updateWin(){}

    /*!
     * \brief updateDeath Treats the death event
     */
    virtual void updateDeath(){}

    virtual void updatePlay(){}

    virtual void updateStartSave(){}

    virtual void updateStartZero(){}

#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wunused-parameter"
    /*!
     * \brief updateRuleChange Treats the rule change event.
     * \param oldPos the rule block's old position.
     * \param newPos the rule block's new position.
     * \param ruleBlock the rule block
     */
    virtual void updateRuleChange(Position oldPos, Position newPos, Block& ruleBlock){}
#pragma GCC diagnostic pop

    virtual ~Observer(){}
};

#endif // OBSERVER_H
