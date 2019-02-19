package data;

public class Edge {
	
	 private int id;
	 private Vertex source;
	 private Vertex target;
	 private Face face; // face to the left of this edge
	 
	 private Edge twin;
	 private Edge next;
	 private Edge previous;
	 
	 private Edge edgeBeforCrossing;
	 private Edge edgeAfterCrossing;

	 // drawing
	 private String  name;
	 private String  mappedName;
	 private boolean toDraw = false;
	 
	 
	 public Edge(int id, Vertex source, Vertex target) {
		 this.id = id;
		 this.source = source;
		 this.target = target;
		 this.name   = id + "";
		 this.mappedName  = name;
	 }
	 
	 public void setId(int id) {
		 this.id = id;
	 }
	 public int getId() {
		return id;
	}
	 
	 public void setName(String name) {
		 this.name = name;
	 }
	 public String getName() {
		return name;
	}
	 
	 public void setMappedName(String mappedName) {
		 this.mappedName = mappedName;
	 }
	 public String getMappedName() {
		return mappedName;
	}
	 
	 public void setSource(Vertex source) {
		 this.source = source;
	 }
	 public Vertex getSource() {
		 return this.source;
	 }
	 
	 public void setTarget(Vertex target) {
		 this.target = target;
	 }
	 public Vertex getTarget() {
		 return this.target;
	 }
	 
	 public void setTwin(Edge twinEdge) {
		 this.twin = twinEdge;
	 }
	 public Edge getTwin() {
		 return this.twin;
	 }
	 
	 public void setFace(Face face) {
		 this.face = face;
	 }
	 public Face getFace() {
		 return this.face;
	 }
	 
	 public void setNext(Edge nextEdge) {
		 this.next = nextEdge;
	 }
	 public Edge getNext() {
		 return this.next;
	 }
	 
	 public void setPrevious(Edge previousEdge) {
		 this.previous = previousEdge;
	 }
	 public Edge getPrevious() {
		 return this.previous;
	 }
	 
	 public void setEdgeBeforeCrossing(Edge edge) {
		 this.edgeBeforCrossing = edge;
	 }
	 public Edge getEdgeBeforeCrossing() {
		 return this.edgeBeforCrossing;
	 }

	 public void setEdgeAfterCrossing(Edge edge) {
		 this.edgeAfterCrossing = edge;
	 }
	 public Edge getEdgeAfterCrossing() {
		 return this.edgeAfterCrossing;
	 }

	public boolean isToDraw() {
		return toDraw;
	}

	public void setToDraw(boolean toDraw) {
		this.toDraw = toDraw;
	}

}
