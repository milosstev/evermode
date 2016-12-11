package model.abstracts;
/**
 Klasa koja poziva metode iz apstraktne klase AbstractEdge.
*/
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
/**
 Ivica u dijagramu.
*/
public interface Edge extends Serializable, Cloneable {
	/**
     Iscrtavanje ivice.
    */
	void draw(Graphics2D g2);
	 /**
      Testiranje da li ivica sadr�i ta�ku.
      @param aPoint imenuje ta�ku za testiranje.
      @return vra�a vrijednost true (istina) ako ivica sadr�i ta�ku(aPoint).
     */
	boolean contains(Point2D aPoint);
	/**
	    Povezivanje ove ivice na 2 �vora.
	    @param aStart po�etni �vor
	    @param anEnd zavr�ni �vor
	 */
	void connect(Node aStart, Node anEnd);
	
	/**
	    Uzima po�etni �vor.
	    @return vra�a po�etni �vor
	  */
	Node getStart();
	
	/**
	    Uzima zavr�ni �vor.
	    @return vra�a zavr�ni �vor
	 */
	Node getEnd();
	
	/**
	    Uzima ta�ke koje su definisane kao �vorovi ove ivice. 
	    @return vra�a liniju, tj. spaja 2 priklju�ene ta�ke linijom a
	 */
	Line2D getConnectionPoints();

	/**
	    Dobija najmanji prvaougaonik koji ograni�ava ovu ivicu.
	    Ograni�eni prvaougaonik sadr�i sve labele.
	    @return vra�a ograni�eni pravougaonik.
	 */
	Rectangle2D getBounds(Graphics2D g2);

	Object clone();

	public String getName();
	
	public void setName(String name);
}
