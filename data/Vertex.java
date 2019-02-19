package data;

import java.awt.Color;

public class Vertex {

	private int     id;
	private String  name;
	private boolean isCrossing = false;
	private Edge    outEdge;
	
	// drawing
	private int       x;
	private int       y;
	private int       strechedX;
	private int       strechedY;
	private String    mappedName;
	private EnumColor color;
	
	
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
	
	void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	
	public void setMappedName(String mappedName) {
		this.mappedName = mappedName;
	}
	public String getMappedName() {
		return this.mappedName;
	}
	
	public void setColor(EnumColor color) {
		this.color = color;
	}
	public EnumColor getEnumColor() {
		return this.color;
	}
	public Color getColor() {
		return color.getColor();
	}
	
	public void setX(int x) {
		this.x = x;
	}
	public int getX() {
		return this.x;
	}
	public int getStretchedX(int factor) {
		return this.x * factor;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	public int getY() {
		return this.y;
	}
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
	 * @param isCrossing	true, iff this vertex is a crossing
	 */
	public void setIsCrossing(boolean isCrossing) {
		this.isCrossing = isCrossing;
	}
	/**
	 * Returns if this vertex represents a crossing.
	 * @return	true, iff this vertex is a crossing
	 */
	public boolean isCrossing() {
		return this.isCrossing;
	}
	
	/**
	 * Sets an outgoing edge of this vertex.
	 * @param outEdge	an outgoing edge from this vertex
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

	public int getStrechedX() {
		return strechedX;
	}

	public void setStrechedX(int strechedX) {
		this.strechedX = strechedX;
	}

	public int getStrechedY() {
		return strechedY;
	}

	public void setStrechedY(int strechedY) {
		this.strechedY = strechedY;
	}
	
}
