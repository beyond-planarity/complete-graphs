package data;

import java.awt.Color;

/**
 * Class to implement a vertex.
 * @author tommy
 *
 */
public class Vertex {

	private int     id;					// id of the vertex
	private String  name;				// name of the vertex
	private boolean isCrossing = false;	// true, if this vertex is a crossing
	private Edge    outEdge;			// an out-edge of this vertex
	
	// drawing
	private int       x;				// x-position, de Fraysseix, Pach & Pollack algorithm
	private int       y;				// y-position, de Fraysseix, Pach & Pollack algorithm
	private int       strechedX;		// stretched x-position for drawing
	private int       strechedY;		// stretched y-position for drawing
	private String    mappedName;		// name of the vertex this vertex is mapped to (if any)
	private EnumColor color;			// color of this vertex when shown in window
	
	
	/**
	 * Creates a new <code>Vertex</code> with the given id.
	 * @param id 	the id of this vertex
	 */
	public Vertex(int id) {
		this.id   = id;
		setName(getInitName());
		
		this.x = 0;
		this.y = 0;
		this.strechedX = 0;
		this.strechedY = 0;
		this.mappedName = name;
		this.color = EnumColor.SET1;
	}
	
	/**
	 * Returns the initial name of this vertex, that is the name right after creation.
	 * @return	initial name
	 */
	public String getInitName() {
		return "x" + id;
	}
	
	/**
	 * Creates a new <code>Vertex</code> with the given id.
	 * If <code>isCrossing</code>, then this vertex represents a crossing.
	 * @param id			the id of this vertex
	 * @param isCrossing	true if this vertex represents a crossing
	 */
	public Vertex(int id, boolean isCrossing) {
		this(id);
		this.isCrossing = isCrossing;
	}
	
	/**
	 * Sets the name of this vertex.
	 * @param name name of this vertex
	 */
	void setName(String name) {
		this.name = name;
	}
	/**
	 * Returns the name of this vertex.
	 * @return name of this vertex
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Sets the mapped name of this vertex.
	 * @param mappedName the new mapped name
	 */
	public void setMappedName(String mappedName) {
		this.mappedName = mappedName;
	}
	/**
	 * Returns the mapped name of this vertex.
	 * @return
	 */
	public String getMappedName() {
		return this.mappedName;
	}
	
	/**
	 * Sets the color of this vertex.
	 * @param color	new color of this vertex
	 */
	public void setColor(EnumColor color) {
		this.color = color;
	}
	/**
	 * Returns the abstract color of this vertex.
	 * @return abstract color
	 */
	public EnumColor getEnumColor() {
		return this.color;
	}
	/**
	 * Returns the color of this vertex.
	 * @return color
	 */
	public Color getColor() {
		return color.getColor();
	}
	
	/**
	 * Sets the x-position.
	 * @param x new x-position
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * Returns the x-position.
	 * @return x-position
	 */
	public int getX() {
		return this.x;
	}
	/**
	 * Returns the x-position stretched by <code>factor</code>.
	 * @param factor 	stretch factor
	 * @return			stretched x-position
	 */
	public int getStretchedX(int factor) {
		return this.x * factor;
	}
	
	/**
	 * Sets the y-position.
	 * @param y	new y-position
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * Returns the y-position.
	 * @return y-position
	 */
	public int getY() {
		return this.y;
	}
	/**
	 * Returns the y-position stretched by <code>factor</code>.
	 * @param factor 	stretch factor
	 * @return			stretched y-position
	 */
	public int getStretchedY(int factor) {
		return this.y * factor;
	}
	
		
	/**
	 * Sets the id of this vertex.
	 * @param id	the new id of this vertex
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Returns the id of this vertex.
	 * @return	id of this vertex
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Sets if this vertex represents a crossing.
	 * @param isCrossing	true, if and only if this vertex is a crossing
	 */
	public void setIsCrossing(boolean isCrossing) {
		this.isCrossing = isCrossing;
	}
	/**
	 * Returns if this vertex represents a crossing.
	 * @return	true, if and only if this vertex is a crossing
	 */
	public boolean isCrossing() {
		return this.isCrossing;
	}
	
	/**
	 * Sets an outgoing edge of this vertex.
	 * @param outEdge	an outgoing edge of this vertex
	 */
	public void setOutEdge(Edge outEdge) {
		this.outEdge = outEdge;
	}
	/**
	 * Returns an outgoing edge of this vertex.
	 * @return	an outgoing edge of this vertex
	 */
	public Edge getOutEdge() {
		return this.outEdge;
	}

	/**
	 * Returns the stretched x-position of this vertex.
	 * @return	stretched x-position
	 */
	public int getStrechedX() {
		return strechedX;
	}
	/**
	 * Sets the stretched x-position of this vertex.
	 * @param strechedX new stretched x-position
	 */
	public void setStrechedX(int strechedX) {
		this.strechedX = strechedX;
	}

	/**
	 * Returns the stretched y-position of this vertex.
	 * @return	stretched y-position
	 */
	public int getStrechedY() {
		return strechedY;
	}
	/**
	 * Sets the stretched y-position of this vertex.
	 * @param strechedY new stretched y-position
	 */
	public void setStrechedY(int strechedY) {
		this.strechedY = strechedY;
	}
	
}
