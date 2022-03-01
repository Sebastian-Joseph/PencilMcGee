package main;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Player {
    private double xPos;
    private double yPos;
    private int xInit;
    private int yInit;

    private double xSpeed;
    private double ySpeed;
    private double dx;
    private double dy;

    private double xIncrement;
    private double yIncrement;
    
    private int width;
    private int height;

    private int leadCount;
    private int leadCountInit;

    private int invincibility;

    private int coinCount;
    private int score;

    private int clearDistance;
    private int clearDistanceInit;

    private GameMusic sfx = new GameMusic();


    public Player(int x, int y, int w, int h, int c, int l) {
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

        clearDistance = c;
        clearDistanceInit = c;

        leadCount = l;
        leadCountInit = l;
        invincibility = 0;

        score = 0;
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

    public int getInvincibility() {
        return invincibility;
    }

    public int getClearDistance() {
        return clearDistance;
    }

    public int getClearDistanceInit() {
        return clearDistanceInit;
    }

    public void setNewLeadCount(int l) {
        leadCountInit = l;
        leadCount = leadCountInit;
    }

    public void setNewClearDistance(int c) {
        clearDistanceInit = c;
        clearDistance = clearDistanceInit;
    }

    public int getCoinCount() {
        return coinCount;
    }

    public int getScore() {
        return score;
    }

    public void playSfx(int i) {
        sfx.setFile(i);
        sfx.play();
    }

    public void collision(Tile t, KeyHandler k, int xMax, int yMax, int offset, boolean topRow, int damage) {
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
                if (t.getType() == 7 && invincibility == 0) {
                    reduceLeadCount(damage);
                    invincibility = 1;
                }
            }
            else if (t.getType() % 2 == 1 && xPos + width >= t.getX() + (offset / 2) && xPos <= t.getX() + offset + xSpeed && yPos + height > t.getY() + ySpeed && yPos < t.getY() + offset - ySpeed) {
                xPos = t.getX() + offset + xSpeed;
                dx = xIncrement;
                if (t.getType() == 11 && invincibility == 0) {
                    reduceLeadCount(damage);
                    invincibility = 1;
                }
            }
            else if (t.getType() % 2 == 1 && xPos + width > t.getX() && xPos < t.getX() + offset && yPos + height >= t.getY() && yPos <= t.getY() + (offset / 2)) {
                yPos = t.getY() - height;
                dy = 0;
                if (t.getType() == 5 && invincibility == 0) {
                    reduceLeadCount(damage);
                    invincibility = 1;
                }
            }
            else if (t.getType() == 4 && xPos + width > t.getX() && xPos < t.getX() + offset && yPos + height >= t.getY() && yPos <= t.getY() + (offset / 2)) {
                t.revert();
                addCoinCount(1);
                addLeadCount(2);
                playSfx(2);
            }
            else if (t.getType() % 2 == 1 && xPos + width > t.getX() && xPos < t.getX() + offset && yPos + height >= t.getY() + (offset / 2) && yPos <= t.getY() + offset) {
                yPos = t.getY() + offset;
                k.upPressed = false;
                dy = 0;
                if (t.getType() == 9 && invincibility == 0) {
                    reduceLeadCount(damage);
                    invincibility = 1;
                }
            }
        }

        if (xPos <= 0) {
            dx = 0;
            xPos = 0;
        }

        if (yPos >= yMax) {
            leadCount = 0;
        }
        
        // Testing purposes only
        // if (yPos + height > yMax) {
        //     yPos = yMax - height;
        //     dy = 0;
        // }
    }

    public void enemyCollision(Enemy e) {
        if (invincibility == 0 && xPos + width > e.getX() && xPos < e.getX() + e.getHeightAndWidth() && yPos + height > e.getY() && yPos < e.getY() + e.getHeightAndWidth()) {
            reduceLeadCount(e.getDamage());
            invincibility = 1;
        }
        else if (invincibility == 1) {
            iFrames();
        }
    }

    private void iFrames() {
        new Thread(() -> {
            invincibility = 2;
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            invincibility = 0;
        }).start();
    }

    public void reset(double xInit, double yInit) {
        xPos = xInit;
        yPos = yInit;
        leadCount = leadCountInit;
        coinCount = 0;
        invincibility = 0;
        clearDistance = clearDistanceInit;
    }

    public void reduceLeadCount(int r) {
        leadCount -= r;
    }
    public void addLeadCount(int r) {
        leadCount += r;
    }
    public void addCoinCount(int p) {
        coinCount += p;
    }

    public void tallyScore(int tileSize, boolean levelCleared, int level) {
        int coinsScore = getCoinCount() * 10;
        int leadScore = getLeadCount() * 3;
        int distanceScore;
        int clearBonus = 0;
        if (levelCleared) {
            distanceScore = (clearDistanceInit / tileSize) + 2;
            clearBonus = 500 * level;
        }
        else {
            distanceScore = (clearDistanceInit / tileSize) - (clearDistance / tileSize) - ((int) xPos);
        }

        score += coinsScore + leadScore + distanceScore + clearBonus;
    }

    public void resetScore() {
        score = 0;
    }

    public void move(KeyHandler k, int xMax, Tilemap tm, ArrayList<Enemy> al, Cannon[] cl, Enemy[] ml) {
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

            if (dy == -1 * ySpeed + yIncrement) playSfx(1);
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
            for (Cannon c : cl) {
                c.scroll((int) dx);
            }
            for (Enemy m : ml) {
                m.scroll((int) dx);
            }
            clearDistance -= ((int) dx);
        }
        if (xPos >= xMax / 2 && dx >= 0) xPos = xMax / 2;

        else xPos += dx;
        yPos += dy;
    }

}
