import javafx.geometry.*;
import javafx.scene.image.*;

/**
 * Represents a cell that contains a {@link CollectableItem}. Upon collecting the item, this cell turns into a ground
 * cell.
 *
 * @author Victor Cai
 * @version 1.0
 */
public class CollectableCell extends Cell {
    private static final Image GROUND_IMAGE;
    private CollectableItem item;

    static {
        GROUND_IMAGE = new Image("Resources/Images/ground.png");
    }

    /**
     * Creates a collectable cell.
     *
     * @param location The coordinates of the cell on the grid.
     * @param item The {@link CollectableItem} this cell contains.
     */
    public CollectableCell(Point2D location, CollectableItem item) {
        super(true, false, item.getImage(), location);
        this.item = item;
    }

    /**
     * Gets the item contained in this cell.
     *
     * @return The item in the cell.
     */
    public CollectableItem getItem() {
        return this.item;
    }

    /**
     * Collects (removes) the item in the cell.
     *
     * @return The item contained in this cell.
     */
    public CollectableItem collectItem() {
        CollectableItem collected = this.getItem();
        this.item = null;
        this.enemyPassable = true;
        this.image = GROUND_IMAGE;
        return collected;
    }
}
