package game;

import java.awt.*;
import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.geom.AffineTransform;


public class Zombie {
    BufferedImage zombieSprite;
    GamePanel panel;
    Random rand = new Random();
    double x;
    double y;
    double dx;
    double dy;
    int health;
    int speed = 3;
    int size = 50;
    double angle;

   

    public Zombie(double targetX, double targetY, GamePanel panel){
    health = 100;
    this.panel = panel;
    Point point = randomSpawn();
     x = point.getX();
     y = point.getY();
    double startX = x;
    double startY = y;

     angle = Math.atan2(targetY - startY, targetX - startX);

        dx = Math.cos(angle) * speed;
        dy = Math.sin(angle) * speed;
       readZombieSprite();

    }

    
    
    
    
    public void update(double targetX, double targetY){
        x +=dx;
        y +=dy;
       
        angle = Math.atan2(targetY - y, targetX - x);

        dx = Math.cos(angle) * speed;
        dy = Math.sin(angle) * speed;



       
    }

    public int getSize(){
        return size;
    }

    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    public int getHP(){
        return health;
    }

    public void decreaseHP(){
        health= health - 25;
    }

    public boolean isDead(){
        return health <=0;
    }
    public double getAngle(){
        return angle;
    }
    public BufferedImage getZombieSprite(){
        return zombieSprite;
    }
    


public void draw(Graphics g){
        g.setColor(Color.GREEN);
        // type cast due to doubles not being accepted
        g.fillOval((int)x, (int)y, size, size);
    }

public Point randomSpawn(){
    // function excludes upperbound
    int spawnX;
    int spawnY;
   while (true){
     spawnX = rand.nextInt(651) + 50;
     spawnY= rand.nextInt(451) + 50;
     double distance = Math.sqrt(Math.pow(spawnX - panel.getPlayerX(), 2) 
     + Math.pow(spawnY - panel.getPlayerY(),2));

     if (distance>140){ // help here
        return new Point(spawnX, spawnY);
     }
     
   }
    

}

public void readZombieSprite(){
    try {
        zombieSprite = ImageIO.read( getClass().getResource("/resources/zombieSprite.png"));
    }
    catch (Exception e) {
        e.printStackTrace();
    }

}










}
