package statistics;

import data.Constant;
import data.Embedding;
import data.Metadata;
import data.Statistics;
import io.safeLoad.SafeLoad;

/**
 * Class for calculating the minimum and maximum number of crossings for a certain graph.
 * @author tommy
 *
 */
public class CrossingCalculator {
	
	private static final String folderName = "opt2planar_7";
	
	public static void main(String[] args) {
		run();
	}
	
	public static void run() {
		if (SafeLoad.isEmpty(folderName)) {
			return;
		}

		
		System.out.println("START CALCULATION");
		
		Metadata metadata = SafeLoad.loadMetadata(folderName);
		
		int counter = 1;
		
		int startEmbId   = metadata.getIdStartEmbedding();
		int currentEmbId = startEmbId;
		
		String strFront = "Finished embeddings: ";
		String strBack  = "/" + metadata.getNumberEmbeddingsTotal();
		
		int minNumberCrossings = Integer.MAX_VALUE;
		int maxNumberCrossings = Integer.MIN_VALUE;
		
		do {
			Embedding emb = SafeLoad.loadEmbedding(folderName, currentEmbId);
			
			//----------------------------------------------
			int crNo = emb.getCrossingNumber();
			minNumberCrossings = Math.min(minNumberCrossings, crNo);
			maxNumberCrossings = Math.max(maxNumberCrossings, crNo);
			//----------------------------------------------
			
			currentEmbId = emb.getNextEmbedding();
			counter++;
			
			if (counter % Constant.COUNTING_PROGRESS == 0) {
				System.out.println(strFront + counter + strBack);
			}
			
		} while (currentEmbId != startEmbId);
		
		// safe to statistics
		Statistics stats = SafeLoad.loadStatistics(folderName);
		stats.maxCrossings = maxNumberCrossings;
		stats.minCrossings = minNumberCrossings;
		SafeLoad.safeStatistics(folderName, stats);
		
		System.out.println("FINISHED CALCULATION");
	}

}
