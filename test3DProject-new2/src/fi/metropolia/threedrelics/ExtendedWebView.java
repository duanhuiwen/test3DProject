package fi.metropolia.threedrelics;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.metaio.sdk.MetaioDebug;

public class ExtendedWebView extends WebView 
{

	private Handler mHandler;
	
	public ExtendedWebView(Context context, AttributeSet attrs, int defStyle,
			boolean privateBrowsing) {
		super(context, attrs, defStyle, privateBrowsing);
		// TODO Auto-generated constructor stub
	}

	public ExtendedWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ExtendedWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ExtendedWebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
        	MetaioDebug.log("TOUCH: ARELViewActivity onTouchEvent ACTION_DOWN "+ev);
               //send message to JunaioARViewActivity with different ids to not overwrite first event
               mHandler.sendMessage(mHandler.obtainMessage(1, ev));
               break;

        case MotionEvent.ACTION_UP:
        		MetaioDebug.log("TOUCH: ARELViewActivity onTouchEvent ACTION_UP "+ev);
               //send message to JunaioARViewActivity
               mHandler.sendMessage(mHandler.obtainMessage(2, ev));
               break;
       
        }
		return super.onTouchEvent(ev);
	}
	
	public void setHandler(Handler handler) {
        this.mHandler = handler;
 }
	
}
