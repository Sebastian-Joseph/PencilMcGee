package main;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class Tilemap {

    private int tileSize;
    private int numRows;
    private int numColumns;
    public Tile[][] map;
    private int Rows;
    private int Columns;


    public Tilemap(int width, int height, int size) {
        tileSize = size;
        numRows = height / (tileSize);
        numColumns = width / (tileSize);
    }

    public Tile[][] getMap() {
        return map;
    }

    public void createMap(String filename) throws IOException {
        System.out.println(LocalDateTime.now());
        BufferedImage tileimage = ImageIO.read(getClass().getResourceAsStream(filename));
        BufferedImage smallpooper = ImageIO.read(getClass().getResourceAsStream("images/small_pooper.png"));
        BufferedImage spikes = ImageIO.read(getClass().getResourceAsStream("images/spikes.png"));
        BufferedImage spikesEdge = ImageIO.read(getClass().getResourceAsStream("images/spikes_edge.png"));
        BufferedImage coin = ImageIO.read(getClass().getResourceAsStream("images/coin.png"));

        Columns = tileimage.getWidth();
        Rows = tileimage.getHeight();
        map = new Tile[Rows][Columns];

        final AffineTransform at = AffineTransform.getScaleInstance(tileSize, tileSize);
        final AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);

        BufferedImage subimage;
        for (int col = 0; col < Columns; col++) {
            for (int row = 0; row < Rows; row++) {
                subimage = tileimage.getSubimage(col, row, 1, 1);
                BufferedImage scaledImage = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);

                scaledImage = ato.filter(subimage, scaledImage);

                Color c = new Color(subimage.getRGB(0, 0));
                if ((c.getRed() == 0 && c.getGreen() == 0 && c.getBlue() == 0)) {
                    map[row][col] = new Tile(scaledImage, 1, tileSize * col, tileSize * row, tileSize);
                }
                else if (c.getRed() == 247 && c.getGreen() == 247 && c.getBlue() == 247) {
                    map[row][col] = new Tile(scaledImage, 2, tileSize * col, tileSize * row, tileSize);
                }
                else if (c.getRed() == 200 && c.getGreen() == 0 && c.getBlue() == 0) {
                    map[row][col] = new Tile(scaledImage, 5, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(spikes);
                }
                else if (c.getRed() == 150 && c.getGreen() == 0 && c.getBlue() == 0) {
                    map[row][col] = new Tile(scaledImage, 5, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(spikesEdge);
                }
                else if (c.getRed() == 0 && c.getGreen() == 200 && c.getBlue() == 0) {
                    map[row][col] = new Tile(scaledImage, 7, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(spikes);
                }
                else if (c.getRed() == 0 && c.getGreen() == 150 && c.getBlue() == 0) {
                    map[row][col] = new Tile(scaledImage, 7, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(spikesEdge);
                }
                else if (c.getRed() == 0 && c.getGreen() == 0 && c.getBlue() == 200) {
                    map[row][col] = new Tile(scaledImage, 9, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(spikes);
                }
                else if (c.getRed() == 0 && c.getGreen() == 0 && c.getBlue() == 150) {
                    map[row][col] = new Tile(scaledImage, 9, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(spikesEdge);
                }
                else if (c.getRed() == 200 && c.getGreen() == 0 && c.getBlue() == 200) {
                    map[row][col] = new Tile(scaledImage, 11, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(spikes);
                }
                else if (c.getRed() == 150 && c.getGreen() == 0 && c.getBlue() == 150) {
                    map[row][col] = new Tile(scaledImage, 11, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(spikesEdge);
                }

                else {
                    map[row][col] = new Tile(scaledImage, 0, tileSize * col, tileSize * row, tileSize);
                }
                if (c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255) {
                    map[row][col].newImage(smallpooper);
                }
                if (c.getRed() == 250 && c.getGreen() == 200 && c.getBlue() == 0) {
                    map[row][col] = new Tile(scaledImage, 4, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(coin);
                }

            }
        }
        System.out.println(LocalDateTime.now());
    }

    public boolean checkTileToLeft(int row, int col) {
        try {
            return (map[row][col - 1].getType() % 2 == 1);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }
}