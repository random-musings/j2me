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
           //we initialize these here cause netbeans cause errring out when initialized
           //in the constructor..error went mysteriously away
           //I didn't move them bac in though..cause I believe in function over form
           gameLevels = new GameLevels();
           gameImages = new GameImages();

            errorLoading = false;
            game = new Game();
            game.gameImages = gameImages;
            game.gameLevels = gameLevels;
          
            //pass in a reference to the parent so that we can call notifydestroyed and quit
            game.parentMidlet = this;
            
       }catch(Exception err)
       {
           showError(err);
       }
   }
   
   //start the app
    public void startApp() {
           //check if we had issues loading the images
        if(errorLoading)
            display.setCurrent(error);
        else
        {//no errors loading  them images so begin the game
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
    
    }
 
    
    protected void showError(Exception err)
    {
        errorLoading = true;
        error = new TextBox("Failed Loading:",err.getMessage(),5,5);
        quit = new Command("Quit",Command.EXIT,1);
        error.addCommand(quit);
    }
    
    
}
