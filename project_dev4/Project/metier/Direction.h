#ifndef DIRECTION_H
#define DIRECTION_H
#include <utility>

/*!
 * \brief The Direction class Represents a direction : N W S E
 */
class Direction{
    std::pair<int,int> coord;
    Direction(int dx, int dy)
        :coord({dx,dy})
    {}
public:
    Direction(const Direction&) = default;
    Direction& operator=(const Direction& dir) = default;
    static const Direction N;
    static const Direction S;
    static const Direction E;
    static const Direction W;
    static const Direction NONE;
    static const Direction getDirection(int i);
    static int getEnumLikeIntegerValue(Direction dir);
    std::pair<int, int> getCoords() const{
        return coord;
    }
    bool operator ==(const Direction dir);
};

#endif // DIRECTION_H
