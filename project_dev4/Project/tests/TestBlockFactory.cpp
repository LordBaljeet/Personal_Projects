#include "BlockFactory.h"
#include "catch.hpp"

TEST_CASE("Testing BlockFactory"){

    /*---------------------*
     * INSTACCIATION TESTS *
     *---------------------*/

    SECTION("Testing Block instanciation OK"){
        BlockFactory bf;
        REQUIRE_NOTHROW(bf.instanciateBlock("rock", {2,1}));
    }

    SECTION("Testing Block instanciation KO"){
        BlockFactory bf;
        REQUIRE_THROWS(bf.instanciateBlock("mouse", {2,1}));
    }
}
