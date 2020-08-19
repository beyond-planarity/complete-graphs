package mainOpt;

import data.Tuple;
import nodeInserter.NodeInserter;
import nodeInserter.NodeInserterQuasiPlanar;

/**
 * Tests for quasiplanar graph class
 * @author Tommy
 *
 */
public class TestQuasiPlanar extends TestCase {
	
	protected TestQuasiPlanar() {
		
		CLASS_NAME     = "optquasiplanar";
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

		NodeInserter inserter = new NodeInserterQuasiPlanar();
		setNodeInserter(inserter);
	}
	
	
	public static void main(String[] args) {
		
		TestCase test = new TestQuasiPlanar();

		long time = System.currentTimeMillis();
		
		test.initComplete();
		test.testComplete();
		//test.prepareDrawingComplete(5);
		//test.drawComplete(5);

		//test.initBipartite();
		//test.testBipartite();
		//test.prepareDrawingBipartite(4,5,false);
		//test.drawBipartite(4,5);

		long dif = System.currentTimeMillis() - time;
		System.out.println("PROGRAM FINISHED - TIME: " + dif);
	}
}