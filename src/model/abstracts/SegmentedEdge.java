package model.abstracts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JLabel;

import model.LineArrow;
import model.LineStyle;

/**
	Ivica koja je sa�injena od vi�e linijskih segmenata.
*/
public abstract class SegmentedEdge extends ShapeEdge 
{
	/**
    	Konstrui�e ivicu bez ikakvih ukrasa.
    */
	public SegmentedEdge() {
		lineStyle = LineStyle.SOLID;
		startArrowHead = LineArrow.NONE;
		endArrowHead = LineArrow.NONE;
		startLabel = "";
		middleLabel = "";
		endLabel = "";
	}

	/**
	    Postavlja osobinu stila linije.
	    @param newValue je nova vrijednost.
    */
	public void setLineStyle(LineStyle newValue) {
		lineStyle = newValue;
	}

	/**
	    Uzima osobinu stila linije.
	    @return vra�a stil linije
    */
	public LineStyle getLineStyle() {
		return lineStyle;
	}

	/**
	    Postavlja po�etak strelice.
	    @param newValue je nova vrijednost
    */
	public void setStartArrowHead(LineArrow newValue) {
		startArrowHead = newValue;
	}

	/**
	    Uzima posjed po�etka strelice.
	    @return vra�a stil po�etka strelice
    */
	public LineArrow getStartArrowHead() {
		return startArrowHead;
	}

	/**
    	Postavlja kraj posjeda strelice.
    	@param newValue je nova vrijednost
	 */
	public void setEndArrowHead(LineArrow newValue) {
		endArrowHead = newValue;
	}

	/**
    	Uzima posjed kraja strelice.
    	@return vra�a stil kraja strelice
   */
	public LineArrow getEndArrowHead() {
		return endArrowHead;
	}

	/**
    	Postavlja posjed po�etne etikete(labele).
    	@param newValue je nova vrijednost
    */
	public void setStartLabel(String newValue) {
		startLabel = newValue;
	}

	 /**
    	Uzima posjed po�etne etikete.
    	@return vra�a etiketu sa po�etka ivice
    */
	public String getStartLabel() {
		return startLabel;
	}

	/**
    	Postavlja posjed srednje etikete.
    	@param newValue je nova vrijednost
    */
	public void setMiddleLabel(String newValue) {
		middleLabel = newValue;
	}

	/**
    	Uzima posjed srednje etikete.
    	@return vra�a etiketu sa sredine ivice
	 */
	public String getMiddleLabel() {
		return middleLabel;
	}

	/**
    	Postavlja posjed krajnje etikete (labele).
    	@param newValue je nova vrijednost
    */
	public void setEndLabel(String newValue) {
		endLabel = newValue;
	}

	/**
    	Uzima posjed krajnje labele-etikete.
    	@return vra�a etiketu sa kraja ivice
    */
	public String getEndLabel() {
		return endLabel;
	}

	/**
    	Crtanje ivice.
    	@param g2 je grafi�ki sadr�aj
    */
	public void draw(Graphics2D g2) {
		ArrayList points = getPoints();

		Stroke oldStroke = g2.getStroke();
		g2.setStroke(lineStyle.getStroke());
		g2.draw(getSegmentPath());
		g2.setStroke(oldStroke);
		startArrowHead.draw(g2, (Point2D) points.get(1),
				(Point2D) points.get(0));
		endArrowHead.draw(g2, (Point2D) points.get(points.size() - 2),
				(Point2D) points.get(points.size() - 1));

		drawString(g2, (Point2D) points.get(1), (Point2D) points.get(0),
				startArrowHead, startLabel, false);
		drawString(g2, (Point2D) points.get(points.size() / 2 - 1),
				(Point2D) points.get(points.size() / 2), null, middleLabel,
				true);
		drawString(g2, (Point2D) points.get(points.size() - 2),
				(Point2D) points.get(points.size() - 1), endArrowHead,
				endLabel, false);
	}

	/**
    	Crtanje stringa.
    	@param g2 je grafi�ki sadr�aj
    	@param p je krajnja ta�ka segmenta du� koga se crta string
    	@param q je druga krajnja ta�ka segmenta du� koga se crta string
    	@param s je string koji se crta
    	@param center ima vrijednost true ako je string centriran du� segmenta
	 */
	private static void drawString(Graphics2D g2, Point2D p, Point2D q,
			LineArrow arrow, String s, boolean center) {
		if (s == null || s.length() == 0)
			return;
		label.setText("<html>" + s + "</html>");
		label.setFont(g2.getFont());
		Dimension d = label.getPreferredSize();
		label.setBounds(0, 0, d.width, d.height);

		Rectangle2D b = getStringBounds(g2, p, q, arrow, s, center);

		Color oldColor = g2.getColor();
		g2.setColor(g2.getBackground());
		g2.fill(b);
		g2.setColor(oldColor);

		g2.translate(b.getX(), b.getY());
		label.paint(g2);
		g2.translate(-b.getX(), -b.getY());
	}

