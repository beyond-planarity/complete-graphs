package graphbuilderExt;

import java.util.LinkedList;
import java.util.List;

import data.Constant;
import data.Embedding;
import data.Face;
import data.Metadata;
import data.Vertex;
import io.safeLoad.SafeLoad;
import nodeInserter.NodeInserter;

/**
 * Class for creating all complete graphs of a certain graph class.
 * @author tommy
 *
 */
public class CompleteGraphCreator {
	
	private static int    embeddingId = 0;

	private NodeInserter nodeInserter;
	private String       toFolder;
	private String       fromFolder;


	/**
	 * Creates a new <code>CompleteGraphCreator</code>.
	 * @param nodeInserter	node inserter to use
	 * @param fromFolder	source folder
	 * @param toFolder		target folder
	 */
	public CompleteGraphCreator(NodeInserter nodeInserter, String fromFolder, String toFolder) {
		this.nodeInserter = nodeInserter;
		this.fromFolder   = fromFolder;
		this.toFolder     = toFolder;
	}

	/**
	 * Creates the unique drawings of a K_3.
	 * @param folderName folder to safe the embedding
	 */
	public static void createPlanarStartEmbeddings(String folderName) {
		System.out.println("Creating K_{3} ...");
		
		Embedding k3 = new Embedding(embeddingId);
		embeddingId++;
		
		Vertex v1 = k3.createVertex(Embedding.SET1_LETTER + "1");
		Vertex v2 = k3.createVertex(Embedding.SET1_LETTER + "2");
		Vertex v3 = k3.createVertex(Embedding.SET1_LETTER + "3");
		
		Face f1   = k3.createFace();
		
		k3.insertEdge(v1.getId(), v2.getId(), f1.getId(), -1);
		k3.insertEdge(v1.getId(), v3.getId(), f1.getId(), -1);
		k3.insertEdge(v2.getId(), v3.getId(), f1.getId(), -1);
		
		SafeLoad.createFolder(folderName);
		SafeLoad.safeEmbedding(k3, folderName);
		SafeLoad.safeMetadata(folderName, new Metadata(1, 1, k3.getId()));
		
	}
	
	
	
	/**
	 * Creates all topologically different drawings of a graph K_{x},
	 * starting from a planar K_{3} and adding one vertex in each step.
	 * @param lastX	final K_{x} to create
	 * @return true, if and only if there were found new embeddings
	 */
	public boolean createKx(int lastX) {
		System.out.println();
		System.out.println();
		System.out.println("Creating K_{" + lastX + "} ...");
		
		if (SafeLoad.isEmpty(fromFolder)) {
			return false;
		}
		SafeLoad.createFolder(toFolder);
		
		Metadata fromMeta     = SafeLoad.loadMetadata(fromFolder);
		int      firstEmbId   = fromMeta.getIdStartEmbedding();
		int      currentEmbId = firstEmbId;
		
		Embedding lastEmb        = null;
		int       totalNumber    = 0;
		int       newFirstEmbId  = -1;
		String    newNodeName    = Embedding.SET1_LETTER + lastX;
		
		String strFront = "Inserted new node " + newNodeName + " into ";
		String strBack  = "/" + fromMeta.getNumberEmbeddingsTopoDifferent() + " embeddings. Total new: ";
		int    counter  = 0;
		
		do {
			Embedding       baseEmb       = SafeLoad.loadEmbedding(fromFolder, currentEmbId);
			List<Embedding> newEmbeddings = insertNewNode(baseEmb, newNodeName);
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


		System.out.println("Creation of K_{" + lastX + "} finished. Found " + totalNumber + " embeddings.");
		
		if (totalNumber > 0) {
			Embedding emb = SafeLoad.loadEmbedding(toFolder, newFirstEmbId);
			lastEmb.setNextEmbedding(emb.getId());
			emb.setPrevEmbedding(lastEmb.getId());
			SafeLoad.safeEmbedding(emb, toFolder);
			SafeLoad.safeEmbedding(lastEmb, toFolder);
			
			SafeLoad.safeMetadata(toFolder, new Metadata(totalNumber, totalNumber, newFirstEmbId));
			
			System.out.println("K_{" + lastX + "} is drawable.");
			return true;
		}
		else {
			System.out.println("SUCCESS!!! K_{" + lastX + "} not drawable.");
			return false;
		}
	}
	

	
	/**
	 * Inserts a new vertex into the given embeddings.
	 * @param embeddings	list of embeddings
	 * @param newNodeName	name of the new vertex
	 * @return
	 */
	private List<Embedding> insertNewNode(Embedding embedding, String newNodeName) {

		nodeInserter.init(
				embedding,
				getVertexSet(embedding),
				embeddingId,
				newNodeName);
		
		embeddingId = nodeInserter.insertAll();
		embedding.setInsertionPossible(nodeInserter.isInsertionPossible());

		return nodeInserter.getCreatedEmbeddings();
	}
	
	

	
	/**
	 * Calculate the real vertices of the given embedding <code>emb</code>
	 * @param emb	the embedding
	 * @return		list of real vertices
	 */
	public List<Integer> getVertexSet(Embedding emb) {
		List<Integer> set = new LinkedList<Integer>();
		int i=1;
		while (emb.hasVertexWithName(Embedding.SET1_LETTER + i)) {
			set.add(emb.getVertexIdByName(Embedding.SET1_LETTER + i));
			i++;
		}
		return set;
	}


	/**
	 * Returns the folder that was newly created.
	 * @return folder name
	 */
	public String getToFolder() {
		return toFolder;
	}


	/**
	 * Returns the folder with the source embeddings.
	 * @return folder name
	 */
	public String getFromFolder() {
		return fromFolder;
	}
}
