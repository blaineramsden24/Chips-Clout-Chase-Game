/**
 * Represents a user profile in the game.
 *
 * @author Aleksandr Kolomijec, Victor Cai
 * @version 2.0
 */
public class Profile {
    private String[] profileInfo;

    private String profileName;
    private int highLevel;
    private int highestLevel;
    private int variant;

    /**
     * Creates a profile.
     *
     * @param profileName name of the user
     * @param highestLevel the highest level this user has completed
     * @param variant the player variant the user has chosen
     */
    public Profile(String profileName, int highestLevel, int variant) {
        this.profileName = profileName;
        this.highestLevel = highestLevel;
        this.variant = variant;

    }

    /**
     * Gets the name of the profile.
     *
     * @return the name of the profile
     */
    public String getName() {
        return profileName;
    }

    /**
     * Sets the name of the profile.
     * @param n the name to be set.
     */
    public void setName(String n) {
        profileName = n;
    }

    ///**
    // * In idea, when called, checks current game level for player, if it's not, should overwrite the current paramentr
    // * in text file. Needs to be reworked
    // *
    // * @param s
    // *
    // * @return
    // */
    //public String getHighLevel(Level s) {
    //    int curLevel = s.getLevelNumber();
    //    if (curLevel >= highLevel) {
    //        highLevel = curLevel;
    //    }
    //
    //    highestLevel = String.valueOf(highLevel);
    //    return highestLevel;
    //}

    /**
     * Gives the highest level of the player
     *
     * @return The highest level
     */
    public int getHighestLevel() {
        return this.highestLevel;
    }

    /**
     * Gives the player variant of the user.
     * @return the player variant
     */
    public int getVariant() {
        return variant;
    }

    /**
     * Represents this profile as a line of text.
     *
     * @return A space-separated string showing the profile's attributes.
     */
    public String toString() {
        return String.format("%s,%d,%d", this.profileName, this.highestLevel, this.variant);
    }


}
