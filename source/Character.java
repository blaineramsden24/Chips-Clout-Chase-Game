import javafx.geometry.Point2D;
import javafx.scene.image.Image;


/**
 * Character class, has basic methods for Player and Enemy.
 * @author Nick
 */
public abstract class Character {
	
	protected Point2D location;
	protected Image image;


    /**
     * Abstract Character Constructor.
     * @param location is the location of the character.
     * @param image is the image of the character.
     */
    public Character(Point2D location, Image image) {
        this.location = location;
        this.image = image;
    }

    /**
     * Returns the characters location.
     * @return location
     */
    public Point2D getLocation() {
        return location;
    }

    /**
     * Gets the image of the character.
     * @return the image of the character
     */
    public Image getImage() {
		return this.image;
    }
    
    /**
     * Sets the location of the character.
     * @param location the character is to be set to.
     */
    public void setLocation(Point2D location) {
		this.location = location;
	}

	/**
	 * Sets the location of the character.
	 * @param x the x coordinate the character should be set to
	 * @param y the y coordinate the character should be set to
	 */
	public void setLocation(int x, int y) {
    	this.location = new Point2D(x, y);
	}

	/**
	 * Modifies the location of the character based off the x and y offsets.
	 * @param xModifier the modifier for the x value
	 * @param yModifier the modifier for the y value
	 */
	public void setRelativeLocation(int xModifier, int yModifier) {
		this.location = this.location.add(xModifier, yModifier);
	}

    /**
     * Moves the character.
     * @param newLoc the location the character is to be moved to.
     */
    public void move(Point2D newLoc) {
		Engine.getCurrentLevel().characterMove(this, newLoc);
    }

	/**
	 * Moves the character.
	 * @param direction the direction the character is to move in.
	 */
	public void move(Direction direction) {
		Engine.getCurrentLevel().characterMove(this, direction);
	}
    
    /**
     * Gets the Point of the cell in a certain direction from the character.
     * @param point current point of the character.
     * @param dir direction the character wants to move.
     * @return the point in the direction of the original point.
     */
	public Point2D getPointInDirection(Point2D point, Direction dir) {
    	
    	switch(dir) {
    	case UP:
    		point = point.add(0, -1.0);
    		break;
    	case RIGHT:
    		point = point.add(1.0, 0);
    		break;
    	case DOWN:
    		point = point.add(0, 1.0);
    		break;
    	case LEFT:
    		point = point.add(-1.0, 0);
    		break;
    	default:
    		break;  	
    	}
    	
    	return point;
    }
	

}
