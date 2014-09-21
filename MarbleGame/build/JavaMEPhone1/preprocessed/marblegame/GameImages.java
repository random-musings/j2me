/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marblegame;

import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
/**
 *
 * @author janiestoy
 */
public class GameImages {
    private Image[]  images;
 
    private int maxImages = 8;
    public static final int MARBLE=0;
    public static int WALL = 1;
    public static int SPIDER = 2;
    public static int BLUEPEG = 3;
    public static int PURPLEPEG = 4;
    public static int BLANK = 5;
    public static int BLUEPEGHIT = 6;
    public static int PURPLEPEGHIT = 7;
    
    public static int IMGHEIGHT =15;
    public static int IMGWIDTH=15;

    public GameImages() throws IOException
    {
        images = new Image[8];
        initializeImages();
    }

    private void initializeImages()throws IOException
    {
        images[MARBLE] = Image.createImage("/marble.png");
        images[WALL] = Image.createImage("/background.PNG");
        images[SPIDER] = Image.createImage("/spider_Walk_sequence.png");
        images[BLUEPEG] = Image.createImage("/blue_button.png");
        images[PURPLEPEG] = Image.createImage("/purple_button.png");
        images[BLUEPEGHIT] = Image.createImage("/blue_button_hit.png");
        images[PURPLEPEGHIT] = Image.createImage("/purple_button_hit.png");
        images[BLANK] = Image.createImage("/blank.png");
    }
    
    public Image getImage(int index)
    {
        if(index<0||index>=maxImages)
            return null;
        return images[index];
    }
    
    public int[] getFrameSequence(int index)
    {
        if(index==SPIDER)
        {
           int[] spiderSequence = {0,1,2,3,2,1,0,4,5,6,5,4};
           return spiderSequence;
        }
        int[] sequence={0};
        return sequence;
    }
    
     public Sprite createSprite(String imageName,int height, int width)
    {
        try
        {
            Image tmpImage  = Image.createImage(imageName);
           return createSprite( tmpImage,height, width);
        }catch(Exception err)
        {
            err.toString();
        }
        return null;
    }

     public Sprite createSprite(Image tmpImage, int width,int height)
    {
        try
        {
            return new Sprite(tmpImage,width, height);
        }catch(Exception err)
        {
            err.toString();
        }
        return null;
    }     
     public Sprite createSprite(int imageIndex,
                                Point position)
    {
        Image tmpImage = getImage(imageIndex);        
        int[] frameSequence  =getFrameSequence(imageIndex);
        Sprite tmpSprite =createSprite(
                                   tmpImage,
                                   IMGWIDTH,
                                   IMGHEIGHT);// tmpImage.getWidth()/frameSequence.length,
                                    //tmpImage.getHeight());
        tmpSprite.setPosition((int)position.x,
                              (int)position.y);
        tmpSprite.setFrameSequence(frameSequence);
        tmpSprite.setFrame(0);
        tmpSprite.defineReferencePixel( (int)Math.floor((double)(IMGWIDTH/2.0f)),
                                        (int)Math.floor((double)(IMGHEIGHT/2.0f)));
        return tmpSprite;
    }
}
