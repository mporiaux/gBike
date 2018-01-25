/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package googlebike;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Michel
 */
public class Cible extends JFrame{
    JLabel zone=new JLabel();
    public Cible(){
        this.setBounds(200,200,100,100);
        
        add(zone);
        this.pack();
        setVisible(true);
    }
}
