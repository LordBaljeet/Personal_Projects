#ifndef OBSERVABLE_H
#define OBSERVABLE_H

#include "Observer.h"
#include <vector>
#include <functional>

using ObserverRef = std::reference_wrapper<Observer>;

/*!
 * \brief Represents an observable object
 */
class observable{

protected:
    std::vector<ObserverRef> observers;

public:
    /*!
     * \brief notifyWin notifies observers when the win event occurs
     */
    virtual void notifyWin();

    /*!
     * \brief notifyDeath notifies obserevers when the death event occurs
     */
    virtual void notifyDeath();

    /*!
     * \brief notifyRuleChange notifies observers when a rule has been potentially changed.
     *
     * Example : pushing a rule block might have created or removed a rule from the game.
     *
     * \param oldPos the old rule block position. Used to break the rule if needed.
     * \param newPos the new rule block position. Used to create a new rule if needed.
     * \param ruleBlock the rule block that has been moved.
     */
    virtual void notifyRuleChange(Position oldPos, Position newPos, Block& ruleBlock);

    virtual void notifyPlay();

    virtual void notifyStartSave();

    virtual void notifyStartZero();

    /*!
     * \brief addObserver adds a new observer to the list of observers
     * \param ob the new observer to add
     */
    virtual void addObserver(Observer& ob);

    /*!
     * \brief addObservers adds a list of observers to the list of observers
     * \param observers the observers to add
     */
    virtual void addObservers(std::vector<ObserverRef> observers);

    /*!
     * \brief removeObserver removes an observer from the list of observers
     * \param ob the observer to remove
     */
    virtual void removeObserver(Observer& ob);

    ~observable(){}
};

#endif // OBSERVABLE_H
