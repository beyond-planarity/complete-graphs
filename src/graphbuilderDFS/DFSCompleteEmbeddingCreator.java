package graphbuilderDFS;

import java.util.LinkedList;
import java.util.List;

import data.Embedding;
import data.Face;
import data.Metadata;
import data.Vertex;
import embeddingComparator.EmbeddingComparator;
import io.safeLoad.SafeLoad;
import nodeInserter.NodeInserter;

/**
 * Class to explore all complete embeddings of a specific beyond-planarity class.
 * @author tommy
 *
 */
public class DFSCompleteEmbeddingCreator extends DFSEmbeddingCreator {

	private int totalCreated = 0;
	private int totalTopo    = 0;
	
	
	public DFSCompleteEmbeddingCreator(NodeInserter nodeInserter, String graphClassName) {
		super(nodeInserter, graphClassName, "_5");
	}
	
	protected List<Embedding> createPlanarStartEmbeddings(boolean useGaps) {
		System.out.println("Creating K_{3} ...");
		
		Embedding k3 = new Embedding(getNextId());
		
		Vertex v1 = k3.createVertex(Embedding.SET1_LETTER + "1");
		Vertex v2 = k3.createVertex(Embedding.SET1_LETTER + "2");
		Vertex v3 = k3.createVertex(Embedding.SET1_LETTER + "3");
		
		Face f1   = k3.createFace();
		
		k3.insertEdge(v1.getId(), v2.getId(), f1.getId(), -1);
		k3.insertEdge(v1.getId(), v3.getId(), f1.getId(), -1);
		k3.insertEdge(v2.getId(), v3.getId(), f1.getId(), -1);
		
		List<Embedding> startEmbs = new LinkedList<Embedding>();
		startEmbs.add(k3);
		return startEmbs;
	}
	
	
	protected List<Embedding> createBaseEmbeddings(List<Embedding> startEmbs) {
		System.out.println("Creating K_{4} ...");		
		String          newNodeName   = Embedding.SET1_LETTER + "4";
	    List<Embedding> k4embeddings = new LinkedList<Embedding>();
	    for (Embedding emb : startEmbs) {
	    	k4embeddings.addAll(insertNewNode(emb, newNodeName, true, false));
	    }
		System.out.println("Created all K_4 embeddings. Removing topologically equivalent embeddings now.");
		k4embeddings = EmbeddingComparator.compareEmbeddingsRAM(k4embeddings, getFolderName(4));

		System.out.println("Creating K_{5} ...");
		newNodeName = Embedding.SET1_LETTER + "5";
	    List<Embedding> k5embeddings = new LinkedList<Embedding>();
		for (Embedding emb : k4embeddings) {
			k5embeddings.addAll(insertNewNode(emb, newNodeName, true, false));
		}
		System.out.println("Created all K_5 embeddings. Removing topologically equivalent embeddings now.");
		k5embeddings = EmbeddingComparator.compareEmbeddingsRAM(k5embeddings, getFolderName(5));

		// update the links in the embeddings
		updateLinks(k5embeddings);
		
		return k5embeddings;
	}
	
	
	@Override
	public void calculateEmbeddings(int firstK5EmbeddingId, int lastK5EmbeddingId) {
		if (SafeLoad.isEmpty(baseFolderName)) {
			return;
		}

		Metadata baseMeta = SafeLoad.loadMetadata(baseFolderName);
		if (firstK5EmbeddingId < 0) {
			lastK5EmbeddingId = 0;
		}
		if (lastK5EmbeddingId >= baseMeta.getNumberEmbeddingsTotal()) {
			lastK5EmbeddingId = baseMeta.getNumberEmbeddingsTotal() - 1;
		}
		
		System.out.println("Calculating drawings for base embeddings " + firstK5EmbeddingId + " - " + lastK5EmbeddingId);
		
		for (int id = firstK5EmbeddingId; id <= lastK5EmbeddingId; id++) {
			// calculate all embeddings for the current id as 'deep' as possible
			Embedding emb = SafeLoad.loadEmbedding(baseFolderName, id);
			calculateEmbeddings(emb, "" + id + "/" + lastK5EmbeddingId, 6);
		}

		System.out.println("Finished. Total created in recursion: " + totalCreated + " - Total topologically equivalent: " + (totalCreated - totalTopo));
	}
	
	private void calculateEmbeddings(Embedding emb, String currentIds, int vertexNo) {
		if(isTimeToPrint()) {
			System.out.println("Current branch: " + currentIds + " - Vertex: " + vertexNo);
		}
		
		String          newNodeName   = Embedding.SET1_LETTER + vertexNo;
		List<Embedding> newEmbeddings = insertNewNode(emb, newNodeName, true, false);
		
		totalCreated += newEmbeddings.size();
		
		newEmbeddings = EmbeddingComparator.compareEmbeddingsRAM(newEmbeddings, currentIds);
		

		totalTopo    += newEmbeddings.size();
		
		updateLinks(newEmbeddings);
		
		if (newEmbeddings.isEmpty()) {
			String folderName = getFolderName(vertexNo-1);
			handleNoEmbeddingsFound(emb, folderName, "" + (vertexNo - 1));
		}
		else {
			for (int id = 0; id<newEmbeddings.size(); id++) {
				calculateEmbeddings(newEmbeddings.get(id), currentIds + "|" + id + "/" + newEmbeddings.size(), vertexNo + 1);
			}
		}
		
	}
	
}
