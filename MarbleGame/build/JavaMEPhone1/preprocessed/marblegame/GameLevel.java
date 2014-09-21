/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marblegame;

import javax.microedition.lcdui.Graphics;
import java.util.Vector;
import java.io.IOException;

public class GameLevel {

    int id;
    Marble marble;
    Peg[] pegs;
    Monster monster;
    
    int topWall;
    int leftWall;
    int rightWall;
    int bottomWall;
    
    private int pegsTouchedSinceRelease;
    public int points;
    public int marblesUsed;
    public int marblePointDeduction;
    public boolean paused;
    public boolean advancedAfterCollision;
    public boolean marbleLost;
    public boolean pegsCleared;
    public boolean waitForRelease;
    public boolean inProgress;
    public boolean levelEnded;
    public int previousLevelPoints;
    public int previousLevelMarblesUsed;

   //create a string of the current level and all its settings
    public static final String sectionSplitter="^";
    public static final String levelDetailsHeader="~Details~";
    public static final String marbleDetailsHeader="~Marble~";
    public static final String monsterDetailsHeader="~Monster~";
    public static final String pegsDetailsHeader="~Pegs~";
    public static final String pegDetailsHeader="~Peg~";
    
    public static final String levelId ="id";
    public static final String levelAdvancedAfterCollision = "advancedaftercollision";
    public static final String levelInProgress="inprogress";
    public static final String levelMarbleLost="marblelost";
    public static final String levelPegsCleared="pegscleared";
    public static final String levelWaitForRelease="waitforrelease";
    public static final String levelLevelEnded="levelended";
    public static final String levelPoints="points";
    public static final String levelMarblesUsed="marblesused";
    public static final String levelpreviouslevelpoints="previouslevelpoints";
    public static final String levelpreviouslevelmarblesused="previouslevelmarblesused";
    public static final String levelmarblePointDeduction="marblePointDeduction";
    public static final String levelPaused="paused";
    public static final String leveltopwall="topWall";
    public static final String levelbottomwall="bottomWall";
    public static final String levelrightwall="rightWall";
    public static final String levelleftwall="leftWall";
    public static final String levelpegsTouchedSinceRelease="pegsTouchedSinceRelease";

    
    
    public GameLevel(int newId)
    {  
      paused=false;
      id=newId;
      marble = new Marble();
      monster = new Monster();
      resetAllFields();
    }
    
    private void resetAllFields()
    {
      topWall = 0;
      leftWall= 0;
      rightWall =0;
      bottomWall = 0;
      pegsTouchedSinceRelease = 0;
      points = 0;
      marblesUsed = 0;
      marblePointDeduction =0;
      advancedAfterCollision = false;
      marbleLost = false;
      pegsCleared =false;
      waitForRelease = true;
      inProgress = false;
      levelEnded = false;
      previousLevelPoints = 0;
      previousLevelMarblesUsed = 0;
    }

    
  
    public void start(int startPoints, int startMarbles)
    {
        paused=false;
        monster.active= true;
        levelEnded = false;
        inProgress=false;
        waitForRelease = true;
        marble.velocity.copy(new Point(0,0,0));
        marbleLost=false;
        pegsCleared=false;
        points = startPoints;
        marblesUsed = startMarbles;
        marblePointDeduction =0;
        marble.image.setPosition((int)GameSettings.MARBLESTARTPOSITION.x,
                                 (int)GameSettings.MARBLESTARTPOSITION.y);
    }
    
    public void draw(Graphics graphics)
    {
       clearScreen(graphics);
       if(!levelEnded)
       {
        marble.image.paint(graphics);
        monster.image.paint(graphics);
        for(int i =0;i<pegs.length;i++)
        {
            if(pegs[i].active)
            {
                if(pegs[i].timeHit+GameSettings.TIMEPEGSHOWNASHIT> System.currentTimeMillis()
                   || (monster.pegEatingIndex == i  
                      && pegs[i].timeHit+GameSettings.TIMEMONSTEREATS> System.currentTimeMillis()))
                {
                    pegs[i].imageHit.paint(graphics);
                }
                if(pegs[i].timeHit==0)
                {
                    pegs[i].image.paint(graphics);
                }
            }
        }       
        drawScoreBoard(graphics);
       }
       
       if(levelEnded && !waitForRelease)
       {
           GameSummary.drawLevelSummary(graphics,points,marblesUsed,marblePointDeduction);
       }
    }
    
