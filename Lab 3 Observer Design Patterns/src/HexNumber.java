public class HexNumber implements myObserver {
    private number n;

    public HexNumber(number in) {
        this.n = in;
        n.addObserver(this);
    }

    public void update() {
        System.out.print(" " + Integer.toHexString(n.getValue()));
    }
}