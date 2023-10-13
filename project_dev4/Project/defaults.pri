INCLUDEPATH += $$PWD/metier

SRC_DIR = $$PWD

CONFIG -= app_bundle
CONFIG -= qt
#CONFIG += qt
CONFIG += c++20
#QT += core
QMAKE_CXXFLAGS += \
    -Wpedantic \
    -Werror \
