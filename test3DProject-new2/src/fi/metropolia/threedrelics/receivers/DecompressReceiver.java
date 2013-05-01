package fi.metropolia.threedrelics.receivers;

import fi.metropolia.threedrelics.services.DecompressService;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DecompressReceiver extends BroadcastReceiver{
	public static String ZIP_FOLDER = "3dModelsZipped";
	public static String UNZIPPED_FOLDER = "3dModelsUnzipped";
	public static String UNZIP_DESTINATION = "destination";
	public static String ZIPFILE = "zipFile";
	public static String DOWNLOAD_ID = "download_id";
	@Override
	public void onReceive(Context ctxt, Intent i) {
		//get the download id
		Long id = i.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,0);
		String idString = String.valueOf(id);
		
    	Intent intent = new Intent(ctxt ,DecompressService.class);
    	//intent.putExtra("title", title);
    //	intent.putExtra(UNZIP_DESTINATION, destination);
   // 	intent.putExtra(ZIPFILE, zipFile);
    	intent.putExtra(DOWNLOAD_ID, idString);
        ctxt.startService(intent);
    //	Decompress myDecompress = new Decompress(zipFile, destination);	
    //	myDecompress.unzip();
	}

}
