/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marblegame;

import java.io.IOException;
import javax.microedition.lcdui.game.Sprite;
import java.util.Vector;
/**
 *
 * @author janiestoy
 */
public class Peg {
    public int imageId;
    public int imageHitId;
    public Sprite image;
    public Sprite imageHit;
    public long timeHit;
    public boolean active;
    
    //serialization Settings
    public static final String pegImageId="imageId";
    public static final String pegImageHitId="imageHitId";
    public static final String pegTimeHit="timeHit";
    public static final String pegLocation="pegLocation";
    public static final String pegStatus="pegStatus";

    public Peg()
    {
        image= null;
        timeHit = 0;
        active = true;
    }
    
    public Peg( Sprite newImage,
                Sprite newHitImage,
                int newImageId,
                int newImageHitId,
                long newTimeHit,
                boolean status)
    {
        image = newImage;
        imageHit = newHitImage;
        timeHit = newTimeHit;
        active = status;
        imageId = newImageId;
        imageHitId = newImageHitId;
    }
            
   public Point getLocation()
    {
        return new Point(image.getRefPixelX(),
                        image.getRefPixelY(),0);
    }
    
    public void setLocation(int x, int y)
    {
        image.setRefPixelPosition(x,y);
    }
    public void setLocation(Point newPosition)
    {
        image.setRefPixelPosition((int)newPosition.x,
                                   (int)newPosition.y);
        
    }    
    
    public String serialize()
    {
        String newLine = GameSettings.NEWLINE;
        String serializedData ="";
        serializedData +=pegImageId+GameSettings.SEPARATOR+imageId+newLine;
        serializedData +=pegImageHitId+GameSettings.SEPARATOR+imageHitId+newLine;
        serializedData +=pegTimeHit+GameSettings.SEPARATOR+timeHit+newLine;
        serializedData +=pegLocation+GameSettings.SEPARATOR+getLocation().serialize()+newLine;
        serializedData +=pegStatus+GameSettings.SEPARATOR+active+newLine;
        return serializedData;
    }
    
    
    public void deserialize(String src) throws IOException
    {
        //src = src.replace(erroneousCharacter, ' '); //remove tab that we added to make it read better;
        Vector values = Split.split(src,""+ GameSettings.NEWLINE);
        int i=0;
        Point tmpLocation = new Point();
        while(i<values.size())
        {
            Vector nameValuePair = Split.split((String)values.elementAt(i),GameSettings.SEPARATOR); 
            if(nameValuePair.size()>=2)
            {
                if ( ((String)nameValuePair.elementAt(0)).equals( pegImageId))
                    imageId=Integer.parseInt((String)nameValuePair.elementAt(1));                    
                if ( ((String)nameValuePair.elementAt(0)).equals( pegImageHitId))
                    imageHitId=Integer.parseInt((String)nameValuePair.elementAt(1));                    
                if ( ((String)nameValuePair.elementAt(0)).equals( pegTimeHit))
                    timeHit=(long)Double.parseDouble((String)nameValuePair.elementAt(1));                    
                if( ((String)nameValuePair.elementAt(0)).equals( pegStatus))
                    {active = Split.parseBoolean((String)nameValuePair.elementAt(1));}
                if ( ((String)nameValuePair.elementAt(0)).equals( pegLocation )) 
                      tmpLocation= Point.parsePoint((String)nameValuePair.elementAt(1));  
            }
            i++;
        }
        GameImages gameImage = new GameImages();
        if(imageId>=0)
            image =gameImage.createSprite(imageId, tmpLocation);
        if(imageHitId>=0)
            imageHit =gameImage.createSprite(imageHitId, tmpLocation);
                
    }
    
}
