package g58744.boulder_dash.Model;

public interface Observable {

    void addObserver(Observer ob);
    void removeObserver(Observer ob);
    void notifyObservers();
}
