package io.parseTreeWriterReader;

import io.parseTree.Leaf;
import io.parseTree.Node;
import io.parseTree.TreeElement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class XMLTreeReader {
	
	//private static final String lineSeparator = "\n";
	private static String INTERN_TAG_SEPARATOR = "|";
	private static String ATTRIBUTE_SEPARATOR = "?";
	private static String TAGSTART = "<";
	private static String TAGEND = ">";
	
	private String 		readPath;
	private FileReader  fileReader;
	private BufferedReader bufferedReader;
	

	public XMLTreeReader(String readPath) {
		this.readPath = readPath;
	}
	
	
//	public void openReader() throws FileNotFoundException {
//		fileReader = new FileReader(readPath);
//		bufferedReader = new BufferedReader(fileReader);
//	}
//	
//	public void closeReader() throws IOException {
//		bufferedReader.close();
//		fileReader.close();
//	}
	
	public TreeElement read() throws IOException {
		fileReader = new FileReader(readPath);
		bufferedReader = new BufferedReader(fileReader);
		
		String startTag = bufferedReader.readLine().trim();
		if(!startTag.startsWith(TAGSTART)) {
			throw new IOException();
		}
		TreeElement treeRoot = readTreeElement(startTag);
		bufferedReader.close();
		fileReader.close();
		return treeRoot;
	}
	
	
	private TreeElement readTreeElement(String startTag) throws IOException {
		String[] analysis = analyseStartTag(startTag);
		String name = analysis[0];
		String tag  = TAGSTART + "/" + name + TAGEND;
		
		if(analysis[1].equals("leaf")) {
			Leaf treeElement = new Leaf(name);
			
			// set attributes
			for(int i=2; i<analysis.length; i++) {
				Pair kv = new Pair(analysis[i]);
				treeElement.setAttribute(kv.getKey(), kv.getValue());
			}
			
			String text = "";
			
			// read till end tag
			String line = bufferedReader.readLine().trim();
			while(!line.equals(tag)) {
				text += line;
				line = bufferedReader.readLine().trim();
				if(line == null) {
					return null;
				}
			}
			
			treeElement.setText(text);

			return treeElement;
		}
		else {
			Node treeElement = new Node(name);
			
			// set attributes
			for(int i=2; i<analysis.length; i++) {
				Pair kv = new Pair(analysis[i]);
				treeElement.setAttribute(kv.getKey(), kv.getValue());
			}
			
			// read till end tag
			String line = bufferedReader.readLine().trim();
			while(!line.equals(tag)) {
				if(!line.startsWith(TAGSTART)) {
					throw new IOException();
				}
				treeElement.addChild(readTreeElement(line));
				
				line = bufferedReader.readLine().trim();
			}
			
			return treeElement;
		}
	}
	
	
	private String[] analyseStartTag(String startTag) {
		String correctedStartTag = startTag.substring(1, startTag.length()-1);
		return correctedStartTag.split("\\" + INTERN_TAG_SEPARATOR);
	}
	
	private class Pair {
		private String key;
		private String value;
		
		public Pair(String keyValue) {
			String[] kv = keyValue.split("\\" + ATTRIBUTE_SEPARATOR);
			key = kv[0];
			value = kv[1];
		}
		
		public String getKey() {
			return key;
		}
		public String getValue() {
			return value;
		}
	}

}
