package model.abstracts;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
	Klasa koja pretpostavlja da ivica mo�e dati svoj oblik i onda uzima prednost �injenice 
	da se zadr�avanje testiranja mo�e izvr�iti udaranjem oblika ja�im udarom.
	U idealnom slu�aju, trebalo bi da mo�ete povuci isti oblik koji se koristi za testiranje za�titnog. 
*/
public abstract class ShapeEdge extends AbstractEdge 
{

	/**
	    Vra�a putanju koja bi mogla biti zna�ajna za crtanje ove ivice.
	    Putanja ne uklju�uje tipove strelice ili etikete. 
	    @return vra�a putanju du� ivice
    */
	public abstract Shape getShape();

	public Rectangle2D getBounds(Graphics2D g2) {
		return getShape().getBounds();
	}

	public boolean contains(Point2D aPoint) {
		final double MAX_DIST = 3;

		Line2D conn = getConnectionPoints();
		if (aPoint.distance(conn.getP1()) <= MAX_DIST
				|| aPoint.distance(conn.getP2()) <= MAX_DIST)
			return false;

		Shape p = getShape();
		BasicStroke fatStroke = new BasicStroke((float) (2 * MAX_DIST));
		Shape fatPath = fatStroke.createStrokedShape(p);
		return fatPath.contains(aPoint);
	}
}
