include(../defaults.pri)

#you MUST include headers to subprojects, for instance like this
#"core" is not added here because it's already in defaults.pri

#INCLUDEPATH += $$PWD/../controllers

TEMPLATE = lib
TARGET = libcore
DESTDIR = ../lib


HEADERS += \
    Block.h \
    BlockFactory.h \
    Board.h \
    Direction.h \
    Enumeration.h \
    Game.h \
    GameRules.h \
    Observable.h \
    Observer.h \
    Position.h \
    RuleChain.h \
    Rules/Best.h \
    Rules/Kill.h \
    Rules/Push.h \
    Rules/Rule.h \
    Rules/RuleFactory.h \
    Rules/Sink.h \
    Rules/Stop.h \
    Rules/Win.h \
    Rules/You.h \
    Rules/exit.h \
    Rules/inactive.h \
    Rules/play.h \
    Rules/save.h \
    Rules/zero.h

SOURCES += \
    Block.cpp \
    BlockFactory.cpp \
    Board.cpp \
    Direction.cpp \
    Game.cpp \
    GameRules.cpp \
    Position.cpp \
    RuleChain.cpp \
    Rules/Best.cpp \
    Rules/Kill.cpp \
    Rules/Push.cpp \
    Rules/Rule.cpp \
    Rules/RuleFactory.cpp \
    Rules/Sink.cpp \
    Rules/Stop.cpp \
    Rules/Win.cpp \
    Rules/You.cpp \
    Rules/exit.cpp \
    Rules/inactive.cpp \
    Rules/play.cpp \
    Rules/save.cpp \
    Rules/zero.cpp

