#include "mainwindow.h"
#include "GameView.h"
#include "ui_mainwindow.h"
#include <QLabel>
#include <QKeyEvent>
#include <iostream>
#include "QStackedWidget"
#include "imagemanager.h"

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent), gameView{new GameView(this)}
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    setWindowTitle("Baba Is You");
    setWindowIcon(QIcon(*ImageManager::getImage("Logo")));
    setCentralWidget(gameView);
    gameView->setObjectName("gameView");
}

MainWindow::~MainWindow()
{
    delete ui;
}
