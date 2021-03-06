package fi.metropolia.threedrelics.receivers;

import fi.metropolia.threedrelics.R;
import fi.metropolia.threedrelics.db.DbEntry;
import fi.metropolia.threedrelics.db.DbHelper;
import fi.metropolia.threedrelics.services.DecompressService;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class DownloadCompleteReceiver extends BroadcastReceiver{

	public static String DOWNLOAD_ID = "download_id";
	@Override
	public void onReceive(Context ctxt, Intent i) {
		//get the download id
		Toast.makeText(ctxt, R.string.download_finish, Toast.LENGTH_LONG).show();
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
