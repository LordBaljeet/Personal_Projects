#include "Game.h"
#include "catch.hpp"
#include "../Config.h"
#include <iostream>

TEST_CASE("Testing Game"){
    SECTION("Testing game instanciation"){
        Game game(Config::TEST_LEVELS, Config::TEST_SAVE);
        REQUIRE_NOTHROW(game.loadNextLevel());
        game.startGame();

        // Finding the player
        const std::vector<BlockRef> playerBlocks = game.getPlayerBlocks();
        REQUIRE(playerBlocks.size() == 1);
        Block playerBlock = playerBlocks[0];
        REQUIRE(playerBlock.getName() == "baba");
        Position playerPos{0,3};
        REQUIRE(playerBlock.getPosition() == playerPos);

        //finding the connectors
        const std::vector<BlockRef> connectorBlocks = game.getConnectors();
        REQUIRE(connectorBlocks.size() == 3);
        Block connectorBlock1 = connectorBlocks[0];
        Block connectorBlock2 = connectorBlocks[1];
        Block connectorBlock3 = connectorBlocks[2];
        Position pos1{2,6};
        Position pos2{5,6};
        Position pos3{2,9};
        REQUIRE(connectorBlock1.getName() == "is");
        REQUIRE(connectorBlock2.getName() == "is");
        REQUIRE(connectorBlock3.getName() == "is");
        REQUIRE(connectorBlock1.getPosition() == pos1);
        REQUIRE(connectorBlock2.getPosition() == pos2);
        REQUIRE(connectorBlock3.getPosition() == pos3);
    }

    /*-------------*
     * Rules TESTS *
     *-------------*/

    /* level layout :
     * 0 1 2 3 4 5 6 7
     * * * * * * * * *  *
     *                  *
     *       0 ~ #      *
     *   M = 0 0 0 p F *
     * $                *
     *         ~ #      *
     * * * * * * * * *  *
     *
     * 0 | rock
     * ~ | water
     * # | lava
     * M | metal
     * = | wall
     * F | flag
     * $ | baba
     * p | push
     */

    SECTION("Testing game default void rule"){
        Game game(Config::TEST_LEVELS, Config::TEST_SAVE);
        REQUIRE_NOTHROW(game.loadNextLevel());
        game.startGame();

        int xPosition = 0;

        //before:
        Block playerBlock = game.getPlayerBlocks()[0];
        Block targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::N);
        REQUIRE(targetBlock.getName() == "void");
        Position playerPos{xPosition,3};
        REQUIRE(playerBlock.getPosition() == playerPos);

        game.move(Direction::N);

        //after
        playerBlock = game.getPlayerBlocks()[0];
        playerPos = {xPosition,2};
        REQUIRE(playerBlock.getPosition() == playerPos);
    }

    SECTION("Testing game default block rule"){
        Game game(Config::TEST_LEVELS, Config::TEST_SAVE);
        REQUIRE_NOTHROW(game.loadNextLevel());
        game.startGame();

        int xPosition = 1;

        for (int i = 0; i < xPosition; ++i) {
            game.move(Direction::E);
        }


        //before:
        Block playerBlock = game.getPlayerBlocks()[0];
        Block targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::N);
        REQUIRE(targetBlock.getName() == "metal");
        Position playerPos{xPosition,3};
        REQUIRE(playerBlock.getPosition() == playerPos);

        game.move(Direction::N);

        //after
        playerBlock = game.getPlayerBlocks()[0];
        playerPos = {xPosition,2};
        REQUIRE(playerBlock.getPosition() == playerPos);
    }

    SECTION("Testing game stop rule"){
        Game game(Config::TEST_LEVELS, Config::TEST_SAVE);
        REQUIRE_NOTHROW(game.loadNextLevel());
        game.startGame();

        int xPosition = 2;

        for (int i = 0; i < xPosition; ++i) {
            game.move(Direction::E);
        }

        //before:
        Block playerBlock = game.getPlayerBlocks()[0];
        Block targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::N);
        REQUIRE(targetBlock.getName() == "wall");
        Position playerPos{xPosition,3};
        REQUIRE(playerBlock.getPosition() == playerPos);

        game.move(Direction::N);

        //after
        playerBlock = game.getPlayerBlocks()[0];
        playerPos = {xPosition,3};
        REQUIRE(playerBlock.getPosition() == playerPos);
    }

    SECTION("Testing game push rule"){
        Game game(Config::TEST_LEVELS, Config::TEST_SAVE);
        REQUIRE_NOTHROW(game.loadNextLevel());
        game.startGame();

        int xPosition = 3;

        for (int i = 0; i < xPosition; ++i) {
            game.move(Direction::E);
        }

        //before:
        Block playerBlock = game.getPlayerBlocks()[0];
        Block targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::N);
        Block targetBlock2 = game.getBoard().getBlock(targetBlock.getPosition() + Direction::N);
        REQUIRE(targetBlock.getName() == "rock");
        REQUIRE(targetBlock2.getName() == "rock");
        Position playerPos{xPosition,3};
        REQUIRE(playerBlock.getPosition() == playerPos);

        game.move(Direction::N);

        //after
        playerBlock = game.getPlayerBlocks()[0];
        targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::N);
        targetBlock2 = game.getBoard().getBlock(targetBlock.getPosition() + Direction::N);
        playerPos = {xPosition,2};
        REQUIRE(targetBlock.getName() == "rock");
        REQUIRE(targetBlock2.getName() == "rock");
        REQUIRE(playerBlock.getPosition() == playerPos);
    }

    SECTION("Testing game sink rule block death"){
        Game game(Config::TEST_LEVELS, Config::TEST_SAVE);
        REQUIRE_NOTHROW(game.loadNextLevel());
        game.startGame();

        int xPosition = 4;

        for (int i = 0; i < xPosition; ++i) {
            game.move(Direction::E);
        }

        //before:
        Block playerBlock = game.getPlayerBlocks()[0];
        Block targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::N);
        Block targetBlock2 = game.getBoard().getBlock(targetBlock.getPosition() + Direction::N);
        REQUIRE(targetBlock.getName() == "rock");
        REQUIRE(targetBlock2.getName() == "water");
        Position playerPos{xPosition,3};
        REQUIRE(playerBlock.getPosition() == playerPos);

        game.move(Direction::N);

        //after
        playerBlock = game.getPlayerBlocks()[0];
        targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::N);
        playerPos = {xPosition,2};
        REQUIRE(targetBlock.getName() == "void");
        REQUIRE(playerBlock.getPosition() == playerPos);
        REQUIRE(game.isFinished() == false);
    }

    SECTION("Testing game sink rule player death"){
        Game game(Config::TEST_LEVELS, Config::TEST_SAVE);
        REQUIRE_NOTHROW(game.loadNextLevel());
        game.startGame();

        int xPosition = 4;

        for (int i = 0; i < xPosition; ++i) {
            game.move(Direction::E);
        }

        //before:
        Block playerBlock = game.getPlayerBlocks()[0];
        Block targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::S);
        REQUIRE(targetBlock.getName() == "water");
        Position playerPos{xPosition,3};
        REQUIRE(playerBlock.getPosition() == playerPos);

        game.move(Direction::S);

        //after
        targetBlock = game.getBoard().getBlock(playerPos + Direction::S);
        REQUIRE(targetBlock.getName() == "void");
        REQUIRE(game.getState() == GameState::LOST);
        REQUIRE(game.getPlayerBlocks().size() == 0);
        REQUIRE(game.isFinished() == true);
    }
    SECTION("Testing game kill rule block death"){
        Game game(Config::TEST_LEVELS, Config::TEST_SAVE);
        REQUIRE_NOTHROW(game.loadNextLevel());
        game.startGame();

        int xPosition = 5;

        for (int i = 0; i < xPosition; ++i) {
            game.move(Direction::E);
        }

        //before:
        Block playerBlock = game.getPlayerBlocks()[0];
        Block targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::N);
        Block targetBlock2 = game.getBoard().getBlock(targetBlock.getPosition() + Direction::N);
        REQUIRE(targetBlock.getName() == "rock");
        REQUIRE(targetBlock2.getName() == "lava");
        Position playerPos{xPosition,3};
        REQUIRE(playerBlock.getPosition() == playerPos);

        game.move(Direction::N);

        //after
        playerBlock = game.getPlayerBlocks()[0];
        targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::N);
        playerPos = {xPosition,2};
        REQUIRE(targetBlock.getName() == "lava");
        REQUIRE(playerBlock.getPosition() == playerPos);
        REQUIRE(game.isFinished() == false);
    }

    SECTION("Testing game kill rule player death"){
        Game game(Config::TEST_LEVELS, Config::TEST_SAVE);
        REQUIRE_NOTHROW(game.loadNextLevel());
        game.startGame();

        int xPosition = 5;

        for (int i = 0; i < xPosition; ++i) {
            game.move(Direction::E);
        }

        //before:
        Block playerBlock = game.getPlayerBlocks()[0];
        Block targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::S);
        REQUIRE(targetBlock.getName() == "lava");
        Position playerPos{xPosition,3};
        REQUIRE(playerBlock.getPosition() == playerPos);

        game.move(Direction::S);

        //after
        targetBlock = game.getBoard().getBlock(playerPos + Direction::S);
        REQUIRE(targetBlock.getName() == "lava");
        REQUIRE(game.getState() == GameState::LOST);
        REQUIRE(game.getPlayerBlocks().size() == 0);
        REQUIRE(game.isFinished() == true);
    }

    SECTION("Testing game push rule"){
        Game game(Config::TEST_LEVELS, Config::TEST_SAVE);
        REQUIRE_NOTHROW(game.loadNextLevel());
        game.startGame();

        int xPosition = 6;

        for (int i = 0; i < xPosition; ++i) {
            game.move(Direction::E);
        }

        //before:
        Block playerBlock = game.getPlayerBlocks()[0];
        Block targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::N);
        REQUIRE(targetBlock.getName() == "push");
        Position playerPos{xPosition,3};
        REQUIRE(playerBlock.getPosition() == playerPos);

        game.move(Direction::N);

        //after
        playerBlock = game.getPlayerBlocks()[0];
        targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::N);
        playerPos = {xPosition,2};
        REQUIRE(targetBlock.getName() == "push");
        REQUIRE(playerBlock.getPosition() == playerPos);
    }
    SECTION("Testing game win rule has next level"){
        Game game(Config::TEST_LEVELS, Config::TEST_SAVE);
        REQUIRE_NOTHROW(game.loadNextLevel());
        game.startGame();

        int xPosition = 7;

        for (int i = 0; i < xPosition; ++i) {
            game.move(Direction::E);
        }

        //before:
        Block playerBlock = game.getPlayerBlocks()[0];
        Block targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::N);
        REQUIRE(targetBlock.getName() == "flag");
        Position playerPos{xPosition,3};
        REQUIRE(playerBlock.getPosition() == playerPos);
        REQUIRE(game.getState() == GameState::PLAYING);

        game.move(Direction::N);

        //after
        playerBlock = game.getPlayerBlocks()[0];
        playerPos = {1,0};
        REQUIRE(playerBlock.getPosition() == playerPos);
        REQUIRE(game.getState() == GameState::PLAYING);
        REQUIRE(game.getConnectors().size() == 1);
        REQUIRE(game.getPlayerBlocks().size() == 1);
    }
    SECTION("Testing game win rule has no next level"){
        Game game(Config::TEST_LEVELS, Config::TEST_SAVE);
        REQUIRE_NOTHROW(game.loadNextLevel());
        game.startGame();

        int xPosition = 7;

        for (int i = 0; i < xPosition; ++i) {
            game.move(Direction::E);
        }

        game.move(Direction::N);

        //before
        Block playerBlock = game.getPlayerBlocks()[0];
        Position playerPos{1,0};
        REQUIRE(playerBlock.getPosition() == playerPos);
        REQUIRE(game.getState() == GameState::PLAYING);
        REQUIRE(game.getConnectors().size() == 1);
        REQUIRE(game.getPlayerBlocks().size() == 1);

        game.move(Direction::E);

        //after
        REQUIRE(game.getState() == GameState::WON);
        REQUIRE(game.isFinished() == true);
    }

    /*-----------------------*
     * Rules Formation TESTS *
     *-----------------------*/

    /* level layout :
     * 0 1 2 3 4 5
     * * * * * * * *
     *   b         *
     * w i   r i p *
     *   u s       *
     *     =       *
     *     $       *
     *     0       *
     *             *
     *             *
     * * * * * * * *
     *
     * b | text_baba
     * w | text_wall
     * i | is
     * r | text_rock
     * p | push
     * u | you
     * s | stop
     * = | wall
     * 0 | rock
     */

    SECTION("Testing rule formation"){
        Game game(Config::TEST_RULES_DETECTION, Config::TEST_SAVE);
        REQUIRE_NOTHROW(game.loadNextLevel());
        game.startGame();

        int xPosition = 2;

        //before:
        Block playerBlock = game.getPlayerBlocks()[0];
        Block targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::N);
        Block targetBlock2 = game.getBoard().getBlock(targetBlock.getPosition() + Direction::N);
        REQUIRE(targetBlock.getName() == "wall");
        REQUIRE(targetBlock2.getName() == "stop");
        Position playerPos{xPosition,4};
        REQUIRE(playerBlock.getPosition() == playerPos);

        game.move(Direction::N);

        //after
        playerBlock = game.getPlayerBlocks()[0];
        targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::N);
        playerPos = {xPosition,3};
        REQUIRE(targetBlock.getName() == "stop");
        REQUIRE(playerBlock.getPosition() == playerPos);

        game.move(Direction::N);

        //before
        playerBlock = game.getPlayerBlocks()[0];
        targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::S);
        playerPos = {xPosition,2};
        REQUIRE(targetBlock.getName() == "wall");
        REQUIRE(playerBlock.getPosition() == playerPos);

        game.move(Direction::S);

        //after
        playerBlock = game.getPlayerBlocks()[0];
        targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::S);
        playerPos = {xPosition,2};
        REQUIRE(targetBlock.getName() == "wall");
        REQUIRE(playerBlock.getPosition() == playerPos);
    }

    SECTION("Testing rule break"){
        Game game(Config::TEST_RULES_DETECTION, Config::TEST_SAVE);
        REQUIRE_NOTHROW(game.loadNextLevel());
        game.startGame();

        int xPosition = 2;

        //before:
        Block playerBlock = game.getPlayerBlocks()[0];
        Block targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::S);
        REQUIRE(targetBlock.getName() == "rock");
        Position playerPos{xPosition,4};
        REQUIRE(playerBlock.getPosition() == playerPos);

        game.move(Direction::S);

        //after
        playerBlock = game.getPlayerBlocks()[0];
        targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::S);
        playerPos = {xPosition,5};
        REQUIRE(targetBlock.getName() == "rock");
        REQUIRE(playerBlock.getPosition() == playerPos);

        //breaking <rock is push> rule
        game.move(Direction::E);
        for (int i = 0; i < 4; ++i) {
            game.move(Direction::N);
        }

        //coming back to initial position
        for (int i = 0; i < 4; ++i) {
            game.move(Direction::S);
        }
        game.move(Direction::W);

        //trying to push the rock

        //before
        playerBlock = game.getPlayerBlocks()[0];
        targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::S);
        playerPos = {xPosition,5};
        REQUIRE(targetBlock.getName() == "rock");
        REQUIRE(playerBlock.getPosition() == playerPos);

        game.move(Direction::S);

        //after
        playerBlock = game.getPlayerBlocks()[0];
        targetBlock = game.getBoard().getBlock(playerBlock.getPosition() + Direction::S);
        playerPos = {xPosition,6};
        REQUIRE(targetBlock.getName() == "void");
        REQUIRE(playerBlock.getPosition() == playerPos);
    }
}