	/**
	    Izra�unava ta�ku vezivanja za crtanje stringa.
	    @param g2 je grafi�ki sadr�aj
	    @param p je krajnja ta�ka segmenta du� koga se crta string
	    @param q je druga krajnja ta�ka segmenta du� koga se crta string
	    @param b su granice stringa koji se crta
	    @param center ima vrijednost true ako je string centriran du� segmenta
	    @return vra�a ta�ku na kojoj se crta string
    */
	private static Point2D getAttachmentPoint(Graphics2D g2, Point2D p,
			Point2D q, LineArrow arrow, Dimension d, boolean center) {
		final int GAP = 3;
		double xoff = GAP;
		double yoff = -GAP - d.getHeight();
		Point2D attach = q;
		if (center) {
			if (p.getX() > q.getX()) {
				return getAttachmentPoint(g2, q, p, arrow, d, center);
			}
			attach = new Point2D.Double((p.getX() + q.getX()) / 2,
					(p.getY() + q.getY()) / 2);
			if (p.getY() < q.getY())
				yoff = -GAP - d.getHeight();
			else if (p.getY() == q.getY())
				xoff = -d.getWidth() / 2;
			else
				yoff = GAP;
		} else {
			if (p.getX() < q.getX()) {
				xoff = -GAP - d.getWidth();
			}
			if (p.getY() > q.getY()) {
				yoff = GAP;
			}
			if (arrow != null) {
				Rectangle2D arrowBounds = arrow.getPath(p, q).getBounds2D();
				if (p.getX() < q.getX()) {
					xoff -= arrowBounds.getWidth();
				} else {
					xoff += arrowBounds.getWidth();
				}
			}
		}
		return new Point2D.Double(attach.getX() + xoff, attach.getY() + yoff);
	}


	/**
	    Izra�unava obim stringa koji je nacrtan du� segmenta.
	    @param g2 je grafi�ki sadr�aj
    	@param p je krajnja ta�ka segmenta du� koga se crta string
    	@param q je druga krajnja ta�ka segmenta du� koga se crta string
	    @param s je string za crtanje
	    @param center ima vrijednost true ako je string centriran du� segmenta
	    @return vra�a pravougaonik koji zatvara string
    */
	private static Rectangle2D getStringBounds(Graphics2D g2, Point2D p,
			Point2D q, LineArrow arrow, String s, boolean center) {
		if (g2 == null)
			return new Rectangle2D.Double();
		if (s == null || s.equals(""))
			return new Rectangle2D.Double(q.getX(), q.getY(), 0, 0);
		label.setText("<html>" + s + "</html>");
		label.setFont(g2.getFont());
		Dimension d = label.getPreferredSize();
		Point2D a = getAttachmentPoint(g2, p, q, arrow, d, center);
		return new Rectangle2D.Double(a.getX(), a.getY(), d.getWidth(),
				d.getHeight());
	}

	public Rectangle2D getBounds(Graphics2D g2) {
		ArrayList points = getPoints();
		Rectangle2D r = super.getBounds(g2);
		r.add(getStringBounds(g2, (Point2D) points.get(1),
				(Point2D) points.get(0), startArrowHead, startLabel, false));
		r.add(getStringBounds(g2, (Point2D) points.get(points.size() / 2 - 1),
				(Point2D) points.get(points.size() / 2), null, middleLabel,
				true));
		r.add(getStringBounds(g2, (Point2D) points.get(points.size() - 2),
				(Point2D) points.get(points.size() - 1), endArrowHead,
				endLabel, false));
		return r;
	}

	public Shape getShape() {
		GeneralPath path = getSegmentPath();
		ArrayList points = getPoints();
		path.append(startArrowHead.getPath((Point2D) points.get(1),
				(Point2D) points.get(0)), false);
		path.append(endArrowHead.getPath(
				(Point2D) points.get(points.size() - 2),
				(Point2D) points.get(points.size() - 1)), false);
		return path;
	}

	private GeneralPath getSegmentPath() {
		ArrayList points = getPoints();

		GeneralPath path = new GeneralPath();
		Point2D p = (Point2D) points.get(points.size() - 1);
		path.moveTo((float) p.getX(), (float) p.getY());
		for (int i = points.size() - 2; i >= 0; i--) {
			p = (Point2D) points.get(i);
			path.lineTo((float) p.getX(), (float) p.getY());
		}
		return path;
	}

	public Line2D getConnectionPoints() {
		ArrayList points = getPoints();
		return new Line2D.Double((Point2D) points.get(0),
				(Point2D) points.get(points.size() - 1));
	}

	 /**
	    Dobija ta�ke uglova(�o�kove) ove segmentne ivice linije.
	    @return vra�a niz objekata iz Point2D koji sadr�e ta�ke uglova.
    */
	public abstract ArrayList getPoints();

	private LineStyle lineStyle;
	private LineArrow startArrowHead;
	private LineArrow endArrowHead;
	private String startLabel;
	private String middleLabel;
	private String endLabel;

	private static JLabel label = new JLabel();
}
