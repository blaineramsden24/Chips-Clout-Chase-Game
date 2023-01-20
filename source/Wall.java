import javafx.geometry.*;
import javafx.scene.image.*;

/**
 * Represents a wall cell. Neither the player nor the enemies can enter this cell.
 *
 * @author Victor Cai
 * @version 1.0
 */
public class Wall extends Cell {
    private static final Image IMAGE;

    static {
        IMAGE = new Image("Resources/Images/wall.png");
    }

    /**
     * Creates a wall cell.
     *
     * @param location The coordinates of the cell on the grid.
     */
    public Wall(Point2D location) {
        super(false, false, IMAGE, location);
    }
}
