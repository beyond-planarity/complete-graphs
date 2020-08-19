package io.parseTreeTranslator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import data.Edge;
import data.Embedding;
import data.EnumColor;
import data.Face;
import data.Vertex;
import io.parseTree.Leaf;
import io.parseTree.Node;
import io.parseTree.TreeElement;

/**
 * Class to help saving.
 * @author tommy
 *
 */
public class ParseTreeXMLEmbeddingTranslator {

	private static final String L_EMBEDDING           = "em";
	private static final String L_SOURCE_ID           = "sEm";
	private static final String L_REFERENCE_ID        = "rEm";
	private static final String L_NEXT_EMBEDDING      = "nEm";
	private static final String L_PREV_EMBEDDING      = "pEm";
	private static final String L_NEXT_TOPO_EMBEDDING = "nTEm";
	private static final String L_PREV_TOPO_EMBEDDING = "pTEm";
	private static final String L_INSERTION_POSSIBLE  = "ins";
	private static final String L_DRAWING_NO          = "dNo";
	
	private static final String L_VERTEX_SET = "vSet";
	private static final String L_EDGE_SET   = "eSet";
	private static final String L_FACE_SET   = "fSet";
	private static final String L_GAP_SET    = "gSet";
	private static final String L_VERTEX     = "v";
	private static final String L_EDGE       = "e";
	private static final String L_FACE       = "f";
	private static final String L_GAP        = "g";

	private static final String L_TRUE            = "1";
	private static final String L_FALSE           = "0";
	private static final String L_NO_SUCH_ELEMENT = "-1";
	
	private static final String L_ID    = "id";
	private static final String L_NAME  = "name";
	private static final String L_LABEL = "l";
	private static final String L_COLOR = "c";
	
	private static final String L_IS_CROSSING    = "cr";
	private static final String L_OUT_EDGE       = "oE";
	private static final String L_X              = "x";
	private static final String L_Y              = "y";
	private static final String L_STRECHED_X     = "sX";
	private static final String L_STRECHED_Y     = "sY";
	
	private static final String L_EDGE_AFTER_CR  = "eACr";
	private static final String L_EDGE_BEFORE_CR = "eBCr";
	private static final String L_SOURCE         = "s";
	private static final String L_TARGET         = "t";
	private static final String L_LEFT_FACE      = "lF";
	private static final String L_TWIN           = "tE";
	private static final String L_NEXT           = "nE";
	private static final String L_PREVIOUS       = "pE";
	private static final String L_TO_DRAW        = "draw";
	
	private static final String L_INCIDENT_EDGE  = "iE";
	
	


	public static TreeElement createTree(Embedding emb) {
		Node root = new Node(L_EMBEDDING);
		root.setAttribute(L_ID, emb.getId() + "");
		root.setAttribute(L_SOURCE_ID, emb.getSourceEmbeddingId() + "");
		root.setAttribute(L_REFERENCE_ID, emb.getReferenceEmbeddingId() + "");
		root.setAttribute(L_INSERTION_POSSIBLE , emb.isInsertionPossible() ? L_TRUE : L_FALSE);

		root.setAttribute(L_NEXT_EMBEDDING, emb.getNextEmbedding() + "");
		root.setAttribute(L_PREV_EMBEDDING, emb.getPrevEmbedding() + "");
		root.setAttribute(L_NEXT_TOPO_EMBEDDING, emb.getNextTopoEmbedding() + "");
		root.setAttribute(L_PREV_TOPO_EMBEDDING, emb.getPrevTopoEmbedding() + "");
		root.setAttribute(L_DRAWING_NO, emb.getDrawingNr() + "");
		
		root.addChild(createVertexTree(emb));
		root.addChild(createEdgeTree(emb));
		root.addChild(createFaceTree(emb));
		
		root.addChild(createGapTree(emb));
		
		return root;
	}
	
