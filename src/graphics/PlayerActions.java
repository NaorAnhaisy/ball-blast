package graphics;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import events.ShootBulletEvent;
import game.Cannon;
import events.EventHandler;

public class PlayerActions extends KeyAdapter {

	private Cannon cannon;
	private List<Integer> keysPressed;

	public PlayerActions(Cannon cannon) {
		this.cannon = cannon;
		keysPressed = new ArrayList<Integer>();
	}

	public void updatePlayerActions() {
		if (keysPressed.contains(KeyEvent.VK_LEFT)) {
			cannon.runLeft();
		}
		if (keysPressed.contains(KeyEvent.VK_RIGHT)) {
			cannon.runRight();
		}
		if (keysPressed.contains(KeyEvent.VK_UP)) {
			cannon.shoot();
			for (int i = 1; i <= cannon.getNumBulletsInRow(); i++)
				EventHandler.getInstance().notifyShootBullet(new ShootBulletEvent(cannon.getBullets().get(cannon.getBullets().size() - i)));
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!keysPressed.contains(e.getKeyCode())) {
			keysPressed.add(e.getKeyCode());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keysPressed.remove(Integer.valueOf(e.getKeyCode()));
	}
}
