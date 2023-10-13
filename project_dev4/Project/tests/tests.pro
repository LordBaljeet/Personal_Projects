include(../defaults.pri)

#you MUST include headers to subprojects, for instance like this
#"core" is not added here because it's already in defaults.pri

#INCLUDEPATH += $$PWD/../controllers

TEMPLATE = app
CONFIG += console

LIBS += -L../lib -llibcore \

SOURCES += main.cpp\
        TestBlock.cpp \
        TestBlockFactory.cpp \
        TestBoard.cpp \
        TestGame.cpp \
        TestGameRules.cpp \
        TestPosition.cpp \
        TestRuleChain.cpp \
        TestRuleFactory.cpp \
        catch.hpp
