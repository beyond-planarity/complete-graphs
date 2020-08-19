package graphbuilderDFS;

import java.util.List;

import data.Constant;
import data.Embedding;
import data.Metadata;
import io.safeLoad.SafeLoad;
import nodeInserter.NodeInserter;

/**
 * Class to create embeddings in a DFS-like manner.
 * @author tommy
 *
 */
public abstract class DFSEmbeddingCreator {
	
	private   int          embeddingId = -1;
	protected NodeInserter nodeInserter;
	protected String       graphClassName;
	protected String       baseFolderName;


	/**
	 * Creates a new <code>GraphCreator</code>
	 * @param nodeInserter    the node inserter that must be used
	 * @param graphClassName  the name of the graph class
	 * @param baseFolderIndex the index of the folder for the base embeddings
	 */
	public DFSEmbeddingCreator(NodeInserter nodeInserter, String graphClassName, String baseFolderIndex) {
		this.nodeInserter   = nodeInserter;
		this.graphClassName = graphClassName;
		this.baseFolderName = graphClassName + baseFolderIndex;
	}
	
	
	/**
	 * Returns the next available id for an embedding.
	 * @return id
	 */
	protected int getNextId() {
		embeddingId++;
		return embeddingId;
	}
	
	
	/**
	 * At initialization the following steps must be executed:
	 * 1. a planar drawing of the startbase must be created;
	 * 2. all planar drawings of the base embeddings are computed;
	 * 3. only topologically different drawings of the base embeddings are kept.
	 */
	public void init(boolean useGaps) {
		List<Embedding> startEmbs = createPlanarStartEmbeddings(useGaps);
		SafeLoad.createFolder(baseFolderName);
		List<Embedding> baseEmbs = createBaseEmbeddings(startEmbs);
		
		safeBaseEmbeddings(baseEmbs);
		System.out.println("Found " + baseEmbs.size() + " base embeddings.");
	}
	
	/**
	 * Prints the number of the base embeddings.
	 */
	public void printNoBaseEmbeddings() {
		if (SafeLoad.hasMetaData(baseFolderName)) {
			Metadata meta = SafeLoad.loadMetadata(baseFolderName);
			System.out.println(baseFolderName + " contains " + meta.getNumberEmbeddingsTotal() + " embeddings.");
		}
		else {
			System.out.println("No base embeddings.");
		}
	}


	/**
	 * Creates the embedding to start with.
	 * @param useGaps set it to true, if the crossings should be considered as gaps
	 * @return the start embedding
	 */
	protected abstract List<Embedding> createPlanarStartEmbeddings(boolean useGaps);

	
	/**
	 * Creates all topologically different base embeddings, starting with <code>startEmbedding</code>.
	 * @param startEmbeddings the embedding to start with
	 * @return list of all base embeddings
	 */
	protected abstract List<Embedding> createBaseEmbeddings(List<Embedding> startEmbeddings);
	
	/**
	 * Updates the links of a list of embeddings. Thereby the ID's are numbered increasingly started from 0. 
	 * @param embeddings list of embeddings
	 */
	protected void updateLinks(List<Embedding> embeddings) {
		if (embeddings.size() > 0) {
			// change ids
			for (int i = 0; i<embeddings.size(); i++) {
				Embedding emb = embeddings.get(i);
				emb.setId(i);
				emb.setReferenceEmbeddingId(emb.getId());
				emb.setNextEmbedding(i+1);
				emb.setNextTopoEmbedding(i+1);
				emb.setPrevEmbedding(i-1);
				emb.setPrevTopoEmbedding(i-1);
			}

			Embedding emb = embeddings.get(0);
			emb.setPrevEmbedding(embeddings.size() - 1);
			emb.setPrevTopoEmbedding(embeddings.size() - 1);
			Embedding lastEmb = embeddings.get(embeddings.size() - 1);
			lastEmb.setNextEmbedding(0);
			lastEmb.setNextTopoEmbedding(0);
		}
	}
	
	/**
	 * Saves all the base embeddings.
	 * @param baseEmbeddings
	 */
	private void safeBaseEmbeddings(List<Embedding> baseEmbeddings) {
		for (Embedding emb : baseEmbeddings) {
			SafeLoad.safeEmbedding(emb, baseFolderName);
		}
		// save metadata
		SafeLoad.safeMetadata(baseFolderName, new Metadata(baseEmbeddings.size(), baseEmbeddings.size(), 0));
	}
	
	
	
