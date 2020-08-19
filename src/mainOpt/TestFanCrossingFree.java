package mainOpt;

import data.Tuple;
import nodeInserter.NodeInserter;
import nodeInserter.NodeInserterFanCrossingFree;

/**
 * Tests for fan-planar graph class
 * @author Tommy
 *
 */
public class TestFanCrossingFree extends TestCase {
	
	protected TestFanCrossingFree() {
		
		CLASS_NAME     = "optfancrfree";
		USE_GAPS       = false;
		
		STOP_AFTER     = Integer.MAX_VALUE;
		TEMP_AVAILABLE = false;
		
		START_INDEX    = 4;
		END_INDEX      = 8; 
		
		Tuple[] biIndices = {
				new Tuple(2,2),
				new Tuple(2,3),
				new Tuple(3,3),
				new Tuple(3,4),
				new Tuple(4,4),
				new Tuple(4,5),
				new Tuple(5,5)
		};
		INDICES = biIndices;

		NodeInserter inserter = new NodeInserterFanCrossingFree();
		setNodeInserter(inserter);
	}

	public static void main(String[] args) {
		
		TestCase test = new TestFanCrossingFree();

		long time = System.currentTimeMillis();
		
		test.initComplete();
		test.testComplete();
		//test.prepareDrawingComplete(6);
		//test.drawComplete(6);

		//test.initBipartite();
		//test.testBipartite();
		//test.prepareDrawingBipartite(4,5,false);
		//test.drawBipartite(4,5);

		long dif = System.currentTimeMillis() - time;
		System.out.println("PROGRAM FINISHED - TIME: " + dif);
	}

}