package main;

public class Player {
    public double xPos;
    public double yPos;
    public double xSpeed;
    public double ySpeed;
    public double dx;
    public double dy;

    public double increment;

    public int height;
    public int width;

    public Player(int x, int y) {
        xPos = x;
        yPos = y;
        xSpeed = 3.0;
        ySpeed = 8.0;
        increment = 0.2;
        dx = 0;
        dy = 0;
    }

    public double getXPos() {
        return xPos;
    }

    public double getYPos() {
        return yPos;
    }

    public void move(KeyHandler k) {



        if (k.leftPressed) {
            xPos -= xSpeed;
        }
        if (k.rightPressed) {
            xPos += xSpeed;
        }
        if (k.upPressed) {
            yPos -= xSpeed;
        }
        if (k.downPressed) {
            yPos += xSpeed;
        }



        System.out.println(dx + " " + dy);

        
    }
}