	private static TreeElement createVertexTree(Embedding emb) {
		Node nVertices = new Node(L_VERTEX_SET);

		// vertices
		for (Entry<Integer, Vertex> entry : emb.getVertices().entrySet()) {
			int    id = entry.getKey();
			Vertex v  = entry.getValue();

			Leaf lVertex = new Leaf(L_VERTEX);
			lVertex.setAttribute(L_ID, id + "");
			lVertex.setAttribute(L_IS_CROSSING, v.isCrossing() ? L_TRUE : L_FALSE);
			lVertex.setAttribute(L_OUT_EDGE, v.getOutEdge() == null ? L_NO_SUCH_ELEMENT : v.getOutEdge().getId() + "");
			lVertex.setAttribute(L_NAME, v.getName());
			lVertex.setAttribute(L_X, v.getX() + "");
			lVertex.setAttribute(L_Y, v.getY() + "");
			lVertex.setAttribute(L_STRECHED_X, v.getStrechedX() + "");
			lVertex.setAttribute(L_STRECHED_Y, v.getStrechedY() + "");
			lVertex.setAttribute(L_LABEL, v.getMappedName());
			lVertex.setAttribute(L_COLOR, v.getEnumColor().getString());
			
			nVertices.addChild(lVertex);
		}
		
		return nVertices;
	}
	
	private static TreeElement createEdgeTree(Embedding emb) {
		Node nEdges = new Node(L_EDGE_SET);

		// edges
		for (Entry<Integer, Edge> entry : emb.getEdges().entrySet()) {
			int  id = entry.getKey();
			Edge e  = entry.getValue();

			Leaf lEdge = new Leaf(L_EDGE);
			lEdge.setAttribute(L_ID, id + "");
			lEdge.setAttribute(L_SOURCE, e.getSource().getId() + "");
			lEdge.setAttribute(L_TARGET, e.getTarget().getId() + "");
			lEdge.setAttribute(L_LEFT_FACE, e.getFace().getId() + "");
			lEdge.setAttribute(L_TWIN, e.getTwin().getId() + "");
			lEdge.setAttribute(L_NEXT, e.getNext().getId() + "");
			lEdge.setAttribute(L_PREVIOUS, e.getPrevious().getId() + "");
			lEdge.setAttribute(L_EDGE_BEFORE_CR, e.getEdgeBeforeCrossing() == null ? L_NO_SUCH_ELEMENT : e.getEdgeBeforeCrossing().getId() + "");
			lEdge.setAttribute(L_EDGE_AFTER_CR, e.getEdgeAfterCrossing() == null ? L_NO_SUCH_ELEMENT : e.getEdgeAfterCrossing().getId() + "");
			lEdge.setAttribute(L_NAME, e.getName());
			lEdge.setAttribute(L_LABEL, e.getMappedName());
			lEdge.setAttribute(L_TO_DRAW, e.isToDraw() ? L_TRUE : L_FALSE);
			 
			nEdges.addChild(lEdge);
		}
		
		return nEdges;
	}
		
	private static TreeElement createFaceTree(Embedding emb) {
		Node nFaces = new Node(L_FACE_SET);

		// faces
		for (Entry<Integer, Face> entry : emb.getFaces().entrySet()) {
			int  id = entry.getKey();
			Face f  = entry.getValue();

			Leaf lFace = new Leaf(L_FACE);
			lFace.setAttribute(L_ID, id + "");
			lFace.setAttribute(L_INCIDENT_EDGE, f.getIncidentEdge() == null ? L_NO_SUCH_ELEMENT : f.getIncidentEdge().getId() + "");

			nFaces.addChild(lFace);
		}
		
		return nFaces;
	}
	
	private static TreeElement createGapTree(Embedding emb) {
		Node nGaps = new Node(L_GAP_SET);

		// faces
		for (Entry<Integer, Integer> entry : emb.getGapCrossingsToEdge().entrySet()) {
			int cr = entry.getKey();
			int ed = entry.getValue();

			Leaf lGap = new Leaf(L_GAP);
			lGap.setAttribute(L_VERTEX, cr + "");
			lGap.setAttribute(L_EDGE, ed + "");

			nGaps.addChild(lGap);
		}

		return nGaps;
	}
	
	
	
	
	

