#ifndef DIRECTIONUTILS_H
#define DIRECTIONUTILS_H


#include "Direction.h"
#include "Position.h"
class DirectionUtils : private Direction
{
public:
    DirectionUtils();
    static Direction getDirection(Position  start, Position  end){
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();
        if(dx != 0){
            return dx > 0 ? Direction::E : Direction::W;
        }else{
            return dy > 0 ? Direction::E : Direction::W;
        }
    }
};

#endif // DIRECTIONUTILS_H
