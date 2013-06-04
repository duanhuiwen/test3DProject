package fi.metropolia.threedrelics.classes;

import android.app.Service;
import android.content.Intent;
import android.util.Log; 
import android.widget.Toast;

import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileOutputStream; 
import java.util.zip.ZipEntry; 
import java.util.zip.ZipInputStream; 

import fi.metropolia.threedrelics.db.DbEntry;
 

public class Decompress implements Runnable { 
  private String zipFile; 
  private String location; 
  private Service service;
  private String scene_id;
 
  public Decompress(Service service, String zipFile, String location, String scene_id) { 
    this.zipFile = zipFile; 
    this.location = location; 
    this.service = service;
    this.scene_id = scene_id;
    dirChecker(""); 
  } 
 
  public void unzip() { 
    try  { 
      FileInputStream fin = new FileInputStream(zipFile); 
      
      ZipInputStream zin = new ZipInputStream(fin); 
      ZipEntry ze = null; 
      while ((ze = zin.getNextEntry()) != null) { 
        Log.v("Decompress", "Unzipping " + ze.getName()); 
 
        if(ze.isDirectory()) { 
          dirChecker(ze.getName()); 
        } else { 
          FileOutputStream fout = new FileOutputStream(location + ze.getName()); 
          for (int c = zin.read(); c != -1; c = zin.read()) { 
            fout.write(c); 
          } 
 
          zin.closeEntry(); 
          fout.close(); 
          
        } 
         
      } 
      zin.close(); 
      
      
      File file = new File(zipFile);
      file.delete();
      
      notifyDecompressFinished();
      
    } catch(Exception e) { 
      Log.e("Decompress", "unzip", e); 
    } 
 
  } 
 
  private void dirChecker(String dir) { 
    File f = new File(location + dir); 
 
    if(!f.isDirectory()) { 
      f.mkdirs(); 
    } 
  }

@Override
public void run() {
	// TODO Auto-generated method stub
	this.unzip();
} 



//notify the app decompresssion is finished
public void notifyDecompressFinished(){
	//send path here for future use in broadcast receiver
	Intent in = new Intent(StaticString.DECOMPRESS_FINISHED);
	in.putExtra(DbEntry.COLUMN_NAME_MODEL_PATH, location);
	in.putExtra(DbEntry.COLUMN_NAME_SCENE_ID, scene_id);
	this.service.sendBroadcast(in);
}
} 
