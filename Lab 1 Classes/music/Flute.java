/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music;

/**
 *
 * @author sharma_au
 */
public class Flute extends Instrument{
    @Override
    public void play(Note n) {
        System.out.println("Flute is playing " + n);
    }
}

