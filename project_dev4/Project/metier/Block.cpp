#include "Block.h"

Block::Block(std::string name, Type type, Position pos, std::string displayName, const Direction facing)
    :pos{pos}, name{name}, type{type}, facing{facing}, displayName{displayName}
{}

Direction Block::getFacingDirection() const{
    return facing;
}

void Block::setFacingDirection(Direction newFacingDirection){
    facing = newFacingDirection;
}

std::string Block::getName() const{
    return name;
}

std::string Block::getDisplayName() const{
    return displayName;
}

Position Block::getPosition() const{
    return pos;
}

Type  Block::getType() const{
    return type;
}

void Block::setPosition(const Position  newPos){
    pos = newPos;
}

bool operator==(Block& block, Block& other){
    return block.getName() == other.getName();
}

std::ostream& operator<<(std::ostream& out, Block& b){
    return out << b.getDisplayName();
}
