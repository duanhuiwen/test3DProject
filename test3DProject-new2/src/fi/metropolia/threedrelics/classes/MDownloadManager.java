package fi.metropolia.threedrelics.classes;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import android.util.Log;
import android.view.View;

public class MDownloadManager {
	  private DownloadManager dm = null;
	  private Context c;
	  public static String FOLDER = "3dModelsZipped";
	  public String scene_id;
	  public String model;
	  public Long id;
	public MDownloadManager(Context c,String model,String scene_id){
		this.model = model;
		this.scene_id = scene_id;
		this.c = c;
		this.dm = (DownloadManager)c.getSystemService(Context.DOWNLOAD_SERVICE); 
		
		
	}
	
	
	public void startDownload() {
			Log.d("in start download", "start download");
		    Uri uri=Uri.parse(model);
		    Log.d("url", "start download url" + model);
		  //  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
		  //  Environment.getExternalStorageDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
		    DownloadManager.Request req=new DownloadManager.Request(uri);

		    req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
		       .setAllowedOverRoaming(false)
		       .setTitle(scene_id)
		       .setDestinationInExternalFilesDir(c,FOLDER,scene_id+".zip");
		       //.setDestinationInExternalFilesDir(c,title,title+".zip");
		      // .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"test.mp4");
		   //Log.d("external dir", "external dir" + c.getExternalFilesDir(null));
		   id = dm.enqueue(req);

		  }

    public Long getDownloadId(){
    	return this.id;
    }
}
