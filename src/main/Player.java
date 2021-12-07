package main;

public class Player {
    public double xPos;
    public double yPos;
    public double xSpeed;
    public double ySpeed;
    public double dx;
    public double dy;

    public double xIncrement;
    public double yIncrement;
    
    public int height;
    public int width;

    public Player(int x, int y) {
        xPos = x;
        yPos = y;
        xSpeed = 12;
        ySpeed = 24;
        xIncrement = 2.4;
        yIncrement = 1.2;
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
        if (yPos >= 32 * 26) {
            dy = 0;
            yPos = 32 * 26;
        }

        if (k.leftPressed) {
            dx = (dx > -1 * xSpeed) ? dx - xIncrement : -1 * xSpeed;
        }
        else if (k.rightPressed) {
            dx = (dx < xSpeed) ? dx + xIncrement : xSpeed;
        }
        else {
            dx = (dx > xIncrement)
            ? dx - xIncrement
            : (dx < -1 * xIncrement) ? dx + xIncrement : 0;
        }
    
        if (k.upPressed) {
            dy = (dy == 0)
            ? -1 * ySpeed + yIncrement
            : (dy < ySpeed) ? dy + yIncrement : ySpeed;
        }
        else {
            dy = (dy == 0)
            ? yIncrement
            : (dy < ySpeed) ? dy + yIncrement : ySpeed;
        }

        // if (k.leftPressed) {
        //     xPos -= xSpeed;
        // }
        // if (k.rightPressed) {
        //     xPos += xSpeed;
        // }
        // if (k.upPressed) {
        //     yPos -= xSpeed;
        // }
        // if (k.downPressed) {
        //     yPos += xSpeed;
        // }

        System.out.println(dx + " " + dy);

        xPos += dx;
        yPos += dy;
    }
}
