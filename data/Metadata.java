package data;

public class Metadata {
	
	private int numberEmbeddingsTotal;
	private int numberEmbeddingsTopoDifferent;
	private int noTotalFound;
	
	private int idStartEmbedding;
	
	
	public Metadata() {}
	
	public Metadata(int numberEmbeddingsTotal, int numberEmbeddingsTopoDifferent,
			int idStartEmbedding) {
		this.numberEmbeddingsTotal         = numberEmbeddingsTotal;
		this.numberEmbeddingsTopoDifferent = numberEmbeddingsTopoDifferent;
		this.idStartEmbedding              = idStartEmbedding;
	}


	public int getNumberEmbeddingsTotal() {
		return numberEmbeddingsTotal;
	}


	public void setNumberEmbeddingsTotal(int numberEmbeddingsTotal) {
		this.numberEmbeddingsTotal = numberEmbeddingsTotal;
	}


	public int getNumberEmbeddingsTopoDifferent() {
		return numberEmbeddingsTopoDifferent;
	}


	public void setNumberEmbeddingsTopoDifferent(int numberEmbeddingsTopoDifferent) {
		this.numberEmbeddingsTopoDifferent = numberEmbeddingsTopoDifferent;
	}


	public int getIdStartEmbedding() {
		return idStartEmbedding;
	}


	public void setIdStartEmbedding(int idStartEmbedding) {
		this.idStartEmbedding = idStartEmbedding;
	}
	
	
	public int getNumberEmbeddingsTotalFound() {
		return noTotalFound;
	}

	public void setNumberEmbeddingsTotalFound(int noTotalFound) {
		this.noTotalFound = noTotalFound;
	}
	

}
