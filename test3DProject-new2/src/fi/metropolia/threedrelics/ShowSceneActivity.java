package fi.metropolia.threedrelics;


import java.util.ArrayList;
import java.util.HashMap;


import fi.metropolia.threedrelics.async.XMLParserTask;
import fi.metropolia.threedrelics.classes.SceneAdapter;
import fi.metropolia.threedrelics.listeners.SingleSceneOnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ShowSceneActivity extends Activity {


    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scene);
   
        XMLParserTask myXMLParserTask = new XMLParserTask(this);
        myXMLParserTask.execute();


    }
	
	public void updateList(ArrayList<HashMap<String,String>> list){
		
		ListView myView = (ListView) this.findViewById(R.id.scenes_list);
		ListAdapter adapter = new SceneAdapter(this, list);
	        
		myView.setAdapter(adapter);
		
		myView.setOnItemClickListener(new SingleSceneOnClickListener(this,list));
	}
	public void onDestory(){
		//ShowSingleSceneActivity.this.imageLoader_marker.clearCache();
	}

}
