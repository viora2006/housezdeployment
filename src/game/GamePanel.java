package game; // says it belongs withi the game folder

// timer updates naturally by mouse movement as well as other natural means
// so for menu purposes no need to implement new timers unless





import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.RescaleOp;

public class GamePanel extends JPanel {
int x = 100;
int y = 100;
int wallWidth = 75;
int wallHeight = 54;
int zombieskilled = 0; // reset
double menuPointerX;
double menuPointerY;
Font font = new Font("Arial", Font.BOLD, 60);
double forward = 20 ;// toward gun direction
double side = 10;    // right side of the player/gun
double playerAngle;
BufferedImage woodFloor;
BufferedImage window;
BufferedImage playerSprite;
BufferedImage walls;
BufferedImage packOfTrees;
BufferedImage grass;
BufferedImage phase1Fire;
BufferedImage phase2Fire;
BufferedImage phase3Fire;
BufferedImage phase4Fire;
BufferedImage phase5Fire;
BufferedImage gameOverBackground;
int phaseOfCycle = 0;
int playerSize = 30; // drawn however the character is drawn as 50 by 50, therefore movements are prohbited by such coordinates, this was done for hitbox purposes
int playerHealth = 100; // 4 zombie hits = death //reset
int waveTimer = 0; //
long lastHitTime = 0;   // for invincibiity frames //reset
ArrayList<Bullet> bullets = new ArrayList<>();
ArrayList<Zombie> zombies = new ArrayList<>();
ArrayList<Obstacle> obstacles = new ArrayList<>();
ArrayList<BufferedImage> fireCycle = new ArrayList<>();
Timer timer;
GamePanel panel = this;
Timer zombieSpawner;
Timer fireTimer; // for fire animation
boolean roundOver = false; //reset
boolean gameOver = false; //reset
int maxZombies = 5; // incrementing each round by 1 //reset to 5
int numberOfZombiesToEndRound = 20; //increments by 10 // reset
int numberOfZombiesSpawned = 0;
int menuXClick;
int menuYClick;
int playAgainWidth ;
int playAgainHeight;
int exitTextWidth;
int exitTextHeight;




public int getPlayerX(){
    return x;
}

public int getPlayerY(){
    return y;
}


public GamePanel(){
    
    setPreferredSize(new Dimension(1920, 1080));
    setFocusable(true);
    requestFocusInWindow();
    panel = this;                       //playerRight.png
    try {playerSprite = ImageIO.read(getClass().getResource("/resources/playerRight.png")) ;}

    catch (Exception e){
        e.printStackTrace();
    }
    wallSprite();
    windowSprite();
    woodFloor();
    packOfTreesSprite();
    grass();
    phasesOfFire();
    gameOverBackground();

    fireCycle.add(phase1Fire);
    fireCycle.add(phase2Fire);
    fireCycle.add(phase3Fire);
    fireCycle.add(phase4Fire);
    fireCycle.add(phase5Fire);

    obstacles.add(new Obstacle(0, 0, 800, 50));     // top wall

    obstacles.add(new Obstacle(0, 550, 800, 50));   // bottom wall

    obstacles.add(new Obstacle(0, 0, 50, 600));     // left wall

    obstacles.add(new Obstacle(750, 0, 50, 600)); 



    fireTimer = new Timer(165, e -> {
    phaseOfCycle++;

    if (phaseOfCycle >= fireCycle.size()) {
        phaseOfCycle = 0;
    }

    repaint();
});

fireTimer.start();
     zombieSpawner = new Timer(1000, e-> {
        // zombie spawner with max 5 zombies at a time at start
          if (zombies.size()<maxZombies && numberOfZombiesSpawned < numberOfZombiesToEndRound) {
        zombies.add(new Zombie(x, y, panel));
        zombieSpawnSound();
        numberOfZombiesSpawned++;

        }       
        if (zombieskilled >= numberOfZombiesToEndRound){
            zombieSpawner.stop();
            roundOver = true;
            waveTimer = 0;
            roundOverSound();
           
         // a one repeated timer to stall for RoundOver
        Timer pauseTimer = new Timer(10000, e2-> {
        maxZombies++;  
        zombieSpawner.start();
        roundOver = false;
        playerHealth = 100;  // heal player
        numberOfZombiesSpawned = 0;
        zombieskilled = 0;
        numberOfZombiesToEndRound +=10;
           });
           pauseTimer.setRepeats(false);
           pauseTimer.start();
        }
        waveTimer++;
    });
    // come back and check this code!!!!
    
    zombieSpawner.start();






    
    timer = new Timer(16, e->{
    
    for (Zombie zombie : zombies) {
        // parameters, targetX and targetY
        zombie.update(x, y);
    }
    checkIfPlayerHasBeenHit();
    



    for (int i = 0; i < bullets.size(); i++){
    
    boolean bulletRemoved = false;
    Bullet bullet = bullets.get(i);
    
    bullet.update();



     for (int j = 0; j < zombies.size(); j++) {

        Zombie zombie = zombies.get(j);
       
        if (bullet.getX() < zombie.getX() + zombie.getSize() &&
            bullet.getX() + bullet.getSize() > zombie.getX() &&
            bullet.getY() < zombie.getY() + zombie.getSize() &&
            bullet.getY() + bullet.getSize() > zombie.getY()) {
        bullets.remove(i);
        i--;
         fleshSound();
        zombie.decreaseHP();
        if (zombie.isDead()){
            zombies.remove(j);
            j--;
            zombieskilled++;
        }
        bulletRemoved = true;
        break;
        
    }
}
    if (bulletRemoved){
        continue;
    }


    if (bullet.getX() <= 50 ||
        bullet.getX() >= 750 ||
        bullet.getY() >= 550 ||
        bullet.getY() <= 50){

        bullets.remove(i);
        bulletHitWoodSound();
        i--; // for arrayList shift naturally
    }
}
        repaint();

    
    });

    timer.start();
   

    addKeyListener(new KeyAdapter() {
    
    public void keyPressed(KeyEvent e){
        // player x cord is left side and y cord is top side 
        int nextX = x;
        int nextY = y;
        if (e.getKeyCode() == KeyEvent.VK_W ) nextY -= 20;
        if (e.getKeyCode() == KeyEvent.VK_S ) nextY += 20;
        if (e.getKeyCode() == KeyEvent.VK_A ) nextX -= 20;
        if (e.getKeyCode() == KeyEvent.VK_D ) nextX += 20;
       
       boolean collision = false;

        for (Obstacle obstacle : obstacles) {

    if (nextX < obstacle.x + obstacle.width &&
        nextX + 50 > obstacle.x &&
        nextY < obstacle.y + obstacle.height &&
        nextY + 50 > obstacle.y) {

        collision = true;
        break;
    }
}
    if (!collision){
        x = nextX;
        y = nextY;
    }
       
        repaint();
    
    }
    
    }
    );

addMouseListener(new MouseAdapter() {
    
    @Override
    public void mousePressed(MouseEvent e){
        if (!gameOver){
    double centerX = x + 25;
    double centerY = y + 25;

   double bulletStartX =
    centerX
    + Math.cos(playerAngle) * forward
    + Math.cos(playerAngle + Math.PI / 2) * side;

    double bulletStartY =
    centerY
    + Math.sin(playerAngle) * forward
    + Math.sin(playerAngle + Math.PI / 2) * side;
   
    bullets.add(new Bullet(
    bulletStartX,
    bulletStartY,
    e.getX(),
    e.getY()
));
    bulletSound();

      }
    
    else {
        menuXClick = e.getX();
        menuYClick = e.getY();


        // Reset Game
        if ((int)menuXClick>=500 && (int)menuXClick<= playAgainWidth + 500 && (int)menuYClick>= 500 - playAgainHeight && (int)menuYClick<=500 ){
                x = 100; //reset player coordinates to spawn
                y = 100; 
                zombieskilled = 0;
                maxZombies = 5;
                numberOfZombiesToEndRound = 20;
                playerAngle =0;
                playerHealth = 100;
                menuXClick = 0;
                menuYClick = 0;
                phaseOfCycle = 0;
                roundOver = false;
                gameOver = false;
                lastHitTime = 0;
                numberOfZombiesSpawned = 0;
                zombies.clear();
                bullets.clear();
                turnOnTimers();
                repaint();
                requestFocusInWindow(); //give ownership of keyboard back
        }
        // leave game
        else if((int)menuXClick >= 1000 && (int)menuXClick<= exitTextWidth + 1000 && (int)menuYClick >= 500 - exitTextHeight && (int)menuYClick<= 500){
            System.exit(0);
        }
            
    }


    }
});


addMouseMotionListener(new MouseMotionAdapter() {
    @Override
    public void mouseMoved(MouseEvent e) {

        double dx = e.getX() - (x + 25);
        double dy = e.getY() - (y + 25);

        if (!gameOver){
        playerAngle = Math.atan2(dy, dx);
        }
        else {
            menuPointerX = e.getX();
            menuPointerY = e.getY();
        }
        
        repaint();
    }
});



}
// end Of GamePanel instance











// ALL HELPER METHODS BELOW
//===================================================================================================================================================================


@Override
protected void paintComponent(Graphics g){
    super.paintComponent(g);
    if (!gameOver) {
    Graphics2D g2 = (Graphics2D) g;
    AffineTransform old = g2.getTransform();

        // paint grass all over
    for (int row = 0; row < 1080; row += 275) {

    for (int col = 0; col < 1920; col += 276) {

        g.drawImage(grass, col, row, 276, 275, null);

    }
}

        //1000, 300
g.drawImage(packOfTrees, 820, 0, 500, 500, null);

g.drawImage(packOfTrees, 1320, 0, 500, 500, null);

// edit here
g.drawImage(packOfTrees, 1300, 620, 600, 600, null);

g.drawImage(packOfTrees, 1000, 500, 500, 500, null);

g.drawImage(packOfTrees, 0, 620, 500, 460, null);

g.drawImage(packOfTrees, 500, 620, 500, 460, null);

g.drawImage(fireCycle.get(phaseOfCycle), 850, 540, 70, 70, null);


// wall thickness
int wallSize = 50;

// TOP WALL
g.drawImage(walls, 0, 0, 800, wallSize, null);

// BOTTOM WALL
g.drawImage(walls, 0, 550, 800, wallSize, null);

// LEFT WALL
old = g2.getTransform();

g2.rotate(Math.toRadians(-90), 25, 25);
g2.drawImage(walls, -550, 0, 600, wallSize, null);

g2.setTransform(old);

// RIGHT WALL 
old = g2.getTransform();

g2.rotate(Math.toRadians(90), 775, 25);
g2.drawImage(walls, 775, 0, 550, wallSize, null);
                     ///775   25
g2.setTransform(old);

// draw window top
g2.drawImage(window, 200, 5, 40, 40, null);

// draw window bottom
g2.rotate(Math.toRadians(180), 200, 595);
g2.drawImage(window, 200, 590, 40, 40, null);
g2.setTransform(old);

for (int row = 50; row < 550; row += 32) {
    for (int col = 50; col < 750; col += 32) {

        g.drawImage(woodFloor, col, row, 32, 32, null);

    }
}





    // draw bullets
    for(Bullet bullet: bullets){
        bullet.draw(g);
    }
    for (Zombie zombie: zombies){
        AffineTransform oldZombie = g2.getTransform();
        g2.rotate(zombie.getAngle(), zombie.getX() + zombie.getSize()/2, zombie.getY() + zombie.getSize()/2);
        g2.drawImage(zombie.getZombieSprite(), (int)zombie.getX(), (int)zombie.getY(), zombie.getSize(), zombie.getSize(), null );
        g2.setTransform(oldZombie);
    }
    // gameOver screen takes priority 
    if (roundOver && !gameOver){
        drawRoundOver(g);
    }
old = g2.getTransform();

g2.rotate(playerAngle, x + 25, y + 25);
g2.drawImage(playerSprite, x, y, 50, 50, null);

g2.setTransform(old);
   
    
    
}
else {
    g.drawImage(gameOverBackground, 0, 0, 1920, 1080, null);
    playerDiedScreen(g);

}


}

public void drawRoundOver(Graphics g) {

    g.setColor(new Color(0, 0, 0, 160));
    g.fillRect(0, 0, getWidth(), getHeight());

    g.setColor(Color.YELLOW);
    g.setFont(new Font("Arial", Font.BOLD, 60));

    g.drawString("ROUND OVER", 190, 250);

    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.PLAIN, 28));

    g.drawString("Next wave incoming...", 230, 320);
}


