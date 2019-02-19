package io.parseTreeTranslator;

import data.Statistics;
import io.parseTree.Leaf;
import io.parseTree.TreeElement;

public class ParseTreeXMLStatisticsTranslator {
	public int     totalCreated;
	public int     nonIsomorphic;
	public int     time;

	private static final String L_ROOT    = "statistics";
	private static final String L_TOTAL   = "createdDrawingsTotal";
	private static final String L_NON_ISO = "nonIsomorphicDrawings";
	private static final String L_TIME    = "executionTime";

	
	public static TreeElement createTree(Statistics stats) {
		Leaf root = new Leaf(L_ROOT);
		root.setAttribute(L_TOTAL,   stats.totalCreated + "");
		root.setAttribute(L_NON_ISO, stats.nonIsomorphic+ "");
		root.setAttribute(L_TIME,    stats.time + "");
		return root;
	}
	

	public static Statistics treeToStatistics(TreeElement treeRoot) throws ParseTreeStructureException {

		Statistics stats = new Statistics();
		
		if(!treeRoot.getName().equals(L_ROOT) || !treeRoot.isLeaf()) {
			throw new ParseTreeStructureException("Statistics.Root");
		}
		
		// get data
		String strTotal  = treeRoot.getAttribute(L_TOTAL);
		String strNonIso = treeRoot.getAttribute(L_NON_ISO);
		String strTime   = treeRoot.getAttribute(L_TIME);
		
		try {
			stats.totalCreated  = Integer.parseInt(strTotal);
			stats.nonIsomorphic = Integer.parseInt(strNonIso);
			stats.time          = Long.parseLong(strTime);
		} catch(NumberFormatException | NullPointerException e) {
			throw new ParseTreeStructureException("Statistics.Parsing");
		}
		
		return stats;
	}

}
