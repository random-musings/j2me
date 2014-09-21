/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marblegame;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author janiestoy
 */
public class Menu {
    
    public int selectedItem;
    private String[] items = {  "New Game",
                                "Return To Game",
                                "Continue Saved Game",
                                "High Scores",
                                "Save Game",
                                "Quit"};
    public String message;
    public boolean showReturnGame;
    public boolean showSaveQuitGame;
    public Point fontColor;
    public Point fontSelectedColor;
    public Point borderColor;
    public Point backgroundColor;
    public Point startCorner;
    public int width;
    public int height;
    public int borderWidth;
    
    public Menu()
    {
        width = 100;
        height = 100;
        borderWidth = 10;
        showReturnGame = false;
        showSaveQuitGame = false;
        selectedItem = 0;
        fontColor = new Point(255,255,255);
        fontSelectedColor = new Point(150,150,255);
        borderColor = new Point(150,150,255);
        backgroundColor = new Point();
        startCorner = new Point();
        message ="";
    }

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
        int drawingHeight =  height  - (int)(startCorner.y+borderWidth);
        
        int startX = drawingWidth/4;
        int startY = drawingHeight/items.length ;

        g.setColor( (int)fontColor.x,
                    (int)fontColor.y,
                    (int)fontColor.z);
        
        for(int i =0;i<items.length;i++)
        {
            if(i!=selectedItem )
            {
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
            {
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
         if(message!=null)
         {
             g.drawString(message,startX,startY+(int)GameSettings.lineHeight,0);
         }
       }
    }

    public int getSelectedAction()
    {
        switch(selectedItem)
        {
            case 0: return GameSettings.LOADNEWGAME;
            case 1: return GameSettings.GAMEINPLAY;
            case 2: return GameSettings.LOADSAVEDGAME;
            case 3: return GameSettings.SHOWSCORES;
            case 4: return GameSettings.SAVEGAME;
            case 5: return GameSettings.QUITGAME;
        }
        return GameSettings.SHOWMENU;
    }
}
