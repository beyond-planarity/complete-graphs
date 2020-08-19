package embeddingComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import data.Constant;
import data.Edge;
import data.Embedding;
import data.Face;
import data.Metadata;
import data.Vertex;
import io.safeLoad.SafeLoad;

/**
 * Class to test embeddings for isomorphism.
 * @author tommy
 *
 */
public class EmbeddingComparator {

	/**
	 * Compares the embeddings in a list of embeddings with each other.
	 * @param embs             list of embeddings
	 * @param currentGraphName name of the current graph for printing intermediate steps
	 * @return                 the list of topologically different embeddings
	 */
	public static List<Embedding> compareEmbeddingsRAM(List<Embedding> embs, String currentGraphName) {
		
		List<Embedding> differentEmbs = new LinkedList<Embedding>();
		
		if (embs.isEmpty()) {
			return differentEmbs;
		}
		
		String strFront = currentGraphName + " - Compared new embeddings: ";
		String strBack  = "/" + embs.size() + " - Different until now: ";
		
		int counter = 0;
		
		for (Embedding baseEmb : embs) {
			Embedding reversedBaseEmb = baseEmb.copyReversed(baseEmb.getId());

			boolean foundEqualEmb = false;
			for (Embedding refEmb : differentEmbs) {
				if (compareEmbedding(baseEmb, refEmb) || compareEmbedding(reversedBaseEmb, refEmb)) {
					foundEqualEmb = true;
					break;
				}
			}
			if (!foundEqualEmb) {
				differentEmbs.add(baseEmb);
			}

			counter++;
			if (counter % Constant.INTERNAL_COMPARING_PROGRESS == 0) {
				System.out.println(strFront + counter + strBack + differentEmbs.size());
			}
		}
		
		return differentEmbs;
	}
	
	

	/**
	 * Compares each embedding in a list of embeddings to the embeddings safed in the folder with
	 * name <code>folderName</code>. If there is a topologically equivalent embedding already
	 * saved in the folder, the embedding is not added to the returned list.
	 * @param embs          list of embeddings that are not equivalent to each other
	 * @param folderName    name of the folder that contains embeddings on the hard drive
	 * @param lastToCompare the id of the last embedding on disc, that should be compared
	 * @param strGenProg    name of the current operation (for printing)
	 * @return              a list of topologically different embeddings
	 */
	public static List<Embedding> compareEmbeddingsHD(List<Embedding> embs, String folderName, int lastToCompare,
			String currentGraphName) {
		
		List<Embedding> differentEmbs = embs;
		
		if (SafeLoad.isEmpty(folderName) || embs.isEmpty()) {
			return differentEmbs;
		}

		// load metadata
		Metadata metadata = SafeLoad.loadMetadata(folderName);
		int startEmbId = metadata.getIdStartEmbedding();
		int totalSaved = metadata.getNumberEmbeddingsTotal();

		// compare embeddings
		String strFront = "General progress: " + currentGraphName + " - Compared saved embeddings: ";
		String strBack  = "/" + totalSaved + " - Different until now: ";

		// compare base embedding to all saved embeddings
		int refEmbId = startEmbId;
		int counter = 0;
		while (true) {
			Embedding refEmb = SafeLoad.loadEmbedding(folderName, refEmbId);
			Embedding reversedRefEmb = refEmb.copyReversed(refEmbId);

			List<Embedding> currentEmbs = new LinkedList<Embedding>();
			boolean foundEquivalent = false;
			for (Embedding baseEmb : differentEmbs) {
				if (foundEquivalent) {
					// since embeddings in the list are pairwise not equivalent and an equivalent embedding
					// to the refEmbedding was already found, all other embeddings in the list cannot
					// be equivalent to the refEmbedding, so no tests are needed here
					currentEmbs.add(baseEmb);
				}
				else if (compareEmbedding(baseEmb, refEmb) || compareEmbedding(baseEmb, reversedRefEmb)) {
					foundEquivalent = true;
				}
				else {
					currentEmbs.add(baseEmb);
				}
			}
			differentEmbs = currentEmbs;

			counter++;
			if (counter % Constant.EXTERNAL_COMPARING_PROGRESS == 0) {
				System.out.println(strFront + counter + strBack + differentEmbs.size());
			}

			if (refEmbId == lastToCompare || embs.isEmpty()) {
				break;
			}
			else {
				refEmbId = refEmb.getNextEmbedding();
			}
		}
		
		return differentEmbs;
	}
	
	

