#ifndef BLOCK_H
#define BLOCK_H
#include "Enumeration.h"
#include "Position.h"
#include <map>
#include <string>
#include <variant>
#include <vector>
/*!
 * \brief The Block class Represents a block in the board, whether textual or not
 */
class Block{
    Position  pos;
    std::string name;
    Type type;
    Direction facing;
    std::string displayName;

public:

    /*!
     * \brief Block Constructs a block of the game
     * \param name the block name aka its identity
     * \param type the block type : Block/Subject/Connector/Behaviour
     * \param pos the block position in the board.
     * \param facing the direction the block is facing. NONE by default
     */
    Block(std::string name, Type type, Position pos,std::string displayName, Direction facing = Direction::NONE);

    /*!
     * \brief setDirection sets the direction the block is facing towards
     * \param dir the new facing direction
     */
    void setFacingDirection(Direction dir);

    /*!
     * \brief setPosition Setter for the position of the Block
     * \param pos the new position of the current Block
     */
    void setPosition(const Position pos);

    /*!
     * \brief getPosition Getter for the position of a Block
     * \return the position of the Block
     */
    Position getPosition() const;

    /*!
     * \brief getName gets the name of the block aka what the block represent.
     * \return a string representing the name of the block.
     */
    std::string getName() const;

    /*!
     * \brief getConsoleName returns the console representation string
     * \return the console representation string
     */
    std::string getDisplayName() const;

    /*!
     * \brief getType a getter for the type of the block
     * \return the block type
     */
    Type getType() const;

    /*!
     * \brief getFaceingDirection gets the direction the block is currently faceing
     * \return a Cardinal literal representing the faceing direction of the block.
     */
    Direction getFacingDirection() const;


};

/*!
 * \brief ostream injection operator.
 * Allows the block to be injected in the ostream, and thus, be displayed.
 * \param out the ostream to inject into
 * \param block the block to be injected
 * \return the ostream after injection
 */
std::ostream& operator<<(std::ostream& s, Block& block);

/*!
 * \brief compares 2 blocks
 * \param block the first block in the comparaison pair
 * \param other the other block to compare to
 * \return true if both blocks are equivalent, false otherwise
 */
bool operator==(Block& block,Block& other);
#endif // BLOCK_H
