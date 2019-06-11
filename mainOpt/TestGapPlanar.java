package mainOpt;

import data.Tuple;
import nodeInserter.NodeInserter;
import nodeInserter.NodeInserterGapPlanar;

/**
 * Test the class of gap planar graphs.
 * @author tommy
 *
 */
public class TestGapPlanar extends TestCase {
	
	protected TestGapPlanar() {
		
		CLASS_NAME     = "optgapplanar";
		USE_GAPS       = true;
		
		STOP_AFTER     = 1000000;
		TEMP_AVAILABLE = false;
		
		START_INDEX    = 9;
		END_INDEX      = 9; 
		
		Tuple[] biIndices = {
				new Tuple(4,8),
				new Tuple(4,9)
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
		//test.prepareDrawingComplete(8);
		//test.drawComplete(8);

		//test.initBipartite();
		test.testBipartite();
		//test.prepareDrawingBipartite(2,4,false);
		//test.drawBipartite(2,4);

		long dif = System.currentTimeMillis() - time;
		System.out.println("PROGRAM FINISHED - TIME: " + dif);
	}
}