	/**
	 * Compares the embeddings in the folder <code>folderName</code>. If there are topologically
	 * equivalent, this information is saved, as well as a mapping of the nodes.
	 * @param folderName  name of the folder
	 * @param startNumber the number where the comparison should start (if the comparison was stopped before)
	 */
	public static void compareEmbeddingsHDExtended(String folderName, int startNumber) {
		
		if (SafeLoad.isEmpty(folderName)) {
			return;
		}
		
		// load metadata
		Metadata metadata = SafeLoad.loadMetadata(folderName);
		
		System.out.println("Compare " + metadata.getNumberEmbeddingsTotal() + " embeddings.");

		int counter         = 0;
		int counterTopoEmbs = 0;
		int startEmbId      = metadata.getIdStartEmbedding();
		
		Embedding prevTopoEmb;
		int       currentEmbId;
		
		if (startNumber <= 0) {

			counter = 1;
			counterTopoEmbs = 1;

			prevTopoEmb = SafeLoad.loadEmbedding(folderName, startEmbId);
			prevTopoEmb.setReferenceEmbeddingId(startEmbId);
			copyVertexNameToLabel(prevTopoEmb);

			if (metadata.getNumberEmbeddingsTotal() == 1) {
				prevTopoEmb.setNextTopoEmbedding(startEmbId);
				prevTopoEmb.setPrevTopoEmbedding(startEmbId);
				SafeLoad.safeEmbedding(prevTopoEmb, folderName);
				return;
			}
			SafeLoad.safeEmbedding(prevTopoEmb, folderName);

			currentEmbId = prevTopoEmb.getNextEmbedding();

		}
		else {
			// search for the first embedding to consider
			
			currentEmbId = startEmbId;
			prevTopoEmb = null;
			
			String strFront = folderName + " - Searching start ... ";
			String strBack  = "/" + metadata.getNumberEmbeddingsTotal() + " - Topo. different: ";
			
			while (counter < startNumber - 1) {
				Embedding baseEmb = SafeLoad.loadEmbedding(folderName, currentEmbId);
				if (baseEmb.getReferenceEmbeddingId() == baseEmb.getId()) {
					prevTopoEmb = baseEmb;
					counterTopoEmbs++;
				}
				currentEmbId = baseEmb.getNextEmbedding();

				counter++;
				if (counter % Constant.EXTERNAL_COMPARING_PROGRESS == 0) {
					System.out.println(strFront + counter + strBack + counterTopoEmbs);
				}
			}
		}
		
		
		String strFront = "Compared embeddings: ";
		String strBack  = "/" + metadata.getNumberEmbeddingsTotal() + " - Topo. different: ";
		
		do {
			Embedding baseEmb = SafeLoad.loadEmbedding(folderName, currentEmbId);
			Embedding reversedBaseEmb = baseEmb.copyReversed(currentEmbId);
			
			// compare base embedding to all embeddings before
			boolean foundRef = false;
			int referenceEmbId = startEmbId;
			while (true) {
				Embedding refEmb = SafeLoad.loadEmbedding(folderName, referenceEmbId);
				if (foundRef = compareEmbedding(baseEmb, refEmb)) {
					baseEmb.setReferenceEmbeddingId(referenceEmbId);
					baseEmb.setPrevTopoEmbedding(prevTopoEmb.getId());
					SafeLoad.safeEmbedding(baseEmb, folderName);
					break;
				}
				else if (foundRef = compareEmbedding(reversedBaseEmb, refEmb)) {
					baseEmb.setReferenceEmbeddingId(referenceEmbId);
					baseEmb.setPrevTopoEmbedding(prevTopoEmb.getId());
					
					// copy vertex labels
					for (Vertex v : reversedBaseEmb.getVertices().values()) {
						baseEmb.setVertexLabel(v.getId(), v.getMappedName());
					}
					
					SafeLoad.safeEmbedding(baseEmb, folderName);
					break;
				}
			
				if (referenceEmbId == prevTopoEmb.getId()) {
					break;
				}
				else {
					referenceEmbId = refEmb.getNextTopoEmbedding();
				}
			}
			
			if (!foundRef) {
				counterTopoEmbs++;
				baseEmb.setReferenceEmbeddingId(baseEmb.getId());
				baseEmb.setPrevTopoEmbedding(prevTopoEmb.getId());
				prevTopoEmb.setNextTopoEmbedding(baseEmb.getId());
				copyVertexNameToLabel(baseEmb);
				
				SafeLoad.safeEmbedding(prevTopoEmb, folderName);
				SafeLoad.safeEmbedding(baseEmb, folderName);
				
				prevTopoEmb = baseEmb;
			}
			currentEmbId = baseEmb.getNextEmbedding();
			
			
			counter++;
			if (counter % Constant.EXTERNAL_COMPARING_PROGRESS == 0) {
				System.out.println(strFront + counter + strBack + counterTopoEmbs);
			}
			
		} while (currentEmbId != startEmbId);
		
		// set info for last embedding
		Embedding firstEmb = SafeLoad.loadEmbedding(folderName, startEmbId);
		firstEmb.setPrevTopoEmbedding(prevTopoEmb.getId());
		prevTopoEmb.setNextTopoEmbedding(firstEmb.getId());
		SafeLoad.safeEmbedding(firstEmb, folderName);
		SafeLoad.safeEmbedding(prevTopoEmb, folderName);
		
		// update pointers
		System.out.println("Updating final references...");
		counter = 1;
		int embId = firstEmb.getPrevEmbedding();
		int refId = startEmbId;
		
		strFront = "Updated: ";
		strBack  = "/" + metadata.getNumberEmbeddingsTotal();
		
		do {
			Embedding emb = SafeLoad.loadEmbedding(folderName, embId);
			
			if (emb.getReferenceEmbeddingId() == emb.getId()) {
				refId = emb.getId();
			}
			else {
				emb.setNextTopoEmbedding(refId);
				SafeLoad.safeEmbedding(emb, folderName);
			}
			embId = emb.getPrevEmbedding();

			counter++;
			if (counter % Constant.EXTERNAL_COMPARING_PROGRESS == 0) {
				System.out.println(strFront + counter + strBack);
			}
			
		} while (embId != startEmbId);
		
		// safe metadata
		metadata.setNumberEmbeddingsTopoDifferent(counterTopoEmbs);
		SafeLoad.safeMetadata(folderName, metadata);

		System.out.println(folderName + " - found " + counterTopoEmbs + " topologically different embeddings.");
		System.out.println();
	}
	
	
	
	
	private static void copyVertexNameToLabel(Embedding emb) {
		for (Vertex v : emb.getVertices().values()) {
			v.setMappedName(v.getName());
		}
	}

