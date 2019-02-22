package io.safeLoad;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import data.Edge;
import data.Embedding;
import data.EnumColor;
import data.Vertex;

/**
 * Class to write to an gml file.
 * @author tommy
 *
 */
public class GMLWriter {
	
	private static final int    STRETCH_FACTOR_X = 60;
	private static final int    STRETCH_FACTOR_Y = 80;
	
	private static final String LINE_SEPARATOR  = "\n";
	private static final String TAB             = "\t";
	private static final String TAGSTART        = "[";
	private static final String TAGEND          = "]";
	private static final String STR_DELIM       = "\"";
	private static final String GRAPH           = "graph";
	private static final String HIERARCHIC      = "hierarchic";
	private static final String DIRECTED        = "directed";
	private static final String NODE            = "node";
	private static final String LABEL           = "label";
	private static final String GRAPHICS        = "graphics";
	private static final String ID              = "id";
	private static final String X_POS           = "x";
	private static final String Y_POS           = "y";
	private static final String WIDTH_LABEL     = "w";
	private static final String WIDTH           = "30.0";
	private static final String HEIGTH_LABEL    = "h";
	private static final String HEIGTH          = "15.0";
	private static final String TYPE            = "type";
	private static final String RAISED_BORDER   = "raisedBorder";
	private static final String FILL            = "fill";
	private static final String OUTLINE         = "outline";
	private static final String ELLIPSE         = STR_DELIM + "ellipse" + STR_DELIM;
	private static final String TRUE            = "1";
	private static final String FALSE           = "0";
	private static final String COLOR_SET1      = STR_DELIM + "#0000FF" + STR_DELIM;
	private static final String COLOR_SET2      = STR_DELIM + "#008000" + STR_DELIM;
	private static final String COLOR_CROSSING  = STR_DELIM + "#FF0000" + STR_DELIM;
	private static final String COLOR_TEXT      = STR_DELIM + "#FFFFFF" + STR_DELIM;
	private static final String COLOR_OUTLINE   = STR_DELIM + "#000000" + STR_DELIM;
	private static final String COLOR_EDGE      = STR_DELIM + "#000000" + STR_DELIM;
	private static final String COLOR_EDGE_FILL = STR_DELIM + "#FFFFFF" + STR_DELIM;
	private static final String LABEL_GRAPHICS  = "LabelGraphics";
	private static final String EMPTY           = STR_DELIM + STR_DELIM;
	private static final String TEXT            = "text";
	private static final String COLOR           = "color";
	private static final String FONT_SIZE_LABEL = "fontSize";
	private static final String FONT_SIZE       = "12";
	private static final String FONT_NAME_LABEL = "fontName";
	private static final String FONT_NAME       = STR_DELIM + "Dialog" + STR_DELIM;
	private static final String ANCHOR_LABEL    = "anchor";
	private static final String ANCHOR          = STR_DELIM + "c" + STR_DELIM;
	private static final String EDGE            = "edge";
	private static final String SOURCE          = "source";
	private static final String TARGET          = "target";
	private static final String CONFIG_LABEL    = "configuration";
	private static final String CONFIG          = STR_DELIM + "AutoFlippingLabel" + STR_DELIM;
	private static final String CONTENT_W_LABEL = "contentWidth";
	private static final String CONTENT_W       = "36";
	private static final String CONTENT_H_LABEL = "contentHeight";
	private static final String CONTENT_H       = "20";
	private static final String MODEL_LABEL     = "model";
	private static final String MODEL           = STR_DELIM + "centered" + STR_DELIM;
	private static final String POS_LABEL       = "position";
	private static final String POS             = STR_DELIM + "center" + STR_DELIM;
	
	
	
	
	private String 	   writePath;
	private Embedding  emb;
	private FileWriter fileWriter;

	
	/**
	 * Creates a new XMLTreeWritter.
	 * @param writePath path to write the new file to (if there is an old file it will be overwritten)
	 * @param treeRoot root of the tree which shall be written
	 */
	public GMLWriter(String writePath, Embedding emb) {
		this.writePath = writePath;
		this.emb       = emb;
	}
	
	
	/**
	 * Writes the tree to the file.
	 * @throws IOException
	 */
	public void write() throws IOException {
	    fileWriter = new FileWriter(writePath);
	    
		if(emb == null) {
			return;
		}
		
		writeStart();
		writeNodes(emb.getVertices());
		writeEdges(emb.getEdges());
		writeEnd();
		
		fileWriter.flush();
		fileWriter.close();
	}
	
	private void writeStart() throws IOException {
		String start = GRAPH + LINE_SEPARATOR;
		start += TAGSTART + LINE_SEPARATOR;
		start += TAB + HIERARCHIC + TAB + TRUE + LINE_SEPARATOR;
		start += TAB + LABEL + TAB + EMPTY + LINE_SEPARATOR;
		start += TAB + DIRECTED + TAB + TRUE + LINE_SEPARATOR;
		fileWriter.append(start);
	}
	
	private void writeNodes(HashMap<Integer, Vertex> vertices) throws IOException {
		for (Vertex v : vertices.values()) {
			writeNode(v);
		}
	}
	
