package graphbuilderOpt;

import java.util.LinkedList;
import java.util.List;

import data.Constant;
import data.Edge;
import data.Embedding;
import data.Face;
import data.Metadata;
import data.Statistics;
import data.TempData;
import data.Tuple;
import data.Vertex;
import embeddingComparator.EmbeddingComparator;
import io.safeLoad.SafeLoad;
import nodeInserter.NodeInserter;

public class OptBipartiteEmbeddingCreator extends OptEmbeddingCreator {
	
	private Tuple[] graphIndices;
	
	public OptBipartiteEmbeddingCreator(NodeInserter nodeInserter, String graphClassName,
			Tuple[] graphIndices) {
		super(nodeInserter, graphClassName, "_2_2");
		this.graphIndices = graphIndices;
	}
	

	
	protected void createBaseEmbeddings(boolean useGaps) {
		System.out.println("Creating Base Embeddings ...");
		
		Embedding planark22 = new Embedding(getNextId());
		
		Vertex v1 = planark22.createVertex(Embedding.SET1_LETTER + "1");
		Vertex v2 = planark22.createVertex(Embedding.SET1_LETTER + "2");
		Vertex u1 = planark22.createVertex(Embedding.SET2_LETTER + "1");
		Vertex u2 = planark22.createVertex(Embedding.SET2_LETTER + "2");
		
		Face f1   = planark22.createFace();
		
		planark22.insertEdge(v1.getId(), u1.getId(), f1.getId(), -1);
		planark22.insertEdge(v1.getId(), u2.getId(), f1.getId(), -1);
		planark22.insertEdge(v2.getId(), u1.getId(), f1.getId(), -1);
		planark22.insertEdge(v2.getId(), u2.getId(), f1.getId(), -1);

	
		Embedding unplanark22 = new Embedding(getNextId());
		
		Vertex v21 = unplanark22.createVertex(Embedding.SET1_LETTER + "1");
		Vertex v22 = unplanark22.createVertex(Embedding.SET1_LETTER + "2");
		Vertex u21 = unplanark22.createVertex(Embedding.SET2_LETTER + "1");
		Vertex u22 = unplanark22.createVertex(Embedding.SET2_LETTER + "2");
		
		Face f21   = unplanark22.createFace();
		
		unplanark22.insertEdge(v21.getId(), u21.getId(), f21.getId(), -1);
		Edge edge = unplanark22.insertEdge(v21.getId(), u22.getId(), f21.getId(), -1);
		unplanark22.insertEdge(v22.getId(), u22.getId(), f21.getId(), -1);
		
		Vertex crossing = unplanark22.subdivideEdge(edge.getId(), true);
		Edge crOutE = crossing.getOutEdge();
		Edge v2ToCr = unplanark22.insertEdge(v22.getId(), crossing.getId(), f21.getId(), -1);
		
		Face faceU1 = f21;
		for (Face f : unplanark22.getFaces().values()) {
			if (unplanark22.hasFaceVertex(f.getId(), u21.getId())) {
				faceU1 = f;
			}
		}
		unplanark22.insertEdge(crossing.getId(), u21.getId(), faceU1.getId(), v2ToCr.getId());
		
		if (useGaps) {
			unplanark22.setGapCrossingToEdge(crossing.getId(), crOutE.getId());
		}
		
		// set references
		planark22.setDrawingNr(planark22.getId());
		planark22.setReferenceEmbeddingId(planark22.getId());
		planark22.setNextEmbedding(unplanark22.getId());
		planark22.setPrevEmbedding(unplanark22.getId());
		planark22.setNextTopoEmbedding(unplanark22.getId());
		planark22.setPrevTopoEmbedding(unplanark22.getId());
		
		unplanark22.setDrawingNr(unplanark22.getId());
		unplanark22.setReferenceEmbeddingId(unplanark22.getId());
		unplanark22.setNextEmbedding(planark22.getId());
		unplanark22.setPrevEmbedding(planark22.getId());
		unplanark22.setNextTopoEmbedding(planark22.getId());
		unplanark22.setPrevTopoEmbedding(planark22.getId());
		
		// safe the embeddings
		SafeLoad.createFolder(baseFolderName);
		SafeLoad.safeEmbedding(planark22, baseFolderName);
		SafeLoad.safeEmbedding(unplanark22, baseFolderName);
		Metadata meta = new Metadata(2, 2, planark22.getId());
		meta.setNumberEmbeddingsTotalFound(2);
		SafeLoad.safeMetadata(baseFolderName, meta);
	}
	
	

	
	/**
	 * Creates all topologically different drawings of a bipartite graph
	 * K_{to.first,to.second}, starting from K_{from.first,from.second}.
	 * It <code>stopAfter</code> embeddings were created, it is stopped.
	 * @param from
	 * @param to
	 * @param stopAfter
	 */
	private void createKxy(Tuple from, Tuple to, int stopAfter) {
		
		System.out.println();
		System.out.println();
		System.out.println("Creating K_{" + to.first + "," + to.second + "} ...");
		
		fromFolder = getFolderName(from.first, from.second);
		toFolder   = getFolderName(to.first, to.second);
			
		if (!SafeLoad.hasFolder(fromFolder) || SafeLoad.isEmpty(fromFolder)) {
			return;
		}
		SafeLoad.createFolder(toFolder);
		
		// load metadata
		Metadata fromMeta = SafeLoad.loadMetadata(fromFolder);

		Embedding lastEmb          = null;
		int       totalNumber      = 0;
		int       totalNumberFound = 0;
		int       toFirstEmbId     = -1;
		
		boolean vertexSetSwitchNeeded = (from.first == from.second);
		
		// determine name of new node
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
		int counter = 1;
		
		createKxy(newNodeName, fromMeta.getNumberEmbeddingsTotal(), connectToSet1, lastEmb,
				toFirstEmbId, fromMeta.getIdStartEmbedding(),
				totalNumber, totalNumberFound, vertexSetSwitchNeeded,
				fromMeta.getIdStartEmbedding(), counter, to, stopAfter);
	}


	


	
	/**
	 * Continues to creates all topologically different drawings of a bipartite graph
	 * K_{to.first,to.second}, starting from K_{from.first,from.second}.
	 * It <code>stopAfter</code> embeddings were created, it is stopped.
	 * @param from
	 * @param to
	 * @param stopAfter
	 */
	private void continueCreateKxy(Tuple from, Tuple to, int stopAfter) {
		
		System.out.println();
		System.out.println();
		System.out.println("Creating K_{" + to.first + "," + to.second + "} ...");
		
		fromFolder = getFolderName(from.first, from.second);
		toFolder   = getFolderName(to.first, to.second);
		
		if (!SafeLoad.hasFolder(fromFolder) || SafeLoad.isEmpty(fromFolder)) {
			return;
		}
		SafeLoad.createFolder(toFolder);
		
		boolean vertexSetSwitchNeeded = (from.first == from.second);
		
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
		String  newNodeName   = temp.newNodeName;
		boolean connectToSet1 = temp.connectToSet1;
		int     counter       = temp.counter;
		
		createKxy(newNodeName, fromMeta.getNumberEmbeddingsTotal(), connectToSet1, lastEmb,
				toFirstEmbId, fromMeta.getIdStartEmbedding(),
				totalNumber, totalNumberFound, vertexSetSwitchNeeded,
				currentEmbId, counter, to, stopAfter);
	}
	
	
	/**
	 * Creates all topologically different drawings of a bipartite graph K_{x,y},
	 * starting from a planar K_{2,2} step-by-step.
	 * Thereby consecutive indices must be such that the value a+b increases by 1.
	 * @param newNodeName
	 * @param fromEmbeddings
	 * @param connectToSet1
	 * @param lastEmb
	 * @param toFirstEmbId
	 * @param fromFirstEmbId
	 * @param totalNumber
	 * @param totalNumberFound
	 * @param vertexSetSwitchNeeded
	 * @param currentEmbId
	 * @param counter
	 * @param to
	 * @param stopAfter
	 */
	private void createKxy(String newNodeName, int fromEmbeddings, boolean connectToSet1,
			Embedding lastEmb, int toFirstEmbId, int fromFirstEmbId,
			int totalNumber, int totalNumberFound, boolean vertexSetSwitchNeeded,
			int currentEmbId, int counter, Tuple to, int stopAfter) {

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
					newEmbeddings.addAll(insertNewNode(baseEmb, newNodeName, connectToSet1, vertexSetSwitchNeeded));
				} catch (Exception e) {
					System.out.println("STOPING: There was an exception. Data were saved. Counter is at " + counter);
					return;
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
			
			// get next base embedding from the folder
			currentEmbId = baseEmb.getNextTopoEmbedding();
			
			// update the links in the embeddings
			if (newEmbeddings.size() != 0) {
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
				TempData temp      = new TempData();
				temp.connectToSet1 = connectToSet1;
				temp.counter       = counter;
				temp.currentEmbId  = currentEmbId;
				temp.lastEmbId     = lastEmb == null ? -99 : lastEmb.getId();
				temp.toFirstEmbId  = toFirstEmbId;
				temp.newNodeName   = newNodeName;
				temp.totalNumber   = totalNumber;
				temp.totalNumberFound = totalNumberFound;
				
				SafeLoad.safeTempData(toFolder, temp);
				
				return;
			}
			
		} while (currentEmbId != fromFirstEmbId);


