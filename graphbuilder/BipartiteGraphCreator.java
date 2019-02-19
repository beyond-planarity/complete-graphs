package graphbuilder;

import java.util.LinkedList;
import java.util.List;

import data.Constant;
import data.Edge;
import data.Embedding;
import data.Face;
import data.Metadata;
import data.Tuple;
import data.Vertex;
import io.safeLoad.SafeLoad;
import nodeInserter.NodeInserter;

public class BipartiteGraphCreator {
	
	private static int    embeddingId = 0;

	private NodeInserter nodeInserter;
	private String       toFolder;
	private String       fromFolder;


	public BipartiteGraphCreator(NodeInserter nodeInserter, String fromFolder, String toFolder) {
		this.nodeInserter = nodeInserter;
		this.fromFolder   = fromFolder;
		this.toFolder     = toFolder;
	}
	
		
	
	/**
	 * Creates all topologically different drawings of a bipartite graph K_{x,y},
	 * starting from a planar K_{2,2} and using the series of indices (a,b), 2≤a≤x, 2≤b≤y,
	 * to create the final drawings step-by-step.
	 * Thereby consecutive indices must be such that the value a+b increases by 1.
	 * @param graphIndices
	 */
	public void createKxy(Tuple from, Tuple to) {
		
		System.out.println();
		System.out.println();
		System.out.println("Creating K_{" + to.first + "," + to.second + "} ...");
			
		if (SafeLoad.isEmpty(fromFolder)) {
			return;
		}
		SafeLoad.createFolder(toFolder);
		
		Metadata fromMeta     = SafeLoad.loadMetadata(fromFolder);
		int      firstEmbId   = fromMeta.getIdStartEmbedding();
		int      currentEmbId = firstEmbId;

		Embedding lastEmb       = null;
		int       totalNumber   = 0;
		int       newFirstEmbId = -1;

		String  newNodeName;
		boolean connectToSet1;
		
		if (to.first == from.first + 1 && to.second == from.second) {
			connectToSet1 = false;
			newNodeName   = Embedding.SET1_LETTER + to.first;
		}
		else {
			connectToSet1 = true;
			newNodeName   = Embedding.SET2_LETTER + to.second;
		}
		String strFront = "Inserted new node " + newNodeName + " into ";
		String strBack = "/" + fromMeta.getNumberEmbeddingsTopoDifferent() + " embeddings. Total new: ";
		int counter = 0;
		
		do {
			Embedding baseEmb = SafeLoad.loadEmbedding(fromFolder, currentEmbId);
			
			List<Embedding> newEmbeddings = insertNewNode(baseEmb, newNodeName, connectToSet1);
			SafeLoad.safeEmbedding(baseEmb, fromFolder);
			int newEmbSize = newEmbeddings.size();
			totalNumber   += newEmbSize;

			
			counter++;
			if (counter % Constant.INSERTING_PROGRESS == 0) {
				System.out.println(strFront + counter + strBack + totalNumber);
			}
			
			
			currentEmbId = baseEmb.getNextTopoEmbedding();
			if (newEmbSize == 0) {
				continue;
			}
			
			for (int i = 1; i<newEmbSize; i++) {
				Embedding emb1 = newEmbeddings.get(i-1);
				Embedding emb2 = newEmbeddings.get(i);
				emb1.setNextEmbedding(emb2.getId());
				emb2.setPrevEmbedding(emb1.getId());
			}

			Embedding emb1 = newEmbeddings.get(0);
			if (lastEmb != null) {
					emb1.setPrevEmbedding(lastEmb.getId());
					lastEmb.setNextEmbedding(emb1.getId());
					SafeLoad.safeEmbedding(lastEmb, toFolder);
			}
			if (newFirstEmbId < 0) {
				newFirstEmbId = emb1.getId();
			}
			
			for (Embedding emb : newEmbeddings) {
				SafeLoad.safeEmbedding(emb, toFolder);
			}
			
			lastEmb = newEmbeddings.get(newEmbSize-1);
			
		} while (currentEmbId != firstEmbId);


		System.out.println("Creation of K_{" + to.first + "," + to.second + "} finished. Found " + totalNumber + " embeddings.");

		
		if (totalNumber > 0) {
			Embedding emb = SafeLoad.loadEmbedding(toFolder, newFirstEmbId);
			lastEmb.setNextEmbedding(emb.getId());
			emb.setPrevEmbedding(lastEmb.getId());
			SafeLoad.safeEmbedding(emb, toFolder);
			SafeLoad.safeEmbedding(lastEmb, toFolder);
			
			SafeLoad.safeMetadata(toFolder, new Metadata(totalNumber, totalNumber, newFirstEmbId));
			
			System.out.println("K_{" + to.first + "," + to.second + "} is drawable.");
		}
		else {
			System.out.println("SUCCESS!!! K_{" + to.first + "," + to.second + "} not drawable.");
		}
	}




