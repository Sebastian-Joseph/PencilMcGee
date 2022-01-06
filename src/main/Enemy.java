package main;

public class Enemy {
    private double xStart;
    private double yStart;
    private double xEnd;
    private double yEnd;
    private double xStartInit;
    private double yStartInit;
    private double xEndInit;
    private double yEndInit;

    private double x;
    private double y;
    private double heightAndWidth;

    private double speed;
    private int damage;
    private boolean disappearAtEnd;

    public Enemy(double x1, double y1, double x2, double y2, double tileSize, double s, int d, boolean dae) {
        xStart = x1;
        yStart = y1;
        xEnd = x2;
        yEnd = y2;

        x = xStart;
        y = yStart;

        xStartInit = xStart;
        yStartInit = yStart;
        xEndInit = xEnd;
        yEndInit = yEnd;

        heightAndWidth = tileSize;

        speed = s;
        damage = d;
        disappearAtEnd = dae;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getXEnd() {
        return xEnd;
    }

    public double getYEnd() {
        return yEnd;
    }

    public double getHeightAndWidth() {
        return heightAndWidth;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public boolean getDisappearance() {
        return disappearAtEnd;
    }

    public boolean move() {
        if (Math.sqrt(((x - xEnd) * (x - xEnd)) + ((y - yEnd) * (y - yEnd))) <= heightAndWidth / 8) {
            x = xEnd;
            y = yEnd;
            double placeholderX = xEnd;
            double placeholderY = yEnd;
            xEnd = xStart;
            yEnd = yStart;
            xStart = placeholderX;
            yStart = placeholderY;

            return disappearAtEnd;
        }
        else {
            double xOffset = xEnd - x;
            double yOffset = yEnd - y;
            double hypotenuse = Math.sqrt((xOffset * xOffset) + (yOffset * yOffset));
            x += (xOffset / hypotenuse) * speed;
            y += (yOffset / hypotenuse) * speed;

            return false;
        }
    }

    public void scroll(double scrollAmount) {
        x -= scrollAmount;
        xStart -= scrollAmount;
        xEnd -= scrollAmount;
    }

    public void reset() {
        xStart = xStartInit;
        yStart = yStartInit;
        xEnd = xEndInit;
        yEnd = yEndInit;
        x = xStart;
        y = yStart;
    }

    public void collidesWithTile(Tile t) {
        int temp = 0;
        if (t.getType() == 3 && x + heightAndWidth > t.getX() && x < t.getX() + t.getSize() && y + heightAndWidth > t.getY() && y < t.getY() + t.getSize()) {
            t.revert();
            temp = 3;
        }
        if (disappearAtEnd && (t.getType() % 2 == 1 || temp == 3) && x + heightAndWidth > t.getX() && x < t.getX() + t.getSize() && y + heightAndWidth > t.getY() && y < t.getY() + t.getSize()) {
            xEnd = x;
            yEnd = y;
        }
    }
}
