import javafx.geometry.*;
import javafx.scene.image.*;

/**
 * Represents water on the map. The player can only go through water if they possess flippers. Otherwise they will die
 * upon entering.
 *
 * @author Victor Cai
 * @version 1.0
 */
public class Water extends Obstacle {
    private static final ItemType REQUIRED_ITEM;
    private static final Image IMAGE;

    static {
        REQUIRED_ITEM = ItemType.FLIPPERS;
        IMAGE = new Image("Resources/Images/water.png");
    }

    /**
     * Creates a water cell.
     *
     * @param location The coordinates of the cell on the grid.
     */
    public Water(Point2D location) {
        super(location, IMAGE, REQUIRED_ITEM);
    }

    /**
     * Override the isDeadly method as Water is deadly.
     */
    @Override
    public boolean isDeadly() {
        return true;
    }

    /**
     * Checks whether a player is safe from Water
     *
     * @param player the player instance whose safety will be checked
     */
    @Override
    public boolean playerIsSafe(Player player) {
        return player.getInventory().hasFlippers();
    }
}
