/**
 * MetaioSDKViewActivity.java
 * metaio SDK v4.0.1
 * 
 * Created by Arsalan Malik on 03.11.2011
 * Copyright 2012 metaio GmbH. All rights reserved.

 */
package fi.metropolia.threedrelics;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.MetaioSurfaceView;
import com.metaio.sdk.SensorsComponentAndroid;
import com.metaio.sdk.jni.ERENDER_SYSTEM;
import com.metaio.sdk.jni.ESCREEN_ROTATION;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKAndroid;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.MetaioSDK;
import com.metaio.sdk.jni.Vector2di;
import com.metaio.tools.Memory;

import fi.metropolia.threedrelics.classes.StaticString;


//base class of metaio sdk
public abstract class MetaioSDKViewActivity extends Activity implements MetaioSurfaceView.Callback, OnTouchListener
{    
	static 
	{     
		IMetaioSDKAndroid.loadNativeLibs();
	} 

	
	/**
	 * Sensor manager
	 */
	protected SensorsComponentAndroid mSensors;
	
	/**
	 *  OpenGL View
	 */
	protected MetaioSurfaceView mSurfaceView;

	/**
	 * GUI overlay, only valid in onStart and if a resource is provided in getGUILayout.
	 */
	protected View mGUIView;
	
	/**
	 * metaioSDK object
	 */
	protected IMetaioSDKAndroid metaioSDK;
	
	/** 
	 * flag for the renderer
	 */
	protected boolean mRendererInitialized;
      
	/**
	 * Wake lock to avoid screen time outs.
	 * <p> The application must request WAKE_LOCK permission.
	 */
	protected PowerManager.WakeLock mWakeLock;
	
	/**
	 * metaio SDK callback handler
	 */
	private IMetaioSDKCallback mHandler;
	
	/**
	 * Camera image resolution
	 */
	protected Vector2di mCameraResolution;
	
	/**
	 * Renderer viewport resolution
	 */
	protected Vector2di mRendererResolution;

	/**
	 * enable/disable see-through mode
	 */
	protected boolean mSeeThrough;
	
	/**
	 * Provide resource for GUI overlay if required.
	 * <p> The resource is inflated into mGUIView which is added in onStart
	 * @return Resource ID of the GUI view
	 */
	protected abstract int getGUILayout();
	
	/**
	 * Provide SDK callback handler if desired. 
	 * 
	 * @return Return sdk callback handler
	 */
	protected abstract IMetaioSDKCallback getMetaioSDKCallbackHandler();
	
	/**
	 * Load contents to sdk in this method, e.g. tracking data,
	 * geometries etc.
	 */
	protected abstract void loadContent();
	
	/**
	 * Called when a geometry is touched.
	 * 
	 * @param geometry Geometry that is touched
	 */
	protected abstract void onGeometryTouched(IGeometry geometry);
	 
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		MetaioDebug.log("MetaioSDKViewActivity.onCreate(){");
		metaioSDK = null;
		mSurfaceView = null;
		mRendererInitialized = false;
		mRendererResolution = null;
		mHandler = null;
		mSeeThrough = false;
		
