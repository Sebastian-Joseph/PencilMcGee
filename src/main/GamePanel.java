package main;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    // Screen Settings
    final int originalTileSize = 8;
    final int scale = 4;

    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 48;
    final int maxScreenRow = 27;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.decode("#f7f7f7"));
        this.setDoubleBuffered(true);
    }

    public void run() {

    }
}
