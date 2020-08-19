package drawing;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import data.Edge;
import data.Embedding;
import data.Face;
import data.Vertex;

/**
 * Class to calculate a canonical order for the vertices of a triangulated embedding. 
 * @author tommy
 *
 */
public class CanonicalOrder {
	
	private final Embedding                       triangulation;
	private 	  Vertex[]                        vertexOrder;
	private       HashMap<Integer, List<Integer>> children;      // adjacent vertices that come later in the vertex order
	
	/**
	 * Creates a new <code>CanonicalOrder</code>.
	 * @param triangulation	a triangulated embedding
	 */
	public CanonicalOrder(final Embedding triangulation) {
		this.triangulation = triangulation;
		this.vertexOrder   = new Vertex[triangulation.getNumberVerices()];
		this.children      = new HashMap<Integer, List<Integer>>();
		
		List<Embedding> embeddings = new LinkedList<>();
		embeddings.add(triangulation);
	}
	
	/**
	 * Calculates a canonical order.
	 */
	public void calculate() {
		
		HashMap<Integer, Vertex> vertices = triangulation.getVertices();
		HashMap<Integer, Face>   faces    = triangulation.getFaces();

		HashMap<Integer, Integer> chords        = new HashMap<Integer, Integer>();
		List<Vertex>              onOuterCircle = new LinkedList<Vertex>();
		List<Edge>                outerCircle   = new LinkedList<Edge>();

		for (Vertex v : vertices.values()) {
			chords.put(v.getId(), 0);
			children.put(v.getId(), new LinkedList<Integer>());
		}

		// determine outer face (randomly, use the first face)
		Face outerFace = null;
		for (Face f : faces.values()) {
			outerFace = f;
			break;
		}
		if (outerFace == null) {
			// there are no faces at all in the embedding
			return;
		}

		Edge e = outerFace.getIncidentEdge();
		vertexOrder[1] = e.getSource();
		vertexOrder[0] = e.getTarget();
		onOuterCircle.add(e.getTarget());
		onOuterCircle.add(e.getNext().getTarget());
		onOuterCircle.add(e.getSource());
		outerCircle.add(e.getNext().getNext().getTwin());
		outerCircle.add(e.getNext().getTwin());
		
		//System.out.println("outerCircle 0 " + outerCircle.get(0).getId() + " - source: " + outerCircle.get(0).getSource().getId() + " - target: " + outerCircle.get(0).getTarget().getId());
		//System.out.println("outerCircle 1 " + outerCircle.get(1).getId() + " - source: " + outerCircle.get(1).getSource().getId() + " - target: " + outerCircle.get(1).getTarget().getId());
		

		for (int k=vertexOrder.length-1; k>1; k--) {
			//System.out.println("k: " + k + " - outerCircle size: " + outerCircle.size());
			// chose v != v_0,v_1 such that v on outer face, not considered yet and chords(v)=0
			Vertex nextVertex   = null;
			int    nextVertexId = -1;
			for (int i=0; i<onOuterCircle.size(); i++) {
				nextVertex   = onOuterCircle.get(i);
				nextVertexId = nextVertex.getId();
				if (nextVertexId != vertexOrder[0].getId() && nextVertexId != vertexOrder[1].getId()
						&& chords.get(nextVertexId) == 0) {
					// remove from list
					onOuterCircle.remove(i);
					break;
				}
			}
			
			//System.out.println("nextVertexId: " + nextVertexId);

			// found the next vertex; add it to the considered vertices
			vertexOrder[k] = nextVertex;
			
			// determine children
			List<Integer> childrenNextVertex = children.get(nextVertexId);
			
			// update edges of outer circle
			int index = 0;
			
			while (outerCircle.get(index).getTarget().getId() != nextVertexId) {
				index++;
			}
			Edge outofNextVertex = outerCircle.remove(index+1);
			
			
			//System.out.println("outOfNextVertex " + outofNextVertex.getId() + " - source: " + outofNextVertex.getSource().getId() + " - target: " + outofNextVertex.getTarget().getId());
			Edge intoNextVertex  = outerCircle.remove(index);
			//System.out.println("intoNextVertex " + intoNextVertex.getId() + " - source: " + intoNextVertex.getSource().getId() + " - target: " + intoNextVertex.getTarget().getId());
			Edge current = intoNextVertex.getNext();
			//System.out.println("current " + current.getId() + " - source: " + current.getSource().getId() + " - target: " + current.getTarget().getId());
			
			int endIndex = index;
			
			while (current.getId() != outofNextVertex.getId()) {
				Edge onCircle = current.getNext().getTwin();
				outerCircle.add(endIndex, onCircle);
				
				childrenNextVertex.add(0, onCircle.getSource().getId());
				
				endIndex++;
				current = current.getTwin().getNext();
				onOuterCircle.add(onCircle.getTarget());
			}
			
			Edge lastEdge = outofNextVertex.getNext().getTwin();
			outerCircle.add(endIndex, lastEdge);
			
			childrenNextVertex.add(0, lastEdge.getSource().getId());
			childrenNextVertex.add(0, lastEdge.getTarget().getId());

			// update chords
			for (Vertex v : onOuterCircle) {
				Edge incidentEdge = v.getOutEdge();
				int  firstEdgeId  = incidentEdge.getId();
				int  chordCounter = -2; // the 2 neighbours are on the outer circle, but no chords
				do {
					if (onOuterCircle.contains(incidentEdge.getTarget())) {
						chordCounter++;
					}
					incidentEdge = incidentEdge.getTwin().getNext();
				} while (incidentEdge.getId() != firstEdgeId);
				chords.put(v.getId(), chordCounter);
			}
		}
	}

	/**
	 * Returns the calculated canonical order.
	 * @return an array of the vertices in canonical order
	 */
	public Vertex[] getVertexOrder() {
		return vertexOrder;
	}
	
	/**
	 * Returns the children of the vertices. The children were obtained during the calculation
	 * of the canonical order.
	 * @return HashMap containing the children for every vertex
	 */
	public HashMap<Integer, List<Integer>> getChildren() {
		return children;
	}

}
