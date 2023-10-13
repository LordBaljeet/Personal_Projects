#ifndef GAME_H
#define GAME_H
#include "Board.h"
#include "Position.h"
#include "RuleChain.h"
#include "Rules/RuleFactory.h"
#include "GameRules.h"
#include <vector>
#include <map>
#include <set>
/*!
 * \brief The Game class
 */
class Game : public Observer
{

    /*!
     * \brief player the blocks that the player controls
     */
    std::vector<BlockRef> player;
    /*!
     * \brief board the game board
     */
    Board board;
    /*!
     * \brief currentLevel the current level
     */
    int currentLevel;
    /*!
     * \brief connectors a list of all rule connectors in the board
     */
    std::vector<BlockRef> connectors;
    /*!
     * \brief rules the list of currently active game rules
     */
    GameRules rules;
    /*!
     * \brief ruleFactory the rules factory
     */
    RuleFactory ruleFactory;
    /*!
     * \brief state the state of the game
     */
    GameState state = GameState::PAUSED;
    /*!
     * \brief levelsFolderPath the path to the level folder
     */
    const std::string levelsFolderPath;
    /*!
     * \brief saveFolderPath the path to teh save folder
     */
    const std::string saveFolderPath;

public:

    /*!
     * \brief guiStart sets the current level to -2 for the gui start level
     */
    void guiStart();

    /*!
     * \brief consoleStart sets the current level to -1 for the console start level
     */
    void consoleStart();

    /*!
     * \brief The game constructor
     * \param levelsFolderPath the path to the level folder
     * \param saveFolderPath the path to the save folder
     */
    Game(const std::string levelsFolderPath, const std::string saveFolderPath);

    /*!
     * \brief indicates if the current game finished or not
     *
     * The game finishes when the player either passed all levels or died.
     *
     * \return true if the player won or died, false otherwise
     */
    bool isFinished();

    /*!
     * \brief starts the game
     *
     * It finds the player and apply the active rules at the start of the game
     */
    void startGame();

    /*!
     * \brief saves the current game layout and level number in a save file
     */
    void saveGame();

    /*!
     * \brief loads the next level and increments the current level value
     * \return true if the load went successfully, false otherwise
     */
    bool loadNextLevel();

    /*!
     * \brief loadSave loads the saved game from the save file if present,
     * calls loadNextLevel() otherwise.
     */
    void loadSave();

    /*!
     * \brief hasSave tells is a save has been made or not.
     * \return true if a save if present, false otherwise.
     */
    bool hasSave();

    /*!
     * \brief moves all player blocks towards the given direction
     *
     * The player blocks are moved starting from left to right and from
     * top to bottom. Because of that, we will need to reverse the order
     * of movment of those blocks if the player decides to move towards
     * South or East.
     *
     * \param dir the direction to move towards
     */
    void move(Direction dir);

    /*!
     * \brief fetchRule fetches the rule chain at the given rule position
     * by checking in the given checkDirection
     * \param rulePos the position of one of the blocks that form a rule chain
     * \param dir the check direction. Either HORIZONTAL or VERTICAL
     * \param block the block that is part of the rule chain
     * \return a RuleChain object if one was found, nothing otherwise
     */
    std::optional<RuleChain> fetchRule(Position rulePos, CheckDirection dir, Block& block);

    /*!
     * \brief fetchAndApplyRules fetches all rules on the board with the use of
     * the list of connectors.
     *
     * It checks both vertical and horizontal directions for any valid ruleChain and
     * applys its rule.
     */
    void fetchAndApplyRules();

    /*!
     * \brief fetchPlayer fetches all blocks controlled by the player
     * and adds them to the list of player blocks.
     */
    void fetchPlayer();

    /*!
     * \brief applyRule applies the rule described by the given rule chain
     * \param ruleChain the rule chain that describes the rule to apply
     */
    void applyRule(RuleChain ruleChain);

    /*!
     * \brief removeRule removes the rule described by the given rule chain
     * \param ruleChain the rule chain that describes the rule to remove
     */
    void removeRule(RuleChain ruleChain);

    /*!
     * \brief getState getter for the state of the game
     * \return the state of the game
     */
    GameState getState() const;

    /*!
     * \brief getBoard getter for the game board
     * \return a reference to the game board
     */
    Board& getBoard();

    /*!
     * \brief getPlayerBlocks getter for the player Blocks. Only used during tests.
     * \return the list of player blocks
     */
    std::vector<BlockRef> getPlayerBlocks() const;

    /*!
     * \brief getConnectors getter for connector blocks. Only used during tests.
     * \return a list of connector blocks
     */
    std::vector<BlockRef> getConnectors() const;

    /*!
     * \brief initialise fetches and applies the rules of the game, and
     * fetches the player blocks as well.
     *
     * It is used to initialise the game
     */
    void initialise();

    /*!
     * \brief restart restarts the game at the current level
     */
    void restartLevel();

    /*!
     * \brief restartGame restarts the game from the first level
     * \param consoleRestart if true, restarts at level 0, if false, restart
     * at level -1.
     */
    void restartGame(bool consoleRestart);

    virtual void updateWin() override;

    virtual void updateDeath() override;

    virtual void updateRuleChange(Position oldPos, Position newPos, Block& ruleBlock) override;

    virtual void updatePlay() override;

    virtual void updateStartSave() override;

    virtual void updateStartZero() override;
};


#endif // GAME_H