	/**
	 * Creates a planar K_{2,2}
	 */
	public static void createK22(String folderName) {
		System.out.println("Creating K_{2,2} ...");
		
		SafeLoad.createFolder(folderName);
		
		Embedding k22 = new Embedding(embeddingId);
		embeddingId++;
		
		Vertex v1 = k22.createVertex(Embedding.SET1_LETTER + "1");
		Vertex v2 = k22.createVertex(Embedding.SET1_LETTER + "2");
		Vertex u1 = k22.createVertex(Embedding.SET2_LETTER + "1");
		Vertex u2 = k22.createVertex(Embedding.SET2_LETTER + "2");
		
		Face f1   = k22.createFace();
		
		k22.insertEdge(v1.getId(), u1.getId(), f1.getId(), -1);
		k22.insertEdge(v1.getId(), u2.getId(), f1.getId(), -1);
		k22.insertEdge(v2.getId(), u1.getId(), f1.getId(), -1);
		k22.insertEdge(v2.getId(), u2.getId(), f1.getId(), -1);
		
		
		Embedding unplanark22 = new Embedding(embeddingId);
		embeddingId++;
		
		Vertex v21 = unplanark22.createVertex(Embedding.SET1_LETTER + "1");
		Vertex v22 = unplanark22.createVertex(Embedding.SET1_LETTER + "2");
		Vertex u21 = unplanark22.createVertex(Embedding.SET2_LETTER + "1");
		Vertex u22 = unplanark22.createVertex(Embedding.SET2_LETTER + "2");
		
		Face f21   = unplanark22.createFace();
		
		unplanark22.insertEdge(v21.getId(), u21.getId(), f21.getId(), -1);
		Edge edge = unplanark22.insertEdge(v21.getId(), u22.getId(), f21.getId(), -1);
		unplanark22.insertEdge(v22.getId(), u22.getId(), f21.getId(), -1);
		
		Vertex crossing = unplanark22.subdivideEdge(edge.getId(), true);
		Edge v2ToCr = unplanark22.insertEdge(v22.getId(), crossing.getId(), f21.getId(), -1);
		
		Face faceU1 = f21;
		for (Face f : unplanark22.getFaces().values()) {
			if (unplanark22.hasFaceVertex(f.getId(), u21.getId())) {
				faceU1 = f;
			}
		}
		unplanark22.insertEdge(crossing.getId(), u21.getId(), faceU1.getId(), v2ToCr.getId());
		
		k22.setNextEmbedding(unplanark22.getId());
		k22.setNextTopoEmbedding(unplanark22.getId());
		k22.setPrevEmbedding(unplanark22.getId());
		k22.setPrevTopoEmbedding(unplanark22.getId());
		
		unplanark22.setNextEmbedding(k22.getId());
		unplanark22.setNextTopoEmbedding(k22.getId());
		unplanark22.setPrevEmbedding(k22.getId());
		unplanark22.setPrevTopoEmbedding(k22.getId());
		
		SafeLoad.safeEmbedding(k22, folderName);
		SafeLoad.safeEmbedding(unplanark22, folderName);
		SafeLoad.safeMetadata(folderName, new Metadata(2, 2, k22.getId()));
	}
	
	
	/**
	 * Inserts a new vertex into the given embeddings.
	 * @param embeddings	list of embeddings
	 * @param newNodeName	name of the new vertex
	 * @param useSet1		if true, the new vertex is connected to all the vertices in the first bipartite set
	 * @return
	 */
	private List<Embedding> insertNewNode(
			Embedding embedding, String newNodeName, boolean useSet1) {

		nodeInserter.init(
				embedding,
				useSet1 ? getVertexSet1(embedding) : getVertexSet2(embedding),
				embeddingId,
				newNodeName);
		
		embeddingId = nodeInserter.insertAll();
		embedding.setInsertionPossible(nodeInserter.isInsertionPossible());

		return nodeInserter.getCreatedEmbeddings();
	}
	
	/**
	 * Calculate the first bipartite set of the given embedding <code>emb</code>
	 * @param emb	the embedding
	 * @return		list of vertices in the first bipartite set
	 */
	private List<Integer> getVertexSet1(Embedding emb) {
		List<Integer> set1 = new LinkedList<Integer>();
		int i=1;		
		while (emb.hasVertexWithName(Embedding.SET1_LETTER + i)) {
			set1.add(emb.getVertexIdByName(Embedding.SET1_LETTER + i));
			i++;
		}
		return set1;
	}

	/**
	 * Calculate the second bipartite set of the given embedding <code>emb</code>
	 * @param emb	the embedding
	 * @return		list of vertices in the second bipartite set
	 */
	private List<Integer> getVertexSet2(Embedding emb) {
		List<Integer> set2 = new LinkedList<Integer>();
		int i=1;
		while (emb.hasVertexWithName(Embedding.SET2_LETTER + i)) {
			set2.add(emb.getVertexIdByName(Embedding.SET2_LETTER + i));
			i++;
		}
		return set2;
	}




	public String getToFolder() {
		return toFolder;
	}

	public String getFromFolder() {
		return fromFolder;
	}
	
}
