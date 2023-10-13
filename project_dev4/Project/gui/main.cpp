#include "mainwindow.h"
#include <QApplication>
#include <QFile>
#include "ImageManager.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    ImageManager::load();
    MainWindow w;
    w.ensurePolished();

    //Loading stylesheet
    QFile file(":/Styles/styles.qss");
    file.open(QFile::ReadOnly | QFile::Text);
    QString styleSheet = QLatin1String(file.readAll());
    a.setStyleSheet(styleSheet);

    w.showMaximized();
    return a.exec();
}
