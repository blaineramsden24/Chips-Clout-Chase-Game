import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * WallFollowingEnemy follows the wall as if it had one hand on it at all times.
 * 
 * @author Nick
 */
public class WallFollowingEnemy extends Enemy {

	private static final String SIMPLE_NAME = "Drunkard";
	private static final Image ENEMY_IMAGE;
	private Direction direction;
	private boolean movedClockwise = false;

	static {
		ENEMY_IMAGE = new Image("Resources/Images/enemy_wall.png");
	}

	/**
	 * The constructor for the wall follow enemy needs a location and the direction of the wall it is following.
	 * @param location the location of the WallFollowingEnemy
	 * @param dirOfWallToFollow the direction of the wall it is going to follow is.
	 */
	public WallFollowingEnemy(Point2D location, Direction dirOfWallToFollow) {
		super(location, ENEMY_IMAGE, SIMPLE_NAME);
		this.setDirection(rotate(dirOfWallToFollow, false));


	}

	/**
	 * Calculates the next move, keeping the wall to its right.
	 */
	public void nextMove() {
		
		Level currentLvl = Engine.getCurrentLevel();

		//Checks cell to rotation once clockwise
		if (currentLvl.checkEnemyMove(this.location, this.direction.getClockwiseDirection())) {

			if(movedClockwise) {
				Direction closest = currentLvl.getClosestEnemyBoundary(this.location);
				if(currentLvl.checkMove(this, closest)) {
					move(getPointInDirection(location, closest));
				} else {
					direction = closest.getAntiClockwiseDirection();
					move(getPointInDirection(location, direction));
				}
			}else if (currentLvl.checkMove(this, this.direction.getClockwiseDirection())) {
				this.direction = this.direction.getClockwiseDirection();
				move(getPointInDirection(this.location, this.direction));
				movedClockwise = true;
			}	
			
		//Check cell in direction of enemy current facing
		} else if (currentLvl.checkEnemyMove(this.location, this.direction)) {
			
			if (currentLvl.checkMove(this, this.direction)) {
				move(getPointInDirection(this.location, this.direction));
				movedClockwise = false;
			}
			 
			
		//Check cell in rotation once anti-clockwise
		} else if (currentLvl.checkEnemyMove(this.location, this.direction.getAntiClockwiseDirection())) {
			
			if (currentLvl.checkMove(this, this.direction.getAntiClockwiseDirection())) {
				this.direction = this.direction.getAntiClockwiseDirection();
				move(getPointInDirection(this.location, this.direction));
				movedClockwise = false;
			}	
			
		//Check cell in rotation twice anti-clockwise
		} else if (currentLvl.checkEnemyMove(this.location, this.direction.getOppositeDirection())) {
			
			if (currentLvl.checkMove(this, this.direction.getOppositeDirection())) {
				this.direction = this.direction.getOppositeDirection();
				move(getPointInDirection(this.location, this.direction));
				movedClockwise = false;
			}
		}

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