	public static Embedding treeToEmbedding(TreeElement treeRoot) throws ParseTreeStructureException {
		Embedding emb;
		
		if(!treeRoot.getName().equals(L_EMBEDDING) || treeRoot.isLeaf()) {
			throw new ParseTreeStructureException("Emedding.Root");
		}
		Node nRoot = (Node) treeRoot;
		
		// get next id's
		String embId          = treeRoot.getAttribute(L_ID);
		String sourceEmbId    = treeRoot.getAttribute(L_SOURCE_ID);
		String referenceEmbId = treeRoot.getAttribute(L_REFERENCE_ID);
		String insertionPos   = treeRoot.getAttribute(L_INSERTION_POSSIBLE);
		String nextEmb        = treeRoot.getAttribute(L_NEXT_EMBEDDING);
		String prevEmb        = treeRoot.getAttribute(L_PREV_EMBEDDING);
		String nextTopoEmb    = treeRoot.getAttribute(L_NEXT_TOPO_EMBEDDING);
		String prevTopoEmb    = treeRoot.getAttribute(L_PREV_TOPO_EMBEDDING);
		String drawingNo      = treeRoot.getAttribute(L_DRAWING_NO);
		
		
		try {
			emb = new Embedding(Integer.parseInt(embId));
			emb.setSourceEmbeddingId(Integer.parseInt(sourceEmbId));
			emb.setReferenceEmbeddingId(Integer.parseInt(referenceEmbId));
			emb.setInsertionPossible(insertionPos.equals(L_TRUE));
			emb.setNextEmbedding(Integer.parseInt(nextEmb));
			emb.setPrevEmbedding(Integer.parseInt(prevEmb));
			emb.setNextTopoEmbedding(Integer.parseInt(nextTopoEmb));
			emb.setPrevTopoEmbedding(Integer.parseInt(prevTopoEmb));
			emb.setDrawingNr(Integer.parseInt(drawingNo));
		} catch(NumberFormatException | NullPointerException e) {
			throw new ParseTreeStructureException("Embedding.Parsing");
		}
		
		// create vertices
		List<TreeElement> teVertexSet = nRoot.getChildren(L_VERTEX_SET);
		List<Leaf>        lVertices   = new LinkedList<Leaf>();
		if(teVertexSet != null && !teVertexSet.isEmpty()) {
			if(teVertexSet.size() != 1 || teVertexSet.get(0).isLeaf()) {
				throw new ParseTreeStructureException("VertexSet");
			}
			Node nVertexSet = (Node) teVertexSet.get(0);

			for(TreeElement element : nVertexSet.getChildren(L_VERTEX)) {
				if(element.isLeaf()) {
					Leaf lVertex = (Leaf) element;
					lVertices.add(lVertex);
					String strId        = lVertex.getAttribute(L_ID);
					String strName      = lVertex.getAttribute(L_NAME);
					String strX         = lVertex.getAttribute(L_X);
					String strY         = lVertex.getAttribute(L_Y);
					String strStrechedX = lVertex.getAttribute(L_STRECHED_X);
					String strStrechedY = lVertex.getAttribute(L_STRECHED_Y);
					String strLabel     = lVertex.getAttribute(L_LABEL);
					String strColor     = lVertex.getAttribute(L_COLOR);

					try {
						Vertex v = emb.createVertex(Integer.parseInt(strId));
						emb.setVertexName(v.getId(), strName);
						v.setX(Integer.parseInt(strX));
						v.setY(Integer.parseInt(strY));
						v.setStrechedX(Integer.parseInt(strStrechedX));
						v.setStrechedY(Integer.parseInt(strStrechedY));
						v.setMappedName(strLabel);
						v.setColor(EnumColor.getEnumColor(strColor));
						
					} catch(NumberFormatException | NullPointerException e) {
						throw new ParseTreeStructureException("Vertex.Creation.Parsing");
					}
				}
				else {
					throw new ParseTreeStructureException("Vertex.Creation");
				}
			}
		}

		// create edges
		List<TreeElement> teEdgeSet = nRoot.getChildren(L_EDGE_SET);
		List<Leaf>        lEdges    = new LinkedList<Leaf>();
		if(teEdgeSet != null && !teEdgeSet.isEmpty()) {
			if(teEdgeSet.size() != 1 || teEdgeSet.get(0).isLeaf()) {
				throw new ParseTreeStructureException("EdgeSet");
			}
			Node nEdgeSet = (Node) teEdgeSet.get(0);

			for(TreeElement element : nEdgeSet.getChildren(L_EDGE)) {
				if(element.isLeaf()) {
					Leaf lEdge = (Leaf) element;
					lEdges.add(lEdge);
					try {
						Edge e = emb.createEdge(Integer.parseInt(lEdge.getAttribute(L_ID)));
						e.setName(lEdge.getAttribute(L_NAME));
						e.setMappedName(lEdge.getAttribute(L_LABEL));
						e.setToDraw(lEdge.getAttribute(L_TO_DRAW).equals(L_TRUE));
					} catch(NumberFormatException | NullPointerException e) {
						throw new ParseTreeStructureException("Edge.Creation.Parsing");
					}
				}
				else {
					throw new ParseTreeStructureException("Edge.Creation");
				}
			}
		}
		
		// create faces
		List<TreeElement> teFaceSet = nRoot.getChildren(L_FACE_SET);
		List<Leaf>        lFaces    = new LinkedList<Leaf>();
		if(teFaceSet != null && !teFaceSet.isEmpty()) {
			if(teFaceSet.size() != 1 || teFaceSet.get(0).isLeaf()) {
				throw new ParseTreeStructureException("FaceSet");
			}
			Node nFaceSet = (Node) teFaceSet.get(0);

			for(TreeElement element : nFaceSet.getChildren(L_FACE)) {
				if(element.isLeaf()) {
					Leaf lFace = (Leaf) element;
					lFaces.add(lFace);
					try {
						emb.createFace(Integer.parseInt(lFace.getAttribute(L_ID)));
					} catch(NumberFormatException | NullPointerException e) {
						throw new ParseTreeStructureException("Face.Creation.Parsing");
					}
				}
				else {
					throw new ParseTreeStructureException("Face.Creation");
				}
			}
		}

		// create gaps
		List<TreeElement> teGapSet = nRoot.getChildren(L_GAP_SET);
		List<Leaf>        lGaps    = new LinkedList<Leaf>();
		if(teGapSet != null && !teGapSet.isEmpty()) {
			if(teGapSet.size() != 1 || teGapSet.get(0).isLeaf()) {
				throw new ParseTreeStructureException("GapSet");
			}
			Node nGapSet = (Node) teGapSet.get(0);

			for(TreeElement element : nGapSet.getChildren(L_GAP)) {
				if(element.isLeaf()) {
					Leaf lGap = (Leaf) element;
					lGaps.add(lGap);
				}
				else {
					throw new ParseTreeStructureException("Gap.Creation");
				}
			}
		}
		

		// set data for vertices
		for (Leaf lVertex : lVertices) {
			try {
				int     idVertex    = Integer.parseInt(lVertex.getAttribute(L_ID));
				String  strCrossing = lVertex.getAttribute(L_IS_CROSSING);
				String  strOutEdge  = lVertex.getAttribute(L_OUT_EDGE);
				boolean bIsCrossing = strCrossing.equals(L_TRUE);
				int     idOutEdge   = Integer.parseInt(strOutEdge);
				emb.setVertex(idVertex, bIsCrossing, idOutEdge);
			} catch(NumberFormatException | NullPointerException e) {
				throw new ParseTreeStructureException("Vertex.Data");
			}
		}
		
		// set data for edges
		for (Leaf lEdge : lEdges) {
			try {
				int idEdge   = Integer.parseInt(lEdge.getAttribute(L_ID));
				int idSource = Integer.parseInt(lEdge.getAttribute(L_SOURCE));
				int idTarget = Integer.parseInt(lEdge.getAttribute(L_TARGET));
				int idFace   = Integer.parseInt(lEdge.getAttribute(L_LEFT_FACE));
				int idTwin   = Integer.parseInt(lEdge.getAttribute(L_TWIN));
				int idNext   = Integer.parseInt(lEdge.getAttribute(L_NEXT));
				int idPrev   = Integer.parseInt(lEdge.getAttribute(L_PREVIOUS));
				int idBefCr  = Integer.parseInt(lEdge.getAttribute(L_EDGE_BEFORE_CR));
				int idAftCr  = Integer.parseInt(lEdge.getAttribute(L_EDGE_AFTER_CR));

				emb.setEdge(idEdge, idSource, idTarget, idFace,
						idTwin, idNext, idPrev, idBefCr, idAftCr);
			} catch(NumberFormatException | NullPointerException e) {
				throw new ParseTreeStructureException("Edge.Data");
			}
		}

		// set data for faces
		for (Leaf lFace : lFaces) {
			try {
				int idFace   = Integer.parseInt(lFace.getAttribute(L_ID));
				int idEdge = Integer.parseInt(lFace.getAttribute(L_INCIDENT_EDGE));
				emb.setFace(idFace, idEdge);
			} catch(NumberFormatException | NullPointerException e) {
				throw new ParseTreeStructureException("Face.Data");
			}
		}

		// set data for gaps
		for (Leaf lGap : lGaps) {
			try {
				int idGap  = Integer.parseInt(lGap.getAttribute(L_VERTEX));
				int idEdge = Integer.parseInt(lGap.getAttribute(L_EDGE));
				emb.setGapCrossingToEdge(idGap, idEdge);
			} catch(NumberFormatException | NullPointerException e) {
				throw new ParseTreeStructureException("Gap.Data");
			}
		}
		
		return emb;
	}

}
