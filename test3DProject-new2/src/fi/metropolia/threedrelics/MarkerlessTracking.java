/**
 * Tutorial3.java
 * metaio SDK v4.0.1
 *
 * Created by Arsalan Malik and Anton Fedosov
 * Copyright 2012 metaio GmbH. All rights reserved.
 *
 */

package fi.metropolia.threedrelics;

import java.io.File;
import java.io.FilenameFilter;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

import fi.metropolia.threedrelics.classes.FindCertainExtension;
import fi.metropolia.threedrelics.classes.XMLParser;

public class MarkerlessTracking extends MetaioSDKViewActivity 
{

	
	private IGeometry myModel1;
	private IGeometry myModel2;
	private IGeometry myModel3;
	private IGeometry myModel4;
	private Intent intent;
	String trackingConfigFile;
	

	@Override
	protected int getGUILayout() 
	{
		// TODO: return 0 in case of no GUI overlay
		return R.layout.markerless_tracking; 
	}

	@Override
	public void onDrawFrame() 
	{
		super.onDrawFrame();
		
		if (metaioSDK != null)
	
		{
			// get all detected poses/targets
			TrackingValuesVector poses = metaioSDK.getTrackingValues();
			Log.d("pose size","pose size"+ poses.size());
			//if we have detected one, attach our metaio man to this coordinate system Id
			
/*			if(poses.get(0)!= null){
				Log.d("poses.get(0)!= null","poses.get(0)!= null"+ poses.size());
				
			}*/
			
				if (poses.size() != 0){
					int COSId = poses.get(0).getCoordinateSystemID();
					myModel1.setCoordinateSystemID(COSId);
					 switch (COSId) {
			            case 1:  myModel1.setRotation(new Rotation((float)-Math.PI/2, 0f, 0f));
			                     break;
			            case 2:  myModel1.setRotation(new Rotation((float)-Math.PI/2, (float)Math.PI, 0f));
			                     break;
			            case 3:  myModel1.setRotation(new Rotation((float)-Math.PI/2,(float)-Math.PI/2, 0f));
			                     break;
			            case 4:  myModel1.setRotation(new Rotation((float)-Math.PI/2,(float)Math.PI/2, 0f));
			            		 break;
			            default: myModel1.setRotation(new Rotation(0f, 0f, 0f));
			                     break;
			        }
					
				}
					
				/*					myModel2.setCoordinateSystemID(poses.get(1).getCoordinateSystemID());
			myModel3.setCoordinateSystemID(poses.get(2).getCoordinateSystemID());
			myModel4.setCoordinateSystemID(poses.get(3).getCoordinateSystemID());*/
			
			
			
				//Log.d("poses.size:", ""+poses.size());
				//if(poses.size() >1){
					//mMetaioMan.setCoordinateSystemID(poses.get(1).getCoordinateSystemID());
				//}
					
				
		}
	}

	@Override
	protected void onStart() 
	{
		super.onStart();
		intent = this.getIntent();
		
		
	}

	public void onButtonClick(View v)
	{
		finish();
	}
	
/*	public void onIdButtonClick(View v)
	{
		trackingConfigFile = AssetsManager.getAssetPath("Assets3/TrackingData_Marker.xml");
		MetaioDebug.log("Tracking Config path = "+trackingConfigFile);
		
		boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFile); 
		MetaioDebug.log("Id Marker tracking data loaded: " + result); 
		mMetaioMan.setScale(new Vector3d(2f, 2f, 2f));
	}
	
	public void onPictureButtonClick(View v)
	{
		trackingConfigFile = AssetsManager.getAssetPath("Assets3/TrackingData_PictureMarker.xml");
		MetaioDebug.log("Tracking Config path = "+trackingConfigFile);
		
		boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFile); 
		MetaioDebug.log("Picture Marker tracking data loaded: " + result); 
		mMetaioMan.setScale(new Vector3d(8f, 8f, 8f));

	}
	
	public void onMarkerlessButtonClick(View v)
	{
		trackingConfigFile = AssetsManager.getAssetPath("Assets3/TrackingData_MarkerlessFast.xml");
		MetaioDebug.log("Tracking Config path = "+trackingConfigFile);
	
		boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFile); 
		MetaioDebug.log("Markerless tracking data loaded: " + result); 
		mMetaioMan.setScale(new Vector3d(4f, 4f, 4f));
	}*/
	
	@Override
	protected void loadContent() 
	{
		try
		{
			
			// Load desired tracking data for planar marker tracking
			//trackingConfigFile = AssetsManager.getAssetPath("Assets3/TrackingData_MarkerlessFast.xml");
			String path = intent.getStringExtra("path") ;
			
			//modify the marker image
			XMLParser xmlParser= new XMLParser();
			
			
			//trackingConfigFile = path + "/" + "TrackingData_MarkerlessFast.xml";
			trackingConfigFile =  new FindCertainExtension(path,".xml").getFile();
			String trackingConfigFileFullPath = path + "/" + trackingConfigFile;
		//	xmlParser.modifyXml(trackingConfigFileFullPath, "marker.jpg");
			boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFileFullPath); 
			
			MetaioDebug.log("Markerless tracking data loaded: " + result); 
	        
			// Load all the geometries
			//String metaioManModel = AssetsManager.getAssetPath("Assets3/watermill_test_03.obj");	
	
			
			
			String model = new FindCertainExtension(path,".obj").getFile();
			String modelFullPath = path + "/" + model;
			
			
			
			if (modelFullPath != null) 
			{
				myModel1 = metaioSDK.createGeometry(modelFullPath);
				if (myModel1 != null) 
				{
					// Set geometry properties
					myModel1.setScale(new Vector3d(0.1f, 0.1f, 0.1f));
				//	myModel2.setScale(new Vector3d(0.08f, 0.08f, 0.08f));
				//	myModel3.setScale(new Vector3d(0.08f, 0.08f, 0.08f));
				//	myModel4.setScale(new Vector3d(0.08f, 0.08f, 0.08f));
					MetaioDebug.log("Loaded geometry "+modelFullPath);
				}
				else
					MetaioDebug.log(Log.ERROR, "Error loading geometry: "+modelFullPath);
			}
			
		
		}       
		catch (Exception e)
		{
			
		}
	}
	
  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		this.getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case R.id.get_scenes:
				Intent in = new Intent(this.getApplicationContext(),ShowSceneActivity.class);
				this.startActivity(in);
		}
			
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onGeometryTouched(IGeometry geometry) {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
