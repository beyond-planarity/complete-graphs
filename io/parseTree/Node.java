package io.parseTree;

import java.util.ArrayList;
import java.util.List;

public class Node extends TreeElement {
	
	private List<TreeElement> children;
	

	public Node(String name) {
		super(name, false);
		children = new ArrayList<TreeElement>();
	}
	
	
	
	// -----------------------------------------------------------------------
	// GETTER
	// -----------------------------------------------------------------------
	public List<TreeElement> getChildren() {
		return children;
	}
	public List<TreeElement> getChildren(String name) {
		List<TreeElement> elements = new ArrayList<TreeElement>();
		for(TreeElement te : children) {
			if(te.getName().equals(name)) {
				elements.add(te);
			}
		}
		return elements;
	}
	public List<String> getChildrenNames() {
		List<String> names = new ArrayList<String>();
		for(TreeElement te : children) {
			String name = te.getName();
			if(!names.contains(name)) {
				names.add(name);
			}
		}
		return names;
	}
	
	
	
	// -----------------------------------------------------------------------
	// ADD - REMOVE
	// -----------------------------------------------------------------------
	public void addChild(TreeElement treeElement) {
		children.add(treeElement);
	}
	
	
	
	// -----------------------------------------------------------------------
	// BOOLEAN RETURN
	// -----------------------------------------------------------------------
	public boolean hasChildren() {
		return !children.isEmpty();
	}
	public boolean hasChild(String name) {
		for(TreeElement te : children) {
			if(te.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	


	


}
