package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class Menu {

    public Rectangle playButton = new Rectangle(GamePanel.WIDTH + 750, 420, 100, 50);
    public Rectangle instructButton = new Rectangle(GamePanel.WIDTH + 700, 570, 200, 50);
    public Rectangle quitButton = new Rectangle(GamePanel.WIDTH + 750, 700, 100, 50);


    public void render(Graphics g) throws IOException {

        Graphics2D g2d = (Graphics2D) g;
        BufferedImage background;
        background = ImageIO.read(getClass().getResourceAsStream("images/notebook.png"));
        g.drawImage(background, 0, 0, null);
        Font font = new Font("Ink Free", Font.BOLD, 50);
        g.setFont(font);
        g.setColor(Color.white);
       // g.drawString("Pencil McGee", GamePanel.WIDTH + 600, 100);


        Font font1 = new Font("Ink Free", Font.BOLD, 30);
        g.setFont(font1);
        g.drawString("Play", playButton.x + 17, playButton.y + 29);
        g2d.draw(playButton);
        g.drawString("Exit", quitButton.x + 17, quitButton.y + 29);
        g2d.draw(quitButton);
        g.drawString("Instructions", instructButton.x + 17, instructButton.y + 29);
        g2d.draw(instructButton);

    }


}