	private long time = System.currentTimeMillis();
	/**
	 * Decides if the current status should be printed.
	 * @return
	 */
	protected boolean isTimeToPrint() {
		long newTime = System.currentTimeMillis();
		if (newTime - time > Constant.DFS_PRINT) {
			time = newTime;
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * Resets the time to the current time.
	 */
	protected void resetTime() {
		time = System.currentTimeMillis();
	}
	

	
	/**
	 * Inserts a new vertex into the given embeddings.
	 * @param embeddings	list of embeddings
	 * @param newNodeName	name of the new vertex
	 * @param useSet1		if true, the new vertex is connected to all the vertices in the first bipartite set
	 * @param vertexSetSwitchNeeded if true, the bipartite sets must be switch in order to obtain all drawnigs
	 * @return
	 */
	protected List<Embedding> insertNewNode(Embedding embedding, String newNodeName, boolean useSet1, boolean vertexSetSwitchNeeded) {
		nodeInserter.init(
				embedding,
				useSet1 ? embedding.getVertexSet1() : embedding.getVertexSet2(),
				embeddingId,
				newNodeName);
		nodeInserter.insertAll();
		List<Embedding> embs = nodeInserter.getCreatedEmbeddings();

		if (vertexSetSwitchNeeded) {
			Embedding switchedEmb = embedding.copy(embedding.getId());
			switchedEmb.switchNames();
			nodeInserter.init(
					switchedEmb,
					useSet1 ? embedding.getVertexSet1() : embedding.getVertexSet2(),
					embeddingId,
					newNodeName);
			embs.addAll(nodeInserter.getCreatedEmbeddings());
		}
		
		return embs;
	}
	
	
	
		
	/**
	 * Searches for embeddings that do not allow to insert more vertices.
	 * If such a drawing is found, it is saved while there is no other such drawing with the
	 * same number of crossings.
	 * The search is started from the embeddings in the base Folder. Thereby in each step a single vertex is added.
	 * 
	 * @param firstBaseEmbeddingId id of the embeddings to start with (first id is 0)
	 * @param lastBaseEmbeddingId  id of the embeddings to stop the search
	 */
	public abstract void calculateEmbeddings(int firstBaseEmbeddingId, int lastBaseEmbeddingId);
	
	
	protected void handleNoEmbeddingsFound(Embedding emb, String folderName, String graphIndex) {
		if (SafeLoad.hasFolder(folderName)) {
			if (!SafeLoad.hasEmbedding(folderName, emb.getCrossingNumber())) {
				Metadata meta = SafeLoad.loadMetadata(folderName);
				SafeLoad.safeEmbedding(emb.copy(emb.getCrossingNumber()), folderName);
				int noEmbs = meta.getNumberEmbeddingsTotal() + 1;
				meta.setNumberEmbeddingsTotal(noEmbs);
				meta.setNumberEmbeddingsTopoDifferent(noEmbs);
				SafeLoad.safeMetadata(folderName, meta);
				System.out.println("Saved embedding: " + folderName + ", " + emb.getCrossingNumber() + " crossings.");
			}
		}
		else {
			SafeLoad.createFolder(folderName);
			Metadata meta = new Metadata(1, 1, emb.getCrossingNumber());
			SafeLoad.safeEmbedding(emb.copy(emb.getCrossingNumber()), folderName);
			SafeLoad.safeMetadata(folderName, meta);
			System.out.println("Saved embedding: " + folderName + " vertices, " + emb.getCrossingNumber() + " crossings.");
		}
	}

	
	/**
	 * Returns the folder name for the vertex with number <code>vertex</code>.
	 * This is for complete graphs.
	 * @param vertex 	number of a vertex
	 * @return			folder name
	 */
	public String getFolderName(int vertex) {
		return graphClassName + "_" + vertex;
	}

	/**
	 * Returns the folder name for the vertices with numbers <code>vertex1</code> and <code>vertex2</code>.
	 * This is for complete graphs.
	 * @param vertex1	number of first vertex
	 * @param vertex2	number of second vertex
	 * @return			folder name
	 */
	public String getFolderName(int vertex1, int vertex2) {
		return graphClassName + "_" + vertex1 + "_" + vertex2;
	}


	/**
	 * Calculates the embeddings. Saves an embedding on hard disc, if it is not possible
	 * to insert more vertices (of a certain set) into it, and if there is no other
	 * embedding already saved with the same crossing number.
	 * @param emb				embedding to insert into
	 * @param newIds			id's of new vertices (for printing)
	 * @param vertex1			vertex of first bipartite set
	 * @param vertex2			vertex of second bipartite set
	 * @param extendSet1		true, if next vertex should be inserted into first bipartite set
	 * @param alternatingMode	true, if vertices should inserted alternating into first and second bipartite set
	 */
	protected void calculateEmbeddings(Embedding emb, String newIds, int vertex1, int vertex2,
			boolean extendSet1, boolean alternatingMode) {}
}
