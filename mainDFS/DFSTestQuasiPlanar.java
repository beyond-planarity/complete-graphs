package mainDFS;

import nodeInserter.NodeInserter;
import nodeInserter.NodeInserterQuasiPlanar;

public class DFSTestQuasiPlanar extends DFSTestCase {
	
	protected DFSTestQuasiPlanar() {
		
	CLASS_NAME = "dfsquasiplanar";
	USE_GAPS   = false;
	
	START = 0;
	END   = 0;

	FINAL_INDEX1 = 100;

	NodeInserter inserter = new NodeInserterQuasiPlanar();
	setNodeInserter(inserter);
	}
	
	public static void main(String[] args) {
		
		DFSTestCase test = new DFSTestQuasiPlanar();

		long time = System.currentTimeMillis();
		
		//test.initComplete();
		//test.testComplete();
		//test.prepareDrawingComplete();
		//test.drawComplete(7);

		test.initBipartite();
		//test.testBipartite();
		//test.prepareDrawingBipartite(6,10);
		//test.drawBipartite(6,10);

		long dif = System.currentTimeMillis() - time;
		System.out.println("PROGRAM FINISHED - TIME: " + dif);
	}
}