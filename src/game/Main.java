package game;
import javax.swing.*;
import java.awt.*;


public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame("Top Down Shooter");
        GamePanel panel = new GamePanel();

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    System.out.println(frame.getWidth());
    System.out.println(frame.getHeight());  
    }

}
