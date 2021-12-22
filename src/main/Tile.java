package main;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.xml.namespace.QName;
import java.awt.geom.AffineTransform;
import java.awt.image.*;



import java.util.concurrent.TimeUnit;

public class Tile {

	private BufferedImage image;
	private int type;
	private int x;
	private int y;
	private int initialx;
	private int initialy;
	private int size;

	private BufferedImage stage1;
	private BufferedImage stage2;
	private BufferedImage stage3;

	private BufferedImage white;

	private BufferedImage scaledImage;

	public Tile(BufferedImage image, int type, int x, int y, int s) throws IOException {
		this.image = image;
		this.type = type;
		this.x = x;
		this.y = y;
		initialx = x;
		initialy = y;
		size = s;
		scaledImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

	}

	public BufferedImage getImage() {
		return image;
	}

	public int getType() {
		return type;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void scroll(double scrollAmount) {
		x -= scrollAmount;
	}

	public void reset() {
		x = initialx;
		y = initialy;
	}

	public void newImage(BufferedImage newimage) throws IOException {
		image = newimage;
	}

	public boolean change() {
		

		final AffineTransform at = AffineTransform.getScaleInstance((double) size/32, (double) size/32);
        final AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		

		if (type == 0) {
			new Thread(() -> {
				try {
					white = ImageIO.read(getClass().getResourceAsStream("images/small_pooper.png"));
					stage1 = ImageIO.read(getClass().getResourceAsStream("images/graphite1.png"));
					stage2 = ImageIO.read(getClass().getResourceAsStream("images/graphite2.png"));
					stage3 = ImageIO.read(getClass().getResourceAsStream("images/graphite3.png"));
				} 
				catch (IOException e1) {}
				image = ato.filter(stage1, scaledImage);
				type = 1;

				try {
					TimeUnit.SECONDS.sleep(4);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				image = ato.filter(stage2, scaledImage);

				try {
					TimeUnit.SECONDS.sleep(4);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				image = ato.filter(stage3, scaledImage);

				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				image = white;
				type = 0;

				
			}).start();

			return true;
		}
		
		return false;
	}
}