import javafx.scene.image.*;

/**
 * Represents a coloured key.
 *
 * @author Victor Cai
 * @version 2.0
 */
public class Key extends CollectableItem {
    private static final ItemType TYPE;
    private static final Image RED_IMAGE;
    private static final Image BLUE_IMAGE;
    private static final Image GREEN_IMAGE;
    private final KeyType colour;

    static {
        TYPE = ItemType.KEY;
        RED_IMAGE = new Image("Resources/Images/key_red.png");
        GREEN_IMAGE = new Image("Resources/Images/key_green.png");
        BLUE_IMAGE = new Image("Resources/Images/key_blue.png");
    }

    /**
     * Creates a key.
     *
     * @param colour The colour of the key. <br>
     * <b>Note:</b> The colour can only be "red", "green" or "blue".
     */
    public Key(String colour) {
        super(null, TYPE);

        switch (colour) {
            case "red":
                this.colour = KeyType.RED;
                this.image = RED_IMAGE;
                break;
            case "green":
                this.colour = KeyType.GREEN;
                this.image = GREEN_IMAGE;
                break;
            case "blue":
                this.colour = KeyType.BLUE;
                this.image = BLUE_IMAGE;
                break;
            default:
                throw new IllegalArgumentException("Illegal colour: " + colour);
        }
    }

    /**
     * Gets the colour of the key
     *
     * @return The colour of the key.
     */
    public KeyType getColour() {
        return this.colour;
    }
}
