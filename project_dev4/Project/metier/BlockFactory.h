#ifndef BLOCKFACTORY_H
#define BLOCKFACTORY_H
#include <map>
#include <stdexcept>
#include <string>
#include "Block.h"
/*!
 * \brief The BlockFactory class is responsible of creating Block's instances on demand
 */
class BlockFactory{

    /*!
     * \brief pos the position that will be set for the instanciated block
     */
    Position pos;

    /*!
     * \brief dir the direction that will be set for the instanciated block
     */
    Direction dir;

    /*!
     * \brief blocks a mapping of all instanciable blocks by their name
     */
    std::map<std::string, Block> blocks{
        {"baba", Block("baba",Type::BLOCK, pos, " $ ",dir)},
        {"text_baba", Block("text_baba",Type::SUBJECT, pos, "bba",dir)},
        {"rock", Block("rock",Type::BLOCK, pos," 0 ", dir)},
        {"text_rock", Block("text_rock",Type::SUBJECT, pos,"rok", dir)},
        {"wall", Block("wall",Type::BLOCK,pos," = ", dir)},
        {"text_wall", Block("text_wall",Type::SUBJECT, pos,"wal", dir)},
        {"grass", Block("grass",Type::BLOCK, pos," ` ", dir)},
        {"text_grass", Block("text_grass",Type::SUBJECT, pos,"grs", dir)},
        {"metal", Block("metal",Type::BLOCK, pos,"[ ]", dir)},
        {"text_metal", Block("text_metal",Type::SUBJECT, pos,"mtl", dir)},
        {"lava", Block("lava",Type::BLOCK, pos," # ", dir)},
        {"text_lava", Block("text_lava",Type::SUBJECT, pos,"lva", dir)},
        {"flag", Block("flag",Type::BLOCK, pos," F ", dir)},
        {"text_flag", Block("text_flag",Type::SUBJECT, pos,"flg", dir)},
        {"water", Block("water",Type::BLOCK, pos,"~~~", dir)},
        {"text_water", Block("text_water",Type::SUBJECT, pos,"wtr", dir)},
        {"game", Block("game",Type::SUBJECT, pos,"gme", dir)},
        {"play", Block("play",Type::BEHAVIOR, pos,"ply", dir)},
        {"exit", Block("exit",Type::BEHAVIOR, pos,"ext", dir)},
        {"start", Block("start",Type::SUBJECT, pos,"srt", dir)},
        {"zero", Block("zero",Type::BEHAVIOR, pos,"zro", dir)},
        {"save", Block("save",Type::BEHAVIOR, pos,"sav", dir)},
        {"is", Block("is",Type::CONNECTOR, pos,"i_s", dir)},
        {"from", Block("from",Type::CONNECTOR, pos,"frm", dir)},
        {"you", Block("you",Type::BEHAVIOR, pos,"you", dir)},
        {"kill", Block("kill",Type::BEHAVIOR, pos,"kll", dir)},
        {"push", Block("push",Type::BEHAVIOR, pos,"psh", dir)},
        {"stop", Block("stop",Type::BEHAVIOR, pos,"stp", dir)},
        {"best", Block("best",Type::BEHAVIOR, pos,"bst", dir)},
        {"sink", Block("sink",Type::BEHAVIOR, pos,"snk", dir)},
        {"win", Block("win",Type::BEHAVIOR, pos,"win", dir)},
        {"void", Block("void",Type::BLOCK, pos,"   ",dir)}
    };

public:
    /*!
     * \brief BlockFactory the block factory constructor.
     */
    BlockFactory();
    /*!
     * \brief instanciateBlock instanciate the appropriate block based on its given name.
     * \param name the name of the block to instanciate
     * \return a reference to the instanciated block.
     * \throws std::invalid_argument if given name does not match a registered block.
     */
    Block instanciateBlock(std::string  name, Position  position, Direction direction = Direction::NONE);
};
#endif // BLOCKFACTORY_H
