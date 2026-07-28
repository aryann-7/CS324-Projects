package music;

public class Guitar implements Instrument {
    @Override
    public void play(Note n) {
        System.out.println("Guitar is playing note " + n);
    }
}
