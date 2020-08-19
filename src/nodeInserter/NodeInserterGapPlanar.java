package nodeInserter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import data.Edge;
import data.Face;
import data.Embedding;


/**
 * A NodeInserter for the class of the gap-planar graphs, that is
 * edges are inserted due to the rules of this graph class.
 * @author Tommy
 *
 */
public class NodeInserterGapPlanar extends NodeInserter {
	
	private final static boolean PRINT = false;
	
	
	/**
	 * Creates a new <code>NodeInserter</code>.
	 */
	public NodeInserterGapPlanar() {
		super();
	}
	
	public NodeInserter copy() {
		return copyFields(new NodeInserterGapPlanar());
	}
	
	
	/**
	 * Inserts the new vertex in the source embedding (in all possible faces)
	 * and creates an edge to it (in all possible ways).
	 * @param firstVertex vertex that should be connected to the new vertex
	 */
	protected void createEdgeToFirstVertex(int firstVertex) {
		List<Face> incidentFaces = embedding.getIncidentFacesToVertex(firstVertex);
		
		Set<Integer> prohibitedEdges = new HashSet<Integer>();
		prohibitedEdges.addAll(embedding.getWholeIncidentEdgesToVertex(firstVertex));
				
		//get all faces from vertex v1
		for (Face f : incidentFaces){
			
			// 0 crossings for first edge
			Embedding embedding0 = getCopy(embedding);
			
			int newVertexId = embedding0.createVertex().getId();
			embedding0.setVertexName(newVertexId, newNodeName);
			embedding0.insertEdge(firstVertex, newVertexId, f.getId(),-1);
			intermediateEmbeddings.add(new IntermediateEmbedding(embedding0, newVertexId));		
			
			if (PRINT) {
				System.out.println("Found new intermetiate embedding - ID: " + embedding0.getId() + " (0 crossings 2nd edge)");
			}
			
			// more crossings for first edge
			Embedding emb = getCopy(embedding);
			
			createEdgeToFirstVertexHelper(emb, f.getId(), prohibitedEdges, firstVertex, -1, false);
		}
	}
	
	

	
	/**
	 * Insert edge to first vertex in all possible ways.
	 * @param emb                  considered embedding
	 * @param faceId               considered face
	 * @param prohibitedEdges      edges not allowed to be crossed
	 * @param sourceId             id of the source vertex
	 * @param edgeBeforeCrossingId id of the edge before the crossing (-1 if there is none)
	 * @param hasGap               true, if the new edge already has a gap
	 */
	private void createEdgeToFirstVertexHelper(Embedding emb, int faceId,
			Set<Integer> prohibitedEdges, int sourceId,	int edgeBeforeCrossingId, boolean hasGap) {
		
		//get incident edges from each vertex that can cross and reach vertex v1
		List<Edge> incidentEdgesTemp = emb.getIncidentEdgesToFace(faceId);
		List<Edge> incidentEdges = new LinkedList<Edge>();
		for (Edge e : incidentEdgesTemp) {
			if (!prohibitedEdges.contains(e.getId())) {
				incidentEdges.add(e);
			}
		}
		
		if (incidentEdges.isEmpty()) {
			return;
		}
		
		for (Edge e : incidentEdges) {
			Embedding embCrossingAdded  = getCopy(emb);
			
			// copy the prohibited edges and insert the new ones
			Set<Integer> currentlyProhibitedEdges = new HashSet<Integer>();
			for (Integer edgeId : prohibitedEdges) {
				currentlyProhibitedEdges.add(edgeId);
			}
			
			int newCrossing       = embCrossingAdded.subdivideEdge(e.getId(), true).getId();
			int edgeToNewCrossing = embCrossingAdded.insertEdge(
					sourceId, newCrossing, faceId, edgeBeforeCrossingId).getId();
			
			currentlyProhibitedEdges.addAll(embCrossingAdded.getWholeIncidentEdgesToVertex(newCrossing));

			int faceOfTwin = e.getTwin().getFace().getId();


			// the current edge e has no gap -> make one
			Embedding newEmbGap = getCopy(embCrossingAdded);

			// make the gap; e gets the gap
			newEmbGap.setGapCrossingToEdge(newCrossing, e.getId());

			Embedding newEmbCopy = getCopy(newEmbGap);

			//create new vertex
			int newVertexId = newEmbGap.createVertex().getId();
			newEmbGap.setVertexName(newVertexId, newNodeName);
			newEmbGap.insertEdge(newCrossing, newVertexId, faceOfTwin, edgeToNewCrossing);
				
				
			if (newEmbGap.areGapsValid()) {
				intermediateEmbeddings.add(new IntermediateEmbedding(newEmbGap, newVertexId));
				
				if (PRINT) {
					System.out.println("Found new intermetiate embedding - ID: " + newEmbGap.getId() + " (0 crossings 2nd edge)");
				}
				
				// copy the prohibited edges (the edge with gap was already added above)
				Set<Integer> currentlyProhibitedEdges2 = new HashSet<Integer>();
				currentlyProhibitedEdges2.addAll(currentlyProhibitedEdges);

				createEdgeToFirstVertexHelper(
						newEmbCopy,
						faceOfTwin,
						currentlyProhibitedEdges2,
						newCrossing,
						edgeToNewCrossing,
						hasGap);
			}

			
			if (!hasGap) {
				// the new edge has no gap; make one
				Embedding newEmb = getCopy(embCrossingAdded);

				// make the gap
				newEmb.setGapCrossingToEdge(newCrossing, edgeToNewCrossing);
				
				Embedding newEmbCopy1 = getCopy(newEmb);
				
				//create new vertex
				int newVertexId1 = newEmb.createVertex().getId();
				newEmb.setVertexName(newVertexId1, newNodeName);
				newEmb.insertEdge(newCrossing, newVertexId1, faceOfTwin, edgeToNewCrossing);
				intermediateEmbeddings.add(new IntermediateEmbedding(newEmb, newVertexId1));
				
				
				if (PRINT) {
					System.out.println("Found new intermetiate embedding - ID: " + newEmb.getId() + " (0 crossings 2nd edge)");
				}
				

				// copy the prohibited edges and insert the new ones
				Set<Integer> currentlyProhibitedEdges3 = new HashSet<Integer>();
				currentlyProhibitedEdges3.addAll(currentlyProhibitedEdges);
				// since the edge to create has now a gap, it cannot cross
				// edges that also have a gap
				for (int edgeId : newEmbCopy1.getGapCrossingsToEdge().values()) {
					currentlyProhibitedEdges3.addAll(newEmbCopy1.getWholeEdge(edgeId));
				}
				
				createEdgeToFirstVertexHelper(
						newEmbCopy1,
						faceOfTwin,
						currentlyProhibitedEdges3,
						newCrossing,
						edgeToNewCrossing,
						true);
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
		

		for (Face f : incidentFaces){

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
			
			
			// more crossings for the new edge			
			connectNodesHelper(
					emb,
					target,
					newVertexId,
					newEmbeddings,
					f.getId(),
					prohibitedEdges,
					source,
					-1,
					false);
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
	 * @param hasGap               true, if the current edge already has a gap
	 */
	private void connectNodesHelper(Embedding emb, int finalTarget, int newVertexId,
			List<IntermediateEmbedding> newEmbeddings, int faceId,
			Set<Integer> prohibitedEdges, int currentSource, int edgeBeforeCrossingId, boolean hasGap) {		
		
		//get incident edges from current face
		List<Edge> incidentEdgesTemp = emb.getIncidentEdgesToFace(faceId);
		List<Edge> incidentEdges = new LinkedList<Edge>();
		for (Edge e : incidentEdgesTemp) {
			if (!prohibitedEdges.contains(e.getId())) {
				incidentEdges.add(e);
			}
		}
		
		for (Edge e : incidentEdges) {
			Embedding embCrossingAdded  = getCopy(emb);
			
			// copy the prohibited edges and insert the new ones
			Set<Integer> currentlyProhibitedEdges = new HashSet<Integer>();
			currentlyProhibitedEdges.addAll(prohibitedEdges);
			
			
			int newCrossing       = embCrossingAdded.subdivideEdge(e.getId(), true).getId();
			int edgeToNewCrossing = embCrossingAdded.insertEdge(
					currentSource, newCrossing, faceId, edgeBeforeCrossingId).getId();

			currentlyProhibitedEdges.addAll(embCrossingAdded.getWholeIncidentEdgesToVertex(newCrossing));
			
			
			int faceOfTwin = e.getTwin().getFace().getId();
			
			// the current edge e has no gap yet
			Embedding newEmbGap = getCopy(embCrossingAdded);
				
			// make the gap; e gets the gap
			newEmbGap.setGapCrossingToEdge(newCrossing, e.getId());
				
			//create new edge to the target, if possible
			boolean areGapsValid = false;
			if (embCrossingAdded.hasFaceVertex(faceOfTwin, finalTarget)) {
				Embedding newFinalGapEmb = getCopy(newEmbGap);
				newFinalGapEmb.insertEdge(newCrossing, finalTarget, faceOfTwin, edgeToNewCrossing);
					
				if (newFinalGapEmb.areGapsValid()) {
					areGapsValid = true;
					newEmbeddings.add(new IntermediateEmbedding(newFinalGapEmb, newVertexId));
					insertionPossible = true;
					
					if (PRINT) {
						System.out.println("Found new intermetiate embedding - ID: " + newFinalGapEmb.getId() + " (1 crossing 2nd edge)");
					}
				}
			}

			if (areGapsValid) {
				// copy the prohibited edges (the edge with gap was already added above)
				Set<Integer> currentlyProhibitedEdges2 = new HashSet<Integer>();
				currentlyProhibitedEdges2.addAll(currentlyProhibitedEdges);
				
				connectNodesHelper(
						newEmbGap,
						finalTarget,
						newVertexId,
						newEmbeddings,
						faceOfTwin,
						currentlyProhibitedEdges2,
						newCrossing,
						edgeToNewCrossing,
						hasGap);
			}
			
			
			if (!hasGap) {
				// the new edge has no gap; make one
				Embedding newEmb = getCopy(embCrossingAdded);

				// make the gap
				newEmb.setGapCrossingToEdge(newCrossing, edgeToNewCrossing);				

				//create new edge to the target, if possible
				if (embCrossingAdded.hasFaceVertex(faceOfTwin, finalTarget)) {
					Embedding newFinalEmb = getCopy(newEmb);
					newFinalEmb.insertEdge(newCrossing, finalTarget, faceOfTwin, edgeToNewCrossing);
					newEmbeddings.add(new IntermediateEmbedding(newFinalEmb, newVertexId));
					insertionPossible = true;
					
					
					if (PRINT) {
						System.out.println("Found new intermetiate embedding - ID: " + newEmb.getId() + " (1 crossing 2nd edge)");
					}
				}
				
				
				// copy the prohibited edges (the edge with gap was already added above)
				Set<Integer> currentlyProhibitedEdges2 = new HashSet<Integer>();
				currentlyProhibitedEdges2.addAll(currentlyProhibitedEdges);
				// since the edge to create has now a gap, it cannot cross
				// edges that also have a gap
				for (int edgeId : newEmb.getGapCrossingsToEdge().values()) {
					currentlyProhibitedEdges2.addAll(newEmb.getWholeEdge(edgeId));
				}
				
				connectNodesHelper(
						newEmb,
						finalTarget,
						newVertexId,
						newEmbeddings,
						faceOfTwin,
						currentlyProhibitedEdges2,
						newCrossing,
						edgeToNewCrossing,
						true);
			}
		}
	}

	
}
