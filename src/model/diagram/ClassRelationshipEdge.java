package model.diagram;

import java.util.ArrayList;

import model.LineType;
import model.abstracts.SegmentedEdge;

/**
	Ivica koja je oblikovana kao linija sa najvi�e 3 segmenta strelice.
*/
public class ClassRelationshipEdge extends SegmentedEdge 
{

	/**
    	Konstrui�e pravu ivicu.
    */
	public ClassRelationshipEdge() 
	{
		lineType = LineType.STRAIGHT;
	}

	/**
	    Postavlja posjed bentStyle-a (savijenog stila)
	    @param newValue je bent style
    */
	public void setBentStyle(LineType newValue) 
	{
		lineType = newValue;
	}

	/**
	    Uzima posjed bentStyle
	    @return vra�a bent style
    */
	public LineType getBentStyle() 
	{
		return lineType;
	}

	public ArrayList getPoints() {
		return lineType.getPath(getStart().getBounds(), getEnd().getBounds());
	}

	private LineType lineType;


}
