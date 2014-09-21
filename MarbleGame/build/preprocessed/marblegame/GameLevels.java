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
        { {new Point(10,30,0), new Point(20,40,0), new Point(30,50,30),new Point(40,60,40)},
          {new Point(80,100,0), new Point(70,90,0), new Point(60,80,30),new Point(50,70,40)},
          {new Point(100,450,0), new Point(170,390,0), new Point(180,280,30),new Point(250,170,40)}
        };
    
    public GameLevel getGameLevel(GameImages gameImages,int index)
    {
        
        if(index<0 || index>=levelCount)
            return null;
        GameLevel level = new GameLevel(index);

        level.monster.image = gameImages.createSprite(GameImages.SPIDER,
                                                    GameSettings.SPIDERSTARTPOSITION);
        level.marble.image = gameImages.createSprite(GameImages.MARBLE,
                                                    new Point(GameSettings.MARBLESTARTPOSITION.x,
                                                              GameSettings.MARBLESTARTPOSITION.y,0));
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
                        0, 
                        true);
    }
    
    
}
