package data;

/**
 * Class for integer coordinates.
 * @author tommy
 *
 */
public class Coordinate {
	
	private int x;
	private int y;
	
	/**
	 * Create a new <code>Coordinate</code>.
	 * @param x x-value of the coordinate
	 * @param y y-value of the coordinate
	 */
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Sets the x-value of the coordinate.
	 * @param x x-value of the coordinate
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * Returns the x-value of the coordinate.
	 * @return x-value
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Sets the y-value of the coordinate.
	 * @param y y-value of the coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * Returns the y-value of the coordinate.
	 * @return y-value
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Returns a string representation of this coordinate.
	 * @return string representation
	 */
	public String getString() {
		return "(" + x + "," + y + ")";
	}

}
