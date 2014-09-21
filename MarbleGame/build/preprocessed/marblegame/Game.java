/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marblegame;

import java.io.IOException;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author janiestoy
 */
public class Game extends GameCanvas implements Runnable{
    
    GameImages gameImages;
    GameLevels gameLevels;
    GameLevel currentLevel;
    int action;
    
    private int screenHeight;
    private int screenWidth;
    
    public Game () throws IOException
    {
        super(false);
        action = GameSettings.LOADNEWGAME;
        screenHeight = this.getHeight();
        screenWidth  = this.getWidth();
        
        gameImages = new GameImages();
        gameLevels = new GameLevels();
        currentLevel = null;
    }
    
    public void run()
    {
        while(true)
        {
            if(action == GameSettings.LOADNEWGAME)
            {
                loadNewGame();
                action = GameSettings.GAMEINPLAY;
            }
            if(action == GameSettings.LOADSAVEDGAME)
            {
                action = GameSettings.GAMEINPLAY;
            }
            if(action ==GameSettings.SAVEGAME)
            {
                action = GameSettings.QUITGAME;
            }
            if(action == GameSettings.QUITGAME)
            {
                return;
            }
            if(action== GameSettings.GAMEINPLAY)
            {
                manageGamePlay();
                
            }
        }   
    }
    
    public void manageGamePlay()
    {
        //if currentLevel==null showERror & menu
        //if currentLevel ended show summary
        //if currentLevel ended and game ended
        if(currentLevel !=null)
        {
            if(currentLevel.levelEnded)
            {
               currentLevel = setLevelSettings((currentLevel.id+1),
                                        currentLevel.points,
                                        currentLevel.marblesUsed);
            }else
            {
                  currentLevel.advanceMarble();
            }
            currentLevel.draw(super.getGraphics());
            flushGraphics();
        }
    }
    
    public void loadNewGame()
    {
        currentLevel =setLevelSettings(0,0,0);
    }
    
    private GameLevel setLevelSettings(int index,int startingPoints, int startingMarbles)
    {
        GameLevel level =gameLevels.getGameLevel(gameImages, index);
        level.topWall = GameSettings.SCOREBOARDHEIGHT;
        level.leftWall = 0;
        level.rightWall = screenWidth;
        level.bottomWall = screenHeight;
        level.start(startingPoints,startingMarbles);
        return level;
    }
    
}
