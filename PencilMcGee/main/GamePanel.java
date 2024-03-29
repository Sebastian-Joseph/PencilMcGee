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

    private int gameState;
    private final int menuState = 0;
    private final int playState = 1;
    private final int pauseState = 2;

    int FPS = 60;
    Music music = new Music();
    Menu menu = new Menu();
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    Player p1 = new Player(tileSize / 2, tileSize * 2, tileSize / 2, tileSize * 2);


   /* private enum STATE {
        MENU,
        GAME
    } */



    public GamePanel() throws IOException {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.decode("#f7f7f7"));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        tiles.createMap("images/amogus2.png");

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

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void setupGame()  {
        gameState = menuState;
        playMusic(3);
    }

    public void startGameThread() throws IOException {
        gameThread = new Thread(this);
        gameThread.start();


    }


    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        setupGame();
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

        if (gameState == menuState) {
            try {
                menu.render(g);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(mouseDown == true) {
                Point point = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(point, this);
                if (menu.playButton.contains(point)) {
                    gameState = playState;
                    music.stop();
                    playMusic(0);
                }
            }
            if(mouseDown == true) {
                Point point = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(point, this);
                if (menu.quitButton.contains(point)) {
                    System.exit(0);
                }
            }
        }

        else {
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

            for (int i = 0; i < tiles.getMap().length; i++) {
                for (int j = 0; j < tiles.getMap()[i].length; j++) {
                    g2.drawImage(tiles.getMap()[i][j].getImage(), tiles.getMap()[i][j].getX(), tiles.getMap()[i][j].getY(), null);
                }
            }

            g2.drawImage(player, (int) p1.getXPos(), (int) p1.getYPos(), null);

            g2.dispose();


        }
        }

        // g2.drawImage(background, 0, 0, null);




}