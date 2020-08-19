package mainOpt;

import data.Tuple;
import nodeInserter.NodeInserter;
import nodeInserter.NodeInserterKPlanar;

/**
 * Tests for k-planar graph class
 * @author Tommy
 *
 */
public class TestKPlanar extends TestCase {
	
	protected TestKPlanar() {
		
	    int PLANARITY  = 1;
		
		CLASS_NAME     = "opt" + PLANARITY + "planar";
		USE_GAPS       = false;
		
		STOP_AFTER     = Integer.MAX_VALUE;
		TEMP_AVAILABLE = false;
		
		START_INDEX    = 4;
		END_INDEX      = 10;
		
		Tuple[] biIndices = {
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
		
		Tuple[] biIndices2 = {
				new Tuple(2,2),
				new Tuple(2,3),
				new Tuple(2,4),
				new Tuple(2,5),
				new Tuple(2,6),
				new Tuple(3,6),
				new Tuple(4,6),
				new Tuple(5,6),
				new Tuple(6,6)
		};
		
		Tuple[] biIndices3 = {
				new Tuple(2,2),
				new Tuple(2,3),
				new Tuple(2,4),
				new Tuple(2,5),
				new Tuple(3,5),
				new Tuple(4,5),
				new Tuple(5,5)
		};
		INDICES = biIndices3;

		NodeInserter inserter = new NodeInserterKPlanar(PLANARITY);
		setNodeInserter(inserter);
	}
	
	
	public static void main(String[] args) {
		
		TestCase test = new TestKPlanar();

		long time = System.currentTimeMillis();
		
		test.initComplete();
		//test.testComplete();
		//test.prepareDrawingComplete(4);
		//test.drawComplete(4);

		//test.initBipartite();
		//test.testBipartite();
		//test.prepareDrawingBipartite(3,3,false);
		//test.drawBipartite(3,3);

		long dif = System.currentTimeMillis() - time;
		System.out.println("PROGRAM FINISHED - TIME: " + dif);
	}
}