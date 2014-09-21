/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marblegame;


/**
 *This class holds the details required to adjust the marbles position
 * after it collides with an object
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
