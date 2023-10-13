#include "Controller.h"
#ifdef _WIN32
#include <windows.h>
#else
#include <sys/ioctl.h>
#include <unistd.h>
#endif

using namespace std;

void resizeTerminalWindow();

int main()
{
    resizeTerminalWindow();
    try {
        Controller ctrl;
        ctrl.play();
    } catch (std::exception& e) {
        cout << e.what() << endl;
    }

    return(0);
}

void resizeTerminalWindow(){
    #ifdef _WIN32
    HANDLE console = GetStdHandle(STD_OUTPUT_HANDLE);
    COORD size = { 114, 21 };
    SMALL_RECT window = { 0, 0, static_cast<SHORT>(size.X - 1), static_cast<SHORT>(size.Y - 1) };
    SetConsoleScreenBufferSize(console, size);
    SetConsoleWindowInfo(console, TRUE, &window);
    #else
    struct winsize size;
    size.ws_col = 80;
    size.ws_row = 40;
    ioctl(STDOUT_FILENO, TIOCSWINSZ, &size);
    #endif
}
