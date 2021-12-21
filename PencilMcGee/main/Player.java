package main;

public class Player {
    private double xPos;
    private double yPos;
    private int xInit = 100;
    private int yInit = 100;

    private double xSpeed;
    private double ySpeed;
    private double dx;
    private double dy;

    private double xIncrement;
    private double yIncrement;

    private int width;
    private int height;

    private int leadCount;
    private int pointCount;

    Music music = new Music();

    public Player(int w, int h) {
        xPos = xInit;
        yPos = yInit;
        xSpeed = 6;
        ySpeed = 16;
        xIncrement = 0.9;
        yIncrement = 0.6;
        dx = 0;
        dy = 0;

        width = w;
        height = h;

        leadCount = 100;
        pointCount = 0;
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

    public int getPointCount() {
        return pointCount;
    }

    public void addPointCount(int h) {
        pointCount += h;
    }
    public void reduceLeadCount(int r) {
        leadCount -= r;
    }

    public void collision(Tile t, KeyHandler k, int xMax, int yMax, int offset) {
        if (t.getType() == 1 && xPos + width >= t.getX() - xSpeed && xPos <= t.getX() + (offset / 2) && yPos + height > t.getY() + ySpeed && yPos < t.getY() + offset - ySpeed) {
            xPos = t.getX() - width - xSpeed;
            dx = 0;
        }
        else if (t.getType() == 1 && xPos + width >= t.getX() + (offset / 2) && xPos <= t.getX() + offset + xSpeed && yPos + height > t.getY() + ySpeed && yPos < t.getY() + offset - ySpeed) {
            xPos = t.getX() + offset + xSpeed;
            dx = 0;
        }
        else if (t.getType() == 1 && xPos + width > t.getX() && xPos < t.getX() + offset && yPos + height >= t.getY() && yPos <= t.getY() + (offset / 2)) {
            yPos = t.getY() - height;
            dy = 0;
        }
        else if (t.getType() == 1 && xPos + width > t.getX() && xPos < t.getX() + offset && yPos + height >= t.getY() + (offset / 2) && yPos <= t.getY() + offset) {
            yPos = t.getY() + offset;
            k.upPressed = false;
            dy = 0;
        }
        else if (t.getType() == 2)

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

    public void playMusic(int i) {
        music.setFile(i);
        music.play();


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
                    //playMusic(3);
        }
        else {
            dy = (dy == 0)
                    ? yIncrement
                    : (dy < ySpeed) ? dy + yIncrement : ySpeed;
        }

        if (k.rightPressed && xPos >= xMax / 2) {
            for (int i = 0; i < tm.getMap().length; i++) {
                for (int j = 0; j < tm.getMap()[i].length; j++) {
                   tm.getMap()[i][j].scroll(xSpeed);
                }
            }
        }
        if (xPos >= xMax / 2 && !k.leftPressed && dx >= 0) xPos = xMax / 2;

        else xPos += dx;
        yPos += dy;
    }

    public void reset() {
        xPos = xInit;
        yPos = yInit;
        leadCount = 100;
        pointCount = 0;
    }
}