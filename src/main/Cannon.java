package main;

public class Cannon {
    private int x;
    private int y;
    
    private int interval;
    private int count;
    private Enemy spawn;
    private int direction;

    public Cannon(int xValue, int yValue, int i, int d, double tileSize, double speed, int damage) {
        x = xValue;
        y = yValue;
        interval = i; // Frames
        count = 0;
        direction = d; // 0 up, 1 right, 2 down, 3 left
        
        if (direction == 0) {
            spawn = new Enemy(x, y, x, tileSize * -1, tileSize, speed, damage, true);
        }
        else if (direction == 1) {
            spawn = new Enemy(x, y, tileSize * 96, y, tileSize, speed, damage, true);
        }
        else if (direction == 2) {
            spawn = new Enemy(x, y, x, tileSize * 28, tileSize, speed, damage, true);
        }
        else if (direction == 3) {
            spawn = new Enemy(x, y, tileSize * -1, y, tileSize, speed, damage, true);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Enemy getSpawn() {
        return spawn;
    }

    public int getCount() {
        return count;
    }

    public void scroll(double scrollAmount) {
        x -= scrollAmount;
        spawn.scroll(scrollAmount);
    }

    public boolean createSpawn() {
        if (count == interval) {
            count = 0;
            return true;
        }
        else {
            count++;
            return false;
        }
    }
}
