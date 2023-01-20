import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * @author Nick
 */
public class StraightLineEnemy extends Enemy {

	private static final String SIMPLE_NAME = "changeme";
	private static final Image ENEMY_IMAGE;
	private Direction direction;

	static {
		ENEMY_IMAGE = new Image("Resources/Images/enemy_straight_line.png");
	}

	/**
	 * StraightLineEnemy constructor.
     *
	 * @param location  is the enemies location.
	 * @param direction is the enemies direction.
	 */
	public StraightLineEnemy(Point2D location, Direction direction) {
        super(location, ENEMY_IMAGE, SIMPLE_NAME);
		this.setDirection(direction);
	}

	/**
	 * StraightLineEnemys next move checks if it can go in its current direction and
	 * if not it reverses and goes the other direction.
	 */
	public void nextMove() {
        Point2D locToCheck = this.getPointInDirection(this.location, this.direction);
        if (Engine.getCurrentLevel().checkMove(this, this.direction)) {
            move(getPointInDirection(this.location, this.direction));
        } else if (Engine.getCurrentLevel().checkMove(this, reverseDirection(this.direction))) {
            this.direction = reverseDirection(this.direction);
            move(getPointInDirection(this.location, this.direction));
        }
	}

	/**
	 * Reverses the direction.
     *
	 * @param dir direction currently going in.
	 * @return direction after reversal.
	 */
	public Direction reverseDirection(Direction dir) {
		switch (dir) {
            case UP:
                dir = Direction.DOWN;
                break;
            case RIGHT:
                dir = Direction.LEFT;
                break;
            case DOWN:
                dir = Direction.UP;
                break;
            case LEFT:
                dir = Direction.RIGHT;
                break;
            default:
                break;
		}

		return dir;
	}

	/**
	 * Gets the direction.
     *
	 * @return the direction.
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Sets the direction.
     *
	 * @param direction to be set
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}