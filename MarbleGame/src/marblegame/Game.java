/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marblegame;

import java.io.IOException;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *this is the game loop thread it handles the following
 * 1. all click events for the midlet
 * 2. controls the game play (level loading,playing etc)
 * 3. midlet navigation (main menu, tutorial, save & restore of game)
 */
public class Game extends GameCanvas implements Runnable{
    
    GameImages gameImages; //all game images
    GameLevels gameLevels; //all game levels
    GameLevel currentLevel; //the current level being played
    GameTutorial gameTutorial; //the instructions on how to play the game
    
    Midlet parentMidlet; //link to the parent midlet - only useed when quitting
    Menu gameMenu; //the main menu
    int action; //the current state of the midlet ( 
                //possible choices
                //SHOWMENU  ->shows the user the main menu
                //LOADNEWGAME ->loads the first level of the game into menu
                //LOADSAVEDGAME --> loads the saved game iinto memory
                //QUIT --> quits the app
                //SAVEGAME --> saves the current game
                //SHOWTUTORIAL --> shows instructions on how to play
                //GAMEINPLAY --> game currently in play
    
    //int lastKeyPressed;
    long keyPressTime; //the time the last click event occured 
                        //this is recorded so that we can prevent accidental double 
                        //clicks GameSettings.maxKeyPressTime holds the time
                        // that must  pass before another click event is allowed
    
    private int screenHeight; //the height of the phone used for determining borders
    private int screenWidth; //the width of the phone used for determining borders
    private String message; //the message to notifty the user of the success of the last action

    
    //constructor inits variables
    public Game () throws IOException
    {
        //init all settings
        super(false);
        gameTutorial = new GameTutorial();
        action = GameSettings.LOADNEWGAME;
        screenHeight = this.getHeight();
        screenWidth  = this.getWidth();
        gameMenu  = new Menu();
        gameMenu.width = this.getWidth();
        gameMenu.height  = this.getHeight();
        gameImages = new GameImages();
       gameMenu.logo = gameImages.getImage(GameImages.MARBLELOGO);
        currentLevel = null;
        keyPressTime=0;

    }
    
