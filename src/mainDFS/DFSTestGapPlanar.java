package mainDFS;

import nodeInserter.NodeInserter;
import nodeInserter.NodeInserterGapPlanar;

public class DFSTestGapPlanar extends DFSTestCase{
	
	protected DFSTestGapPlanar() {
		
	CLASS_NAME = "dfsgapplanar";
	USE_GAPS   = true;
	
	START = 149;
	END   = 149;

	FINAL_INDEX1 = 4;

	NodeInserter inserter = new NodeInserterGapPlanar();
	setNodeInserter(inserter);
	}
		

	public static void main(String[] args) {
		
		DFSTestCase test = new DFSTestGapPlanar();

		long time = System.currentTimeMillis();
		
		//test.initComplete();
		//test.testComplete();
		//test.prepareDrawingComplete();
		//test.drawComplete(7);

		//test.initBipartite();
		test.testBipartite();
		//test.prepareDrawingBipartite(4,9);
		//test.drawBipartite(4,5);

		long dif = System.currentTimeMillis() - time;
		System.out.println("PROGRAM FINISHED - TIME: " + dif);
	}
}