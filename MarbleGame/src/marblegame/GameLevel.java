/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marblegame;

import javax.microedition.lcdui.Graphics;
import java.util.Vector;
import java.io.IOException;
import javax.microedition.lcdui.Image;

/*This ia an actual Game level
 * it holds all game objects
 * advances all game play
 * detects and acts on all in game events
 */
public class GameLevel {

    int id;             //the level ID 
    Marble marble;       //the marble that is dropped
    Peg[] pegs;         //and array of pegs
    Monster monster;    //the monster that eats pegs
    public Image logo;  //the Marble Drop logo show when drawing level and game summaries
    
    int topWall;           //the top border of the game set when level is initialized
    int leftWall;           //the left border of the game set when level is initialized
    int rightWall;          //the right border of the game set when level is initialized
    int bottomWall;         //the bottom border of the game set when level is initialized
    
    private int pegsTouchedSinceRelease;    //count pegs touched since release so we can apply bonus points
    public int points;                      //numer of points this level
    public int marblesUsed;                 //number of marbles used this level
    public int marblePointDeduction;        //number of points deducted this level
    public boolean paused;                  //game is paused because user returned to menu
    public boolean advancedAfterCollision;  //usaed to stop the game play and advance marble to collision point
    public boolean marbleLost;              //marble dropped off the screen
    public boolean pegsCleared;             //all pegs cleared on level - level ends
    public boolean waitForRelease;          //marble is at top of screen waiting to be released
    public boolean inProgress;              //game is in progress (pegs still exist)
    public boolean levelEnded;              //all pegs cleared level is over
    public boolean gameEnded;               //all levels completed
    public boolean continueToNextLevel;     //level is compelted - summary is being shown
    public int previousLevelPoints;         //accumulation of points from all previous levels
    public int previousLevelMarblesUsed;    //accumulation of mrbles from all previous levels

    //Settings below are used to serialize this class
    //for storage in record store
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
    //end of serialization section

    //creates the level
    public GameLevel(int newId)
    {  
      paused=false;
      id=newId;
      marble = new Marble();
      monster = new Monster();
      resetAllFields();
    }
    
    
    //clear fields to empty values
    private void resetAllFields()
    {
      continueToNextLevel = false;
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
      gameEnded = false;
    }

    
  //set all fields so that a level can start from the beginning
    public void start(int startPoints, int startMarbles)
    {
        continueToNextLevel = false;
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
        gameEnded = false;
    }
    
    //draw all game objects
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
       
