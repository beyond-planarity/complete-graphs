package graphbuilderDFS;

import java.util.List;

import data.Embedding;
import data.Metadata;
import embeddingComparator.EmbeddingComparator;
import io.safeLoad.SafeLoad;
import nodeInserter.NodeInserter;

/**
 * Class to explore all complete bipartite embeddings of a specific beyond-planarity class.
 * The exploration is done as follows: a new node is first inserted in one of the bipartite sets,
 * and then in the other until one both sets has the size of <code>finalVertex1</code>;
 * then new nodes will be inserted only in the second bipartite set; the first bipartite
 * set will from this point on have always the size of <code>finalVertex1</code>. 
 * @author Tommy
 *
 */
public class DFSBipartiteEmbeddingCreatorDiag extends DFSBipartiteEmbeddingCreatorSingle {
	
	private int totalCreated = 0;
	private int totalTopo    = 0;
	
	private int finalVertex1;


	public DFSBipartiteEmbeddingCreatorDiag(NodeInserter nodeInserter, String graphClassName, int finalVertex1) {
		super(nodeInserter, graphClassName);
		this.finalVertex1 = finalVertex1;
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
			calculateEmbeddings(emb, "" + id + "/" + lastK4EmbeddingId, 3, 4, false);
		}
		
		System.out.println("Finished. Total created in recursion: " + totalCreated + " - Total topologically equivalent: " + (totalCreated - totalTopo));
	}
	
	
	protected void calculateEmbeddings(Embedding emb, String currentIds,
			int vertex1, int vertex2, boolean extendSet1) {
		
		if(isTimeToPrint()) {
			System.out.println("Current branch: " + currentIds + " - Vertices: (" + vertex1 + "," + vertex2 + ")");
		}
		
		String          newNodeName   = extendSet1 ? Embedding.SET1_LETTER + vertex1 : Embedding.SET2_LETTER + vertex2;
		List<Embedding> newEmbeddings = insertNewNode(
				emb, newNodeName, !extendSet1, vertex1 == finalVertex1 && vertex1+1==vertex2);
		
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
					calculateEmbeddings(newEmbeddings.get(id), newIds, vertex1, vertex2 + 1, false);
				}
				else {
					if (vertex1 == finalVertex1) {
						calculateEmbeddings(newEmbeddings.get(id), newIds, vertex1, vertex2 + 1, false);
					}
					else {
						calculateEmbeddings(newEmbeddings.get(id), newIds, vertex1 + 1, vertex2, true);
					}
				}
			}
		}
	}
}
