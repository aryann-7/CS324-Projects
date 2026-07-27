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
public class Note {
    public static final Note
    MIDDLE_C = new Note("Middle C"),
    C_SHARP  = new Note("C Sharp"),
    B_FLAT   = new Note("B Flat");
    // Etc.
    
    private String noteName;
    
    private Note(String noteName) {
        this.noteName = noteName;
    }
  
    public String toString() { 
        return noteName; 
    } 
}
