package mainExt;

import data.Tuple;
import nodeInserter.NodeInserterKPlanar;

/**
 * Class for testing the k-planar graphs.
 * @author Tommy
 *
 */
public class TestKPlanar {
	
	private final int    PLANARITY = 1;
	private final String CLASS_NAME = PLANARITY + "planar";
	
	
	// K_{9} is not 5-planar
	private final int start = 4;
	private final int end   = 8;

	
	// K_{6,?} is not 5-planar
	private Tuple[] indices = {
			new Tuple(2,2),
			new Tuple(2,3),
			new Tuple(3,3),
			new Tuple(3,4),
			new Tuple(4,4),
			new Tuple(4,5),
			new Tuple(5,5),
			new Tuple(5,6),
			new Tuple(6,6)
	};


	
	public static void main(String[] args) {
		TestKPlanar test = new TestKPlanar();
		
		//test.testComplete();
		//test.prepareDrawingComplete();
		//test.drawComplete(5);
		
		//test.testBipartite();
		//test.prepareDrawingBipartite();
		test.drawBipartite(4,4);
	}
	

	
	public void testComplete() {
		TestComplete tc = new TestComplete(
				CLASS_NAME,
				new NodeInserterKPlanar(PLANARITY), start, end);
		tc.createK3();
		tc.createKx();
	}
	
	public void prepareDrawingComplete() {
		TestComplete tc = new TestComplete(
				CLASS_NAME,
				new NodeInserterKPlanar(PLANARITY), start, end);
		tc.prepareDrawingKx();
	}
	
	public void drawComplete(int x) {
		TestComplete tc = new TestComplete(
				CLASS_NAME,
				new NodeInserterKPlanar(PLANARITY), start, end);
		tc.drawKx(x);
	}
	
		
	public void testBipartite() {
		TestBipartite tc = new TestBipartite(
				CLASS_NAME,
				new NodeInserterKPlanar(PLANARITY),
				indices);
		tc.createK22();
		tc.createKxy();
	}
	
		
	public void prepareDrawingBipartite() {
		TestBipartite tc = new TestBipartite(
				CLASS_NAME,
				new NodeInserterKPlanar(PLANARITY),
				indices);
		tc.prepareDrawingKxy();
	}
	
		
	public void drawBipartite(int x, int y) {
		TestBipartite tc = new TestBipartite(
				CLASS_NAME,
				new NodeInserterKPlanar(PLANARITY),
				indices);
		tc.drawKxy(x,y);
	}

}