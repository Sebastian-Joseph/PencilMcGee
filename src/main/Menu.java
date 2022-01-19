package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class Menu {

    private int width;
    private int height;
    private int tileSize;

    public Rectangle playButton;
    public Rectangle instructButton;
    public Rectangle quitButton;

    public Menu(int w, int h, int t) {
        width = w;
        height = h;
        tileSize = t;

        playButton = new Rectangle((width / 2) - tileSize, 13 * tileSize + tileSize / 2, 3 * tileSize, (tileSize * 5) / 3);
        instructButton = new Rectangle((width / 2) - tileSize * 3, 17 * tileSize + tileSize / 2, 7 * tileSize, (tileSize * 5) / 3);
        quitButton = new Rectangle((width / 2) - tileSize, 21 * tileSize + tileSize / 2, 3 * tileSize, (tileSize * 5) / 3);
    }

    public void render(Graphics g) throws IOException {

        Graphics2D g2d = (Graphics2D) g;
        BufferedImage background;
        background = ImageIO.read(getClass().getResourceAsStream("images/notebook.png"));
        g.drawImage(background, 0, 0, width, height, null);
        Font font = new Font("Ink Free", Font.BOLD, tileSize * 2);
        g.setFont(font);
        g.setColor(Color.white);

        Font font1 = new Font("Ink Free", Font.BOLD, tileSize);
        g.setFont(font1);
        g.drawString("Play", playButton.x + (tileSize * 2) / 3, playButton.y + tileSize);
        g2d.draw(playButton);
        g.drawString("Exit", quitButton.x + (tileSize * 2) / 3, quitButton.y + tileSize);
        g2d.draw(quitButton);
        g.drawString("Instructions", instructButton.x + (tileSize / 2), instructButton.y + tileSize);
        g2d.draw(instructButton);

    }


}