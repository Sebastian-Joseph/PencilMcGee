package main;

public class Enemy {
    private double xStart;
    private double yStart;
    private double xEnd;
    private double yEnd;

    private double x;
    private double y;

    private double speed;
    private boolean disapppearAtEnd;

    public Enemy(double x1, double y1, double x2, double y2, double s, boolean d) {
        xStart = x1;
        yStart = y1;
        xEnd = x2;
        yEnd = y2;

        x = xStart;
        y = yStart;

        speed = s;
        disapppearAtEnd = d;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean move(double tileSize) {
        if (Math.sqrt(((x - xEnd) * (x - xEnd)) + ((y - yEnd) * (y - yEnd))) <= tileSize / 8) {
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
