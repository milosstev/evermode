package model.abstracts;
/**
Klasa koja poziva metode iz apstraktne klase AbstractNode.
*/
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.List;

import model.Diagram;
import model.Direction;
import view.Grid;
/**
	�vor u dijagramu.
*/
public interface Node extends Serializable, Cloneable {
	/**
	    Iscrtavanje �vora.
	    @param parametar g2 grafi�ki element.
	 */
	void draw(Graphics2D g2);

	/**
	    Prevodi (pomjera) �vor na datu vrijednost.
	    @param dx iznos za prevod u x-smijeru
	    @param dy iznos za prevod u y-smijeru
	 */
	void translate(double dx, double dy);
	
	/**
	    Testiranje da li �vor sadr�i ta�ku.
	    @param parametar aPoint imenuje ta�ku za testiranje
	    @return vra�a vrijednost true (istina) ako ovaj �vor sadr�i aPoint
	 */
	boolean contains(Point2D aPoint);

	 /**
	    Uzima najbolju ta�ku povezivanja za povezivanje ovog �vora sa drugim �vorom. 
	    Ovo bi trebalo da bude ta�ka na granici oblika ovog �vora.
	    @param d smijer od centra ograni�enog pravougaonika prema granicama.
	    @return vra�a preporu�enu ta�ku povezivanja.
	  */
	Point2D getConnectionPoint(Direction d);

	 /**
	    Nabavlja grani�ni pravougaonik oblika ovog �vora.
	    @return vra�a ograni�eni pravougaonik
	  */
	Rectangle2D getBounds();

	 /**
	    Dodaje ivicu koja nastaje u ovome �voru.
	    @param p je ta�ka koju korisnik uzima za po�etnu ta�ku.
	    @param e je ivica koja se dodaje
	    @return vra�a true ako je ivica dodata
    */
	boolean addEdge(Edge e, Point2D p1, Point2D p2);

	/**
	    Dodaje �vor kao dijete �vor ovoga �vora.
	    @param n je dijete �vor
	    @param p je ta�ka od koje je ovaj �vor dodat
	    @return vra�a true ako ovaj �vor prihvata dati �vor kao dijete
	 */
	boolean addNode(Node n, Point2D p);

	/**
	    Obavje�tava ovaj �vor da je ivica ukonjena
	   	@param g je ambijent dijagram(graf)
	    @param e je ivica koja je uklonjena
	 */
	void removeEdge(Diagram g, Edge e);

	 /**
	    Obavje�tava ovaj �vor da je �vor uklonjen.Notifies this node that a node is being removed.
	    @param g je ambijent graf
	    @param n je �vor koji je uklonjen
	  */
	void removeNode(Diagram g, Node n);

	/**
	    Postavlja �vor i njegovu djecu.
	    @param g je ambijent graf
	    @param g2 je grafi�ki sadr�aj
	    @param grid je koordinatno mjesto za naglo pomjeranje
	 */
	void layout(Diagram g, Graphics2D g2, Grid grid);

	/**
	    Dobija roditelja ovog �vora.
	    @return vra�a roditeljski �vor, ili null vrijednost ako �vor nema roditelja.
	 */
	Node getParent();

	/**
	    Postavlja roditelja ovog �vora.
	    @param node je �vor roditelj, ili null ako �vor nema roditelja
	 */
	void setParent(Node node);

	/**
	    Uzima potomak ovoga �vora.Gets the children of this node.
	    @return vra�a nemodofikovanu listu potomaka
    */
	List getChildren();

	/**
	    Dodaje �vor potomak(dijete).Adds a child node.
	    @param index predstavlja poziciju na koju dodajemo potomka
	    @param node predstavlja potomak �vor koji se dodaje
   */
	void addChild(int index, Node node);

	/**
	    Uklanjanje potomak �vora.
	    @param node predstavlja dijete(potomka) koji se uklanja.
	 */
	void removeChild(Node node);

	Object clone();
}
