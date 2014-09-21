/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marblegame;

/**
 *
 * @author janiestoy
 */
public class GameSettings {
    public static final int POINTSPERPEG=10;
    public static final int CONSECUTIVEPEGMODIFIER=10;
    public static final int MARBLEPOINTPENALTY=5;
    public static final float FRICTION = 0.8f;
    public static final float COLLISIONFRICTION = 1.0f;
    public static final float GRAVITY =1f;
    public static final long FRAMES_PER_SECOND = 30;
    public static final long TICKSPERFRAME = 1000 / FRAMES_PER_SECOND;
    public static final Point OFFSCREENPOINT = new Point(-15,-15,-15);
    public static final long TIMEPEGSHOWNASHIT = 800;
    public static final long TIMEMONSTEREATS = 3000;
    public static final Point MAXVELOCITY = new Point(10,10,10);
    public static final int MONSTERSPEED =1;
    public static final Point MARBLESTARTPOSITION = new Point(40,40,0);
    public static final Point SPIDERSTARTPOSITION = new Point(120,230,0);
    public static final int SCOREBOARDHEIGHT = 10;
    public static final int SHIFTUNIT = 2;
    public static final Point BORDERCOLOR = new Point(150,150,255);
    public static final Point BACKGROUNDCOLOR = new Point(150,150,255);
    public static final Point FONTCOLOR = new Point(255,255,255);
    public static final int lineHeight = 20;
    public static final String NEWLINE="\n";
    public static final String SEPARATOR=":";
    public static final String SAVEDNAMEGAME="SAVEDLEVEL";
    public static final int maxKeyPressTime =250; //slow down clicks when showing menu - prevents double jumps on menu
    
    public static final int LOADNEWGAME =0;
    public static final int GAMEINPLAY =1;
    public static final int LOADSAVEDGAME =2;
    public static final int SHOWSCORES =3;
    public static final int SAVEGAME =4;
    public static final int QUITGAME =5;
    public static final int SHOWMENU =6;

}
