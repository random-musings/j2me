/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marblegame;

import javax.microedition.lcdui.Graphics;


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
    public boolean advancedAfterCollision;
    public boolean marbleLost;
    public boolean pegsCleared;
    public boolean waitForRelease;
    public boolean inProgress;
    public boolean levelEnded;
    public int previousLevelPoints;
    public int previousLevelMarblesUsed;

    
    public GameLevel(int newId)
    { 
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
         monster.active= true;
        levelEnded = false;
        inProgress=false;
        waitForRelease = true;
        marble.velocity.copy(new Point(0,0,0));
        marbleLost=false;
        pegsCleared=false;
        points = startPoints;
        marblesUsed = startMarbles;
        marble.image.setPosition((int)GameSettings.MARBLESTARTPOSITION.x,
                                 (int)GameSettings.MARBLESTARTPOSITION.y);
    }
    
    public void draw(Graphics graphics)
    {
       clearScreen(graphics);
       marble.image.paint(graphics);
       monster.image.paint(graphics);
       for(int i =0;i<pegs.length;i++)
       {
           if(pegs[i].active)
           {
               if(pegs[i].timeHit+GameSettings.TIMEPEGSHOWNASHIT> System.currentTimeMillis())
               {
                   pegs[i].imageHit.paint(graphics);
               }
               if(pegs[i].timeHit==0)
               {
                   pegs[i].image.paint(graphics);
               }
           }
       }
        
    }
    
    public void advanceMarble()
    {
        if(!inProgress)
        {
            return;
        }
        
       advancedAfterCollision = detectCollision();
       if(advancedAfterCollision)
       {
            advancedAfterCollision = true;
            marble.image.setRefPixelPosition(marble.image.getRefPixelX()+(int)marble.velocity.x,
                                            marble.image.getRefPixelY()+(int)marble.velocity.y);
            marble.velocity.y +=GameSettings.GRAVITY;
        }
        //advance monster
       
       detectMarbleLost();
      
    }
    
    
    private boolean detectCollision()
    {
        boolean collisionOccurs = false;
                //using current marble collision
        Marble testMarble = new Marble(marble);

        //move testMarble to future point
        testMarble.velocity.y +=GameSettings.GRAVITY;
        testMarble.image.setRefPixelPosition(testMarble.image.getRefPixelX()+ (int)testMarble.velocity.x,
                                             testMarble.image.getRefPixelY()+ (int)testMarble.velocity.y);

        //Tests marble collides with peg
        //      marble collides with monster
        //      marble collides with walls
        //      marble gets Lost

         if(marble.image.collidesWith(monster.image, true))
        {
            //monster dies
            monster.velocity.copy(new Point(0,0,0));
            monster.timeHit = System.currentTimeMillis();
            monster.active= false;
        }
        CollisionDetails cd = detectWallCollision(testMarble);
        if(cd.collisionOccurs)
        {
            advancedAfterCollision= false;
            marble.velocity.copy(cd.resultingVelocity);
            marble.image.setPosition((int)cd.pointOfCollision.x,
                                    (int)cd.pointOfCollision.y);
            collisionOccurs= cd.collisionOccurs;        
        }
        cd = detectPegCollision(testMarble);
        if(cd.collisionOccurs)
        {
            advancedAfterCollision= false;
            marble.velocity.copy(cd.resultingVelocity);
            marble.image.setPosition((int)cd.pointOfCollision.x,
                                    (int)cd.pointOfCollision.y);
            points +=GameSettings.POINTSPERPEG;
            points +=GameSettings.CONSECUTIVEPEGMODIFIER * pegsTouchedSinceRelease;
            pegsTouchedSinceRelease++;            
            collisionOccurs= cd.collisionOccurs;        
        }
        return !collisionOccurs;
    }
    
    private CollisionDetails detectWallCollision(Marble movingObject)
    {
        float xRadius =movingObject.image.getWidth()/2;
        float yRadius =movingObject.image.getHeight()/2;
        int posX = movingObject.image.getRefPixelX();
        int posY =  movingObject.image.getRefPixelY();
        CollisionDetails collisionDetails = new CollisionDetails();
        collisionDetails.pointOfCollision.copy(new Point(posX,posY,0));
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
        collisionDetails.pointOfCollision.copy( new Point(
                                                    testMarble.image.getRefPixelX(),
                                                    testMarble.image.getRefPixelY(),
                                                    0)
                                                );
        for(int i = 0;i<pegs.length;i++)
        {
            if(pegs[i].active 
               && pegs[i].timeHit ==0
               && pegs[i].image.collidesWith(testMarble.image,true) )
            {
                collisionDetails.collisionOccurs =true;
                Point marbleLocation = new Point (testMarble.image.getRefPixelX(),
                                                    testMarble.image.getRefPixelY(),0);
                Point marbleVelocity = new Point (testMarble.velocity.x,
                                                    testMarble.velocity.y,0);
                Point pegLocation  = new Point (pegs[i].image.getRefPixelX(),
                                                    pegs[i].image.getRefPixelY(),0);
                collisionDetails.resultingVelocity = Collision.getResultingVelocity
                                                (marbleLocation,
                                                 marbleVelocity,
                                                 testMarble.image.getHeight(),
                                                  pegLocation);
                return collisionDetails;
            }
        }
        return collisionDetails;
    }
    
    private void detectMarbleLost()
    {
            if (marbleLost)
            {
                 marble.image.setRefPixelPosition((int)GameSettings.MARBLESTARTPOSITION.x,
                            (int)GameSettings.MARBLESTARTPOSITION.y);
                marble.velocity.copy( new Point(0,0,0));
                marblesUsed++;
                waitForRelease=true;
                marbleLost = false;
                inProgress=false;
                pegsTouchedSinceRelease=0;
                detectEndLevel();
            }
    }    
    
    private void detectEndLevel()
    {
        if(!pegsStillActive())
        {
            inProgress=false;
            levelEnded=true;
        }
    }
    
    private boolean pegsStillActive()
    {
        for(int i=0;i<pegs.length;i++)
        {
            if(pegs[i].active)
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
        {        marble.image.setRefPixelPosition(marble.image.getRefPixelX() +(int)unit,
                                                marble.image.getRefPixelY());
        }
    }

    public void releaseMarble()
    {
        waitForRelease=false;
        inProgress=true;
        pegsTouchedSinceRelease=0;
    }
     
}