    //the main game loop
    public void run()
    {
        while(true)
        {
            //check to see if events have occured
            handleKeyEvents();
            if(action == GameSettings.SHOWTUTORIAL)
            {
                gameTutorial.drawInstructions(super.getGraphics());
            }
            if(action == GameSettings.LOADNEWGAME)
            {
                loadNewGame();
                action = GameSettings.GAMEINPLAY;
            }
            if(action == GameSettings.LOADSAVEDGAME)
            {
                //restore a game if restore fails show the user the menu
                //the message will be set telling the user of the error
               action =restoreGame()?GameSettings.GAMEINPLAY:GameSettings.SHOWMENU;
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
            {   //game is in play - advance marble etc.
                manageGamePlay();
            }
            flushGraphics();
           try
            {   //shut down the thread so we don't consume all resources
                Thread.sleep(GameSettings.TICKSPERFRAME);
            }catch(InterruptedException ex)
            {}
           
        }   
    }
    

    //if a user clicks a button it will be handled
    private void handleKeyEvents()
    {
    int keyState = getKeyStates();
            //up_pressed is only listened to if we are showing the user the menu
            if((keyState & UP_PRESSED)!=0)
            {
                if(action == GameSettings.SHOWMENU && System.currentTimeMillis()>keyPressTime)
                {
                    gameMenu.selectPrevious();
                    keyPressTime = System.currentTimeMillis()+GameSettings.maxKeyPressTime;
                }
            }else if ( (keyState & RIGHT_PRESSED)>0)
            {   //right pressed shifts marble to the right 
                if(action == GameSettings.GAMEINPLAY && currentLevel!=null)
                {
                    currentLevel.shiftMarble(GameSettings.SHIFTUNIT);
                }
            }else if((keyState & LEFT_PRESSED)>0)
            {   //left pressed  shifts marble to the left
                if(action == GameSettings.GAMEINPLAY && currentLevel!=null)
                {
                    currentLevel.shiftMarble(-GameSettings.SHIFTUNIT);
                }
            }else if( (keyState & DOWN_PRESSED)>0)
            {   //will release marble if game is being played
                //or navigate the menu
                if(action == GameSettings.SHOWMENU && System.currentTimeMillis()>keyPressTime)
                {
                    keyPressTime = System.currentTimeMillis()+GameSettings.maxKeyPressTime;
                    gameMenu.selectNext();
                }
                if(action == GameSettings.GAMEINPLAY && currentLevel!=null)
                {
                    currentLevel.releaseMarble();
                }    
            }else if( (keyState & FIRE_PRESSED)>0)
            {   //returns user to the menu if game is being played
                //returns user to menu if How To Play is being shown
                //will select a menu option if menu is being shown
                //will load another level if current level has been completed
                if(System.currentTimeMillis()>keyPressTime) //prevent double clicks
                {
                    keyPressTime = System.currentTimeMillis()+GameSettings.maxKeyPressTime;
                    if(action== GameSettings.SHOWTUTORIAL)
                    {
                        action = GameSettings.SHOWMENU;
                        return;
                    }
                    if(action == GameSettings.GAMEINPLAY
                    && currentLevel!=null 
                    && currentLevel.gameEnded
                    && currentLevel.continueToNextLevel)
                    {
                        action = GameSettings.SHOWMENU;
                        return;
                    }
                    if(action == GameSettings.GAMEINPLAY)
                    {
                      
                            if(currentLevel.levelEnded )
                            {
                                currentLevel.continueToNextLevel=true;
                                return;
                            }else
                            {
                                message="game paused!"; 
                                currentLevel.pause();
                                action = GameSettings.SHOWMENU;
                                gameMenu.showSaveQuitGame = !currentLevel.gameEnded;
                                gameMenu.showReturnGame = !currentLevel.gameEnded;
                                gameMenu.selectedItem = GameSettings.SHOWMENU;
                                return;
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
    
    
    //if game is being played this function
    //handles the 
    //*game play
    //*drawing of game objects
    //*loading of new levels once a level was completed
  public void manageGamePlay()
    {
        //if currentLevel==null showERror & menu
        //if currentLevel ended show summary
        //if currentLevel ended and game ended
        if(currentLevel !=null)
        {
            if(currentLevel.levelEnded && currentLevel.continueToNextLevel)
            {          
                if(!gameLevels.isLastLevel(currentLevel.id+1))
                {
                currentLevel = setLevelSettings((currentLevel.id+1),
                                        0,
                                        0,
                                        currentLevel.previousLevelPoints +currentLevel.points,
                                        currentLevel.previousLevelMarblesUsed +currentLevel.marblesUsed);
                }else
                {
                    currentLevel.gameEnded = true;
                }
            }else
            {
               currentLevel.advanceMarble();
            }
           currentLevel.draw(super.getGraphics()); 
        }
    }
    
  
  //shows menu to user
  //and prints out messages to user if 
  //  game was paused
  //  level was saved
  //  failed to load saved level
  //  game was completed
    public void showMenu()
    {
        if(currentLevel!=null)
        {
            if(currentLevel.gameEnded)
            {
                gameMenu.showReturnGame=false;
                gameMenu.showSaveQuitGame=false;
                message="Game Completed!";
            }
        }
        gameMenu.message = message;
        gameMenu.showMenu(super.getGraphics());
    }
    
    
    //loads a new game 
    public void loadNewGame()
    {
        currentLevel =setLevelSettings(0,0,0,0,0);
    }
    
    //creates the requested game level 
    //sets all variables required to start the game
    private GameLevel setLevelSettings(int index,
                                int startingPoints, 
                                int startingMarbles,
                                int previousPoints,
                                int previouseMarbles)
    {
        GameLevel level =gameLevels.getGameLevel(gameImages, index);
        if(level ==null)
        {
            currentLevel.gameEnded=true;
            return currentLevel;
        }
        level.logo = gameImages.getImage(GameImages.MARBLELOGO);
        level.previousLevelMarblesUsed = previouseMarbles;
        level.previousLevelPoints = previousPoints;
        level.topWall = GameSettings.SCOREBOARDHEIGHT;
        level.leftWall = 0;
        level.rightWall = screenWidth;
        level.bottomWall = screenHeight;
        level.start(startingPoints,startingMarbles);
        return level;
    }

    
    //saves game to the record store GameSettings.SAVEDNAMEGAME
    public void saveGame()
    {
       if(currentLevel!=null)
       {
            String serializedLevel = currentLevel.serialize();
            if(!GameData.saveData(GameSettings.SAVEDNAMEGAME, 
                                serializedLevel.getBytes(), 1))
            {
                message="failed to save game";
                return;
            }
            message ="Game Saved!";
       }
    }
    
    //opens the record store and loads a game into memory
    public boolean restoreGame()
    {
        try
          {
                byte[] data = GameData.retrieveData(GameSettings.SAVEDNAMEGAME,1);
                if(data==null)
                {
                    message="Saved game not found";
                    return false;
                }
                currentLevel = new GameLevel(0);
                currentLevel.deserialize(new String(data));
                currentLevel.resume();
        }catch(IOException err2)
        {
            message="Failed to load game";
            return false;
        }
        return true;
    }
}
