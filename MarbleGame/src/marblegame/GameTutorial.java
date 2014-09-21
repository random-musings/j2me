/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marblegame;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * prints out the image that tells the user how the controls work
 */
public class GameTutorial {
    
    Image  instructions;
        public GameTutorial()
        {
            try
            {
            GameImages gi = new GameImages();
            instructions = gi.getImage(GameImages.INSTRUCTIONS);
            }catch(IOException err)
            {
                instructions = null;
            }
        }
        
    public  void drawInstructions(Graphics g)
    {
            if(instructions!=null)
            {
                g.drawImage(instructions,
                        2* GameSettings.BORDERHEIGHT,
                            GameSettings.LOGOHEIGHT, 0);
            }
    }

    
    
}
