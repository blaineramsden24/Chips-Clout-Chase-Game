import javafx.scene.image.*;

/**
 * Represents a pair of fire boots.
 *
 * @author Victor Cai
 * @version 1.0
 */
public class FireBoots extends CollectableItem {
    private static final ItemType TYPE;
    private static final Image IMAGE;

    static {
        TYPE = ItemType.FIRE_BOOTS;
        IMAGE = new Image("Resources/Images/fire_boots.png");
    }

    /**
     * Creates a pair of fire boots
     */
    public FireBoots() {
        super(IMAGE, TYPE);
    }
}