package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class Menu {

    public Rectangle playButton = new Rectangle(GamePanel.WIDTH + 700, 150, 100, 50);
    public Rectangle quitButton = new Rectangle(GamePanel.WIDTH + 700, 350, 100, 50);


    public void render(Graphics g) throws IOException {

        Graphics2D g2d = (Graphics2D) g;
        BufferedImage background;
        background = ImageIO.read(getClass().getResourceAsStream("images/pooper3.5.png"));
        g.drawImage(background, 0, 0, null);
        Font font = new Font("arial", Font.BOLD, 50);
        g.setFont(font);
        g.setColor(Color.black);
        g.drawString("Pencil McGee", GamePanel.WIDTH + 600, 100);


        Font font1 = new Font("arial", Font.BOLD, 30);
        g.setFont(font1);
        g.drawString("Play", playButton.x + 19, playButton.y + 30);
        g2d.draw(playButton);
        g.drawString("Exit", quitButton.x + 19, quitButton.y + 30);
        g2d.draw(quitButton);
    }


}