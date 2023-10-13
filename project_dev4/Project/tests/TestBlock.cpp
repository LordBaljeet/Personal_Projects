#include "Block.h"
#include "BlockFactory.h"
#include "catch.hpp"

TEST_CASE("Testing Block"){

    /*---------------*
     * GETTERS TESTS *
     *---------------*/

    SECTION("Testing Block type getters"){
        Block block = BlockFactory().instanciateBlock("rock", {0,0});
        REQUIRE(block.getName() == "rock");
        REQUIRE(block.getDisplayName() == " 0 ");
        REQUIRE(block.getFacingDirection() == Direction::NONE);
        REQUIRE(block.getType() == Type::BLOCK);
        REQUIRE(block.getPosition() == Position(0,0));
    }

    SECTION("Testing Rule type getters"){
        Block block = BlockFactory().instanciateBlock("text_rock", {0,0});
        REQUIRE(block.getName() == "text_rock");
        REQUIRE(block.getDisplayName() == "rok");
        REQUIRE(block.getFacingDirection() == Direction::NONE);
        REQUIRE(block.getType() == Type::SUBJECT);
        REQUIRE(block.getPosition() == Position(0,0));
    }

    /*---------------*
     * SETTERS TESTS *
     *---------------*/

    SECTION("Testing Block setters"){
        Block block = BlockFactory().instanciateBlock("rock", {0,0});

        REQUIRE(block.getFacingDirection() == Direction::NONE);
        block.setFacingDirection(Direction::E);
        REQUIRE(block.getFacingDirection() == Direction::E);

        REQUIRE(block.getPosition() == Position(0,0));
        block.setPosition({2,2});
        REQUIRE(block.getPosition() == Position(2,2));
    }

}
