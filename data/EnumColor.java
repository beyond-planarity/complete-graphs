package data;

import java.awt.Color;

public enum EnumColor {

	SET1, SET2, CROSSING, NEW_NODE, EDGE, INFO, DRAWING_BACK, VERTEX_TEXT;
	
	public Color getColor() {
		switch(this) {
		case SET1: default: return new Color(0,0,190);
		case SET2:  	    return new Color(130,40,130);
		case CROSSING:	    return new Color(156,0,0);
		case NEW_NODE:      return new Color(0,128,0);
		case EDGE:          return new Color(0,0,0);
		case INFO:          return new Color(0,0,0);
		case DRAWING_BACK:  return new Color(180,180,180);
		case VERTEX_TEXT:   return new Color(255,255,255);
		}
	}
	
	public String getString() {
		return this.name();
	}
	
	public static EnumColor getEnumColor(String name) {
		return EnumColor.valueOf(name);
	}
}