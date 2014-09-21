/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marblegame;

/**
 *This class handles two things
 * 1. calculates the resulting velocity of the marble after hitting the peg or the enemy
 * 2. calculates the path of the marble before velocity is applied to see if 
 *      it will collide with a peg this ensures that the marble doesn't overrun the peg
 *      if its velocity is really high
 */
public class Collision {
    
    //determine the resulting velocity of the marble 
    //after it collides with an object
    public static Point getResultingVelocity(Point objectPoint,
                                    Point objectVelocity,
                                    float objectRadius,
                                    Point axisPoint)
	{

		Point tmpVelocity = new Point(objectVelocity);
		
		//get axis of marble and node
		Point b1Axis = new Point(axisPoint);
		b1Axis.subtract(objectPoint);
		
		//get axis between node and marble.
		Point b2Axis = new Point(objectPoint);
		b2Axis.subtract(axisPoint);
		
		//find the perpendicular 
		Point b1Perp = new Point(tmpVelocity);
		b1Perp.subtract(b1Axis);
		
		//find the resulting direction after hitting the object
		Point resultVelocityBall  = new Point(b1Perp);
		resultVelocityBall.add(b2Axis);

		//apply COllision Friction - cause it bounces like crazy if we don't
		resultVelocityBall.multiplyScalar(GameSettings.COLLISIONFRICTION);

                //ENSURE VELOCITY DOESN't exceed max
		if( Math.abs(resultVelocityBall.x)> GameSettings.MAXVELOCITY.x
				|| Math.abs(resultVelocityBall.y)> GameSettings.MAXVELOCITY.y)
		{
					resultVelocityBall.normalize();
					resultVelocityBall.multiplyScalar( GameSettings.MAXVELOCITY.y);
		}
		return resultVelocityBall;
	}   
    
    //verify that the marbles path traveled 
    //does not cross through the peg
    public static boolean marblePathIntersectsWithPeg(Marble marble,Peg peg, Point collisionPoint)
    {
        //determine if path of marble intersects with peg
        //find the unit vector of the velocity
        int velocityMagnitude = (int)(Math.floor(marble.velocity.magnitude())+1);
        Point velocity = new Point (marble.velocity);
        velocity.normalize();
        
        Marble testMarble = new Marble(marble);
        //start at the current position
        Point currentPosition = testMarble.getLocation();
        for(int i =0;i<velocityMagnitude;i++)
        {
            //advance the marble one unit at a time until we reach 
            //the distance it will travel when we apply the current velocity
            currentPosition.add(velocity);
            testMarble.setLocation(currentPosition);
            
            //test to see if new position collides with peg
            if(testMarble.image.collidesWith(peg.image, true))
            {
                //if yes then provide collision point 
                //so we can draw the marble at the collision point
                //and then apply it;s new velocity from the collision point
                collisionPoint = new Point(currentPosition);
                return true;
            }
        }
        return false;
    }

    
}
