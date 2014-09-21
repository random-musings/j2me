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
 * All game Images are stored here
 * Game images are loaded in the constructor and used to create levels
 */
public class GameImages {
    private Image[]  images;
 
    private int maxImages = 10;
    
    //a const index to game Images...makes it easy to change 
    //later 
    public static final int MARBLE=0;
    public static int WALL = 1;
    public static int SPIDER = 2;
    public static int BLUEPEG = 3;
    public static int PURPLEPEG = 4;
    public static int BLANK = 5;
    public static int BLUEPEGHIT = 6;
    public static int PURPLEPEGHIT = 7;
    public static int INSTRUCTIONS = 8;
    public static int MARBLELOGO = 9;
    
    //all images used during game play are 15x15 
    public static int IMGHEIGHT =15;
    public static int IMGWIDTH=15;

    public GameImages() throws IOException
    {
        images = new Image[maxImages];
        initializeImages();
    }

    //create all images
    private void initializeImages()throws IOException
    {
        images[MARBLE] = Image.createImage("/marble.png");
        images[WALL] = Image.createImage("/background.PNG");
        images[SPIDER] = Image.createImage("/propellerseq.png");
        images[BLUEPEG] = Image.createImage("/blue_button.png");
        images[PURPLEPEG] = Image.createImage("/purple_button.png");
        images[BLUEPEGHIT] = Image.createImage("/blue_button_hit.png");
        images[PURPLEPEGHIT] = Image.createImage("/purple_button_hit.png");
        images[BLANK] = Image.createImage("/blank.png");
        images[INSTRUCTIONS] = Image.createImage("/instructions.png");
        images[MARBLELOGO] = Image.createImage("/marblelogo.png");
    }
    
    //get an image at a specific index
    public Image getImage(int index)
    {
        if(index<0||index>=maxImages)
            return null;
        return images[index];
    }
    
    //if any of our Sprites have a framesequence we define it here
    public int[] getFrameSequence(int index)
    {
        if(index==SPIDER)
        {
           int[] spiderSequence = {0,1,2,3};
           return spiderSequence;
        }
        int[] sequence={0};
        return sequence;
    }
    
    
    //this function creates a basic sprite from a file name
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

     //creates a sprite from an existing image
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
     
     //creates a sprite and ensures that 
     // *the frames sequence is set
     // *the position is set
     // *the reference pixel is set
     //this function is used to create all sprites in the game
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
