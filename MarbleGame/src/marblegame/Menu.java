/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marblegame;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *This is the main menu shown to the user
 * 
 */
public class Menu {
    
    public int selectedItem;
    //the available options
    private String[] items = {  "How To Play",
                                "New Game",
                                "Return To Game",
                                "Continue Saved Game",
                                "Save Game",
                                "Quit"};
    public String message;              //a message that can be displayed
    public boolean showReturnGame;      //inidcate if user can return to game 
    public boolean showSaveQuitGame;    //indicate if use can save a game
    public Point fontColor;             //printing settings
    public Point fontSelectedColor;
    public Point borderColor;
    public Point backgroundColor;
    public Point startCorner;
    public int width;               
    public int height;
    public int borderWidth;
    public Image logo;                  //the main logo
    
    public Menu()
    {
        logo = null;
        width = 100;
        height = 100;
        borderWidth = 10;
        showReturnGame = false;
        showSaveQuitGame = false;
        selectedItem = 0;
        fontColor = new Point(GameSettings.BORDERCOLOR);
        fontSelectedColor = new Point(GameSettings.FONTCOLOR);
        borderColor = new Point(GameSettings.BORDERCOLOR);
        backgroundColor = new Point();
        startCorner = new Point();
        message ="";

    }

    //change the highlighted item in the menu to the previous entry
    //if their is no previous entry move highlighted entry to last 
    //menu item
    public void selectPrevious()
    {
      selectedItem = selectedItem<=0?items.length-1:(selectedItem-1);   
      if(selectedItem ==GameSettings.GAMEINPLAY
              && !showReturnGame)
          selectPrevious();
      if(selectedItem ==GameSettings.SAVEGAME
              && !showSaveQuitGame)
          selectPrevious();      
    }
    
        //change the highlighted item in the menu to the next entry
    //if their is no next entry move highlighted entry to first 
    //menu item
    public void selectNext()
    {
      selectedItem = selectedItem>=(items.length-1)?0:(selectedItem+1);  
      if(selectedItem ==GameSettings.GAMEINPLAY
              && !showReturnGame)
          selectNext();
      if(selectedItem ==GameSettings.SAVEGAME
              && !showSaveQuitGame)
          selectNext();
    }
    
    
    //draw the menu
    public void showMenu(Graphics g)
    {
        g.setColor( (int)borderColor.x,
                    (int)borderColor.y,
                    (int)borderColor.z);
        g.fillRect((int)startCorner.x,
                        (int) startCorner.y,
                        width,
                        height);
        g.setColor( (int)backgroundColor.x,
                    (int)backgroundColor.y,
                    (int)backgroundColor.z);
        
        g.fillRoundRect((int)startCorner.x+borderWidth,
                        (int) startCorner.y+ borderWidth,
                        width -  2*borderWidth,
                        height - 2*borderWidth,
                        borderWidth,
                        borderWidth);  
       if(items.length>0)
       {
        int drawingWidth = width  - (int)(startCorner.x+borderWidth);
        int drawingHeight = GameSettings.LOGOHEIGHT + height  - (int)(startCorner.y+borderWidth);

        //draw the logo it was passed in
        if(logo!=null)
        {
            g.drawImage(logo, borderWidth, borderWidth, 0);
        }
        int startX = drawingWidth/4;
        int startY = drawingHeight/items.length ;

        g.setColor( (int)fontColor.x,
                    (int)fontColor.y,
                    (int)fontColor.z);
        
        
        //draw the menu items
        for(int i =0;i<items.length;i++)
        {
            if(i!=selectedItem )
            { //this menu item is not selected paint it in the correct color
                if( (i !=GameSettings.SAVEGAME || showSaveQuitGame)
                   && (i !=GameSettings.GAMEINPLAY || showReturnGame)
                        )
                {
                    g.drawString(items[i], 
                        startX,startY,
                        0);
                    startY +=GameSettings.lineHeight;
                }
            }else
            {   //this menu is currently selected paint it in a different color
                g.setColor( (int)fontSelectedColor.x,
                    (int)fontSelectedColor.y,
                    (int)fontSelectedColor.z);
                g.drawString(items[selectedItem] ,
                    startX,
                    startY,
                    0);                
                startY +=GameSettings.lineHeight;
                g.setColor( (int)fontColor.x,
                    (int)fontColor.y,
                    (int)fontColor.z);
            }
        }
        //if a message has been set print it out at the bottom
         if(message!=null)
         {
             g.drawString(message,startX,startY+(int)GameSettings.lineHeight,0);
         }
       }
    }

    
    //users pressed the fire button figure out what action was selected and return it
    public int getSelectedAction()
    {
        switch(selectedItem)
        {
            case 0: return GameSettings.SHOWTUTORIAL;
            case 1: return GameSettings.LOADNEWGAME;
            case 2: return GameSettings.GAMEINPLAY;
            case 3: return GameSettings.LOADSAVEDGAME;
            case 4: return GameSettings.SAVEGAME;
            case 5: return GameSettings.QUITGAME;
        }
        return GameSettings.SHOWMENU;
    }
}
