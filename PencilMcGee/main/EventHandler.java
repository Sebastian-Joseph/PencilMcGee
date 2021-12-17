package main;

import java.awt.Rectangle;
import java.io.IOException;

public class EventHandler {
    GamePanel gp;

    {
        try {
            gp = new GamePanel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Player player = new Player(gp.tileSize / 2, gp.tileSize * 2);
    Rectangle eventRect;
    int eventRectDefaultX,  eventRectDefaultY;

    public EventHandler() {
        eventRect = new Rectangle();
        eventRect.x = 23;
        eventRect.y = 23;
        eventRect.width = 2;
        eventRect.height = 2;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    public void checkEvent() {

    }
    public boolean hit(int eventCol, int eventRow, String reqDirection) {

        boolean hit = false;
        double playerX = player.getXPos();
        double playerY = player.getYPos();
        eventRect.x = eventCol*gp.tileSize + eventRect.x;
        eventRect.y = eventCol*gp.tileSize + eventRect.y;


        return hit;
    }
}
