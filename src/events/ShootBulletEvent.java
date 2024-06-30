package events;

import game.Bullet;

public class ShootBulletEvent {
	private Bullet bullet;

	public ShootBulletEvent(Bullet bullet){
		this.bullet = bullet;
	}

	public Bullet getBullet() {
		return bullet;
	}

	public void setBullet(Bullet bullet) {
		this.bullet = bullet;
	}
}
