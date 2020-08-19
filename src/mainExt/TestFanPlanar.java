package mainExt;

import data.Tuple;
import nodeInserter.NodeInserterFanPlanar;

/**
 * Class for testing the fan-planar graphs.
 * @author Tommy
 *
 */
public class TestFanPlanar {
	
	private final String CLASS_NAME = "fanplanar";
	
	
	// K_{7} is fan-planar, K_{8} is not fan-planar
	private final int start = 4;
	private final int end   = 10;

	
	
	// K_{5,5} is not fan-planar
	private Tuple[] indices = {
			new Tuple(2,2),
			new Tuple(2,3),
			new Tuple(3,3),
			new Tuple(3,4),
			new Tuple(4,4),
			new Tuple(4,5),
			new Tuple(5,5)
	};
	
	
	public static void main(String[] args) {
		TestFanPlanar test = new TestFanPlanar();
		
		//test.testComplete();
		//test.prepareDrawingComplete();
		//test.drawComplete(5);
		
		//test.testBipartite();
		//test.prepareDrawingBipartite();
		test.drawBipartite(4,5);
	}
	
	public void testComplete() {
		TestComplete tc = new TestComplete(
				CLASS_NAME,
				new NodeInserterFanPlanar(), start, end);
		tc.createK3();
		tc.createKx();
	}
	
	public void prepareDrawingComplete() {
		TestComplete tc = new TestComplete(
				CLASS_NAME,
				new NodeInserterFanPlanar(), start, end);
		tc.prepareDrawingKx();
	}
	
	public void drawComplete(int x) {
		TestComplete tc = new TestComplete(
				CLASS_NAME,
				new NodeInserterFanPlanar(), start, end);
		tc.drawKx(x);
	}
	
		
	public void testBipartite() {
		TestBipartite tc = new TestBipartite(
				CLASS_NAME,
				new NodeInserterFanPlanar(),
				indices);
		tc.createK22();
		tc.createKxy();
	}
	
		
	public void prepareDrawingBipartite() {
		TestBipartite tc = new TestBipartite(
				CLASS_NAME,
				new NodeInserterFanPlanar(),
				indices);
		tc.prepareDrawingKxy();
	}
	
		
	public void drawBipartite(int x, int y) {
		TestBipartite tc = new TestBipartite(
				CLASS_NAME,
				new NodeInserterFanPlanar(),
				indices);
		tc.drawKxy(x,y);
	}

}