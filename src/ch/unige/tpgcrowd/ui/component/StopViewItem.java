package ch.unige.tpgcrowd.ui.component;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.model.Connection;
import ch.unige.tpgcrowd.model.Stop;
import ch.unige.tpgcrowd.util.ColorStore;

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
        TextView stopDistance = (TextView)findViewById(R.id.stop_distance);
        
        GridLayout imageLayout = (GridLayout)findViewById(R.id.stop_images);
        GridLayout imageSmallLayout = (GridLayout)findViewById(R.id.stop_small_images);
        
        Set<String> lineCodes = new HashSet<String>();
        
        for (Connection conn : stop.getConnections()) {
        	
        	if (!lineCodes.contains(conn.getLineCode())) {
    			final TextView lineIcon = (TextView)mInflater.inflate(R.layout.small_line_icon, null, false);
    			final TextView lineIconSmall = (TextView)mInflater.inflate(R.layout.small_line_icon, null, false);
    			
    			lineIcon.setText(conn.getLineCode());
    			lineIcon.setBackgroundColor(ColorStore.getColor(context, conn.getLineCode()));
    			lineIconSmall.setText(conn.getLineCode());
    			lineIconSmall.setBackgroundColor(ColorStore.getColor(context, conn.getLineCode()));
    			
    			imageLayout.addView(lineIcon);
    			imageSmallLayout.addView(lineIconSmall);
    			
    			lineCodes.add(conn.getLineCode());
        	}
        	

		}
        
        smallView = (LinearLayout)findViewById(R.id.stopview_small);
        TextView stopSmallContent = (TextView)findViewById(R.id.stop_small_content);
        
        stopTitle.setText(stop.getStopName());
        stopDistance.setText(stop.getDistance() + "m");
        
        stopSmallContent.setText(stop.getDistance() + "");
        
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
