/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marblegame;

import javax.microedition.lcdui.game.Sprite;
import java.util.Vector;
import java.io.IOException;

/**
 *  this is our monster
 * 
 */
public class Monster {
    public int imageId;     //the sprite ID  used to draw our monster
    public int imageHitId;  //the zprite ID of monster when hit by marble
    public Sprite image; //the monsters walking sequence
    public Sprite imageHit; //what the monster looks like when its dead (active=false)
    public Point velocity; //how fast the monster travels towards the peg
    public boolean active; //is monsters alive (active=true)
    public long timeHit; //the time the monster was hit by the marble
    public long foundPeg; //time the monster started eating the peg
    public int pegEatingIndex; //which peg is being eaten

    //Serialization Settings
    public static final String monsterImageId="imageId";
    public static final String monsterLocation="monsterLocation";
    public static final String monsterVelocity="monsterVelocity";
    public static final String monsterPegEatingIndex="monsterPegEatingIndex";
    public static final String monsterActive="monsterActive";
    public static final String monsterTimeHit="monsterTimeHit";
    public static final String monsterFoundPeg="monsterFoundPeg";
    
    public Monster()
    {
        imageId=-1;
        imageHitId=-1;
        pegEatingIndex=-1;
        imageHit = null;
        image = null;
        velocity = new Point();
        active = true;
        timeHit = 0;
        foundPeg =0;
    }
    
    //convert the monsters location to a point so it is easier to work with
    public Point getLocation()
    {
        return new Point(image.getRefPixelX(),
                        image.getRefPixelY(),0);
    }
    
    //set the monsters location
    public void setLocation(int x, int y)
    {
        image.setRefPixelPosition(x,y);
    }
    
    //set the monsters location
    public void setLocation(Point newPosition)
    {
        image.setRefPixelPosition((int)newPosition.x,
                                   (int)newPosition.y);
        
    }
    
    
    //create  a string of data from the monster
  public String serialize()
    {
        String newLine = GameSettings.NEWLINE;
        String serializedData ="";
        serializedData +=monsterImageId+GameSettings.SEPARATOR+imageId+newLine;
        serializedData +=monsterLocation+GameSettings.SEPARATOR+getLocation().serialize()+newLine;
        serializedData +=monsterVelocity+GameSettings.SEPARATOR+velocity.serialize()+newLine;
        serializedData +=monsterPegEatingIndex+GameSettings.SEPARATOR+pegEatingIndex+newLine;
        serializedData +=monsterActive+GameSettings.SEPARATOR+active+newLine;
        serializedData +=monsterTimeHit+GameSettings.SEPARATOR+timeHit+newLine;
        serializedData +=monsterFoundPeg+GameSettings.SEPARATOR+foundPeg+newLine;
        return serializedData;
    }
    
    //create a monster from a string of data
    public void deserialize(String src) throws IOException
    {
        Vector values = Split.split(src, GameSettings.NEWLINE);
        int i=0;
        Point tmpLocation = new Point();
        while(i<values.size())
        {
                Vector nameValuePair = Split.split((String)values.elementAt(i),GameSettings.SEPARATOR); 
                if(nameValuePair.size()>=2)
                {
                    if ( ((String)nameValuePair.elementAt(0)).equals( monsterImageId))
                        imageId=Integer.parseInt((String)nameValuePair.elementAt(1));                    
                    if ( ((String)nameValuePair.elementAt(0)).equals( monsterLocation )) 
                          tmpLocation= Point.parsePoint((String)nameValuePair.elementAt(1));  
                    if ( ((String)nameValuePair.elementAt(0)).equals( monsterVelocity )) 
                          velocity= Point.parsePoint((String)nameValuePair.elementAt(1));  
                    if ( ((String)nameValuePair.elementAt(0)).equals( monsterPegEatingIndex))
                        pegEatingIndex=Integer.parseInt((String)nameValuePair.elementAt(1));                    
                    if ( ((String)nameValuePair.elementAt(0)).equals( monsterTimeHit))
                        timeHit=(long)Double.parseDouble((String)nameValuePair.elementAt(1));                    
                    if ( ((String)nameValuePair.elementAt(0)).equals( monsterFoundPeg))
                        foundPeg=(long)Double.parseDouble((String)nameValuePair.elementAt(1));                    
                    if( ((String)nameValuePair.elementAt(0)).equals( monsterActive))
                        {active = Split.parseBoolean((String)nameValuePair.elementAt(1));}
                }
                i++;
        }
        //re-create the images so the monster can be drawn
        GameImages gameImage = new GameImages();
        if(imageId>=0)
            image =gameImage.createSprite(imageId, tmpLocation);               
        if(imageHitId>=0)
            imageHit =gameImage.createSprite(imageHitId, tmpLocation);               
    }    
}