       //level or game ended show the summary
      if(levelEnded )
       {

           if(gameEnded )
                 GameSummary.drawEndOfGame(
                         logo,
                         graphics,
                         "Game Completed",
                         points ,
                         marblesUsed,
                         marblePointDeduction,
                         points + previousLevelPoints );
           else  if( !continueToNextLevel )
                 GameSummary.drawLevelSummary( logo,
                            graphics,
                            "level "+(id+1),
                            points,
                            marblesUsed,
                            marblePointDeduction);
       }
    }
    
    //advance the marble through the screen
    //check for collisions
    // advance the monster
    //detect  in game events like 
    //  lost marble (marble drops off screen)
    //  end of game
    //  end of level
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
    

     //Test if marble collides with peg
    //      marble collides with monster
    //      marble collides with walls
    //      marble gets Lost
    private boolean detectCollision()
    {
        boolean collisionOccurs = false;
                //using current marble collision
        Marble testMarble = new Marble(marble);

        //move testMarble to future point
        //apply gravity
        testMarble.velocity.y +=GameSettings.GRAVITY;
        testMarble.setLocation((int)(testMarble.getLocation().x + testMarble.velocity.x),
                                (int)(testMarble.getLocation().y+ testMarble.velocity.y));

        //check if marble collides with walls
        CollisionDetails cd = detectWallCollision(testMarble);

        //if marble collides with monster
         if(marble.image.collidesWith(monster.image, true))
        {
            //monster dies
            monster.velocity.copy(new Point(0,0,0));
            monster.setLocation(GameSettings.SPIDERSTARTPOSITION);
            cd.resultingVelocity.copy( Collision.getResultingVelocity
                                                (testMarble.getLocation(),
                                                 testMarble.velocity,
                                                 testMarble.image.getHeight(),
                                                 monster.getLocation()));
            cd.collisionOccurs = true;
        }

         //if collision occurs in above settings then change marble velocity
         if(cd.collisionOccurs)
        {
            advancedAfterCollision= false;
            marble.velocity.copy(cd.resultingVelocity);
            marble.setLocation(cd.pointOfCollision);
            collisionOccurs= cd.collisionOccurs;        
        }
         //check if marble collides with pegs
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
    
    
    //handles all monster events 
    private void advanceMonster()
    {
        //if monster is dead do nothing
        if(!monster.active)
            return;
        
        //advance the image to the next frame sequence so it looks like its moving
         monster.image.nextFrame();
         
         //if the monster is not destroying a peg that is active
         //then reset settings so that it will find a new peg
        if(monster.pegEatingIndex<0 
            ||pegs.length<= monster.pegEatingIndex
            || pegs[ monster.pegEatingIndex]==null
            || pegs[ monster.pegEatingIndex].image == null)
        {
            monster.pegEatingIndex =-1;
            monster.foundPeg=0;    
        }
        
        //if monster advancing towards a peg but the peg is no longer active
        //or invalid then reset its settings so that it will find a new peg
        if( monster.pegEatingIndex>=0
             &&(   
            (monster.foundPeg ==0 && pegs[ monster.pegEatingIndex].timeHit>0)
           || (monster.foundPeg >0 && monster.foundPeg+GameSettings.TIMEMONSTEREATS < System.currentTimeMillis())))
        {
            monster.pegEatingIndex =-1;
            monster.foundPeg=0;
        }
        
        //monster has no peg to destroy  -  it will look for a new one
        if( monster.pegEatingIndex<0)
        {            
            int nextPeg = detectClosestPeg(monster.getLocation());
            if(nextPeg>-1)
            { //monster found an active peg...set a course for the peg
                monster.pegEatingIndex = nextPeg;
                monster.foundPeg = 0;
            }else
            { //no active pegs. check to see if the level ended
                detectEndLevel();
            }
        }else
        {  //monster currently advancing towards an active peg
            //ensure peg is still active
            if( monster.foundPeg==0
                && monster.image.collidesWith(pegs[ monster.pegEatingIndex].image,true))
            {   //monster reached peg...begins destroying it
                monster.velocity.copy(new Point(0,0,0));
                monster.foundPeg = System.currentTimeMillis();
                pegs[monster.pegEatingIndex].timeHit = System.currentTimeMillis();
            }
            else if(monster.foundPeg==0)
            {  //monster still hasn't reached the peg.
                //move the monster closer
                //ensure that the peg it is looking for is still active
                //and that it is taking the shortest route
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
               
            }
         }
    }
    
    
    //checks to see if marble is bouncing off of the walls or ceiling
    //if the marble reaches a wall its velocity is adjusted accordingly
    //will also check to see if the marble was lost (dropped below the bottom wall
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
    
    
    //checks to see if a marble is colliding with any of the pegs
    //if it does return the collision details so its velcoity can be adjusted
    private CollisionDetails detectPegCollision(Marble testMarble)
    {
        CollisionDetails collisionDetails = new CollisionDetails();
        collisionDetails.pointOfCollision.copy(testMarble.getLocation());
        for(int i = 0;i<pegs.length;i++)
        {
            Point collidingPoint = new Point(testMarble.getLocation());
            if(pegs[i].active 
               && pegs[i].timeHit ==0
               && (pegs[i].image.collidesWith(testMarble.image,true) 
                   || Collision.marblePathIntersectsWithPeg(testMarble,pegs[i],collidingPoint)) )
            {
                
                collisionDetails.collisionOccurs =true;
                collisionDetails.resultingVelocity = Collision.getResultingVelocity
                                                (testMarble.getLocation(),
                                                 testMarble.velocity,
                                                 testMarble.image.getWidth(),
                                                  pegs[i].getLocation());
                collisionDetails.pointOfCollision = collidingPoint;
                pegs[i].timeHit = System.currentTimeMillis();
                return collisionDetails;
            }
        }
        return collisionDetails;
    }
    
    
    //the monster uses this function to find the closest peg to destroy
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
               {    //we found a peg that is active 
                    lowestDistance = testDistance;
                    pegSelected = i;
               }
           }
       }
       return pegSelected;
   }
    
   //if the marble was lost then adjust points and all other variables
   //so that the user can "re-drop" the marble to finish the level
    private void detectMarbleLost()
    {
            if (marbleLost)
            {
                marble.setLocation((int)GameSettings.MARBLESTARTPOSITION.x,
                                    (int)GameSettings.MARBLESTARTPOSITION.y);
                marble.velocity.copy( new Point(0,0,0));
                marbleLost = false;
                inProgress=false;
                pegsTouchedSinceRelease=0;
                accrueMarblePoints();
                detectEndLevel();
                waitForRelease = !levelEnded;
            }
    }    
    
    //if  we havn't declared the level over
    //then check if any pegs active
    //if none found then apply then penalize the users for using marbles
    //set the flags so that the user will be shown the level summary
    private void detectEndLevel()
    {
        if(!pegsStillActive() && !levelEnded)
        {
            inProgress=false;
            levelEnded=true;
            continueToNextLevel = false;
            GameLevels gl = new GameLevels();
           gameEnded  = gl.isLastLevel(id+1);
            points = points - marblePointDeduction;
        }
    }
    
    //runs through the peg array to see if any have not been hit
    private boolean pegsStillActive()
    {
        for(int i=0;i<pegs.length;i++)
        {
            if(pegs[i].active && pegs[i].timeHit==0)
                return true;
        }
        return false;
    }
    
    //clear the screen with our background color
     protected void clearScreen(Graphics g)
    {
        g.setColor(0x000000);
        g.fillRect(leftWall,
                topWall - GameSettings.SCOREBOARDHEIGHT,
                rightWall, 
                bottomWall);   
    }
     
     //shift the marble left or right before dropping it
     public void shiftMarble(float unit)
    {
        if(waitForRelease)
        {        marble.setLocation((int)marble.getLocation().x +(int)unit,
                                    (int)marble.getLocation().y);
    
            //check to ensure that we have not shifted off the screen
             CollisionDetails cd = detectWallCollision(marble);
             if(cd.collisionOccurs)
             {//oops we hit a wall. Undo the shift
                    marble.setLocation((int)marble.getLocation().x -(int)unit,
                                    (int)marble.getLocation().y); 
             }
        }
    }

     //drop the marble and begin the game
     //set all flags 
     //increment the number of marbles used
    public void releaseMarble()
    {
        if(waitForRelease)
        {
            paused=false;
            waitForRelease=false;
            inProgress=true;
            pegsTouchedSinceRelease=0;
            marblesUsed++;
        }
    }
     
 
    //user returns from the menu to the active game - this starts the game play up
    public void resume()
    {
        paused=false;
    }
    
   //user presses fire and we pause the game using this function
    public void pause()
    {
        paused=true;
    }
    

    //the score board at the top
    private void drawScoreBoard(Graphics g)
    {
        g.setColor(255,255,255);
        String ptsString ="Points:"+points;
        String mrblString = "Marbles Used:"+marblesUsed;
         g.drawString(ptsString, 10, GameSettings.SCOREBOARDHEIGHT,0);
         g.drawString(mrblString, 100, GameSettings.SCOREBOARDHEIGHT,0);
      
    }
    
    //accrues poitns as the pegs have been hit
    private void accruePoints()
    {
            points +=GameSettings.POINTSPERPEG;
            points +=GameSettings.CONSECUTIVEPEGMODIFIER * pegsTouchedSinceRelease;
            pegsTouchedSinceRelease++;            

    }
    
    //accrues the marble deduction points
    private void accrueMarblePoints()
    {
        marblePointDeduction += GameSettings.MARBLEPOINTPENALTY;
    }
    
 //creates a string of class data so it can be stored in the RecordStore
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
   
   //creates a string of pegs so they can be stored in the record store
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
    
    
    //creates a string of level flags so they can be stored in the record store
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
    
    
    //takes data from the record store and creates a game level
    //that can be resumed
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
    
    //creates the pegs from data from a record store so that the level can be played
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
    
    //takes a level in string format and instantiates the current class with 
    //data from the record store
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
