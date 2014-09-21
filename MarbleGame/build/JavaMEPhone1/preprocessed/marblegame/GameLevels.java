/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marblegame;


/**
 *
 * @author janiestoy
 */
public class GameLevels {
    public final int pegsPerLevel = 4;
    public final int levelCount = 3;
    
        Point[][] allPegs =
        { {new Point(200,80,0), new Point(20,160,0), new Point(100,90,30),new Point(80,210,40)},
          {new Point(10,120,0), new Point(203,210,0), new Point(60,300,30),new Point(150,290,40)},
          {new Point(100,200,0), new Point(170,190,0), new Point(180,280,30),new Point(50,270,40)}
        };
    
    public GameLevel getGameLevel(GameImages gameImages,int index)
    {
        
        if(index<0 || index>=levelCount)
            return null;
        GameLevel level = new GameLevel(index);
        level.monster.imageId =  GameImages.SPIDER;
        level.monster.image = gameImages.createSprite(GameImages.SPIDER,
                                                    GameSettings.SPIDERSTARTPOSITION);
        level.marble.image = gameImages.createSprite(GameImages.MARBLE,
                                                    new Point(GameSettings.MARBLESTARTPOSITION.x,
                                                              GameSettings.MARBLESTARTPOSITION.y,0));
        level.marble.imageId =  GameImages.MARBLE;
        level.pegs = new Peg[pegsPerLevel];
        for (int i =0;i<pegsPerLevel;i++)
        {
            level.pegs[i] = createPeg(gameImages,
                                GameImages.PURPLEPEG,
                                GameImages.PURPLEPEGHIT, 
                                allPegs[index][i]);
        }
       
        return level;
    }
    
    private Peg  createPeg(GameImages gameImages,
                            int imageIndex,
                            int imageHitIndex,
                           Point position)
    {
         return new Peg(gameImages.createSprite(imageIndex,position), 
                        gameImages.createSprite(imageHitIndex,position), 
                        imageIndex,
                        imageHitIndex,
                        0, 
                        true);
    }
    
    
}
