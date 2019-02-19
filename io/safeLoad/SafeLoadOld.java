package io.safeLoad;

import io.parseTree.TreeElement;
import io.parseTreeTranslator.ParseTreeStructureException;
import io.parseTreeTranslator.ParseTreeXMLEmbeddingTranslatorNames;
import io.parseTreeTranslator.ParseTreeXMLMetaTranslator;
import io.parseTreeWriterReader.XMLTreeReader;
import io.parseTreeWriterReader.XMLTreeWriter;

import java.io.File;
import java.io.IOException;
import data.Embedding;
import data.Metadata;

public class SafeLoadOld {
	
	public static void createFolder(String folder) {
		
		folder = folder.toLowerCase();
		File f = new File(folder);
		
		if (!f.isDirectory()) {
			f.mkdir();
		}
	}

	public static boolean safeEmbedding(Embedding emb, String folder) {

		folder = folder.toLowerCase();
		String safePath = folder + File.separator + emb.getId() + "." + EnumFileType.XML;

		TreeElement treeRoot = ParseTreeXMLEmbeddingTranslatorNames.createTree(emb);
		XMLTreeWriter treeWriter = new XMLTreeWriter(safePath, treeRoot);
		try {
			treeWriter.write();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

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
			return ParseTreeXMLEmbeddingTranslatorNames.treeToEmbedding(treeRoot);
		} catch (ParseTreeStructureException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	

	private final static String META = "meta";
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

	
	public static boolean isEmpty(String folder) {		
		folder = folder.toLowerCase();
		File f = new File(folder);
		
		if (!f.isDirectory()) {
			return false;
		}
		
		return f.list().length == 0;
	}
	
}
