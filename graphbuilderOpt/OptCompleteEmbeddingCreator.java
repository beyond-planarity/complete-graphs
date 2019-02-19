package graphbuilderOpt;

import java.util.LinkedList;
import java.util.List;

import data.Constant;
import data.Embedding;
import data.Face;
import data.Metadata;
import data.Statistics;
import data.TempData;
import data.Vertex;
import embeddingComparator.EmbeddingComparator;
import io.safeLoad.SafeLoad;
import nodeInserter.NodeInserter;

public class OptCompleteEmbeddingCreator extends OptEmbeddingCreator {
	
	private int startGraphIndex;
	private int endGraphIndex;
	
	public OptCompleteEmbeddingCreator(NodeInserter nodeInserter, String graphClassName,
			int startGraphIndex, int endGraphIndex) {
		super(nodeInserter, graphClassName, "_3");
		this.startGraphIndex = startGraphIndex;
		this.endGraphIndex   = endGraphIndex;
	}
	

	
	protected void createBaseEmbeddings(boolean useGaps) {
		System.out.println("Creating Base Embeddings ...");
		
		Embedding k3 = new Embedding(getNextId());
		
		Vertex v1 = k3.createVertex(Embedding.SET1_LETTER + "1");
		Vertex v2 = k3.createVertex(Embedding.SET1_LETTER + "2");
		Vertex v3 = k3.createVertex(Embedding.SET1_LETTER + "3");
		
		Face f1   = k3.createFace();
		
		k3.insertEdge(v1.getId(), v2.getId(), f1.getId(), -1);
		k3.insertEdge(v1.getId(), v3.getId(), f1.getId(), -1);
		k3.insertEdge(v2.getId(), v3.getId(), f1.getId(), -1);
		
		// set references
		k3.setDrawingNr(k3.getId());
		k3.setReferenceEmbeddingId(k3.getId());
		k3.setNextEmbedding(k3.getId());
		k3.setPrevEmbedding(k3.getId());
		k3.setNextTopoEmbedding(k3.getId());
		k3.setPrevTopoEmbedding(k3.getId());

		// safe the embedding
		SafeLoad.createFolder(baseFolderName);
		SafeLoad.safeEmbedding(k3, baseFolderName);
		Metadata meta = new Metadata(1, 1, k3.getId());
		meta.setNumberEmbeddingsTotalFound(1);
		SafeLoad.safeMetadata(baseFolderName, meta);
	}
	
	
	
	/**
	 * Creates all topologically different drawings of a graph K_{targetX},
	 * starting from embeddings of K_{targetX-1}.
	 * It <code>stopAfter</code> embeddings were created, it is stopped.
	 * @param targetX	final K_{x} to create
	 * @param stopAfter 
	 */
	private void createKx(int targetX, int stopAfter) {
		
		int fromX = targetX - 1;
		
		System.out.println();
		System.out.println();
		System.out.println("Creating K_{" + targetX + "} ...");
		
		fromFolder = getFolderName(fromX);
		toFolder   = getFolderName(targetX);
		
		if (!SafeLoad.hasFolder(fromFolder) || SafeLoad.isEmpty(fromFolder)) {
			return;
		}
		SafeLoad.createFolder(toFolder);

		// load metadata
		Metadata  fromMeta         = SafeLoad.loadMetadata(fromFolder);
		
		Embedding lastEmb          = null;
		int       totalNumber      = 0;
		int       totalNumberFound = 0;
		int       toFirstEmbId     = -1;
		String    newNodeName      = Embedding.SET1_LETTER + targetX;
		int       counter          = 1;
		
		createKx(newNodeName, fromMeta.getNumberEmbeddingsTotal(), lastEmb,
				toFirstEmbId, fromMeta.getIdStartEmbedding(),
				totalNumber, totalNumberFound,
				fromMeta.getIdStartEmbedding(), counter, targetX, stopAfter);
	}
	

	
	/**
	 * Continues to creates all topologically different drawings of a graph
	 * K_{targetX}, starting from embeddings of K_{targetX-1}.
	 * It <code>stopAfter</code> embeddings were created, it is stopped.
	 * @param targetX	final K_{x} to create
	 * @param stopAfter 
	 */
	private void continueCreateKx(int targetX, int stopAfter) {
		
		int fromX = targetX - 1;
		
		System.out.println();
		System.out.println();
		System.out.println("Creating K_{" + targetX + "} ...");
		
		fromFolder = getFolderName(fromX);
		toFolder   = getFolderName(targetX);
		
		if (!SafeLoad.hasFolder(fromFolder) || SafeLoad.isEmpty(fromFolder)) {
			return;
		}
		SafeLoad.createFolder(toFolder);
		
		TempData temp = SafeLoad.loadTempData(toFolder);
		SafeLoad.deleteTempData(toFolder);
		
		// load metadata
		Metadata fromMeta = SafeLoad.loadMetadata(fromFolder);
		
		Embedding lastEmb = null;
		if (temp.lastEmbId >= 0) {
			lastEmb = SafeLoad.loadEmbedding(toFolder, temp.lastEmbId);
		}
		int totalNumber      = temp.totalNumber;
		int totalNumberFound = temp.totalNumberFound;
		int toFirstEmbId     = temp.toFirstEmbId;
		int currentEmbId     = temp.currentEmbId;
		embeddingId          = totalNumber + 1;

		// determine name of new node
		String newNodeName = temp.newNodeName;
		int    counter     = temp.counter;
		
		createKx(newNodeName, fromMeta.getNumberEmbeddingsTotal(), lastEmb,
				toFirstEmbId, fromMeta.getIdStartEmbedding(),
				totalNumber, totalNumberFound,
				currentEmbId, counter, targetX, stopAfter);
	}


