package mainOpt;

import data.Embedding;
import drawing.DrawerOpt;
import drawing.DrawingPositioner;
import graphbuilderOpt.OptEmbeddingCreator;

/**
 * Class to run tests for a graph class on complete and complete bipartite graphs,
 * using the full creation of topologically not equivalent graphs.
 * @author Tommy
 *
 */
public class Test {
	
	OptEmbeddingCreator graphCreator;
	
	public Test(OptEmbeddingCreator graphCreator) {
		this.graphCreator = graphCreator;
	}

	public void init(boolean useGaps) {
		graphCreator.init(useGaps);
	}

	public void printNoBaseEmbeddings() {
		graphCreator.printNoBaseEmbeddings();
	}
	
	public void run(int stopAfter, boolean tempAvailable) {		
		graphCreator.calculateEmbeddings(stopAfter, tempAvailable);
	}
	
	public void prepareDrawing(int vertex) {
		String  newNodeName = Embedding.SET1_LETTER + vertex;
		new DrawingPositioner(graphCreator.getFolderName(vertex)).calcPosAndColorLargeSize(newNodeName);
	}
	public void prepareDrawing(int vertex1, int vertex2, boolean v1IsNew) {
		String  newNodeName = v1IsNew ? Embedding.SET1_LETTER + vertex1 : Embedding.SET2_LETTER + vertex2;
		new DrawingPositioner(graphCreator.getFolderName(vertex1, vertex2)).calcPosAndColorLargeSize(newNodeName);
	}
	
	public void draw(int vertex) {
		new DrawerOpt(graphCreator.getFolderName(vertex), graphCreator.getFolderName(vertex)).show();
	}
	public void draw(int vertex1, int vertex2) {
		new DrawerOpt(graphCreator.getFolderName(vertex1, vertex2), graphCreator.getFolderName(vertex1, vertex2)).show();
	}
	
}
