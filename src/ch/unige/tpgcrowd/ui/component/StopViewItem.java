package ch.unige.tpgcrowd.ui.component;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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
	public StopViewItem(Context context, Stop stop, final OnClickListener listener) {
		this(context);
		this.stop = stop;
		
        LayoutInflater  mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.nearby_stop_item, this, true);
        
        fullView = (LinearLayout)findViewById(R.id.stopview_full);
        TextView stopTitle = (TextView)findViewById(R.id.stop_title);
        TextView stopDistance = (TextView)findViewById(R.id.stop_distance);
        
        GridView imageLayout = (GridView)findViewById(R.id.stop_images);
        GridView imageSmallLayout = (GridView)findViewById(R.id.stop_small_images);

        imageLayout.setAdapter(new StopViewAdapter(stop));
        imageSmallLayout.setAdapter(new StopViewAdapter(stop));

        imageLayout.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				listener.onClick(StopViewItem.this);
			}
		});
        imageSmallLayout.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				listener.onClick(StopViewItem.this);
			}
		});
        
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
	
	protected class StopViewAdapter extends BaseAdapter {

		List<String> lineCodes = new ArrayList<String>();
		private LayoutInflater mInflater;
		
		public StopViewAdapter(Stop stop) {
			super();
			
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			for (Connection conn : stop.getConnections()) {
				if (!lineCodes.contains(conn.getLineCode())) {
					lineCodes.add(conn.getLineCode());
				}				
			}
		}
		
		@Override
		public int getCount() {
			return lineCodes.size();
		}

		@Override
		public String getItem(int position) {
			return lineCodes.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final TextView lineIconLayout = (TextView)mInflater.inflate(R.layout.small_line_icon, parent, false);
			//TextView lineIcon = (TextView)lineIconLayout.findViewById(R.id.lineIcon);
			
			lineIconLayout.setText(getItem(position));
			lineIconLayout.setBackgroundColor(ColorStore.getColor(getContext(), getItem(position)));
			
			return lineIconLayout;
		}
		
	}
}
