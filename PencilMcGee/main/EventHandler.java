package main;

import java.awt.Rectangle;

public class EventHandler {

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



        return hit;
    }
}
