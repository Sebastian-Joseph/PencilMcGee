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
    
    public int width;
    public int height;

    public Player(int x, int y, int w, int h) {
        xPos = x;
        yPos = y;
        xSpeed = 8;
        ySpeed = 16;
        xIncrement = 1.2;
        yIncrement = 0.6;
        dx = 0;
        dy = 0;

        width = w;
        height = h;
    }

    public double getXPos() {
        return xPos;
    }

    public double getYPos() {
        return yPos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void collision(int xMax, int yMax) {
        if (xPos >= xMax - width) {
            dx = 0;
            xPos = xMax - width;
        }
        if (xPos <= 0) {
            dx = 0;
            xPos = 0;
        }

        if (yPos >= yMax - height) {
            dy = 0;
            yPos = yMax - height;
        }
    }

    public void move(KeyHandler k) {
        if (yPos >= 32 * 25) {
            dy = 0;
            yPos = 32 * 25;
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

        xPos += dx;
        yPos += dy;
    }
}
