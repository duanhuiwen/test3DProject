package fi.metropolia.threedrelics;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.metaio.sdk.MetaioDebug;
import com.metaio.tools.io.AssetsManager;




@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity
{
	
	WebView mWebView;

	/**
	 * Task that will extract all the assets
	 */
	AssetsExtracter mTask;
	
	/**
	 * Progress view
	 */
	View mProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.webview);
		 
		mProgress = findViewById(R.id.progress);
		
		// extract all the assets
		mTask = new AssetsExtracter();
		mTask.execute(0);
		
		MetaioDebug.enableLogging(true);
		
		mWebView = (WebView) findViewById(R.id.webview);
        
        WebSettings settings = mWebView.getSettings();
		
        settings.setRenderPriority(RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setJavaScriptEnabled(true);

		//settings.setBuiltInZoomControls(false);
		//settings.setLoadWithOverviewMode(false);
		//settings.setUseWideViewPort(false);
		//settings.setSupportMultipleWindows(false);
		
	
		
	}
	
	@Override
	public void onBackPressed() 
	{
		// if web view can go back, go back
		if (mWebView.canGoBack())
			mWebView.goBack();
		else
			super.onBackPressed();
	}
	
	private class AssetsExtracter extends AsyncTask<Integer, Integer, Boolean>
	{

		@Override
		protected void onPreExecute() 
		{
			mProgress.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected Boolean doInBackground(Integer... params) 
		{
			try 
			{
				AssetsManager.extractAllAssets(getApplicationContext(), true);
			} 
			catch (IOException e) 
			{
				MetaioDebug.printStackTrace(Log.ERROR, e);
				return false;
			}
			
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) 
		{
			mProgress.setVisibility(View.GONE);
			
			if (result)
			{
				Intent intent = new Intent(getApplicationContext(), MarkerlessTracking.class);
    			startActivity(intent);
			}
			else
			{
				MetaioDebug.log(Log.ERROR, "Error extracting assets, closing the application...");
				finish();
			}
	    }
		
	}
	
	

	    	
	
	
}

