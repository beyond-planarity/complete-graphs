package mainOpt;

import data.Tuple;
import graphbuilderOpt.OptBipartiteEmbeddingCreator;
import graphbuilderOpt.OptCompleteEmbeddingCreator;
import graphbuilderOpt.OptEmbeddingCreator;
import nodeInserter.NodeInserter;

/**
 * Setup for tests
 * @author Tommy
 *
 */
public abstract class TestCase {
	
	protected String  CLASS_NAME;     // name of the graph class for saving
	protected boolean USE_GAPS;       // for the gap-planar graphs
	protected int     STOP_AFTER;     // to stop the calculation at a certain point
	protected boolean TEMP_AVAILABLE; // to continue the calculation

	// for complete graphs
	// • start by creating graph K_{START_INDEX}; it is expected that K_{START_INDEX - 1} has already been created 
	// • last graph which is created is K_{END_INDEX}
	protected int START_INDEX;
	protected int END_INDEX; 
	
	// for complete bipartite graphs
	// • it is expected that the first index in this array has already been created 
	// • the creation follows this array
	protected Tuple[] INDICES;

	protected NodeInserter    ni;
	protected OptEmbeddingCreator cgc;
	protected OptEmbeddingCreator bgc;
	
	
	protected void setNodeInserter(NodeInserter ni) {
		this.ni  = ni;
		this.cgc = new OptCompleteEmbeddingCreator(ni, CLASS_NAME, START_INDEX, END_INDEX);
		this.bgc = new OptBipartiteEmbeddingCreator(ni, CLASS_NAME, INDICES);
	}
	
	protected void initComplete() {
		Test tc = new Test(cgc);
		tc.init(USE_GAPS);
	}
	
	protected void testComplete() {
		Test tc = new Test(cgc);
		tc.run(STOP_AFTER, TEMP_AVAILABLE);
	}
	
	protected void prepareDrawingComplete(int index) {
		Test tc = new Test(cgc);
		tc.prepareDrawing(index);
	}
	
	protected void drawComplete(int index) {
		Test tc = new Test(cgc);
		tc.draw(index);
	}
	
	

	protected void initBipartite() {
		Test tc = new Test(bgc);
		tc.init(USE_GAPS);
	}
	
	protected void testBipartite() {
		Test tc = new Test(bgc);
		tc.run(STOP_AFTER, TEMP_AVAILABLE);
	}
	
	protected void prepareDrawingBipartite(int index1, int index2, boolean v1IsNew) {
		Test tc = new Test(bgc);
		tc.prepareDrawing(index1, index2, v1IsNew);
	}
	
	protected void drawBipartite(int index1, int index2) {
		Test tc = new Test(bgc);
		tc.draw(index1, index2);
	}

}