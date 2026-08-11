public class number extends mySubject {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int in) {
        value = in;
        notifyObservers();
    }
}