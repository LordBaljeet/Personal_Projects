#ifndef RULE_H
#define RULE_H
#include "../Board.h"
#include "../GameRules.h"
#include "../Observable.h"
#include <optional>
#include <set>
#include <string>
/*!
 * \brief Represents a game rule
 */
class Rule : public observable
{

protected:

    Board& board;
    std::string name;
    GameRules& gameRules;

public:

    inline static const std::string PLAYER_IDENTIFIER = "you";
    inline static const std::string WIN_IDENTIFIER = "win";
    const bool instant;

    /*!
     * \brief Rule the rule constructor.
     *
     * Creates a rule with the given name and saves the board the rule would apply in.
     * \param ruleName the name the rule would be identified with
     * \param board the board the rule would apply in.
     */
    Rule(std::string ruleName, Board& board, GameRules& gameRules, bool instant = false);

    /*!
     * \brief execute defines the rule nature and behavior.
     *
     * if for example the rule is STOP, this execute method would work as such and
     * prevent any other block to move through it.
     *
     * \param targetBlock the block the rule would apply to.
     * In case of <Wall Is STOP>, <Wall> is the target of the rule <STOP>.
     *
     * \param collidingBlock the player block in collision with the target block, and that would
     * be affected by the rule's nature.
     */
    virtual void execute(Block& targetBlock, Block& collidingBlock) = 0;

    /*!
     * \brief getName returns the rule name
     * \return the rule name
     */
    std::string getName();

    virtual ~Rule();
};

#endif // RULE_H