    public void advanceMarble()
    {
        if( paused)
        {
            return;
        }
        if(inProgress )
        {
            advancedAfterCollision = detectCollision();
            if(advancedAfterCollision)
            {
                 advancedAfterCollision = true;
                 marble.setLocation( (int)(marble.getLocation().x+marble.velocity.x),
                                      (int)(marble.getLocation().y+marble.velocity.y));
                 marble.velocity.y +=GameSettings.GRAVITY;
             }
        }
        //advance monster only if user is moving marble into position
        if(!marble.getLocation().equal(GameSettings.MARBLESTARTPOSITION))
            advanceMonster();
       detectMarbleLost();
       
      
    }
    
    
    private boolean detectCollision()
    {
        boolean collisionOccurs = false;
                //using current marble collision
        Marble testMarble = new Marble(marble);

        //move testMarble to future point
        testMarble.velocity.y +=GameSettings.GRAVITY;
        testMarble.setLocation((int)(testMarble.getLocation().x + testMarble.velocity.x),
                                (int)(testMarble.getLocation().y+ testMarble.velocity.y));

        //Tests marble collides with peg
        //      marble collides with monster
        //      marble collides with walls
        //      marble gets Lost
        CollisionDetails cd = detectWallCollision(testMarble);

         if(marble.image.collidesWith(monster.image, true))
        {
            //monster dies
            monster.velocity.copy(new Point(0,0,0));
            monster.timeHit = System.currentTimeMillis();
            monster.active= false;
            cd.resultingVelocity.copy( Collision.getResultingVelocity
                                                (testMarble.getLocation(),
                                                 testMarble.velocity,
                                                 testMarble.image.getHeight(),
                                                 monster.getLocation()));
            cd.collisionOccurs = true;
        }

         if(cd.collisionOccurs)
        {
            advancedAfterCollision= false;
            marble.velocity.copy(cd.resultingVelocity);
            marble.setLocation(cd.pointOfCollision);
            collisionOccurs= cd.collisionOccurs;        
        }
        cd = detectPegCollision(testMarble);
        if(cd.collisionOccurs)
        {
            advancedAfterCollision= false;
            marble.velocity.copy(cd.resultingVelocity);
            marble.setLocation(cd.pointOfCollision);
            accruePoints();
            collisionOccurs= cd.collisionOccurs;        
        }
        return !collisionOccurs;
    }
    
    private void advanceMonster()
    {
        if(!monster.active)
            return;
        if( monster.pegEatingIndex>=0
             &&(   
            (monster.foundPeg ==0 && pegs[ monster.pegEatingIndex].timeHit>0)
           || (monster.foundPeg >0 && monster.foundPeg+GameSettings.TIMEMONSTEREATS < System.currentTimeMillis())))
        {
            monster.pegEatingIndex =-1;
            monster.foundPeg=0;
        }
        
        //find next marble to eat
        if( monster.pegEatingIndex<0)
        {            
            int nextPeg = detectClosestPeg(monster.getLocation());
            if(nextPeg>-1)
            {
                monster.pegEatingIndex = nextPeg;
                monster.foundPeg = 0;
            }else
            {
                detectEndLevel();
            }
        }else
        { //monster tracing course for next marble
            //ensure marble is still active
            //move closer to next Marble
            if( monster.foundPeg==0
                    && monster.image.collidesWith(pegs[ monster.pegEatingIndex].image,true))
            {
                monster.velocity.copy(new Point(0,0,0));
                monster.foundPeg = System.currentTimeMillis();
                pegs[monster.pegEatingIndex].timeHit = System.currentTimeMillis();
            }
            else if(monster.foundPeg==0)
            {
                Point newVelocity = new Point(pegs[monster.pegEatingIndex].getLocation());
                Point monsterPosition = new Point(monster.getLocation());
                newVelocity.subtract(monsterPosition);
                int newX = 0;
                int newY = 0;
                if( newVelocity.x!=0)
                {
                    newX =  (newVelocity.x>0?GameSettings.MONSTERSPEED:-GameSettings.MONSTERSPEED);
                }
                if(newVelocity.y!=0);
                {
                    newY = (newVelocity.y>0?GameSettings.MONSTERSPEED:-GameSettings.MONSTERSPEED);
                }
                newX = (int)(newX+ monster.getLocation().x);
                newY = (int)(newY+ monster.getLocation().y);

                monster.setLocation(newX,newY);
                monster.image.nextFrame();
            }
         }
    }
    
