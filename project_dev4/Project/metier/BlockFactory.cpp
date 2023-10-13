#include "BlockFactory.h"
#include <iostream>

BlockFactory::BlockFactory()
    :pos{}, dir{Direction::NONE}
{}

Block BlockFactory::instanciateBlock(std::string name, Position  position, Direction direction){
    if(blocks.count(name)){
        Block block = blocks.at(name);
        block.setPosition(position);
        block.setFacingDirection(direction);
        return block;
    }else{
        throw std::invalid_argument("The block doesn't exist " + name);
    }
}
