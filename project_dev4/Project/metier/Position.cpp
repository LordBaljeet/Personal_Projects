#include "Position.h"

int assignValue(int value);

Position::Position(int x, int y)
    : y{y}, x{x}
{}

Position::Position(std::pair<int,int> coords)
    :y{coords.second},x{coords.first}
{}

int Position::getX() const{
    return this->x;
}

int Position::getY() const{
    return this->y;
}

void Position::setX(int newX){
    this->x = newX;
}

void Position::setY(int newY){
    this->y = newY;
}

Position Position::operator+(const Position otherPosition){
    Position pos{x+otherPosition.getX(), y+otherPosition.getY()};
    return pos;
}

Position Position::operator+(const Direction dir){
    Position pos{dir.getCoords().first, dir.getCoords().second};
    return *this + pos;
}

Position& Position::operator+=(const Position otherPosition){
    *this = *this + otherPosition;
    return *this;
}

Position& Position::operator+=(const Direction dir){
    Position pos{dir.getCoords().first, dir.getCoords().second};
    return *this += pos;
}

Position& Position::operator-=(const Position otherPosition){
    Position pos{-otherPosition.getX(), -otherPosition.getY()};
    return *this += pos;
}

Position& Position::operator-=(const Direction dir){
    Position pos{dir.getCoords().first, dir.getCoords().second};
    return *this -= pos;
}

Position Position::operator-(const Position otherPosition){
    Position pos{-otherPosition.getX(), -otherPosition.getY()};
    return *this + pos;
}

Position Position::operator-(const Direction dir){
    Position pos{dir.getCoords().first, dir.getCoords().second};
    return *this - pos;
}

std::ostream& operator<<(std::ostream& out, Position& pos){
    return out << "{" << pos.getX() << ", " << pos.getY() << "}";
}

std::string operator+(const std::string& str, Position& pos){
    return str+"{" + std::to_string(pos.getX()) + ", " + std::to_string(pos.getY()) + "}";
}
