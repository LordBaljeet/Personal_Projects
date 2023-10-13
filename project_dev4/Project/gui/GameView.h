#ifndef GAMEVIEW_H
#define GAMEVIEW_H

#include <QObject>
#include <QWidget>
#include "../metier/game.h"
#include "BoardView.h"
#include "qstackedwidget.h"
#include "GameplayViewType.h"
#include <QApplication>
/*!
 * \brief The GameView class graphically represents the game
 */
class GameView : public QWidget
{
    Q_OBJECT
    /*!
     * \brief game the game to represent
     */
    Game game;
    /*!
     * \brief board the graphical board of the game
     */
    BoardView* board;
    /*!
     * \brief globalView basically the window with
     * the board, action buttons and a help section.
     */
    QGridLayout* globalView;
    /*!
     * \brief gameplayViews the different views the player will see
     * during his gameplay such as the board, the win screen and the
     * lose screen.
     */
    QStackedWidget* gameplayViews;

    /*!
     * \brief keyBindings the different keys used during gameplay
     * associated with their respective action(s).
     */
    std::map<int, std::function<void()>> keyBindings =
    {
        //Moving Up
        {Qt::Key_W, [this](){game.move(Direction::N);}},
        {Qt::Key_Z, [this](){game.move(Direction::N);}},
        {Qt::Key_Up, [this](){game.move(Direction::N);}},
        //Moving Left
        {Qt::Key_A, [this](){game.move(Direction::W);}},
        {Qt::Key_Q, [this](){game.move(Direction::W);}},
        {Qt::Key_Left, [this](){game.move(Direction::W);}},
        //Moving Down
        {Qt::Key_S, [this](){game.move(Direction::S);}},
        {Qt::Key_Down, [this](){game.move(Direction::S);}},
        //Moving Right
        {Qt::Key_D, [this](){game.move(Direction::E);}},
        {Qt::Key_Right, [this](){game.move(Direction::E);}},
        //Quitting
        {Qt::Key_Escape, [](){QApplication::quit();}},
        //Restarting Level
        {Qt::Key_R, [this](){game.restartLevel();}},
        //Restarting Game | Returning Home
        {Qt::Key_H, [this](){game.restartGame(false);}},
    };

public:
    /*!
     * \brief GameView the graphical game constructor
     * creates the global view as well as the gameplay views.
     *
     * \param parent the parent widget, null by default.
     */
    explicit GameView(QWidget *parent = nullptr);
    /*!
     * \brief keyPressEvent manages the key press event.
     * Ultimatly, if the action binded to the key(s) pressed
     * is present in the keyBindings map, it calls its associated function.
     *
     * if the key(s) is (are) binded to an action that cannot be set inside the map
     * (for example: an action that requires 2 keys at the same time -> CTRL + S)
     * then this method will take care of it.
     *
     * It is also responsable of switching to either the win screen or lose screen
     * at the end of the game.
     *
     * \param event the key press event
     */
    void keyPressEvent(QKeyEvent *event);

private:
    /*!
     * \brief createGlobalView creates the global view grid with the gameplay screen currently active,
     * the action buttons and the help section.
     */
    void createGlobalView();
    /*!
     * \brief switchGameplayView switches the gameplay view to the specified one.
     * \param gameplayView the desired gameplay view
     */
    void switchGameplayView(GameplayViewType gameplayView);
    /*!
     * \brief createGameplayViews creates the board, win view and lose view
     */
    void createGameplayViews();
    /*!
     * \brief createHelpSection creates the help section
     * \return the widget that holds and represenets the help section
     */
    QWidget* createHelpSection();

signals:

};

#endif // GAMEVIEW_H
