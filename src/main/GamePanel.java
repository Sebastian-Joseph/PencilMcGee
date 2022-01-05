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
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.MouseInfo;

import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener; 
import java.awt.event.KeyListener;

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
    BufferedImage leadCountBackground;

    BufferedImage player;
    BufferedImage playerRight;
    BufferedImage playerLeft;

    BufferedImage enemy;

    BufferedImage testTile;

    private Tilemap tiles = new Tilemap(screenWidth, screenHeight, tileSize);
    private boolean mouseDown = false;
    // private boolean enterDown = false;


    private int gameState;
    private final int menuState = 0;
    private final int playState = 1;
    private final int pauseState = 2;

    int FPS = 60;
    GameMusic music = new GameMusic();
    Menu menu = new Menu();
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    Player p1 = new Player(tileSize * 2, screenHeight - tileSize * 8, tileSize / 2, tileSize * 2);

    ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    private final int level1EnemyDamage = 10;

    Enemy[] enemiesInit1 = new Enemy[] {
        new Enemy(tileSize * 81, tileSize * 13, tileSize * 81, tileSize * 4, tileSize, 4, level1EnemyDamage, false),
        new Enemy(tileSize * 96, tileSize * 14, tileSize * 102, tileSize * 14, tileSize, 3, level1EnemyDamage, false),
        new Enemy(tileSize * 202, tileSize * 5, tileSize * 206, tileSize * 17, tileSize * 2, 4, level1EnemyDamage, false),
        new Enemy(tileSize * 228, tileSize * 18, tileSize * 241, tileSize * 16, tileSize * 2, 5, level1EnemyDamage, false),
        new Enemy(tileSize * 250, tileSize * 15, tileSize * 257, tileSize * 15, tileSize, 3, level1EnemyDamage, false),
        new Enemy(tileSize * 270, tileSize * 11, tileSize * 264, tileSize * 11, tileSize, 2, level1EnemyDamage, false),
        new Enemy(tileSize * 293, tileSize * 3, tileSize * 293, tileSize * 11, tileSize, 3, level1EnemyDamage, false),
        new Enemy(tileSize * 293, tileSize * 11, tileSize * 302, tileSize * 11, tileSize, 3, level1EnemyDamage, false),
        new Enemy(tileSize * 327.25, tileSize * 16, tileSize * 327.25, tileSize * 24.5, tileSize * 1.5, 2, level1EnemyDamage, false),
        new Enemy(tileSize * 687, tileSize * 14, tileSize * 704, tileSize * 9, tileSize * 2, 3, level1EnemyDamage, false),
        new Enemy(tileSize * 752, tileSize, tileSize * 752, tileSize * 9, tileSize * 2, 4, level1EnemyDamage, false),
        new Enemy(tileSize * 773, tileSize * 9, tileSize * 773, tileSize, tileSize * 2, 3, level1EnemyDamage, false)

    };

    public GamePanel() throws IOException {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.decode("#f7f7f7"));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        tiles.createMap("images/actual_level.png");

        background = ImageIO.read(getClass().getResourceAsStream("images/pooper3.5.png"));
        leadCountBackground = ImageIO.read(getClass().getResourceAsStream("images/lead_count_background.png"));

        player = ImageIO.read(getClass().getResourceAsStream("images/pencil_mcgee.png"));
        playerRight = ImageIO.read(getClass().getResourceAsStream("images/pencil_mcgee_right.png"));
        playerLeft = ImageIO.read(getClass().getResourceAsStream("images/pencil_mcgee_left.png"));

        enemy = ImageIO.read(getClass().getResourceAsStream("images/eraser.png"));

        testTile = ImageIO.read(getClass().getResourceAsStream("images/smol_spunch.jpg"));

        for (Enemy e : enemiesInit1) {
            enemies.add(e);
        }

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


        // addKeyListener(new KeyListener() { 
        //     public void keyTyped(KeyEvent ke) {} 
        //     public void keyPressed(KeyEvent ke) {
        //         if (ke.getKeyCode() == KeyEvent.VK_ENTER){
        //             enterDown = !enterDown;
        //         }
        //     } 
        //     public void keyReleased(KeyEvent ke) {} 
        // } 
        // );

        
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void setupGame() {
        gameState = menuState;
        playMusic(3);
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
        if (gameState == playState) {
            for (int i = 0; i < tiles.getMap().length; i++) {
                for (int j = 0; j < tiles.getMap()[i].length; j++) {
                    p1.collision(tiles.getMap()[i][j], keyHandler, screenWidth, screenHeight, tileSize, i == 0);
                }
            }

            for (Enemy e : enemies) {
                p1.enemyCollision(e);

                if (e.move()) {
                    e = null;
                }
            }

            if (p1.getLeadCount() <= 0) {
                p1.reset(tileSize * 2, screenHeight - tileSize * 8);
                for (int i = 0; i < tiles.getMap().length; i++) {
                    for (int j = 0; j < tiles.getMap()[i].length; j++) {
                        tiles.getMap()[i][j].reset();
                    }
                }
                for (Enemy e : enemies) {
                    e.reset();
                }
            }

            if (!keyHandler.enterDown) {
                p1.move(keyHandler, screenWidth, tiles, enemies);
            }
        }
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
            if (mouseDown == true) {
                Point point = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(point, this);
                if (menu.playButton.contains(point)) {
                    gameState = playState;
                    music.stop();
                    playMusic(0);
                }
            }
            if (mouseDown == true) {
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
                                if (tiles.getMap()[i][j].change()) {
                                    p1.reduceLeadCount(1);
                                }
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

            g2.setColor(Color.pink);
            for (Enemy e : enemies) {
                g2.drawImage(enemy, (int) e.getX(), (int) e.getY(), (int) e.getHeightAndWidth(), (int) e.getHeightAndWidth(), null);
            }

            g2.setColor(Color.red);
            if (p1.getInvincibility() != 0) {
                g2.fillRect((int) p1.getXPos() - 4, (int) p1.getYPos() - 4, scale * 4 + 8, scale * 16 + 8);
            }

            if (keyHandler.leftPressed) {
                g2.drawImage(playerLeft, (int) p1.getXPos(), (int) p1.getYPos(), scale * 4, scale * 16, null);
            }
            else if (keyHandler.rightPressed) {
                g2.drawImage(playerRight, (int) p1.getXPos(), (int) p1.getYPos(), scale * 4, scale * 16, null);
            }
            else {
                g2.drawImage(player, (int) p1.getXPos(), (int) p1.getYPos(), scale * 4, scale * 16, null);
            }

            g2.drawImage(leadCountBackground, tileSize, tileSize, tileSize * 3, tileSize * 2, null);

            if (p1.getLeadCount() > 30) g2.setColor(Color.black);
            else g2.setColor(Color.red);
            Font font = new Font("Ink Free", Font.BOLD, tileSize);
            g.setFont(font);
            double textOffset = tileSize * 0.6;
            g2.drawString(String.valueOf(p1.getLeadCount()), ((int) textOffset) + tileSize, ((int) textOffset) + tileSize * 2);

            if (keyHandler.enterDown == true) {
                g2.drawImage(testTile, screenWidth / 2 - tileSize, screenHeight / 2 - tileSize, tileSize * 2, tileSize * 2, null);
                if (mouseDown == true) {
                    Point point = MouseInfo.getPointerInfo().getLocation();
                    SwingUtilities.convertPointFromScreen(point, this);
                    Rectangle resetBounds = new Rectangle(screenWidth / 2 - tileSize, screenHeight / 2 - tileSize, tileSize * 2, tileSize * 2);
                    if (resetBounds.contains(point)) {
                        keyHandler.enterDown = false;
                        p1.reset(tileSize * 2, screenHeight - tileSize * 8);
                        for (int i = 0; i < tiles.getMap().length; i++) {
                            for (int j = 0; j < tiles.getMap()[i].length; j++) {
                                tiles.getMap()[i][j].reset();
                            }
                        }
                    }
                }
            }

            g2.dispose();
        }
    }
}
