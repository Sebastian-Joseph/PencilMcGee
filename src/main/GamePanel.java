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
import java.awt.Stroke;
import java.awt.MouseInfo;

import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener; 
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements Runnable {
    // Screen Settings
    final int originalTileSize = 8;
    public static final int scale = 4;

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
    BufferedImage enemy2;

    BufferedImage cannon0;
    BufferedImage cannon1;
    BufferedImage cannon2;
    BufferedImage cannon3;

    BufferedImage testTile;

    private Tilemap tiles = new Tilemap(screenWidth, screenHeight, tileSize);
    private boolean mouseDown = false;
    // private boolean enterDown = false;


    private int gameState;
    private final int menuState = 0;
    private final int playState = 1;
    private final int instructState = 2;
    private final int pauseState = 3;

    private int levelState;

    private final int level1EnemyDamage = 10;
    private final int level2EnemyDamage = 15;
    private final int level3EnemyDamage = 20;

    private final int level1ClearDistance = 798 * tileSize;
    private final int level2ClearDistance = 798 * tileSize;
    private final int level3ClearDistance = 643 * tileSize;

    private final int level1LeadCount = 300;
    private final int level2LeadCount = 500;
    private final int level3LeadCount = 200;

    private final int xInit = tileSize * 2;
    private final int yInit = screenHeight - tileSize * 8;

    int FPS = 60;
    GameMusic music = new GameMusic();
    Menu menu = new Menu();
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    Player p1 = new Player(xInit, yInit, tileSize / 2, tileSize * 2, level1ClearDistance, level1LeadCount);

    ArrayList<Enemy> enemies = new ArrayList<Enemy>();

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

    Enemy[] enemiesInit2 = new Enemy[] {
        new Enemy(tileSize * 70.5, tileSize * 12, tileSize * 70.5, tileSize * 24, tileSize * 2, 3.5, level2EnemyDamage, false),
        new Enemy(tileSize * 159, tileSize * 4, tileSize * 159, tileSize * 21, tileSize * 1, 7, level2EnemyDamage, false),

        new Enemy(tileSize * 328, tileSize * 12, tileSize * 333, tileSize * 12, tileSize * 1, 3, level2EnemyDamage, false),
        new Enemy(tileSize * 333, tileSize * 18, tileSize * 328, tileSize * 18, tileSize * 1, 3, level2EnemyDamage, false),

        new Enemy(tileSize * 340, tileSize * 17, tileSize * 345, tileSize * 17, tileSize * 1, 3, level2EnemyDamage, false),
        new Enemy(tileSize * 345, tileSize * 6, tileSize * 340, tileSize * 6, tileSize * 1, 3, level2EnemyDamage, false),

        new Enemy(tileSize * 390, tileSize * 6, tileSize * 390, tileSize * 11, tileSize * 2, 2.5, level2EnemyDamage, false),
        new Enemy(tileSize * 403, tileSize * 6, tileSize * 403, tileSize * 11, tileSize * 2, 3.5, level2EnemyDamage, false),
        new Enemy(tileSize * 390, tileSize * 18, tileSize * 390, tileSize * 24, tileSize * 2, 3.5, level2EnemyDamage, false),
        new Enemy(tileSize * 403, tileSize * 24, tileSize * 403, tileSize * 18, tileSize * 2, 2.5, level2EnemyDamage, false),
        new Enemy(tileSize * 412, tileSize * 5, tileSize * 420, tileSize * 5, tileSize * 1, 4, level2EnemyDamage, false),

        new Enemy(tileSize * 486, tileSize * 7, tileSize * 486, tileSize * 19, tileSize * 1, 8, level2EnemyDamage, false),

        new Enemy(tileSize * 651, tileSize * 14, tileSize * 630, tileSize * 3, tileSize * 2, 4.5, level2EnemyDamage, false)
    };

    Enemy[] enemiesInit3 = new Enemy[] {
        new Enemy(tileSize * 123, tileSize * 5, tileSize * 123, tileSize * 17, tileSize, 7, level3EnemyDamage, false),

        new Enemy(tileSize * 349, tileSize * 4, tileSize * 349, tileSize * 21, tileSize * 2, 6, level3EnemyDamage, false),
        new Enemy(tileSize * 369, tileSize * 4, tileSize * 369, tileSize * 21, tileSize * 2, 6, level3EnemyDamage, false),

        new Enemy(tileSize * 388, tileSize * 4, tileSize * 407, tileSize * 21, tileSize * 2, 4, level3EnemyDamage, false),
        new Enemy(tileSize * 388, tileSize * 21, tileSize * 407, tileSize * 4, tileSize * 2, 4, level3EnemyDamage, false),

        new Enemy(tileSize * 511, tileSize * 8, tileSize * 511, tileSize * 22, tileSize * 1.5, 4, level3EnemyDamage, false),
        new Enemy(tileSize * 600, tileSize * 2, tileSize * 600, tileSize * 8, tileSize * 1.5, 2, level3EnemyDamage, false)
    };

    Enemy[] movingNoDraws1 = new Enemy[0]; // Empty on purpose

    Enemy[] movingNoDraws2 = new Enemy[] {
        new MovingNoDraw(tileSize * 12, tileSize, tileSize * 20, tileSize, tileSize, 3.25, tileSize, tileSize * 9),

        new MovingNoDraw(tileSize * 26, tileSize * 8, tileSize * 37, tileSize * 8, tileSize, 1.5, tileSize * 2, tileSize * 8),

        new MovingNoDraw(tileSize * 65, tileSize * 12, tileSize * 65, tileSize * 25, tileSize, 2, tileSize * 5, tileSize),
        new MovingNoDraw(tileSize * 73, tileSize * 25, tileSize * 73, tileSize * 12, tileSize, 2, tileSize * 5, tileSize),

        new MovingNoDraw(tileSize * 98, tileSize * 23, tileSize * 98, tileSize * 7, tileSize, 2, tileSize * 4, tileSize * 4),
        new MovingNoDraw(tileSize * 98, tileSize * 7, tileSize * 98, tileSize * 23, tileSize, 2, tileSize * 4, tileSize * 4),

        new MovingNoDraw(tileSize * 113, tileSize * 27, tileSize * 113, 0, tileSize, 3, tileSize * 4, tileSize * 4),
        
        new MovingNoDraw(tileSize * 156, tileSize * 11, tileSize * 127, tileSize * 11, tileSize, 3, tileSize, tileSize * 6),

        new MovingNoDraw(tileSize * 165, tileSize * 17, tileSize * 175, tileSize * 17, tileSize, 2.5, tileSize, tileSize * 4),

        new MovingNoDraw(tileSize * 163, tileSize * 7, tileSize * 189, tileSize * 7, tileSize, 4, tileSize, tileSize * 4),
        
        new MovingNoDraw(tileSize * 192, tileSize * 2, tileSize * 192, tileSize * 25, tileSize, 3.5, tileSize * 27, tileSize),

        new MovingNoDraw(tileSize * 272, tileSize * 5, tileSize * 290, tileSize * 5, tileSize, 7, tileSize * 3, tileSize * 4),
        new MovingNoDraw(tileSize * 276, tileSize * 15, tileSize * 304, tileSize * 15, tileSize, 4.5, tileSize, tileSize * 9),

        new MovingNoDraw(tileSize * 309, tileSize * 11, tileSize * 309, tileSize * 21.5, tileSize, 2, tileSize * 7, tileSize * 0.5),
        new MovingNoDraw(tileSize * 309, tileSize * 10, tileSize * 309, 0, tileSize, 4, tileSize * 7, tileSize),

        new MovingNoDraw(tileSize * 335, tileSize * 20, tileSize * 335, tileSize * 26, tileSize, 1, tileSize * 4, tileSize),

        new MovingNoDraw(tileSize * 420, tileSize * 1, tileSize * 374, tileSize * 1, tileSize, 7, tileSize, tileSize * 25),
        
        new MovingNoDraw(tileSize * 456, tileSize * 25, tileSize * 465, tileSize * 18, tileSize, 3, tileSize * 2, tileSize * 2),

        new MovingNoDraw(tileSize * 479, tileSize * 9, tileSize * 479, tileSize * 18, tileSize, 2.5, tileSize * 3, tileSize),
        new MovingNoDraw(tileSize * 491, tileSize * 19, tileSize * 491, tileSize * 12, tileSize, 2, tileSize * 5, tileSize),

        new MovingNoDraw(tileSize * 500, 0, tileSize * 500, tileSize * 26, tileSize, 7, tileSize * 13, tileSize),

        new MovingNoDraw(tileSize * 523, tileSize * 2, tileSize * 544, tileSize * 2, tileSize, 6, tileSize, tileSize * 2),
        new MovingNoDraw(tileSize * 523, tileSize * 4, tileSize * 544, tileSize * 4, tileSize, 5, tileSize, tileSize * 2),

        new MovingNoDraw(tileSize * 556, tileSize * 19, tileSize * 556, tileSize * 26, tileSize, 3.5, tileSize * 9, tileSize),
        new MovingNoDraw(tileSize * 568, tileSize * 3, tileSize * 556, tileSize * 3, tileSize, 2.5, tileSize * 4, tileSize * 14),
        new MovingNoDraw(tileSize * 587, tileSize * 3, tileSize * 587, tileSize * 23, tileSize, 4, tileSize * 4, tileSize),

        new MovingNoDraw(tileSize * 603, tileSize * 13, tileSize * 618, tileSize * 2, tileSize, 6, tileSize * 8, tileSize * 5),
        new MovingNoDraw(tileSize * 603, tileSize * 2, tileSize * 650, tileSize * 2, tileSize, 8, tileSize * 5, tileSize * 8),
        new MovingNoDraw(tileSize * 627, tileSize * 2, tileSize * 627, tileSize * 17, tileSize, 3, tileSize * 10, tileSize),
        new MovingNoDraw(tileSize * 650, tileSize * 7, tileSize * 636, tileSize * 2, tileSize, 4, tileSize * 5, tileSize * 5),

        new MovingNoDraw(tileSize * 658, tileSize * 0, tileSize * 658, tileSize * 26.25, tileSize, 5, tileSize * 75, tileSize * 0.75),
        new MovingNoDraw(tileSize * 738, tileSize * 0, tileSize * 738, tileSize * 26.25, tileSize, 7, tileSize * 22, tileSize * 0.75),
        new MovingNoDraw(tileSize * 765, tileSize * 0, tileSize * 765, tileSize * 26.25, tileSize, 6, tileSize * 26, tileSize * 0.75),
        new MovingNoDraw(tileSize * 765, tileSize * 0, tileSize * 790, tileSize * 0, tileSize, 6, tileSize, tileSize * 27)
    };

    Enemy[] movingNoDraws3 = new Enemy[] {
        new MovingNoDraw(tileSize * 22, tileSize * 4, tileSize * 32, tileSize * 4, tileSize, 4.5, tileSize, tileSize * 23),

        new MovingNoDraw(tileSize * 87, tileSize * 0, tileSize * 87, tileSize * 26.25, tileSize, 4, tileSize * 60, tileSize * 0.75),

        new MovingNoDraw(tileSize * 87, tileSize * 5, tileSize * 106, tileSize * 5, tileSize, 5, tileSize * 3, tileSize * 5),

        new MovingNoDraw(tileSize * 104, tileSize * 13, tileSize * 115, tileSize * 13, tileSize, 4, tileSize * 3, tileSize * 5),

        new MovingNoDraw(tileSize * 152, tileSize * 9, tileSize * 174, tileSize * 9, tileSize, 5, tileSize * 1, tileSize * 3),

        new MovingNoDraw(tileSize * 152, tileSize * 0, tileSize * 152, tileSize * 25.25, tileSize, 5, tileSize * 44, tileSize * 0.75),

        new MovingNoDraw(tileSize * 183, tileSize * 15, tileSize * 197, tileSize * 15, tileSize, 5.5, tileSize * 3, tileSize * 4),

        new MovingNoDraw(tileSize * 202, tileSize * 10, tileSize * 202, tileSize * 14, tileSize, 1, tileSize * 3, tileSize),
        new MovingNoDraw(tileSize * 208, tileSize * 11, tileSize * 208, tileSize * 7, tileSize, 1.5, tileSize * 3, tileSize),
        new MovingNoDraw(tileSize * 212, tileSize * 14, tileSize * 212, tileSize * 18, tileSize, 1, tileSize * 3, tileSize),
        new MovingNoDraw(tileSize * 218, tileSize * 8, tileSize * 218, tileSize * 12, tileSize, 1.5, tileSize * 3, tileSize),
        new MovingNoDraw(tileSize * 219, tileSize * 22, tileSize * 219, tileSize * 18, tileSize, 1, tileSize * 3, tileSize),
        new MovingNoDraw(tileSize * 225, tileSize * 8, tileSize * 225, tileSize * 4, tileSize, 1.5, tileSize * 3, tileSize),
        new MovingNoDraw(tileSize * 228, tileSize * 19, tileSize * 228, tileSize * 23, tileSize, 1, tileSize * 3, tileSize),
        new MovingNoDraw(tileSize * 233, tileSize * 8, tileSize * 233, tileSize * 12, tileSize, 1.5, tileSize * 3, tileSize),
        new MovingNoDraw(tileSize * 238, tileSize * 3, tileSize * 238, tileSize * 7, tileSize, 1, tileSize * 3, tileSize),
        new MovingNoDraw(tileSize * 236, tileSize * 20, tileSize * 236, tileSize * 24, tileSize, 1, tileSize * 3, tileSize),
    
        new MovingNoDraw(tileSize * 243, tileSize * 6, tileSize * 247.5, tileSize * 6, tileSize, 1, tileSize * 0.5, tileSize * 14),

        new MovingNoDraw(tileSize * 310, tileSize * 22, tileSize * 310, tileSize * 27, tileSize, 1, tileSize * 3, tileSize),

        new MovingNoDraw(tileSize * 335, tileSize * 4, tileSize * 335, tileSize * 22.25, tileSize, 4, tileSize * 80, tileSize * 0.75),
        new MovingNoDraw(tileSize * 414, tileSize * 4, tileSize * 335, tileSize * 4, tileSize, 7, tileSize, tileSize * 19),

        new MovingNoDraw(tileSize * 477, tileSize * 0, tileSize * 477, tileSize * 26.25, tileSize, 4, tileSize * 51, tileSize * 0.75),
        new MovingNoDraw(tileSize * 477, tileSize * 0, tileSize * 528, tileSize * 0, tileSize, 7, tileSize, tileSize * 27),

        new MovingNoDraw(tileSize * 483, tileSize * 13, tileSize * 506.5, tileSize * 13, tileSize, 3, tileSize * 1.5, tileSize * 5),

        new MovingNoDraw(tileSize * 534, tileSize * 0, tileSize * 534, tileSize * 26, tileSize, 5, tileSize * 46, tileSize),
        new MovingNoDraw(tileSize * 534, tileSize * 0, tileSize * 579, tileSize * 0, tileSize, 7, tileSize, tileSize * 27),

        new MovingNoDraw(tileSize * 555, tileSize * 10, tileSize * 565, tileSize * 10, tileSize, 4, tileSize * 2, tileSize * 4),

        new MovingNoDraw(tileSize * 534, tileSize * 13, tileSize * 548, tileSize * 13, tileSize, 4, tileSize * 3, tileSize * 3),

        new MovingNoDraw(tileSize * 590, tileSize * 7, tileSize * 590, tileSize * 18, tileSize, 3, tileSize * 3, tileSize),
        new MovingNoDraw(tileSize * 596, tileSize * 18, tileSize * 596, tileSize * 7, tileSize, 3, tileSize * 3, tileSize),

        new MovingNoDraw(tileSize * 587, tileSize * 0, tileSize * 587, tileSize * 26, tileSize, 5, tileSize * 28, tileSize * 2)
    };

    Cannon[] cannons1 = new Cannon[] {
        new Cannon(tileSize * 122, tileSize * 19, 150, 2, tileSize, 2, level1EnemyDamage, 45),
        new Cannon(tileSize * 123, tileSize * 19, 150, 2, tileSize, 2, level1EnemyDamage, 30),
        new Cannon(tileSize * 124, tileSize * 19, 150, 2, tileSize, 2, level1EnemyDamage, 15),
        new Cannon(tileSize * 125, tileSize * 19, 150, 2, tileSize, 2, level1EnemyDamage, 0),
        
        new Cannon(tileSize * 177, tileSize, 120, 2, tileSize, 3, level1EnemyDamage, 0),
        new Cannon(tileSize * 183, tileSize, 30, 2, tileSize, 3, level1EnemyDamage, 0),

        new Cannon(tileSize * 417, tileSize * 17, 240, 3, tileSize, 4, level1EnemyDamage, 239),

        new Cannon(tileSize * 488, tileSize * 10, 60, 0, tileSize, 7, level1EnemyDamage, 0),
        new Cannon(tileSize * 490, tileSize * 12, 60, 1, tileSize, 7, level1EnemyDamage, 0),
        new Cannon(tileSize * 488, tileSize * 14, 120, 2, tileSize, 4, level1EnemyDamage, 0),

        new Cannon(tileSize * 505, tileSize, 120, 2, tileSize, 7, level1EnemyDamage, 0),
        new Cannon(tileSize * 507, tileSize, 120, 2, tileSize, 7, level1EnemyDamage, 0),
        new Cannon(tileSize * 509, tileSize, 120, 2, tileSize, 7, level1EnemyDamage, 0),

        new Cannon(tileSize * 519, tileSize, 120, 2, tileSize, 7, level1EnemyDamage, 0),
        new Cannon(tileSize * 521, tileSize, 120, 2, tileSize, 7, level1EnemyDamage, 0),
        new Cannon(tileSize * 523, tileSize, 120, 2, tileSize, 7, level1EnemyDamage, 0),

        new Cannon(tileSize * 564, tileSize * 15, 120, 3, tileSize, 5, level1EnemyDamage, 0),
        
        new Cannon(tileSize * 585, tileSize * 14, 90, 3, tileSize, 5, level1EnemyDamage, 0),
        new Cannon(tileSize * 586, tileSize * 13, 120, 0, tileSize, 5, level1EnemyDamage, 0),
        new Cannon(tileSize * 587, tileSize * 14, 90, 1, tileSize, 5, level1EnemyDamage, 0),
        new Cannon(tileSize * 586, tileSize * 15, 120, 2, tileSize, 5, level1EnemyDamage, 0)
    };
    
    Cannon[] cannons2 = new Cannon[] {
        new Cannon(tileSize * 242, tileSize * 9, 45, 1, tileSize, 8, level2EnemyDamage, 0),

        new Cannon(tileSize * 238, tileSize * 18, 150, 1, tileSize, 4, level2EnemyDamage, 0),
        new Cannon(tileSize * 267, tileSize * 20, 150, 3, tileSize, 4, level2EnemyDamage, 0),

        new Cannon(tileSize * 478, tileSize * 3, 165, 3, tileSize, 4, level2EnemyDamage, 0),
        new Cannon(tileSize * 478, tileSize * 5, 165, 3, tileSize, 4, level2EnemyDamage, 0)
    };

    Cannon[] cannons3 = new Cannon[] {
        new Cannon(tileSize * 61, tileSize * 5, 240, 3, tileSize, 4, level3EnemyDamage, 0),
        new Cannon(tileSize * 61, tileSize * 6, 240, 3, tileSize, 4, level3EnemyDamage, 0),
        new Cannon(tileSize * 61, tileSize * 7, 240, 3, tileSize, 4, level3EnemyDamage, 0),
        new Cannon(tileSize * 61, tileSize * 9, 240, 3, tileSize, 4, level3EnemyDamage, 120),
        new Cannon(tileSize * 61, tileSize * 10, 240, 3, tileSize, 4, level3EnemyDamage, 120),
        new Cannon(tileSize * 61, tileSize * 11, 240, 3, tileSize, 4, level3EnemyDamage, 120),

        new Cannon(tileSize * 83, tileSize * 24, 180, 3, tileSize, 5, level3EnemyDamage, 0),
        new Cannon(tileSize * 83, tileSize * 23, 180, 3, tileSize, 5, level3EnemyDamage, 0),
        new Cannon(tileSize * 83, tileSize * 22, 180, 3, tileSize, 5, level3EnemyDamage, 0),
        new Cannon(tileSize * 83, tileSize * 21, 180, 3, tileSize, 5, level3EnemyDamage, 0),
        new Cannon(tileSize * 83, tileSize * 20, 180, 3, tileSize, 5, level3EnemyDamage, 0),
        new Cannon(tileSize * 83, tileSize * 19, 180, 3, tileSize, 5, level3EnemyDamage, 0),
        new Cannon(tileSize * 83, tileSize * 18, 180, 3, tileSize, 5, level3EnemyDamage, 0),
        new Cannon(tileSize * 83, tileSize * 17, 180, 3, tileSize, 5, level3EnemyDamage, 0),

        new Cannon(tileSize * 300, tileSize * 7, 50, 1, tileSize, 4, level3EnemyDamage, 0),
        new Cannon(tileSize * 300, tileSize * 9, 50, 1, tileSize, 4, level3EnemyDamage, 0),
        new Cannon(tileSize * 300, tileSize * 11, 50, 1, tileSize, 4, level3EnemyDamage, 0),
        new Cannon(tileSize * 300, tileSize * 13, 50, 1, tileSize, 4, level3EnemyDamage, 0),
        new Cannon(tileSize * 300, tileSize * 15, 50, 1, tileSize, 4, level3EnemyDamage, 0),
        new Cannon(tileSize * 300, tileSize * 17, 50, 1, tileSize, 4, level3EnemyDamage, 0),
        new Cannon(tileSize * 300, tileSize * 19, 50, 1, tileSize, 4, level3EnemyDamage, 0),

        new Cannon(tileSize * 322, tileSize * 19, 120, 3, tileSize, 5, level3EnemyDamage, 0),
        new Cannon(tileSize * 322, tileSize * 17, 120, 3, tileSize, 5, level3EnemyDamage, 0),
        new Cannon(tileSize * 322, tileSize * 15, 120, 3, tileSize, 5, level3EnemyDamage, 0),
        new Cannon(tileSize * 322, tileSize * 13, 120, 3, tileSize, 5, level3EnemyDamage, 0),
        new Cannon(tileSize * 322, tileSize * 11, 120, 3, tileSize, 5, level3EnemyDamage, 0),
        new Cannon(tileSize * 322, tileSize * 9, 120, 3, tileSize, 5, level3EnemyDamage, 0),
        new Cannon(tileSize * 322, tileSize * 7, 120, 3, tileSize, 5, level3EnemyDamage, 0)
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
        enemy2 = ImageIO.read(getClass().getResourceAsStream("images/eraser2.png"));

        cannon0 = ImageIO.read(getClass().getResourceAsStream("images/cannon0.png"));
        cannon1 = ImageIO.read(getClass().getResourceAsStream("images/cannon1.png"));
        cannon2 = ImageIO.read(getClass().getResourceAsStream("images/cannon2.png"));
        cannon3 = ImageIO.read(getClass().getResourceAsStream("images/cannon3.png"));

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
        levelState = 1;
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
        if (gameState == playState) {
            for (int i = 0; i < tiles.getMap().length; i++) {
                for (int j = 0; j < tiles.getMap()[i].length; j++) {
                    if (levelState == 1) {
                        p1.collision(tiles.getMap()[i][j], keyHandler, screenWidth, screenHeight, tileSize, i == 0, level1EnemyDamage);
                    }
                    else if (levelState == 2) {
                        p1.collision(tiles.getMap()[i][j], keyHandler, screenWidth, screenHeight, tileSize, i == 0, level2EnemyDamage);
                    }
                    else if (levelState == 3) {
                        p1.collision(tiles.getMap()[i][j], keyHandler, screenWidth, screenHeight, tileSize, i == 0, level3EnemyDamage);
                    }
                }
            }

            if (levelState == 1) {
                for (Cannon c : cannons1) {
                    if (c.getX() <= screenWidth * 2 && c.getX() >= tileSize * -4) {
                        if (c.createSpawn()) {
                            Enemy temp = new Enemy(c.getSpawn().getX(), c.getSpawn().getY(), c.getSpawn().getXEnd(), c.getSpawn().getYEnd(), tileSize, c.getSpawn().getSpeed(), c.getSpawn().getDamage(), true);
                            enemies.add(temp);
                        }
                    }
                }
            }
            else if (levelState == 2) {
                for (Cannon c : cannons2) {
                    if (c.getX() <= screenWidth * 2 && c.getX() >= tileSize * -4) {
                        if (c.createSpawn()) {
                            Enemy temp = new Enemy(c.getSpawn().getX(), c.getSpawn().getY(), c.getSpawn().getXEnd(), c.getSpawn().getYEnd(), tileSize, c.getSpawn().getSpeed(), c.getSpawn().getDamage(), true);
                            enemies.add(temp);
                        }
                    }
                }
                for (Enemy m : movingNoDraws2) {
                    if (m.getX() <= screenWidth * 2 && m.getX() >= screenWidth * -2) {
                        for (int i = 0; i < tiles.getMap().length; i++) {
                            for (int j = 0; j < tiles.getMap()[i].length; j++) {
                                m.collidesWithTile(tiles.getMap()[i][j]);
                            }
                        }

                        m.move();
                    }
                }
                for (int i = 0; i < tiles.getMap().length; i++) {
                    for (int j = 0; j < tiles.getMap()[i].length; j++) {
                        tiles.getMap()[i][j].tileIsOverMovingNoDraw(movingNoDraws2);
                    }
                }
            }
            else if (levelState == 3) {
                for (Cannon c : cannons3) {
                    if (c.getX() <= screenWidth * 2 && c.getX() >= tileSize * -4) {
                        if (c.createSpawn()) {
                            Enemy temp = new Enemy(c.getSpawn().getX(), c.getSpawn().getY(), c.getSpawn().getXEnd(), c.getSpawn().getYEnd(), tileSize, c.getSpawn().getSpeed(), c.getSpawn().getDamage(), true);
                            enemies.add(temp);
                        }
                    }
                }
                for (Enemy m : movingNoDraws3) {
                    if (m.getX() <= screenWidth * 2 && m.getX() >= screenWidth * -2) {
                        for (int i = 0; i < tiles.getMap().length; i++) {
                            for (int j = 0; j < tiles.getMap()[i].length; j++) {
                                m.collidesWithTile(tiles.getMap()[i][j]);
                            }
                        }

                        m.move();
                    }
                }
                for (int i = 0; i < tiles.getMap().length; i++) {
                    for (int j = 0; j < tiles.getMap()[i].length; j++) {
                        tiles.getMap()[i][j].tileIsOverMovingNoDraw(movingNoDraws2);
                    }
                }
            }

            for (int e = 0; e < enemies.size(); e++) {
                if (enemies.get(e).getX() <= screenWidth * 2 && enemies.get(e).getX() >= tileSize * -4) {
                    for (int i = 0; i < tiles.getMap().length; i++) {
                        for (int j = 0; j < tiles.getMap()[i].length; j++) {
                            enemies.get(e).collidesWithTile(tiles.getMap()[i][j]);
                        }
                    }
                    p1.enemyCollision(enemies.get(e));

                    if (enemies.get(e).move()) {
                        enemies.remove(enemies.get(e));
                    }
                }
                else {
                    p1.enemyCollision(new Enemy(0, screenHeight * 2, 0, 0, 0, 0, 0, false));
                }
            }

            if (p1.getLeadCount() <= 0) {
                p1.reset(xInit, yInit);
                for (int i = 0; i < tiles.getMap().length; i++) {
                    for (int j = 0; j < tiles.getMap()[i].length; j++) {
                        tiles.getMap()[i][j].reset();
                        if (tiles.getMap()[i][j].getType() == 3) tiles.getMap()[i][j].revert();
                    }
                }
                enemies.clear();
                if (levelState == 1) {
                    for (Enemy e : enemiesInit1) {
                        enemies.add(e);
                    }
                    for (Cannon c : cannons1) {
                        c.reset();
                    }
                }
                else if (levelState == 2) {
                    for (Enemy e : enemiesInit2) {
                        enemies.add(e);
                    }
                    for (Cannon c : cannons2) {
                        c.reset();
                    }
                    for (Enemy m : movingNoDraws2) {
                        m.reset();
                    }
                }
                else if (levelState == 3) {
                    for (Enemy e : enemiesInit3) {
                        enemies.add(e);
                    }
                    for (Cannon c : cannons3) {
                        c.reset();
                    }
                    for (Enemy m : movingNoDraws3) {
                        m.reset();
                    }
                }        
                for (Enemy e : enemies) {
                    e.reset();
                }
            }

            if (!keyHandler.enterDown) {
                if (levelState == 1) p1.move(keyHandler, screenWidth, tiles, enemies, cannons1, movingNoDraws1);
                else if (levelState == 2) p1.move(keyHandler, screenWidth, tiles, enemies, cannons2, movingNoDraws2);
                else if (levelState == 3) p1.move(keyHandler, screenWidth, tiles, enemies, cannons3, movingNoDraws3);
            }

            if (p1.getXPos() >= p1.getClearDistance()) clearLevel();
        }

        if (p1.getLeadCount() <= 0) {
            p1.reset(tileSize * 2, screenHeight - tileSize * 8);
            for (int i = 0; i < tiles.getMap().length; i++) {
                for (int j = 0; j < tiles.getMap()[i].length; j++) {
                    tiles.getMap()[i][j].reset();
                    if (tiles.getMap()[i][j].getType() == 3) tiles.getMap()[i][j].revert();
                }
            }
        }
    }

    public void clearLevel() {
        enemies.clear();
        levelState++;
        if (levelState == 2) {
            for (Enemy e : enemiesInit2) {
                enemies.add(e);
            }
            p1.setNewLeadCount(level2LeadCount);
            p1.setNewClearDistance(level2ClearDistance);
            try {
                tiles.createMap("images/actual_level2.png");
            }
            catch (Exception e) {}
        }
        else if (levelState == 3) {
            for (Enemy e : enemiesInit3) {
                enemies.add(e);
            }
            p1.setNewLeadCount(level3LeadCount);
            p1.setNewClearDistance(level3ClearDistance);
            try {
                tiles.createMap("images/actual_level3.png");
            }
            catch (Exception e) {}
        }
        else if (levelState == 4) {
            // Insert ending screen stuff here
        }
        p1.reset(xInit, yInit);
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
            if (mouseDown == true && gameState == playState) {
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
                                if (!tiles.getMap()[i][j].tileIsOverMovingEnemy(enemies)
                                    && (
                                        (levelState == 1 && !tiles.getMap()[i][j].tileIsOverMovingCannon(cannons1))
                                        || (levelState == 2 && !tiles.getMap()[i][j].tileIsOverMovingNoDraw(movingNoDraws2) && !tiles.getMap()[i][j].tileIsOverMovingCannon(cannons2))
                                        || (levelState == 3 && !tiles.getMap()[i][j].tileIsOverMovingNoDraw(movingNoDraws3) && !tiles.getMap()[i][j].tileIsOverMovingCannon(cannons3))
                                        )
                                ) {
                                    if (tiles.getMap()[i][j].change(levelState)) {
                                        p1.reduceLeadCount(1);
                                    }
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

            if (levelState == 1) {
                g2.setColor(Color.pink);
                for (Cannon c : cannons1) {
                    if (c.getX() <= screenWidth * 2 && c.getX() >= tileSize * -4) {
                        if (c.getDirection() == 0) g2.drawImage(cannon0, c.getX(), c.getY(), tileSize, tileSize, null);
                        if (c.getDirection() == 1) g2.drawImage(cannon1, c.getX(), c.getY(), tileSize, tileSize, null);
                        if (c.getDirection() == 2) g2.drawImage(cannon2, c.getX(), c.getY(), tileSize, tileSize, null);
                        if (c.getDirection() == 3) g2.drawImage(cannon3, c.getX(), c.getY(), tileSize, tileSize, null);
                    }
                }
            }

            if (levelState == 2) {
                for (Enemy m : movingNoDraws2) {
                    if (m.getX() <= screenWidth * 2 && m.getX() >= screenWidth * -2) {
                        g2.setColor(Color.decode("#f7f7f7"));
                        g2.fillRect((int) m.getX(), (int) m.getY(), (int) ((MovingNoDraw) m).getWidth(), (int) ((MovingNoDraw) m).getHeight());
                        g2.setColor(Color.decode("#32b1d1"));
                        g2.drawRect((int) m.getX(), (int) m.getY(), (int) ((MovingNoDraw) m).getWidth(), (int) ((MovingNoDraw) m).getHeight());
                    }
                }
                g2.setColor(Color.pink);
                for (Cannon c : cannons2) {
                    if (c.getX() <= screenWidth * 2 && c.getX() >= tileSize * -4) {
                        if (c.getDirection() == 0) g2.drawImage(cannon0, c.getX(), c.getY(), tileSize, tileSize, null);
                        if (c.getDirection() == 1) g2.drawImage(cannon1, c.getX(), c.getY(), tileSize, tileSize, null);
                        if (c.getDirection() == 2) g2.drawImage(cannon2, c.getX(), c.getY(), tileSize, tileSize, null);
                        if (c.getDirection() == 3) g2.drawImage(cannon3, c.getX(), c.getY(), tileSize, tileSize, null);
                    }
                }
            }

            if (levelState == 3) {
                for (Enemy m : movingNoDraws3) {
                    if (m.getX() <= screenWidth * 2 && m.getX() >= screenWidth * -2) {
                        g2.setColor(Color.decode("#f7f7f7"));
                        g2.fillRect((int) m.getX(), (int) m.getY(), (int) ((MovingNoDraw) m).getWidth(), (int) ((MovingNoDraw) m).getHeight());
                        g2.setColor(Color.decode("#32b1d1"));
                        g2.drawRect((int) m.getX(), (int) m.getY(), (int) ((MovingNoDraw) m).getWidth(), (int) ((MovingNoDraw) m).getHeight());
                    }
                }
                g2.setColor(Color.pink);
                for (Cannon c : cannons3) {
                    if (c.getX() <= screenWidth * 2 && c.getX() >= tileSize * -4) {
                        if (c.getDirection() == 0) g2.drawImage(cannon0, c.getX(), c.getY(), tileSize, tileSize, null);
                        if (c.getDirection() == 1) g2.drawImage(cannon1, c.getX(), c.getY(), tileSize, tileSize, null);
                        if (c.getDirection() == 2) g2.drawImage(cannon2, c.getX(), c.getY(), tileSize, tileSize, null);
                        if (c.getDirection() == 3) g2.drawImage(cannon3, c.getX(), c.getY(), tileSize, tileSize, null);
                    }
                }
            }

            for (Enemy e : enemies) {
                if (e.getX() <= screenWidth * 2 && e.getX() >= tileSize * -4) {
                    if (e.getDisappearance()) {
                        g2.drawImage(enemy2, (int) e.getX(), (int) e.getY(), (int) e.getHeightAndWidth(), (int) e.getHeightAndWidth(), null);
                    }
                    else {
                        g2.drawImage(enemy, (int) e.getX(), (int) e.getY(), (int) e.getHeightAndWidth(), (int) e.getHeightAndWidth(), null);
                    }
                }
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

            if (p1.getInvincibility() == 0) g2.setColor(Color.black);
            else g2.setColor(Color.red);
            Font font = new Font("Ink Free", Font.BOLD, tileSize);
            g.setFont(font);
            double textOffset = tileSize * 0.6;
            g2.drawString(String.valueOf(p1.getLeadCount()), ((int) textOffset) + tileSize, ((int) textOffset) + tileSize * 2);

            if (keyHandler.enterDown) {
                gameState = pauseState;
            }
            
            if (gameState == pauseState) {
                try {
                    background = ImageIO.read(getClass().getResourceAsStream("images/pooper3.5.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g.drawImage(background, 0, 0, null);
                Font foont = new Font("Ink Free", Font.BOLD, 50);
                g.setFont(foont);
                g.setColor(Color.black);
                g.drawString("Paused", GamePanel.WIDTH + 600, 100);

                Rectangle continueButton = new Rectangle(GamePanel.WIDTH + 630, 150, 120, 50);
                Rectangle exitButton = new Rectangle(GamePanel.WIDTH + 630, 350, 100, 50);


                Font font1 = new Font("Ink Free", Font.BOLD, 30);
                g.setFont(font1);
                g.drawString("Continue", GamePanel.WIDTH + 630,180);
                g2.draw(continueButton);
                g.drawString("Exit", GamePanel.WIDTH + 650,380);
                g2.draw(exitButton);

                if (mouseDown) {
                    Point point = MouseInfo.getPointerInfo().getLocation();
                    SwingUtilities.convertPointFromScreen(point, this);
                    if (continueButton.contains(point)) {
                        gameState = playState;
                    }
                    if(exitButton.contains(point)){
                        System.exit(0);
                    }
                }




                if (keyHandler.enterDown == true) {
                    gameState = playState;
                }
            }

            g2.dispose();
        }
    }
}
