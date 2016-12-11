package model;

import java.awt.geom.Point2D;

/**
	Ova klasa opisuje pravac u 2D ravni. Pravac je vektor du�ine 1 sa uglom izme�u 0
	(uklju�uju�i) i 360 stepeni (isklju�uju�i). Tako�e, postoji "izrod" pravac du�ine 0. 
*/
public class Direction 
{

	/**
	    Konstrui�e pravac(normalizovan do du�ine 1).
	    @param dx je x-vrijednost pravca
	    @param dy je odgovaraju�a y-vrijednost pravca
    */
	public Direction(double dx, double dy) {
		x = dx;
		y = dy;
		double length = Math.sqrt(x * x + y * y);
		if (length == 0)
			return;
		x = x / length;
		y = y / length;
	}

	/**
	    Konstrui�e pravac izme�u 2 ta�ke.
	    @param p je po�etna ta�ka
	    @param q je krajnja ta�ka
    */
	public Direction(Point2D p, Point2D q) 
	{
		this(q.getX() - p.getX(), q.getY() - p.getY());
	}

	
	/**
	    Pretvara ovaj pravac u ugao.
	    @param angle je ugao u stepenima
	*/
	public Direction turn(double angle) 
	{
		double a = Math.toRadians(angle);
		return new Direction(x * Math.cos(a) - y * Math.sin(a), x * Math.sin(a)
				+ y * Math.cos(a));
	}

	
	/**
	    Uzima x-komponentu ovog pravca
	    @return vra�a x-komponentu (izme�u -1 i 1)
    */
	public double getX() 
	{
		return x;
	}

	
	/**
	    Uzima y-komponentu ovog pravca
	    @return vra�a y-komponentu (izme�u -1 i 1)
    */
	public double getY() 
	{
		return y;
	}

	private double x;
	private double y;

	public static final Direction NORTH = new Direction(0, -1);
	public static final Direction SOUTH = new Direction(0, 1);
	public static final Direction EAST = new Direction(1, 0);
	public static final Direction WEST = new Direction(-1, 0);
}