	private void writeNode(Vertex v) throws IOException {
		String str = TAB + NODE + LINE_SEPARATOR;
		str += TAB + TAGSTART + LINE_SEPARATOR;
		str += TAB + TAB + ID + TAB + v.getId() + LINE_SEPARATOR;
		str += TAB + TAB + LABEL + TAB + STR_DELIM + v.getName() + STR_DELIM + LINE_SEPARATOR;
		str += TAB + TAB + GRAPHICS + LINE_SEPARATOR;
		str += TAB + TAB + TAGSTART + LINE_SEPARATOR;
		// inside graphics now
		str += TAB + TAB + TAB + X_POS + TAB + v.getStretchedX(STRETCH_FACTOR_X) + LINE_SEPARATOR;
		str += TAB + TAB + TAB + Y_POS + TAB + v.getStretchedY(STRETCH_FACTOR_Y) + LINE_SEPARATOR;
		str += TAB + TAB + TAB + WIDTH_LABEL + TAB + WIDTH + LINE_SEPARATOR;
		str += TAB + TAB + TAB + HEIGTH_LABEL + TAB + HEIGTH + LINE_SEPARATOR;
		str += TAB + TAB + TAB + TYPE + TAB + ELLIPSE + LINE_SEPARATOR;
		str += TAB + TAB + TAB + RAISED_BORDER + TAB + FALSE + LINE_SEPARATOR;
		
		String col = COLOR_SET1;
		if (v.getEnumColor() == EnumColor.SET2) {
			col = COLOR_SET2;
		}
		else if (v.getEnumColor() == EnumColor.CROSSING) {
			col = COLOR_CROSSING;
		}
		
		str += TAB + TAB + TAB + FILL + TAB + col + LINE_SEPARATOR;
		str += TAB + TAB + TAB + OUTLINE + TAB + COLOR_OUTLINE + LINE_SEPARATOR;
		str += TAB + TAB + TAGEND + LINE_SEPARATOR;
		
		str += TAB + TAB + LABEL_GRAPHICS + LINE_SEPARATOR;
		str += TAB + TAB + TAGSTART + LINE_SEPARATOR;
		// inside LabelGraphics now
		str += TAB + TAB + TAB + TEXT + TAB + STR_DELIM + v.getName() + STR_DELIM + LINE_SEPARATOR;
		str += TAB + TAB + TAB + COLOR + TAB + COLOR_TEXT + LINE_SEPARATOR;
		str += TAB + TAB + TAB + FONT_SIZE_LABEL + TAB + FONT_SIZE + LINE_SEPARATOR;
		str += TAB + TAB + TAB + FONT_NAME_LABEL + TAB + FONT_NAME + LINE_SEPARATOR;
		str += TAB + TAB + TAB + ANCHOR_LABEL + TAB + ANCHOR + LINE_SEPARATOR;
		str += TAB + TAB + TAGEND + LINE_SEPARATOR;
		str += TAB + TAGEND + LINE_SEPARATOR;
		
		// write starting tag
		fileWriter.append(str);
	}
	

	
	private void writeEdges(HashMap<Integer, Edge> edges) throws IOException {
		for (Edge e : edges.values()) {
			if (e.isToDraw()) {
				writeEdge(e);
			}
		}
	}


	
	private void writeEdge(Edge e) throws IOException {
		String str = TAB + EDGE + LINE_SEPARATOR;
		str += TAB + TAGSTART + LINE_SEPARATOR;
		str += TAB + TAB + SOURCE + TAB + e.getSource().getId() + LINE_SEPARATOR;
		str += TAB + TAB + TARGET + TAB + e.getTarget().getId() + LINE_SEPARATOR;
		str += TAB + TAB + GRAPHICS + LINE_SEPARATOR;
		str += TAB + TAB + TAGSTART + LINE_SEPARATOR;
		// inside graphics now
		str += TAB + TAB + TAB + FILL + TAB + COLOR_EDGE + LINE_SEPARATOR;
		str += TAB + TAB + TAGEND + LINE_SEPARATOR;
		
		str += TAB + TAB + LABEL_GRAPHICS + LINE_SEPARATOR;
		str += TAB + TAB + TAGSTART + LINE_SEPARATOR;
		// inside LabelGraphics now
		str += TAB + TAB + TAB + TEXT + TAB + STR_DELIM + e.getName() + STR_DELIM + LINE_SEPARATOR;
		str += TAB + TAB + TAB + FILL + TAB + COLOR_EDGE_FILL + LINE_SEPARATOR;
		str += TAB + TAB + TAB + FONT_SIZE_LABEL + TAB + FONT_SIZE + LINE_SEPARATOR;
		str += TAB + TAB + TAB + FONT_NAME_LABEL + TAB + FONT_NAME + LINE_SEPARATOR;
		str += TAB + TAB + TAB + CONFIG_LABEL + TAB + CONFIG + LINE_SEPARATOR;
		str += TAB + TAB + TAB + CONTENT_W_LABEL + TAB + CONTENT_W + LINE_SEPARATOR;
		str += TAB + TAB + TAB + CONTENT_H_LABEL + TAB + CONTENT_H + LINE_SEPARATOR;
		str += TAB + TAB + TAB + MODEL_LABEL + TAB + MODEL + LINE_SEPARATOR;
		str += TAB + TAB + TAB + POS_LABEL + TAB + POS + LINE_SEPARATOR;
		str += TAB + TAB + TAGEND + LINE_SEPARATOR;
		str += TAB + TAGEND + LINE_SEPARATOR;
		
		// write starting tag
		fileWriter.append(str);
	}
	
	
	private void writeEnd() throws IOException {
		String end = TAGEND + LINE_SEPARATOR;
		fileWriter.append(end);
	}
}
