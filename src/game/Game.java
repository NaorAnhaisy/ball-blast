package game;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import balls.Ball;
import balls.Ball.Side;
import balls.BonusBall;
import events.EventHandler;
import events.addBallEvent;
import events.addCannonEvent;
import graphics.GameGraphics;
import graphics.GamePanel;

public class Game {
	
	private static final int OFFSET_TO_HIT = 40;
	private List<Ball> balls;
	private Cannon cannon, cannonHelper;
	private List<Bullet> bulletsToRemove;
	private List<Ball> ballsToRemove;
	private List<Ball> ballsToAdd;
	private static int score, sumPointsOnScreen;
	private static int bulletLevel, numBulletsInRow;
	private long lastBallTimeEntered;
	private Random rand;
	private static final int SUM_POINTS_ALLOWED_ON_SCREEN = 500;
	public static final int SUM_POINTS_TO_WIN = 10000;
	
	public Game(int bulletLevel, int numBulletsInRow) throws IOException {
		rand = new Random();
		lastBallTimeEntered = System.currentTimeMillis();
		score = 0;
		sumPointsOnScreen = 0;
		cannon = new Cannon(GameGraphics.getScreenWidth() / 2, bulletLevel, numBulletsInRow);
		cannonHelper = null;
		balls = new LinkedList<Ball>();
		bulletsToRemove = new LinkedList<Bullet>();
		ballsToRemove = new LinkedList<Ball>();
		ballsToAdd = new LinkedList<Ball>();
		addStartBalls();
	}

	public List<Ball> getBalls() {
		return balls;
	}

	public void setBalls(List<Ball> balls) {
		this.balls = balls;
	}

	public Cannon getCannon() {
		return cannon;
	}

	public void setCannon(Cannon cannon) {
		this.cannon = cannon;
	}

	private void addStartBalls() {
		balls.add(new Ball(350, Side.LEFT, 0, 1));
//		balls.add(new BonusBall(400, Side.RIGHT, 0, 1));
		balls.add(new Ball(3000, Side.RIGHT, 0, 3));
		EventHandler.getInstance().notifyAddBall(new addBallEvent(balls.get(balls.size() - 1)));
		EventHandler.getInstance().notifyAddBall(new addBallEvent(balls.get(balls.size() - 2)));
	}
	
	private void timePassed() {
		if (System.currentTimeMillis() - lastBallTimeEntered > 3000 && sumPointsOnScreen < SUM_POINTS_ALLOWED_ON_SCREEN && score < SUM_POINTS_TO_WIN) {
			addNewBall();
			lastBallTimeEntered = System.currentTimeMillis();
		}
	}
	
	private void addNewBall() {
		int points = rand.nextInt(2000) + 400;
		int level = rand.nextInt(3) + 1;
		Ball newBall = new Ball(points, Side.RIGHT, 0, level);
		balls.add(newBall);
		EventHandler.getInstance().notifyAddBall(new addBallEvent(newBall));
	}

	/**
	 * 
	 * @param panel
	 * @return 0 to continue game
	 * 		   1 - player lose
	 * 		   2 - player wins
	 */
	public int update(GamePanel panel) {
		timePassed();
		for (Bullet bullet : cannon.getBullets()) {
			bullet.timePassed();
			bulletHitBall(panel, bullet);
			if (bullet.getY() < 0) {
				deleteBullet(panel, bullet);
			}
		}
		cannon.removeBullets(bulletsToRemove);
		bulletsToRemove.clear();
		
		balls.removeAll(ballsToRemove);
		for (Ball ball : ballsToRemove) {
			panel.removeBall(ball);
		}
		ballsToRemove.clear();
		
		cannon.timePassed(cannon, cannonHelper);
		
		for (Ball ballToAdd : ballsToAdd) {
			balls.add(ballToAdd);
		}
		ballsToAdd.clear();
		
		if (score >= SUM_POINTS_TO_WIN && balls.isEmpty())
			return 2;
		for (Ball ball : balls) {
			ball.timePassed();
			if (ballHitCannon(ball, cannon) && !GameGraphics.isGameOver) {
				ball.setTxtColor(Color.RED);
				return 1;
			}
		}
		return 0;
	}
		
