package balls;

import java.awt.Color;

public class BonusBall extends Ball {

	public BonusBall(int points, Side startSide, double y, int level) {
		super(points, startSide, y, level);
		this.color = new Color(255, 103, 233);
		this.level = 1;
	}
}
