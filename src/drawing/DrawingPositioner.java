package drawing;

import java.util.HashMap;
import java.util.Map.Entry;

import data.Constant;
import data.Coordinate;
import data.Edge;
import data.Embedding;
import data.EnumColor;
import data.Metadata;
import data.Vertex;
import io.safeLoad.SafeLoad;

/**
 * Class to position the vertices of all drawings in a certain folder.
 * @author tommy
 *
 */
public class DrawingPositioner {

	private String folder; // name of folder on hard disc
	
	/**
	 * Creates a new <code>DrawingPositioner</code>
	 * @param folder folder with the drawings
	 */
	public DrawingPositioner(String folder) {
		this.folder = folder;
	}

	
	/**
	 * Calculate the position and the color of all embeddings in the given folder.
	 * @param newVertexName name of the new vertex
	 */
	public void calcPosAndColorSmallSize(String newVertexName) {
		if (SafeLoad.isEmpty(folder)) {
			return;
		}
		
		Metadata metadata = SafeLoad.loadMetadata(folder);
		String[] embNames = SafeLoad.getEmbeddingFiles(folder);
		
		System.out.println();
		System.out.println(folder + " - Calculate positions of " + metadata.getNumberEmbeddingsTotal() + " embeddings.");
		String strFront = folder + " - Calculated positions: ";
		String strBack  = "/" + metadata.getNumberEmbeddingsTotal();
		
		for (int i=0; i<embNames.length; i++) {
			Embedding emb = SafeLoad.loadEmbedding(folder, embNames[i]);
			calcPos(emb, newVertexName);
			emb.setDrawingNr(i);
			SafeLoad.safeEmbedding(emb, folder);
			
			if ((i+1) % Constant.DRAWING_PROGRESS == 0) {
				System.out.println(strFront + (i+1) + strBack);
			}
		}
	}
	

	/**
	 * Calculate the position and the color of all embeddings in the given folder.
	 * @param newVertexName name of the new vertex
	 */
	public void calcPosAndColorLargeSize(String newVertexName) {
		if (!SafeLoad.hasMetaData(folder)) {
			return;
		}
		
		Metadata metadata = SafeLoad.loadMetadata(folder);
		int firstId = metadata.getIdStartEmbedding();
		int id = firstId;
		
		System.out.println();
		System.out.println(folder + " - Calculate positions of " + metadata.getNumberEmbeddingsTotal() + " embeddings.");
		String strFront = folder + " - Calculated positions: ";
		String strBack  = "/" + metadata.getNumberEmbeddingsTotal();
		
		int counter = 1;
		do {
			Embedding emb = SafeLoad.loadEmbedding(folder, id);
			calcPos(emb, newVertexName);
			emb.setDrawingNr(counter);
			SafeLoad.safeEmbedding(emb, folder);
			id = emb.getNextEmbedding();
			
			if (counter % Constant.DRAWING_PROGRESS == 0) {
				System.out.println(strFront + counter + strBack);
			}
			counter++;
		} while (id != firstId);
	}
	
