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
		
	    int PLANARITY  = 4;
		
		CLASS_NAME     = "opt" + PLANARITY + "planar";
		USE_GAPS       = false;
		
		STOP_AFTER     = Integer.MAX_VALUE;
		TEMP_AVAILABLE = false;
		
		START_INDEX    = 4;
		END_INDEX      = 12;
		
		Tuple[] biIndices = {
				new Tuple(4,4),
				new Tuple(4,5)
		};
		INDICES = biIndices;

		NodeInserter inserter = new NodeInserterKPlanar(PLANARITY);
		setNodeInserter(inserter);
	}
	
	
	public static void main(String[] args) {
		
		TestCase test = new TestKPlanar();

		long time = System.currentTimeMillis();
		
		//test.initComplete();
		//test.testComplete();
		//test.prepareDrawingComplete(5);
		//test.drawComplete(5);

		//test.initBipartite();
		test.testBipartite();
		//test.prepareDrawingBipartite(3,3,false);
		//test.drawBipartite(3,3);

		long dif = System.currentTimeMillis() - time;
		System.out.println("PROGRAM FINISHED - TIME: " + dif);
	}
}