public class BinNumber implements myObserver {
    private number n;

    private BinNumber() {
        ;
    }

    public BinNumber(number in) {
        this.n = in;
        n.addObserver(this);
    }

    public void update() {
        System.out.print(" " + Integer.toBinaryString(n.getValue()));
    }
}