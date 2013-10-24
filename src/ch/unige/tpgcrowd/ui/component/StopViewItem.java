package ch.unige.tpgcrowd.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.model.Stop;

public class StopViewItem extends LinearLayout {

	private Stop stop;
	private LinearLayout fullView;
	private LinearLayout smallView;
	
	public StopViewItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public StopViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StopViewItem(Context context) {
		super(context);
	}
	public StopViewItem(Context context, Stop stop) {
		this(context);
		this.stop = stop;
		
        LayoutInflater  mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.nearby_stop_item, this, true);
        
        fullView = (LinearLayout)findViewById(R.id.stopview_full);
        TextView stopTitle = (TextView)findViewById(R.id.stop_title);
        TextView stopContent = (TextView)findViewById(R.id.stop_content);
        
        smallView = (LinearLayout)findViewById(R.id.stopview_small);
        TextView stopSmallContent = (TextView)findViewById(R.id.stop_small_content);
        
        stopTitle.setText(stop.getStopName());
        stopContent.setText(stop.getDistance() + "m");
        
        stopSmallContent.setText(stop.getDistance() + "m");
        
        fullView.setVisibility(VISIBLE);
        smallView.setVisibility(GONE);
	}
	
	public void extendView(boolean extend) {
        fullView.setVisibility(extend?VISIBLE:GONE);
        smallView.setVisibility(extend?GONE:VISIBLE);
	}
	
	public Stop getStop() {
		return stop;
	}

}
