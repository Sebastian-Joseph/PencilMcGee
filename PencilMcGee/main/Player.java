package main;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;


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
    private int point;
    private int invincibility;

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
        point = 0;
        invincibility = 0;
        Timer timer = new Timer();
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

    public void reduceLeadCount(int r) {
        leadCount -= r;
    }

    public int getPointCount() {
        return point;
    }

    public void addPointCount(int a) {
        point -= a;
    }


    public void collision(Tile t, KeyHandler k, int xMax, int yMax, int offset, boolean topRow)  {
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
            } else if (t.getType() % 2 == 1 && xPos + width >= t.getX() + (offset / 2) && xPos <= t.getX() + offset + xSpeed && yPos + height > t.getY() + ySpeed && yPos < t.getY() + offset - ySpeed) {
                xPos = t.getX() + offset + xSpeed;
                dx = xIncrement;

            } else if (t.getType() % 2 == 1 && xPos + width > t.getX() && xPos < t.getX() + offset && yPos + height >= t.getY() && yPos <= t.getY() + (offset / 2)) {
                yPos = t.getY() - height;
                dy = 0;

            } else if (t.getType() % 2 == 1 && xPos + width > t.getX() && xPos < t.getX() + offset && yPos + height >= t.getY() + (offset / 2) && yPos <= t.getY() + offset) {
                yPos = t.getY() + offset;
                k.upPressed = false;
                dy = 0;

            }
            //spikes collision

            //head hits spike
            else if (t.getType() == 6 && xPos + width > t.getX() && xPos < t.getX() + offset && yPos + height >= t.getY() + (offset / 2) && yPos <= t.getY() + offset) {
                yPos = t.getY() + offset;
                k.upPressed = false;
                dy = 0;
                reduceLeadCount(1);
            }
            //standing on spike
            else if (t.getType() == 6 && invincibility == 0 && xPos + width > t.getX() && xPos < t.getX() + offset && yPos + height >= t.getY() && yPos <= t.getY() + (offset / 2)) {
                yPos = t.getY() - height;
                dy = 0;
                for (int i = 0; i == 0; i = 300) {
                    if (i % 2 == 1) {
                        reduceLeadCount(1);
                    }
                }

            }

            //coin up
            else if (t.getType() == 4 && xPos + width > t.getX() && xPos < t.getX() + offset && yPos + height >= t.getY() + (offset / 2) && yPos <= t.getY() + offset) {

            }
        }


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



    public void move(KeyHandler k, int xMax, Tilemap tm, ArrayList<Enemy> al) {
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
            for (Enemy e : al) {
                e.scroll((int) dx);
            }
        }
        if (xPos >= xMax / 2 && dx >= 0) xPos = xMax / 2;

        else xPos += dx;
        yPos += dy;
    }
}