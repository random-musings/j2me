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
public class Marble {
    public Sprite image;
    public Point velocity;
    
    public Marble()
    {
        image = null;
        velocity = new Point();
    }
    
    public Marble (Marble copy)
    {
        image = new Sprite(copy.image);
        velocity = new Point(copy.velocity);
    }
}
