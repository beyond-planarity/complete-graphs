package drawing;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import data.Edge;
import data.Embedding;
import data.Face;
import data.Vertex;

public class Triangulator {
	
	public static Embedding triangulate(final Embedding embedding, int triangulationId) {

		Embedding triangulation      = embedding.copy(triangulationId);
		HashMap<Integer, Face> faces = embedding.getFaces();
		
		for (Entry<Integer, Face> entry : faces.entrySet()) {
			int  faceId = entry.getKey();
			if (triangulation.getFaceDegree(faceId) <= 3) {
				continue;
			}
						
			Vertex middleVertex = triangulation.createVertex();
			int    targetId     = middleVertex.getId();
			List<Edge> edges    = embedding.getIncidentEdgesToFace(faceId);
			for (Edge e : edges) {
				int sourceId        = e.getSource().getId();
				Edge triangEdge     = triangulation.getEdge(e.getId());
				int insertionFaceId = triangEdge.getFace().getId();
				triangulation.insertEdge(sourceId, targetId, insertionFaceId,-1);
			}
		}
		
		return triangulation;
	}

}