    private CollisionDetails detectWallCollision(Marble movingObject)
    {
        float xRadius =movingObject.image.getWidth()/2;
        float yRadius =movingObject.image.getHeight()/2;
        int posX = (int)movingObject.getLocation().x;
        int posY = (int)(movingObject.getLocation().y- GameSettings.GRAVITY);
        CollisionDetails collisionDetails = new CollisionDetails();
        collisionDetails.pointOfCollision.copy(new Point(posX ,posY,0));
        collisionDetails.resultingVelocity.copy(movingObject.velocity);
        if( ( posX- xRadius) < leftWall)
        {
            collisionDetails.collisionOccurs = true;
            collisionDetails.resultingVelocity.x =  Math.abs(movingObject.velocity.x ); 
        }
        if( (posX+xRadius ) > rightWall)
        {
            collisionDetails.collisionOccurs = true;
            collisionDetails.resultingVelocity.x =  -1 *Math.abs(movingObject.velocity.x ); 
        }
        if( (posY-yRadius) < topWall)
        {
            collisionDetails.collisionOccurs = true;
            collisionDetails.resultingVelocity.y =  Math.abs(movingObject.velocity.y ); 
        }
        marbleLost=( (posY-yRadius) > bottomWall);
        return collisionDetails;
    }
    
    private CollisionDetails detectPegCollision(Marble testMarble)
    {
        CollisionDetails collisionDetails = new CollisionDetails();
        collisionDetails.pointOfCollision.copy(testMarble.getLocation());
        for(int i = 0;i<pegs.length;i++)
        {
            if(pegs[i].active 
               && pegs[i].timeHit ==0
               && pegs[i].image.collidesWith(testMarble.image,true) )
            {
                collisionDetails.collisionOccurs =true;
                collisionDetails.resultingVelocity = Collision.getResultingVelocity
                                                (testMarble.getLocation(),
                                                 testMarble.velocity,
                                                 testMarble.image.getHeight(),
                                                  pegs[i].getLocation());
                pegs[i].timeHit = System.currentTimeMillis();
                return collisionDetails;
            }
        }
        return collisionDetails;
    }
    
   private int detectClosestPeg(Point currentPosition)
   {
       //pick an impossible long distance so that any peg will be closer
       float lowestDistance = bottomWall +rightWall;
       
       //don't select any pegs..cause none may be active.
       int pegSelected  = -1;
       for(int i=0; i<pegs.length;i++)
       {
           if(pegs[i].active && pegs[i].timeHit==0 )
           {
               Point pegLocation =new Point(pegs[i].getLocation());
               pegLocation.subtract(currentPosition);
               float testDistance = (float)pegLocation.magnitude();
               if(testDistance<lowestDistance)
               {
                    lowestDistance = testDistance;
                    pegSelected = i;
               }
           }
       }
       return pegSelected;
   }
    
    private void detectMarbleLost()
    {
            if (marbleLost)
            {
                marble.setLocation((int)GameSettings.MARBLESTARTPOSITION.x,
                                    (int)GameSettings.MARBLESTARTPOSITION.y);
                marble.velocity.copy( new Point(0,0,0));
                marblesUsed++;
                marbleLost = false;
                inProgress=false;
                pegsTouchedSinceRelease=0;
                accrueMarblePoints();
                detectEndLevel();
                waitForRelease = !levelEnded;
            }
    }    
    
