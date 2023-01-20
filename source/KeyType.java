import javafx.scene.paint.*;

/**
 * An enum to represent the colour of a key.
 *
 * @author Victor Cai, Jozef Bohosian
 * @version 1.0
 */
public enum KeyType {
    RED, GREEN, BLUE;

    private Color colour;
    private String string;

    static {
        RED.colour = Color.RED;
        GREEN.colour = Color.GREEN;
        BLUE.colour = Color.MEDIUMBLUE;

        RED.string = "red";
        GREEN.string = "green";
        BLUE.string = "blue";
    }

    public Color getColour() {
        return colour;
    }

    public String toString() {
        return string;
    }
}
