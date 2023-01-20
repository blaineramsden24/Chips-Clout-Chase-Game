import javafx.geometry.*;
import javafx.scene.image.*;

/**
 * Represents the player in the gameplay
 * @author Nick Robertson, Victor Cai
 * @version 2.0
 */
public class Player extends Character {
    private static final Image PLAYER_1_IMAGE = new Image("Resources/Images/player1.png");
    private static final Image PLAYER_2_IMAGE = new Image("Resources/Images/player2.png");
    private final int variant;
    private Inventory inventory;
    private Direction movingDirection;
    private boolean isDead = false;

    /**
     * The player constructor.
     *
     * @param location the location of the player
     * @param variant The variant of this player (affects their appearance)
     */
    public Player(Point2D location, int variant) {
        super(location, null);

        if (variant == 1) {
            this.image = PLAYER_1_IMAGE;
        } else if (variant == 2) {
            this.image = PLAYER_2_IMAGE;
        } else {
            throw new IllegalArgumentException("Invalid player type: " + variant);
        }
        this.variant = variant;

        this.inventory = new Inventory();
    }

    /**
     * Gives which variant the player is
     *
     * @return the number representing the variant
     */
    public int getVariant() {
        return variant;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Adds the item specified to its inventory.
     *
     * @param item the item to be added to the inventory
     */
    public void pickUpItem(CollectableItem item) {

        if (item != null) {
            switch (item.getType()) {
                case KEY:
                    inventory.addKey((Key) item);
                    break;
                case TOKEN:
                    inventory.addToken();
                    break;
                case FLIPPERS:
                    inventory.setFlippers();
                    break;
                case FIRE_BOOTS:
                    inventory.setFireBoots();
                    break;
                default:
                    break;
            }
        }


    }
    
    /**
     * It will return if the player has a certain item.
     * @param item The item to see if its in the inventory.
     * @param numberNeeded The number of the items needed, normally just 1 but for tokens can be more.
     * @return if the player has the item required.
     */
    public boolean hasItem(CollectableItem item, int numberNeeded) {
    	
    	boolean hasItem = false;
    	
    	switch (item.getType()) {
        case KEY:
        	Key key = (Key) item;
            hasItem = inventory.hasKey(key.getColour());
            break;
        case TOKEN:
            hasItem = inventory.hasTokens(numberNeeded);
            break;
        case FLIPPERS:
            hasItem = inventory.hasFlippers();
            break;
        case FIRE_BOOTS:
            hasItem = inventory.hasFireBoots();
            break;
        default:
            break;
    }
    	
    	return hasItem;
    	
    }

    /**
     * Gets the {@link Direction} the player is currently moving in.
     * @return The {@link Direction} the player is currently moving in.
     */
    public Direction getMovingDirection() {
        return this.movingDirection;
    }

    /**
     * Sets the {@link Direction} the player is currently moving in.
     * @param direction The {@link Direction} the player is currently moving in.
     */
    public void setMovingDirection(Direction direction) {
        this.movingDirection = direction;
    }

    /**
     * Remove a key of a specific colour.
     *
     * @param colour The colour of the key to be removed.
     */
    public void useKey(KeyType colour) {
        this.inventory.removeKey(colour);
    }

    /**
     * Removes a specified number of tokens from the inventory.
     *
     * @param tokensNeeded how many tokens needed to be removed
     */
    public void useTokens(int tokensNeeded) {
        this.inventory.removeTokens(tokensNeeded);
    }

    /**
     * Gets whether the player is dead.
     * @return Whether the player is dead.
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Sets whether the player is dead.
     * @param dead Whether the player is dead.
     */
    public void setDead(boolean dead) {
        isDead = dead;
    }
}
