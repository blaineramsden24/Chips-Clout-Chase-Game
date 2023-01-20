import javafx.scene.image.*;

/**
 * Represents a pair of flippers.
 *
 * @author Victor Cai
 * @version 1.0
 */
public class Flippers extends CollectableItem {
    private static final ItemType TYPE;
    private static final Image IMAGE;

    static {
        TYPE = ItemType.FLIPPERS;
        IMAGE = new Image("Resources/Images/flippers.png");
    }

    /**
     * Creates a pair of flippers.
     */
    public Flippers() {
        super(IMAGE, TYPE);
    }
}
