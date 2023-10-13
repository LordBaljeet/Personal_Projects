#include "GameView.h"
#include "../Config.h"
#include <QGridLayout>
#include <QLabel>
#include <QPushButton>
#include <QApplication>

GameView::GameView(QWidget *parent)
    : QWidget{parent}, game{Config::LEVELS, Config::SAVE}, board{new BoardView(game.getBoard())}
{
    createGameplayViews();
    createGlobalView();
    setFocus();
}

void GameView::createGlobalView()
{
    globalView = new QGridLayout(this);
    globalView->setRowStretch(0, 1);   // First row
    globalView->setRowStretch(1, 4);   // Middle row (4 times height)
    globalView->setRowStretch(2, 1);   // Last row
    globalView->setColumnStretch(0, 1); // First column (3 times width)
    globalView->setColumnStretch(1, 1); // Second column

    //Title
    QLabel* title = new QLabel("Baba Is You", this);
    title->setSizePolicy(QSizePolicy::Expanding,QSizePolicy::Expanding);
    title->setAlignment(Qt::AlignCenter);

    //Help Section
    QWidget* helpContainer = createHelpSection();

    //Exit Button
    QPushButton* exitBtn = new QPushButton("Exit", this);
    exitBtn->setCursor(Qt::PointingHandCursor);
    connect(exitBtn, &QPushButton::clicked, qApp, &QApplication::quit);
    exitBtn->setSizePolicy(QSizePolicy::Expanding,QSizePolicy::Expanding);

    //Home Button - restarts the game and shows the board
    QPushButton* homeBtn = new QPushButton("Home", this);
    homeBtn->setCursor(Qt::PointingHandCursor);
    connect(homeBtn, &QPushButton::clicked, this, [this](){
        game.restartGame(false);
        board->update();
        switchGameplayView(GameplayViewType::GAME);
    });
    homeBtn->setSizePolicy(QSizePolicy::Expanding,QSizePolicy::Expanding);
    homeBtn->setFocusPolicy(Qt::NoFocus);

    globalView->addWidget(title, 0, 0, 1, 2); // First row, spans 2 columns
    globalView->addWidget(gameplayViews, 1, 0);       // Middle row, first column
    globalView->addWidget(helpContainer, 1, 1);       // Middle row, second columns
    globalView->addWidget(homeBtn, 2, 0); // Last row, first columns
    globalView->addWidget(exitBtn, 2, 1);       // Last row, second column
    globalView->setAlignment(Qt::AlignHCenter);
    setLayout(globalView);

}

void GameView::keyPressEvent(QKeyEvent *event)
{
    bool ctrlPressed = (event->modifiers() & Qt::ControlModifier);

    bool sPressed = (event->key() == Qt::Key_S);

    if (ctrlPressed && sPressed)
    {
        game.saveGame();
        qDebug() << "Progress Saved";
    }
    else if(keyBindings.count(event->key()))
    {
        keyBindings[event->key()]();
        board->update();
        if(game.isFinished())
        {
            if(game.getState() == GameState::WON)
            {
                switchGameplayView(GameplayViewType::WIN);
            }
            else
            {
                switchGameplayView(GameplayViewType::LOSE);
            }
        }
    }
}

QWidget* GameView::createHelpSection()
{
    QWidget* helpContainer = new QWidget(this);
    QVBoxLayout* helpSection = new QVBoxLayout(helpContainer);
    helpSection->setAlignment(Qt::AlignCenter);
    helpSection->setSpacing(50);
    helpContainer->setObjectName("helpContainer");

    QLabel* sectionTitle = new QLabel("Key Bindings", helpContainer);
    sectionTitle->setAlignment(Qt::AlignCenter);
    sectionTitle->setObjectName("helpSectionTitle");
    helpSection->addWidget(sectionTitle);

    std::map<QString, QString> keys =
    {
        {"Arrows", "Move"},
        {"R", "Restart"},
        {"CTRL + S", "Save"},
        {"ESC", "Exit"},
    };

    int width = 300;
    int height = 50;

    for (const auto& pair : keys)
    {
        QHBoxLayout* keyContainer = new QHBoxLayout();
        QLabel* key = new QLabel(pair.first, helpContainer);
        key->setAlignment(Qt::AlignCenter);
        key->setFixedWidth(width);
        key->setFixedHeight(height);
        key->setObjectName("helpSectionKey");


        QLabel* keyLabel = new QLabel(pair.second, helpContainer);
        keyLabel->setAlignment(Qt::AlignCenter);
        keyLabel->setFixedWidth(width);
        keyLabel->setFixedHeight(height);
        keyLabel->setObjectName("helpSectionLabel");

        keyContainer->addWidget(key);
        keyContainer->addWidget(keyLabel);
        helpSection->addLayout(keyContainer);
    }

    helpContainer->setLayout(helpSection);

    return helpContainer;
}

void GameView::switchGameplayView(GameplayViewType gameplayView)
{
    gameplayViews->setCurrentIndex(static_cast<int>(gameplayView));
}

void GameView::createGameplayViews()
{
    gameplayViews = new QStackedWidget(this);

    //Board View
    QWidget* boardContainer = new QWidget(gameplayViews);
    game.guiStart();
    game.loadNextLevel();
    game.startGame();
    board->init();
    boardContainer->setLayout(board);
    boardContainer->setObjectName("boardView");
    boardContainer->show();

    //Death View
    QWidget* deathView = new QWidget(gameplayViews);
    QLabel* deathLabel = new QLabel("You Died!", deathView);
    deathLabel->setAlignment(Qt::AlignCenter);
    deathLabel->setObjectName("resetStyling");
    QVBoxLayout* deathLayout = new QVBoxLayout(deathView);
    deathLayout->addWidget(deathLabel);
    deathView->setLayout(deathLayout);
    deathView->setSizePolicy(QSizePolicy::Expanding,QSizePolicy::Expanding);

    //Win View
    QWidget* winView = new QWidget(gameplayViews);
    QLabel* winLabel = new QLabel("You Win!", winView);
    winLabel->setAlignment(Qt::AlignCenter);
    winLabel->setObjectName("resetStyling");
    winLabel->setStyleSheet("color: cyan;");
    QVBoxLayout* winLayout = new QVBoxLayout(winView);
    winLayout->addWidget(winLabel);
    winView->setLayout(winLayout);
    winView->setSizePolicy(QSizePolicy::Expanding,QSizePolicy::Expanding);

    //adding views
    gameplayViews->addWidget(boardContainer);
    gameplayViews->addWidget(deathView);
    gameplayViews->addWidget(winView);
}
