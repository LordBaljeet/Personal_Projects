#include "catch.hpp"
#include "Position.h"

TEST_CASE("Testing Position"){

    /*---------------------*
     * INSTANCIATION TESTS *
     *---------------------*/

    SECTION("Testing default instanciation"){
        Position pos;
        REQUIRE(pos.getX() == 0);
        REQUIRE(pos.getY() == 0);
    }
    SECTION("Testing instanciation using a pair"){
        std::pair<int,int> values = {3,1};
        Position pos{values};
        REQUIRE(pos.getX() == 3);
        REQUIRE(pos.getY() == 1);
    }
    SECTION("Testing instanciation using = operator"){
        Position pos1{2,3};
        Position pos2 = pos1;
        REQUIRE(pos2.getX() == 2);
        REQUIRE(pos2.getY() == 3);
    }

    /*-----------------------*
     * GETTERS/SETTERS TESTS *
     *-----------------------*/

    SECTION("Testing setters"){
        Position pos1;
        pos1.setX(3);
        pos1.setY(13);
        REQUIRE(pos1.getX() == 3);
        REQUIRE(pos1.getY() == 13);
    }

    /*-----------------------*
     * OPERATORS TESTS *
     *-----------------------*/

    SECTION("Testing operator + on Positions"){
        Position pos1{1,1};
        Position pos2{2,5};
        Position posPlus;
        posPlus = pos1 + pos2;
        REQUIRE(posPlus.getX() == 3);
        REQUIRE(posPlus.getY() == 6);

    }
    SECTION("Testing operator + on Directions"){
        Position pos{2,2};

        Position posNorth = pos + Direction::N;
        REQUIRE(posNorth.getX() == 2);
        REQUIRE(posNorth.getY() == 1);

        Position posSouth = pos + Direction::S;
        REQUIRE(posSouth.getX() == 2);
        REQUIRE(posSouth.getY() == 3);

        Position posEast = pos + Direction::E;
        REQUIRE(posEast.getX() == 3);
        REQUIRE(posEast.getY() == 2);

        Position posWest = pos + Direction::W;
        REQUIRE(posWest.getX() == 1);
        REQUIRE(posWest.getY() == 2);
    }
    SECTION("Testing operator - on Positions"){
        Position pos1{1,1};
        Position pos2{2,5};
        Position posPlus;
        posPlus = pos2 - pos1;
        REQUIRE(posPlus.getX() == 1);
        REQUIRE(posPlus.getY() == 4);

    }
    SECTION("Testing operator - on Directions"){
        Position pos{2,2};

        Position posNorth = pos - Direction::N;
        REQUIRE(posNorth.getX() == 2);
        REQUIRE(posNorth.getY() == 3);

        Position posSouth = pos - Direction::S;
        REQUIRE(posSouth.getX() == 2);
        REQUIRE(posSouth.getY() == 1);

        Position posEast = pos - Direction::E;
        REQUIRE(posEast.getX() == 1);
        REQUIRE(posEast.getY() == 2);

        Position posWest = pos - Direction::W;
        REQUIRE(posWest.getX() == 3);
        REQUIRE(posWest.getY() == 2);
    }
    SECTION("Testing operator += on Positions"){
        Position pos1{1,1};
        Position pos2{2,5};
        REQUIRE_NOTHROW(pos2 += pos1);
        REQUIRE(pos2.getX() == 3);
        REQUIRE(pos2.getY() == 6);

    }
    SECTION("Testing operator += on Directions"){
        Position pos{2,2};

        Position posNorth = pos;
        posNorth +=Direction::N;
        REQUIRE(posNorth.getX() == 2);
        REQUIRE(posNorth.getY() == 1);

        Position posSouth = pos;
        posSouth += Direction::S;
        REQUIRE(posSouth.getX() == 2);
        REQUIRE(posSouth.getY() == 3);

        Position posEast = pos;
        posEast +=Direction::E;
        REQUIRE(posEast.getX() == 3);
        REQUIRE(posEast.getY() == 2);

        Position posWest = pos;
        posWest += Direction::W;
        REQUIRE(posWest.getX() == 1);
        REQUIRE(posWest.getY() == 2);
    }
    SECTION("Testing operator -= on Positions"){
        Position pos1{1,1};
        Position pos2{2,5};
        REQUIRE_NOTHROW(pos2 -= pos1);
        REQUIRE(pos2.getX() == 1);
        REQUIRE(pos2.getY() == 4);

    }
    SECTION("Testing operator -= on Directions"){
        Position pos{2,2};

        Position posNorth = pos;
        posNorth -= Direction::N;
        REQUIRE(posNorth.getX() == 2);
        REQUIRE(posNorth.getY() == 3);

        Position posSouth = pos;
        posSouth -= Direction::S;
        REQUIRE(posSouth.getX() == 2);
        REQUIRE(posSouth.getY() == 1);

        Position posEast = pos;
        posEast -= Direction::E;
        REQUIRE(posEast.getX() == 1);
        REQUIRE(posEast.getY() == 2);

        Position posWest = pos;
        posWest -= Direction::W;
        REQUIRE(posWest.getX() == 3);
        REQUIRE(posWest.getY() == 2);
    }
}
