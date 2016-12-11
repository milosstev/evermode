package view.property;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JLabel;

/**
	String koji mo�e se mo�e pro�iriti sa vi�e linija.
*/
public class TextArea implements Cloneable, Serializable 
{

	/**
	    Konstrui�e prazan, centriran, normalne veli�ine vi�elinijski string, koji nije podvu�en.
    */
	public TextArea() 
	{
		text = "";
		justification = CENTER;
		size = NORMAL;
		underlined = false;
	}

	/**
	    Postavlja vrijednost tekstualnog podru�ja.
	    @param newValue je text vi�elinijskog stringa
    */
	public void setText(String newValue) {
		text = newValue;
		setLabelText();
	}

	/**
	    Uzima vrijednoost tekstualnog podru�ja.
	    @return vra�a tekst vi�elinijskog stringa
    */
	public String getText() {
		return text;
	}

	/**
	    Postavlja vrijednost poravnatog podru�ja.
	    @param newValue je poravnanje (justification), jedno od mogu�ih LEFT, CENTER, RIGHT
    */
	public void setJustification(int newValue) {
		justification = newValue;
		setLabelText();
	}

	/**
	    Uzima vrijednost poravnatog podru�ja.
	    @return vra�a poravnanje, jedno od mogu�ih LEFT, CENTER, RIGHT
    */
	public int getJustification() {
		return justification;
	}

	/**
	    Uzima vrijednost podvu�enog podru�ja.
	    @return vra�a true ako e tekst podvu�en
    */
	public boolean isUnderlined() {
		return underlined;
	}

	/**
	    Uzima vrijednost podvu�enog podru�ja.
	    @param newValue je podvu�eni tekst
    */
	public void setUnderlined(boolean newValue) {
		underlined = newValue;
		setLabelText();
	}

	/**
	    Postavlja vrijednost veli�ine imovine.
	    @param newValue je veli�ina, jedna od mogu�ih: SMALL, NORMAL, LARGE
    */
	public void setSize(int newValue) {
		size = newValue;
		setLabelText();
	}

	/**
	    Uzima vrijednost veli�ine imovine.
	    @return vra�a veli�inu, jedna od mogu�ih: SMALL, NORMAL, LARGE
   */
	public int getSize() {
		return size;
	}

	public String toString() {
		return text.replace('\n', '|');
	}

	private void setLabelText() {
		StringBuffer prefix = new StringBuffer();
		StringBuffer suffix = new StringBuffer();
		StringBuffer htmlText = new StringBuffer();
		prefix.append("&nbsp;");
		suffix.insert(0, "&nbsp;");
		if (underlined) {
			prefix.append("<u>");
			suffix.insert(0, "</u>");
		}
		if (size == LARGE) {
			prefix.append("<font size=\"+1\">");
			suffix.insert(0, "</font>");
		}
		if (size == SMALL) {
			prefix.append("<font size=\"-1\">");
			suffix.insert(0, "</font>");
		}
		htmlText.append("<html>");
		StringTokenizer tokenizer = new StringTokenizer(text, "\n");
		boolean first = true;
		while (tokenizer.hasMoreTokens()) {
			if (first)
				first = false;
			else
				htmlText.append("<br>");
			htmlText.append(prefix);
			htmlText.append(tokenizer.nextToken());
			htmlText.append(suffix);
		}
		htmlText.append("</html>");

		// replace any < that are not followed by {u, i, b, tt, font, br} with
		// &lt;

		List dontReplace = Arrays.asList(new String[] { "u", "i", "b", "tt",
				"font", "br" });

		int ltpos = 0;
		while (ltpos != -1) {
			ltpos = htmlText.indexOf("<", ltpos + 1);
			if (ltpos != -1
					&& !(ltpos + 1 < htmlText.length() && htmlText
							.charAt(ltpos + 1) == '/')) {
				int end = ltpos + 1;
				while (end < htmlText.length()
						&& Character.isLetter(htmlText.charAt(end)))
					end++;
				if (!dontReplace.contains(htmlText.substring(ltpos + 1, end)))
					htmlText.replace(ltpos, ltpos + 1, "&lt;");
			}
		}

		label.setText(htmlText.toString());
		if (justification == LEFT)
			label.setHorizontalAlignment(JLabel.LEFT);
		else if (justification == CENTER)
			label.setHorizontalAlignment(JLabel.CENTER);
		else if (justification == RIGHT)
			label.setHorizontalAlignment(JLabel.RIGHT);
	}

	/**
	    Uzima ograni�eni pravougaonik za ovaj vi�elinijski string.
	    @param g2 je grafi�ki sadr�aj
	    @return vra�a ograni�eni pravougaonik (sa koordinatama gornjeg lijevog ugla (0,0))
    */
	public Rectangle2D getBounds(Graphics2D g2) {
		if (text.trim().length() == 0)
			return new Rectangle2D.Double();
		// setLabelText();
		Dimension dim = label.getPreferredSize();
		return new Rectangle2D.Double(0, 0, dim.getWidth(), dim.getHeight());
	}


	   /**
	      Isrctava ovaj vi�elinijski string unutar zadatog pravougaonika.
	      @param g2 je grafi�ki sadr�aj
	      @param r je pravougaonik unutar koga se postavlja ovaj vi�elinijski string (niz)
	   */
	public void draw(Graphics2D g2, Rectangle2D r) {
		// setLabelText();
		label.setFont(g2.getFont());
		label.setBounds(0, 0, (int) r.getWidth(), (int) r.getHeight());
		g2.translate(r.getX(), r.getY());
		label.paint(g2);
		g2.translate(-r.getX(), -r.getY());
	}

	public Object clone() {
		try {
			TextArea cloned = (TextArea) super.clone();
			cloned.label = new JLabel();
			cloned.setLabelText();
			return cloned;
		} catch (CloneNotSupportedException exception) {
			return null;
		}
	}

	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;
	public static final int LARGE = 3;
	public static final int NORMAL = 4;
	public static final int SMALL = 5;

	private static final int GAP = 3;

	private String text;
	private int justification;
	private int size;
	private boolean underlined;

	private transient JLabel label = new JLabel();
}
