package io.parseTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class TreeElement {
	
	private String name;
	private boolean isLeaf;
	private Map<String, String> attributes;

	/**
	 * Create new tree element with name <code>name</code>
	 * @param name name of the TreeElement
	 * @param isLeaf true if the TreeElement is a leaf
	 */
	protected TreeElement(String name, boolean isLeaf) {
		this.name = name;
		this.isLeaf = isLeaf;
		attributes = new HashMap<String, String>();
	}
	
	
	
	// ------------------------------------------------------------
	// GETTER
	// ------------------------------------------------------------
	/**
	 * Returns the name of this TreeElement.
	 * @return name of the TreeElement
	 */
	public String getName() {
		return name;
	}
	
	public Set<Entry<String, String>> getAttributeEntrySet() {
		return attributes.entrySet();
	}
	
	/**
	 * Returns the value for the given key, or null if the key doesn't exist.
	 * @param key 
	 * @return value value to the key
	 */
	public String getAttribute(String key) {
		return attributes.get(key);
	}
	
	/**
	 * Returns all attributes of this TreeElement. If there are no attributes, an empty list
	 * will be returned.
	 * @return string list of all attributes
	 */
	public List<String> getAttributes() {
		List<String> as = new ArrayList<String>();
		for(String s : attributes.keySet()) {
			as.add(s);
		}
		return as;
	}
	
	/**
	 * Returns a list of all values. If there are no values an empty list will be returned.
	 * @return string list of all values
	 */
	public List<String> getValues() {
		List<String> vs = new ArrayList<String>();
		for(String s : attributes.values()) {
			vs.add(s);
		}
		return vs;
	}
	
	
	
	// ------------------------------------------------------------
	// SETTER
	// ------------------------------------------------------------
	/**
	 * Sets a new attribute with key <code>key</code> and value <code>value</code>.
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, String value) {
		attributes.put(key, value);
	}
	
	
	
	// ------------------------------------------------------------
	// BOOLEAN RETURN
	// ------------------------------------------------------------
	/**
	 * Returns true if this TreeElement is a leaf, false otherwise.
	 * @return true if and only if this TreeElement is a leaf
	 */
	public boolean isLeaf() {
		return isLeaf;
	}
	
	/**
	 * Returns true if there are attributes.
	 * @return true if and only if this TreeElement has attributes.
	 */
	public boolean hasAttributes() {
		return !attributes.isEmpty();
	}
	
	/**
	 * Returns true if this TreeElement has an attribute <code>key</code>.
	 * @param key
	 * @return true if and only if this TreeElement has the attribute <code>key</code>
	 */
	public boolean hasAttribute(String key) {
		return attributes.containsKey(key);
	}

}
