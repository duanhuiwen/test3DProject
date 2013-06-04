package fi.metropolia.threedrelics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final Context c = this.getApplicationContext();
		setContentView(R.layout.welcome);
	//	this.getWindow().setBackgroundDrawableResource(R.drawable.back);
		Button bt = (Button) this.findViewById(R.id.welcome_launch);
		bt.setOnClickListener(new Button.OnClickListener(){
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(c, ShowSceneActivity.class);
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				c.startActivity(in);
			}
			
		});
		ViewPager pager = (ViewPager) this.findViewById(R.id.pager);
		pager.setAdapter(new SampleAdapter());
		pager.setOffscreenPageLimit(3);
	}
	
	private class SampleAdapter extends PagerAdapter {
	    @Override
	    public Object instantiateItem(ViewGroup container, int position) {
	    	
	    	

	    	switch(position){
	    	case 0:
	    		View page = getLayoutInflater().inflate(R.layout.page, container, false);
	    		container.addView(page);
	    		return page;
	    	case 1:
	    		View page1 = getLayoutInflater().inflate(R.layout.instruction_pages, container, false);
	    		((TextView)page1.findViewById(R.id.instruction_title)).setText(R.string.step1);
	    		page1.findViewById(R.id.instruction_image).setBackgroundResource(R.drawable.instruction_listview);
	    		 container.addView(page1);
	    		return page1;
	    	case 2:
	    		View page2 = getLayoutInflater().inflate(R.layout.instruction_pages, container, false);
	    		((TextView)page2.findViewById(R.id.instruction_title)).setText(R.string.step2);
	    		page2.findViewById(R.id.instruction_image).setBackgroundResource(R.drawable.instruction_download);
	    		 container.addView(page2);
	    		return page2;
	    	case 3:
	    		View page3 = getLayoutInflater().inflate(R.layout.instruction_pages, container, false);
	    		((TextView)page3.findViewById(R.id.instruction_title)).setText(R.string.step3);
	    		page3.findViewById(R.id.instruction_image).setBackgroundResource(R.drawable.instruction_launch);
	    		 container.addView(page3);
	    		return page3;
	    	default:
	    		View page4 = getLayoutInflater().inflate(R.layout.instruction_pages, container, false);
	    		((TextView)page4.findViewById(R.id.instruction_title)).setText(R.string.step4);
	    		page4.findViewById(R.id.instruction_image).setBackgroundResource(R.drawable.instruction_ar);
	    		 container.addView(page4);
	    		return page4;

	    		
	    	}
		    //  return(page);		    	     
	    }



    	
	    
	    
	    @Override
	    public void destroyItem(ViewGroup container, int position,
	                            Object object) {
	      container.removeView((View)object);
	    }

	    @Override
	    public int getCount() {
	      return(5);
	    }

	    @Override
	    public float getPageWidth(int position) {
	      return(1f);
	    }

	    @Override
	    public boolean isViewFromObject(View view, Object object) {
	      return(view == object);
	    }
	  }
	

}

