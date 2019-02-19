package graphbuilderOpt;

import java.util.List;

import data.Embedding;
import data.Metadata;
import data.Statistics;
import io.safeLoad.SafeLoad;
import nodeInserter.NodeInserter;

public abstract class OptEmbeddingCreator {
	
	protected int          embeddingId = -1;
	protected NodeInserter nodeInserter;
	protected String       graphClassName;
	protected String       baseFolderName;
	
	protected String       fromFolder;
	protected String       toFolder;
	
	protected Statistics   stats;
	protected String       statsFile;


	/**
	 * Creates a new <code>OptGraphCreator</code>
	 * @param nodeInserter    the node inserter that must be used
	 * @param graphClassName  the name of the graph class
	 * @param baseFolderIndex the index of the folder for the base embeddings
	 */
	public OptEmbeddingCreator(NodeInserter nodeInserter, String graphClassName, String baseFolderIndex) {
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
		createBaseEmbeddings(useGaps);
		printNoBaseEmbeddings();
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
	 * Creates the embeddings to start with.
	 * @param useGaps set it to true, if the crossings should be considered as gaps
	 * @return the start embeddings
	 */
	protected abstract void createBaseEmbeddings(boolean useGaps);
	
	
	
	/**
	 * Updates the links of a list of embeddings. Thereby the ID's are numbered increasingly started from 0. 
	 * @param embeddings list of embeddings
	 */
	protected void updateLinks(List<Embedding> embeddings, int totalNumber, Embedding lastEmb) {
		// change ids
		for (int i = 0; i<embeddings.size(); i++) {
			Embedding emb = embeddings.get(i);
			emb.setId(totalNumber + i);
			emb.setReferenceEmbeddingId(totalNumber + i);
			emb.setNextEmbedding(totalNumber + i+1);
			emb.setNextTopoEmbedding(totalNumber + i+1);
			emb.setPrevEmbedding(totalNumber + i-1);
			emb.setPrevTopoEmbedding(totalNumber + i-1);
		}

		Embedding emb = embeddings.get(0);
		if (lastEmb != null) {
			emb.setPrevEmbedding(lastEmb.getId());
			emb.setPrevTopoEmbedding(lastEmb.getId());
			lastEmb.setNextEmbedding(emb.getId());
			lastEmb.setNextTopoEmbedding(emb.getId());
		}
	}
	
	
	
	

	
	/**
	 * Inserts a new vertex into the given embeddings.
	 * @param embeddings	list of embeddings
	 * @param newNodeName	name of the new vertex
	 * @param useSet1		if true, the new vertex is connected to all the vertices in the first bipartite set
	 * @param vertexSetSwitchNeeded if true, insertion is also needed for switched bipartite sets
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
					useSet1 ? switchedEmb.getVertexSet1() : switchedEmb.getVertexSet2(),
					embeddingId,
					newNodeName);
			nodeInserter.insertAll();
			embs.addAll(nodeInserter.getCreatedEmbeddings());
		}
		if (!embs.isEmpty()) {
			embedding.setInsertionPossible(true);
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
	public abstract void calculateEmbeddings(int stopAfter, boolean tempAvailable);
	
	
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

	
	public String getFolderName(int vertex) {
		return graphClassName + "_" + vertex;
	}

	public String getFolderName(int vertex1, int vertex2) {
		return graphClassName + "_" + vertex1 + "_" + vertex2;
	}
	

}
