package main;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.io.IOException;
import java.lang.Exception;

import javax.imageio.ImageIO;

public class GamePanel extends JPanel implements Runnable {
    // Screen Settings
    final int originalTileSize = 8;
    final int scale = 3;

    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 48;
    final int maxScreenRow = 27;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;
    BufferedImage background;
    BufferedImage player;
    private Tilemap tiles = new Tilemap(screenWidth, screenHeight);

    int FPS = 60;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    Player p1 = new Player(100, 100, tileSize / 2, tileSize * 2);

    public GamePanel() throws IOException {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.decode("#f7f7f7"));
        tiles.createMap("images/amogus.png");
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        background = ImageIO.read(getClass().getResourceAsStream("images/pooper3.5.png"));
        player = ImageIO.read(getClass().getResourceAsStream("images/mcgee.png"));
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {

            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        p1.collision(screenWidth, screenHeight);
        p1.move(keyHandler);
    }

    

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(background, 0, 0, null);
        tiles.draw(g2, p1.getXPos(), p1.getYPos());
        g2.setColor(Color.black);
        g2.drawImage(player, 500, (int) p1.getYPos(), null);
        //g.drawImage(image, 100, 100, null);
        g2.dispose();
    }
}
