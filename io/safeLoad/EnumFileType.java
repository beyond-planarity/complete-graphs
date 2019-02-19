package io.safeLoad;

public enum EnumFileType {

	XML, GML, TXT;
	
	public static String getFileExtension(EnumFileType type) {
		switch(type) {
		case XML:  	       return "xml";
		case GML:  	       return "gml";
		case TXT: default: return "txt";
		}
	}
}
