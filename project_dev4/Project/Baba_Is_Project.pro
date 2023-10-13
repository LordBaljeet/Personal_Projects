TEMPLATE = subdirs
QMAKE_CXXFLAGS += "-Werror"

SUBDIRS += \
    gui \
    metier \
    console \
    tests

HEADERS += Config.h

OTHER_FILES += \
    defaults.pri \
