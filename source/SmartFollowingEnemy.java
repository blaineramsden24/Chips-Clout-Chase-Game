import java.util.ArrayList;

import javafx.geometry.*;
import javafx.scene.image.*;

/**
 * SmartFollowingEnemy follows player using an algorithm, pathfinds to the
 * player.
 *
 * @author Nick
 */
public class SmartFollowingEnemy extends Enemy {

    private static final String SIMPLE_NAME = "changeme";
	private static final Image ENEMY_IMAGE;

	static {
		ENEMY_IMAGE = new Image("Resources/Images/enemy_smart.png");
	}

	/**
	 * @param location
	 */
	public SmartFollowingEnemy(Point2D location) {
        super(location, ENEMY_IMAGE, SIMPLE_NAME);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Calculates the next move for this enemy based on A* algorithm.
	 */
	public void nextMove() {

		if (this.getLocation().getX() == Engine.getCurrentLevel().getPlayer().getLocation().getX()
				&& this.getLocation().getY() == Engine.getCurrentLevel().getPlayer().getLocation().getY()) {

		} else {


			ArrayList<Node> openList = new ArrayList<Node>();
			ArrayList<Node> closedList = new ArrayList<Node>();

			Node start = new Node(null, this.getLocation(), 0);
			start.setH(0);

			openList.add(start);

			boolean foundDest = false;
			Node finalNode = null;

			while (!openList.isEmpty() && !foundDest) {
				Node smallestF = openList.get(0);
				for (Node elem : openList) {
					if (elem.getF() < smallestF.getF()) {
						smallestF = elem;
					}
				}

				openList.remove(smallestF);

				Direction dirToAdd = Direction.UP;
				for (int i = 0; i < 4; i++) {
					if (Engine.getCurrentLevel().checkEnemyMove(smallestF.getNodeLocation(), rotate(dirToAdd, true))) {

						Point2D newPoint = getPointInDirection(smallestF.getNodeLocation(), rotate(dirToAdd, true));
						double newG = smallestF.getG() + 1.0;
						Node tempNode = new Node(smallestF, newPoint, newG);

						boolean onOpenList = false;
						boolean onClosedList = false;

						for (Node elem : openList) {
							if (elem.isPointEqual(newPoint)) {
								onOpenList = true;
							}
						}

						for (Node elem : closedList) {
							if (elem.isPointEqual(newPoint)) {
								onClosedList = true;
							}
						}

						if (newPoint.getX() == Engine.getCurrentLevel().getPlayer().getLocation().getX()
								&& newPoint.getY() == Engine.getCurrentLevel().getPlayer().getLocation().getY()) {

							if (tempNode.getNodeLocation() == this.location) {

							}

							if (openList.isEmpty() && closedList.isEmpty()) {
								finalNode = tempNode;
								finalNode.setNodeLocation(this.location);
								foundDest = true;
							} else {
								closedList.add(tempNode);
								finalNode = tempNode;
								foundDest = true;
							}


						} else if (!onClosedList) {

							if (!onOpenList) {
								openList.add(tempNode);
							} else {
								for (Node elem : openList) {
									if (elem.isPointEqual(newPoint) && elem.getF() < tempNode.getF()) {
										elem.setParent(smallestF);
										elem.setG(newG);
										elem.setH(tempNode.getH());
									}

								}
							}

						}

					}
					dirToAdd = rotate(dirToAdd, true);
				}

				closedList.add(smallestF);

			}

			if (foundDest == false) {

				move(randomPointToMoveTo());

			} else {
				Node tempNode = finalNode;
				Point2D locToMove = null;

				while (tempNode.getParent() != null) {

					locToMove = tempNode.getNodeLocation();
					tempNode = tempNode.getParent();
				}

				if (!Engine.getCurrentLevel().getCell(locToMove).isEnemyOnCell()) {
					move(locToMove);
				}
				

			}

		}

	}

	/**
	 * 
	 * @return
	 */
	private Point2D randomPointToMoveTo() {

		boolean hasMoved = false;
		Point2D locToMove = null;

		while (!hasMoved) {
			int randomInt = (int) (4.0 * Math.random());
			Level level = Engine.getCurrentLevel();

			if (randomInt == 0) {

				if (level.checkMove(this, Direction.UP)) {
					locToMove = this.getPointInDirection(this.location, Direction.UP);
					hasMoved = true;
				}
			} else if (randomInt == 1) {
				if (level.checkMove(this, Direction.RIGHT)) {
					locToMove = this.getPointInDirection(this.location, Direction.RIGHT);
					hasMoved = true;
				}
			} else if (randomInt == 2) {

				if (level.checkMove(this, Direction.DOWN)) {
					locToMove = this.getPointInDirection(this.location, Direction.DOWN);
					hasMoved = true;
				}
			} else {
				if (level.checkMove(this, Direction.LEFT)) {

					locToMove = this.getPointInDirection(this.location, Direction.LEFT);
					hasMoved = true;
				}
			}
		}
		return locToMove;
	}


}
