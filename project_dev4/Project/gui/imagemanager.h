#ifndef IMAGEMANAGER_H
#define IMAGEMANAGER_H
#include <map>
#include <QString>
#include <QPixmap>
#include <QDirIterator>

/*!
 * \brief The ImageManager class manages access to images on disk
 */
class ImageManager
{
    /*!
     * \brief imageContainer contains all loaded images, identified by their
     * name without their extension.
     */
    inline static std::map<std::string, QPixmap> imageContainer;
    /*!
     * \brief loadCallCount keeps track of the number of calls to the load method.
     * Used to make the call of this method unique as in a one time event.
     */
    inline static int loadCallCount = 0;

public:
    /*!
     * \brief load loades images present in the :/Images/ directory and adds them
     * to the ImageContainer.
     *
     * This method can only be called once.
     */
    static void load();
    /*!
     * \brief getImage gets the desired image, identified by its name, if it exits.
     * \param identifier the image identifier
     * \returns a pointer to the desired image if it exists, null pointer otherwise.
     */
    static QPixmap* getImage(const std::string& identifier);
};

#endif // IMAGEMANAGER_H
