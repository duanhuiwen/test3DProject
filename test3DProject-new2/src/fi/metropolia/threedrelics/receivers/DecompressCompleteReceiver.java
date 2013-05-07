package fi.metropolia.threedrelics.receivers;

import fi.metropolia.threedrelics.R;
import fi.metropolia.threedrelics.ShowSingleSceneActivity;
import fi.metropolia.threedrelics.db.DbEntry;
import fi.metropolia.threedrelics.db.DbHelper;
import fi.metropolia.threedrelics.listeners.LaunchOnClickListener;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DecompressCompleteReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context c, Intent in) {
		// TODO Auto-generated method stub
	//	Toast.makeText(c, "finished decompress",Toast.LENGTH_LONG ).show();
		
		
//        Cursor myCursor = db.query(DbEntry.TABLE_NAME, new String[]{DbEntry.COLUMN_NAME_MODEL_PATH, DbEntry.COLUMN_NAME_DATE}, DbEntry.COLUMN_NAME_SCENE_ID+"=?", new String[]{scene_id}, null, null, null);
		String path = in.getStringExtra(DbEntry.COLUMN_NAME_MODEL_PATH);
		String scene_id = in.getStringExtra(DbEntry.COLUMN_NAME_SCENE_ID);

		ContentValues cv = new ContentValues();
		SQLiteDatabase db = new DbHelper(c.getApplicationContext()).getWritableDatabase();
		Log.d("in decompress complete receiver", "path is" +path);
		cv.put(DbEntry.COLUMN_NAME_MODEL_PATH,path);
		db.update(DbEntry.TABLE_NAME, cv, DbEntry.COLUMN_NAME_SCENE_ID+ "= ?" , new String[]{ scene_id });
		
	}
}
