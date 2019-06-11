package io.export;

import io.safeLoad.SafeLoad;

/**
 * Class to export the contents of a folder to .gml files.
 * @author Tommy
 *
 */
public class RunExport {
	
	// TODO: SPECIFY FOLDER NAME TO EXPORT
	private static String folderName = "dfsgapplanar_4_9";
	
	
	public static void main(String[] args) {
		System.out.println("Exporting files ...");
		SafeLoad.exportAll(folderName);
		System.out.println("DONE.");
	}

}
