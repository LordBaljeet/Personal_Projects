#include "Direction.h"
const Direction Direction::N{
    Direction{0,-1}
};

const Direction Direction::S{
    Direction{0,1}
};

const Direction Direction::E{
    Direction{1,0}
};

const Direction Direction::W{
    Direction{-1,0}
};

const Direction Direction::NONE{
    Direction{0,0}
};

const Direction Direction::getDirection(int i){
    switch(i){
    case 0: return E;
        break;
    case 1: return N;
        break;
    case 2: return W;
        break;
    default:
        return S;
    }
}

int Direction::getEnumLikeIntegerValue(Direction dir){
    if(dir == E) return 0;
    if(dir == N) return 1;
    if(dir == W) return 2;
    return 3;
}

bool Direction::operator==(const Direction dir){
    if(dir.getCoords() == this->getCoords()){
        return true;
    }else return false;
}