	/**
	 * Compares two embeddings. Calculate a mapping of vertices, crossings, edges and faces,
	 * if both embeddings are equivalent.
	 * @param emb    first embedding
	 * @param refEmb second embedding (reference embedding)
	 * @return true, if and only if two embeddings are equivalent
	 */
	private static boolean compareEmbedding(Embedding emb, Embedding refEmb) {

		HashMap<Integer, Vertex> embVs    = emb.getVertices();
		HashMap<Integer, Edge>   embEs    = emb.getEdges();
		HashMap<Integer, Face>   embFs    = emb.getFaces();
		HashMap<Integer, Vertex> refEmbVs = refEmb.getVertices();
		HashMap<Integer, Edge>   refEmbEs = refEmb.getEdges();
		HashMap<Integer, Face>   refEmbFs = refEmb.getFaces();

		// compare number of vertices, crossings, edges, faces
		if (embVs.size() != refEmbVs.size() ||
				emb.getCrossingNumber() != refEmb.getCrossingNumber() ||
				embEs.size() != refEmbEs.size() ||
				embFs.size() != refEmbFs.size()) {
			return false;
		}

		// sort edges of each embedding into four lists:
		// - edges that have no crossing as endpoint
		// - edges that have a crossing as source only
		// - edges that have a crossing as target only
		// - edges that have two crossings as endpoints
		List<Edge> emb0CrEdges     = new ArrayList<Edge>();
		List<Edge> emb1sCrEdges    = new ArrayList<Edge>();
		List<Edge> emb1tCrEdges    = new ArrayList<Edge>();
		List<Edge> emb2CrEdges     = new ArrayList<Edge>();
		List<Edge> refEmb0CrEdges  = new ArrayList<Edge>();
		List<Edge> refEmb1sCrEdges = new ArrayList<Edge>();
		List<Edge> refEmb1tCrEdges = new ArrayList<Edge>();
		List<Edge> refEmb2CrEdges  = new ArrayList<Edge>();

		for (Edge edge : embEs.values()) {
			Vertex source = edge.getSource();
			Vertex target = edge.getTarget();
			if (source.isCrossing()) {
				if (target.isCrossing()) {
					emb2CrEdges.add(edge);
				}
				else {
					emb1sCrEdges.add(edge);
				}
			}
			else {
				if (target.isCrossing()) {
					emb1tCrEdges.add(edge);
				}
				else {
					emb0CrEdges.add(edge);
				}
			}
		}

		for (Edge edge : refEmbEs.values()) {
			Vertex source = edge.getSource();
			Vertex target = edge.getTarget();
			if (source.isCrossing()) {
				if (target.isCrossing()) {
					refEmb2CrEdges.add(edge);
				}
				else {
					refEmb1sCrEdges.add(edge);
				}
			}
			else {
				if (target.isCrossing()) {
					refEmb1tCrEdges.add(edge);
				}
				else {
					refEmb0CrEdges.add(edge);
				}
			}
		}

		if (emb0CrEdges.size() != refEmb0CrEdges.size() || 
				emb1sCrEdges.size() != refEmb1sCrEdges.size() ||
				emb1tCrEdges.size() != refEmb1tCrEdges.size() ||
				emb2CrEdges.size() != refEmb2CrEdges.size()) {
			return false;
		}
		// now the number of edges in each of the 4 lists is the same

		// it is only necessary to map edges of one list
		// no need to test target list, since the twins in the source list take care of that
		// use smallest list
		
		// sort the three lists
		List<List<Edge>> sortedLists = new LinkedList<List<Edge>>();
		List<List<Edge>> sortedListsRef = new LinkedList<List<Edge>>();
		sortedLists.add(emb0CrEdges);
		sortedListsRef.add(refEmb0CrEdges);
		if (emb1sCrEdges.size() < emb0CrEdges.size()) {
			sortedLists.add(0, emb1sCrEdges);
			sortedListsRef.add(0, refEmb1sCrEdges);
		}
		else {
			sortedLists.add(emb1sCrEdges);
			sortedListsRef.add(refEmb1sCrEdges);
		}
		if (emb2CrEdges.size() <= sortedLists.get(0).size()) {
			sortedLists.add(0, emb2CrEdges);
			sortedListsRef.add(0, refEmb2CrEdges);
		}
		else if (emb2CrEdges.size() <= sortedLists.get(1).size()) {
			sortedLists.add(1, emb2CrEdges);
			sortedListsRef.add(1, refEmb2CrEdges);
		}
		else {
			sortedLists.add(emb2CrEdges);
			sortedListsRef.add(refEmb2CrEdges);
		}
		

		if (!sortedLists.get(0).isEmpty()) {
			return testList(sortedLists.get(0), sortedListsRef.get(0), emb, refEmb);
		}
		else if (!sortedLists.get(1).isEmpty()) {
			return testList(sortedLists.get(1), sortedListsRef.get(1), emb, refEmb);
		} 
		else if (!sortedLists.get(2).isEmpty()) {
			return testList(sortedLists.get(2), sortedListsRef.get(2), emb, refEmb);
		}
		else {	// all lists are empty, no edges at all
			return true;
		}
	}


