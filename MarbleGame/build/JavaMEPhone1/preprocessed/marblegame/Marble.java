/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marblegame;

import javax.microedition.lcdui.game.Sprite;
import java.util.Vector;
import java.io.IOException;

/**
 *
 * @author janiestoy
 */
public class Marble {
    public int imageId;
    public Sprite image;
    public Point velocity;
    
    //Serialization Settings
    public static final String marbleImageId="imageId";
    public static final String marbleLocation="pegLocation";
    public static final String marbleVelocity="pegStatus";

    
    public Marble()
    {
        image = null;
        velocity = new Point();
    }
    
    public Marble (Marble copy)
    {
        image = new Sprite(copy.image);
        velocity = new Point(copy.velocity);
        imageId = copy.imageId;
    }
    public Point getLocation()
    {
        return new Point(image.getRefPixelX(),
                        image.getRefPixelY(),0);
    }
    public void setLocation(Point newLocation)
    {
        image.setRefPixelPosition(  (int)newLocation.x,
                                    (int)newLocation.y);
    }
    public void setLocation(int x, int y)
    {
        image.setRefPixelPosition(x,y);
    }
    
    
      public String serialize()
    {
        String newLine = GameSettings.NEWLINE;
        String serializedData ="";
        serializedData +=marbleImageId+GameSettings.SEPARATOR+imageId+newLine;
        serializedData +=marbleLocation+GameSettings.SEPARATOR+getLocation().serialize()+newLine;
        serializedData +=marbleVelocity+GameSettings.SEPARATOR+velocity.serialize()+newLine;
        return serializedData;
    }
    
    
    public void deserialize(String src) throws IOException
    {
        Vector values = Split.split(src,""+ GameSettings.NEWLINE);
        int i=0;
        Point tmpLocation = new Point();
        while(i<values.size())
        {
                Vector nameValuePair = Split.split((String)values.elementAt(i),GameSettings.SEPARATOR); 
                if(nameValuePair.size()>=2)
                {
                    if ( ((String)nameValuePair.elementAt(0)).equals( marbleImageId))
                        imageId=Integer.parseInt((String)nameValuePair.elementAt(1));                    
                    if ( ((String)nameValuePair.elementAt(0)).equals( marbleLocation )) 
                          tmpLocation= Point.parsePoint((String)nameValuePair.elementAt(1));  
                    if ( ((String)nameValuePair.elementAt(0)).equals( marbleVelocity )) 
                          velocity= Point.parsePoint((String)nameValuePair.elementAt(1));  
                }
                i++;
        }
        GameImages gameImage = new GameImages();
        if(imageId>=0)
            image =gameImage.createSprite(imageId, tmpLocation);               
    }
    
}
