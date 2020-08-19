package io.safeLoad;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import data.Constant;

public class PathChooser {

	/* current directory */
	public static String DIRECTORY = null;
	public static final String DATA_FILE_STRING = EnumFileType.getFileExtension(EnumFileType.GML);
	private static final FileFilter DATA_FILTER = new FileNameExtensionFilter(DATA_FILE_STRING, DATA_FILE_STRING);
	
	
	/**
	 * Displays a file chooser for saving/exporting.
	 * @param parent	parent component of the file chooser
	 * @param fileType	file type for the file to save/export
	 * @return			save/export path or null
	 */
	public static String displayExportFileChooser(Component parent) {
		
		/* choose file destination */
		@SuppressWarnings("serial")
		JFileChooser fileChooser = new JFileChooser(DIRECTORY){
			@Override
			public void approveSelection(){
				File f = getSelectedFile();
				String fName = f.getAbsolutePath();
				String extension = getFileFilter().getDescription();
				
				if(!fName.toLowerCase().endsWith("." + extension)) {
					fName += "." + extension;
				}
				
				
				if(new File(fName).exists()) {
					int result = JOptionPane.showConfirmDialog(
							this,
							Constant.CONFIRM_FILE_OVERWRITE_TEXT,
							Constant.CONFIRM_FILE_OVERWRITE_TITLE,
							JOptionPane.OK_CANCEL_OPTION);
					switch(result){
					case JOptionPane.OK_OPTION:
						super.approveSelection();
						return;
					case JOptionPane.CLOSED_OPTION:
						return;
					case JOptionPane.CANCEL_OPTION:
						cancelSelection();
						return;
					}
				}
			super.approveSelection();
			}
		};
		
		
		fileChooser.setFileFilter(DATA_FILTER);
		String chooserTitle = Constant.TITLE_FILE_CHOOSER;
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		
		int returnValue = fileChooser.showDialog(parent, chooserTitle);
		//DIRECTORY = fileChooser.getCurrentDirectory().getAbsolutePath();
		
		
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String fileName = selectedFile.getAbsolutePath();
			String extension = fileChooser.getFileFilter().getDescription();
			
			if(!fileName.toLowerCase().endsWith("." + extension)) {
				fileName += "." + extension;
			}
			
			DIRECTORY = fileName;
			return fileName;
		}
		return null;
	}

}
