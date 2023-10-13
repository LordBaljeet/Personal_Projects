#ifndef BOARDVIEW_H
#define BOARDVIEW_H

#include <QWidget>
#include <QKeyEvent>
#include <QGridLayout>
#include <../metier/Board.h>

/*!
 * \brief The GBoard class graphically represents the board
 */
class BoardView : public QGridLayout
{
    Q_OBJECT

    /*!
     * \brief board the board to draw
     */
    Board& board;

    /*!
     * \brief reference helper board to compare and update changes
     */
    Board reference;

    /*!
     * \brief initCallCount keeps track of the number of calls to the init method.
     * Used to make the call of this method unique as in a one time event.
     */
    int initCallCount = 0;
public:

    /*!
     * \brief GBoard The graphical board constructor. Initialises some propreties
     * \param board the board to represent
     * \param parent the parent widget, null by default.
     */
    explicit BoardView(Board& board, QWidget *parent = nullptr);

    /*!
     * \brief update called after every movement.
     * Updates the graphical board to accuratly represent the current board.
     *
     * Ultimatly, it loops through every tile of the board, gets the block
     * at the top of the tile and compares it to the one in the reference board.
     * if they are different, it clears the whole tile and redraws all of
     * the blocks at the given tile.
     *
     * In some cases, the reference board does not contain checking position,
     * in which case, the tile is also cleared and redrawn.
     */
    void update();

    /*!
     * \brief init The very first draw of the board.
     * It also initialises the reference board.
     *
     * Calling it would only have an effect the very first time.
     */
    void init();

    /*!
     * \brief eraseDrawingsAt erases the drawings at the given position.
     * \param pos the position of the drawings to erase.s
     */
    void eraseDrawingsAt(Position& pos);

    /*!
     * \brief drawPortrait draws a portrait of the given block and displays it at the
     * given position.
     * \param client the client block to draw.
     * \param pos the position where to "hang" the portrait
     */
    void drawPortrait(const Block& client, const Position& pos);
};

#endif // BOARDVIEW_H
