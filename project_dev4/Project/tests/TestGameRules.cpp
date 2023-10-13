#include "GameRules.h"
#include "catch.hpp"

TEST_CASE("Testing GameRules"){

    SECTION("Testing getRule non-existant"){
        GameRules gr;
        REQUIRE(gr.getBlockRuleName("wall", Type::BLOCK) == "inactive");
        REQUIRE(gr.getBlockRuleName("stop", Type::BEHAVIOR) == "push");
    }

    SECTION("Testing getRule existant not empty"){
        GameRules gr;
        gr.addRule("wall", "stop");
        REQUIRE(gr.getBlockRuleName("wall", Type::BLOCK) == "stop");
        gr.addRule("wall", "push");
        REQUIRE(gr.getBlockRuleName("wall", Type::BLOCK) == "push");
    }

    SECTION("Testing getRule existant empty"){
        GameRules gr;
        gr.addRule("wall", "stop");
        gr.removeRule("wall", "stop");
        REQUIRE(gr.getBlockRuleName("wall", Type::BLOCK) == "inactive");
    }

    SECTION("Testing getPlayer empty"){
        GameRules gr;
        REQUIRE(gr.getPlayer().size() == 0);
        gr.addRule("wall", "you");
        gr.removeRule("wall", "you");
        REQUIRE(gr.getPlayer().size() == 0);
    }

    SECTION("Testing getPlayer not empty"){
        GameRules gr;
        gr.addRule("wall", "you");
        REQUIRE(gr.getPlayer().size() == 1);
        gr.addRule("rock", "you");
        gr.addRule("baba", "you");
        REQUIRE(gr.getPlayer().size() == 3);
    }

    SECTION("Testing clear"){
        GameRules gr;
        gr.addRule("wall", "you");
        gr.addRule("rock", "you");
        gr.addRule("baba", "you");
        REQUIRE(gr.getBlockRuleName("wall", Type::BLOCK) == "you");
        gr.clear();
        REQUIRE(gr.getBlockRuleName("wall", Type::BLOCK) == "inactive");
    }
}
