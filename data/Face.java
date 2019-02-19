package data;


public class Face {

	private int id;
	private Edge incidentEdge;	
	
	
	public Face(int id) {
		this.id = id;
	}
	
	public Face(int id, Edge incidentEdge) {
		this(id);
		this.incidentEdge = incidentEdge;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}
	
	public void setIncidentEdge(Edge incidentEdge) {
		this.incidentEdge = incidentEdge;
	}
	public Edge getIncidentEdge() {
		return this.incidentEdge;
	}
}
