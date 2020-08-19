package mainOpt;

import java.util.LinkedList;
import java.util.List;

import data.Constant;
import data.Embedding;
import data.Metadata;
import drawing.DrawingPositioner;
import io.safeLoad.SafeLoad;
import nodeInserter.NodeInserter;
import nodeInserter.NodeInserterFanCrossingFree;
import nodeInserter.NodeInserterFanPlanar;
import nodeInserter.NodeInserterGapPlanar;
import nodeInserter.NodeInserterKPlanar;
import nodeInserter.NodeInserterQuasiPlanar;

/**
 * Class for testing how many edges can be inserted into a complete graph.
 * @author tommy
 *
 */
public class ExactBoundCheckerComplete {
	
	private static final String       sourceFolderName = "opt5planar_9";            // folder with the drawings for the complete graph
	private static final String       targetFolderName = "edges5planar8";         // folder to save possible drawings
	private static final NodeInserter nodeInserter     = new NodeInserterKPlanar(5); // inserter for the graph class

	private static final int    noOfEdges   = 8;   // number of edges that should be tried to insert
	private static final String newNodeName = "v"; // name of the new node that will be inserted
	
	
	
	public static void main(String[] args) {
		run();
	}
	
	
	public static void run() {
		if (SafeLoad.isEmpty(sourceFolderName)) {
			return;
		}

		
		
		List<Embedding> newEmbs = new LinkedList<Embedding>();
		
		
		
		System.out.println("START CALCULATION");
		
		Metadata metadata = SafeLoad.loadMetadata(sourceFolderName);
		
		int counter = 1;
		
		int startEmbId   = metadata.getIdStartEmbedding();
		int currentEmbId = startEmbId;
		
		String strFront = "Finished embeddings: ";
		String strBack  = "/" + metadata.getNumberEmbeddingsTotal();
		
				
		do {
			Embedding emb = SafeLoad.loadEmbedding(sourceFolderName, currentEmbId);
			
			//----------------------------------------------
			nodeInserter.init(emb, emb.getVertexSet1(), 1, newNodeName);
			nodeInserter.insertEdges(noOfEdges);
			newEmbs.addAll(nodeInserter.getCreatedEmbeddings());
			//----------------------------------------------
			
			currentEmbId = emb.getNextEmbedding();
			counter++;
			
			if (counter % Constant.COUNTING_PROGRESS == 0) {
				System.out.println(strFront + counter + strBack);
			}
			
		} while (currentEmbId != startEmbId && newEmbs.isEmpty());
		
		
		// safe to statistics
		if (!newEmbs.isEmpty()) {

			Embedding emb = newEmbs.get(0);
			emb.setId(1);
			emb.setDrawingNr(1);
			emb.setNextEmbedding(1);
			emb.setPrevEmbedding(1);
			emb.setNextTopoEmbedding(1);
			emb.setPrevTopoEmbedding(1);
			
			SafeLoad.createFolder(targetFolderName);
			SafeLoad.safeEmbedding(emb, targetFolderName);
			SafeLoad.safeMetadata(targetFolderName, new Metadata(1, 1, emb.getId()));
			DrawingPositioner dp = new DrawingPositioner(targetFolderName);
			dp.calcPosAndColorSmallSize(newNodeName);
			
			SafeLoad.exportAll(targetFolderName);
			
			System.out.println();
			
			System.out.println("EMBEDDING FOUND WITH " + emb.getNumberOfRealEdges() + " EDGES");
		}
		else {
			System.out.println("NO EMBEDDING FOUND");
		}
	}

}
