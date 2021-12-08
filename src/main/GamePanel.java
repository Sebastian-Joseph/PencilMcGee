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
    final int scale = 4;

    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 48;
    final int maxScreenRow = 27;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;
    BufferedImage background;
    BufferedImage player;
    BufferedImage testTile;

    int FPS = 60;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    Player p1 = new Player(100, 100, tileSize / 2, tileSize * 2);

    public GamePanel() throws IOException {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.decode("#f7f7f7"));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        background = ImageIO.read(getClass().getResourceAsStream("images/pooper3.5.png"));
        player = ImageIO.read(getClass().getResourceAsStream("images/mcgee.png"));
        testTile = ImageIO.read(getClass().getResourceAsStream("images/smol_spunch.jpg"));
    }

    Tile t1 = new Tile("images/smol_spunch.jpg", 1, 35 * 32, screenHeight - 32);
    Tile t2 = new Tile("images/smol_spunch.jpg", 1, 36 * 32, screenHeight - 32);
    Tile t3 = new Tile("images/smol_spunch.jpg", 1, 37 * 32, screenHeight - 32);
    Tile t4 = new Tile("images/imverytired.png", 1, 40 * 32, screenHeight - 128);
    Tile t5 = new Tile("images/imverytired.png", 1, 41 * 32, screenHeight - 128);
    Tile t6 = new Tile("images/imverytired.png", 1, 42 * 32, screenHeight - 128);
    Tile tiles[] = new Tile[6];
    BufferedImage tileImages[] = new BufferedImage[6];

    public void startGameThread() throws IOException {
        gameThread = new Thread(this);
        gameThread.start();

        tiles[0] = t1;
        tiles[1] = t2;
        tiles[2] = t3;
        tiles[3] = t4;
        tiles[4] = t5;
        tiles[5] = t6;

        for (int i = 0; i < tiles.length; i++) {
            tileImages[i] = tiles[i].getImage();
        }
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
        for (Tile t : tiles) {
            p1.collision(t, keyHandler, screenWidth, screenHeight, tileSize);
        }
        p1.move(keyHandler);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.black);

        g2.drawImage(background, 0, 0, null);
        for (int i = 0; i < tiles.length; i++) {
            g2.drawImage(tileImages[i], tiles[i].getx(), tiles[i].gety(), null);
        }
        g2.drawImage(player, (int) p1.getXPos(), (int) p1.getYPos(), null);

        g2.dispose();
    }
}
