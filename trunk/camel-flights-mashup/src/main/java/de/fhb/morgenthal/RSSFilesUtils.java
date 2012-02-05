package de.fhb.morgenthal;

import java.io.File;

public class RSSFilesUtils {

	public static File getLatestRssFile(String land){
		File rssDir = new File(MyRouteBuilder.RSSTempDirectory+"\\"+land);
		File[] rssDirFile = rssDir.listFiles();
        
		if(rssDirFile != null && rssDirFile.length > 0){

   		 File latest = rssDirFile[0];
   		 long latestDate = latest.lastModified();
   		 
   		 for (File file : rssDirFile) {
				if(file.lastModified() > latestDate && file.canRead()){
					latest = file;
					latestDate = file.lastModified();
				}         		 
   		 }
   		 
   		return latest; 
			
   	 }
   	 
   	 else return null;        		 
   		
	}
}