    private void detectEndLevel()
    {
        if(!pegsStillActive() && !levelEnded)
        {
            inProgress=false;
            levelEnded=true;
            points = points - marblePointDeduction;
        }
    }
    
    private boolean pegsStillActive()
    {
        for(int i=0;i<pegs.length;i++)
        {
            if(pegs[i].active && pegs[i].timeHit==0)
                return true;
        }
        return false;
    }
    
     protected void clearScreen(Graphics g)
    {
        g.setColor(0x000000);
        g.fillRect(leftWall,
                topWall - GameSettings.SCOREBOARDHEIGHT,
                rightWall, 
                bottomWall);   
    }
     
     public void shiftMarble(float unit)
    {
        if(waitForRelease)
        {        marble.setLocation((int)marble.getLocation().x +(int)unit,
                                    (int)marble.getLocation().y);
        }
    }

    public void releaseMarble()
    {
        paused=false;
        waitForRelease=false;
        inProgress=true;
        pegsTouchedSinceRelease=0;
    }
     
    public void resume()
    {
        paused=false;
    }
    
    public void pause()
    {
        paused=true;
    }
    

    private void drawScoreBoard(Graphics g)
    {
        g.setColor(255,255,255);
        String ptsString ="Points:"+points;
        String mrblString = "Marbles Used:"+marblesUsed;
         g.drawString(ptsString, 10, GameSettings.SCOREBOARDHEIGHT,0);
         g.drawString(mrblString, 100, GameSettings.SCOREBOARDHEIGHT,0);
      
    }
    
    
    private void accruePoints()
    {
            points +=GameSettings.POINTSPERPEG;
            points +=GameSettings.CONSECUTIVEPEGMODIFIER * pegsTouchedSinceRelease;
            pegsTouchedSinceRelease++;            

    }
    private void accrueMarblePoints()
    {
        marblePointDeduction += GameSettings.MARBLEPOINTPENALTY;
    }
    
 
    public String serialize()
    {
        String newLine=GameSettings.NEWLINE;
        String serializedData ="";
        serializedData +=serializeDetails();
       serializedData +=sectionSplitter+newLine;
       serializedData +=monsterDetailsHeader+newLine;
       serializedData +=monster.serialize()+newLine;
       serializedData +=sectionSplitter+newLine;
       serializedData +=marbleDetailsHeader+newLine;
       serializedData +=marble.serialize();
       serializedData +=sectionSplitter+newLine;
       serializedData += pegsDetailsHeader+newLine; 
       serializedData +=serializePegs()+newLine;
       
        return serializedData;
    }
   
   
    private String serializePegs()
    {
        String serializedData ="";
        String newLine=GameSettings.NEWLINE;
        for(int i=0;i<pegs.length;i++)
        {
             serializedData += pegDetailsHeader+newLine; 
             serializedData += pegs[i].serialize();
        }
        return serializedData;
    }
    private String serializeDetails()
    {
        String newLine=GameSettings.NEWLINE;
        String serializedData = "";
        serializedData +=levelDetailsHeader+newLine;
        serializedData +=levelId+GameSettings.SEPARATOR+id+newLine;
        serializedData +=levelAdvancedAfterCollision+GameSettings.SEPARATOR+advancedAfterCollision+newLine;
        serializedData +=levelInProgress+GameSettings.SEPARATOR+inProgress+newLine;
        serializedData +=levelMarbleLost+GameSettings.SEPARATOR+marbleLost+newLine;
        serializedData +=levelPegsCleared+GameSettings.SEPARATOR+pegsCleared+newLine;
        serializedData +=levelWaitForRelease+GameSettings.SEPARATOR+waitForRelease+newLine;
        serializedData +=levelLevelEnded+GameSettings.SEPARATOR+levelEnded+newLine;
        serializedData +=levelPoints+GameSettings.SEPARATOR+points+newLine;
        serializedData +=levelMarblesUsed+GameSettings.SEPARATOR+marblesUsed+newLine;
        serializedData +=levelpegsTouchedSinceRelease+GameSettings.SEPARATOR+pegsTouchedSinceRelease+newLine;
        serializedData +=levelpreviouslevelpoints+GameSettings.SEPARATOR+previousLevelPoints+newLine;
        serializedData +=levelpreviouslevelmarblesused+GameSettings.SEPARATOR+previousLevelMarblesUsed+newLine;
        serializedData +=levelmarblePointDeduction+GameSettings.SEPARATOR+marblePointDeduction+newLine;
        serializedData +=levelPaused+GameSettings.SEPARATOR+paused+newLine;
        serializedData +=leveltopwall+GameSettings.SEPARATOR+topWall+newLine;
        serializedData +=levelbottomwall+GameSettings.SEPARATOR+bottomWall+newLine;
        serializedData +=levelrightwall+GameSettings.SEPARATOR+rightWall+newLine;
        serializedData +=levelleftwall+GameSettings.SEPARATOR+leftWall+newLine;
        return serializedData;
    }
    
