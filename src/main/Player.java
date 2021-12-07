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
        xSpeed = 0.1;
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

        // if (keyHandler.leftPressed) {
        //     dx = (dx > -1 * xSpeed) ? dx - increment : -1 * xSpeed;
        // }
        // else if (keyHandler.rightPressed) {
        //     dx = (dx < xSpeed) ? dx + increment : xSpeed;
        // }
        // else {
        //     dx = (dx > increment)
        //     ? dx - increment
        //     : (dx < -1 * increment) ? dx + increment : 0;
        // }
    
        // if (keyHandler.upPressed) {
        //     dy = (dy == 0)
        //     ? -1 * ySpeed + increment
        //     : (dy < ySpeed) ? dy + increment : ySpeed;
        // }
        // else {
        //     dy = (dy == 0)
        //     ? increment
        //     : (dy < 8) ? dy + increment : 8;
        // }

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

        // xPos += dx;
        // yPos += (yPos >= 32 * 26) ? 0 : dy;
    }
}
