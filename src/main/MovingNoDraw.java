package main;

public class MovingNoDraw extends Enemy {

    public MovingNoDraw(double x1, double y1, double x2, double y2, double tileSize, double s, int d, boolean dae) {
        super(x1, y1, x2, y2, tileSize, s, d, dae);
    }
    
    public void overWriteTile(Tile t) {
        if ((t.getType() == 0 || t.getType() == 3) && x + heightAndWidth > t.getX() && x < t.getX() + t.getSize() && y + heightAndWidth > t.getY() && y < t.getY() + t.getSize()) {
            t.revert();
            t.convertToNoDraw();
        }
    }
}
