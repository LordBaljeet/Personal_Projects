#include "BoardView.h"
#include <iostream>
#include "imagemanager.h"
#include <QLabel>

BoardView::BoardView(Board& board, QWidget *parent)
    : QGridLayout{parent}, board{board}
{
    setAlignment(Qt::AlignCenter);
    setSpacing(0);
}

void BoardView::init(){
    if(initCallCount == 0)
    {
        for (int y = 0; y < board.getHeight(); ++y) {
            for (int x = 0; x < board.getWidth(); ++x) {
                std::vector<BlockRef> clients = board.getBlocks({x,y});
                for (Block& client : clients) {
                    drawPortrait(client, {x,y});
                }
            }
        }
        reference = board;
    }
}

void BoardView::update(){

    for (int y = 0; y < board.getHeight(); ++y)
    {
        for (int x = 0; x < board.getWidth(); ++x)
        {
            Position pos(x,y);
            Block current = board.getBlock(pos);
            /*
             * temporary value until confirming that the position is inside
             * the reference board.
             */
            Block original = current;

            /*
             * position might not be inside the reference board in case of level switch and the new
             * level has different dimensions.
             */
            if(!reference.isInsideBoard(pos) || (original = reference.getBlock(pos)) != current)
            {
                std::vector<BlockRef> waitingChange = board.getBlocks(pos);
                eraseDrawingsAt(pos);
                for (const Block& pending :  waitingChange)
                {
                    drawPortrait(pending, pos);
                }
            }
        }
    }
    reference = board;
}

void BoardView::eraseDrawingsAt(Position& pos){
    QLayoutItem* target = itemAtPosition(pos.getY(), pos.getX());
    while(target != nullptr && target->widget() != nullptr)
    {
        removeWidget(target->widget());
        delete target->widget();
        target = itemAtPosition(pos.getY(), pos.getX());
    }
}

void BoardView::drawPortrait(const Block& client, const Position& pos)
{
    QPixmap* portrait = ImageManager::getImage(client.getName());
    if(portrait != nullptr)
    {
        QLabel* frame = new QLabel;
        frame->setPixmap(*portrait);
        frame->setObjectName("resetStyling");
        addWidget(frame, pos.getY(), pos.getX());
    }
}
