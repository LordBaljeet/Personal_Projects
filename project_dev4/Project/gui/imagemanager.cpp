#include "imagemanager.h"

void ImageManager::load()
{
    if(loadCallCount == 0)
    {
        QDirIterator it(":/Images/", QDirIterator::Subdirectories);

        while(it.hasNext())
        {
            QString filePath = it.next();
            QPixmap image(filePath);
            QString identifer = filePath.section("/", -1).section(".",0,0);
            imageContainer[identifer.toStdString()] = image;
        }

        qDebug() << "Images Loaded Successfully";
        loadCallCount++;
    }
}

QPixmap* ImageManager::getImage(const std::string& identifier)
{
    if(imageContainer.count(identifier))
    {
        return &imageContainer.at(identifier);
    }
    return nullptr;
}
