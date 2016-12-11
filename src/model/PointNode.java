package model;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import model.abstracts.AbstractNode;

/**
	Nevidljivi �vor koji je kori�ten u paleti za crtanje ivice
	a u bilje�kama slu�i kao krajnja ta�ka �vora konektora. 
*/
public class PointNode extends AbstractNode 
{

	/**
    	Konstrui�e ta�ku �vora sa koordinatama (0, 0)
    */
	public PointNode() 
	{
		point = new Point2D.Double();
	}

	public void draw(Graphics2D g2) {
	}

	public void translate(double dx, double dy) {
		point.setLocation(point.getX() + dx, point.getY() + dy);
	}

	public boolean contains(Point2D p) {
		final double THRESHOLD = 5;
		return point.distance(p) < THRESHOLD;
	}

	public Rectangle2D getBounds() {
		return new Rectangle2D.Double(point.getX(), point.getY(), 0, 0);
	}

	public Point2D getConnectionPoint(Direction d) {
		return point;
	}

	private Point2D point;
}
