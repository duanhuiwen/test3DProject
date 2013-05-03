package fi.metropolia.threedrelics.listeners;

import fi.metropolia.threedrelics.activities.MarkerlessTrackingActivity;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


public class LaunchOnClickListener implements OnClickListener{

	//@Override
	private String path;
	private Activity activity;
	public LaunchOnClickListener(Activity a,String path){
		this.path = path;
		this.activity = a;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent in = new Intent(activity.getApplicationContext(), MarkerlessTrackingActivity.class);
		in.putExtra("path", path);
		activity.startActivity(in);
		Log.d("start launch", "start launch");
	}

}
