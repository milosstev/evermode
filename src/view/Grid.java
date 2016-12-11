package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
	Koordinatna mre�a na koju ta�ke i pravougaonici mogu biti odvu�eni. Opcija odvla�enja
	pomjera ta�u do najbli�e koordinatne ta�ke.
*/
public class Grid
{

	/**
    	Konstrui�e re�etku bez po�etnih ta�aka (0,0).
    */
	public Grid() 
	{
		setGrid(0, 0);
	}

	/**
	    Postavlja koordinate u x i y pravcima.
	    @param x je ta�ka na x-osi
	    @param y je ta�ka na y-osi
    */
	public void setGrid(double x, double y) {
		gridx = x;
		gridy = y;
	}

	/**
	    Iscrtava ovu re�etku unutar pravougaonika.
	    @param g2 je grafi�ki sadr�aj
	    @param bounds je oivi�eni pravougaonik
    */
	public void draw(Graphics2D g2, Rectangle2D bounds) 
	{
		Color PALE_BLUE = new Color(0.9F, 0.8F, 0.9F);
		Color oldColor = g2.getColor();
		g2.setColor(PALE_BLUE);
		Stroke oldStroke = g2.getStroke();
		for (double x = bounds.getX(); x < bounds.getMaxX(); x += gridx)
			g2.draw(new Line2D.Double(x, bounds.getY(), x, bounds.getMaxY()));
		for (double y = bounds.getY(); y < bounds.getMaxY(); y += gridy)
			g2.draw(new Line2D.Double(bounds.getX(), y, bounds.getMaxX(), y));
		g2.setStroke(oldStroke);
		g2.setColor(oldColor);
	}

	/**
	    Povla�i ta�ku d najbli�e koordinate.
	    @param p je ta�ka za povla�enje. 
    */
	public void snap(Point2D p) {
		double x;
		if (gridx == 0)
			x = p.getX();
		else
			x = Math.round(p.getX() / gridx) * gridx;
		double y;
		if (gridy == 0)
			y = p.getY();
		else
			y = Math.round(p.getY() / gridy) * gridy;

		p.setLocation(x, y);
	}

	/**
	    Povla�i pravoguoanik do najbli�ih koordinatnih ta�aka.
	    @param r je pravougaonik za povla�enje
	*/
	public void snap(Rectangle2D r) {
	/*	double x;
		double w;
		if (gridx == 0) {
			x = r.getX();
			w = r.getWidth();
		} else {
			x = Math.round(r.getX() / gridx) * gridx;
			w = Math.ceil(r.getWidth() / (2 * gridx)) * (2 * gridx);
		}
		double y;
		double h;
		if (gridy == 0) {
			y = r.getY();
			h = r.getHeight();
		} else {
			y = Math.round(r.getY() / gridy) * gridy;
			h = Math.ceil(r.getHeight() / (2 * gridy)) * (2 * gridy);
		}

		r.setFrame(x, y, w, h);*/
	}

	private double gridx;
	private double gridy;
}
