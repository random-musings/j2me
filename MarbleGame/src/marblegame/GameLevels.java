/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marblegame;


/**
 * All levels from the game are defined here
 * 
 */
public class GameLevels {
    public final int pegsPerLevel = 12;
    public final int levelCount = 5;
    
        Point[][] allPegs =
        { 
          //Level1
          { new Point(10,80,0), new Point(40,100,0), new Point(60,120,0),new Point(80,140,0),
            new Point(100,160,0), new Point(120,180,0), new Point(120,150,0),new Point(140,160,0),
            new Point(170,140,0), new Point(190,120,0), new Point(210,100,0),new Point(220,80,0)
            },
          //level2
          {new Point(40,120,0), new Point(90,120,0), new Point(140,120,30),new Point(180,120,40),
            new Point(220,180,0), new Point(65,180,0), new Point(115,180,30),new Point(165,180,40),
            new Point(50,240,0), new Point(115,240,0), new Point(165,240,30),new Point(210,240,40)
          },
          
          //Level3
          {new Point(50,100,0), new Point(125,100,0), new Point(200,100,0),new Point(200,150,0),
            new Point(200,200,0),new Point(125,200,0), new Point(50,200,0),new Point(50,150,0),
            new Point(75,125,0), new Point(175,125,0), new Point(175,175,0),new Point(75,175,0)          
          },

          //Level4
          {new Point(80,80,0), new Point(50,160,0), new Point(20,240,0),new Point(120,160,0),
            new Point(140,240,0), new Point(90,160,0), new Point(80,250,0), new Point(150,160,0),
            new Point(180,160,0), new Point(210,160,0), new Point(180,200,0), new Point(180,120,0)
          },
                
          //Level5
          {new Point(20,150,0), new Point(40,150,0), new Point(60,150,30),new Point(80,150,40),
            new Point(100,150,0), new Point(120,150,0), new Point(140,150,30),new Point(160,150,40),
            new Point(180,150,0), new Point(200,150,0), new Point(220,150,30),new Point(110,110,40)          
          },

        };
    
        
        //Creates a game level
        //instantiates all sprites
        //creates pegs at the correct position
        //creates marble and monster
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
                                GameImages.BLUEPEG,
                                GameImages.BLUEPEGHIT, 
                                allPegs[index][i]);
        }
       
        return level;
    }
    
    //creates an individual peg with the images required to draw it
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
    
    //checks to see if this is the last level
    public boolean isLastLevel(int currLevel)
    {
        return  (currLevel>=levelCount);
        
    }
}
