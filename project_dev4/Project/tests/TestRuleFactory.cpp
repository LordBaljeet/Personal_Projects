#include "Rules/RuleFactory.h"
#include "catch.hpp"

TEST_CASE("Testing RuleFactory"){

    /*---------------------*
     * INSTACCIATION TESTS *
     *---------------------*/

    SECTION("Testing Rule instanciation OK"){
        Board board;
        GameRules gameRules;
        RuleFactory rf(board, gameRules);
        REQUIRE_NOTHROW(rf.getRulePointer("stop"));
    }

    SECTION("Testing Rule instanciation KO"){
        Board board;
        GameRules gameRules;
        RuleFactory rf(board, gameRules);
        REQUIRE_THROWS(rf.getRulePointer("slide"));
    }
}
