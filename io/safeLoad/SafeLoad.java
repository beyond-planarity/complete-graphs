package io.safeLoad;

import io.parseTree.TreeElement;
import io.parseTreeTranslator.ParseTreeStructureException;
import io.parseTreeTranslator.ParseTreeXMLEmbeddingTranslator;
import io.parseTreeTranslator.ParseTreeXMLMetaTranslator;
import io.parseTreeTranslator.ParseTreeXMLStatisticsTranslator;
import io.parseTreeTranslator.ParseTreeXMLTempDataTranslator;
import io.parseTreeWriterReader.XMLTreeReader;
import io.parseTreeWriterReader.XMLTreeWriter;

import java.io.File;
import java.io.IOException;
import data.Embedding;
import data.Metadata;
import data.Statistics;
import data.TempData;

/**
 * Class to safe and load data.
 * @author tommy
 *
 */
public class SafeLoad {
	
	/**
	 * Tests if the folder with the specified name exist.
	 * @param folder	a string
	 * @return			result of the test
	 */
	public static boolean hasFolder(String folder) {
		folder = folder.toLowerCase();
		File f = new File(folder);
		return f.isDirectory();
	}
	
	/**
	 * Creates a folder with the specified name.
	 * @param folder	name of the new folder
	 */
	public static void createFolder(String folder) {
		folder = folder.toLowerCase();
		File f = new File(folder);
		
		if (!f.isDirectory()) {
			f.mkdir();
		}
	}
	
	/**
	 * Returns all file names of the embeddings saved in the specified folder.
	 * @param folder	folder name
	 * @return			array of file names
	 */
	public static String[] getEmbeddingFiles(String folder) {
		folder = folder.toLowerCase();
		File f = new File(folder);
		
		File[] files = f.listFiles();
		
		String[] fileNames = new String[files.length-1];
		int counter = 0;
		for (int i=0; i<files.length; i++) {
			String name = files[i].getName();
			if (!name.equals(META + "." + EnumFileType.XML)) {
				fileNames[counter] = name;
				counter++;
			}
		}
		
		return fileNames;
	}
	
	/**
	 * Tests if the specified folder contains an embedding with the specified id.
	 * @param folder	folder name
	 * @param id		integer
	 * @return			result of the test
	 */
	public static boolean hasEmbedding(String folder, int id) {
		folder = folder.toLowerCase();
		String path = folder + File.separator + id + "." + EnumFileType.XML;
		
		File f = new File(path);
		return f.exists();
	}

