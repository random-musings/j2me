/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marblegame;

/**
 *
 * @author janiestoy
 */
public class Collision {
    
    public static Point getResultingVelocity(Point objectPoint,
                                    Point objectVelocity,
                                    float objectRadius,
                                    Point axisPoint)
	{

		Point tmpVelocity = new Point(objectVelocity);
		//tmpVelocity.normalize();
		//tmpVelocity.multiplyScalar(objectRadius*2.0f);
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


		//apply COllision Friction
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
}
