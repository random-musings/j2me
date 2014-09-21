/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marblegame;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author janiestoy
 */
public class GameSummary {
    
    
    public static void drawLevelSummary(Graphics g,
                                int points,
                                int marblesUsed,
                                int marblePointDeduction
                                )
    {
        
            int width = g.getClipX();
            int height = g.getClipHeight();
            Point startCorner = new Point(  (int) width/10,
                                            (int) height/10,
                                            0);
            g.setColor( (int)GameSettings.BORDERCOLOR.x,
                    (int)GameSettings.BORDERCOLOR.y,
                    (int)GameSettings.BORDERCOLOR.z);
            g.fillRect( (int)startCorner.x,
                        (int)startCorner.y ,
                        (int)(width- 2*startCorner.x),
                        (int)(height- 2*startCorner.y));
            
            g.setColor( (int)GameSettings.BACKGROUNDCOLOR.x,
                        (int)GameSettings.BACKGROUNDCOLOR.y,
                        (int)GameSettings.BACKGROUNDCOLOR.z);

            g.fillRect( (int)(startCorner.x + 2*startCorner.x),
                        (int)(startCorner.y + 2*startCorner.y),
                        (int)(width- 4*startCorner.x),
                        (int)(height- 4*startCorner.y));

            int drawingWidth =  2*(int)(startCorner.x);
            int drawingHeight =  2*(int)(startCorner.y);

            int startX = drawingWidth;
            int startY = drawingHeight;

            g.setColor( (int)GameSettings.FONTCOLOR.x,
                        (int)GameSettings.FONTCOLOR.y,
                        (int)GameSettings.FONTCOLOR.z);

            String strLevelSummary ="Summary";
            g.drawString(strLevelSummary ,
                        startX,
                        startY,
                        0);             
            int pointsEarned = points + Math.abs(marblePointDeduction);
            startY +=GameSettings.lineHeight;
            g.drawString("Points Earned:"+pointsEarned ,
                        startX,
                        startY,
                        0);  
            startY +=GameSettings.lineHeight;
            g.drawString("Marbles Used ("+marblesUsed+") : -"+marblePointDeduction,
                    startX,
                    startY,
                    0);
            startY +=GameSettings.lineHeight;
            g.drawString("Total Points : "+points,
                    startX,
                    startY,
                    0);
            

    }
    
    public static void GameSummary(Graphics g)
    {
        
        
    }
}
