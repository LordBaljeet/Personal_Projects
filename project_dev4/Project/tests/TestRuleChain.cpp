#include "BlockFactory.h"
#include "RuleChain.h"
#include"catch.hpp"

TEST_CASE("Testing RuleChain"){

    /*---------------------*
     * INSTANCIATION TESTS *
     *---------------------*/

    SECTION("Testing instanciation S-C-B"){
        BlockFactory bf;
        Block s = bf.instanciateBlock("text_wall", {0,0});
        Block c = bf.instanciateBlock("is", {1,0});
        Block b = bf.instanciateBlock("stop", {2,0});

        RuleChain rc{s,c,b};
        REQUIRE(rc.RuleToBlockName(Type::SUBJECT) == "wall");
        REQUIRE(rc.RuleToBlockName(Type::CONNECTOR) == "is");
        REQUIRE(rc.RuleToBlockName(Type::BEHAVIOR) == "stop");
    }

    SECTION("Testing instanciation S-C-S"){
        BlockFactory bf;
        Block s = bf.instanciateBlock("text_wall", {0,0});
        Block c = bf.instanciateBlock("is", {1,0});
        Block s2 = bf.instanciateBlock("text_baba", {2,0});

        RuleChain rc{s,c,s2};
        REQUIRE(rc.RuleToBlockName(Type::SUBJECT) == "wall");
        REQUIRE(rc.RuleToBlockName(Type::CONNECTOR) == "is");
        REQUIRE(rc.RuleToBlockName(Type::BEHAVIOR) == "baba");
    }

    SECTION("Testing instanciation S-C-S reversed"){
        BlockFactory bf;
        Block s = bf.instanciateBlock("text_wall", {0,0});
        Block c = bf.instanciateBlock("is", {1,0});
        Block s2 = bf.instanciateBlock("text_baba", {2,0});

        RuleChain rc{s2,c,s};
        REQUIRE(rc.RuleToBlockName(Type::SUBJECT) == "wall");
        REQUIRE(rc.RuleToBlockName(Type::CONNECTOR) == "is");
        REQUIRE(rc.RuleToBlockName(Type::BEHAVIOR) == "baba");
    }

    /*---------------------*
     * IsValid TESTS       *
     *---------------------*/

    SECTION("Testing isValid valid"){
        BlockFactory bf;
        Block s = bf.instanciateBlock("text_wall", {0,0});
        Block c = bf.instanciateBlock("is", {1,0});
        Block b = bf.instanciateBlock("stop", {2,0});

        RuleChain rc{s,c,b};
        REQUIRE(rc.isValid() == true);
    }

    SECTION("Testing isValid not valid"){
        BlockFactory bf;
        Block s = bf.instanciateBlock("stop", {0,0});
        Block c = bf.instanciateBlock("is", {1,0});
        Block b = bf.instanciateBlock("text_wall", {2,0});

        RuleChain rc{s,c,b};
        REQUIRE(rc.isValid() == false);
    }

    /*---------------------*
     * getBlockName TESTS  *
     *---------------------*/

    SECTION("Testing getBlockName OK"){
        BlockFactory bf;
        Block s = bf.instanciateBlock("text_wall", {0,0});
        Block c = bf.instanciateBlock("is", {1,0});
        Block b = bf.instanciateBlock("stop", {2,0});

        RuleChain rc{s,c,b};
        REQUIRE(rc.RuleToBlockName(Type::SUBJECT) == "wall");
        REQUIRE(rc.RuleToBlockName(Type::CONNECTOR) == "is");
        REQUIRE(rc.RuleToBlockName(Type::BEHAVIOR) == "stop");

        s = bf.instanciateBlock("text_wall", {0,0});
        c = bf.instanciateBlock("is", {1,0});
        Block s2 = bf.instanciateBlock("text_baba", {2,0});

        rc = {s2,c,s};
        REQUIRE(rc.RuleToBlockName(Type::SUBJECT) == "wall");
        REQUIRE(rc.RuleToBlockName(Type::CONNECTOR) == "is");
        REQUIRE(rc.RuleToBlockName(Type::BEHAVIOR) == "baba");
    }

    SECTION("Testing getBlockName KO"){
        BlockFactory bf;
        Block s = bf.instanciateBlock("text_wall", {0,0});
        Block c = bf.instanciateBlock("is", {1,0});
        Block b = bf.instanciateBlock("stop", {2,0});

        RuleChain rc{s,c,b};
        REQUIRE_THROWS(rc.RuleToBlockName(Type::BLOCK));
    }
}
