/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marblegame;
import java.util.Vector;
/**
 *
 * this is a utility class that allows me to work with the velocity
 * and location of the objects in a game easily (originally designed for 3D world)
 */
public class Point {
    
	
	 private static final float TOLERANCE=0.001f;
	 public float x;
	 public float y;
	 public float z;
	 public Point()
	 {
	     x=0;
	     y=0;
	     z=0;
	 }
	 
	 public Point(float newX, float newY, float newZ)
	 {
	     x = newX;
	     y = newY;
	     z = newZ;
	 }
	 public Point(Point newPoint)
	 {
		 if(newPoint!=null)
		 {
		     x = newPoint.x;
		     y = newPoint.y;
		     z = newPoint.z;
		 }
	 }
	 
	 public void copy (Point copyPoint)
	 {
		 if(copyPoint!=null)
		 {
		     x = copyPoint.x;
		     y = copyPoint.y;
		     z = copyPoint.z;
		 }
	 }
	 
	 public boolean equal(Point point)
	 {
		 if(point!=null)
		 {
			 if(Math.abs(x - point.x)<TOLERANCE &&
	        Math.abs(y - point.y)<TOLERANCE &&
	        Math.abs(z - point.z)<TOLERANCE)
	        return true;
		 }
	     return false;
	 }
	 
	 
	  public void normalize()
	  {
	      double mag = magnitude();
	      if(mag>0)
	      {
	         x = x/(float)mag;
	         y = y/(float)mag;
	         z = z/(float)mag;
	      }      
	  }
	  
	  
	  public double magnitude()
	  {
	     return Math.sqrt( x*x + y*y + z*z);
	  }
	  
	  public Point add(Point newPoint)
	  {
		  if(newPoint==null)
			  return this;
	      x = newPoint.x + x;
	      y = newPoint.y + y;
	      z = newPoint.z + z;
	      return this;
	  }

	  public Point subtract(Point newPoint)
	  {
		 if(newPoint==null)
			 return this;
	     x = x - newPoint.x;
	     y = y - newPoint.y;
	     z = z - newPoint.z;
	     return this;
	  }
	   
	  public Point multiplyScalar(float scalar)
	  {
	     x = scalar * x;
	     y = scalar * y;
	     z = scalar * z;
	     return this;
	  }

	  public String serialize()
	  {
		return x+Split.comma+y+Split.comma+z;  
	  }

	  public static Point parsePoint(String src)
	  {
		 Vector variables  = Split.parseStringList(src);
			if(variables.size()<3)
				return new Point();
			return new Point((float)Double.parseDouble((String)variables.elementAt(0)),
					(float)	Double.parseDouble((String)variables.elementAt(1)),
					(float)	Double.parseDouble((String)variables.elementAt(2)));
	  }
         
}
