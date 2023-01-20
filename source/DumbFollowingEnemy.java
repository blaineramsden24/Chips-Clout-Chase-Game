import javafx.geometry.*;
import javafx.scene.image.*;

/**
 * DumbFollowingEnemy tries to go in a straight line towards the player.
 * 
 * @author Nick
 */
public class DumbFollowingEnemy extends Enemy {

    private static final String SIMPLE_NAME = "changeme";
	private static final Image ENEMY_IMAGE;

	static {
		ENEMY_IMAGE = new Image("Resources/Images/enemy_dumb.png");
	}

	/**
	 * @param location
	 */
	public DumbFollowingEnemy(Point2D location) {
		super(location, ENEMY_IMAGE, SIMPLE_NAME);
		// TODO Auto-generated constructor stub
	}

	/**
	 * DumbFollowingEnemy just tries to move closer to the player regardless of if
	 * it can pass through cells, if there is something in the way it will just not
	 * move.
	 */
	public void nextMove() {

		if (this.getLocation() == Engine.getCurrentLevel().getPlayer().getLocation()) {
			System.out.println("Printed");
		} else {
			Point2D playerLoc = Engine.getCurrentLevel().getPlayer().getLocation();
			Point2D enemyLoc = this.getLocation();

			double playerX = playerLoc.getX();
			double playerY = playerLoc.getY();

			double enemyX = enemyLoc.getX();
			double enemyY = enemyLoc.getY();

			double xDist = Math.abs(playerX - enemyX);
			double yDist = -Math.abs(playerY - enemyY);
			Direction xDir = (enemyX < playerX ? Direction.RIGHT : Direction.LEFT);
			Direction yDir = (enemyY > playerY ? Direction.UP : Direction.DOWN);
			double error = xDist + yDist;

			if (2 * error - yDist == 0 && xDist - 2 * error == 0) {

			} else if (2 * error - yDist > xDist - 2 * error) {
				error += yDist;
				if (Engine.getCurrentLevel().checkMove(this, xDir)) {
					move(getPointInDirection(this.location, xDir));
				}
			} else {
				error += xDist;
				if (Engine.getCurrentLevel().checkMove(this, yDir)) {
					move(getPointInDirection(this.location, yDir));
				}
			}
		}
	}
	
}