    public void deserializeDetails(String src)
    {
         String newLine=GameSettings.NEWLINE;
        Vector attributes = Split.split(src,newLine);
        for (int i=0;i<attributes.size();i++)
        {
            Vector nameValuePair = Split.split((String)attributes.elementAt(i), GameSettings.SEPARATOR);
            if ( ((String)nameValuePair.elementAt(0)).equals( levelId))
            {id =Integer.parseInt((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelAdvancedAfterCollision))
            {advancedAfterCollision =Split.parseBoolean((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelInProgress))
            {inProgress =Split.parseBoolean((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelMarbleLost))
            {marbleLost =Split.parseBoolean((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelPegsCleared))
            {pegsCleared =Split.parseBoolean((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelWaitForRelease))
            {waitForRelease =Split.parseBoolean((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelLevelEnded))
            {levelEnded =Split.parseBoolean((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelPoints))
            {points =Integer.parseInt((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelMarblesUsed))
            {marblesUsed =Integer.parseInt((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelpegsTouchedSinceRelease))
            {pegsTouchedSinceRelease =Integer.parseInt((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelpreviouslevelpoints))
            {previousLevelPoints =Integer.parseInt((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelpreviouslevelmarblesused))
            {previousLevelMarblesUsed =Integer.parseInt((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelmarblePointDeduction))
            {marblePointDeduction =Integer.parseInt((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelPaused))
            {paused =Split.parseBoolean((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( leveltopwall))
            {topWall =Integer.parseInt((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelbottomwall))
            {bottomWall =Integer.parseInt((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelrightwall))
            {rightWall =Integer.parseInt((String)nameValuePair.elementAt(1));}
            if ( ((String)nameValuePair.elementAt(0)).equals( levelleftwall))
            {leftWall =Integer.parseInt((String)nameValuePair.elementAt(1));}
        }
    }
    
    public void deserializePegs(String src) throws IOException
    {
        Vector mainSection = Split.split(src,pegsDetailsHeader);
        Vector sections;
        if(mainSection.size()>1)
        {
            sections = Split.split((String)mainSection.elementAt(1),pegDetailsHeader);
        }else
        {
            sections = Split.split(src,pegDetailsHeader);
        }
        pegs = new Peg[sections.size()];
        for(int i=0;i<sections.size();i++)
        {
            pegs[i] = new Peg();
            pegs[i].active = false;
            String section = (String)sections.elementAt(i);
            if(section.length()>1)
            {
                pegs[i].deserialize(section);
            }
        }
    }
    
    public void deserialize(String src)throws IOException
    {
        String newLine=GameSettings.NEWLINE;
        Vector sections = Split.split(src,sectionSplitter+newLine);
        for(int i =0;i<sections.size();i++)
        {
            String section = (String) sections.elementAt(i);
            if(section.startsWith(levelDetailsHeader))
            {
                deserializeDetails(section);
            }
            if(section.startsWith(marbleDetailsHeader))
            {
                marble.deserialize(section);
            }
            if(section.startsWith(monsterDetailsHeader))
            {
                monster.deserialize(section);
            }
            if(section.startsWith(pegsDetailsHeader))
            {
                deserializePegs(section);
            }
        }
    }
    
    
    
    
}
