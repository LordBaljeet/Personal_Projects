#ifndef RULECHAIN_H
#define RULECHAIN_H

#include "Block.h"
#include <string>
struct RuleChain{

    /*!
     * \brief subject the subject of the rule.
     */
    Block subject;
    /*!
     * \brief connector the rule connector
     */
    Block connector;
    /*!
     * \brief behavior the behavior of the rule
     */
    Block behavior;

    /*!
     * \brief Constructor constructs a rule chain
     * \param subject the rule subject
     * \param connector the rule connector
     * \param behavior the rule behavior
     */
    RuleChain(Block subject, Block connector, Block behavior);

    /*!
     * \brief isValid determins if the rule chain forms a valid rule
     * or not
     * \return true if the rule formed by the chain is valid, false otherwise
     */
    bool isValid();

    /*!
     * \brief transformsBlock determins if rule formed by this ruleChain
     * applys a transformation of blocks or not.
     *
     * That is determined if the behavior of the rule is also of subject type
     *
     * \return true if the rule formed by the chain applys a transformation,
     * false otherwise
     */
    bool transformsBlock();

    /*!
     * \brief RuleToBlockName translates the rule name to its block name alternative and returns it.
     *
     *Example 1:
     * text_wall(S) is(C) stop(B) :
     *   -> getBlockName(Type::Subject)   ==> wall
     *   -> getBlockName(Type::Connector) ==> is
     *   -> getBlockName(Type::Behavior) ==> stop
     *
     *Example 2:
     * text_wall(S) is(C) text_baba(B) :
     *   -> getBlockName(Type::Subject)   ==> wall
     *   -> getBlockName(Type::Connector) ==> is
     *   -> getBlockName(Type::Behavior) ==> baba
     *
     * \param blockType the type of the block in the rule chain for which to get the name
     * \return the block name, cropped if the block name contains 'text_'
     * \throw std::invalidArgument() if the given block type is not of Type SUBJECT,CONNECTOR
     * or BEHAVIOR
     */
    std::string RuleToBlockName(Type blockType);
};

#endif // RULECHAIN_H
