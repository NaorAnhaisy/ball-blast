package balls;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.Game;
import graphics.Drawable;
import graphics.GameGraphics;
import graphics.SpritePosition;
import other.StaticFunctions;

public class Ball implements Drawable {
	
	public static enum Side { LEFT, RIGHT };
	
	private static final int FPS_ADDAPTION = 1;
	private static final int SPRITE_SIZE = 80;
	private static final double GRAVITY = 0.1;
	private static final int BALL_HIT_DAMAGE = 4;
	private int points, startPoints;
	private double y, x, vX, vY;
	protected Color color, txtColor;
	protected int level, ovalSize;
	private final static SpritePosition[] SMOKE = {
			new SpritePosition(0, 0, SPRITE_SIZE, SPRITE_SIZE),
			new SpritePosition(SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE),
			new SpritePosition(SPRITE_SIZE * 2, 0, SPRITE_SIZE, SPRITE_SIZE),
			
			new SpritePosition(0, SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE),
			new SpritePosition(SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE),
			new SpritePosition(SPRITE_SIZE * 2, SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE),
			
			new SpritePosition(0, SPRITE_SIZE * 2, SPRITE_SIZE, SPRITE_SIZE),
			new SpritePosition(SPRITE_SIZE, SPRITE_SIZE * 2, SPRITE_SIZE, SPRITE_SIZE),
			new SpritePosition(SPRITE_SIZE * 2, SPRITE_SIZE * 2, SPRITE_SIZE, SPRITE_SIZE),
			};
	private BufferedImage smokeEffect;
	private int smokeIndex;
	private boolean isOnFloor, isEntering;
	
	public Ball(int points, Side startSide, double y, int level) {
		
		try {
			smokeEffect = ImageIO.read(new File("src/img/smokeSprite.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		isOnFloor = false;
		isEntering = true;
		smokeIndex = 0;
		if (startSide != null)
			Game.addSumPointsOnScreen(points * level);
		this.points = points;
		this.startPoints = points;
		this.level = level;
		this.y = y;
		txtColor = Color.WHITE;
		vY = 0;
		
		switch (level) {
		case 1:
			ovalSize = 75;
			vX = 3.3;
			break;
		case 2:
			ovalSize = 110;
			vX = 2.7;
			break;
		case 3:
			ovalSize = 130;
			vX = 2;
			break;
		default:
			break;
		}
		
		if (startSide != null) {
			if (startSide == Side.LEFT) {
				x = -ovalSize;
			} else {
				x = GameGraphics.getScreenWidth() + ovalSize;
				vX = -vX;
			}
		}
		
		if (points <= 500)
			color = new Color(0, 255, 0);
		else if (points <= 1500)
			color = new Color(255, 0, 0);
		else 
			color = new Color(255, 0, 255);
	}
	
	public Ball(int points, double x, double y, int level, Ball fatherBall) {
		this(points, null, y, level);
		this.x = x;
		isEntering = false;
	}

	@Override
	public void draw(final Graphics g) {
		g.setColor(this.color);
		g.fillOval((int)x, (int)y, ovalSize, ovalSize);
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 21));
	    g.setColor(txtColor);
	    
		g.drawString(StaticFunctions.returnBigNumbersInShortcat(points), (int)(x) + ovalSize / 2 - 15, (int)(y) + ovalSize / 2);
		if (isOnFloor) {
			smokeIndex %= (SMOKE.length) * FPS_ADDAPTION;
			g.drawImage(getFromSpritePosition(SMOKE[smokeIndex++ / FPS_ADDAPTION]), (int) x, (int) GameGraphics.getScreenHeight() - 120, SPRITE_SIZE, SPRITE_SIZE, null);
			if ((int) ((SMOKE.length) * FPS_ADDAPTION) == smokeIndex) 
				isOnFloor = false;
		}
	}
	
	public int getPoints() {
		return points;
	}
	
	public int getStartPoints() {
		return startPoints;
	}

	public double getvX() {
		return vX;
	}

	public void setvX(double vX) {
		this.vX = vX;
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
	
	public double getY() {
		return y;
	}

	public Color getColor() {
		return color;
	}
	
	public Color getTxtColor() {
		return txtColor;
	}

	public void setTxtColor(Color txtColor) {
		this.txtColor = txtColor;
	}
	
	public int getLevel() {
		return level;
	}

	public int getOvalSize() {
		return ovalSize;
	}

	public void ballShooted() {
		this.points -= BALL_HIT_DAMAGE;
		Game.reduceSumPointsOnScreen(BALL_HIT_DAMAGE);
	}
	
	public void timePassed() {
		checkBorders();
		handleLocation();
	}

	private void handleLocation() {
		x += vX;
		if (!isEntering)
			y += vY;
	}
	
	private void checkBorders() {
		if (isEntering) {
			if (x > 0 && x + ovalSize < GameGraphics.getScreenWidth())
				isEntering = false;
			else
				return;
		}
		//System.out.println("Checking borders");
		if (y + ovalSize <= 0)
			vY = -vY;
		else if (y + ovalSize + 100 >= GameGraphics.getScreenHeight()) {
			isOnFloor = true;
			vY = -vY;
		}
		else 
			vY += GRAVITY;
		if (x < 0 || x + ovalSize > GameGraphics.getScreenWidth()) {
			vX = -vX;
		}
	}

	public void changeColor() {
		int r = this.color.getRed();
		int g = this.color.getGreen();
		int b = this.color.getBlue();
		if (points <= 1000) {
			if (g < 254) {
				g++;
			}
			else if (r > 1) {
				r--;
			}
		} else if (points <= 2000) {
			if (b > 0) {
				b--;
			}
		}
		this.color = new Color(r, g, b);
	}
	
	/**
     * Checks the color integer components supplied for validity.
     * @param r the Red component
     * @param g the Green component
     * @param b the Blue component
	 * @return true if the value is out of range.
	 */
    private static boolean testColorValueRange(int r, int g, int b) {
    	
        if (r <= 0 || r >= 255 || g <= 0 || g >= 255 || b <= 0 || b >= 255)
            return true;
        return false;
    }
	
	public Image getFromSpritePosition(SpritePosition p) {
		return smokeEffect.getSubimage(p.getX(), p.getY(), p.getWidth(), p.getHeight());
	}
}
