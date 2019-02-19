package drawing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import data.Coordinate;
import data.Embedding;
import data.Vertex;

public class TriangulationPositioner {


	public static HashMap<Integer, Coordinate> calculatePositions(
			Embedding triangulation,
			Vertex[] vertexOrder,
			HashMap<Integer, List<Integer>> children) {

		HashMap<Integer, Vertex>     vertices    = triangulation.getVertices();
		HashMap<Integer, Coordinate> coordinates = new HashMap<Integer, Coordinate>();
		HashMap<Integer, List<Integer>> covering = new HashMap<Integer, List<Integer>>();
		List<Integer>				 onOuterFace = new ArrayList<Integer>();

		for (Entry<Integer, Vertex> entry : vertices.entrySet()) {
			int    id = entry.getKey();
			List<Integer> covList = new ArrayList<Integer>();
			covList.add(id);
			covering.put(id, covList);
		}

		// initialize
		coordinates.put(vertexOrder[0].getId(), new Coordinate(0,0));
		coordinates.put(vertexOrder[1].getId(), new Coordinate(2,0));
		coordinates.put(vertexOrder[2].getId(), new Coordinate(1,1));

		onOuterFace.add(vertexOrder[0].getId());
		onOuterFace.add(vertexOrder[2].getId());
		onOuterFace.add(vertexOrder[1].getId());

		for (int k = 3; k < vertexOrder.length; k++) {
			int currentId    = vertexOrder[k].getId();
			List<Integer> cs = children.get(currentId);
			int leftChildId  = cs.get(0);
			int rightChildId = cs.get(cs.size()-1);

			int indexP = 0;
			while (onOuterFace.get(indexP) != leftChildId) {
				indexP++;
			}

			int indexQ = indexP + 1;
			// indexQ is now position of w_{p+1}; add 1 to the position
			List<Integer> currentCov = covering.get(currentId); // for calculating new covering		
			while (onOuterFace.get(indexQ) != rightChildId) {
				int outerId = onOuterFace.get(indexQ);
				for (Integer vertexId : covering.get(outerId)) {
					Coordinate coord = coordinates.get(vertexId);
					coord.setX(coord.getX() + 1);
					currentCov.add(vertexId); // adding to covering of v_k
				}
				indexQ++;
			}

			// indexQ is now position of w_q; add 2 to the position
			for (int i=indexQ; i<onOuterFace.size(); i++) {
				int outerId = onOuterFace.get(i);
				for (Integer vertexId : covering.get(outerId)) {
					Coordinate coord = coordinates.get(vertexId);
					coord.setX(coord.getX() + 2);
				}
			}

			// calculate coordinate of v_k
			int leftX  = coordinates.get(leftChildId).getX();
			int leftY  = coordinates.get(leftChildId).getY();
			int rightX = coordinates.get(rightChildId).getX();
			int rightY = coordinates.get(rightChildId).getY();
			int vkx    = (rightX + rightY + leftX - leftY) / 2;
			int vky    = (rightX + rightY - leftX + leftY) / 2;
			coordinates.put(currentId, new Coordinate(vkx,vky));

			// calculate new outer face
			for (int i = indexQ-1; i>indexP; i--) {
				onOuterFace.remove(i);
			}
			onOuterFace.add(indexP+1, currentId);
		}

		return coordinates;
	}

}
