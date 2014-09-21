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
public class Monster {
    public Sprite image;
    public Point velocity;
    public boolean active;
    public long timeHit;
    public Monster()
    {
        image = null;
        velocity = new Point();
        active = true;
        timeHit = 0;
    }
}