		try
		{

			mSensors = new SensorsComponentAndroid( getApplicationContext() );
			

			
		

			metaioSDK = MetaioSDK.CreateMetaioSDKAndroid(this, StaticString.SIGNATURE);
			metaioSDK.registerSensorsComponent(mSensors);
			metaioSDK.setSeeThrough(mSeeThrough);
			
			// Inflate GUI view if provided
			mGUIView = View.inflate(this, getGUILayout(), null);
		}
		catch (Exception e)
		{
			//MetaioDebug.log(Log.ERROR, "Error creating metaio SDK");
			Log.d("Error creating metaio SDK","Error creating metaio SDK:" + e.getMessage());
			finish();
		}
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, getPackageName());
		
	}
	
	
	@Override
	protected void onStart() 
	{
		super.onStart();
		Log.d("MetaioSDKViewActivity.onstart: thread: ",""+Thread.currentThread().getId());
		try 
		{
			mSurfaceView = null;
			
  
			if (metaioSDK != null)
			{
				// Set empty content view
				setContentView(new FrameLayout(this));
				
				// according to nexus s screen size
				mCameraResolution = metaioSDK.startCamera(0, 800, 480);
				

				// Add GL Surface view
				mSurfaceView = new MetaioSurfaceView(this, mSeeThrough);
				Log.d("register callback","register callback");
			
				Log.d("config surfaceview","config surfaceview");
				mSurfaceView.setKeepScreenOn(true);
				mSurfaceView.setOnTouchListener(this);
	
				// Get layout params that stretches surface view to entire screen while keeping aspect ratio.
				// Pass false, to fit entire surface view in the center of the parent.
				// Determine automatically if the activity is running in landscape or portrait orientation
				final boolean portrait = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
				FrameLayout.LayoutParams params = mSurfaceView.getLayoutParams(mCameraResolution, getWindowSize(), true, portrait);
				
			
				Log.d("add surface view","add surface view");
				addContentView(mSurfaceView, params);
				mSurfaceView.setZOrderMediaOverlay(true);
				mSurfaceView.registerCallback(this);
				
			}
			
			// If GUI view is inflated, add it
	   		if (mGUIView != null)
	   		{
	   			Log.d("load gui","load gui");
		   		addContentView(mGUIView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		   		mGUIView.bringToFront();
	   		}
	  
		} catch (Exception e) {
			//MetaioDebug.log(Log.ERROR, "Error creating views: "+e.getMessage());
			Log.d("Error creating views:", "Error creating views:");
		}

	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		Log.d("MetaioSDKViewActivity.onPause()","MetaioSDKViewActivity.onPause()");

		if (mWakeLock != null)
			mWakeLock.release();
		
		// pause the sdk surface
		if (mSurfaceView != null)
			mSurfaceView.onPause();
		
		if (metaioSDK != null)
			metaioSDK.pause();
		
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		MetaioDebug.log("MetaioSDKViewActivity.onResume(){");
		
		if (mWakeLock != null)
			mWakeLock.acquire();
		
		
		if (mSurfaceView != null)
			mSurfaceView.onResume();
	
		if (metaioSDK != null)
			metaioSDK.resume();
		
		
	}

	@Override
	protected void onStop() 
	{
		super.onStop();
		
		Log.d("MetaioSDKViewActivity.onStop()","MetaioSDKViewActivity.onStop()");
		
		if (metaioSDK != null)
		{
			// Disable the camera
			metaioSDK.stopCamera();
		}
		
		if (mSurfaceView != null)
		{
			ViewGroup v = (ViewGroup) findViewById(android.R.id.content);
			v.removeAllViews();
		}
		
		
		System.runFinalization();
		System.gc();
		
		
	} 

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		
		MetaioDebug.log("MetaioSDKViewActivity.onDestroy");
		
		if (metaioSDK != null) {
			metaioSDK.delete();
			metaioSDK = null;
		}
		
		if (mSensors != null)
		{
			mSensors.release();
			mSensors.registerCallback(null);
			mSensors.delete();
			mSensors = null;
		}
		
		Memory.unbindViews(findViewById(android.R.id.content));
		
		System.runFinalization();
		System.gc();
		
		
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		super.onConfigurationChanged(newConfig);
		
		MetaioDebug.log("onConfigurationChanged: "+newConfig.toString());
		
		updateLayout(0);
		
	}
	
	/**
	 * Set screen rotation
	 */
	private void setScreenRotation()
	{
		MetaioDebug.log(Log.INFO, "getResources().getConfiguration().orientation = "+getResources().getConfiguration().orientation);
		
		MetaioDebug.log(Log.INFO, "getWindowManager().getDefaultDisplay().getRotation() = "+getWindowManager().getDefaultDisplay().getRotation());
		
		ESCREEN_ROTATION screenRotation = ESCREEN_ROTATION.ESCREEN_ROTATION_0;
		
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			if (getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_90)
				screenRotation = ESCREEN_ROTATION.ESCREEN_ROTATION_90;
			else
				screenRotation = ESCREEN_ROTATION.ESCREEN_ROTATION_270;
		else 
		{
			if (getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_270)
				screenRotation = ESCREEN_ROTATION.ESCREEN_ROTATION_180;
			else if (getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_180)
				screenRotation = ESCREEN_ROTATION.ESCREEN_ROTATION_180;
			else
				screenRotation = ESCREEN_ROTATION.ESCREEN_ROTATION_0;
		}
		
		MetaioDebug.log(Log.INFO, "Setting camera rotation to "+screenRotation);
		
		metaioSDK.setScreenRotation(screenRotation);
		
	}
	
	/**
	 * Get window size that is available to draw the views
	 * @return Window rectangle
	 */
	private Rect getWindowSize()
	{
		Rect rect = new Rect(); 
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect); 
        return rect;
	}
	
	/**
	 * Update surface view layout according to screen orientation
	 * @param delay delay in millisecond
	 */
	private void updateLayout(long delay)
	{
		try
		{	Log.d("update layout", "update layout");
			mSurfaceView.queueEvent(new Runnable()
			{
				@Override
				public void run() {
					
					setScreenRotation();
					
				};
				
			});
			
			
			mSurfaceView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					final boolean portrait = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);

					MetaioDebug.log("Portrait: "+portrait);
					
					FrameLayout.LayoutParams params = mSurfaceView.getLayoutParams(mCameraResolution, getWindowSize(), true, portrait);
					mSurfaceView.setLayoutParams(params);
					
				}
			}, delay);
			
			
		}
		catch (Exception e)
		{
			Log.d("update layout:","update layout:"+e.getMessage());
		}
	}
	

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_UP) 
		{
			MetaioDebug.log("MetaioSDKViewActivity touched at: "+event.toString());
			Log.d("ontouch","ontouch");
			try
			{
				final int x = (int) ((event.getX() * mRendererResolution.getX())/mSurfaceView.getWidth());
				final int y = (int) ((event.getY() * mRendererResolution.getY())/mSurfaceView.getHeight());
				
				// ask the SDK if a geometry has been hit
				IGeometry geometry = metaioSDK.getGeometryFromScreenCoordinates(x, y, true);
				if (geometry != null) 
				{
					MetaioDebug.log("MetaioSDKViewActivity geometry found: "+geometry);
					onGeometryTouched(geometry);
				}
			}
			catch (Exception e)
			{
				MetaioDebug.log(Log.ERROR, "onTouch: "+e.getMessage());
			}
			
		}
		
		
		return true;
	}
	

	@Override
	public void onSurfaceCreated() 
	{
		
		Log.d("MetaioSDKViewActivity.onstart: onSurfaceCreated: ",""+Thread.currentThread().getId());
		try
		{
			// initialized the renderer
			if(!mRendererInitialized && mSurfaceView!= null)
			{
				mRendererResolution = new Vector2di(mSurfaceView.getWidth(), mSurfaceView.getHeight());
				
				Log.d("init renderer","init renderer");
				metaioSDK.initializeRenderer(	mRendererResolution.getX(),
						                        mRendererResolution.getY(),
												ERENDER_SYSTEM.ERENDER_SYSTEM_OPENGL_ES_2_0 );
				Log.d("render initialized","render initialized");
				
				loadContent();
				Log.d("load content","load content");
				mRendererInitialized=true;
			}
			else
			{
		
				Log.d("MetaioSDKViewActivity.onSurfaceCreated: Reloading textures...","MetaioSDKViewActivity.onSurfaceCreated: Reloading textures...");
				metaioSDK.reloadTextures();
			}
	
			updateLayout(500);
			


		}
		catch (Exception e)
		{
			//MetaioDebug.log(Log.ERROR, "MetaioSDKViewActivity.onSurfaceCreated: "+e.getMessage());
			Log.d("MetaioSDKViewActivity.onSurfaceCreated: ", "MetaioSDKViewActivity.onSurfaceCreated: "+e.getMessage());
		}
 	}

 
	@Override
	public void onDrawFrame() 
	{
		try 
		{	
			Log.d("ondraw", "ondraw");
			// render the the results
			if (mRendererInitialized && mSurfaceView != null)
				metaioSDK.render();

		}
		catch (Exception e)
		{
			Log.d("on draw frame","on draw frame"+e.getMessage());
		}  
	}

	
	@Override
	public void onSurfaceDestroyed() 
	{
		
		Log.d("MetaioSDKViewActivity.onSurfaceDestroyed()", "MetaioSDKViewActivity.onSurfaceDestroyed()");
		
		mSurfaceView = null;
		//metaioSDK.delete();
	}

	@Override
	public void onSurfaceChanged() 
	{		
		Log.d("MetaioSDKViewActivity.onSurfaceChanged()", "MetaioSDKViewActivity.onSurfaceChanged()");
	}
 	  
	@Override
	public void onScreenshot(Bitmap bitmap) 
	{
	}	
}
