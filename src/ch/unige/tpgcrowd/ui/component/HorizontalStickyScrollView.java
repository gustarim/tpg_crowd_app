package ch.unige.tpgcrowd.ui.component;

import java.util.List;

import ch.unige.tpgcrowd.model.PhysicalStop;
import ch.unige.tpgcrowd.model.Stop;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

public class HorizontalStickyScrollView extends  HorizontalScrollView {

    private static final int SWIPE_MIN_DISTANCE = 25;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    
	protected static final String TAG = HorizontalStickyScrollView.class.getSimpleName();
	
	public interface StopSelectedListener {
		public void setSelectedStop(Stop stop);
	}
	
	protected StopSelectedListener fragmentListener;
	
	int currentIndex = 0;
	
	protected OnTouchListener touchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			//If the user swipes
			return (mGestureDetector.onTouchEvent(event));
		}
	};

	protected GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){
		
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	move(true);
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	move(false);
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
	});

	public void setStopSelectedListener(StopSelectedListener listener) {
		this.fragmentListener = listener;
	}
	
	private OnClickListener childClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v instanceof StopViewItem) {
				StopViewItem stopView = (StopViewItem)v;
				setSelectedStop(stopView);
				
				if (fragmentListener != null) {
					fragmentListener.setSelectedStop(stopView.getStop());
				}
			}
			else {
				Log.d(TAG, "Error getting event source view.");
			}
		}
	};;

	public HorizontalStickyScrollView(Context context) {
		super(context);
	}

	public HorizontalStickyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setSmoothScrollingEnabled(true);
		this.setOnTouchListener(touchListener);
	}

	public HorizontalStickyScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	protected void setSelectedStop(StopViewItem stop) {
		ViewGroup parent = (ViewGroup) getChildAt(0);
		int nbChild = parent.getChildCount();
		
		//Remove previsously selected stop
		for (int i = 0; i < nbChild; i++) {
			parent.getChildAt(i).setSelected(false);
		}
		
		//Move to display the selected in full view
		while(stop.getId() > currentIndex + 1) {
			move(true);
		}
		while(stop.getId() < currentIndex) {
			move(false);
		}
		
		stop.setSelected(true);
	}
	
	public void move(boolean next) {

		ViewGroup parent = (ViewGroup) getChildAt(0);

		int nbChild = parent.getChildCount();

		StopViewItem view = (StopViewItem)parent.getChildAt(currentIndex);
		
		Log.d("NEXT", "nbChild : " + nbChild + " - current Index : " + currentIndex);
		
		if (next) {
			if (currentIndex < nbChild - 2) {
				LinearLayout.LayoutParams mLayParamCollapse = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
				mLayParamCollapse.weight = 0.15f;
				view.setLayoutParams(mLayParamCollapse);

				view.extendView(false);

				StopViewItem viewToExtend = (StopViewItem)parent.getChildAt(currentIndex + 2);
				LinearLayout.LayoutParams mLayParamExtend = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
				mLayParamExtend.weight = 0.35f;
				viewToExtend.setLayoutParams(mLayParamExtend);
				viewToExtend.extendView(true);
				
				if (currentIndex < nbChild - 3) {
					StopViewItem viewToDisplay = (StopViewItem)parent.getChildAt(currentIndex + 3);
					//Not really needed but just to be sure...
					LinearLayout.LayoutParams mLayParam = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
					mLayParam.weight = 0.15f;
					viewToDisplay.setLayoutParams(mLayParam);
					viewToDisplay.setVisibility(VISIBLE);
					//viewToDisplay.extendView(false);
					
					if (currentIndex > 0) {
						StopViewItem viewToRemove = (StopViewItem)parent.getChildAt(currentIndex - 1);
						viewToRemove.setVisibility(GONE);
					}
				}
				
				currentIndex++;
			}
		}
		else {
			
			if (currentIndex > 0) {
				StopViewItem viewToExtend = (StopViewItem)parent.getChildAt(currentIndex - 1);
				LinearLayout.LayoutParams mLayParamExtend = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
				mLayParamExtend.weight = 0.35f;
				viewToExtend.setLayoutParams(mLayParamExtend);
				viewToExtend.extendView(true);
				
				StopViewItem viewToCollapse = (StopViewItem)parent.getChildAt(currentIndex + 1);
				LinearLayout.LayoutParams mLayParamCollapse = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
				mLayParamCollapse.weight = 0.15f;
				viewToCollapse.setLayoutParams(mLayParamCollapse);
				viewToCollapse.extendView(false);
				
			
				if (currentIndex > 1) {
					StopViewItem viewToDisplay = (StopViewItem)parent.getChildAt(currentIndex - 2);
					//Not really needed but just to be sure...
					LinearLayout.LayoutParams mLayParam = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
					mLayParam.weight = 0.15f;
					viewToDisplay.setLayoutParams(mLayParam);
					viewToDisplay.setVisibility(VISIBLE);
					//viewToDisplay.extendView(false);
					
					if (currentIndex < nbChild - 2) {
						StopViewItem viewToRemove = (StopViewItem)parent.getChildAt(currentIndex + 2);
						viewToRemove.setVisibility(GONE);
					
					}
				}

				currentIndex--;
			}

		}



		//		if(next && prevIndex < nbChild -1) prevIndex++;
		//		else if (!next && prevIndex > 0) prevIndex--;
		//		else return;
		//
		//		View view = parent.getChildAt(prevIndex);
		//
		//		//        DisplayMetrics metrics = new DisplayMetrics();
		//		//        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		//		//        int screenWidth = metrics.widthPixels;
		//
		//		int scrollX = (view.getLeft());        
		//
		//		this.smoothScrollTo(scrollX, 0);
	}


	public void setFakeItems(int nb) {

		ViewGroup parent = (ViewGroup) getChildAt(0);

		parent.removeAllViews();
		parent.setBackgroundColor(Color.BLUE);
		
		for (int i = 0; i < nb; i++) {
			Stop stopTmp = new Stop();
			stopTmp.setStopName("Stop " + i);
			stopTmp.setDistance(20*i);
			
			LinearLayout.LayoutParams layParam = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);

			StopViewItem stView = new StopViewItem(getContext(),stopTmp);
			stView.setId(i);

			stView.setOnClickListener(childClickListener);
			stView.setLongClickable(false);
			stView.setOnTouchListener(touchListener);
			
			if (i > 1) {	
				stView.extendView(false);
				layParam.weight = 0.15f;
			}
			if (i > 3) {
				stView.extendView(false);
				stView.setVisibility(GONE);
				layParam.weight = 0.15f;
			}
			if (i <= 1) {
				layParam.weight = 0.35f;
			}
			
			parent.addView(stView, layParam);
		}
		
		parent.forceLayout();
	}

	public void setAdapter(Context context, ListAdapter mAdapter) {
		fillViewWithAdapter(mAdapter);

	}

	private void fillViewWithAdapter(ListAdapter mAdapter) {
		if (getChildCount() == 0 || mAdapter == null)
			return;

		ViewGroup parent = (ViewGroup) getChildAt(0);

		parent.removeAllViews();

		for (int i = 0; i < mAdapter.getCount(); i++) {
			parent.addView(mAdapter.getView(i, null, parent));
		}
	}

	public void setNearbyStops(List<Stop> stops) {
		ViewGroup parent = (ViewGroup) getChildAt(0);

		parent.removeAllViews();
		//parent.setBackgroundColor(Color.BLUE);
		int id = 0;
		for (Stop stop : stops) {
						
			LinearLayout.LayoutParams layParam = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);

			StopViewItem stView = new StopViewItem(getContext(),stop);
			stView.setId(id);

			stView.setOnClickListener(childClickListener);
			stView.setLongClickable(false);
			stView.setOnTouchListener(touchListener);
			
			if (id > 1) {	
				stView.extendView(false);
				layParam.weight = 0.15f;
			}
			if (id > 3) {
				stView.extendView(false);
				stView.setVisibility(GONE);
				layParam.weight = 0.15f;
			}
			if (id <= 1) {
				layParam.weight = 0.35f;
			}
			
			parent.addView(stView, layParam);
			id += 1;
		}
		
		parent.forceLayout();
	}

}