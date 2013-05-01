package fi.metropolia.threedrelics.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import fi.metropolia.threedrelics.ShowSingleSceneActivity;
import fi.metropolia.threedrelics.classes.XMLElement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


public class SingleSceneOnClickListener implements OnItemClickListener{
	private ArrayList<HashMap<String, String>> availableScenes = new ArrayList<HashMap<String, String>>();	
	private Activity mActivity;
	
	String title;
	String desc;
	String scene_pic;
	String model;
	String scene_id;
	String date;

	
	
	
	
	String marker_pic_front;
	String marker_pic_back;
	String marker_pic_right;
	String marker_pic_left;
	
	public SingleSceneOnClickListener(Activity a,ArrayList<HashMap<String, String>> availableScenes){
		this.mActivity = a;
		this.availableScenes = availableScenes;
		
	}
	public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		title = (String) availableScenes.get(position).get(XMLElement.TITLE);
		desc = availableScenes.get(position).get(XMLElement.DESC);
		scene_pic = availableScenes.get(position).get(XMLElement.PICTURE);
		model = availableScenes.get(position).get(XMLElement.MODEL);
		scene_id = availableScenes.get(position).get(XMLElement.SCENE_ID);
		date =  availableScenes.get(position).get(XMLElement.DATE);
		marker_pic_front = availableScenes.get(position).get(XMLElement.MARKER_FRONT);
		marker_pic_back = availableScenes.get(position).get(XMLElement.MARKER_BACK);
		marker_pic_right = availableScenes.get(position).get(XMLElement.MARKER_RIGHT);
		marker_pic_left = availableScenes.get(position).get(XMLElement.MARKER_LEFT);
		
		Log.d("scene_pic from onclicklistener","scene_pic from onclicklistener"+ scene_pic);
		Log.d("title", "title"+title);
/*		int i=0;
		while(i<entry_info.size()){
			url[i] = entry_info.get("url"+i);
			
		}
		bundle.putStringArray("assets", url); 
*/
		
		
		
		
	
		
		
		
		Intent in = new Intent(mActivity.getApplicationContext(), ShowSingleSceneActivity.class);
		
		in.putExtra(XMLElement.TITLE, title);
		in.putExtra(XMLElement.DESC, desc);
		in.putExtra(XMLElement.PICTURE, scene_pic);
		in.putExtra(XMLElement.MODEL, model);
		in.putExtra(XMLElement.SCENE_ID, scene_id);
		in.putExtra(XMLElement.DATE, date);
		in.putExtra(XMLElement.MARKER_FRONT, marker_pic_front);
		in.putExtra(XMLElement.MARKER_BACK, marker_pic_back);
		in.putExtra(XMLElement.MARKER_RIGHT, marker_pic_right);
		in.putExtra(XMLElement.MARKER_LEFT, marker_pic_left);
		mActivity.startActivity(in);


		
	}
	
}
