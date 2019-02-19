package io.parseTreeTranslator;

@SuppressWarnings("serial")
public class ParseTreeStructureException extends Exception {

	public ParseTreeStructureException() {
		super("Couldn't translate the tree.");
	}
	
	public ParseTreeStructureException(String treeType) {
		super("Couldn't translate the tree: " + treeType);
	}

}
