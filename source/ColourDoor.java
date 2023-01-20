import javafx.geometry.*;
import javafx.scene.image.*;

/**
 * Represents a colour door. It requires a key of the correct colour to open.
 *
 * @author Victor Cai, Jozef Bohosian
 * @version 3.0
 */
public class ColourDoor extends Obstacle {
    private static final ItemType REQUIRED_ITEM;
    private static final Image RED_IMAGE;
    private static final Image BLUE_IMAGE;
    private static final Image GREEN_IMAGE;
    private final KeyType colour;
    private boolean unlocked;


    static {
        REQUIRED_ITEM = ItemType.KEY;
        RED_IMAGE = new Image("Resources/Images/door_red.png");
        GREEN_IMAGE = new Image("Resources/Images/door_green.png");
        BLUE_IMAGE = new Image("Resources/Images/door_blue.png");
    }

    /**
     * Creates a colour door.
     *
     * <br>
     * <b>Note:</b> The colour can only be "red", "green" or "blue".
     *
     * @param location The coordinates of the cell on the grid.
     * @param colour The colour of the correct key.
     */
    public ColourDoor(Point2D location, String colour, boolean unlocked) {
        super(location, null, REQUIRED_ITEM);

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
                throw new IllegalArgumentException("Invalid colour: " + colour);
        }

        this.unlocked = unlocked;
    }

    /**
     * Gets the colour of the required key.
     *
     * @return The colour of the correct key.
     */
    public KeyType getColour() {
        return this.colour;
    }

    /**
     * Doors will never kill the player, so this method will always return false.
     *
     * @return false
     */
    @Override
    public boolean isDeadly() {
        return false;
    }

    /**
     * Doors will never kill the player, so the player is always safe.
     *
     * @param player The player of the game
     *
     * @return true
     */
    @Override
    public boolean playerIsSafe(Player player) {
        return true;
    }

    /**
     * Indicates if the door is unlocked.
     *
     * @return A boolean indicating if the door is unlocked
     */
    public boolean isUnlocked() {
        return this.unlocked;
    }

    /**
     * Unlocks the door.
     */
    public void unlock() {
        this.unlocked = true;
    }
}
