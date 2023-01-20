import javafx.geometry.*;
import javafx.scene.image.*;

/**
 * Represents a token door. The player can only enter a token door if they possess the correct number of tokens.
 * Otherwise they will be prevented from entering this cell.
 *
 * @author Victor Cai, Jozef Bohosian
 * @version 2.0
 */
public class TokenDoor extends Obstacle {
    private static final ItemType REQUIRED_ITEM;
    private static final Image IMAGE; // Number of tokens will be represented using a text box
    private final int numTokens;
    private boolean unlocked;

    static {
        REQUIRED_ITEM = ItemType.TOKEN;
        IMAGE = new Image("Resources/Images/token_door.png");
    }

    /**
     * Creates a token door.
     *
     * @param location The coordinates of the cell on the grid.
     * @param numTokens The number of tokens required to open this door.
     */
    public TokenDoor(Point2D location, int numTokens, boolean unlocked) {
        super(location, IMAGE, REQUIRED_ITEM);
        this.numTokens = numTokens;
        this.unlocked = unlocked;
    }

    /**
     * Gets the number of tokens needed to open the door.
     *
     * @return The number of tokens required.
     */
    public int getNumTokens() {
        return this.numTokens;
    }

    /**
     * Gets whether the obstacle is deadly to the player.
     *
     * @return Whether the the obstacle is deadly to the player.
     */
    @Override
    public boolean isDeadly() {
        return false;
    }

    /**
     * Gets the number of tokens needed to open the door.
     *
     * @return Whether the player is safe.
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
