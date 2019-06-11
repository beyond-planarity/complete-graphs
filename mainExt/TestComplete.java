package mainExt;

import data.Embedding;
import drawing.DrawerExt;
import drawing.DrawingPositioner;
import embeddingComparator.EmbeddingComparator;
import graphbuilderExt.CompleteGraphCreator;
import io.safeLoad.SafeLoad;
import nodeInserter.NodeInserter;

/**
 * Class to create drawings of complete graphs.
 * @author Tommy
 *
 */
public class TestComplete {
	
	private String       className;
	private NodeInserter nodeInserter;
	private int          startIndex;
	private int          endIndex;
	
	/**
	 * Creates an environment for creating all drawings for certain complete graphs.
	 * @param className    name of the graph class
	 * @param nodeInserter rules for inserting the new node
	 * @param startIndex   an index for a complete graph, were all the drawings are already calculated
	 * @param endIndex     index for the complete graph, for that all drawings should be calculated; must be higher than <code>startIndex</code>
	 */
	public TestComplete(String className, NodeInserter nodeInserter, int startIndex, int endIndex) {
		this.className    = className;
		this.nodeInserter = nodeInserter;
		this.startIndex   = startIndex;
		this.endIndex     = endIndex;
	}

	/**
	 * Creates the drawing for graph K_3.
	 */
	public void createK3() {
		CompleteGraphCreator.createPlanarStartEmbeddings(getFolderName(3));
		EmbeddingComparator.compareEmbeddingsHDExtended(getFolderName(3), -1);
		new DrawingPositioner(getFolderName(3)).calcPosAndColorLargeSize("");
	}
	
	
	/**
	 * Creates all the drawings for the specified graphs.
	 */
	public void createKx() {
		
		if (startIndex < 4 || startIndex > endIndex) {
			System.out.println("No graph indices found.");
			return;
		}
		
		String firstFolder = getFolderName(startIndex-1);
		if (SafeLoad.isEmpty(firstFolder)) {
			System.out.println("Folder not found");
			return;
		}
		
		for (int i=startIndex; i<=endIndex; i++) {
			String fromFolder = getFolderName(i-1);
			String toFolder   = getFolderName(i);
			
			CompleteGraphCreator graphCreator = new CompleteGraphCreator(nodeInserter, fromFolder, toFolder);
			if (!graphCreator.createKx(i)) {
				break;
			};
			EmbeddingComparator.compareEmbeddingsHDExtended(toFolder, -1);
		}
	}
	
	/**
	 * Prepare the specified graphs for drawing.
	 */
	public void prepareDrawingKx() {	
		for (int i=startIndex; i<=endIndex; i++) {
			String  newNodeName = Embedding.SET1_LETTER + i;
			new DrawingPositioner(getFolderName(i)).calcPosAndColorLargeSize(newNodeName);
		}
	}
	
	/**
	 * Show all the drawings of the specified complete graph.
	 * @param x
	 */
	public void drawKx(int x) {
		new DrawerExt(getFolderName(x), "K_" + x).show();
	}
	
	/**
	 * Returns the folder name in which the drawings for K_x are saved.
	 * @param x index of the complete graph
	 * @return  folder name
	 */
	private String getFolderName(int x) {
		return className + "_" + x;
	}
	
}
