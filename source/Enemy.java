import javafx.geometry.*;
import javafx.scene.image.*;

/**
 * Enemy class that forces the subclasses to implement nextMove().
 * @author Nick
 */
public abstract class Enemy extends Character {
    protected String simpleName;

    /**
     * @param location the location of the enemy.
     * @param image the image of the enemy.
     * @param simpleName The simplified name of the enemy.
     */
    public Enemy(Point2D location, Image image, String simpleName) {
        super(location, image);
        this.simpleName = simpleName;
        // TODO Auto-generated constructor stub
    }

    /**
     * Method to be overwritten.
     */
    public void nextMove() {
        
    }
    
    /**
	 * Rotates a direction in either clockwise or anti clockwise.
	 * @param dir the current direction
	 * @param clockwise if it wants to rotate clockwise, otherwise anti clockwise
	 * @return the new direction.
	 */
	protected Direction rotate(Direction dir, boolean clockwise) {
		switch (dir) {

		case UP:
			if (clockwise) {
				dir = Direction.RIGHT;
			} else {
				dir = Direction.LEFT;
			}
			break;
		case RIGHT:
			if (clockwise) {
				dir = Direction.DOWN;
			} else {
				dir = Direction.UP;
			}
			break;
		case DOWN:
			if (clockwise) {
				dir = Direction.LEFT;
			} else {
				dir = Direction.RIGHT;
			}
			break;
		case LEFT:
			if (clockwise) {
				dir = Direction.UP;
			} else {
				dir = Direction.DOWN;
			}
			break;
		default:
			break;

		}
		
		return dir;
	}


    /**
     * Sets the {@link #simpleName} of the enemy.
     * @param simpleName The {@link #simpleName} to be assigned to the enemy;
     */
    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    /**
     * Gets the {@link #simpleName} of the enemy.
     * @return The {@link #simpleName} of the enemy.
     */
    public String getSimpleName() {
        return this.simpleName;
    }


}
