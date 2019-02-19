package mainOpt;

import data.Tuple;
import nodeInserter.NodeInserter;
import nodeInserter.NodeInserterGapPlanar;

/**
 * Test the class of gap planar graphs.
 * NOT FINISHED!!! CODE NOT WORKING YET FOR GAPS!!!
 * @author tommy
 *
 */
public class TestGapPlanar extends TestCase {
	
	protected TestGapPlanar() {
		
		CLASS_NAME     = "optgapplanar";
		USE_GAPS       = true;
		
		STOP_AFTER     = Integer.MAX_VALUE;
		TEMP_AVAILABLE = false;
		
		START_INDEX    = 7;
		END_INDEX      = 7; 
		
		Tuple[] biIndices = {
				new Tuple(3,3),
				new Tuple(3,4)
		};
		INDICES = biIndices;

		NodeInserter inserter = new NodeInserterGapPlanar();
		setNodeInserter(inserter);
	}
	
	public static void main(String[] args) {
		
		TestCase test = new TestGapPlanar();

		long time = System.currentTimeMillis();
		
		//test.initComplete();
		//test.testComplete();
		//test.prepareDrawingComplete(5);
		//test.drawComplete(5);

		//test.initBipartite();
		test.testBipartite();
		//test.prepareDrawingBipartite(4,5,false);
		//test.drawBipartite(4,5);

		long dif = System.currentTimeMillis() - time;
		System.out.println("PROGRAM FINISHED - TIME: " + dif);
	}
}