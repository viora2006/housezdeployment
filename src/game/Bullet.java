package game;

import java.awt.*;

public class Bullet {
    double x;
    double y;
    double dx;
    double dy;
    

    int size = 10;
    int speed = 20;

    public Bullet(double startX, double startY, double targetX, double targetY){
        x  = startX;
        y = startY;


        double angle = Math.atan2(targetY - startY, targetX - startX);

        dx = Math.cos(angle) * speed;
        dy = Math.sin(angle) * speed;

       


    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }

    public void update(){
        x+= dx;
        y+= dy;
    }
    public int getSize(){
        return size;
    }

    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        // type cast due to doubles not being accepted
        g.fillOval((int)x, (int)y, size, size);
    }




}