	/**
	 * Calculates the positions for the specified embedding.
	 * Also calculates additional information for showing the embedding on the screen.
	 * @param embedding		embedding
	 * @param newVertexName	name of the lastly inserted vertex
	 * @return				the drawing
	 */
	private void calcPos(Embedding embedding, String newVertexName) {
		
		Embedding triangulation       = Triangulator.triangulate(embedding, -1);
		CanonicalOrder canonicalOrder = new CanonicalOrder(triangulation);
		canonicalOrder.calculate();
		
		HashMap<Integer, Coordinate> coordinates = TriangulationPositioner.calculatePositions(
				triangulation,
				canonicalOrder.getVertexOrder(),
				canonicalOrder.getChildren());
		
		
		// color the nodes
		for (Vertex v : embedding.getVertices().values()) {
			if (v.isCrossing()) {
				v.setColor(EnumColor.CROSSING);
			}
			else if (v.getName().startsWith(Embedding.SET2_LETTER)) {
				v.setColor(EnumColor.SET2);
			}
			else {
				v.setColor(EnumColor.SET1);
			}
		}
		
		if (embedding.hasVertexWithName(newVertexName)) {
			// make new node green
			Vertex v = embedding.getVertexByName(newVertexName);
			v.setColor(EnumColor.NEW_NODE);
		}
		
		
		// save the positions of the nodes
		for (Entry<Integer, Coordinate> entry : coordinates.entrySet()) {
			int id = entry.getKey();
			Coordinate coord = entry.getValue();

			if (embedding.hasVertex(id)) {
				Vertex v = embedding.getVertex(id);
				v.setX(coord.getX());
				v.setY(coord.getY());
			}
		}
		
		calcStrechedPos(embedding);
		
		// calculate edges to draw
		for (Edge e : embedding.getEdges().values()) {			   
			if (!e.isToDraw() && !e.getTwin().isToDraw()) {	
				if (!embedding.isWholeEdge(e)) {
					while (e.getEdgeAfterCrossing() != null) {
						e = e.getEdgeAfterCrossing();
					}
					e.setToDraw(true);
					while (e.getEdgeBeforeCrossing() != null) {
						e = e.getEdgeBeforeCrossing();
						e.setToDraw(true);
					}
				}
				else {
					e.setToDraw(true);
				}
			}
		}
		
		// calculate edge labels
		for (Entry<Integer, Edge> entry : embedding.getEdges().entrySet()) {	
			int  eId = entry.getKey();
			Edge e   = entry.getValue();
			
			if (!e.isToDraw()) {
				continue;
			}

			int realSourceId = embedding.getRealSource(eId);
			int realTargetId = embedding.getRealTarget(eId);
			Vertex v = embedding.getVertex(realSourceId);
			if (v.getName().startsWith(Embedding.SET1_LETTER)) {
				v = embedding.getVertex(realTargetId);
			}
			e.setName(v.getName());
			e.setMappedName(v.getMappedName());
		}
		
		// calculate labels of gaps
		for (Entry<Integer, Integer> entry : embedding.getGapCrossingsToEdge().entrySet()) {
			int gapId = entry.getKey();
			int edge  = entry.getValue();
			Edge e    = embedding.getEdge(edge);
			
			if (!e.isToDraw()) {
				e    = e.getTwin();
				edge = e.getId();
			}

			int realSourceId = embedding.getRealSource(edge);
			int realTargetId = embedding.getRealTarget(edge);
			Vertex realV = embedding.getVertex(realSourceId);
			if (realV.getName().startsWith(Embedding.SET1_LETTER)) {
				realV = embedding.getVertex(realTargetId);
			}
			
			embedding.setVertexName(gapId, /*embedding.getVertex(gapId).getInitName() +*/ "*" + realV.getName());			
		}
	}
	
	
	
	/**
	 * Calculates the stretched coordinates for all embeddings.
	 */
	public void calculateStretchedCoordinates() {
		if (SafeLoad.isEmpty(folder)) {
			return;
		}
		
		Metadata metadata = SafeLoad.loadMetadata(folder);
		int firstId = metadata.getIdStartEmbedding();
		int id = firstId;
		
		System.out.println();
		System.out.println(folder + " - Calculate stretched coordinates of " + metadata.getNumberEmbeddingsTotal() + " embeddings.");
		String strFront = folder + " - Calculated coordinates: ";
		String strBack  = "/" + metadata.getNumberEmbeddingsTotal();
		
		int counter = 1;
		do {
			Embedding emb = SafeLoad.loadEmbedding(folder, id);
			calcStrechedPos(emb);
			SafeLoad.safeEmbedding(emb, folder);
			id = emb.getNextEmbedding();
			
			counter++;
			if (counter % Constant.DRAWING_PROGRESS == 0) {
				System.out.println(strFront + counter + strBack);
			}
		} while (id != firstId);
	}
		
	/**
	 * Calculates the stretched Coordinates for the specified embedding.
	 * @param emb	an embedding
	 */
	private void calcStrechedPos(Embedding emb) {
		// save the positions of the nodes
		int maxX = Integer.MIN_VALUE;
		int minX = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		
		for (Vertex v : emb.getVertices().values()) {
			maxX = Math.max(maxX, v.getX());
			maxY = Math.max(maxY, v.getY());
			minX = Math.min(minX, v.getX());
			minY = Math.min(minY, v.getY());
		}

		float stretchFactorX = ((float) Constant.CANVAS_WIDTH - 2 * Constant.NODE_SIZE) / ((float) maxX - minX);
		float stretchFactorY = ((float) Constant.CANVAS_HEIGHT - 2* Constant.NODE_SIZE) / ((float) maxY - minY);

		for (Vertex v : emb.getVertices().values()) {
			int strechedX = (int) (Constant.NODE_SIZE + (v.getX() - minX) * stretchFactorX);
			int strechedY = (int) (Constant.NODE_SIZE + (v.getY() - minY) * stretchFactorY);
			v.setStrechedX(strechedX);
			v.setStrechedY(strechedY);
		}
	}
	
}
