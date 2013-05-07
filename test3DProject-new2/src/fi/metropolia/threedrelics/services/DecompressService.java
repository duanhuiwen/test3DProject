package fi.metropolia.threedrelics.services;

import java.io.File;

import fi.metropolia.threedrelics.classes.Decompress;
import fi.metropolia.threedrelics.db.DbEntry;
import fi.metropolia.threedrelics.db.DbHelper;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

public class DecompressService extends Service{
	public static String ZIP_FOLDER = "3dModelsZipped";
	public static String UNZIPPED_FOLDER = "3dModelsUnzipped";
	public static String UNZIP_DESTINATION = "destination";
	public static String ZIPFILE = "zipFile";
	public static String DOWNLOAD_ID = "download_id";
	
	public Context context;
	public String downloadId;
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startid){
		
		Log.d("start to decompress in decomress service", "start to decompress in decomress service!");
        Handler compressHandler;
        HandlerThread handlerThread = new HandlerThread("ht"); 
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        compressHandler = new Handler(looper);
		
		context = this.getApplicationContext();
		
		downloadId = intent.getStringExtra(DOWNLOAD_ID);

		
		SQLiteDatabase db = new DbHelper(context).getWritableDatabase();
		Cursor cursor = db.query(DbEntry.TABLE_NAME, new String[]{DbEntry.COLUMN_NAME_SCENE_ID }, DbEntry.COLUMN_NAME_DOWNLOAD_ID + " = ?", new String[]{downloadId}, null, null, null);
		
		String scene_id = null;
		//String link = null;
		Log.d("cursor count", cursor.getCount()+"");
		if(cursor!=null && cursor.moveToFirst()){
			scene_id = cursor.getString(cursor.getColumnIndex(DbEntry.COLUMN_NAME_SCENE_ID));
		//	link = cursor.getString(cursor.getColumnIndex(DbEntry.COLUMN_NAME_MODEL_PATH));
		}
		
		String destination = context.getExternalFilesDir(null)+ "/" + UNZIPPED_FOLDER +"/" + scene_id;
    	String zipFile = context.getExternalFilesDir(null) + "/"+ZIP_FOLDER +"/" +scene_id+".zip";
    	
    	
    	
    	
    	
        compressHandler.post(new Decompress(this,zipFile, destination + "/",scene_id));
  //      ContentValues unzipped_path = new ContentValues();
    //    unzipped_path.put(DbEntry.COLUMN_NAME_MODEL_PATH, destination);
        // update the path of the unzipped files
      //  db.update(DbEntry.TABLE_NAME, unzipped_path, DbEntry.COLUMN_NAME_DOWNLOAD_ID + "= ?", new String[] {downloadId} );
       
        
		return startid;
		
	}
}
