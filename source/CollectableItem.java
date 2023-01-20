import javafx.scene.image.*;

/**
 * Represents an item that can be placed on the map or in the player's inventory,
 *
 * @author Victor Cai
 * @version 1.0
 */
public abstract class CollectableItem {
    private final ItemType type;
    protected Image image;

    /**
     * Creates a collectable item.
     *
     * @param image Specifies how this item should look.
     * @param type The type of this item.
     */
    public CollectableItem(Image image, ItemType type) {
        this.image = image;
        this.type = type;
    }

    /**
     * Gets the image that represents the item.
     *
     * @return The item's image.
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Gets the type of the item
     * @return The type of the item.
     */
    public ItemType getType() {
        return this.type;
    }
}
