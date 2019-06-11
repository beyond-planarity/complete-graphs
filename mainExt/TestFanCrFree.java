package mainExt;

import data.Tuple;
import nodeInserter.NodeInserterFanCrossingFree;

/**
 * Class for testing the fan-crossing free graphs.
 * @author Tommy
 *
 */
public class TestFanCrFree {
	
	private final String CLASS_NAME = "fancrossingfree";
	
	private final int start = 4;
	private final int end   = 10;

	private Tuple[] indices = {
			new Tuple(2,2),
			new Tuple(2,3),
			new Tuple(2,4),
			new Tuple(2,5),
			new Tuple(3,5),
			new Tuple(4,5),
			new Tuple(5,5)
	};
	
	
	public static void main(String[] args) {
		TestFanCrFree test = new TestFanCrFree();
		
		test.testComplete();
		//test.prepareDrawingComplete();
		//test.drawComplete(5);
		
		//test.testBipartite();
		//test.prepareDrawingBipartite();
		//test.drawBipartite(2,5);
	}
	
	public void testComplete() {
		TestComplete tc = new TestComplete(
				CLASS_NAME,
				new NodeInserterFanCrossingFree(), start, end);
		tc.createK3();
		tc.createKx();
	}
	
	public void prepareDrawingComplete() {
		TestComplete tc = new TestComplete(
				CLASS_NAME,
				new NodeInserterFanCrossingFree(), start, end);
		tc.prepareDrawingKx();
	}
	
	public void drawComplete(int x) {
		TestComplete tc = new TestComplete(
				CLASS_NAME,
				new NodeInserterFanCrossingFree(), start, end);
		tc.drawKx(x);
	}
	
		
	public void testBipartite() {
		TestBipartite tc = new TestBipartite(
				CLASS_NAME,
				new NodeInserterFanCrossingFree(),
				indices);
		tc.createK22();
		tc.createKxy();
	}
	
		
	public void prepareDrawingBipartite() {
		TestBipartite tc = new TestBipartite(
				CLASS_NAME,
				new NodeInserterFanCrossingFree(),
				indices);
		tc.prepareDrawingKxy();
	}
	
		
	public void drawBipartite(int x, int y) {
		TestBipartite tc = new TestBipartite(
				CLASS_NAME,
				new NodeInserterFanCrossingFree(),
				indices);
		tc.drawKxy(x,y);
	}

}