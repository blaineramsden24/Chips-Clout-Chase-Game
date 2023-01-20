import javafx.geometry.Point2D;

public class Node {
	
	private Node parent;
	private Point2D nodeLocation;
	private double g;
	private double h;
	
	public Node(Node parent, Point2D nodeLocation, double g) {
		this.parent = parent;
		this.nodeLocation = nodeLocation;
		this.g = g;
		this.h = this.calcH(nodeLocation, Engine.getCurrentLevel().getPlayer().getLocation());
		
	}
	
	public double getF() {
		return (this.getG() + this.getH());
	}

	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Point2D getNodeLocation() {
		return nodeLocation;
	}
	
	public void setNodeLocation(Point2D point) {
		this.nodeLocation = point;
	}

	public double getG() {
		return g;
	}

	public double getH() {
		return h;
	}
	
	public void setH(double h) {
		this.h = h;
	}
	
	public void setG(double h) {
		this.g = g;
	}
	
	public double calcH(Point2D cur, Point2D end) {
    	
    	return (Math.abs((cur.getX() - end.getX()) + Math.abs(cur.getY() - end.getY())));
    	
    }	
	
	public boolean isPointEqual(Point2D pointCompare) {
		if (this.getNodeLocation().getX() == pointCompare.getX()
				&& this.getNodeLocation().getY() == pointCompare.getY()) {
			return true;
		} else {
			return false;
		}
	}
}
