import java.util.ArrayList;

public class mySubject {
    private ArrayList<myObserver> observers = new ArrayList<myObserver>();

    public void addObserver(myObserver obs) {
        observers.add(obs);
    }

    public void removeObserver(myObserver obs) {
        observers.remove(obs);
    }

    protected void notifyObservers() {
        for (int i = 0; i < observers.size(); i++)
            observers.get(i).update();
    }
}