		System.out.println("Creation of K_{" + to.first + "," + to.second + "} finished. Found " + totalNumber + " embeddings.\n");

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
			
			System.out.println("### K_{" + to.first + "," + to.second + "} is drawable. ###");
		}
		else {
			System.out.println("### SUCCESS!!! K_{" + to.first + "," + to.second + "} not drawable. ###");
		}

		System.out.println("--------------------------------------------------------------------------------------");
		System.out.println("--------------------------------------------------------------------------------------");
	}


	public void calculateEmbeddings(int stopAfter, boolean tempAvailable) {
		
		if (graphIndices == null || graphIndices.length == 0) {
			System.out.println("No graph indices found.");
			return;
		}
		
		for (int i=0; i<graphIndices.length-1; i++) {
			Tuple current = graphIndices[i];
			Tuple next    = graphIndices[i+1];
			if (current.first + current.second + 1 != next.first + next.second) {
				System.out.println("Sum of graph indices should be increased by 1 in every step.");
				return;
			}
		}
		
		String firstFolder = getFolderName(graphIndices[0].first, graphIndices[0].second);
		if (SafeLoad.isEmpty(firstFolder)) {
			System.out.println("Folder not found");
			return;
		}
				
		int graphindex = 1;
		while (graphindex < graphIndices.length) {

			int stopAfterTemp = (graphindex == graphIndices.length-1) ? stopAfter : Integer.MAX_VALUE;
			
			if (tempAvailable && graphindex == 1) {
				continueCreateKxy(graphIndices[graphindex-1], graphIndices[graphindex], stopAfterTemp);
			}
			else {
				stats = new Statistics();
				statsFile = getFolderName(graphIndices[graphindex].first, graphIndices[graphindex].second);
				long time = System.currentTimeMillis();
				
				createKxy(graphIndices[graphindex-1], graphIndices[graphindex], stopAfterTemp);
				
				stats.time = System.currentTimeMillis() - time;
				SafeLoad.safeStatistics(statsFile, stats);
				
				if (stats.nonIsomorphic == 0) {
					break;
				}
			}
			
			graphindex++;
		}
		
	}
	
	
}
