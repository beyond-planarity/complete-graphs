package io.safeLoad;

import data.Constant;
import data.Embedding;
import data.Metadata;

public class Migration {
	
	private static final String folderName = "3planar_4_9";
	
	public static void run() {
		if (SafeLoad.isEmpty(folderName)) {
			return;
		}
		
		Metadata metadata = SafeLoad.loadMetadata(folderName);
		
		System.out.println("Migrate " + metadata.getNumberEmbeddingsTotal() + " embeddings.");
		int counter = 1;
		
		int startEmbId   = metadata.getIdStartEmbedding();
		int currentEmbId = startEmbId;
		
		String strFront = "Migrated embeddings: ";
		String strBack  = "/" + metadata.getNumberEmbeddingsTotal();
		
		do {
			Embedding emb = SafeLoadOld.loadEmbedding(folderName, currentEmbId);
			SafeLoad.safeEmbedding(emb, folderName);
			
			currentEmbId = emb.getNextEmbedding();
			counter++;
			
			if (counter % Constant.MIGRATION_PROGRESS == 0) {
				System.out.println(strFront + counter + strBack);
			}
			
		} while (currentEmbId != startEmbId);
		
		System.out.println();
	}

}
