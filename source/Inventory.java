import java.util.*;

/**
 * The inventory class to manage items held by the player.
 *
 * @author Nick
 */
public class Inventory {

    private boolean fireBoots;
    private boolean flippers;
    private ArrayList<Key> keyList = new ArrayList<Key>();
    private int tokenCount;

    /**
     *
     */
    public Inventory() {
        this.fireBoots = false;
        this.flippers = false;
        this.tokenCount = 0;
    }

    /**
     * @return
     */
    public boolean hasFireBoots() {
        return fireBoots;
    }

    /**
     * @return
     */
    public boolean hasFlippers() {
        return flippers;
    }

    /**
     * @param colour
     *
     * @return
     */
    public boolean hasKey(KeyType colour) {
        boolean hasKey = false;

        for (Key key : keyList) {
            if (key.getColour() == colour) {
                hasKey = true;
            }
        }

        return hasKey;
    }

    /**
     * @param tokensNeeded
     *
     * @return
     */
    public boolean hasTokens(int tokensNeeded) {
        return this.tokenCount - tokensNeeded >= 0;
    }

    /**
     * @param colour
     */
    public void removeKey(KeyType colour) {
        boolean searching = true;
        int i = 0;
        while (searching) {
            if (keyList.get(i).getColour() == colour) {
                keyList.remove(i);
                searching = false;
            }

            i++;

            if (i == keyList.size()) {
                searching = false;
            }
        }
    }

    public void removeTokens(int amount) {
        this.tokenCount -= amount;
    }

    public void setFireBoots() {
        this.fireBoots = true;
    }

    public void setFlippers() {
        this.flippers = true;
    }

    public void addKey(Key key) {
        this.keyList.add(key);
    }

    public void addToken() {
        this.tokenCount++;
    }

    public void addCollectable(CollectableItem item) {
        if(item instanceof Key) {
            addKey((Key) item);
        } else if (item instanceof Token) {
            addToken();
        }
    }

    public ArrayList<Key> getKeyList() {
        return this.keyList;
    }

    public int getTokenCount() {
        return this.tokenCount;
    }
    

    
}