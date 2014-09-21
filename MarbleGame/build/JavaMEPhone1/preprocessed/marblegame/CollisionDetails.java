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
public class CollisionDetails {
        boolean collisionOccurs;
        Peg collidingNode;
	Point resultingVelocity;
	Point pointOfCollision;
        
        public CollisionDetails()
        {
            collisionOccurs = false;
            collidingNode = null;
            resultingVelocity  = new Point();
            pointOfCollision= new Point();
        }
}
