package statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import data.Constant;
import data.Embedding;
import data.Metadata;
import io.safeLoad.SafeLoad;

/**
 * Class for migrating saved files.
 * @author tommy
 *
 */
public class DataToCSV {
	
	private static final String folderName = "fanplanar_4_5";
	private static final String csvFileName = "CSV" + folderName + ".csv";
	
	public static void main(String[] args) {
		try {
			run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void run() throws IOException {
		if (SafeLoad.isEmpty(folderName)) {
			return;
		}

		
		System.out.println("START CREATING CSV");
		
		Metadata metadata = SafeLoad.loadMetadata(folderName);
		
		int counter = 1;
		
		int startEmbId   = metadata.getIdStartEmbedding();
		int currentEmbId = startEmbId;
		
		String strFront = "Finished embeddings: ";
		String strBack  = "/" + metadata.getNumberEmbeddingsTotal();
		
		String line;
		FileWriter writer = new FileWriter(csvFileName);
		
		Map<Integer, Integer> embToDrawingNo = new HashMap<Integer, Integer>();
		
		do {
			Embedding emb = SafeLoad.loadEmbedding(folderName, currentEmbId);
			
			//----------------------------------------------
			embToDrawingNo.put(emb.getId(), emb.getDrawingNr());
			
			
			line = "" + emb.getId() + ",";
			line += emb.getDrawingNr() + ",";
			line += emb.getReferenceEmbeddingId() + ",";
			line += embToDrawingNo.get(emb.getReferenceEmbeddingId()) + ",";
			line += emb.getSourceEmbeddingId() + ",";
			line += emb.isInsertionPossible() + "\n";
			
			writer.append(line);
			//----------------------------------------------
			
			currentEmbId = emb.getNextEmbedding();
			counter++;
			
			if (counter % Constant.COUNTING_PROGRESS == 0) {
				System.out.println(strFront + counter + strBack);
			}
			
		} while (currentEmbId != startEmbId);
		
		writer.flush();
		writer.close();
		
		System.out.println("FINISHED CALCULATION");
	}

}