	private boolean ballHitCannon(Ball ball, Cannon cannon) {
	    double circleDistanceX = Math.abs(ball.getX() - cannon.getX());
	    double circleDistanceY = Math.abs(ball.getY() - cannon.getY());

	    if (circleDistanceX > ((cannon.getCannonWidth() - OFFSET_TO_HIT) / 2 + ball.getOvalSize() / 2)) { return false; }
	    if (circleDistanceY > ((cannon.getCannonHeight() - OFFSET_TO_HIT) / 2 + ball.getOvalSize() / 2)) { return false; }

	    if (circleDistanceX < ((cannon.getCannonWidth() + OFFSET_TO_HIT) / 2)) { return true; } 
	    if (circleDistanceY < ((cannon.getCannonHeight() - OFFSET_TO_HIT) / 2)) { return true; }

	    double cornerDistance_sq = Math.pow(circleDistanceX - cannon.getCannonWidth() / 2, 2) +
	                         Math.pow(circleDistanceY -  (cannon.getCannonHeight() - 40) / 2, 2);
	    return (cornerDistance_sq < Math.pow(ball.getOvalSize() / 2, 2));
	}

	private void bulletHitBall(GamePanel panel, Bullet bullet) {
		for (Ball ball : new ArrayList<Ball>(balls)) {
			double centerX = ball.getX() + ball.getOvalSize() / 2;
			double centerY = ball.getY() + ball.getOvalSize() / 2;
			if (Math.sqrt(Math.pow(bullet.getX() - centerX, 2) + Math.pow(bullet.getY() - centerY, 2)) < (ball.getOvalSize() / 2))
			{
				deleteBullet(panel, bullet);
				hitBall(panel, ball);
			}
		}
	}
	
	private void deleteBullet(GamePanel panel, Bullet bullet) {
		panel.removeBullet(bullet);
		bulletsToRemove.add(bullet);
	}
	
	private void hitBall(GamePanel panel, Ball ball) {
		ball.ballShooted();
		if (!(ball instanceof BonusBall))
			ball.changeColor();
		if (ball.getPoints() <= 0) {
			handleBallIsDown(panel, ball);
		}
	}

	private void handleBallIsDown(GamePanel panel, Ball ball) {
		
		ballsToRemove.add(ball);
		balls.remove(ball);
		panel.removeBall(ball);
		if (ball.getLevel() > 1) {
			Ball b1 = new Ball(ball.getStartPoints() / 2, ball.getX(), ball.getY(), ball.getLevel() - 1, ball);
			ballsToAdd.add(b1);
			Ball b2 = new Ball(ball.getStartPoints() / 2, ball.getX(), ball.getY(), ball.getLevel() - 1, ball);
			b2.setvX(-b1.getvX());
			ballsToAdd.add(b2);
			for (Ball ballToAdd : ballsToAdd) {
				EventHandler.getInstance().notifyAddBall(new addBallEvent(ballToAdd));
			}
		}
		
		if (ball instanceof BonusBall) {
			createNewCannon();
		}
	}

	private void createNewCannon() {
		try {
			System.out.println(cannon.getX());
			cannonHelper = new Cannon((int) cannon.getX() - 20, bulletLevel, numBulletsInRow);
		} catch (IOException e) {
			e.printStackTrace();
		}
		EventHandler.getInstance().notifyAddCannon(new addCannonEvent(cannonHelper));
	}

	public int getScore() {
		return score;
	}

	public static void addSumPointsOnScreen(int points) {
		sumPointsOnScreen += points;
	}

	public static void reduceSumPointsOnScreen(int points) {
		sumPointsOnScreen -= points;
		score += points;
	}
}
