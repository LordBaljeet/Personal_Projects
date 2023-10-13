#ifndef BOARD_H
#define BOARD_H
#include "Block.h"
#include "Position.h"
#include "Enumeration.h"
#include <cstdio>
#include <map>
#include <optional>
#include<stack>
#include <vector>

using BlockRef = std::reference_wrapper<Block>;

/*!
 * \brief The Board class represents the game board
 */
class Board{

    /*!
     * \brief height of the board
     */
    int height;

    /*!
     * \brief width of the board
     */
    int width;

    /*!
     * \brief tiles of the board
     *
     * The tiles of the board are stored in a map that uses positions
     * as keys and a list of blocks as value.
     *
     * The first element in the list in the block at the very bottom
     * and the last element is the block at the very top.
     */
    std::map<Position,std::vector<Block>> tiles;

public:

    /*!
     * \brief loads the level that is found in the given file path
     *
     * \param path the path to the level file
     *
     * \param isSave determins if the level we want to load is
     * a save or a level. This information is important because the save file contains,
     * in addition to the level layout, the level number that will be returned.
     *
     * This is so we are able to load the next level when this one is finished.
     *
     * This parameter is defaulted to false.
     *
     * \return the level number if its a save file, nothing otherwise.
     *
     * \throw std::invalid_argument if the path is invalid
     */
    std::optional<int> loadLevel(std::string path, bool isSave = false);

    /*!
     * \brief Add a block to the attribute tiles
     *
     * This method adds the given block at thye very end of the list of blocks
     * and thus puts it on top of all other blocks.
     *
     * \param block the block to add
     * \param pos the position in which to add the block
     * \throw std::invalid_argument if the position is outside the board
     */
    void addBlock(Block& block, Position  pos);

    /*!
     * \brief Removes the block at the very top of the tile at the given position
     * and returns it.
     *
     * \param pos the position of the tile that contains the block
     * \return the upmost block.
     * \throw std::invalid_argument if the position is outside the board
     */
    Block pop(Position pos);

    /*!
     * \brief Save the current layout of the board in a save file in addition to the level number
     * \param levelNumber the level number
     * \param saveFolderPath the path to the folder that contains the save file
     */
    void saveBoard(int levelNumber, std::string saveFolderPath);

    /*!
     * \brief Getter for the height of the board
     * \return the height of the board
     */
    int getHeight() const;

    /*!
     * \brief Getter for the width of the board
     * \return the width of the board
     */
    int getWidth() const;

    /*!
     * \brief Gets the block at the top of the given position
     * \param pos the Position of the block
     * \return the Block at this position
     * \throw std::invalid_argument if the given position isn't in the board
     */
    const Block& getBlock(Position pos);

    /*!
     * \brief Check if a given position is in the board
     * \param pos the Position to check
     * \return true if the position is inside the board, false otherwise
     */
    bool isInsideBoard(Position pos);

    /*!
     * \brief getBlocks gets all current blocks of the same given identity
     * \param blockName the name of the blocks to retrieve
     * \param type the type of blocks to retrieve
     * \return a list of all found blocks of the same identity and type.
     */
    std::vector<BlockRef> getBlocks(std::string blockName, Type type);

    /*!
     * \brief getBlocks gets all blocks at the given position
     * \param pos the position of the blocks to retrieve
     * \return a list blocks.
     */
    std::vector<BlockRef> getBlocks(Position pos);

    /*!
     * \brief replaceBlocks replaces all blocks of name <code>targetName</code>
     * with blocks of name <code>replacementName</code>.
     *
     * It instanciates new blocs using their name given by the replacementName
     * parameter, removes blocks of name targetName and inserts the new blocks
     * in their position.
     *
     * \param targetName the name of the blocks to be replaced
     * \param replacementName the name of the replacment blocks
     */
    void replaceBlocks(std::string targetName, std::string replacementName);

    /*!
     * \brief moveBlock Moves the given block from its tile to the destination tile
     * \param block the block to move
     * \param destination the tile position
     * \throws std::invalid_argument if the given position is outside the board.
     */
    void moveBlock(Block& block, Position destination);

};

/*!
 * \brief ostream injection operator.
 * Allows the board to be injected in the ostream, and thus, be displayed.
 * \param out the ostream to inject into
 * \param board the board to be injected
 * \return the ostream after injection
 */
std::ostream& operator<<(std::ostream& out, Board& board);

#endif // BOARD_H
