/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marblegame;

/**
 *This file holds constants for our game
 *
 */
public class GameSettings {
    public static final int POINTSPERPEG=10;
    
    //when multiple pegs hit in one drop apply additional points
    public static final int CONSECUTIVEPEGMODIFIER=10;
    
    //points deducted for each marble dropped
    public static final int MARBLEPOINTPENALTY=5;
    
    //amount of friction applied to marble as it moves through the world
    public static final float FRICTION = 0.8f;
    
    //the friction applied when marble collides - prevents crazy bounces
    public static final float COLLISIONFRICTION = 1.0f;
    
    //gravity applied to marble
    public static final float GRAVITY =1f;
    
    //number of frames we wish to render per seconds
    public static final long FRAMES_PER_SECOND = 60;
    public static final long TICKSPERFRAME = 1000 / FRAMES_PER_SECOND;

    //amount of time in milliseconds to show peg as hit
    public static final long TIMEPEGSHOWNASHIT = 800;
    
    //time monster spends destroying a peg before moving on
    public static final long TIMEMONSTEREATS = 3000;
    
    //max velocity of marble -- so we can accurately handle collision
    public static final Point MAXVELOCITY = new Point(11,11,11);
    
    //speed of monster
    public static final int MONSTERSPEED =1;
    
    //where all marbles are placed at beginning of level
    public static final Point MARBLESTARTPOSITION = new Point(120,40,0);
    
    //where all monsters are placed at beginning of level
    public static final Point SPIDERSTARTPOSITION = new Point(120,230,0);
    
    //space reequired to print points at top of game screen
    public static final int SCOREBOARDHEIGHT = 15;
    
    //height of main screen logo
    public static final int LOGOHEIGHT = 35;
    
    //how many pixels to shift the marble when lef tor right key presseed
    public static final int SHIFTUNIT = 2;
    
    //widht of border (menu, game summary)
    public static final int BORDERHEIGHT = 5;
    
    //color of border
    public static final Point BORDERCOLOR = new Point(200,0,0);
    
    //background color
    public static final Point BACKGROUNDCOLOR = new Point(0,0,0);
    
    //font color of selected menu item
    public static final Point FONTCOLOR = new Point(255,255,255);
    
    //height of selected font
    public static final int lineHeight = 20;
    
    //serialization settings 
    public static final String NEWLINE="\n";
    public static final String SEPARATOR=":";
    
    //name of record store
    public static final String SAVEDNAMEGAME="SAVEDLEVEL";
    
    //time between responding to user click events
    public static final int maxKeyPressTime =250; //slow down clicks when showing menu - prevents double jumps on menu
            
    //Game states
    public static final int SHOWTUTORIAL=0;
    public static final int LOADNEWGAME =1;
    public static final int GAMEINPLAY =2;
    public static final int LOADSAVEDGAME =3;
    public static final int SAVEGAME =4;
    public static final int QUITGAME =5;
    public static final int SHOWMENU =6;

}
