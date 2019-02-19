package edgeInserter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import data.Edge;
import data.Embedding;
import data.Face;

public class EdgeInserterStrange extends EdgeInserter {
	
	private final static boolean PRINT = false;
	
	private Face outerFace;
	
	/**
	 * Creates a new <code>NodeInserter</code>.
	 */
	public EdgeInserterStrange(Face outerFace) {
		super();
		this.outerFace = outerFace;
	}
	
	public EdgeInserter copy() {
		return copyFields(new EdgeInserterStrange(outerFace));
	}
	
	
	/**
	 * Inserts the new vertex in the source embedding (in all possible faces)
	 * and creates an edge to it (in all possible ways).
	 * @param firstVertex vertex that should be connected to the new vertex
	 */
	protected void createEdgeToFirstVertex(int firstVertex) {
		// not needed
	}	
	
	
	/**
	 * Connects the vertices with ids <code>source</code> and <code>target</code> in all possible
	 * ways such that the resulting embedding is 2-planar.
	 * @param iEmb		embedding to start with
	 * @param source	id of source vertex
	 * @param target	id of target vertex
	 * @return			list of (intermediate) embeddings
	 */
	protected List<Embedding> connectNodes(Embedding emb, int source, int target) {
		List<Embedding> newEmbeddings = new LinkedList<Embedding>();
		
		List<Face> incidentFaces = emb.getIncidentFacesToVertex(source);
		incidentFaces.remove(outerFace);
		
		Set<Integer> prohibitedEdges = new HashSet<Integer>();
		List<Edge> outerEdges     = emb.getIncidentEdgesToFace(outerFace.getId());
		for (Edge e : outerEdges) {
			prohibitedEdges.add(e.getId());
			prohibitedEdges.add(e.getTwin().getId());
		}
				
		for (Face f : incidentFaces) {
			// 0 crossing for the new edge
			if (emb.hasFaceVertex(f.getId(), target)) {
				Embedding newEmb = getCopy(emb);
				newEmb.insertEdge(source, target, f.getId(),-1);
				newEmbeddings.add(newEmb);
				insertionPossible = true;
				if (PRINT) {
					System.out.println("Found new intermetiate embedding - ID: " + newEmb.getId() + " (0 crossings 2nd edge)");
				}
			}
		}

		for (Face f : incidentFaces){
			// 1 crossing for the new edge			
			connectNodesHelper(
					emb,
					target,
					newEmbeddings,
					f.getId(),
					prohibitedEdges,
					source,
					-1);
		}
		return newEmbeddings;
	}

	

	
	
	private void connectNodesHelper(Embedding emb, int finalTarget, List<Embedding> newEmbeddings,
			int faceId, Set<Integer> prohibitedEdges, int currentSource, int edgeBeforeCrossingId) {		
		
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
			for (Integer edgeId : prohibitedEdges) {
				currentlyProhibitedEdges.add(edgeId);
			}

			int newCrossing       = embCrossingAdded.subdivideEdge(e.getId(), true).getId();
			int edgeToNewCrossing = embCrossingAdded.insertEdge(
					currentSource, newCrossing, faceId, edgeBeforeCrossingId).getId();

			currentlyProhibitedEdges.addAll(embCrossingAdded.getWholeIncidentEdgesToVertex(newCrossing));
			
			
			int faceOfTwin = e.getTwin().getFace().getId();
			if (faceOfTwin == outerFace.getId()) {
				continue;
			}
			
			//create new edge
			if (embCrossingAdded.hasFaceVertex(faceOfTwin, finalTarget)) {
				Embedding newEmb = getCopy(embCrossingAdded);
				newEmb.insertEdge(newCrossing, finalTarget, faceOfTwin, edgeToNewCrossing);
				newEmbeddings.add(newEmb);
				insertionPossible = true;
				if (PRINT) {
					System.out.println("Found new intermetiate embedding - ID: " + newEmb.getId() + " (1 crossing 2nd edge)");
				}
			}

			connectNodesHelper(
					embCrossingAdded,
					finalTarget,
					newEmbeddings,
					faceOfTwin,
					currentlyProhibitedEdges,
					newCrossing,
					edgeToNewCrossing);
		}
	}
}
