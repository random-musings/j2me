/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marblegame;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * Prints Level and Game summary
 */
public class GameSummary {
    
    
    public static int drawLevelSummary(
                                Image logo,
                                Graphics g,
                                String levelId,
                                int points,
                                int marblesUsed,
                                int marblePointDeduction
                                )
    {
        //paint screen with border color
            int width = g.getClipWidth();
            int height = g.getClipHeight();
            Point startCorner = new Point(  0,
                                            0,
                                            0);
            g.setColor( (int)GameSettings.BORDERCOLOR.x,
                    (int)GameSettings.BORDERCOLOR.y,
                    (int)GameSettings.BORDERCOLOR.z);
            g.fillRect( (int)startCorner.x,
                        (int)startCorner.y ,
                        (int)(width),
                        (int)(height));
            
            //create smaller inset rectable with background color
            g.setColor( (int)GameSettings.BACKGROUNDCOLOR.x,
                        (int)GameSettings.BACKGROUNDCOLOR.y,
                        (int)GameSettings.BACKGROUNDCOLOR.z);

            g.fillRect( (int)(startCorner.x + GameSettings.BORDERHEIGHT),
                        (int)(startCorner.y + GameSettings.BORDERHEIGHT),
                        (int)(width- 2*GameSettings.BORDERHEIGHT),
                        (int)(height- 2*GameSettings.BORDERHEIGHT));
                        
            
            //draw logo it it was passed in
            if(logo!=null)
            {
                    g.drawImage(logo,0,GameSettings.BORDERHEIGHT,0);
            }
            
            int drawingWidth =  4*(int)GameSettings.BORDERHEIGHT;
            int drawingHeight =  3*(int)GameSettings.BORDERHEIGHT;

            int startX = drawingWidth;
            int startY = drawingHeight + GameSettings.LOGOHEIGHT;

            //draw level summary
            g.setColor( (int)GameSettings.FONTCOLOR.x,
                        (int)GameSettings.FONTCOLOR.y,
                        (int)GameSettings.FONTCOLOR.z);

            String strLevelSummary ="";
            if(levelId.length()>=0)
                strLevelSummary += levelId;
            g.drawString(strLevelSummary,
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
            g.drawString("Points this level: "+points,
                    startX,
                    startY,
                    0);
           
          return startY;

    }
    
    //if end of game is reached ensure that we print out total points
    public static void drawEndOfGame(Image logo,
                                Graphics g,
                                String levelId,
                                int points,
                                int marblesUsed,
                                int marblePointDeduction,
                                int totalPoints
                                )
    {
         
        //draw the last level summary
         int height  = drawLevelSummary(logo,
                            g,
                              "Game Completed",
                              points,
                              marblesUsed,
                              marblePointDeduction
                              );
         //tell user they finished the game
         height +=GameSettings.lineHeight;
        g.drawString("Total Game Points:"+ totalPoints, 
                         4*(int)GameSettings.BORDERHEIGHT,
                        height , 
                        0);
                        
    }
}
