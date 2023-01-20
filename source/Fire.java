import javafx.geometry.*;
import javafx.scene.image.*;

/**
 * Represents fire on the map. The player can only walk through fire if they possess fire boots. Otherwise they will die
 * upon entering.
 *
 * @author Victor Cai
 * @version 1.0
 */
public class Fire extends Obstacle {
    private static final ItemType REQUIRED_ITEM;
    private static final Image IMAGE;

    static {
        REQUIRED_ITEM = ItemType.FIRE_BOOTS;
        IMAGE = new Image("Resources/Images/fire.png");
    }

    /**
     * Creates a fire cell.
     *
     * @param location The coordinates of the cell on the grid.
     */
    public Fire(Point2D location) {
        super(location, IMAGE, REQUIRED_ITEM);
    }

    /**
     * Override the isDeadly method as Fire is deadly.
     */
    @Override
    public boolean isDeadly() {
        return true;
    }

    /**
     * Checks whether a player is safe from Fire
     *
     * @param player the player instance whose safety will be checked
     */
    @Override
    public boolean playerIsSafe(Player player) {
        return player.getInventory().hasFireBoots();
    }
}
