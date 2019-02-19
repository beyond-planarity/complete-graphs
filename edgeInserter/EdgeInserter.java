package edgeInserter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import data.Embedding;

public abstract class EdgeInserter {
		
	private int             ID_COUNTER = 0;
	
	private int             startId;
	
	protected Embedding     embedding;
	protected Map<Integer, List<Integer>> vertexMap;
	
	protected List<Embedding> newEmbeddings;
	protected List<Embedding> intermediateEmbeddings;

	protected boolean insertionPossible = false;
	
	/**
	 * Creates a new <code>NodeInserter</code>.
	 */
	public EdgeInserter() {
	}
	
	protected EdgeInserter copyFields(EdgeInserter inserter) {
		inserter.startId     = this.startId;
		inserter.embedding   = this.embedding;
		inserter.vertexMap   = this.vertexMap;

		inserter.newEmbeddings          = new LinkedList<Embedding>();
		inserter.intermediateEmbeddings = new LinkedList<Embedding>();
		return inserter;
	}
	
	public abstract EdgeInserter copy();
	
	
	/**
	 * Initializes the <code>NodeInserter</code> for the given embedding.
	 * @param embedding		an embedding
	 * @param vertexMap		all the vertices that should be connected
	 * @param startId		id for the next embedding that is created
	 */
	public void init(Embedding embedding, Map<Integer,List<Integer>> vertexMap, int startId) {
		this.embedding   = embedding;
		this.vertexMap   = vertexMap;
		this.startId     = startId;
		ID_COUNTER       = 0;
		newEmbeddings    = new LinkedList<Embedding>();
		intermediateEmbeddings = new LinkedList<Embedding>();
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
	protected abstract List<Embedding> connectNodes(Embedding iEmb, int source, int target);
	
	
	/**
	 * Insert the new vertice in all the given embeddings.
	 * @return the id of the embedding inserted last
	 */
	public int insertAll() {
		if (vertexMap.isEmpty()) {
			//TODO
			System.out.println("empty vertices");
			return startId;
		}
		
		// Insert the new vertex into all the given embeddings in all possible ways
		// such that it is connected to the first given vertex
		createEdgeToFirstVertex(-1);
		intermediateEmbeddings.add(embedding);
		
		
		for (Entry<Integer, List<Integer>> e : vertexMap.entrySet()) {
			
			int sourceVertex = e.getKey();
			List<Integer> targets = e.getValue();
			
			String strSource = "Creating edge from vertex " + sourceVertex + " to vertex ";

			// Connect the source vertex to the targets
			for (int i=0; i<targets.size(); i++) {
				
				System.out.println(strSource + targets.get(i) + " - no of embbedings: " + intermediateEmbeddings.size() );
				
				insertionPossible = false;
				List<Embedding> tempIntEmbs = new LinkedList<Embedding>();
				for (Embedding iEmb : intermediateEmbeddings) {
					tempIntEmbs.addAll(connectNodes(iEmb, sourceVertex, targets.get(i)));
				}
				intermediateEmbeddings = tempIntEmbs;
				
				//intermediateEmbeddings = OptInternEmbeddingComparator.compareEmbeddings(intermediateEmbeddings, "gon6");
			}
		}
		
		
		// fill the new embeddings
		for (Embedding ie : intermediateEmbeddings) {
			newEmbeddings.add(ie);
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
	 * Returns if there was created at least one new embedding.
	 * @return true, if there was created at least one embedding
	 */
	public boolean isInsertionPossible() {
		return insertionPossible ;
	}

}
