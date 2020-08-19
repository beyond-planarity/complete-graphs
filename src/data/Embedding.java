package data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class Embedding {

	public static final String SET1_LETTER = "b"; // letter for first bipartite set
	public static final String SET2_LETTER = "r"; // letter for second bipartite set
	
	protected int     id;
	protected int     sourceEmbeddingId;    // id of the embedding this embedding was created from
	protected int     referenceEmbeddingId; // id of isomorphic embedding, if any
	protected boolean insertionPossible;    // true, if an vertex can be inserted into this embedding
	
	// information for navigating through embeddings on hard disc, without loading all at the same
	// time into main memory
	protected int     nextEmbedding;
	protected int     prevEmbedding;
	protected int     nextTopoEmbedding;
	protected int     prevTopoEmbedding;
	protected int     drawingNr = 0;
	
	// counter for the id's of vertices, edges and faces
	protected int vertexId = 0;
	protected int edgeId = 0;
	protected int faceId = 0;
	
	// elements in this embedding
	protected HashMap<Integer, Vertex> vertices;
	protected HashMap<String, Integer> name2Vertex;
	protected HashMap<Integer, Edge>   edges;
	protected HashMap<Integer, Face>   faces;

	// save the gaps; the crossing is mapped to one edge part of the edge with the gap
	protected HashMap<Integer, Integer> gapCrossingToEdge;
	
	
	/**
	 * Creates a new <code>Embedding</code>. An embedding is a structure to safe the drawing of a graph.
	 * @param id id of the new embedding
	 */
	public Embedding(int id) {
		this.id = id;
		this.sourceEmbeddingId = -99;
		this.referenceEmbeddingId = -99;
		this.insertionPossible = true;
		vertices    = new HashMap<Integer, Vertex>();
		name2Vertex = new HashMap<String, Integer>();
		edges       = new HashMap<Integer, Edge>();
		faces       = new HashMap<Integer, Face>();
		
		gapCrossingToEdge = new HashMap<Integer, Integer>();
		
		nextEmbedding     = id;
		prevEmbedding     = id;
		nextTopoEmbedding = id;
		prevTopoEmbedding = id;
	}
	
	/**
	 * Sets the id of this embedding. Each embedding must have a unique id.
	 * @param id new id of this embedding
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Get the id of this embedding.
	 * @return id
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Sets the id of the source embedding. The source embedding is the embedding,
	 * this embedding was created from.
	 * @param sourceEmbeddingId id of the source embedding
	 */
	public void setSourceEmbeddingId(int sourceEmbeddingId) {
		this.sourceEmbeddingId = sourceEmbeddingId;
	}
	/**
	 * Returns the id of the source embedding. The source embedding is the embedding,
	 * this embedding was created from.
	 * @return id of source embedding
	 */
	public int getSourceEmbeddingId() {
		return this.sourceEmbeddingId;
	}
	
	/**
	 * Sets the id of the reference embedding. The reference embedding is an isomorphic
	 * embedding to this one. Only needed, if all embeddings are saved and not only
	 * non-isomorphic ones.
	 * @param referenceEmbeddingId id of reference embedding
	 */
	public void setReferenceEmbeddingId(int referenceEmbeddingId) {
		this.referenceEmbeddingId = referenceEmbeddingId;
	}
	/**
	 * Returns the id of the reference embedding. The reference embedding is an isomorphic
	 * embedding to this one. Only needed, if all embeddings are saved and not only
	 * non-isomorphic ones.
	 * @return id of reference embedding
	 */
	public int getReferenceEmbeddingId() {
		return this.referenceEmbeddingId;
	}
	
	/**
	 * Returns the number of all vertices (real vertices and crossings) in this embedding.
	 * @return number of all vertices
	 */
	public int getNumberVerices() {
		return vertices.size();
	}
	public int getNumberEdges() {
		return edges.size();
	}
	public int getNumberFaces() {
		return faces.size();
	}
	
	public Vertex getVertex(int id) {
		return vertices.get(id);
	}
	public Edge getEdge(int id) {
		return edges.get(id);
	}
	public Face getFace(int id) {
		return faces.get(id);
	}
	
	/**
	 * Sets the <code>insertionPossibel</code> flag of this embedding.
	 * The flag is true by default and should be set to false,
	 * if no vertex can be inserted into this embedding (or into a certain bipartite set of this embedding).
	 * @param insertionPossible false, if no vertex can be inserted into this embedding
	 */
	public void setInsertionPossible(boolean insertionPossible) {
		this.insertionPossible = insertionPossible;
	}
	/**
	 * Returns false, if no vertex can be inserted (into a certain bipartite set) into this embedding 
	 * @return false, if no vertex can be inserted into this embedding
	 */
	public boolean isInsertionPossible() {
		return insertionPossible;
	}
	
	/**
	 * Tests if this embedding has a vertex with the specified name.
	 * @param vertexName a string, possibly the name of a vertex
	 * @return true, if this embedding has a vertex with <code>vertexName</code>
	 */
	public boolean hasVertexWithName(String vertexName) {
		return name2Vertex.containsKey(vertexName);
	}
	/**
	 * Returns the vertex with <code>vertexName</code>.
	 * @param vertexName name of a vertex in this embedding
	 * @return the id of the vertex
	 */
	public int getVertexIdByName(String vertexName) {
		return name2Vertex.get(vertexName);
	}
	/**
	 * Returns the vertex with <code>vertexName</code>.
	 * @param vertexName name of a vertex in this embedding
	 * @return the vertex
	 */
	public Vertex getVertexByName(String vertexName) {
		int id = name2Vertex.get(vertexName);
		return vertices.get(id);
	}
	
	
	/**
	 * Creates a copy of this embedding. The new embedding has the id <code>embeddingId</code>.
	 * @return a copy of this embedding
	 */
	public Embedding copy(int embeddingId) {
		Embedding em = new Embedding(embeddingId);
		
		for (int id : vertices.keySet()) {
			em.createVertex(id);
			em.setVertexName(id, getVertexName(id));
		}
		for (int id : edges.keySet()) {
			em.createEdge(id);
		}
		for (int id : faces.keySet()) {
			em.createFace(id);
		}
		
		HashMap<Integer,Vertex> verticesCopy = em.getVertices();
		HashMap<Integer,Edge> 	edgesCopy 	 = em.getEdges();
		HashMap<Integer,Face> 	facesCopy 	 = em.getFaces();
		
		for (Entry<Integer,Vertex> entry : verticesCopy.entrySet()) {
			Vertex copyV = entry.getValue();
			Vertex v     = vertices.get(entry.getKey());
			copyV.setIsCrossing(v.isCrossing());
			copyV.setOutEdge(edgesCopy.get(v.getOutEdge().getId()));
		}
		
		for (Entry<Integer,Edge> entry : edgesCopy.entrySet()) {
			Edge copyE = entry.getValue();
			Edge e     = edges.get(entry.getKey());
			copyE.setSource(verticesCopy.get(e.getSource().getId()));
			copyE.setTarget(verticesCopy.get(e.getTarget().getId()));
			copyE.setFace(facesCopy.get(e.getFace().getId()));
			copyE.setTwin(edgesCopy.get(e.getTwin().getId()));
			copyE.setPrevious(edgesCopy.get(e.getPrevious().getId()));
			copyE.setNext(edgesCopy.get(e.getNext().getId()));
			if (e.getEdgeBeforeCrossing() != null) {
				copyE.setEdgeBeforeCrossing(edgesCopy.get(e.getEdgeBeforeCrossing().getId()));
			}
			if (e.getEdgeAfterCrossing() != null) {
				copyE.setEdgeAfterCrossing(edgesCopy.get(e.getEdgeAfterCrossing().getId()));
			}
		}
		
		for (Entry<Integer,Face> entry : facesCopy.entrySet()) {
			Face copyF = entry.getValue();
			Face f     = faces.get(entry.getKey());
			copyF.setIncidentEdge(edgesCopy.get(f.getIncidentEdge().getId()));
		}
		
		for (Entry<Integer, Integer> entry : gapCrossingToEdge.entrySet()) {
			em.setGapCrossingToEdge(entry.getKey(), entry.getValue());
		}
		
		return em;
	}
	
	/**
	 * Creates a reversed copy of this embedding. The new embedding hat the id <code>embeddingId</code>.
	 * Reversed means: faces are right of edges, this implies a reversion of the rotation system.
	 * @return a reversed copy of this embedding
	 */
	public Embedding copyReversed(int embeddingId) {
		Embedding em = new Embedding(embeddingId);
		
		for (int id : vertices.keySet()) {
			em.createVertex(id);
			em.setVertexName(id, getVertexName(id));
		}
		for (int id : edges.keySet()) {
			em.createEdge(id);
		}
		for (int id : faces.keySet()) {
			em.createFace(id);
		}
		
		HashMap<Integer,Vertex> verticesCopy = em.getVertices();
		HashMap<Integer,Edge> 	edgesCopy 	 = em.getEdges();
		HashMap<Integer,Face> 	facesCopy 	 = em.getFaces();
		
		for (Entry<Integer,Vertex> entry : verticesCopy.entrySet()) {
			Vertex copyV = entry.getValue();
			Vertex v     = vertices.get(entry.getKey());
			copyV.setIsCrossing(v.isCrossing());
			copyV.setOutEdge(edgesCopy.get(v.getOutEdge().getTwin().getId()));
		}
		
		for (Entry<Integer,Edge> entry : edgesCopy.entrySet()) {
			Edge copyE = entry.getValue();
			Edge e     = edges.get(entry.getKey());
			copyE.setSource(verticesCopy.get(e.getTarget().getId()));
			copyE.setTarget(verticesCopy.get(e.getSource().getId()));
			copyE.setFace(facesCopy.get(e.getFace().getId()));
			copyE.setTwin(edgesCopy.get(e.getTwin().getId()));
			copyE.setPrevious(edgesCopy.get(e.getNext().getId()));
			copyE.setNext(edgesCopy.get(e.getPrevious().getId()));
			if (e.getEdgeAfterCrossing() != null) {
				copyE.setEdgeBeforeCrossing(edgesCopy.get(e.getEdgeAfterCrossing().getId()));
			}
			if (e.getEdgeBeforeCrossing() != null) {
				copyE.setEdgeAfterCrossing(edgesCopy.get(e.getEdgeBeforeCrossing().getId()));
			}
		}
		
		for (Entry<Integer,Face> entry : facesCopy.entrySet()) {
			Face copyF = entry.getValue();
			Face f     = faces.get(entry.getKey());
			copyF.setIncidentEdge(edgesCopy.get(f.getIncidentEdge().getId()));
		}
		
		for (Entry<Integer, Integer> entry : gapCrossingToEdge.entrySet()) {
			em.setGapCrossingToEdge(entry.getKey(), entry.getValue());
		}
		
		return em;
	}
	

	/**
	 * Returns all vertices in this embedding.
	 * @return a <code>HashMap</code> of all vertices 
	 */
	public HashMap<Integer, Vertex> getVertices() {
		return vertices;
	}
	/**
	 * Returns all edges in this embedding.
	 * @return a <code>HashMap</code> of all edges
	 */
	public HashMap<Integer, Edge> getEdges() {
		return edges;
	}

	/**
	 * Returns all faces in this embedding.
	 * @return a <code>HashMap</code> of all faces 
	 */
	public HashMap<Integer, Face> getFaces() {
		return faces;
	}

	/**
	 * Creates and returns a new vertex with the given id.
	 * @param id	the id of this vertex
	 * @return		the new vertex
	 */
	public Vertex createVertex(int id) {
		Vertex v = new Vertex(id);
		this.vertexId = Math.max(this.vertexId, id);
		vertices.put(v.getId(), v);
		setVertexName(v.getId(), v.getName());
		return v;
	}
	/**
	 * Creates and returns a new vertex with an auto-generated id.
	 * @return the new vertex
	 */
	public Vertex createVertex() {
		vertexId++;
		Vertex v = this.createVertex(this.vertexId);
		return v;
	}
	
	/**
	 * Creates and returns a new vertex with an auto-generated id and the specified name.
	 * @param name the name of this vertex
	 * @return     the new vertex
	 */
	public Vertex createVertex(String name) {
		Vertex v = createVertex();
		setVertexName(v.getId(), name);
		return v;
	}

	/**
	 * Creates and returns a new edge with the given id, source vertex and target vertex.
	 * @param id		id of this vertex
	 * @param source	source vertex
	 * @param target	target vertex
	 * @return			the new edge
	 */
	public Edge createEdge(int id, Vertex source, Vertex target) {
		Edge e = new Edge(id, source, target);
		this.edgeId = Math.max(this.edgeId, id);
		edges.put(e.getId(), e);
		return e;
	}
	/**
	 * Creates and returns a new edge with the given id.
	 * @param id	id of this vertex
	 * @return		the new edge
	 */
	public Edge createEdge(int id) {
		return this.createEdge(id, null, null);
	}
	/**
	 * Creates and returns a new edge with an auto-generated id.
	 * @return	the new edge
	 */
	public Edge createEdge() {
		edgeId++;
		Edge e = this.createEdge(this.edgeId);
		return e;
	}

	/**
	 * Creates and returns a new face with the given id and incident edge.
	 * @param id			the id of this edge
	 * @param incidentEdge	one incident edge to this face
	 * @return				the new face
	 */
	public Face createFace(int id, Edge incidentEdge) {
		Face f = new Face(id, incidentEdge);
		this.faceId = Math.max(this.faceId, id);
		faces.put(f.getId(), f);
		return f;
	}
	/**
	 * Creates and returns a new face with the given id.
	 * @param id	the id of this face
	 * @return		the face
	 */
	public Face createFace(int id) {
		return this.createFace(id, null);
	}
	/**
	 * Creates and returns a new face with an auto-generated id.
	 * @return	the new face
	 */
	public Face createFace() {
		faceId++;
		Face f = this.createFace(this.faceId);
		return f;
	}
	
	/**
	 * Sets <code>isCrossing</code> and the outgoing edge of the vertex with the given id.
	 * @param id			id of the vertex
	 * @param isCrossing	true, if this is a crossing
	 * @param outEdgeId		id of the outgoing edge
	 */
	public void setVertex(int id, boolean isCrossing, int outEdgeId) {
		Vertex v = vertices.get(id);
		if (v == null) {
			System.out.println("vertex is null");
		}
		v.setIsCrossing(isCrossing);
		if (outEdgeId != -1) {
			v.setOutEdge(edges.get(outEdgeId));
		}
	}
	
	/**
	 * Sets several parameters of this edges.
	 * @param id			id of the edge
	 * @param sourceId		id of source vertex
	 * @param targetId		id of target vertex
	 * @param faceId		id of left face
	 * @param twinId		id of twin edge
	 * @param nextId		id of next edge
	 * @param previousId	id of previous edge
	 */
	public void setEdge(int id, int sourceId, int targetId,
			int faceId, int twinId, int nextId, int previousId,
			int edgeBeforeCrossingId, int edgeAfterCrossingId) {
		Edge e = edges.get(id);
		e.setSource(vertices.get(sourceId));
		e.setTarget(vertices.get(targetId));
		e.setFace(faces.get(faceId));
		e.setTwin(edges.get(twinId));
		e.setNext(edges.get(nextId));
		e.setPrevious(edges.get(previousId));
		if (edgeBeforeCrossingId >= 0) {
			e.setEdgeBeforeCrossing(edges.get(edgeBeforeCrossingId));
		}
		if (edgeAfterCrossingId >= 0) {
			e.setEdgeAfterCrossing(edges.get(edgeAfterCrossingId));
		}
	}
	
	/**
	 * Sets the incident edge to the face with the specified id.
	 * @param id				id of the face
	 * @param incidentEdgeId	new id of incident edge
	 */
	public void setFace(int id, int incidentEdgeId) {
		Face f = faces.get(id);
		if (incidentEdgeId != -1) {
			f.setIncidentEdge(edges.get(incidentEdgeId));
		}
	}
	
	
	
	
	
	
	/**
	 * Returns all incident edges to the specified face.
	 * @param faceId id of a face
	 * @return		 list of all incident (half)edges to the face
	 */
	public List<Edge> getIncidentEdgesToFace(int faceId) {
		Face       face          = getFace(faceId);
		List<Edge> incidentEdges = new LinkedList<Edge>();
		
		Edge edge = face.getIncidentEdge();
		if (edge == null) {
			return incidentEdges;
		}
		
		int startId = edge.getId();
		incidentEdges.add(edge);
		edge = edge.getNext();
		while(edge.getId() != startId) {
			incidentEdges.add(edge);
			edge = edge.getNext();
		}
		
		return incidentEdges;
	}
	
	/**
	 * Returns true, if this edge part is the only edge part of the corresponding edge.
	 * In this case the edge is plane.
	 * @param edge the edge to test
	 * @return     true, if edge is plane
	 */
	public boolean isWholeEdge(Edge edge) {
		return (edge.getEdgeBeforeCrossing() == null) && (edge.getEdgeAfterCrossing() == null);
	}
	
	/**
	 * Returns the set of edges representing the edge in the not-planarized graph,
	 * to which the given edge part with <code>edgeId</code> belongs.
	 * @param edgeId 	id of an edge part in the embedding
	 * @return 			list of edge parts (list contains only half-edges, twins are not added)
	 */
	public List<Integer> getWholeHalfEdge(int edgeId) {
		List<Integer> wholeEdge = new LinkedList<Integer>();
		Edge edge = edges.get(edgeId);
		
		while (edge.getEdgeBeforeCrossing() != null) {
			edge = edge.getEdgeBeforeCrossing();
		}
		
		wholeEdge.add(edge.getId());
		while (edge.getEdgeAfterCrossing() != null) {
			edge = edge.getEdgeAfterCrossing();
			wholeEdge.add(edge.getId());
		}
		
		return wholeEdge;
	}
	
	/**
	 * Returns the set of edges representing the edge in the not-planarized graph,
	 * to which the given edge part with <code>edgeId</code> belongs.
	 * @param edgeId 	id of an edge part in the embedding
	 * @return 			list of edge parts (list contains all half-edges, twins are also added)
	 */
	public List<Integer> getWholeEdge(int edgeId) {
		List<Integer> wholeEdge = new LinkedList<Integer>();
		Edge edge = edges.get(edgeId);
		
		while (edge.getEdgeBeforeCrossing() != null) {
			edge = edge.getEdgeBeforeCrossing();
		}
		
		wholeEdge.add(edge.getId());
		while (edge.getEdgeAfterCrossing() != null) {
			edge = edge.getEdgeAfterCrossing();
			wholeEdge.add(edge.getId());
			wholeEdge.add(edge.getTwin().getId());
		}
		
		return wholeEdge;
	}
	
	/**
	 * Returns the degree of a face, that is the number of edges (vertices) on its boundary.
	 * @param faceId 	id of the face
	 * @return 			the degree of the face
	 */
	public int getFaceDegree(int faceId) {
		int deg = 0;
		Face f = faces.get(faceId);
		Edge e = f.getIncidentEdge();
		if (e == null) {
			return deg;
		}
		int startId = e.getId();
		do {
			deg++;
			e = e.getNext();
		} while (e.getId() != startId);
		
		return deg;
	}
	
	/**
	 * Returns the degree of a vertex. The degree is the number of outgoing edges.
	 * @param vertexId 	id of the vertex
	 * @return 			degree of the vertex
	 */
	public int getVertexDegree(int vertexId) {
		int deg = 0;
		Vertex v = vertices.get(vertexId);
		Edge e = v.getOutEdge();
		if (e == null) {
			return deg;
		}
		int startId = e.getId();
		do {
			deg++;
			e = e.getTwin().getNext();
		} while (e.getId() != startId);
		
		return deg;
	}
	
	
	public int getNumberOfRealEdges() {
		int edges = 0;
		for (Vertex v : vertices.values()) {
			if (!v.isCrossing()) {
				edges += getVertexDegree(v.getId());
			}
		}
		return edges / 2;
	}
	
	/**
	 * Returns the outgoing edges of the specified vertex.
	 * @param int 	the vertex id
	 * @return		list of all incident out edges
	 */
	public List<Edge> getIncidentEdgesToVertex(int vertex) {
		List<Edge> incidentEdges = new LinkedList<Edge>();
		
		Edge edge = vertices.get(vertex).getOutEdge();
		if (edge == null) {
			return incidentEdges;
		}

		int startId = edge.getId();
		incidentEdges.add(edge);
		edge = edge.getTwin().getNext();
		while(edge.getId() != startId) {
			incidentEdges.add(edge);
			edge = edge.getTwin().getNext();
		}
		
		return incidentEdges;
	}
	
	/**
	 * Returns the incident edges to a given vertex including those that are separated by a crossing.
	 * Returned are all edge parts including the twin edges.
	 * @param int 	the vertex id
	 * @return		list of all incident edges
	 */
	public List<Integer> getWholeIncidentEdgesToVertex(int vertex) {
		List<Integer> incidentEdges = new LinkedList<Integer>();
		
		Edge edge = vertices.get(vertex).getOutEdge();
		if (edge == null) {
			return incidentEdges;
		}

		int startId = edge.getId();
		incidentEdges.addAll(getWholeHalfEdge(startId));
		edge = edge.getTwin();
		incidentEdges.addAll(getWholeHalfEdge(edge.getId()));
		edge = edge.getNext();
		while(edge.getId() != startId) {
			incidentEdges.addAll(getWholeHalfEdge(edge.getId()));
			edge = edge.getTwin();
			incidentEdges.addAll(getWholeHalfEdge(edge.getId()));
			edge = edge.getNext();
		}
		
		return incidentEdges;
	}
	
	/**
	 * Returns all incident faces to a given vertex 
	 * @param int	the vertex id
	 * @return		list of all incident faces
	 */
	public List<Face> getIncidentFacesToVertex(int vertex) {
		List<Face> incidentFaces = new LinkedList<Face>();
		List<Edge> incidentEdges = getIncidentEdgesToVertex(vertex);
		for (Edge e : incidentEdges) {
			incidentFaces.add(e.getFace());
		}
				
		return incidentFaces;
	}
	
	/**
	 * Returns the number of crossings of a given edge (part).
	 * @param e	the edge (part)
	 * @return	the number of crossings
	 */
	public int getNumberOfCrossings(Edge e) {
		int crossings = 0;
		
		while (e.getTarget().isCrossing()) {
			e = e.getEdgeAfterCrossing();
		}
		while (e.getSource().isCrossing()) {
			crossings++;
			e = e.getEdgeBeforeCrossing();
		}
		
		return crossings;
	}
	
	/**
	 * Returns the total number of crossings of this embedding.
	 * @return	the number of crossings
	 */
	public int getCrossingNumber() {
		int cr = 0;
		for (Vertex v : vertices.values()) {
			if (v.isCrossing()) {
				cr++;
			}
		}
		return cr;
	}
	
	
	/**
	 * Subdivides a given edge into two edges; inserts a
	 * new vertex between the two edges and takes care of pointers.
	 * @param edgeId		the id of the edge to subdivide
	 * @param isCrossing	true, if the subdividing vertex is a crossing
	 * @return 				the new vertex
	 */
	public Vertex subdivideEdge(int edgeId, boolean isCrossing) {
		Edge   edge         = edges.get(edgeId);
		Vertex source       = edge.getSource();
		Edge   prev         = edge.getPrevious();
		Edge   edgeBeforeCr = edge.getEdgeBeforeCrossing();
		Face   face         = edge.getFace();
		
		Edge twin              = edge.getTwin();
		Edge twinNext          = twin.getNext();
		Edge twinAfterCrossing = twin.getEdgeAfterCrossing();
		Face twinFace          = twin.getFace();
		
		Vertex newVertex   = this.createVertex();
		Edge   newEdge     = this.createEdge();
		Edge   newTwinEdge = this.createEdge();
		newEdge.setTwin(newTwinEdge);
		newTwinEdge.setTwin(newEdge);
		
		newVertex.setIsCrossing(isCrossing);
		newVertex.setOutEdge(edge);
		source.setOutEdge(newEdge);
		
		newEdge.setSource(source);
		newEdge.setTarget(newVertex);
		newTwinEdge.setSource(newVertex);
		newTwinEdge.setTarget(source);
		edge.setSource(newVertex);
		twin.setTarget(newVertex);
		
		newEdge.setFace(face);
		newEdge.setNext(edge);
		newEdge.setPrevious(prev);
		prev.setNext(newEdge);
		edge.setPrevious(newEdge);
		
		newTwinEdge.setFace(twinFace);
		newTwinEdge.setNext(twinNext);
		twinNext.setPrevious(newTwinEdge);
		newTwinEdge.setPrevious(twin);
		twin.setNext(newTwinEdge);
		
		if (isCrossing) {
			newEdge.setEdgeAfterCrossing(edge);
			edge.setEdgeBeforeCrossing(newEdge);
			twin.setEdgeAfterCrossing(newTwinEdge);
			newTwinEdge.setEdgeBeforeCrossing(twin);
		}
		
		if (source.isCrossing()) {
			newEdge.setEdgeBeforeCrossing(edgeBeforeCr);
			edgeBeforeCr.setEdgeAfterCrossing(newEdge);
			newTwinEdge.setEdgeAfterCrossing(twinAfterCrossing);
			twinAfterCrossing.setEdgeBeforeCrossing(newTwinEdge);
		}
		
		return newVertex;
	}
	
	/**
	 * Inserts an edge between source and target into the face with id <code>faceId</code>.
	 * It is assumed that source, target and face are already present in the drawing.
	 * It is taken care of all the pointers and the twin edge.
	 * @param sourceId				the id of the source vertex of the new edge
	 * @param targetId				the id of the target of the new edge
	 * @param faceId				the id of the face to insert the edge into
	 * @param edgeBeforeCrossingId	the id of the edge before the crossing, in case there is such an edge; input a negativ value if there is no such edge.
	 * @return 						the new edge
	 */
	public Edge insertEdge(int sourceId, int targetId, int faceId, int edgeBeforeCrossingId) throws IllegalArgumentException {
		Vertex source = vertices.get(sourceId);
		Vertex target = vertices.get(targetId);
		Face   face	  = faces.get(faceId);
		
		boolean switched = false;
		if (source.getOutEdge() == null) {
			Vertex temp = source;
			source      = target;
			target      = temp;
			
			switched = true;
		}

		Edge edge = createEdge();
		edge.setSource(source);
		edge.setTarget(target);
		edge.setFace(face);
		face.setIncidentEdge(edge);

		Edge twin = createEdge();
		twin.setSource(target);
		twin.setTarget(source);

		edge.setTwin(twin);
		twin.setTwin(edge);
		
		if (edgeBeforeCrossingId > 0) {
			Edge edgeBeforeCrossing = edges.get(edgeBeforeCrossingId);
			if (switched) {
				twin.setEdgeBeforeCrossing(edgeBeforeCrossing);
				edgeBeforeCrossing.setEdgeAfterCrossing(twin);
				edge.setEdgeAfterCrossing(edgeBeforeCrossing.getTwin());
				edgeBeforeCrossing.getTwin().setEdgeBeforeCrossing(edge);
			}
			else {
				edge.setEdgeBeforeCrossing(edgeBeforeCrossing);
				edgeBeforeCrossing.setEdgeAfterCrossing(edge);
				twin.setEdgeAfterCrossing(edgeBeforeCrossing.getTwin());
				edgeBeforeCrossing.getTwin().setEdgeBeforeCrossing(twin);
			}
		}

		if (target.getOutEdge() == null) {
			edge.setNext(twin);
			twin.setPrevious(edge);
			target.setOutEdge(twin);
			twin.setFace(face);
			
			if (source.getOutEdge() == null) {
				source.setOutEdge(edge);
				edge.setPrevious(twin);
				twin.setNext(edge);
			}
			else {
				// source has an outedge
				List<Edge> outEdges = this.getIncidentEdgesToVertex(source.getId());
				Edge outEdgeFace = null;
				for (Edge e : outEdges) {
					if (e.getFace().getId() == face.getId()) {
						outEdgeFace = e;
						break;
					}
				}
				Edge inEdgeFace = outEdgeFace.getPrevious();
				
				edge.setPrevious(inEdgeFace);
				inEdgeFace.setNext(edge);
				twin.setNext(outEdgeFace);
				outEdgeFace.setPrevious(twin);
			}
		}
		else {
			// source and target are not null
			Face newFace = createFace();
			newFace.setIncidentEdge(twin);
			twin.setFace(newFace);

			List<Edge> outEdges = this.getIncidentEdgesToVertex(source.getId());
			Edge sourceOutEdge = null;
			for (Edge e : outEdges) {
				if (e.getFace().getId() == face.getId()) {
					sourceOutEdge = e;
					break;
				}
			}
			Edge sourceInEdge = sourceOutEdge.getPrevious();
					

			outEdges = this.getIncidentEdgesToVertex(target.getId());
			Edge targetOutEdge = null;
			for (Edge e : outEdges) {
				if (e.getFace().getId() == face.getId()) {
					targetOutEdge = e;
					break;
				}
			}
			Edge targetInEdge = targetOutEdge.getPrevious();

			edge.setNext(targetOutEdge);
			targetOutEdge.setPrevious(edge);
			edge.setPrevious(sourceInEdge);
			sourceInEdge.setNext(edge);

			twin.setNext(sourceOutEdge);
			sourceOutEdge.setPrevious(twin);
			twin.setPrevious(targetInEdge);
			targetInEdge.setNext(twin);
			
			// update edges incident to new face
			Edge e = twin.getNext();
			while (e.getId() != twin.getId()) {
				e.setFace(newFace);
				e = e.getNext();
			}
		}

		return switched? twin : edge;
	}
	
	
	/**
	 * Returns true, if and only if the vertex with id <code>vertexId</code> is on the boundary
	 * of the face with id <code>vertexId</code>.
	 * @param faceId    id of a face
	 * @param vertexId  id of a vertex
	 * @return			true or false
	 */
	public boolean hasFaceVertex(int faceId, int vertexId) {
		for (Edge e : getIncidentEdgesToFace(faceId)) {
			if (e.getSource().getId() == vertexId) {
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * Returns for the edge with id <code>edgeId</code> the real source
	 * (the source vertex of the edge that the specified edge part is part of).
	 * @param edgeId	id of the edge(part)
	 * @return			id of the real source
	 */
	public int getRealSource(int edgeId) {
		Edge edge = edges.get(edgeId);
		Vertex source = edge.getSource();
		while (source.isCrossing()) {
			edge = edge.getEdgeBeforeCrossing();
			source = edge.getSource();
		}
		// source is not a crossing
		return source.getId();
	}
	
	/**
	 * Returns for the edge with id <code>edgeId</code> the real target
	 * (the target vertex of the edge that the specified edge part is part of).
	 * @param edgeId	id of the edge(part)
	 * @return			id of the real target
	 */
	public int getRealTarget(int edgeId) {
		Edge edge = edges.get(edgeId);
		Vertex target = edge.getTarget();
		while (target.isCrossing()) {
			edge = edge.getEdgeAfterCrossing();
			target = edge.getTarget();
		}
		// target is not a crossing
		return target.getId();
	}

	

	/**
	 * Returns true, if and only if the whole edge (in the not planarized graph) that contains
	 * the edge part with id <code>edgeId</code> has a crossing with id <code>crossingId</code>.
	 * @param edgeId      	id of an edge in this embedding
	 * @param crossingId  	id of a crossing
	 * @return 				true or false
	 */
	public boolean hasCrossing(int edgeId, int crossingId) {
		Edge   e = edges.get(edgeId);

		Vertex source = e.getSource();
		while (source.isCrossing()) {
			e = e.getEdgeBeforeCrossing();
			source = e.getSource();
		}
		// source is not a crossing
		
		Vertex target = e.getTarget();
		while (target.isCrossing()) {
			if (target.getId() == crossingId) {
				return true;
			}
			e = e.getEdgeAfterCrossing();
			target = e.getTarget();
			
		}
		
		return false;
	}
	
	/**
	 * True, if and only if the vertices with the ids <code>idV1</code> and <code>idV2</code>
	 * have the same name.
	 * @param idV1	id of a vertex
	 * @param idV2	id of a vertex
	 * @return		true or false
	 */
	public boolean isNameEqual(int idV1, int idV2) {
		Vertex v1 = getVertex(idV1);
		Vertex v2 = getVertex(idV2);
		return v1.getName().equals(v2.getName());
	}
	
	/**
	 * Sets the name of the vertex with id <code>vertexId</code> to <code>vertexName</code>.
	 * @param vertexId    id of a vertex
	 * @param vertexName  new name of the vertex
	 */
	public void setVertexName(int vertexId, String vertexName) {
		Vertex v = getVertex(vertexId);
		name2Vertex.remove(v.getName());
		v.setName(vertexName);
		name2Vertex.put(vertexName, v.getId());
	}
	
	/**
	 * Returns the name of the vertex with id <code>vertexId</code>
	 * @param vertexId 	id of a vertex
	 * @return 			name of the vertex
	 */
	public String getVertexName(int vertexId) {
		return getVertex(vertexId).getName();
	}
	
	/**
	 * Exchanges the two bipartite sets with each other.
	 */
	public void switchNames() {
		String TEMP_NAME = "temp";
		List<Integer> set1 = new LinkedList<Integer>();
		int i=1;
		while (hasVertexWithName(Embedding.SET1_LETTER + i)) {
			int vId = getVertexIdByName(Embedding.SET1_LETTER + i);
			setVertexName(vId, TEMP_NAME + i);
			set1.add(vId);
			i++;
		}

		i = 1;
		while (hasVertexWithName(Embedding.SET2_LETTER + i)) {
			int vId = getVertexIdByName(Embedding.SET2_LETTER + i);
			setVertexName(vId, SET1_LETTER + i);
			i++;
		}
		
		for (i=0; i<set1.size(); i++) {
			setVertexName(set1.get(i), SET2_LETTER + (i+1));
		}
	}
	

	
	
	/**
	 * Calculate the first bipartite set of this embedding.
	 * @return	list of vertices in the first bipartite set
	 */
	public List<Integer> getVertexSet1() {
		List<Integer> set1 = new LinkedList<Integer>();
		int i=1;		
		while (hasVertexWithName(Embedding.SET1_LETTER + i)) {
			set1.add(getVertexIdByName(Embedding.SET1_LETTER + i));
			i++;
		}
		return set1;
	}

	/**
	 * Calculate the second bipartite set of this embedding.
	 * @return	list of vertices in the second bipartite set
	 */
	public List<Integer> getVertexSet2() {
		List<Integer> set2 = new LinkedList<Integer>();
		int i=1;
		while (hasVertexWithName(Embedding.SET2_LETTER + i)) {
			set2.add(getVertexIdByName(Embedding.SET2_LETTER + i));
			i++;
		}
		return set2;
	}
	

	
	/**
	 * Returns all the edge parts that belong to the crossing edges in the not planarized graph.
	 * @param edgeId 	the edge id of an edge (part)
	 * @return			list of all crossing edges (both half-edges are in the list)
	 */
	public List<Integer> getWholeCrossingEdges(int edgeId) {
		List<Integer> wholeEdge     = getWholeHalfEdge(edgeId);
		List<Integer> crossingEdges = new LinkedList<Integer>();
		
		for (int id : wholeEdge) {
			Edge e = getEdge(id);
			if (e.getTarget().isCrossing()) {
				Edge crEdge = e.getNext();
				crossingEdges.addAll(getWholeHalfEdge(crEdge.getId()));
				crEdge = crEdge.getTwin();
				crossingEdges.addAll(getWholeHalfEdge(crEdge.getId()));
			}
		}
		
		return crossingEdges;
	}

	
	/**
	 * Returns all the crossing edges to a given edge.
	 * @param int 	the edge id
	 * @return		list of all crossing edges; returned is one single edge part for every edge that crosses the given edge
	 */
	public List<Integer> getCrossingEdgesRepresentant(int edgeId) {
		List<Integer> wholeEdge     = getWholeHalfEdge(edgeId);
		List<Integer> crossingEdges = new LinkedList<Integer>();
		
		for (int id : wholeEdge) {
			Edge e = getEdge(id);
			if (e.getTarget().isCrossing()) {
				Edge crEdge = e.getNext();
				crossingEdges.add(crEdge.getId());
			}
		}
		
		return crossingEdges;
	}
	

	/**
	 * Returns the next embedding (for navigation on hard disc).
	 * @return id of then next embedding
	 */
	public int getNextEmbedding() {
		return nextEmbedding;
	}
	/**
	 * Sets the next embedding.
	 * @param nextEmbedding id of the next embedding
	 */
	public void setNextEmbedding(int nextEmbedding) {
		this.nextEmbedding = nextEmbedding;
	}

	/**
	 * Returns the previous embedding (for navigation on hard disc).
	 * @return id of then previous embedding
	 */
	public int getPrevEmbedding() {
		return prevEmbedding;
	}
	/**
	 * Sets the previous embedding.
	 * @param prevEmbedding id of the previous embedding
	 */
	public void setPrevEmbedding(int prevEmbedding) {
		this.prevEmbedding = prevEmbedding;
	}

	/**
	 * Returns the next non-isomorphic embedding (for navigation on hard disc).
	 * @return id of then next non-isomorphic embedding
	 */
	public int getNextTopoEmbedding() {
		return nextTopoEmbedding;
	}
	/**
	 * Sets the next non-isomorphic embedding.
	 * @param nextTopoEmbedding id of the next non-isomorphic embedding
	 */
	public void setNextTopoEmbedding(int nextTopoEmbedding) {
		this.nextTopoEmbedding = nextTopoEmbedding;
	}


	/**
	 * Returns the previous non-isomorphic embedding (for navigation on hard disc).
	 * @return id of then previous non-isomorphic embedding
	 */
	public int getPrevTopoEmbedding() {
		return prevTopoEmbedding;
	}
	/**
	 * Sets the previous non-isomorphic embedding.
	 * @param prevTopoEmbedding id of the previous non-isomorphic embedding
	 */
	public void setPrevTopoEmbedding(int prevTopoEmbedding) {
		this.prevTopoEmbedding = prevTopoEmbedding;
	}
	
	/**
	 * Sets the label of the vertex with id <code>vertexId</code>.
	 * @param vertexId	id of a vertex
	 * @param label		new label of the vertex
	 */
	public void setVertexLabel(int vertexId, String label) {
		vertices.get(vertexId).setMappedName(label);
	}
	/**
	 * Returns the label of vertex with id <code>vertexId</code>.
	 * @param vertexId	id of a vertex
	 * @return			label of the vertex
	 */
	public String getVertexLabel(int vertexId) {
		return vertices.get(vertexId).getMappedName();
	}

	/**
	 * Returns the drawing number of this embedding.
	 * @return the drawing number
	 */
	public int getDrawingNr() {
		return drawingNr;
	}
	/**
	 * Sets the drawing number of this embedding.
	 * @param drawingNr the new drawing number
	 */
	public void setDrawingNr(int drawingNr) {
		this.drawingNr = drawingNr;
	}

	/**
	 * Tests if this embedding has a vertex with the specified id.
	 * @param id	a integer
	 * @return		result of the test
	 */
	public boolean hasVertex(int id) {
		return vertices.containsKey(id);
	}
	
	/**
	 * Tests if the edge with id <code>edgeId</code> is incident to the vertex with id <code>vertexId</code>.
	 * @param edgeId	id of an edge
	 * @param vertexId	id of a vertex
	 * @return			Result of the test
	 */
	public boolean isEdgeIncidentToVertex(int edgeId, int vertexId) {
		return getRealSource(edgeId) == vertexId || getRealTarget(edgeId) == vertexId;
	}
	
	
	/**
	 * Returns an edge part of the edge to which this gap belongs.
	 * @param gap	id of a crossing
	 * @return		edge (part)
	 */
	public int getGapEdge(int gap) {
		return gapCrossingToEdge.get(gap);
	}
	
	/**
	 * Returns the mapping of gaps to edges.
	 * @return mapping
	 */
	public HashMap<Integer, Integer> getGapCrossingsToEdge() {
		return gapCrossingToEdge;
	}
	
	/**
	 * Maps a gap crossing to an edge part. The gap should be part of the
	 * corresponding edge in the non-planarized graph.
	 * @param crossing	id of a crossing
	 * @param edge		id of an edge
	 */
	public void setGapCrossingToEdge(int crossing, int edge) {
		gapCrossingToEdge.put(crossing, edge);
	}
	
	/**
	 * True, if and only if the edge part is part of an edge with a gap.
	 * @param edge	some edge
	 * @return		result of test
	 */
	public boolean hasWholeEdgeGap(Edge edge) {
		return hasWholeEdgeGap(edge.getId());
	}
	/**
	 * True, if and only if the edge part with id <code>edge</code> is part of
	 * an edge containing a gap.
	 * @param edge	id of an edge
	 * @return		result of test
	 */
	public boolean hasWholeEdgeGap(int edge) {
		Set<Tuple> edges = new HashSet<Tuple>();
		
		for (int edgeId : gapCrossingToEdge.values()) {
			int source = getRealSource(edgeId);
			int target = getRealTarget(edgeId);
			if (source > target) {
				int temp = source;
				source = target;
				target = temp;
			}
			edges.add(new Tuple(source, target));
		}
		
		int edgeS = getRealSource(edge);
		int edgeT = getRealTarget(edge);
		if (edgeS > edgeT) {
			int temp = edgeS;
			edgeS    = edgeT;
			edgeT    = temp;
		}
		return edges.contains(new Tuple(edgeS, edgeT));
	}
	
	
	
	/**
	 * Tests if the current drawing is gap-planar.
	 * @return result of the test 
	 */
	public boolean areGapsValid() {
		Set<Tuple> edges = new HashSet<Tuple>();
		
		for (Entry<Integer,Integer> entry : gapCrossingToEdge.entrySet()) {
			int edgeId = entry.getValue();
			int source = getRealSource(edgeId);
			int target = getRealTarget(edgeId);
			if (source > target) {
				int temp = source;
				source = target;
				target = temp;
			}
			Tuple e = new Tuple(source, target);
			if (edges.contains(e)) {
				return false;
			}
			else {
				edges.add(e);
			}
		}
		return true;
	}
	
}
