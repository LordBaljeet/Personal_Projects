#define CATCH_CONFIG_RUNNER
#include <iostream>
#include "catch.hpp"
int main( int argc , char*const argv [] ){
    Catch ::Session ( ) .run(argc,argv);
}
