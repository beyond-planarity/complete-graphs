package mainExt;

import data.Embedding;
import data.Tuple;
import drawing.DrawerExt;
import drawing.DrawingPositioner;
import embeddingComparator.EmbeddingComparator;
import graphbuilderExt.BipartiteGraphCreator;
import io.safeLoad.SafeLoad;
import nodeInserter.NodeInserter;

/**
 * Class to create drawings of complete bipartite graphs.
 * @author Tommy
 *
 */
public class TestBipartite {
	
	private String       className;
	private NodeInserter nodeInserter;
	private Tuple[]      graphIndices;
	
	/**
	 * Creates an environment for creating all drawings for certain complete bipartite graphs.
	 * @param className    name of the graph class
	 * @param nodeInserter rules for inserting the new node
	 * @param graphIndices contains a sequence of indices (a,b); for graphs K_{a,b} all drawings are calculated; the drawings for the first index pair must already be calculated
	 */
	public TestBipartite(String className, NodeInserter nodeInserter, Tuple[] graphIndices) {
		this.className    = className;
		this.nodeInserter = nodeInserter;
		this.graphIndices = graphIndices;
	}

	/**
	 * Creates the drawing for graph K_{2,2}.
	 */
	public void createK22() {
		BipartiteGraphCreator.createK22(getFolderName(2, 2));
		EmbeddingComparator.compareEmbeddingsHDExtended(getFolderName(2, 2), -1);
		//new DrawingPositioner(getFolderName(2, 2)).calcPosAndColorLargeSize("");
	}
	

	/**
	 * Creates all the drawings for the specified graphs.
	 */
	public void createKxy() {
		
		if (graphIndices == null || graphIndices.length == 0) {
			System.out.println("No graph indices found.");
			return;
		}
		
		for (int i=0; i<graphIndices.length-1; i++) {
			Tuple current = graphIndices[i];
			Tuple next    = graphIndices[i+1];
			if (current.first + current.second + 1 != next.first + next.second) {
				System.out.println("Sum of graph indices should be increased by 1 in every step.");
				return;
			}			
		}
		
		String firstFolder = getFolderName(graphIndices[0].first, graphIndices[0].second);
		if (SafeLoad.isEmpty(firstFolder)) {
			System.out.println("Folder not found");
			return;
		}
				
		int graphindex = 1;
		while (graphindex < graphIndices.length) {
			String fromFolder = getFolderName(graphIndices[graphindex-1].first, graphIndices[graphindex-1].second);
			String toFolder   = getFolderName(graphIndices[graphindex].first, graphIndices[graphindex].second);
			
			BipartiteGraphCreator graphCreator = new BipartiteGraphCreator(nodeInserter, fromFolder, toFolder);
			if (!graphCreator.createKxy(graphIndices[graphindex-1], graphIndices[graphindex])) {
				break;
			}
			EmbeddingComparator.compareEmbeddingsHDExtended(toFolder, -1);
			
			graphindex++;
		}
	}
	
	
	/**
	 * Prepare the specified graphs for drawing.
	 */
	public void prepareDrawingKxy() {	
		for (int i=1; i<graphIndices.length; i++) {
			Tuple currentInd = graphIndices[i];
			Tuple lastInd    = graphIndices[i-1];
			
			String  newNodeName = Embedding.SET2_LETTER + currentInd.second;
			if (currentInd.first == lastInd.first + 1 && currentInd.second == lastInd.second) {
				newNodeName   = Embedding.SET1_LETTER + currentInd.first;
			}
			
			Tuple t = graphIndices[i];
			new DrawingPositioner(getFolderName(t.first, t.second)).calcPosAndColorLargeSize(newNodeName);
		}
	}

	
	/**
	 * Show all the drawings of the specified complete bipartite graph.
	 * @param x
	 */
	public void drawKxy(int x, int y) {
		new DrawerExt(getFolderName(x, y), "K_" + x + "_" + y).show();
	}

	
	/**
	 * Returns the folder name in which the drawings for K_{x,y} are saved.
	 * @param x first index of the complete graph
	 * @param y second index of the complete graph
	 * @return  folder name
	 */
	private String getFolderName(int x, int y) {
		return className + "_" + x + "_" + y;
	}
}