	private static boolean testList(List<Edge> embEdges, List<Edge> refEmbEdges,
			Embedding emb, Embedding refEmb) {

		// map edges with two crossings on each other
		// get first edge; map this edge to all edges in the reference embedding;
		// then explore and map in both graphs until all is mapped
		Edge   basicEdge     = embEdges.get(0);
		Edge   basicTwinEdge = basicEdge.getTwin();
		Face   basicFace     = basicEdge.getFace();
		Face   basicTwinFace = basicTwinEdge.getFace();
		Vertex basicSource   = basicEdge.getSource();
		Vertex basicTarget   = basicEdge.getTarget();

		for (Edge refEdge : refEmbEdges) {
			// map the basic edge to the refEdge

			// data structures to map nodes, edges and faces of the embedding to the reference embedding
			HashMap<Integer,Integer> mapVs = new HashMap<Integer,Integer>();
			HashMap<Integer,Integer> mapEs = new HashMap<Integer,Integer>();
			HashMap<Integer,Integer> mapFs = new HashMap<Integer,Integer>();

			List<VertexEdgeTuple> verticesToConsider = new LinkedList<VertexEdgeTuple>();

			Edge refTwinEdge = refEdge.getTwin();
			Face refFace     = refEdge.getFace();
			Face refTwinFace = refTwinEdge.getFace();

			mapEs.put(basicEdge.getId(), refEdge.getId());
			mapEs.put(basicTwinEdge.getId(), refTwinEdge.getId());
			mapVs.put(basicSource.getId(), refEdge.getSource().getId());
			mapVs.put(basicTarget.getId(), refEdge.getTarget().getId());
			if (emb.getFaceDegree(basicFace.getId()) != refEmb.getFaceDegree(refFace.getId())) {
				// conflict
				continue;
			}
			mapFs.put(basicFace.getId(), refFace.getId());
			if (emb.getFaceDegree(basicTwinFace.getId()) != refEmb.getFaceDegree(refTwinFace.getId())) {
				// conflict
				continue;
			}
			mapFs.put(basicTwinFace.getId(), refTwinFace.getId());

			verticesToConsider.add(new VertexEdgeTuple(basicSource,basicEdge));
			verticesToConsider.add(new VertexEdgeTuple(basicTarget,basicTwinEdge));



			boolean isConflict = false;

			QUEUE:
				while (!verticesToConsider.isEmpty()) {
					VertexEdgeTuple vet = verticesToConsider.remove(0);
					Vertex currentV     = vet.v;
					Edge   currentE     = vet.e;
					int    currentVId   = currentV.getId();
					int    currentEId   = currentE.getId();

					int   currentRefVId = mapVs.get(currentVId);
					int   currentRefEId = mapEs.get(currentEId);
					Edge   currentRefE  = refEmb.getEdge(currentRefEId);

					if (emb.getVertexDegree(currentVId) != refEmb.getVertexDegree(currentRefVId)) {
						// conflict -> not a valid mapping
						isConflict = true;
						break QUEUE;
					}

					// loop over all edges of node
					Edge loopE    = currentE;
					Edge loopRefE = currentRefE; 
					do {
						Vertex loopTarget    = loopE.getTarget();
						Edge   loopTwin      = loopE.getTwin();
						Vertex loopRefTarget = loopRefE.getTarget();
						Edge   loopRefTwin   = loopRefE.getTwin();

						// test target
						if (mapVs.containsKey(loopTarget.getId())) {
							if (mapVs.get(loopTarget.getId()) != loopRefTarget.getId()) {
								// conflict
								isConflict = true;
								break QUEUE;
							}
						}
						else {
							if ((loopTarget.isCrossing() && !loopRefTarget.isCrossing()) 
									|| (!loopTarget.isCrossing() && loopRefTarget.isCrossing())) {
								// conflict
								isConflict = true;
								break QUEUE;
							}
							mapVs.put(loopTarget.getId(), loopRefTarget.getId());
							verticesToConsider.add(new VertexEdgeTuple(loopTarget, loopTwin));
						}

						// test edges
						if (mapEs.containsKey(loopE.getId())) {
							if (mapEs.get(loopE.getId()) != loopRefE.getId()) {
								// conflict
								isConflict = true;
								break QUEUE;
							}
						}
						else {
							mapEs.put(loopE.getId(), loopRefE.getId());
						}

						if (mapEs.containsKey(loopTwin.getId())) {
							if (mapEs.get(loopTwin.getId()) != loopRefTwin.getId()) {
								// conflict
								isConflict = true;
								break QUEUE;
							}
						}
						else {
							mapEs.put(loopTwin.getId(), loopRefTwin.getId());
						}

						// test faces
						if (mapFs.containsKey(loopE.getFace().getId())) {
							if (mapFs.get(loopE.getFace().getId()) != loopRefE.getFace().getId()) {
								// conflict
								isConflict = true;
								break QUEUE;
							}
						}
						else {
							mapFs.put(loopE.getFace().getId(), loopRefE.getFace().getId());
						}

						if (mapFs.containsKey(loopTwin.getFace().getId())) {
							if (mapFs.get(loopTwin.getFace().getId()) != loopRefTwin.getFace().getId()) {
								// conflict
								isConflict = true;
								break QUEUE;
							}
						}
						else {
							mapFs.put(loopTwin.getFace().getId(), loopRefTwin.getFace().getId());
						}


						loopE    = loopTwin.getNext();
						loopRefE = loopRefTwin.getNext();
					} while (loopE.getId() != currentEId);

				}




			if (!isConflict) {
				// test if the gaps are correctly mapped;
				for (Entry<Integer, Integer> entry : emb.getGapCrossingsToEdge().entrySet()) {
					int gap     = entry.getKey();
					int gapEdge = entry.getValue();
					int source  = emb.getRealSource(gapEdge);
					int target  = emb.getRealTarget(gapEdge);
					
					int refGap     = mapVs.get(gap);
					int refGapMapE = refEmb.getGapEdge(refGap);
					int refSource  = refEmb.getRealSource(refGapMapE);
					
					// the source or the target must be mapped to refSource, then the gaps have the same direction
					if (mapVs.get(source) != refSource && mapVs.get(target) != refSource) {
						isConflict = true;
						break;
					}
				}
			}
			
			if (!isConflict) {
				// update the references in the vertices
				for (Entry<Integer, Integer> entry : mapVs.entrySet()) {
					emb.setVertexLabel(entry.getKey(), refEmb.getVertexLabel(entry.getValue()));
				}
				return true;
			}
		}


		return false;
	}



	private static class VertexEdgeTuple {
		Vertex v;
		Edge   e;
		VertexEdgeTuple(Vertex v, Edge e) {
			this.v = v;
			this.e = e;
		}
	}

}
