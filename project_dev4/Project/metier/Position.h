#ifndef POSITION_H
#define POSITION_H
#include "Direction.h"
#include <compare>
#include <ostream>
class Position{

    /*!
     * \brief y the position on the y axis
     */
    int y;

    /*!
     * \brief x the position on the x axis
     */
    int x;

public:

    /*!
     * \brief Constructs a Position instance with the given x and y values
     * \param x the value of x
     * \param y the value of y
     */
    Position(int x = 0, int y = 0);

    /*!
     * \brief Constructs a Position from a pair of ints representing x and y values
     * \param positionValues the pair containing the position values
     */
    Position(std::pair<int,int> positionValues);

    /*!
     * \brief getter for the x value
     * \returns the value of x
     */
    int getX() const;

    /*!
     * \brief getter for the y value
     * \returns the value of y
     */
    int getY() const;

    /*!
     * \brief setter for the x value
     * \param the new value of x
     */
    void setX(int newX);

    /*!
     * \brief getter for the y value
     * \returns the value of y
     */
    void setY(int newY);

    /*!
     * \brief modifies the position's x and y values by adding their values
     * to the direction's dx and dy values.
     *
     * These values are as such:
     * N: dx = 0, dy = -1;
     * S: dx = 0, dy = 1;
     * E: dx = -1, dy = 0;
     * W: dx = 1, dy = 0;
     * NONE: dx = 0, dy = 0;
     *
     * \param dir the direction part of the addition
     * \return the current position modified
     */
    Position& operator+=(const Direction dir);

    /*!
     * \brief modifies the position's x and y values by adding their values
     * to the other position's x and y values.
     *
     * \param otherPos the other position that is part of the addition
     * \return the current position modified
     */
    Position& operator+=(const Position otherPos);

    /*!
     * \brief modifies the position's x and y values by substracting their values
     * to the direction's dx and dy values.
     *
     * These values are as such:
     * N: dx = 0, dy = -1;
     * S: dx = 0, dy = 1;
     * E: dx = -1, dy = 0;
     * W: dx = 1, dy = 0;
     * NONE: dx = 0, dy = 0;
     *
     * \param dir the direction part of the substraction
     * \return the current position modified
     */
    Position& operator-=(const Direction dir);

    /*!
     * \brief modifies the position's x and y values by substracting their values
     * to the other position's x and y values.
     *
     * \param otherPos the other position that is part of the substraction
     * \return the current position modified
     */
    Position& operator-=(const Position car);

    /*!
     * \brief creates a new position equal to the current position after addition by
     * the direction and returns it
     *
     * \param dir the direction part of the addition
     * \return a new position being the result of the addition.
     */
    Position operator+(const Direction dir);

    /*!
     * \brief creates a new position equal to the current position after addition by
     * the other position and returns it
     *
     * \param otherPosition the other position that is part of the addition
     * \return a new position being the result of the addition.
     */
    Position operator+(const Position otherPosition);

    /*!
     * \brief creates a new position equal to the current position after substraction by
     * the direction and returns it
     *
     * \param dir the direction part of the substraction
     * \return a new position being the result of the substraction.
     */
    Position operator-(const Direction dir);

    /*!
     * \brief creates a new position equal to the current position after substraction by
     * the other position and returns it
     *
     * \param otherPosition the other position that is part of the substraction
     * \return a new position being the result of the substraction.
     */
    Position operator-(const Position otherPosition);

    /*!
     * \brief first compares the 2 positions y values, and if they
     * are equivalent, compares their x values
     *
     * \param p the other position to compare to
     * \return the comparaison result
     */
    auto operator<=>(const Position& p) const = default;

};

/*!
 * \brief the out stream injection operator.
 * Allows for a position to be injected in an out stream
 * \param out the stream to be injected in
 * \param pos the position to inject
 * \return the ostream after injection.
 */
std::ostream& operator<<(std::ostream& out, Position& pos);

/*!
 * \brief the string concatenation operator
 * Allows for a position to be concatenated in a string, and
 * thus, to be displayed and printed
 * \param str the string to be concatenated into
 * \param pos the position to concatenate
 * \return the string after concatenation.
 */
std::string operator+(const std::string& str, Position& pos);
#endif // POSITION_H
