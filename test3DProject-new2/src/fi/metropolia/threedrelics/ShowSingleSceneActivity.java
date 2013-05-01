package fi.metropolia.threedrelics;


import java.util.HashMap;

import fi.metropolia.threedrelics.classes.Decompress;
import fi.metropolia.threedrelics.classes.ImageLoader;
import fi.metropolia.threedrelics.classes.MyDownloadManager;
import fi.metropolia.threedrelics.classes.XMLElement;
import fi.metropolia.threedrelics.db.DbEntry;
import fi.metropolia.threedrelics.db.DbHelper;
import fi.metropolia.threedrelics.listeners.LaunchOnClickListener;
import fi.metropolia.threedrelics.receivers.DecompressReceiver;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowSingleSceneActivity extends Activity{
	public static String ZIP_FOLDER = "3dModelsZipped";
	public static String UNZIPPED_FOLDER = "3dModelsUnzipped";
	private Context context;
	public DecompressReceiver decompressReceiver;
	final String DECOMPRESS_FINISHED = "decompress_finished";
	private BroadcastReceiver receiveDecompressionNotification;
	public ImageLoader imageLoader = new ImageLoader(this);

	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		context = this.getApplicationContext();
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.scene_detail);
		Intent in = this.getIntent();
		final String title = in.getStringExtra(XMLElement.TITLE);
		String scene_pic = in.getStringExtra(XMLElement.PICTURE);
		String desc = in.getStringExtra(XMLElement.DESC);
		String scene_id = in.getStringExtra(XMLElement.SCENE_ID);
		String marker_pic_front = in.getStringExtra(XMLElement.MARKER_FRONT);
		String marker_pic_back = in.getStringExtra(XMLElement.MARKER_BACK);
		String marker_pic_right = in.getStringExtra(XMLElement.MARKER_RIGHT);
		String marker_pic_left = in.getStringExtra(XMLElement.MARKER_LEFT);
		String dateFromXML = in.getStringExtra(XMLElement.DATE);
		
		String dateFromDb;
		
		final String model = in.getStringExtra(XMLElement.MODEL);
		
		
        TextView sTitle = (TextView) findViewById(R.id.scene_title);
        TextView sDesc = (TextView) findViewById(R.id.scene_desc);
        Button sButton = (Button) findViewById(R.id.scene_button);
        final Button lButton = (Button) findViewById(R.id.launch_button);
        ImageView sImage = (ImageView) findViewById(R.id.scene_pic);
        ImageView mImage_front = (ImageView) findViewById(R.id.marker_pic_front);
        ImageView mImage_back = (ImageView) findViewById(R.id.marker_pic_back);
        ImageView mImage_right = (ImageView) findViewById(R.id.marker_pic_right);
        ImageView mImage_left = (ImageView) findViewById(R.id.marker_pic_left);

        
/*        Log.d("scene pic from show single scene", "scene pic from show single scene" + scene_pic);
        Log.d("title from show single scene", "desc from show single scene" + desc);
        Log.d("desc pic from show single scene", "scene_id from show single scene" + scene_id);*/
   
    //    Bundle bu = in.getExtras();
    //    Log.d("bundle", bu.keySet());
        
        final SQLiteDatabase db = new DbHelper(this.getApplicationContext()).getWritableDatabase();

	//    Log.d("scene id: ", scene_id+"");
        
        Cursor c = db.query(DbEntry.TABLE_NAME, new String[]{DbEntry.COLUMN_NAME_MODEL_PATH, DbEntry.COLUMN_NAME_DATE}, DbEntry.COLUMN_NAME_SCENE_ID+"=?", new String[]{scene_id}, null, null, null);
        
        if(c!=null&&c.moveToFirst()){
        	Log.d("cursor is not null in single activity","cursor is not null in single activity");
        	
        	final String path = c.getString(c.getColumnIndex(DbEntry.COLUMN_NAME_MODEL_PATH));
        	dateFromDb =  c.getString(c.getColumnIndex(DbEntry.COLUMN_NAME_DATE));
        	
        	
        	//if path is not null enable launch button
        	if(!path.trim().equals("")) {
        		lButton.setEnabled(true);
        		lButton.setOnClickListener(new LaunchOnClickListener(this,path));
        	//if date is not same as the date from requested xml, then enable update button	
        		if (dateFromDb.equals(dateFromXML)){
        			sButton.setText(R.string.download_button);
        			sButton.setEnabled(false);
        		}else{
        			sButton.setText(R.string.update_button);
        			sButton.setEnabled(true);
        		}
        	}
        	
        	//if path is null enable download button
        	else {
        		sButton.setText(R.string.download_button);
        		lButton.setEnabled(false);
        	}
        	
        	
        	
        	
        	 
        }
        
        
        
       // sButton.setText(R.string.download_button);
	    //context.registerReceiver(decompressReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
       
        imageLoader.displayImage(scene_pic, sImage,200);   
        imageLoader.displayImage(marker_pic_front, mImage_front, 100);
        imageLoader.displayImage(marker_pic_back, mImage_back, 100);
        imageLoader.displayImage(marker_pic_right, mImage_right, 100);
        imageLoader.displayImage(marker_pic_left, mImage_left, 100);
       // Log.d("scene_pic", ""+scene_pic);
        sTitle.setText(title);
       
        sDesc.setText(desc);
        
        sButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//start download, once the download is finished, it will notify the decompress receiver(registered in manifest) and then triger decompress service
				MyDownloadManager dm = new MyDownloadManager(context, model, title);
				((Button) (ShowSingleSceneActivity.this.findViewById(R.id.scene_button))).setText(R.string.download_button);
				v.setEnabled(false);
				dm.startDownload();
				String downloadId = String.valueOf(dm.getDownloadId()); 
				
				//correlate the download id with specific scene
				ContentValues cv = new ContentValues();
				cv.put(DbEntry.COLUMN_NAME_DOWNLOAD_ID, downloadId);
				db.update(DbEntry.TABLE_NAME, cv, DbEntry.COLUMN_NAME_MODEL_URL+ "= ?" , new String[]{ model });
			}});
        
           receiveDecompressionNotification = new BroadcastReceiver(){

			@Override
			public void onReceive(Context c, Intent in) {
				// TODO Auto-generated method stub
			//	Toast.makeText(c, "finished decompress",Toast.LENGTH_LONG ).show();
				
				
//		        Cursor myCursor = db.query(DbEntry.TABLE_NAME, new String[]{DbEntry.COLUMN_NAME_MODEL_PATH, DbEntry.COLUMN_NAME_DATE}, DbEntry.COLUMN_NAME_SCENE_ID+"=?", new String[]{scene_id}, null, null, null);
				String path = in.getStringExtra("path");
				
				ShowSingleSceneActivity.this.findViewById(R.id.launch_button).setEnabled(true);
				
				ShowSingleSceneActivity.this.findViewById(R.id.launch_button).setOnClickListener(new LaunchOnClickListener(ShowSingleSceneActivity.this, path));
				ShowSingleSceneActivity.this.findViewById(R.id.scene_button).setEnabled(false);
			
			}
        	
        };
        
        // in Decompress.class, we notify the app when it finished, and pass an intent with DECOMPRESS_FINISHED as para 
        IntentFilter intentFilter = new IntentFilter(DECOMPRESS_FINISHED);
        this.registerReceiver(receiveDecompressionNotification, intentFilter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		this.unregisterReceiver(receiveDecompressionNotification);
		super.onDestroy();
	}

}
