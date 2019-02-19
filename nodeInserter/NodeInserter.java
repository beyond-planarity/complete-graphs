package nodeInserter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import data.Embedding;

/**
 * Abstract class each <code>NodeInserter</code> should extend.
 * @author Tommy
 *
 */
public abstract class NodeInserter {
		
	private int             ID_COUNTER = 0;
	
	private int             startId;
	protected String        newNodeName;
	
	protected Embedding     embedding;
	protected List<Integer> vertices;
	
	protected List<Embedding>             newEmbeddings;
	protected HashMap<Integer, Integer>   newVertexIds;
	protected List<IntermediateEmbedding> intermediateEmbeddings;

	protected boolean insertionPossible = false;
	
	/**
	 * Creates a new <code>NodeInserter</code>.
	 */
	public NodeInserter() {
	}
	
	/**
	 * Copy the NodeInserte <code>inserter</code>.
	 * @param inserter
	 * @return a copy of the NodeInserter, but with empty list of embeddings.
	 */
	protected NodeInserter copyFields(NodeInserter inserter) {
		inserter.startId     = this.startId;
		inserter.newNodeName = this.newNodeName;
		inserter.embedding   = this.embedding;
		inserter.vertices    = this.vertices;

		inserter.newEmbeddings          = new LinkedList<Embedding>();
		inserter.newVertexIds           = new HashMap<Integer,Integer>();
		inserter.intermediateEmbeddings = new LinkedList<IntermediateEmbedding>();
		return inserter;
	}
	
	/**
	 * Copies this NodeInserter.
	 * @return copy of the NodeInserter
	 */
	public abstract NodeInserter copy();
	
	
	/**
	 * Initializes the <code>NodeInserter</code> for the given embedding.
	 * @param embedding		an embedding
	 * @param vertices		all the vertices the new node should be connected to
	 * @param startId		id for the next embedding that is created
	 * @param newNodeName	name of the new vertex
	 */
	public void init(Embedding embedding, List<Integer> vertices, int startId, String newNodeName) {
		this.embedding   = embedding;
		this.vertices    = vertices;
		this.startId     = startId;
		this.newNodeName = newNodeName;
		ID_COUNTER       = 0;
		newEmbeddings    = new LinkedList<Embedding>();
		newVertexIds     = new HashMap<Integer,Integer>();
		intermediateEmbeddings = new LinkedList<IntermediateEmbedding>();
	}
	
	/**
	 * Creates a copy of the given embedding and taking care of the embedding id.
	 * @param embedding an embedding to copy
	 * @return			the copy of the given embedding
	 */
	public Embedding getCopy(Embedding embedding) {
		Embedding copy = embedding.copy(startId + ID_COUNTER);
		copy.setSourceEmbeddingId(this.embedding.getId());
		ID_COUNTER++;
		return copy;
	}
	
	/**
	 * Class for temporarily saving embeddings.
	 *
	 */
	protected class IntermediateEmbedding {
		Embedding emb;
		int       newVertexId;
		IntermediateEmbedding(Embedding embedding, int vertexId) {
			emb         = embedding;
			newVertexId = vertexId;
		}
	}
	
	/**
	 * Inserts the new vertex in the source embedding (in all possible faces)
	 * and creates an edge to it (in all possible ways).
	 * @param firstVertex vertex that should be connected to the new vertex
	 */
	protected abstract void createEdgeToFirstVertex(int firstVertex);
	
	
	/**
	 * Connects the vertices with ids <code>source</code> and <code>target</code> in all possible
	 * ways such that the resulting embedding is 2-planar.
	 * @param iEmb		embedding to start with
	 * @param source	id of source vertex
	 * @param target	id of target vertex
	 * @return			list of (intermediate) embeddings
	 */
	protected abstract List<IntermediateEmbedding> connectNodes(IntermediateEmbedding iEmb, int source, int target);
	
	
	/**
	 * Insert the new vertice in all the given embeddings.
	 * @return the id of the embedding inserted last
	 */
	public int insertAll() {
		if (vertices.isEmpty()) {
			System.out.println("empty vertices");
			return startId;
		}
		
		// Insert the new vertex into all the given embeddings in all possible ways
		// such that it is connected to the first given vertex
		createEdgeToFirstVertex(vertices.get(0));
		
		// Connect the new vertex to the remaining vertices
		for (int i=1; i<vertices.size(); i++) {
			insertionPossible = false;
			List<IntermediateEmbedding> tempIntEmbs = new LinkedList<IntermediateEmbedding>();
			for (IntermediateEmbedding iEmb : intermediateEmbeddings) {
				tempIntEmbs.addAll(connectNodes(iEmb, iEmb.newVertexId, vertices.get(i)));
			}
			intermediateEmbeddings = tempIntEmbs;
		}
		
		// fill the new embeddings
		for (IntermediateEmbedding ie : intermediateEmbeddings) {
			newEmbeddings.add(ie.emb);
			newVertexIds.put(ie.emb.getId(), ie.newVertexId);
		}
		
		
		return startId + ID_COUNTER;
	}
	
	/**
	 * Returns the newly created embeddings.
	 * @return list of embeddings
	 */
	public List<Embedding> getCreatedEmbeddings() {
		return newEmbeddings;
	}
	
	/**
	 * Returns the id's of the newly created embeddings.
	 * @return HashMap embeddingId → newVertexId
	 */
	public HashMap<Integer, Integer> getNewVertexIds() {
		return newVertexIds;
	}

	/**
	 * Returns if there was created at least one new embedding.
	 * @return true, if there was created at least one embedding
	 */
	public boolean isInsertionPossible() {
		return insertionPossible ;
	}

}
