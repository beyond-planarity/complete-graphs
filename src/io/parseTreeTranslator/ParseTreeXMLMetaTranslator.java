package io.parseTreeTranslator;

import data.Metadata;
import io.parseTree.Leaf;
import io.parseTree.TreeElement;

/**
 * Translate metadata to a tree that can be saved as pseudo-xml.
 * @author Tommy
 *
 */
public class ParseTreeXMLMetaTranslator {

	private static final String L_META            = "metadata";
	private static final String L_TOTAL_NO        = "totalNumberEmbeddings";
	private static final String L_TOPO_NO         = "topologicalDifferentEmbeddings";
	private static final String L_TOTAL_NO_FOUND  = "totalNumberEmbeddingsFound";
	private static final String L_FIRST_EMBEDDING = "firstEmbedding";

	/**
	 * Creates a tree from the metadata
	 * @param metadata
	 * @return
	 */
	public static TreeElement createTree(Metadata metadata) {
		Leaf root = new Leaf(L_META);
		root.setAttribute(L_TOTAL_NO,        metadata.getNumberEmbeddingsTotal() + "");
		root.setAttribute(L_TOPO_NO,         metadata.getNumberEmbeddingsTopoDifferent() + "");
		root.setAttribute(L_TOTAL_NO_FOUND,  metadata.getNumberEmbeddingsTotalFound() + "");
		root.setAttribute(L_FIRST_EMBEDDING, metadata.getIdStartEmbedding() + "");
		return root;
	}
	

	/**
	 * Translates a tree into a metadata object.
	 * @param treeRoot root of the tree
	 * @return metadata
	 * @throws ParseTreeStructureException
	 */
	public static Metadata treeToMeta(TreeElement treeRoot) throws ParseTreeStructureException {

		Metadata metadata = new Metadata();
		
		if(!treeRoot.getName().equals(L_META) || !treeRoot.isLeaf()) {
			throw new ParseTreeStructureException("Meta.Root");
		}
		
		// get data
		String strTotalNo     = treeRoot.getAttribute(L_TOTAL_NO);
		String strTopoNo      = treeRoot.getAttribute(L_TOPO_NO);
		String strTopoNoFound = treeRoot.getAttribute(L_TOTAL_NO_FOUND);
		String strFirstEmb    = treeRoot.getAttribute(L_FIRST_EMBEDDING);
		
		try {
			metadata.setNumberEmbeddingsTotal(Integer.parseInt(strTotalNo));
			metadata.setNumberEmbeddingsTopoDifferent(Integer.parseInt(strTopoNo));
			metadata.setIdStartEmbedding(Integer.parseInt(strFirstEmb));
			if (strTopoNoFound != null) {
				metadata.setNumberEmbeddingsTotalFound(Integer.parseInt(strTopoNoFound));
			}
		} catch(NumberFormatException | NullPointerException e) {
			throw new ParseTreeStructureException("Meta.Parsing");
		}
		
		return metadata;
	}

}
