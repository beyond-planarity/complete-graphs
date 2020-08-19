package io.safeLoad;

public enum EnumFileType {

	XML,	// safe in xml-like style
	GML,	// safe in gml
	TXT;	// safe as txt
	
	public static String getFileExtension(EnumFileType type) {
		switch(type) {
		case XML:  	       return "xml";
		case GML:  	       return "gml";
		case TXT: default: return "txt";
		}
	}
}
