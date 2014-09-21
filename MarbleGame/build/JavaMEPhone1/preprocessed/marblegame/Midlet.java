/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marblegame;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.TextBox;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * @author janiestoy
 */
public class Midlet extends MIDlet  implements CommandListener{

    GameLevels gameLevels;
    GameImages gameImages;
    RecordStore rs;
    Game game;
    //main Menu Controls
    TextBox error;
   List mainMenu;
   Command menu;
   Command quit;
   Command go;
   Display display;
   boolean errorLoading;
   public Midlet()
   {
       try
       {
           gameLevels = new GameLevels();
           gameImages = new GameImages();

            errorLoading = false;
            game = new Game();
            game.gameImages = gameImages;
            game.gameLevels = gameLevels;
          
            game.parentMidlet = this;
            
       }catch(Exception err)
       {
           showError(err);
       }
   }
   
    public void startApp() {
      //  GameLevel level3;
      //  level3 = gameLevels.getGameLevel(gameImages,0);
        if(errorLoading)
            display.setCurrent(error);
        else
        {
            game.action = GameSettings.SHOWMENU;
             display = Display.getDisplay(this);
             display.setCurrent(game);
             new Thread(game).start();
        }    
        
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
    
     public void commandAction(Command cmnd, Displayable dsplbl) {
        
        if(cmnd == go)
        {
          List down = (List)display.getCurrent();
          switch(down.getSelectedIndex()) {
                case 0: loadNewGame();break;
                case 1: loadSavedGame();break;
                case 2: loadHighScores();break;
                case 3: cmnd= quit;
            }
        }
        if(cmnd== quit)
        {
            destroyApp(true);
            notifyDestroyed();
        }
    }
     
    protected void loadNewGame()
    {
       game.action = GameSettings.LOADNEWGAME;
       display.setCurrent(game);
       new Thread(game).start();
    }
    
    protected void loadSavedGame()
    {
       game.action = GameSettings.LOADSAVEDGAME;
       display.setCurrent(game);
       new Thread(game).start();
    }
    
    protected void loadHighScores()
    {
    }
    
    protected void showError(Exception err)
    {
        errorLoading = true;
        error = new TextBox("Failed Loading:",err.getMessage(),5,5);
        quit = new Command("Quit",Command.EXIT,1);
        error.addCommand(quit);
    }
    
    
}