	/**
	 * Creates all topologically different drawings of a graph K_{x},
	 * starting from a planar K_{3} and adding one vertex in each step.
	 * @param lastX	final K_{x} to create
	 */
	private void createKx(String newNodeName, int fromEmbeddings,
			Embedding lastEmb, int toFirstEmbId, int fromFirstEmbId,
			int totalNumber, int totalNumberFound,
			int currentEmbId, int counter, int targetX, int stopAfter) {

		String strFront   = "Inserted new node " + newNodeName + " into ";
		String strBack    = "/" + fromEmbeddings + " embeddings - found new: ";
		String strGenProg = "/" + fromEmbeddings;
		
		do {
			int step = 0;
			
			Embedding baseEmb;
			List<Embedding> newEmbeddings = new LinkedList<Embedding>();
			
			do {
				// load embedding in which the new node will be inserted
				baseEmb = SafeLoad.loadEmbedding(fromFolder, currentEmbId);

				// insert the new node
				try {
					newEmbeddings.addAll(insertNewNode(baseEmb, newNodeName, true, false));
				} catch (Exception e) {
					break;
				}

				if ( (counter + step) % Constant.INSERTING_PROGRESS == 0) {
					System.out.println(strFront + (counter+step) + strBack + newEmbeddings.size());
				}
				
				// save the changes in the base embedding
				SafeLoad.safeEmbedding(baseEmb, fromFolder);

				// get next base embedding id from the folder
				currentEmbId = baseEmb.getNextTopoEmbedding();
				step++;

			} while (currentEmbId != fromFirstEmbId && counter + step <= stopAfter &&
					newEmbeddings.size() < Constant.EMBEDDING_RAM_LIST_SIZE);
			
					
			totalNumberFound += newEmbeddings.size(); 
					
					
			// remove duplicates in the currently found new embeddings
			//System.out.println("\nRemoving duplicates in the new embeddings.");
			newEmbeddings = EmbeddingComparator.compareEmbeddingsRAM(newEmbeddings, counter + "-" + (counter + step - 1) + strGenProg);
			//System.out.println("Internal removing done. There are " + newEmbeddings.size() + " embeddings left.");
			
			// remove duplicates when comparing the new embeddings with the already saved embeddings
			//System.out.println("\nRemoving duplicates comparing saved embeddings.");
			if (lastEmb != null) {
				newEmbeddings = EmbeddingComparator.compareEmbeddingsHD(newEmbeddings, toFolder, lastEmb.getId(), counter + "-" + (counter + step - 1) + strGenProg);
			}
			//System.out.println("External removing done. There are " + newEmbeddings.size() + " embeddings left.");
			
			
			// update the links in the embeddings
			if (newEmbeddings.size() > 0) {
				//System.out.println("\nUpdating links ...");
				
				updateLinks(newEmbeddings, totalNumber, lastEmb);
				if (lastEmb != null) {
					SafeLoad.safeEmbedding(lastEmb, toFolder);
				}
				//System.out.println("... links are updated!");

				lastEmb = newEmbeddings.get(newEmbeddings.size()-1);
				lastEmb.setNextEmbedding(0);
				lastEmb.setNextTopoEmbedding(0);

				//System.out.println("Saving the embeddings ...");
				for (Embedding emb : newEmbeddings) {
					SafeLoad.safeEmbedding(emb, toFolder);
				}
				//System.out.println("... embeddings are saved!");

				// update the number of found embeddings
				totalNumber += newEmbeddings.size();
				embeddingId = totalNumber + 1;

				if (toFirstEmbId < 0) {
					toFirstEmbId = newEmbeddings.get(0).getId();
				}
				
				// save metadata
				SafeLoad.safeMetadata(toFolder, new Metadata(totalNumber, totalNumber, toFirstEmbId));
			}
			
			System.out.println(strFront + counter + "-" + (counter + step - 1) + strBack + totalNumber);
			
			counter += step;
			
			
			if (counter > stopAfter && currentEmbId != fromFirstEmbId) {
				// save all the stuff
				TempData temp         = new TempData();
				temp.connectToSet1    = true;
				temp.counter          = counter;
				temp.currentEmbId     = currentEmbId;
				temp.lastEmbId        = lastEmb == null ? -99 : lastEmb.getId();
				temp.toFirstEmbId     = toFirstEmbId;
				temp.newNodeName      = newNodeName;
				temp.totalNumber      = totalNumber;
				temp.totalNumberFound = totalNumberFound;
				
				SafeLoad.safeTempData(toFolder, temp);
				
				return;
			}
			
		} while (currentEmbId != fromFirstEmbId);


		System.out.println("Creation of K_{" + targetX + "} finished. Found " + totalNumber + " embeddings.");

		// fix the final links
		if (totalNumber > 0) {
			Embedding emb = SafeLoad.loadEmbedding(toFolder, toFirstEmbId);
			if (totalNumber == 1) {
				emb.setNextEmbedding(emb.getId());
				emb.setNextTopoEmbedding(emb.getId());
				emb.setPrevEmbedding(emb.getId());
				emb.setPrevTopoEmbedding(emb.getId());
				SafeLoad.safeEmbedding(emb, toFolder);
			}
			else {
				lastEmb.setNextEmbedding(emb.getId());
				lastEmb.setNextTopoEmbedding(emb.getId());
				emb.setPrevEmbedding(lastEmb.getId());
				emb.setPrevTopoEmbedding(lastEmb.getId());
				SafeLoad.safeEmbedding(emb, toFolder);
				SafeLoad.safeEmbedding(lastEmb, toFolder);
			}
			
			Metadata meta = new Metadata(totalNumber, totalNumber, toFirstEmbId);
			meta.setNumberEmbeddingsTotalFound(totalNumberFound);
			SafeLoad.safeMetadata(toFolder, meta);
			

			// statistic
			stats.totalCreated  = totalNumberFound;
			stats.nonIsomorphic = totalNumber;
			
			System.out.println("### K_{" + targetX + "} is drawable. ###");
		}
		else {
			System.out.println("### SUCCESS!!! K_{" + targetX + "} not drawable. ###");
		}

		System.out.println("--------------------------------------------------------------------------------------");
		System.out.println("--------------------------------------------------------------------------------------");
	}


	public void calculateEmbeddings(int stopAfter, boolean tempAvailable) {
		
		if (startGraphIndex < 4 || startGraphIndex > endGraphIndex) {
			System.out.println("No graph indices found.");
			return;
		}
		
		String firstFolder = getFolderName(startGraphIndex-1);
		if (SafeLoad.isEmpty(firstFolder)) {
			System.out.println("Folder not found");
			return;
		}
		
		for (int i=startGraphIndex; i<=endGraphIndex; i++) {
			
			int stopAfterTemp = (i == endGraphIndex) ? stopAfter : Integer.MAX_VALUE;
			
			if (tempAvailable && i==startGraphIndex) {
				continueCreateKx(i, stopAfterTemp);
			}
			else {
				stats = new Statistics();
				statsFile = getFolderName(i);
				long time = System.currentTimeMillis();
				
				createKx(i, stopAfterTemp);
				
				stats.time = System.currentTimeMillis() - time;
				SafeLoad.safeStatistics(statsFile, stats);
				
				if (stats.nonIsomorphic == 0) {
					break;
				}
			}
		}
	}
}
