package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import graphics.Drawable;

public class Bullet implements Drawable {
	
	private static final int BULLET_SPEED = -15;
	private static final int OVAL_SIZE = 10;
	private double vY;
	private double x, y;
	private BufferedImage bullet;
	
	public Bullet(double x, double y, int bulletLevel) {
		this.x = x;
		this.y = y;
		vY = BULLET_SPEED;
		if (bulletLevel == 1) {
			try {
				bullet = ImageIO.read(new File("src/img/bullet0.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (bulletLevel == 2) {
			try {
				bullet = ImageIO.read(new File("src/img/bullet1.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (bulletLevel == 3) {
			try {
				bullet = ImageIO.read(new File("src/img/bullet2.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(bullet, (int)x, (int)y, OVAL_SIZE, OVAL_SIZE, null);
	}
	
	public double getvY() {
		return vY;
	}
	
	public void setvY(double vY) {
		this.vY = vY;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void timePassed() {
		handleLocation();
	}

	private void handleLocation() {
		y += vY;
	}
}

