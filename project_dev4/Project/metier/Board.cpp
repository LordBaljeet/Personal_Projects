#include "Board.h"
#include "BlockFactory.h"
#include <cstring>
#include <functional>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <string>
#include <sstream>

std::optional<int> Board::loadLevel(std::string path, bool isSave){

    std::string line;
    BlockFactory br;
    std::ifstream level(path);
    int levelNumber;

    if(level.good())
        tiles.clear();

    try {

        std::getline(level,line);
        if(isSave){
            levelNumber = stoi(line);
            std::getline(level,line);
        }

        std::vector<std::string> result;
        std::istringstream iss(line);

        for (std::string s; iss >> s; ){
            result.push_back(s);
        }

        this->height = std::stoi(result.at(0));
        this->width = std::stoi(result.at(1));

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                tiles[{x,y}].push_back(br.instanciateBlock("void",{x,y}));
            }
        }

        result.clear();

        while ( getline (level,line) )
        {

            std::vector<std::string> result;
            std::istringstream iss(line);
            for (std::string s; iss >> s; ){
                result.push_back(s);
            }
            if(result.size() == 4){
                Position pos{std::stoi(result.at(1)),std::stoi(result.at(2))};
                Block block = br.instanciateBlock(result.at(0),pos,Direction::getDirection(std::stoi(result.at(3))));
                tiles[pos].push_back(block);
            }else{
                Position pos{std::stoi(result.at(1)),std::stoi(result.at(2))};
                Block block = br.instanciateBlock(result.at(0),pos);
                tiles[pos].push_back(block);
            }
            result.clear();
        }

    } catch (std::ios_base::failure& e) {
        throw std::invalid_argument("No file was found at the given path : " + path);
    }

    level.close();
    return levelNumber;
}

void Board::addBlock(Block& block, Position pos){
    if(!isInsideBoard(pos))
        throw std::invalid_argument("[addBlock] Position out of board : " + pos);
    tiles[pos].push_back(block);
}

Block Board::pop(Position pos){
    if(!isInsideBoard(pos))
        throw std::invalid_argument("[pop] Position out of board :" + pos);
    Block b = tiles[pos].at(tiles[pos].size()-1);
    tiles[pos].pop_back();
    return b;
}

int Board::getHeight() const{
    return height;
}

int Board::getWidth() const{
    return width;
}

const Block& Board::getBlock(Position pos){
    if(!isInsideBoard(pos))
        throw std::invalid_argument("[getBlock] Position out of board :" + pos);
    return tiles[pos].back();
}

bool Board::isInsideBoard(Position pos){
    return tiles.count(pos);
}

std::vector<BlockRef> Board::getBlocks(std::string identity, Type type){
    std::vector<BlockRef> blocksList;
    for (auto &key : tiles) {
        for (Block& b : key.second) {
            if(b.getName() == identity && b.getType() == type) {
                blocksList.push_back(b);
            }
        }
    }
    return blocksList;
}

std::vector<BlockRef> Board::getBlocks(Position pos){
    if(!isInsideBoard(pos))
        throw std::invalid_argument("[getBlocks] Position out of board :" + pos);
    std::vector<BlockRef> blocks;
    for (auto& block : tiles[pos]) {
        blocks.push_back(block);
    }
    return blocks;
}

void Board::replaceBlocks(std::string targetName, std::string replacementName){
    std::vector<BlockRef> blocksList = getBlocks(targetName, Type::BLOCK);
    for (Block&  b : blocksList) {
        Block newBlock = BlockFactory()
                .instanciateBlock(replacementName,b.getPosition(),b.getFacingDirection());
        b = newBlock;
    }
}

void Board::moveBlock(Block& block, Position destination){
    if(!isInsideBoard(destination))
        throw std::invalid_argument("[moveBlock] Position out of board :" + destination);
    pop(block.getPosition());
    block.setPosition(destination);
    addBlock(block, destination);
}

void Board::saveBoard(int levelNumber, std::string saveFolderPath){
    std::ofstream save (saveFolderPath + "/save.txt");
    save << levelNumber << std::endl;
    save<<height<<" "<<width<<std::endl;
    for (const auto &key : tiles) {
        for (Block b: key.second) {
            if(b.getName() != "void"){
                if(b.getFacingDirection() != Direction::NONE){
                    save<<b.getName()<<" "<<b.getPosition().getX()<<" "<<b.getPosition().getY()<<" "<<Direction::getEnumLikeIntegerValue(b.getFacingDirection())<<std::endl;
                }else{
                    save<<b.getName()<<" "<<b.getPosition().getX()<<" "<<b.getPosition().getY()<<std::endl;
                }
            }

        }
    }
    save.close();
}

std::ostream& operator<<(std::ostream& out, Board& board){
    Block referenceBlock = board.getBlock({0,0});
    int blockNameLength = referenceBlock.getDisplayName().size();
    for(int i = 0; i < (board.getWidth()+1) * blockNameLength; ++i)
        out << "* ";
    out << std::endl;
    for (int y = 0; y < board.getHeight(); ++y) {
        out << "* | ";
        for (int x = 0; x < board.getWidth(); ++x) {
            Block b = board.getBlock({x,y});
            out << b << " | ";
        }
        out << "*" << std::endl;
    }
    for(int i = 0; i < (board.getWidth()+1) * blockNameLength; ++i)
        out << "* ";
    return out;
}
