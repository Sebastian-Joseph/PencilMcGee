package main;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.*;
import java.io.IOException;
import java.lang.Exception;

import javax.imageio.ImageIO;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.MouseInfo;

import java.awt.event.MouseEvent; 
import java.awt.event.MouseListener; 

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

    private Tilemap tiles = new Tilemap(screenWidth, screenHeight, tileSize);
    private boolean mouseDown = false;

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

        tiles.createMap("images/amogus.png");

        background = ImageIO.read(getClass().getResourceAsStream("images/pooper3.5.png"));
        player = ImageIO.read(getClass().getResourceAsStream("images/pencil_mcgee.png"));
        testTile = ImageIO.read(getClass().getResourceAsStream("images/smol_spunch.jpg"));

        addMouseListener(new MouseListener() { 
            public void mouseClicked(MouseEvent e) {} 
            public void mousePressed(MouseEvent e) {
                mouseDown = true;
            } 
            public void mouseReleased(MouseEvent e) {
                mouseDown = false;
            } 
            public void mouseEntered(MouseEvent e) {} 
            public void mouseExited(MouseEvent e) {} 
        } 
        );
    }


    public void startGameThread() throws IOException {
        gameThread = new Thread(this);
        gameThread.start();

        // for (int i = 0; i < tiles.length; i++) {
        //     tileImages[i] = tiles[i].getImage();
        // }
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
        for (int i = 0; i < tiles.getMap().length; i++) {
            for (int j = 0; j < tiles.getMap()[i].length; j++) {
                p1.collision(tiles.getMap()[i][j], keyHandler, screenWidth, screenHeight, tileSize);
            }
        }

        p1.move(keyHandler, screenWidth, tiles);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.black);

        if (mouseDown == true) {
            Point point = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(point, this);

            for (int i = 0; i < tiles.getMap().length; i++) {
                for (int j = 0; j < tiles.getMap()[i].length; j++) {
                    Rectangle tileBounds = new Rectangle(tiles.getMap()[i][j].getX(), tiles.getMap()[i][j].getY(), tileSize, tileSize);

                    if (tileBounds.contains(point)) {
                        Point playerCurrentPosition1 = new Point((int) p1.getXPos(), (int) p1.getYPos());
                        Point playerCurrentPosition2 = new Point((int) p1.getXPos() + p1.getWidth(), (int) p1.getYPos() + p1.getHeight() - 1);
                        Point playerCurrentPosition3 = new Point((int) p1.getXPos() + (p1.getWidth() / 2), (int) p1.getYPos() + (p1.getHeight() / 2));

                        if (
                            (!tileBounds.contains(playerCurrentPosition1) && !tileBounds.contains(playerCurrentPosition2) && !tileBounds.contains(playerCurrentPosition3))
                            || (tileBounds.contains(playerCurrentPosition3) && !tiles.checkTileToLeft(i, j) && !tiles.checkTileToLeft(i + 1, j) && !tiles.checkTileToLeft(i + 2, j) && !tiles.checkTileToLeft(i - 1, j) && !tiles.checkTileToLeft(i - 2, j))
                            || (tileBounds.contains(playerCurrentPosition1) && !tiles.checkTileToLeft(i, j) && !tiles.checkTileToLeft(i + 1, j) && !tiles.checkTileToLeft(i + 2, j))
                            || (tileBounds.contains(playerCurrentPosition2) && !tiles.checkTileToLeft(i, j) && !tiles.checkTileToLeft(i - 1, j) && !tiles.checkTileToLeft(i - 2, j))
                            ) {
                            tiles.getMap()[i][j].change();
                        }
                    }
                }
            }
        }

        // g2.drawImage(background, 0, 0, null);

        for (int i = 0; i < tiles.getMap().length; i++) {
            for (int j = 0; j < tiles.getMap()[i].length; j++) {
                g2.drawImage(tiles.getMap()[i][j].getImage(), tiles.getMap()[i][j].getX(), tiles.getMap()[i][j].getY(), null);
            }
        }
        g2.drawImage(player, (int) p1.getXPos(), (int) p1.getYPos(), null);

        g2.dispose();
    }
}
