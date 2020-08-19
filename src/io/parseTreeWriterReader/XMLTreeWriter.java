package io.parseTreeWriterReader;

import io.parseTree.Leaf;
import io.parseTree.Node;
import io.parseTree.TreeElement;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

/**
 * Class to help saving.
 * @author tommy
 *
 */
public class XMLTreeWriter {
	
	private static final String lineSeparator = "\n";
	private static final String INTERN_TAG_SEPARATOR = "|";
	private static final String ATTRIBUTE_SEPARATOR = "?";
	private static final String TAGSTART = "<";
	private static final String TAGEND = ">";
	
	private String 		writePath;
	private TreeElement treeRoot;
	private FileWriter	fileWriter;

	
	/**
	 * Creates a new XMLTreeWritter.
	 * @param writePath path to write the new file to (if there is an old file it will be overwritten)
	 * @param treeRoot root of the tree which shall be written
	 */
	public XMLTreeWriter(String writePath, TreeElement treeRoot) {
		this.writePath = writePath;
		this.treeRoot  = treeRoot;
	}
	
	
//	/**
//	 * Opens the file writer.
//	 * @throws IOException
//	 */
//	public void openWriter() throws IOException {
//	    fileWriter = new FileWriter(writePath);
//	}
//
//	/**
//	 * Closes the file writer.
//	 * @throws IOException
//	 */
//	public void closeWriter() throws IOException {
//		fileWriter.flush();
//		fileWriter.close();
//	}
	
	/**
	 * Writes the tree to the file.
	 * @throws IOException
	 */
	public void write() throws IOException {
	    fileWriter = new FileWriter(writePath);
	    
		if(treeRoot == null) {
			return;
		}
		writeTreeElement(treeRoot);
		
		fileWriter.flush();
		fileWriter.close();
	}
	
	private void writeTreeElement(TreeElement element) throws IOException {
		String kind = "node";
		// get kind of TreeElement
		if(element.isLeaf()) {
			kind = "leaf";
		}
		
		// get attributes
		String attributes = "";
		for(Entry<String, String> attribute : element.getAttributeEntrySet()) {
			if(!attribute.getValue().equals("")) {
				attributes += INTERN_TAG_SEPARATOR + attribute.getKey() + ATTRIBUTE_SEPARATOR + attribute.getValue();
			}
		}
		
		// write starting tag
		String startTag = TAGSTART + element.getName() + INTERN_TAG_SEPARATOR + kind + attributes + TAGEND + lineSeparator;
		fileWriter.append(startTag);
		
		// write children / text
		if(element.isLeaf()) {
			Leaf leaf = (Leaf) element;
			if(leaf.hasText()) {
				fileWriter.append(leaf.getText() + lineSeparator);
			}
		}
		else {
			Node node = (Node) element;
			for(TreeElement te : node.getChildren()) {
				writeTreeElement(te);
			}
		}
		
		// write end tag
		String endTag = TAGSTART + "/" + element.getName() + TAGEND + lineSeparator;
		fileWriter.append(endTag);
	}
	
	

	
	
}
