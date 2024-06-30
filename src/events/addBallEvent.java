package events;

import balls.Ball;

public class addBallEvent {
	private Ball ball;

	public addBallEvent(Ball ball){
		this.ball = ball;
	}

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}
}
