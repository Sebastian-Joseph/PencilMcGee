package main;

public class Enemy {
    private double xStart;
    private double yStart;
    private double xEnd;
    private double yEnd;

    private double x;
    private double y;
    private double heightAndWidth;

    private double speed;
    private int damage;
    private boolean disapppearAtEnd;

    public Enemy(double x1, double y1, double x2, double y2, double tileSize, double s, int d, boolean dae) {
        xStart = x1;
        yStart = y1;
        xEnd = x2;
        yEnd = y2;

        x = xStart;
        y = yStart;
        heightAndWidth = tileSize;

        speed = s;
        damage = d;
        disapppearAtEnd = dae;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getHeightAndWidth() {
        return heightAndWidth;
    }

    public int getDamage() {
        return damage;
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

            return disapppearAtEnd;
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
}
