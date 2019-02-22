package io.parseTreeTranslator;

import data.TempData;
import io.parseTree.Leaf;
import io.parseTree.TreeElement;

/**
 * Class to help saving.
 * @author tommy
 *
 */
public class ParseTreeXMLTempDataTranslator {
	
	private static final String L_TEMP               = "tempdata";
	private static final String L_CURRENT_EMB_ID     = "currentEmbId";
	private static final String L_LAST_EMB_ID        = "lastEmbId";
	private static final String L_TOTAL_NUMBER       = "totalNumber";
	private static final String L_TOTAL_NUMBER_FOUND = "totalNumberFound";
	private static final String L_TO_FIRST_EMB_ID    = "toFirstEmbId";
	private static final String L_NEW_NODE_NAME      = "newNodeName";
	private static final String L_CONNECT_TO_SET1    = "connoctToSet1";
	private static final String L_COUNTER            = "counter";

	private static final String L_TRUE               = "1";
	private static final String L_FALSE              = "0";

	
	public static TreeElement createTree(TempData tempData) {
		Leaf root = new Leaf(L_TEMP);
		root.setAttribute(L_CURRENT_EMB_ID, tempData.currentEmbId + "");
		root.setAttribute(L_LAST_EMB_ID, tempData.lastEmbId + "");
		root.setAttribute(L_TOTAL_NUMBER, tempData.totalNumber + "");
		root.setAttribute(L_TOTAL_NUMBER_FOUND, tempData.totalNumberFound + "");
		root.setAttribute(L_TO_FIRST_EMB_ID, tempData.toFirstEmbId + "");
		root.setAttribute(L_NEW_NODE_NAME, tempData.newNodeName + "");
		root.setAttribute(L_CONNECT_TO_SET1, ( tempData.connectToSet1 ? L_TRUE : L_FALSE ) + "");
		root.setAttribute(L_COUNTER, tempData.counter + "");
		return root;
	}
	

	public static TempData treeToTempData(TreeElement treeRoot) throws ParseTreeStructureException {

		TempData temp = new TempData();
		
		if(!treeRoot.getName().equals(L_TEMP) || !treeRoot.isLeaf()) {
			throw new ParseTreeStructureException("TempData.Root");
		}
		
		// get data
		String strCurrentEmbId     = treeRoot.getAttribute(L_CURRENT_EMB_ID);
		String strLastEmbId        = treeRoot.getAttribute(L_LAST_EMB_ID);
		String strTotalNumber      = treeRoot.getAttribute(L_TOTAL_NUMBER);
		String strTotalNumberFound = treeRoot.getAttribute(L_TOTAL_NUMBER_FOUND);
		String strNewFirstEmbId    = treeRoot.getAttribute(L_TO_FIRST_EMB_ID);
		temp.newNodeName           = treeRoot.getAttribute(L_NEW_NODE_NAME);
		String strConnectToSet1    = treeRoot.getAttribute(L_CONNECT_TO_SET1);
		String strCounter          = treeRoot.getAttribute(L_COUNTER);
		
		try {
			temp.currentEmbId     = Integer.parseInt(strCurrentEmbId);
			temp.lastEmbId        = Integer.parseInt(strLastEmbId);
			temp.totalNumber      = Integer.parseInt(strTotalNumber);
			temp.totalNumberFound = Integer.parseInt(strTotalNumberFound);
			temp.toFirstEmbId     = Integer.parseInt(strNewFirstEmbId);
			temp.connectToSet1    = strConnectToSet1.equals(L_TRUE);
			temp.counter          = Integer.parseInt(strCounter);
		} catch(NumberFormatException | NullPointerException e) {
			throw new ParseTreeStructureException("TempData.Parsing");
		}
		
		return temp;
	}

}
