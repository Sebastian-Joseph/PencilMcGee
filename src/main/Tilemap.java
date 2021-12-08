package main;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;


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
        BufferedImage tileimage = ImageIO.read(getClass().getResourceAsStream(filename));
        Columns = tileimage.getWidth();
        Rows = tileimage.getHeight();
        map = new Tile[Rows][Columns];

        BufferedImage subimage;
        for (int col = 0; col < Columns; col++) {
            for (int row = 0; row < Rows; row++) {
                subimage = tileimage.getSubimage(col, row, 1, 1);
                BufferedImage scaledImage = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
                final AffineTransform at = AffineTransform.getScaleInstance(tileSize, tileSize);
                final AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
                scaledImage = ato.filter(subimage, scaledImage);

                Color c = new Color(subimage.getRGB(0, 0));
                if (c.getRed() == 0) {
                    map[row][col] = new Tile(scaledImage, 1, tileSize * col, tileSize * row);
                }
                else {
                    map[row][col] = new Tile(scaledImage, 0, tileSize * col, tileSize * row);
                }
                if (c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255) {
                    map[row][col].newImage("images/small_pooper.png");
                }
            }
        }
    }

    // public void draw(Graphics2D g){
    //     System.out.println(map[0][0].getImage());
    //     for(int col = 0; col < numColumns; col++) {
    //         for(int row = 0; row < numRows; row++) {
    //             g.drawImage(map[row][col].getImage(), col * tileSize, row * tileSize, null);
    //         }
    //     }
    // }
}