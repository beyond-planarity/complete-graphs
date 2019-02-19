package io.parseTree;

public class Leaf extends TreeElement {
	
	String text;

	public Leaf(String name) {
		super(name, true);
		text = "";
	}
	public Leaf(String name, String text) {
		super(name, true);
		this.text = text;
	}

	
	
	// ---------------------------------------------------------------
	// GETTER
	// ---------------------------------------------------------------
	public String getText() {
		return text;
	}
	
	
	// ---------------------------------------------------------------
	// SETTER
	// ---------------------------------------------------------------
	public void setText(String text) {
		this.text = text;
	}

	
	// ---------------------------------------------------------------
	// BOOLEAN RETURN
	// ---------------------------------------------------------------
	public boolean hasText() {
		return text != null & !text.equals("");
	}

}
