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
    public static final float COLLISIONFRICTION = 0.8f;
    public static final float GRAVITY =0.09f;
    public static final long FRAMES_PER_SECOND = 80;
    public static final long TICKSPERFRAME = 1000 / FRAMES_PER_SECOND;
    public static final Point OFFSCREENPOINT = new Point(-15,-15,-15);
    public static final long TIMEPEGSHOWNASHIT = 1000;
    public static final long TIMEMONSTEREATS = 2000;
    public static final Point MAXVELOCITY = new Point(5,5,5);
    public static final int MONSTERSPEED = 3;
    public static final Point MARBLESTARTPOSITION = new Point(40,20,0);
    public static final Point SPIDERSTARTPOSITION = new Point(10,60,0);
    public static final int SCOREBOARDHEIGHT = 10;
    
    public static final int LOADNEWGAME =0;
    public static final int LOADSAVEDGAME =1;
    public static final int SAVEGAME =2;
    public static final int QUITGAME =3;
    public static final int GAMEINPLAY =5;

}
