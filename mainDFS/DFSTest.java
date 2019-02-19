package mainDFS;

import drawing.DrawerDFS;
import drawing.DrawingPositioner;
import graphbuilderDFS.DFSEmbeddingCreator;

public class DFSTest {
	
	private DFSEmbeddingCreator graphCreator;
	
	public DFSTest(DFSEmbeddingCreator graphCreator) {
		this.graphCreator = graphCreator;
	}
	
	public void init(boolean useGaps) {
		graphCreator.init(useGaps);
	}
	
	public void printNoBaseEmbeddings() {
		graphCreator.printNoBaseEmbeddings();
	}
	
	
	public void run(int startIndex, int endIndex) {
		if (startIndex > endIndex) {
			endIndex = startIndex;
		}		
		graphCreator.calculateEmbeddings(startIndex, endIndex);
	}
	
	
	public void prepareDrawing(int vertex) {
		new DrawingPositioner(graphCreator.getFolderName(vertex)).calcPosAndColorSmallSize("");
	}
	public void prepareDrawing(int vertex1, int vertex2) {
		new DrawingPositioner(graphCreator.getFolderName(vertex1, vertex2)).calcPosAndColorSmallSize("");
	}
	
	public void draw(int vertex) {
		new DrawerDFS(graphCreator.getFolderName(vertex)).show();
	}
	public void draw(int vertex1, int vertex2) {
		new DrawerDFS(graphCreator.getFolderName(vertex1, vertex2)).show();
	}
}
