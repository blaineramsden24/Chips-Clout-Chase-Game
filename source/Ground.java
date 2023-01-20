import javafx.geometry.*;
import javafx.scene.image.*;

/**
 * Represents a ground cell. Both the player and enemies can walk through this cell.
 *
 * @author Victor Cai
 * @version 1.0
 */
public class Ground extends Cell {
    private static final Image IMAGE;

    static {
        IMAGE = new Image("Resources/Images/ground.png");
    }

    /**
     * Creates a ground cell.
     *
     * @param location The coordinates of the cell on the grid.
     */
    public Ground(Point2D location) {
        super(true, true, IMAGE, location);
    }
}
