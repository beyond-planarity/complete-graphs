package io.export;

import io.safeLoad.SafeLoad;

/**
 * Class to export the contents of a folder to .gml files.
 * @author Tommy
 *
 */
public class RunExport {
	
	private static String folderName = "optfancrfree_4_5";
	
	public static void main(String[] args) {
		System.out.println("Exporting files ...");
		SafeLoad.exportAll(folderName);
		System.out.println("DONE.");
	}

}
