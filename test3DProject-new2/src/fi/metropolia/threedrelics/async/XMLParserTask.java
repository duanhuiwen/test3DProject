package fi.metropolia.threedrelics.async;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fi.metropolia.threedrelics.R;
import fi.metropolia.threedrelics.ShowSceneActivity;
import fi.metropolia.threedrelics.classes.StaticString;
import fi.metropolia.threedrelics.classes.XMLParser;
import fi.metropolia.threedrelics.db.DbEntry;
import fi.metropolia.threedrelics.db.DbHelper;
import fi.metropolia.threedrelics.listeners.SingleSceneOnClickListener;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.net.URL;
public class XMLParserTask extends AsyncTask<String, Void, String>{
	
	//represent the element in xml

	static final String URL = "http://users.metropolia.fi/~huiwend/3DRelics/test_3d.xml";
	
	

	
	public DbHelper dbHelper;
	public SQLiteDatabase db;
	private boolean ifInsert = false;
	
	 ArrayList<HashMap<String,String>> availableScenes = new ArrayList<HashMap<String, String>>();
	
	 
	XMLParser myParser;

	
	private ShowSceneActivity myListActivity;
	
	
	public XMLParserTask(Activity la){
		myListActivity = (ShowSceneActivity) la;
		
	}
	
	
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
	//	Log.d("inside do in backgroud", "inside do in backgroud");
		myParser = new XMLParser();
		
	//	Log.d("return myParser.getXmlFromUrl(URL): ","return myParser.getXmlFromUrl(URL):"+ myParser.getXmlFromUrl(URL));
		
		return myParser.getXmlFromUrl(URL);
		
		
	}



	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		dbHelper = new DbHelper(myListActivity.getApplicationContext());
		db = dbHelper.getWritableDatabase();
		
		//	Log.d("result", "result not null"+result);

		Document doc = myParser.getDomElement(result); 
		if(doc != null){
		//	Log.d("doc is not null", "doc is not null");
		}
		NodeList nl = doc.getElementsByTagName(StaticString.OBJECT);

		Log.d("nl.getLength() : ","nl.getLength()"+ nl.getLength());
		
		Cursor cur = db.rawQuery("SELECT COUNT(*) FROM scenes", null);
		if (cur != null) {
		    cur.moveToFirst();                       // Always one row returned.
		    if (cur.getInt (0) == 0) {               // Zero count means empty table.
		    	ifInsert = true;
		    }
		}
		
		Log.d("if insert ", String.valueOf(ifInsert));	
		for (int i = 0; i < nl.getLength(); i++) {
			
			
			HashMap<String,String> scene = new HashMap<String,String>();
			
			Element e =  (Element) nl.item(i);
			// adding each child node to HashMap key => value
 			String title = myParser.getValue(e,StaticString.TITLE);
 			//Log.d("title", "title: "+ entry_title);
 			//NodeList models = e.getElementsByTagName("model");
 			//NodeList models = myParser.getValue(e, "model",0);
 			String picture = myParser.getValue(e, StaticString.PICTURE);
 			String desc = myParser.getValue(e, StaticString.DESC);
 			String model = myParser.getValue(e, StaticString.MODEL);
 			String date = myParser.getValue(e,StaticString.DATE);
 			String marker_front = myParser.getValue(e,StaticString.MARKER_FRONT);
 			String marker_back = myParser.getValue(e,StaticString.MARKER_BACK);
 			String marker_right = myParser.getValue(e,StaticString.MARKER_RIGHT);
 			String marker_left = myParser.getValue(e,StaticString.MARKER_LEFT);
 			String scene_id = myParser.getValue(e, StaticString.SCENE_ID);
 		//	HashMap<String,String> entry_info = new HashMap<String,String>();
 			
 			//int n = 0;
 			//Log.d("entry_assets size", "entry_assets:"+models.getLength());
 			
 			//while(n<models.getLength()){
 				//Element ele = (Element) entry_assets.item(n);
 				//String  url= entry_assets
 				//model_url[n] = models.item(n).getNodeValue();
 				// NodeList models = entry_assets.item(n).getChildNodes();
 				//model_url[n]
 				//myParser.getElementValue(models.item(0));
  				//Log.d("model_url", "model_url: "+model);
 				//entry_info.put("url"+n,model_url[n]);
 				//n++;
 			//}
  			Log.d("scene pic from xml :", "scene pic from xml :"+ picture);
  			Log.d("scene pic from xml marker front:", "scene pic from xml marker front:"+ marker_front);
  			
  			Log.d("element desc:", "element desc"+ StaticString.DESC);
 			scene.put(StaticString.TITLE, title);
 			scene.put(StaticString.PICTURE, picture);
 			scene.put(StaticString.DESC, desc);
 			scene.put(StaticString.MODEL, model);
 			scene.put(StaticString.MARKER_FRONT, marker_front);
 			scene.put(StaticString.MARKER_BACK, marker_back);
 			scene.put(StaticString.MARKER_LEFT, marker_left);
 			scene.put(StaticString.MARKER_RIGHT, marker_right);
 			scene.put(StaticString.DATE, date);
 			scene.put(StaticString.SCENE_ID, scene_id);

 			availableScenes.add(scene);
 			//Log.d("scene id from xml task", "scene id from xml task" + scene_id);
 			ContentValues values = new ContentValues();
 			values.put(DbEntry.COLUMN_NAME_SCENE_ID, scene_id);
 			values.put(DbEntry.COLUMN_NAME_DOWNLOAD_ID, "");
 			values.put(DbEntry.COLUMN_NAME_TITLE, title);
 	//		values.put(DbEntry.COLUMN_NAME_MODEL_URL, model);
 			values.put(DbEntry.COLUMN_NAME_MODEL_PATH, "");
 			values.put(DbEntry.COLUMN_NAME_DATE, "");
    //		values.put(DbEntry.COLUMN_NAME_DOWNLOAD_COMPLETE, "false");
 			Cursor c = db.query(DbEntry.TABLE_NAME, new String[]{DbEntry.COLUMN_NAME_TITLE}, DbEntry.COLUMN_NAME_SCENE_ID + "= ?", new String[]{String.valueOf(scene_id)}, null, null, null);
 			//if table is empty insert
 			if(ifInsert == true) db.insert(DbEntry.TABLE_NAME, null, values);
 			//if this is new scene, insert to database
 			else if(c != null){
 				c.moveToFirst();                       
 			    if (c.getInt (0) == 0) {                			    	
 			    	db.insert(DbEntry.TABLE_NAME, null, values);
 			    } 				
 			} 						
		}
//		Log.d("element picture:", "element pic"+ XMLElement.PICTURE);
		myListActivity.updateList(availableScenes);

		
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}


	
	
	
	
	


}
