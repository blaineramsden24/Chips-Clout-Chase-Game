import javafx.geometry.*;
import javafx.scene.image.*;

/**
 * Represents a generic obstacle cell. An obstacle is a cell that requires an item to be present in the player's
 * inventory to enter.
 *
 * @author Victor Cai
 * @version 1.0
 */
public abstract class Obstacle extends Cell {
    protected final ItemType requiredItem;

    /**
     * Creates an obstacle cell.
     *
     * @param location The coordinates of the cell on the grid.
     * @param image Image shown to represent the obstacle in the game.
     * @param requiredItem The type of item needed to enter the cell.
     */
    public Obstacle(Point2D location, Image image, ItemType requiredItem) {
        super(true, false, image, location);
        this.requiredItem = requiredItem;
    }

    /**
     * Gets the type of item required.
     *
     * @return The type of item required.
     */
    public ItemType getRequiredItem() {
        return requiredItem;
    }

    /**
     * Indicates if the obstacle is deadly to the player if stepped on
     * @return true if the obstacle is deadly when stepped on
     */
    public abstract boolean isDeadly();

    /**
     * Indicates if the the player is safe from the obstacle
     * @return true if the player is safe
     */
    public abstract boolean playerIsSafe(Player player);
}
