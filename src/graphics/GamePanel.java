package graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import balls.Ball;
import events.BallListener;
import events.BulletListener;
import events.CannonListener;
import events.EventHandler;
import events.ShootBulletEvent;
import events.addBallEvent;
import events.addCannonEvent;
import game.Bullet;
import game.Cannon;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements BulletListener, BallListener, CannonListener {
	private List<Drawable> objectsToDraw;
	private BufferedImage image, image2;
	
	public GamePanel(List<Ball> balls, Cannon cannon) throws IOException {
		setLayout(null);
		EventHandler handler = EventHandler.getInstance();
		handler.addBulletListener(this);
		handler.addBallListener(this);
		handler.addCannonListener(this);
		image = ImageIO.read(new File("src/img/backgroundImg.jpg"));
		image2 = ImageIO.read(new File("src/img/stopBackground.png"));
		objectsToDraw = new ArrayList<Drawable>();
		objectsToDraw.add(cannon);
		for (Ball ball : balls) {
			objectsToDraw.add(ball);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {			
		super.paintComponent(g);
		g.drawImage(image, 0, 0, GameGraphics.getScreenWidth(), GameGraphics.getScreenHeight(), null);
		for (Drawable drawable : new ArrayList<Drawable>(objectsToDraw)) {
			drawable.draw(g);
		}
		if (GameGraphics.suspended)
			g.drawImage(image2, 0, 0, null);
	}

	@Override
	public void shootBullet(ShootBulletEvent event) {
		objectsToDraw.add(event.getBullet());
	}
	
	@Override
	public void addBall(addBallEvent event) {
		objectsToDraw.add(event.getBall());
	}
	
	public void removeBullet(Bullet b) {
		objectsToDraw.remove(b);
	}
	
	public void removeBall(Ball b) {
		objectsToDraw.remove(b);
	}
	
	public void addCannon(addCannonEvent event) {
		objectsToDraw.add(event.getCannon());
	}
	
	public void removeCannon(Cannon cannon) {
		objectsToDraw.remove(cannon);
	}
}
