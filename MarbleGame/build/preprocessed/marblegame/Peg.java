/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marblegame;

import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author janiestoy
 */
public class Peg {
    public Sprite image;
    public Sprite imageHit;
    public long timeHit;
    public boolean active;
    
    public Peg()
    {
        image= null;
        timeHit = 0;
        active = true;
    }
    
    public Peg( Sprite newImage,
                Sprite newHitImage,
                long newTimeHit,
                boolean status)
    {
        image = newImage;
        imageHit = newHitImage;
        timeHit = newTimeHit;
        active = status;
    }
            
    
}
