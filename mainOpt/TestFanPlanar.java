package mainOpt;

import data.Tuple;
import nodeInserter.NodeInserter;
import nodeInserter.NodeInserterFanPlanar;

/**
 * Tests for fan-planar graph class
 * @author Tommy
 *
 */
public class TestFanPlanar extends TestCase {
	
	protected TestFanPlanar() {
		
		CLASS_NAME     = "optfanplanar";
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

		NodeInserter inserter = new NodeInserterFanPlanar();
		setNodeInserter(inserter);
	}

	public static void main(String[] args) {
		
		TestCase test = new TestFanPlanar();

		long time = System.currentTimeMillis();
		
		//test.initComplete();
		//test.testComplete();
		//test.prepareDrawingComplete(5);
		//test.drawComplete(5);

		test.initBipartite();
		test.testBipartite();
		//test.prepareDrawingBipartite(4,5,false);
		//test.drawBipartite(4,5);

		long dif = System.currentTimeMillis() - time;
		System.out.println("PROGRAM FINISHED - TIME: " + dif);
	}

}