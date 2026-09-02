/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gallery;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author Roman
 */

public class Gallery {
    static ImagePanel [] imagePanels;
    static int imageCounter = 0;
    static Tred [] treds = new Tred[5];
    static String [] threadNames = {"Thread_Martin", "Thread_Dan", "Thread_Tomas", "Thread_Jirka", "Thread_Roman"};
    static Image emptyImage;
    static Image[][] IMAGES = new Image[5][2];
    
    public static void main(String [] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            System.out.println("Error setting native LAF: " + e);
        }
        JFrame frame = new JFrame("NB Debugger Team");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        
        // RootPanel
        JPanel rootPanel = new JPanel();
        BorderLayout rootLayout = new BorderLayout(); 
        rootPanel.setLayout(rootLayout);
        
        // GalleryPanel
        JPanel galleryPanel = new JPanel();
        GridLayout galleryLayout = new GridLayout(0, 1);
        galleryPanel.setLayout(galleryLayout);
        
        // ImagePanels
        imagePanels = new ImagePanel[5];
        emptyImage = new ImageIcon(Gallery.class.getResource("/images/img_empty.jpg")).getImage();
        for(int i = 0; i < imagePanels.length; i++) {
            imagePanels[i] = new ImagePanel(emptyImage);
            IMAGES[i][0] = new ImageIcon(Gallery.class.getResource("/images/img_" + (i+1) + "_1.jpg")).getImage();
            IMAGES[i][1] = new ImageIcon(Gallery.class.getResource("/images/img_" + (i+1) + "_2.jpg")).getImage();
            galleryPanel.add(imagePanels[i]);
        }
        
        // ControlPanel
        JPanel controlPanel = new JPanel();
        final JButton moreButton = new JButton("More");
        final JButton lessButton = new JButton("Less");
        moreButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (imageCounter == 5) return;
                    lessButton.setEnabled(true);
                    treds[imageCounter] = new Tred(imagePanels[imageCounter], imageCounter);
                    treds[imageCounter].setName(threadNames[imageCounter]);
                    treds[imageCounter++].start();
                    if (imageCounter == 5) {
                        moreButton.setEnabled(false);
                    }
                }
            }
        );
        controlPanel.add(moreButton);
        lessButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (imageCounter == 0) return;
                    moreButton.setEnabled(true);
                    treds[--imageCounter].interrupt();
                    if (imageCounter == 0) {
                        lessButton.setEnabled(false);
                    }
                }
            }
        );
        lessButton.setEnabled(false);
        controlPanel.add(lessButton);
        
        rootPanel.add(galleryPanel, BorderLayout.PAGE_START);
        rootPanel.add(controlPanel, BorderLayout.PAGE_END);
        frame.getContentPane().add(rootPanel);
        frame.pack();
        frame.setVisible(true);
        
    }
}

class ImagePanel extends JPanel {

  private Image img;

  public ImagePanel() {
      img = null;
  }
  
  public ImagePanel(URL img) {
    this(new ImageIcon(img).getImage());
  }
 //new change
  public ImagePanel(Image img) {
    this.img = img;
    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
    setPreferredSize(size);
    setMinimumSize(size);
    setMaximumSize(size);
    setSize(size);
    setLayout(null);
  }
  
  public void updateImage(Image img) {
    this.img = img;
    repaint();
  }

  public void updateImage(String img) {
    updateImage(new ImageIcon(img).getImage());
  }
  
    @Override
  public void paintComponent(Graphics g) {
    g.drawImage(img, 0, 0, null);
  }

}


class Tred extends Thread {
    ImagePanel panel;
    Image [] images;
    Image currentImage;
    Image emptyImage;
    
    int a() {
        return 1;
    }
    
    public Tred(ImagePanel panel, int imgIndex) {
        this.panel = panel;
        int n = Gallery.IMAGES[imgIndex].length;
        images = new Image[n];
        for (int i = 0; i < n; i++) {
            images[i] = Gallery.IMAGES[imgIndex][i];
        }
        emptyImage = Gallery.emptyImage;
        
    }
    
    @Override
    public void run() {
        currentImage = images[0];
        try {
            while(!Thread.currentThread().isInterrupted()) {
                Runnable updatePanel = new Runnable() {
                    public void run() {
                        panel.updateImage(currentImage);
                    }
                };
                try {
                    SwingUtilities.invokeAndWait(updatePanel);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Tred.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(Tred.class.getName()).log(Level.SEVERE, null, ex);
                }
                Thread.sleep(1000);
                if(currentImage == images[0])
                    currentImage = images[1];
                else currentImage = images[0];
            }
         } catch (InterruptedException e) {}
        
        Runnable clearPanel = new Runnable() {
            public void run() {
                panel.updateImage(emptyImage);
            }
         };
         try {
            SwingUtilities.invokeAndWait(clearPanel);
         } catch (InterruptedException ex) {
            Logger.getLogger(Tred.class.getName()).log(Level.SEVERE, null, ex);
         } catch (InvocationTargetException ex) {
            Logger.getLogger(Tred.class.getName()).log(Level.SEVERE, null, ex);
         }
         
    }
}

