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

    private int leadCount;

    public Player(int x, int y, int w, int h) {
        xPos = x;
        yPos = y;
        xSpeed = 6;
        ySpeed = 16;
        xIncrement = 0.8;
        yIncrement = 0.6;
        int size = w * 2;
        int scale = size / 8;
        xIncrement = xIncrement * scale / 4;
        yIncrement = yIncrement * scale / 4;
        xSpeed = xSpeed * scale / 4;
        ySpeed = ySpeed * scale / 4;
        dx = 0;
        dy = 0;

        width = w;
        height = h;

        leadCount = 300;
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

    public int getLeadCount() {
        return leadCount;
    }

    public void collision(Tile t, KeyHandler k, int xMax, int yMax, int offset, boolean topRow) {
        if (topRow && t.getType() == 1) {
            if (xPos + width >= t.getX() - xSpeed && xPos <= t.getX() + (offset / 2) && yPos < t.getY() + offset - ySpeed) {
                xPos = t.getX() - width - xSpeed;
                dx = -1 * xIncrement;
            }
            else if (xPos + width >= t.getX() + (offset / 2) && xPos <= t.getX() + offset + xSpeed && yPos < t.getY() + offset - ySpeed) {
                xPos = t.getX() + offset + xSpeed;
                dx = xIncrement;
            }
            else if (xPos + width > t.getX() && xPos < t.getX() + offset && yPos + height >= t.getY() + (offset / 2) && yPos <= t.getY() + offset) {
                yPos = t.getY() + offset;
                k.upPressed = false;
                dy = 0;
            }
        }
        else {
            if (t.getType() % 2 == 1 && xPos + width >= t.getX() - xSpeed && xPos <= t.getX() + (offset / 2) && yPos + height > t.getY() + ySpeed && yPos < t.getY() + offset - ySpeed) {
                xPos = t.getX() - width - xSpeed;
                dx = -1 * xIncrement;
            }
            else if (t.getType() % 2 == 1 && xPos + width >= t.getX() + (offset / 2) && xPos <= t.getX() + offset + xSpeed && yPos + height > t.getY() + ySpeed && yPos < t.getY() + offset - ySpeed) {
                xPos = t.getX() + offset + xSpeed;
                dx = xIncrement;
            }
            else if (t.getType() % 2 == 1 && xPos + width > t.getX() && xPos < t.getX() + offset && yPos + height >= t.getY() && yPos <= t.getY() + (offset / 2)) {
                yPos = t.getY() - height;
                dy = 0;
            }
            else if (t.getType() % 2 == 1 && xPos + width > t.getX() && xPos < t.getX() + offset && yPos + height >= t.getY() + (offset / 2) && yPos <= t.getY() + offset) {
                yPos = t.getY() + offset;
                k.upPressed = false;
                dy = 0;
            }
        }

        // if (xPos >= xMax - width) {
        //     dx = 0;
        //     xPos = xMax - width;
        // }
        if (xPos <= 0) {
            dx = 0;
            xPos = 0;
        }

        if (yPos >= yMax) {
            leadCount = 0;
        }
    }

    public void reset(double xInit, double yInit) {
        xPos = xInit;
        yPos = yInit;
        leadCount = 300;
    }

    public void reduceLeadCount(int r) {
        leadCount -= r;
    }

    public void move(KeyHandler k, int xMax, Tilemap tm) {
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

        if (xPos >= xMax / 2) {
            for (int i = 0; i < tm.getMap().length; i++) {
                for (int j = 0; j < tm.getMap()[i].length; j++) {
                    tm.getMap()[i][j].scroll((int) dx);
                }
            }
        }
        if (xPos >= xMax / 2 && dx >= 0) xPos = xMax / 2;

        else xPos += dx;
        yPos += dy;
    }
}
