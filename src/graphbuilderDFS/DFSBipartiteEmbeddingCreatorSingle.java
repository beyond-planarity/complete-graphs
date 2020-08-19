package graphbuilderDFS;

import java.util.LinkedList;
import java.util.List;

import data.Edge;
import data.Embedding;
import data.Face;
import data.Metadata;
import data.Vertex;
import embeddingComparator.EmbeddingComparator;
import io.safeLoad.SafeLoad;
import nodeInserter.NodeInserter;

/**
 * Class to explore all complete bipartite embeddings of a specific beyond-planarity class.
 * The exploration is done in all possible ways, that is: given an embedding it will be
 * tried to insert a new node to each of the bipartitional sets. 
 * @author tommy
 *
 */
public class DFSBipartiteEmbeddingCreatorSingle extends DFSEmbeddingCreator {

	private int totalCreated = 0;
	private int totalTopo    = 0;
	
	/**
	 * Creates a new <code>DFSBipartiteEmbeddingCreatorSingle</code>.
	 * @param nodeInserter		node inserter to use
	 * @param graphClassName	name of the graph class
	 */
	public DFSBipartiteEmbeddingCreatorSingle(NodeInserter nodeInserter, String graphClassName) {
		super(nodeInserter, graphClassName, "_3_3");
	}
		
	/**
	 * Creates the two drawings of K_{2,2}.
	 */
	protected List<Embedding> createPlanarStartEmbeddings(boolean useGaps) {
		System.out.println("Creating Start Embeddings ...");
		
		Embedding planark22 = new Embedding(getNextId());
		
		Vertex v1 = planark22.createVertex(Embedding.SET1_LETTER + "1");
		Vertex v2 = planark22.createVertex(Embedding.SET1_LETTER + "2");
		Vertex u1 = planark22.createVertex(Embedding.SET2_LETTER + "1");
		Vertex u2 = planark22.createVertex(Embedding.SET2_LETTER + "2");
		
		Face f1   = planark22.createFace();
		
		planark22.insertEdge(v1.getId(), u1.getId(), f1.getId(), -1);
		planark22.insertEdge(v1.getId(), u2.getId(), f1.getId(), -1);
		planark22.insertEdge(v2.getId(), u1.getId(), f1.getId(), -1);
		planark22.insertEdge(v2.getId(), u2.getId(), f1.getId(), -1);

	
		Embedding unplanark22 = new Embedding(getNextId());
		
		Vertex v21 = unplanark22.createVertex(Embedding.SET1_LETTER + "1");
		Vertex v22 = unplanark22.createVertex(Embedding.SET1_LETTER + "2");
		Vertex u21 = unplanark22.createVertex(Embedding.SET2_LETTER + "1");
		Vertex u22 = unplanark22.createVertex(Embedding.SET2_LETTER + "2");
		
		Face f21   = unplanark22.createFace();
		
		unplanark22.insertEdge(v21.getId(), u21.getId(), f21.getId(), -1);
		Edge edge = unplanark22.insertEdge(v21.getId(), u22.getId(), f21.getId(), -1);
		unplanark22.insertEdge(v22.getId(), u22.getId(), f21.getId(), -1);
		
		Vertex crossing = unplanark22.subdivideEdge(edge.getId(), true);
		Edge crOutE = crossing.getOutEdge();
		Edge v2ToCr = unplanark22.insertEdge(v22.getId(), crossing.getId(), f21.getId(), -1);
		
		Face faceU1 = f21;
		for (Face f : unplanark22.getFaces().values()) {
			if (unplanark22.hasFaceVertex(f.getId(), u21.getId())) {
				faceU1 = f;
			}
		}
		unplanark22.insertEdge(crossing.getId(), u21.getId(), faceU1.getId(), v2ToCr.getId());
		
		if (useGaps) {
			unplanark22.setGapCrossingToEdge(crossing.getId(), crOutE.getId());
		}
		
		List<Embedding> startEmbs = new LinkedList<Embedding>();
		startEmbs.add(planark22);
		startEmbs.add(unplanark22);
		
		return startEmbs;
	}
	
