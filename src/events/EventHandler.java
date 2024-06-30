package events;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {
	private List<BulletListener> shootBulletListeners;
	private List<BallListener> addBallListeners;
	private List<CannonListener> addCannonListeners;
	
	private static EventHandler instance = null;
	
	public static EventHandler getInstance() {
		if (instance == null) {
			instance = new EventHandler();
		}
		return instance;
	}
	
	public EventHandler() {
		shootBulletListeners = new ArrayList<BulletListener>();
		addBallListeners = new ArrayList<BallListener>();
		addCannonListeners = new ArrayList<CannonListener>();
	}
	
	public void addBulletListener(BulletListener listener) {
		shootBulletListeners.add(listener);
	}
	
	public void addBallListener(BallListener listener) {
		addBallListeners.add(listener);
	}
	
	public void addCannonListener(CannonListener listener) {
		addCannonListeners.add(listener);
	}
	
	public void notifyShootBullet(ShootBulletEvent shootBulletEvent) {
		for (BulletListener shootBulletListeners : shootBulletListeners) {
			shootBulletListeners.shootBullet(shootBulletEvent);
		}
	}
	
	public void notifyAddBall(addBallEvent addBallEvent) {
		for (BallListener addBallListeners : addBallListeners) {
			addBallListeners.addBall(addBallEvent);
		}
	}
	
	public void notifyAddCannon(addCannonEvent addCannonEvent) {
		for (CannonListener addCannonListeners : addCannonListeners) {
			addCannonListeners.addCannon(addCannonEvent);
		}
	}
}
