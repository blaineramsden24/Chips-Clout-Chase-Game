import javafx.geometry.*;
import javafx.scene.image.*;

/**
 * Represents the finishing point in a level.
 *
 * @author Victor Cai
 * @version 2.0
 */
public class Goal extends Cell {
    private static final Image GOAL_1_IMAGE;
    private static final Image GOAL_2_IMAGE;
    private static final Image GOAL_3_IMAGE;
    private final int variant;

    static {
        GOAL_1_IMAGE = new Image("Resources/Images/goal1.png");
        GOAL_2_IMAGE = new Image("Resources/Images/goal2.png");
        GOAL_3_IMAGE = new Image("Resources/Images/goal3.png");
    }

    /**
     * Creates a goal cell.
     *
     * @param location The coordinates of the cell on the grid.
     * @param variant The variant of the goal. This affects the goal's appearance.
     */
    public Goal(Point2D location, int variant) {
        super(true, false, null, location);

        if (variant == 1) {
            this.image = GOAL_1_IMAGE;
        } else if (variant == 2) {
            this.image = GOAL_2_IMAGE;
        } else if (variant == 3) {
            this.image = GOAL_3_IMAGE;
        } else {
            throw new IllegalArgumentException("Invalid goal variant: " + variant);
        }

        this.variant = variant;
    }

    /**
     * Gets the variant of the cell
     *
     * @return The cell's variant number
     */
    public int getVariant() {
        return this.variant;
    }
}
