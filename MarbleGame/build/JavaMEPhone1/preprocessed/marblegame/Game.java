/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marblegame;

import java.io.IOException;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.rms.RecordStore;


/**
 *
 * @author janiestoy
 */
public class Game extends GameCanvas implements Runnable{
    
    GameImages gameImages;
    GameLevels gameLevels;
    GameLevel currentLevel;
    Midlet parentMidlet;
    Menu gameMenu;
    int action;
    int lastKeyPressed;
    long keyPressTime;
    private int screenHeight;
    private int screenWidth;
    private String message;
    
    public Game () throws IOException
    {
        super(false);
        action = GameSettings.LOADNEWGAME;
        screenHeight = this.getHeight();
        screenWidth  = this.getWidth();
        gameMenu  = new Menu();
        gameMenu.width = this.getWidth();
        gameMenu.height  = this.getHeight();
        currentLevel = null;
        lastKeyPressed=0;
        keyPressTime=0;
    }
    
    public void run()
    {
        while(true)
        {
            handleKeyEvents();
            if(action == GameSettings.LOADNEWGAME)
            {
                loadNewGame();
                action = GameSettings.GAMEINPLAY;
            }
            if(action == GameSettings.LOADSAVEDGAME)
            {
                restoreGame();
                action = GameSettings.GAMEINPLAY;
            }
            if(action ==GameSettings.SAVEGAME)
            {
                saveGame();
                action = GameSettings.SHOWMENU;
            }
            if(action ==GameSettings.SHOWMENU)
            {
                showMenu();
            }
            if(action == GameSettings.QUITGAME)
            {
                parentMidlet.notifyDestroyed();
            }
            if(action== GameSettings.GAMEINPLAY)
            {
                manageGamePlay();
            }
            flushGraphics();
           try
            {
                Thread.sleep(GameSettings.TICKSPERFRAME);
            }catch(InterruptedException ex)
            {}
           
        }   
    }
    

    private void handleKeyEvents()
    {
    int keyState = getKeyStates();

            if((keyState & UP_PRESSED)!=0)
            {
                if(action == GameSettings.SHOWMENU && System.currentTimeMillis()>keyPressTime)
                {
                    gameMenu.selectPrevious();
                    keyPressTime = System.currentTimeMillis()+GameSettings.maxKeyPressTime;
                }
                lastKeyPressed =UP_PRESSED;
            }else if ( (keyState & RIGHT_PRESSED)>0)
            {
                if(action == GameSettings.GAMEINPLAY && currentLevel!=null)
                {
                    currentLevel.shiftMarble(GameSettings.SHIFTUNIT);
                }
                lastKeyPressed =RIGHT_PRESSED;
            }else if((keyState & LEFT_PRESSED)>0)
            {
                if(action == GameSettings.GAMEINPLAY && currentLevel!=null)
                {
                    currentLevel.shiftMarble(-GameSettings.SHIFTUNIT);
                }
                 lastKeyPressed =LEFT_PRESSED;
            }else if( (keyState & DOWN_PRESSED)>0)
            {
                if(action == GameSettings.SHOWMENU && System.currentTimeMillis()>keyPressTime)
                {
                    keyPressTime = System.currentTimeMillis()+GameSettings.maxKeyPressTime;
                    gameMenu.selectNext();
                }
                if(action == GameSettings.GAMEINPLAY && currentLevel!=null)
                {
                    currentLevel.releaseMarble();
                }    
                lastKeyPressed =DOWN_PRESSED;
            }else if( (keyState & FIRE_PRESSED)>0)
            {
                if(lastKeyPressed!=FIRE_PRESSED) //prevent double clicks
                {
                    lastKeyPressed=FIRE_PRESSED;
                    if(action == GameSettings.GAMEINPLAY)
                    {
                        if(currentLevel.levelEnded)
                        {
                            currentLevel.waitForRelease=true;
                        }else
                        {
                            currentLevel.pause();
                            action = GameSettings.SHOWMENU;
                            gameMenu.showSaveQuitGame = true;
                            gameMenu.showReturnGame = true;
                            gameMenu.selectedItem = GameSettings.SHOWMENU;
                        }
                    }
                    if(action == GameSettings.SHOWMENU)
                    {
                        action = gameMenu.getSelectedAction();
                        if((action == GameSettings.GAMEINPLAY) && currentLevel !=null)
                            currentLevel.resume();
                    }
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
            if(currentLevel.levelEnded && currentLevel.waitForRelease)
            {            
                currentLevel = setLevelSettings((currentLevel.id+1),
                                        currentLevel.points,
                                        currentLevel.marblesUsed);
            }else
            {
               currentLevel.advanceMarble();
            }
            currentLevel.draw(super.getGraphics());
        }
    }
    
    public void showMenu()
    {
        gameMenu.showMenu(super.getGraphics());
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

    public void saveGame()
    {
       if(currentLevel!=null)
       {
           message="saved game";
            String serializedLevel = currentLevel.serialize();
            System.out.print("saved game");
            System.out.print(serializedLevel);
            if(!GameData.saveData(GameSettings.SAVEDNAMEGAME, 
                                serializedLevel.getBytes(), 1))
            {
                message="failed to save game";
            }
            message ="Game Saved!";
       }
    }
    
    public void restoreGame()
    {
        try
          {
                byte[] data = GameData.retrieveData(GameSettings.SAVEDNAMEGAME,1);
                if(data==null)
                {
                    message="failed to load saved game";
                    return;
                }
                currentLevel = new GameLevel(0);
                currentLevel.deserialize(new String(data));
                currentLevel.resume();
        }catch(IOException err2)
        {
            message="Failed to load game";
        }
    }
}
