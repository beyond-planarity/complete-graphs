package mainDFS;

import nodeInserter.NodeInserter;
import nodeInserter.NodeInserterFanPlanar;

public class DFSTestFanPlanar extends DFSTestCase {
	
	protected DFSTestFanPlanar() {
		
	CLASS_NAME = "dfsfanplanar";
	USE_GAPS   = false;
	
	START = 0;
	END   = 10000000;

	FINAL_INDEX1 = 10;

	NodeInserter inserter = new NodeInserterFanPlanar();
	setNodeInserter(inserter);
	}
	
	public static void main(String[] args) {
		
		DFSTestCase test = new DFSTestFanPlanar();

		long time = System.currentTimeMillis();
		
		test.initComplete();
		test.testComplete();
		//test.prepareDrawingComplete();
		//test.drawComplete(7);

		//test.initBipartite();
		//test.testBipartite();
		//test.prepareDrawingBipartite();
		//test.drawBipartite(4,5);

		long dif = System.currentTimeMillis() - time;
		System.out.println("PROGRAM FINISHED - TIME: " + dif);
	}
}