public void roundOverSound(){
     try { // Audio for roundOver

        AudioInputStream audio =
        AudioSystem.getAudioInputStream(
       getClass().getResource("/resources/RoundOver.wav")
        );

        Clip clip = AudioSystem.getClip();

        clip.open(audio);

        clip.start();

} catch (Exception E) {
    E.printStackTrace();
}
}



public void zombieSpawnSound(){

 try { 

        AudioInputStream audio =
        AudioSystem.getAudioInputStream(
       getClass().getResource("/resources/zombieSound.wav")
        );

        Clip clip = AudioSystem.getClip();

        clip.open(audio);
        FloatControl volume =
        (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        volume.setValue(-15.0f);

        clip.start();

} catch (Exception E) {
    E.printStackTrace();
}

}

public void playerDamageSound(){

 try { // Audio for roundOver

        AudioInputStream audio =
        AudioSystem.getAudioInputStream(
       getClass().getResource("/resources/playerDamageSound.wav")
        );

        Clip clip = AudioSystem.getClip();

        clip.open(audio);

        clip.start();

} catch (Exception E) {
    E.printStackTrace();
}

}



public boolean playerTookDamage(Zombie zombie) {
    if (zombie.getX() < x + playerSize && zombie.getX() + zombie.getSize() > x
       && zombie.getY() < y + playerSize && zombie.getY() + zombie.getSize() > y )   {
        return true;
    }
    return false;
}

public void checkIfPlayerHasBeenHit(){
    long currentTime = System.currentTimeMillis();

    for (int i = 0; i<zombies.size();i++){
        if (playerTookDamage(zombies.get(i))){
            // Invincibility frames 
            if (currentTime - lastHitTime >1000){
            playerDamageSound();
            playerHealth -= 25;
            lastHitTime = currentTime;
            if (playerHealth <=0){
                gameOver = true;
                turnOffTimers();
            } }
        }
    }

}

public void playerDiedScreen(Graphics g){
    g.setFont(font); //initialized at top of program for gameOverScreen
    g.setColor(Color.RED);
    g.setFont(new Font("Arial", Font.BOLD, 60));
    FontMetrics metrics = g.getFontMetrics(font);
    String playAgainText = "Play Again?";
     playAgainWidth = metrics.stringWidth(playAgainText);
     playAgainHeight = metrics.getHeight();

    String exitText = "Exit Game";

    exitTextWidth = metrics.stringWidth(exitText);
    exitTextHeight = metrics.getHeight();



    g.drawString("Game Over!", 750, 350); 
    // hovering over "play again?""
    if ((int)menuPointerX>=500 && (int)menuPointerX<= playAgainWidth + 500 && (int)menuPointerY>= 500 - playAgainHeight && (int)menuPointerY<=500){
        g.drawString(exitText, 1000, 500);
        g.setColor(Color.BLACK);
        g.drawString(playAgainText, 504, 504);
        g.setColor(Color.RED);
        g.drawString(playAgainText, 500, 500);
    }

    else if((int)menuPointerX >= 1000 && (int)menuPointerX<= exitTextWidth + 1000 && (int)menuPointerY >= 500 - exitTextHeight && (int)menuPointerY<= 500){
         g.drawString(playAgainText, 500, 500);
         g.setColor(Color.BLACK);
         g.drawString(exitText, 1004, 504);
         g.setColor(Color.RED);
         g.drawString(exitText, 1000, 500);
    }

    else {
    g.drawString(playAgainText, 500, 500);
    g.drawString(exitText, 1000, 500);
    }
}

public void turnOffTimers(){
    zombieSpawner.stop();
    timer.stop();
    fireTimer.stop();
    
   
}

public void turnOnTimers(){
    zombieSpawner.start();
    timer.start();
    fireTimer.start();
    
}


public void bulletSound(){
    try {

    AudioInputStream audio =
        AudioSystem.getAudioInputStream(
            getClass().getResource("/resources/pistolSoundUpdated.wav")
        );

    Clip clip = AudioSystem.getClip();

    clip.open(audio);

    clip.start();

} catch (Exception E) {
    E.printStackTrace();
}

}

public void wallSprite(){
   try{ walls = ImageIO.read(getClass().getResource("/resources/newSmallWalls.png")); }

    catch (Exception e){
        e.printStackTrace();
    }

}

public void windowSprite(){
   try{ window = ImageIO.read(getClass().getResource("/resources/window.png")); }

    catch (Exception e){
        e.printStackTrace();
    }

}

public void woodFloor(){
   try{ woodFloor = ImageIO.read(getClass().getResource("/resources/WoodFloor.png")); 
    RescaleOp op = new RescaleOp(
            0.8f, // darker
            0,
            null
        );

        woodFloor = op.filter(woodFloor, null);
    }
    catch (Exception e){
        e.printStackTrace();
    }

}

public void bulletHitWoodSound(){
     try { // Audio for roundOver

        AudioInputStream audio =
        AudioSystem.getAudioInputStream(
        getClass().getResource("/resources/bulletHittingWood.wav")
        );

        Clip clip = AudioSystem.getClip();

        clip.open(audio);

        clip.start();

} catch (Exception E) {
    E.printStackTrace();
}
}

public void fleshSound(){
     try { // Audio for roundOver

        AudioInputStream audio =
        AudioSystem.getAudioInputStream(
       getClass().getResource("/resources/fleshSound.wav")
        );

        Clip clip = AudioSystem.getClip();

        clip.open(audio);

        clip.start();

} catch (Exception E) {
    E.printStackTrace();
}
}


public void packOfTreesSprite(){
   try{ packOfTrees = ImageIO.read(getClass().getResource("/resources/outsidePackOfTrees.png")); }

    catch (Exception e){
        e.printStackTrace();
    }

}


public void grass(){
   try{ grass = ImageIO.read(getClass().getResource("/resources/gloomyGrass.png")); }

    catch (Exception e){
        e.printStackTrace();
    }

}

public void phasesOfFire(){
   try{ phase1Fire = ImageIO.read(getClass().getResource("/resources/phase1FireTrial.png")); 
    phase2Fire = ImageIO.read(getClass().getResource("/resources/phase2trial.png"));
     phase3Fire = ImageIO.read(getClass().getResource("/resources/phase3trial.png"));
      phase4Fire = ImageIO.read(getClass().getResource("/resources/phase4trial.png"));
       phase5Fire = ImageIO.read(getClass().getResource("/resources/phase5trial.png"));



   }

    catch (Exception e){
        e.printStackTrace();
    }

}

public void gameOverBackground(){
   try{ gameOverBackground = ImageIO.read(getClass().getResource("/resources/gameOverBackGround.png")); }

    catch (Exception e){
        e.printStackTrace();
    }

}











    
}


