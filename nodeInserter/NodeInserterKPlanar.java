package nodeInserter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import data.Edge;
import data.Embedding;
import data.Face;

/**
 * A NodeInserter for the class of the k-planar graphs, that is
 * edges are inserted due to the rules of this graph class.  
 * @author Tommy
 *
 */
public class NodeInserterKPlanar extends NodeInserter {
	
	private final static boolean PRINT = false;
	
	private final int kPlanarity;
	
	
	/**
	 * Creates a new <code>NodeInserter</code>.
	 * @param kPlanarity number of allowed edge crossings
	 */
	public NodeInserterKPlanar(int kPlanarity) {
		super();
		this.kPlanarity = kPlanarity;
	}
	
	public NodeInserter copy() {
		return copyFields(new NodeInserterKPlanar(this.kPlanarity));
	}
	
	
	/**
	 * Inserts the new vertex in the source embedding (in all possible faces)
	 * and creates an edge to it (in all possible ways).
	 * @param firstVertex vertex that should be connected to the new vertex
	 */
	protected void createEdgeToFirstVertex(int firstVertex) {
		List<Face> incidentFaces = embedding.getIncidentFacesToVertex(firstVertex);
		
		Set<Integer> prohibitedEdges = new HashSet<Integer>();
		// simplicity
		prohibitedEdges.addAll(embedding.getWholeIncidentEdgesToVertex(firstVertex));
		
		// 0 crossings for first edge
		for (Face f : incidentFaces) {
			Embedding embedding0 = getCopy(embedding);
			
			int newVertexId = embedding0.createVertex().getId();
			embedding0.setVertexName(newVertexId, newNodeName);
			embedding0.insertEdge(firstVertex, newVertexId, f.getId(),-1);
			intermediateEmbeddings.add(new IntermediateEmbedding(embedding0, newVertexId));
			if (PRINT) {
				System.out.println("Found new intermetiate embedding - ID: " + embedding0.getId() + " (0 crossings 2nd edge)");
			}
		}
		
		if (kPlanarity == 0) {
			return;
		}

		// more crossing for first edge
		for (Face f : incidentFaces){
			Embedding emb = getCopy(embedding);
			createEdgeToFirstVertexHelper(emb, f.getId(), prohibitedEdges, firstVertex, -1, 1);
		}
	}
	
	
	/**
	 * Insert edge to first vertex in all possible ways.
	 * @param emb                  considered embedding
	 * @param faceId               considered face
	 * @param prohibitedEdges      edges not allowed to be crossed
	 * @param sourceId             id of the source vertex
	 * @param edgeBeforeCrossingId id of the edge before the crossing (-1 if there is none)
	 * @param depth                number of edges already crossed by the edge to insert
	 */
	private void createEdgeToFirstVertexHelper(Embedding emb, int faceId, Set<Integer> prohibitedEdges, int sourceId,
			int edgeBeforeCrossingId, int depth) {		
		
		//get incident edges from each vertex that can cross and reach vertex v1
		List<Edge> incidentEdgesTemp = emb.getIncidentEdgesToFace(faceId);
		List<Edge> incidentEdges = new LinkedList<Edge>();
		for (Edge e : incidentEdgesTemp) {
			if (!prohibitedEdges.contains(e.getId()) &&	emb.getNumberOfCrossings(e) < kPlanarity) {
				incidentEdges.add(e);
			}
		}
		
		for (Edge e : incidentEdges) {
			Embedding embCrossingAdded  = getCopy(emb);
			int       newCrossing       = embCrossingAdded.subdivideEdge(e.getId(), true).getId();
			int       edgeToNewCrossing = embCrossingAdded.insertEdge(
					sourceId, newCrossing, faceId, edgeBeforeCrossingId).getId();
			
			// copy the prohibited edges and insert the new ones
			Set<Integer> currentlyProhibitedEdges = new HashSet<Integer>();
			for (Integer edgeId : prohibitedEdges) {
				currentlyProhibitedEdges.add(edgeId);
			}
			currentlyProhibitedEdges.addAll(embCrossingAdded.getWholeIncidentEdgesToVertex(newCrossing));
			
			
			int faceOfTwin = e.getTwin().getFace().getId();
			//create new vertex
			Embedding newEmb = getCopy(embCrossingAdded);
			int newVertexId = newEmb.createVertex().getId();
			newEmb.setVertexName(newVertexId, newNodeName);
			newEmb.insertEdge(newCrossing, newVertexId, faceOfTwin, edgeToNewCrossing);
			intermediateEmbeddings.add(new IntermediateEmbedding(newEmb, newVertexId));
			if (PRINT) {
				System.out.println("Found new intermetiate embedding - ID: " + newEmb.getId() + " (0 crossings 2nd edge)");
			}
			
			
			if (depth < kPlanarity) {
				createEdgeToFirstVertexHelper(
						embCrossingAdded,
						faceOfTwin,
						currentlyProhibitedEdges,
						newCrossing,
						edgeToNewCrossing,
						depth+1);
			}
		}
	}
	
	
	
	
	
	
	
	
	/**
	 * Connects the vertices with ids <code>source</code> and <code>target</code> in all possible
	 * ways such that the resulting embedding is 2-planar.
	 * @param iEmb		embedding to start with
	 * @param source	id of source vertex
	 * @param target	id of target vertex
	 * @return			list of (intermediate) embeddings
	 */
	protected List<IntermediateEmbedding> connectNodes(IntermediateEmbedding iEmb, int source, int target) {
		List<IntermediateEmbedding> newEmbeddings = new LinkedList<IntermediateEmbedding>();
		
		Embedding  emb           = iEmb.emb;
		int        newVertexId   = iEmb.newVertexId;
		List<Face> incidentFaces = emb.getIncidentFacesToVertex(source);
		
		Set<Integer> prohibitedEdges = new HashSet<Integer>();
		prohibitedEdges.addAll(emb.getWholeIncidentEdgesToVertex(source));
		prohibitedEdges.addAll(emb.getWholeIncidentEdgesToVertex(target));
				
		for (Face f : incidentFaces) {
			// 0 crossing for the new edge
			if (emb.hasFaceVertex(f.getId(), target)) {
				Embedding newEmb = getCopy(emb);
				newEmb.insertEdge(source, target, f.getId(),-1);
				newEmbeddings.add(new IntermediateEmbedding(newEmb, newVertexId));
				insertionPossible = true;
				if (PRINT) {
					System.out.println("Found new intermetiate embedding - ID: " + newEmb.getId() + " (0 crossings 2nd edge)");
				}
			}
		}
		
		if (kPlanarity == 0) {
			return newEmbeddings;
		}

		for (Face f : incidentFaces){
			// 1 crossing for the new edge			
			connectNodesHelper(
					emb,
					target,
					newVertexId,
					newEmbeddings,
					f.getId(),
					prohibitedEdges,
					source,
					-1,
					1);
		}
		return newEmbeddings;
	}

	

	
	/**
	 * Creates an edge from a new vertex to some other vertex.
	 * @param emb                  current drawing
	 * @param finalTarget          target of the edge
	 * @param newVertexId          id of the newly inserted vertex
	 * @param newEmbeddings        list of newly created embedding (so far)
	 * @param faceId               id of the considered face
	 * @param prohibitedEdges      edges that may not be crossed
	 * @param currentSource        source of edge (part) to be inserted
	 * @param edgeBeforeCrossingId edge before the crossing (if the currentSource is a crossing, -1 otherwise)
	 * @param depth                number of edges already crossed
	 */
	private void connectNodesHelper(Embedding emb, int finalTarget, int newVertexId, List<IntermediateEmbedding> newEmbeddings,
			int faceId, Set<Integer> prohibitedEdges, int currentSource, int edgeBeforeCrossingId, int depth) {		

		//get incident edges from current face
		List<Edge> incidentEdgesTemp = emb.getIncidentEdgesToFace(faceId);
		List<Edge> incidentEdges = new LinkedList<Edge>();
		for (Edge e : incidentEdgesTemp) {
			if (!prohibitedEdges.contains(e.getId()) &&	emb.getNumberOfCrossings(e) < kPlanarity) {
				incidentEdges.add(e);
			}
		}
		
		for (Edge e : incidentEdges) {
			Embedding embCrossingAdded  = getCopy(emb);
			int       newCrossing       = embCrossingAdded.subdivideEdge(e.getId(), true).getId();
			int       edgeToNewCrossing = embCrossingAdded.insertEdge(
					currentSource, newCrossing, faceId, edgeBeforeCrossingId).getId();
			
			// copy the prohibited edges and insert the new ones
			Set<Integer> currentlyProhibitedEdges = new HashSet<Integer>();
			for (Integer edgeId : prohibitedEdges) {
				currentlyProhibitedEdges.add(edgeId);
			}
			currentlyProhibitedEdges.addAll(embCrossingAdded.getWholeIncidentEdgesToVertex(newCrossing));
			
			int faceOfTwin = e.getTwin().getFace().getId();
			
			//create new edge
			if (embCrossingAdded.hasFaceVertex(faceOfTwin, finalTarget)) {
				Embedding newEmb = getCopy(embCrossingAdded);
				newEmb.insertEdge(newCrossing, finalTarget, faceOfTwin, edgeToNewCrossing);
				newEmbeddings.add(new IntermediateEmbedding(newEmb, newVertexId));
				insertionPossible = true;
				if (PRINT) {
					System.out.println("Found new intermetiate embedding - ID: " + newEmb.getId() + " (1 crossing 2nd edge)");
				}
			}
			
			if (depth < kPlanarity) {
				connectNodesHelper(
						embCrossingAdded,
						finalTarget,
						newVertexId,
						newEmbeddings,
						faceOfTwin,
						currentlyProhibitedEdges,
						newCrossing,
						edgeToNewCrossing,
						depth+1);
			}
		}
		
	}
}
