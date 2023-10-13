#include "BlockFactory.h"
#include "catch.hpp"
#include "Board.h"
#include "../Config.h"

TEST_CASE("Testing Board"){

    /*---------------*
     * LOADING TESTS *
     *---------------*/

    std::string path = Config::TEST_LEVELS+"/TestLevel0.txt";

    SECTION("Testing level loading OK"){
        Board board;
        REQUIRE_NOTHROW(board.loadLevel(path));
    }
    SECTION("Testing level loading KO"){
        Board board;
        std::string path = Config::TEST_LEVELS+"/TestLevell0.txt";
        REQUIRE_THROWS(board.loadLevel(path));
    }
    SECTION("Testing save loading"){
        Board board;
        REQUIRE_NOTHROW(board.loadLevel(path));

        //saving
        Block block = board.getBlock({2,2});
        board.moveBlock(block, {1,2});
        REQUIRE_NOTHROW(board.saveBoard(69, Config::TEST_SAVE));

        //loading save
        std::optional<int> levelNumber;
        REQUIRE_NOTHROW(levelNumber = board.loadLevel(Config::TEST_SAVE+"/save.txt", true));

        REQUIRE(levelNumber.value() == 69);
        block = board.getBlock({1,2});
        REQUIRE(block.getName() == "baba");

    }

    /*---------------*
     * GETTERS TESTS *
     *---------------*/

    SECTION("Testing board attributs getters"){
        Board board;
        REQUIRE_NOTHROW(board.loadLevel(path));
        REQUIRE(board.getHeight() == 5);
        REQUIRE(board.getWidth() == 5);
    }
    SECTION("Testing getBlock OK"){
        Board board;
        REQUIRE_NOTHROW(board.loadLevel(path));
        Block block = board.getBlock({1,1});
        REQUIRE(block.getName() == "wall");
        block = board.getBlock({2,2});
        REQUIRE(block.getName() == "baba");
        block = board.getBlock({0,0});
        REQUIRE(block.getName() == "void");
    }
    SECTION("Testing getBlock KO"){
        Board board;
        REQUIRE_NOTHROW(board.loadLevel(path));
        REQUIRE_THROWS(board.getBlock({10,1}));
    }
    SECTION("Testing getBlock list full"){
        Board board;
        std::vector<BlockRef> blocks;
        REQUIRE_NOTHROW(board.loadLevel(path));
        REQUIRE_NOTHROW(blocks = board.getBlocks("wall", Type::BLOCK));
        REQUIRE(blocks.size() == 8);

        REQUIRE_NOTHROW(blocks = board.getBlocks("baba", Type::BLOCK));
        REQUIRE(blocks.size() == 1);

        REQUIRE_NOTHROW(blocks = board.getBlocks("void", Type::BLOCK));
        REQUIRE(blocks.size() == 25);
    }
    SECTION("Testing getBlock list empty"){
        Board board;
        std::vector<BlockRef> blocks;
        REQUIRE_NOTHROW(board.loadLevel(path));
        REQUIRE_NOTHROW(blocks = board.getBlocks("rock", Type::BLOCK));
        REQUIRE(blocks.size() == 0);
    }
    SECTION("Testing pop OK"){
        Board board;
        REQUIRE_NOTHROW(board.loadLevel(path));

        Block block = board.getBlock({1,1});
        REQUIRE(block.getName() == "wall");

        REQUIRE_NOTHROW(block = board.pop({1,1}));
        REQUIRE(block.getName() == "wall");
        block = board.getBlock({1,1});
        REQUIRE(block.getName() == "void");

        block = board.getBlock({2,2});
        REQUIRE(block.getName() == "baba");

        block = board.getBlock({0,0});
        REQUIRE(block.getName() == "void");
    }
    SECTION("Testing pop KO"){
        Board board;
        REQUIRE_NOTHROW(board.loadLevel(path));

        Block block = board.getBlock({1,1});
        REQUIRE(block.getName() == "wall");

        REQUIRE_THROWS(block = board.pop({10,1}));
    }

    /*-----------------------*
     * ADDING BLOCKS TESTS   *
     *-----------------------*/

    SECTION("Testing addBlock OK"){
        Board board;
        REQUIRE_NOTHROW(board.loadLevel(path));
        Block block = board.getBlock({0,0});
        REQUIRE(block.getName() == "void");
        Block newBlock = BlockFactory()
                .instanciateBlock("rock", {0,0});
        REQUIRE_NOTHROW(board.addBlock(newBlock, {0,0}));
        block = board.getBlock({0,0});
        REQUIRE(block.getName() == "rock");
    }
    SECTION("Testing addBlock KO"){
        Board board;
        REQUIRE_NOTHROW(board.loadLevel(path));
        Block newBlock = BlockFactory()
                .instanciateBlock("rock", {10,0});
        REQUIRE_THROWS(board.addBlock(newBlock, {10,0}));
    }

    /*--------------------------*
     * Replacing BLOCKS TESTS   *
     *--------------------------*/

    SECTION("Testing replacing"){
        Board board;
        std::vector<BlockRef> blocks;
        REQUIRE_NOTHROW(board.loadLevel(path));

        REQUIRE(board.getBlocks("wall", Type::BLOCK).size() == 8);
        REQUIRE(board.getBlocks("rock", Type::BLOCK).size() == 0);

        board.replaceBlocks("wall", "rock");

        REQUIRE(board.getBlocks("wall", Type::BLOCK).size() == 0);
        REQUIRE(board.getBlocks("rock", Type::BLOCK).size() == 8);
    }

    /*--------------------------*
     * moveBlock TESTS          *
     *--------------------------*/

    SECTION("Testing moveBlock OK"){
        Board board;
        REQUIRE_NOTHROW(board.loadLevel(path));

        Block block = board.getBlock({2,2});
        REQUIRE(block.getName() == "baba");

        REQUIRE_NOTHROW(board.moveBlock(block, {1,2}));

        block = board.getBlock({2,2});
        REQUIRE(block.getName() == "void");

        block = board.getBlock({1,2});
        REQUIRE(block.getName() == "baba");

        REQUIRE_NOTHROW(board.moveBlock(block, {0,2}));

        block = board.getBlock({1,2});
        REQUIRE(block.getName() == "wall");

        block = board.getBlock({0,2});
        REQUIRE(block.getName() == "baba");

    }

    SECTION("Testing moveBlock KO"){
        Board board;
        REQUIRE_NOTHROW(board.loadLevel(path));

        Block block = board.getBlock({2,2});
        REQUIRE(block.getName() == "baba");

        REQUIRE_THROWS(board.moveBlock(block, {10,2}));

    }

    /*--------------------------*
     * IsInsideBoard TESTS      *
     *--------------------------*/

    SECTION("Testing isInsideBoard true"){
        Board board;
        Position pos{3,1};
        REQUIRE_NOTHROW(board.loadLevel(path));

        REQUIRE(board.isInsideBoard(pos) == true);
    }
    SECTION("Testing isInsideBoard false"){
        Board board;
        Position pos{13,-1};
        REQUIRE_NOTHROW(board.loadLevel(path));

        REQUIRE(board.isInsideBoard(pos) == false);
    }


}
