package fi.metropolia.threedrelics.classes;

import java.io.*;

public class ExtensionFinder {
 
	
 
	private String filename;
	public ExtensionFinder(String folder, String ext){
		GenericExtFilter filter = new GenericExtFilter(ext);
		 
		File dir = new File(folder);
 
		if(dir.isDirectory()==false){
			System.out.println("Directory does not exists : " + folder);
			return;
		}
 
		// list out all the file name and filter by the extension
		String[] list = dir.list(filter);
		//filename = list[0];
		if (list.length == 0) {
			System.out.println("no files end with : " + ext);
			return;
		}
 
/*		for (String file : list) {
			filename = new StringBuffer(folder).append(File.separator)
					.append(list[0]).toString();
			System.out.println("file : " + temp);
		}*/
		
		
		//filename = new StringBuffer(folder).append(File.separator).append(list[0]).toString();
		filename = list[0];
	}
	/*public void listFile(String folder, String ext) {
 
		GenericExtFilter filter = new GenericExtFilter(ext);
 
		File dir = new File(folder);
 
		if(dir.isDirectory()==false){
			System.out.println("Directory does not exists : " + FILE_DIR);
			return;
		}
 
		// list out all the file name and filter by the extension
		String[] list = dir.list(filter);
 
		if (list.length == 0) {
			System.out.println("no files end with : " + ext);
			return;
		}
 
		for (String file : list) {
			String temp = new StringBuffer(FILE_DIR).append(File.separator)
					.append(file).toString();
			System.out.println("file : " + temp);
		}
	}*/
 
	// inner class, generic extension filter
	public class GenericExtFilter implements FilenameFilter {
 
		private String ext;
		private String extUpper;
 
		public GenericExtFilter(String ext) {
			this.ext = ext;
			this.extUpper = ext.toUpperCase();
		}
 
		public boolean accept(File dir, String name) {
			return (name.endsWith(extUpper) || name.endsWith(ext) ) ;
		}
	}
	
	
	public String getFile(){
		return filename;
	}
}