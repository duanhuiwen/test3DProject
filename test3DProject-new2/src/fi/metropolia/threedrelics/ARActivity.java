package fi.metropolia.threedrelics;

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


import fi.metropolia.threedrelics.classes.ExtensionFinder;
import fi.metropolia.threedrelics.classes.StaticString;
import fi.metropolia.threedrelics.classes.XMLParser;

public class ARActivity extends MetaioSDKViewActivity 
{

	
	private IGeometry myModel1;

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
				if (poses.size() != 0){
					int COSId = poses.get(0).getCoordinateSystemID();
					myModel1.setCoordinateSystemID(COSId);
					 switch (COSId) {

			            case 1:  myModel1.setRotation(new Rotation(0f, 0f, 0f));
	                     break;
			            case 2:  myModel1.setRotation(new Rotation(0f, (float)Math.PI, 0f));
	                     break;
			            case 3:  myModel1.setRotation(new Rotation(0f,(float)-Math.PI/2, 0f));
	                     break;
			            case 4:  myModel1.setRotation(new Rotation(0f,(float)Math.PI/2, 0f));
	            		 break;
			            default: myModel1.setRotation(new Rotation(0f, 0f, 0f));
	                     break;
			        }					
				}
	
			
			
			

				
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

	
	@Override
	protected void loadContent() 
	{
		try
		{
			

			String path = intent.getStringExtra(StaticString.PATH) ;
			
			//modify the marker image
			XMLParser xmlParser= new XMLParser();
			
			
			//trackingConfigFile = path + "/" + "TrackingData_MarkerlessFast.xml";
			trackingConfigFile =  new ExtensionFinder(path,".xml").getFile();
			String trackingConfigFileFullPath = path + "/" + trackingConfigFile;
		//	xmlParser.modifyXml(trackingConfigFileFullPath, "marker.jpg");
			boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFileFullPath); 
			
			MetaioDebug.log("Markerless tracking data loaded: " + result); 
	        
			// Load all the geometries
			//String metaioManModel = AssetsManager.getAssetPath("Assets3/watermill_test_03.obj");	
	
			
			
			String model = new ExtensionFinder(path,".obj").getFile();
			String modelFullPath = path + "/" + model;
			
			
			
			if (modelFullPath != null) 
			{
				myModel1 = metaioSDK.createGeometry(modelFullPath);
				if (myModel1 != null) 
				{
					
					myModel1.setScale(new Vector3d(0.07f, 0.07f, 0.07f));

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
