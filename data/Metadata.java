package data;

/**
 * Class for the metadata of the drawings for a certain graph. 
 * @author tommy
 *
 */
public class Metadata {
	
	private int numberEmbeddingsTotal;			// total number of drawings stored on disc
	private int numberEmbeddingsTopoDifferent;  // total number of non-isomorphic drawings stored on dics
	private int noTotalFound;					// total number of calculated drawings
	
	private int idStartEmbedding;				// id of a drawing
	
	/**
	 * Creates new <code>Metadata</code>.
	 */
	public Metadata() {}
	
	/**
	 * Creates new <code>Metadata</code>.
	 * @param numberEmbeddingsTotal			total number of drawings
	 * @param numberEmbeddingsTopoDifferent	total number of non-isomorphic drawings
	 * @param idStartEmbedding				id of a drawing
	 */
	public Metadata(int numberEmbeddingsTotal, int numberEmbeddingsTopoDifferent,
			int idStartEmbedding) {
		this.numberEmbeddingsTotal         = numberEmbeddingsTotal;
		this.numberEmbeddingsTopoDifferent = numberEmbeddingsTopoDifferent;
		this.idStartEmbedding              = idStartEmbedding;
	}


	/**
	 * Returns the total number of drawings
	 * @return	number of drawings
	 */
	public int getNumberEmbeddingsTotal() {
		return numberEmbeddingsTotal;
	}
	/**
	 * Sets the total number of drawings.
	 * @param numberEmbeddingsTotal	number of drawings
	 */
	public void setNumberEmbeddingsTotal(int numberEmbeddingsTotal) {
		this.numberEmbeddingsTotal = numberEmbeddingsTotal;
	}

	/**
	 * Returns the total number on non-isomorphic drawings.
	 * @return	number of non-isomorphic drawings
	 */
	public int getNumberEmbeddingsTopoDifferent() {
		return numberEmbeddingsTopoDifferent;
	}
	/**
	 * Sets the total number of non-isomorphic drawings.
	 * @param numberEmbeddingsTopoDifferent	numeber of non-isomorphic drawings
	 */
	public void setNumberEmbeddingsTopoDifferent(int numberEmbeddingsTopoDifferent) {
		this.numberEmbeddingsTopoDifferent = numberEmbeddingsTopoDifferent;
	}

	/**
	 * Returns the id of the start embedding (usually the first drawing in a folder).
	 * @return id of start embedding
	 */
	public int getIdStartEmbedding() {
		return idStartEmbedding;
	}
	/**
	 * Sets the start embedding. 
	 * @param idStartEmbedding id of an embedding
	 */
	public void setIdStartEmbedding(int idStartEmbedding) {
		this.idStartEmbedding = idStartEmbedding;
	}
	
	/**
	 * Return the number of drawings that were created.
	 * @return number of created drawings
	 */
	public int getNumberEmbeddingsTotalFound() {
		return noTotalFound;
	}
	/**
	 * Sets the number of drawings that were created in total.
	 * @param noTotalFound	numeber of created drawings
	 */
	public void setNumberEmbeddingsTotalFound(int noTotalFound) {
		this.noTotalFound = noTotalFound;
	}
	
}
