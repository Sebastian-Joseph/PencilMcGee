package main;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.xml.namespace.QName;

import java.util.concurrent.TimeUnit;

public class Tile {

	private BufferedImage image;
	private int type;
	private int x;
	private int y;

	private BufferedImage stage1;
	private BufferedImage stage2;
	private BufferedImage stage3;

	private BufferedImage white;

	private int xInit;
	private int yInit;

	public Tile(BufferedImage image, int type, int x, int y) throws IOException {
		this.image = image;
		this.type = type;
		this.x = x;
		this.y = y;

		xInit = x;
		yInit = y;

		white = ImageIO.read(getClass().getResourceAsStream("images/small_pooper.png"));
		stage1 = ImageIO.read(getClass().getResourceAsStream("images/graphite1.png"));
		stage2 = ImageIO.read(getClass().getResourceAsStream("images/graphite2.png"));
		stage3 = ImageIO.read(getClass().getResourceAsStream("images/graphite3.png"));
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
		x = xInit;
		y = yInit;
	}

	public void newImage(String path) throws IOException {
		image = ImageIO.read(getClass().getResourceAsStream(path));
	}

	public boolean change() {
		if (type == 0) {
			new Thread(() -> {
				image = stage1;
				type = 1;

				try {
					TimeUnit.SECONDS.sleep(4);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				image = stage2;

				try {
					TimeUnit.SECONDS.sleep(4);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				image = stage3;

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