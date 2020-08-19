package mainDFS;

import nodeInserter.NodeInserter;
import nodeInserter.NodeInserterKPlanar;

public class DFSTestKPlanar extends DFSTestCase {
	
	protected DFSTestKPlanar() {

	int PLANARITY = 5;
	CLASS_NAME    = "dfs" + PLANARITY + "planar";
	USE_GAPS   = false;
	
	START = 7;
	END   = 7;

	FINAL_INDEX1 = 5;

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

		//test.initBipartite();
		test.testBipartite();
		//test.prepareDrawingBipartite(6,7);
		//test.drawBipartite(5,8);

		long dif = System.currentTimeMillis() - time;
		System.out.println("PROGRAM FINISHED - TIME: " + dif);
	}
}