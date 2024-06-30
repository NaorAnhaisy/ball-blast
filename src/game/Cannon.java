package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import graphics.Drawable;
import graphics.GameGraphics;
import graphics.SpritePosition;

public class Cannon implements Drawable {
	
	private static final int FPS_ADDAPTION = 6;
	private static final int SPRITE_HEIGHT = 128;
	private static final int SPRITE_WIDTH = 128;
	private static final double ACCELERATION = 1;
	private static final double MAX_SPEED = 15;
	private static final double FRICTION_COEFFICIENT = 0.9;
	private int cannonHeigth = 70;
	private int cannonWidth = 80;
	private double vX;
	private double x, y;
	private List<Bullet> bullets;
	private BufferedImage image, wheel1, wheel2;
	private int numOfImage;
	private boolean isShotting = false, isAlive;
	private final static SpritePosition[] EXPLODE = {new SpritePosition(0, 0, SPRITE_WIDTH, SPRITE_HEIGHT), 
			new SpritePosition(0, 128, SPRITE_WIDTH, SPRITE_HEIGHT),
			new SpritePosition(0, 256, SPRITE_WIDTH, SPRITE_HEIGHT),
			new SpritePosition(0, 384, SPRITE_WIDTH, SPRITE_HEIGHT),
			
			new SpritePosition(128, 0, SPRITE_WIDTH, SPRITE_HEIGHT), 
			new SpritePosition(128, 128, SPRITE_WIDTH, SPRITE_HEIGHT),
			new SpritePosition(128, 256, SPRITE_WIDTH, SPRITE_HEIGHT),
			new SpritePosition(128, 384, SPRITE_WIDTH, SPRITE_HEIGHT),
			
			new SpritePosition(256, 0, SPRITE_WIDTH, SPRITE_HEIGHT), 
			new SpritePosition(256, 128, SPRITE_WIDTH, SPRITE_HEIGHT),
			new SpritePosition(256, 256, SPRITE_WIDTH, SPRITE_HEIGHT),
			new SpritePosition(256, 384, SPRITE_WIDTH, SPRITE_HEIGHT),
			
			new SpritePosition(384, 0, SPRITE_WIDTH, SPRITE_HEIGHT), 
			new SpritePosition(384, 128, SPRITE_WIDTH, SPRITE_HEIGHT),
			new SpritePosition(384, 256, SPRITE_WIDTH, SPRITE_HEIGHT),
			new SpritePosition(384, 384, SPRITE_WIDTH, SPRITE_HEIGHT)};
	private BufferedImage explodeImg;
	private int explodeIndex = 0;
	private int bulletLevel, numBulletsInRow;
	
	public Cannon(int x, int bulletLevel, int numBulletsInRow) throws IOException {
		
		explodeImg = ImageIO.read(new File("src/img/Explosion.png"));
		image = ImageIO.read(new File("src/img/cannon3.png"));
	    wheel2 = ImageIO.read(new File("src/img/wheel2.png"));
	    wheel1 = ImageIO.read(new File("src/img/wheel1.png"));
	    numOfImage = 0;
	    isAlive = true;
	    
	    this.bulletLevel = bulletLevel;
	    this.numBulletsInRow = numBulletsInRow;
		this.x = x;
		this.y = GameGraphics.getScreenHeight() - cannonHeigth - 90;
		System.out.println(this.x);
		bullets = new LinkedList<Bullet>();
		vX = 0;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		//g.fillRect((int) x, (int) y, CANNON_WIDTH, CANNON_HEIGHT);
		g.drawImage(image, (int) x, (int) y, cannonWidth, cannonHeigth, null);
//		if (numOfImage % 2 == 0) {
//			g.drawImage(wheel2, (int) x + 46, (int) y + 37, 35, 35, null);
//			g.drawImage(wheel2, (int) x + 2, (int) y + 37, 35, 35, null);
//		}
//		else {
//			g.drawImage(wheel1, (int) x + 46, (int) y + 37, 35, 35, null);
//			g.drawImage(wheel1, (int) x + 2, (int) y + 37, 35, 35, null);
//		}
		
		if (isShotting) {
			explodeIndex %= (EXPLODE.length) * FPS_ADDAPTION; 
			g.drawImage(getFromSpritePosition(EXPLODE[explodeIndex++ / FPS_ADDAPTION]), (int) x, (int) y - 30, cannonWidth, cannonHeigth, null);
			isShotting = false;
		}
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

	public int getCannonHeight() {
		return cannonHeigth;
	}

	public int getCannonWidth() {
		return cannonWidth;
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public void setBullets(List<Bullet> bullets) {
		this.bullets = bullets;
	}
	
	public int getNumBulletsInRow() {
		return numBulletsInRow;
	}

	public void timePassed(Cannon master, Cannon solider) {
		if (solider != null)
			solider.followTheMaster(master, this);
		master.handleSpeed();
		master.checkBorders();
		master.handleFriction();
	}
	
	private void checkBorders() {
		int limit1 = GameGraphics.getScreenWidth() - cannonWidth + 25;
		int limit2 = cannonWidth - 110;
		if (x >= limit1) {
			x = limit1;
			vX = 0;
		}
		if (x <= limit2) {
			x = limit2;
			vX = 0;
		}
	}

	private void handleSpeed() {
		x += vX;
	}

	private void handleFriction() {
		vX *= FRICTION_COEFFICIENT;
	}
	
	public void runRight() {
		numOfImage = (numOfImage + 1) % 5;
		vX += ACCELERATION;
		if (vX > MAX_SPEED)
			vX = MAX_SPEED;
	}
	
	public void runLeft() {
		numOfImage = (numOfImage + 1) % 5;
		vX -= ACCELERATION;
		if (vX < -MAX_SPEED)
			vX = -MAX_SPEED;
	}

	public void shoot() {
		isShotting  = true;
		switch (numBulletsInRow) {
		case 1:
			bullets.add(new Bullet(x + cannonWidth / 2 - 5, y, bulletLevel));
			break;
		case 2:
			bullets.add(new Bullet(x + cannonWidth / 2 - 15, y, bulletLevel));
			bullets.add(new Bullet(x + cannonWidth / 2 + 5, y, bulletLevel));			
			break;
		case 3:
			bullets.add(new Bullet(x + cannonWidth / 2 - 20, y, bulletLevel));
			bullets.add(new Bullet(x + cannonWidth / 2 - 5, y, bulletLevel));
			bullets.add(new Bullet(x + cannonWidth / 2 + 10, y, bulletLevel));	
			break;
		default:
			break;
		}
	}
	
	public void removeBullet(Bullet b) {
		bullets.remove(b);
	}
	
	public void removeBullets(List<Bullet> b) {
		bullets.removeAll(b);
	}
	
	public Image getFromSpritePosition(SpritePosition p) {
		return explodeImg.getSubimage(p.getX(), p.getY(), p.getWidth(), p.getHeight());
	}
	
	public void followTheMaster(Cannon master, Cannon soldier) {
		soldier.setX(master.getX() - 70);
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
}