	/**
	 * Saves an embedding in the specified folder.
	 * @param emb		embedding
	 * @param folder	folder name
	 * @return			true, if saving was successful
	 */
	public static boolean safeEmbedding(Embedding emb, String folder) {

		folder = folder.toLowerCase();
		String safePath = folder + File.separator + emb.getId() + "." + EnumFileType.XML;

		TreeElement treeRoot = ParseTreeXMLEmbeddingTranslator.createTree(emb);
		XMLTreeWriter treeWriter = new XMLTreeWriter(safePath, treeRoot);
		try {
			treeWriter.write();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Loads the embedding with the specified id from the specified folder
	 * @param folder	folder name
	 * @param id		id of an embedding
	 * @return			the embedding
	 */
	public static Embedding loadEmbedding(String folder, int id) {

		String loadPath = folder.toLowerCase() + File.separator + id + "." + EnumFileType.XML;
	
		TreeElement treeRoot = null;
		XMLTreeReader treeReader = new XMLTreeReader(loadPath);
		try {
			treeRoot = treeReader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			return ParseTreeXMLEmbeddingTranslator.treeToEmbedding(treeRoot);
		} catch (ParseTreeStructureException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Loads the embedding with file name <code>file</code> from the specified folder.
	 * @param folder	folder name
	 * @param file		file name
	 * @return			the embedding
	 */
	public static Embedding loadEmbedding(String folder, String file) {

		String loadPath = folder.toLowerCase() + File.separator + file;
	
		TreeElement treeRoot = null;
		XMLTreeReader treeReader = new XMLTreeReader(loadPath);
		try {
			treeRoot = treeReader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			return ParseTreeXMLEmbeddingTranslator.treeToEmbedding(treeRoot);
		} catch (ParseTreeStructureException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	

	
	private final static String META = "meta";
	/**
	 * Tests if the specified folder contains a metadata file
	 * @param folder	folder name
	 * @return			result of the test
	 */
	public static boolean hasMetaData(String folder) {

		folder = folder.toLowerCase();
		String path = folder + File.separator + META + "." + EnumFileType.XML;
		
		File f = new File(path);
		return f.exists();
	}
	
	/**
	 * Saves metadata into the specified folder.
	 * @param folder	folder name
	 * @param metadata	metadata to save.
	 * @return			true, if saving was successful
	 */
	public static boolean safeMetadata(String folder, Metadata metadata) {

		folder = folder.toLowerCase();
		String safePath = folder + File.separator + META + "." + EnumFileType.XML;

		TreeElement treeRoot = ParseTreeXMLMetaTranslator.createTree(metadata);
		XMLTreeWriter treeWriter = new XMLTreeWriter(safePath, treeRoot);
		try {
			treeWriter.write();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Loads metadata from the specified folder.
	 * @param folder	folder name
	 * @return			metadata
	 */
	public static Metadata loadMetadata(String folder) {

		String loadPath = folder.toLowerCase() + File.separator + META + "." + EnumFileType.XML;
	
		TreeElement treeRoot = null;
		XMLTreeReader treeReader = new XMLTreeReader(loadPath);
		try {
			treeRoot = treeReader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			return ParseTreeXMLMetaTranslator.treeToMeta(treeRoot);
		} catch (ParseTreeStructureException e) {
			e.printStackTrace();
		}
		return null;
	}



	private final static String TEMP = "temp";
	/**
	 * Saves temporary data into the specified folder.
	 * @param folder	folder name
	 * @param tempData	temporary data
	 * @return			true, if saving was successful
	 */
	public static boolean safeTempData(String folder, TempData tempData) {

		folder = folder.toLowerCase();
		String safePath = folder + File.separator + TEMP + "." + EnumFileType.XML;

		TreeElement treeRoot = ParseTreeXMLTempDataTranslator.createTree(tempData);
		XMLTreeWriter treeWriter = new XMLTreeWriter(safePath, treeRoot);
		try {
			treeWriter.write();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Loads temporary data from the specified folder.
	 * @param folder	folder name
	 * @return			temporary data
	 */
	public static TempData loadTempData(String folder) {

		String loadPath = folder.toLowerCase() + File.separator + TEMP + "." + EnumFileType.XML;
	
		TreeElement treeRoot = null;
		XMLTreeReader treeReader = new XMLTreeReader(loadPath);
		try {
			treeRoot = treeReader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			return ParseTreeXMLTempDataTranslator.treeToTempData(treeRoot);
		} catch (ParseTreeStructureException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Deletes temporary data in the specified folder.
	 * @param folder	folder name
	 */
	public static void deleteTempData(String folder) {
		
		String loadPath = folder.toLowerCase() + File.separator + TEMP + "." + EnumFileType.XML;
		File f = new File(loadPath);
		
		if (f.exists()) {
			f.delete();
		}
	}
	
	
	/**
	 * Saves statistical data into the specified folder.
	 * @param folderName	folder name
	 * @param stats			statistical data
	 * @return				true, if saving was successful
	 */
	public static boolean safeStatistics(String folderName, Statistics stats) {

		folderName = folderName.toLowerCase();
		String safePath = folderName + "." + EnumFileType.XML;

		TreeElement treeRoot = ParseTreeXMLStatisticsTranslator.createTree(stats);
		XMLTreeWriter treeWriter = new XMLTreeWriter(safePath, treeRoot);
		try {
			treeWriter.write();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Loads statistical data from the specified folder.
	 * @param folder	folder name
	 * @return			statistical data
	 */
	public static Statistics loadStatistics(String folder) {

		String loadPath = folder.toLowerCase() + "." + EnumFileType.XML;
	
		TreeElement treeRoot = null;
		XMLTreeReader treeReader = new XMLTreeReader(loadPath);
		try {
			treeRoot = treeReader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			return ParseTreeXMLStatisticsTranslator.treeToStatistics(treeRoot);
		} catch (ParseTreeStructureException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Tests if the specified folder is empty.
	 * @param folder	folder name
	 * @return			result of the test
	 */
	public static boolean isEmpty(String folder) {		
		folder = folder.toLowerCase();
		File f = new File(folder);
		
		if (!f.isDirectory()) {
			return false;
		}
		
		return f.list().length == 0;
	}
	
	
	
	
	

	/**
	 * Exports the specified embedding into the specified folder.
	 * @param emb		embedding
	 * @param folder	target folder name
	 * @return			true, if exporting was successful
	 */
	public static boolean export(Embedding emb, String folder) {

		folder          = folder.toLowerCase();
		String safePath = folder + File.separator + emb.getId() + "." + EnumFileType.GML;
		
		GMLWriter gmlWriter = new GMLWriter(safePath, emb);
		try {
			gmlWriter.write();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Exports all embeddings of the specified folder into the folder gml<code>sourceFolder</code>.
	 * @param sourceFolder	folder containing the embeddings to export
	 * @return				true, if exporting was successful
	 */
	public static boolean exportAll(String sourceFolder) {
		if (isEmpty(sourceFolder)) {
			return false;
		}
		
		String[] files = getEmbeddingFiles(sourceFolder);
		if (files == null || files.length == 0) {
			return false;
		}
		
		String targetFolder = "GML" + sourceFolder;
		
		if (!hasFolder(targetFolder)) {
			createFolder(targetFolder);
		}
		
		for (String file : files) {
			Embedding emb = loadEmbedding(sourceFolder, file);
			export(emb, targetFolder);
		}
		
		return true;
	}
}
