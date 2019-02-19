package mainDFS;

import nodeInserter.NodeInserter;
import nodeInserter.NodeInserterKPlanar;

public class DFSTestKPlanar extends DFSTestCase {
	
	protected DFSTestKPlanar() {

	int PLANARITY = 2;
	CLASS_NAME    = "dfs" + PLANARITY + "planar";
	USE_GAPS   = false;
	
	START = 0;
	END   = 1000000000;

	FINAL_INDEX1 = 10;

	NodeInserter inserter = new NodeInserterKPlanar(PLANARITY);
	setNodeInserter(inserter);
	}

	public static void main(String[] args) {
		
		DFSTestCase test = new DFSTestKPlanar();

		long time = System.currentTimeMillis();
		
		//test.initComplete();
		//test.testComplete();
		//test.prepareDrawingComplete(8);
		//test.drawComplete(8);

		test.initBipartite();
		test.testBipartite();
		//test.prepareDrawingBipartite(5,8);
		//test.drawBipartite(5,8);

		long dif = System.currentTimeMillis() - time;
		System.out.println("PROGRAM FINISHED - TIME: " + dif);
	}
}