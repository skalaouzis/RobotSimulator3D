//Κουφάκης Αλέξανδρος-Μιχαήλ, 2175
//Σεϊταρίδης Ανδρέας, 2200


package Data;

import javax.vecmath.Point2i;


public class ListData {

	private Point2i point;
	private double f;
	private ListData parent;
	
	// checks if a block is a parent of the given block
	public boolean compareData(ListData block)
	{
		if(this.getX() == block.getParent().getX() && this.getY() == block.getParent().getY())
			return true;
		else
			return false;
	}
	
	public ListData getParent() {
		return parent;
	}

	public void setParent(ListData parent) {
		this.parent = parent;
	}

	public ListData(int x, int y, ListData parent)
	{
		point = new Point2i(x, y);
		f = 0.0;
		this.parent = parent;
	}
	
	public Point2i getPoint() {
		return point;
	}
	
	public void setPoint(int x, int y) {
		//point = new Point2i(x, y);
	}
	
	public int getX()
	{
		return point.x; 
	}
	
	public int getY()
	{
		return point.y;
	}
	
	public double getF() {
		return f;
	}
	
	public void setF(double f) {
		this.f = f;
	}
	
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		if(parent != null)
		{
			b.append("(" + point.x + ", " + point.y + ")");
			b.append(" with an f of: " + f);
			b.append(" Parent: " + "(" + parent.getX() + ", " + parent.getY() + ")");
		}
		else
		{
			b.append("(" + point.x + ", " + point.y + ")");
			b.append(" with an f of: " + f);
			b.append(" No parent");
		}
		return b.toString();
	}
	
	
}
