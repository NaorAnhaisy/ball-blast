package events;

import game.Cannon;

public class addCannonEvent {
	private Cannon cannon;

	public addCannonEvent(Cannon cannon){
		this.cannon = cannon;
	}

	public Cannon getCannon() {
		return cannon;
	}

	public void setCannon(Cannon cannon) {
		this.cannon = cannon;
	}
}
