import javafx.scene.image.*;

/**
 * Represents a token.
 *
 * @author Victor Cai
 * @version 1.0
 */
public class Token extends CollectableItem {
    private static final ItemType TYPE;
    private static final Image IMAGE;

    static {
        TYPE = ItemType.TOKEN;
        IMAGE = new Image("Resources/Images/token.png");
    }

    /**
     * Creates a token.
     */
    public Token() {
        super(IMAGE, TYPE);
    }
}