	/**
	 * Creates the base embeddings, that is, all non-isomorphic embeddings of K_{3,3}.
	 */
	protected List<Embedding> createBaseEmbeddings(List<Embedding> startEmbeddings) {
		System.out.println("Creating K_{3,3} ...");
		
		String          newNodeName   = Embedding.SET1_LETTER + "3";
	    List<Embedding> embK23 = new LinkedList<Embedding>();
	    for (Embedding emb : startEmbeddings) {
	    	embK23.addAll(insertNewNode(emb, newNodeName, false, false));
	    }
		System.out.println("Created all K_{2,3} embeddings. Removing topologically equivalent embeddings now.");
		embK23 = EmbeddingComparator.compareEmbeddingsRAM(embK23, getFolderName(2,3));
		System.out.println("Removed topologically equivalent embeddings from K_{2,3}. No of remaining: " + embK23.size());
		
		newNodeName = Embedding.SET2_LETTER + "3";
		List<Embedding> embK33 = new LinkedList<Embedding>();
		for (Embedding emb : embK23) {
			embK33.addAll(insertNewNode(emb, newNodeName, true, false));
		}
		System.out.println("Created all K_{3,3} embeddings. Removing topologically equivalent embeddings now.");
		embK33 = EmbeddingComparator.compareEmbeddingsRAM(embK33, getFolderName(3,3));
		System.out.println("Removed topologically equivalent embeddings from K_{3,3}. No of remaining: " + embK33.size());

		// update the links in the embeddings
		updateLinks(embK33);
		
		return embK33;
	}
	

	@Override
	public void calculateEmbeddings(int firstK4EmbeddingId, int lastK4EmbeddingId) {
		if (SafeLoad.isEmpty(baseFolderName)) {
			return;
		}

		Metadata baseMeta = SafeLoad.loadMetadata(baseFolderName);
		if (firstK4EmbeddingId < 0) {
			lastK4EmbeddingId = 0;
		}
		if (lastK4EmbeddingId >= baseMeta.getNumberEmbeddingsTotal()) {
			lastK4EmbeddingId = baseMeta.getNumberEmbeddingsTotal() - 1;
		}
		
		System.out.println("Calculating drawings for base embeddings " + firstK4EmbeddingId + " - " + lastK4EmbeddingId);
		
		for (int id = firstK4EmbeddingId; id <= lastK4EmbeddingId; id++) {
			// calculate all embeddings for the current id as 'deep' as possible
			Embedding emb = SafeLoad.loadEmbedding(baseFolderName, id);
			calculateEmbeddings(emb, "" + id + "/" + lastK4EmbeddingId, 3, 4, false, true);
		}

		System.out.println("Finished. Total created in recursion: " + totalCreated + " - Total topologically equivalent: " + (totalCreated - totalTopo));
	}
	
	protected void calculateEmbeddings(Embedding emb, String currentIds, int vertex1, int vertex2,
			boolean extendSet1, boolean alternatingMode) {
		if(isTimeToPrint()) {
			System.out.println("Current branch: " + currentIds + " - Vertices: (" + vertex1 + "," + vertex2 + ")");
		}
		
		String          newNodeName   = extendSet1 ? Embedding.SET1_LETTER + vertex1 : Embedding.SET2_LETTER + vertex2;
		List<Embedding> newEmbeddings = insertNewNode(emb, newNodeName, !extendSet1, vertex1+1 == vertex2);
		
		totalCreated += newEmbeddings.size();
		
		newEmbeddings = EmbeddingComparator.compareEmbeddingsRAM(newEmbeddings, currentIds);
		
		totalTopo    += newEmbeddings.size();
		
		updateLinks(newEmbeddings);
		
		if (newEmbeddings.isEmpty()) {
			int fNameV1 = extendSet1 ? vertex1 - 1 : vertex1;
			int fNameV2 = extendSet1 ? vertex2 : vertex2 - 1;
			
			String folderName = getFolderName(fNameV1, fNameV2);
			handleNoEmbeddingsFound(emb, folderName, getGraphIndex(vertex1, vertex2));
		}
		else {
			for (int id = 0; id<newEmbeddings.size(); id++) {
				String newIds = currentIds + "|" + id + "/" + newEmbeddings.size();
				if (vertex1 == vertex2) {
					calculateEmbeddings(newEmbeddings.get(id), newIds, vertex1, vertex2 + 1, false, true);
				}
				else {
					if (alternatingMode) {
						calculateEmbeddings(newEmbeddings.get(id), newIds, vertex1 + 1, vertex2, true, true);
						calculateEmbeddings(newEmbeddings.get(id), newIds, vertex1, vertex2 + 1, false, false);
					}
					else {
						calculateEmbeddings(newEmbeddings.get(id), newIds, vertex1, vertex2 + 1, false, true);
					}
				}
			}
		}
	}
	
	/**
	 * Returns a string for printing the graph indices
	 * @param vertex1	vertices in set 1
	 * @param vertex2	vertices in set 2
	 * @return			string representation
	 */
	protected String getGraphIndex(int vertex1, int vertex2) {
		return "(" + vertex1 + "," + vertex2 + ")";
	}
}
