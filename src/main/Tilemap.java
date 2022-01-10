package main;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.Color;
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
        final AffineTransform at2 = AffineTransform.getScaleInstance((double) tileSize/32, (double) tileSize/32);
        final AffineTransformOp ato2 = new AffineTransformOp(at2, AffineTransformOp.TYPE_BICUBIC);
        AffineTransform flip = AffineTransform.getScaleInstance(1, -1);
        flip.translate(0, -tileSize);
        flip.concatenate(at2);
        AffineTransformOp flipping = new AffineTransformOp(flip, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        AffineTransform rotate = AffineTransform.getRotateInstance(Math.PI/2, tileSize/2, tileSize/2);
        rotate.concatenate(at2);
        AffineTransformOp rotation = new AffineTransformOp(rotate, AffineTransformOp.TYPE_BILINEAR);
        AffineTransform rotateflip = AffineTransform.getRotateInstance(Math.PI/2, tileSize/2, tileSize/2);
        rotateflip.concatenate(flip);
        AffineTransformOp rotateflipping = new AffineTransformOp(rotateflip, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        
        


        BufferedImage subimage;
        for (int col = 0; col < Columns; col++) {
            for (int row = 0; row < Rows; row++) {
                
                subimage = tileimage.getSubimage(col, row, 1, 1);
                BufferedImage scaledImage = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
                BufferedImage se = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
                BufferedImage s = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
                BufferedImage co = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
                BufferedImage rse = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
                BufferedImage rs = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
                BufferedImage lse = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
                BufferedImage ls = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
                BufferedImage dse = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
                BufferedImage ds = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
                se = ato2.filter(spikesEdge, se);
                s = ato2.filter(spikes, s);
                co = ato2.filter(coin, co);
                rse = rotation.filter(spikesEdge, rse); 
                rs = rotation.filter(spikes, rs);
                dse = flipping.filter(spikesEdge, dse); 
                ds = flipping.filter(spikes, ds);
                lse = rotateflipping.filter(spikesEdge, lse); 
                ls = rotateflipping.filter(spikes, ls);

                
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
                    map[row][col].newImage(s);
                }
                else if (c.getRed() == 150 && c.getGreen() == 0 && c.getBlue() == 0) {
                    map[row][col] = new Tile(scaledImage, 5, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(se);
                }
                else if (c.getRed() == 0 && c.getGreen() == 200 && c.getBlue() == 0) {
                    map[row][col] = new Tile(scaledImage, 7, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(ls);
                }
                else if (c.getRed() == 0 && c.getGreen() == 150 && c.getBlue() == 0) {
                    map[row][col] = new Tile(scaledImage, 7, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(lse);
                }
                else if (c.getRed() == 0 && c.getGreen() == 0 && c.getBlue() == 200) {
                    map[row][col] = new Tile(scaledImage, 9, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(ds);
                }
                else if (c.getRed() == 0 && c.getGreen() == 0 && c.getBlue() == 150) {
                    map[row][col] = new Tile(scaledImage, 9, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(dse);
                }
                else if (c.getRed() == 200 && c.getGreen() == 0 && c.getBlue() == 200) {
                    map[row][col] = new Tile(scaledImage, 11, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(rs);
                }
                else if (c.getRed() == 150 && c.getGreen() == 0 && c.getBlue() == 150) {
                    map[row][col] = new Tile(scaledImage, 11, tileSize * col, tileSize * row, tileSize);
                    map[row][col].newImage(rse);
                }

                else {
                    map[row][col] = new Tile(scaledImage, 0, tileSize * col, tileSize * row, tileSize);
                }
                if (c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255) {
                    map[row][col].newImage(smallpooper);
                }
                if (c.getRed() == 250 && c.getGreen() == 200 && c.getBlue() == 0) {
                    map[row][col].newImage(co);
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