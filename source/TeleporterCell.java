import javafx.geometry.*;
import javafx.scene.image.*;

/**
 * A teleporter cell allows the player to immediately travel to its destination (another {@code Teleporter} Cell) upon
 * entering.
 *
 * @author Victor Cai
 * @version 1.0
 */
public class TeleporterCell extends Cell {
    private static final Image IMAGE;
    private TeleporterCell destination;

    static {
        IMAGE = new Image("Resources/Images/teleporter.png");
    }

    /**
     * Creates a teleporter cell.
     *
     * @param location The coordinates of the cell on the grid.
     */
    public TeleporterCell(Point2D location) {
        super(true, false, IMAGE, location);
    }

    /**
     * Gives the cell that this one connects to.
     *
     * @return the destination teleporter cell.
     */
    public TeleporterCell getDestination() {
        return this.destination;
    }

    /**
     * Sets the destination cell of the teleporter.
     *
     * @param destination The teleporter cell to connect to.
     */
    public void setDestination(TeleporterCell destination) {
        this.destination = destination;
    }

}
