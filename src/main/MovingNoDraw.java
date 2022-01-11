package main;

public class MovingNoDraw extends Enemy {

    private double width;
    private double height;

    public MovingNoDraw(double x1, double y1, double x2, double y2, double s, double w, double h) {
        super(x1, y1, x2, y2, 0, s, 0, false);

        width = w;
        height = h;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public void collidesWithTile(Tile t) {
        if (t.getType() == 3 && x + width > t.getX() && x < t.getX() + t.getSize() && y + height > t.getY() && y < t.getY() + t.getSize()) {
            t.revert();
        }
    }
}
