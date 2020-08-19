package mainDFS;

import graphbuilderDFS.DFSBipartiteEmbeddingCreatorDiag;
import graphbuilderDFS.DFSCompleteEmbeddingCreator;
import graphbuilderDFS.DFSEmbeddingCreator;
import nodeInserter.NodeInserter;

/**
 * Setup for tests in DFS
 * @author Tommy
 *
 */
public abstract class DFSTestCase {
	
	protected String  CLASS_NAME;	
	protected boolean USE_GAPS;		// for the gap-planar graphs

	// calculate all drawings for base drawings with id start to end
	protected int START;
	protected int END;

	// for the bipartite complete graphs, vertices are inserted alternating between the two
	// bipartite sets, until there are FINAL_INDEX1 vertices of the first bipartite
	// set in the drawing; then vertices are only inserted into the second bipartite set 
	protected int FINAL_INDEX1;

	protected NodeInserter        ni;
	protected DFSEmbeddingCreator cgc;
	protected DFSEmbeddingCreator bgc;

	protected void setNodeInserter(NodeInserter ni) {
		this.ni  = ni;
		this.cgc = new DFSCompleteEmbeddingCreator(ni, CLASS_NAME);
		this.bgc = new DFSBipartiteEmbeddingCreatorDiag(ni, CLASS_NAME, FINAL_INDEX1);
	}

	
	protected void initComplete() {
		DFSTest tc = new DFSTest(cgc);
		tc.init(USE_GAPS);
	}
	protected void testComplete() {
		DFSTest tc = new DFSTest(cgc);
		tc.run(START, END);
	}
	protected void prepareDrawingComplete(int graphIndex) {
		DFSTest tc = new DFSTest(cgc);
		tc.prepareDrawing(graphIndex);
	}
	protected void drawComplete(int index) {
		DFSTest tc = new DFSTest(cgc);
		tc.draw(index);
	}
	
	protected void initBipartite() {
		DFSTest tc = new DFSTest(bgc);
		tc.init(USE_GAPS);
	}	
	protected void testBipartite() {
		DFSTest tc = new DFSTest(bgc);
		tc.run(START, END);
	}	
	protected void prepareDrawingBipartite(int vertex1, int vertex2) {
		DFSTest tc = new DFSTest(bgc);
		tc.prepareDrawing(vertex1, vertex2);
	}	
	protected void drawBipartite(int vertex1, int vertex2) {
		DFSTest tc = new DFSTest(bgc);
		tc.draw(vertex1, vertex2);
	}
}