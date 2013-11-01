package ch.unige.tpgcrowd.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import ch.unige.tpgcrowd.R;

public class CrowdStopFragment extends Fragment {
	
	public static final String EXTRA_PHYSICAL_STOP_CODE = "ch.unige.tpgcrowd.extra.PHYSICAL_STOP_CODE";
	public static final String EXTRA_PHYSICAL_STOP_CROWD = "ch.unige.tpgcrowd.extra.PHYSICAL_STOP_CROWD";
	private String physicalStopCode;
	private int physicalStopCrowd;
	private ImageView crowdImg;
	private SeekBar seekBar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Bundle b = getArguments();
		
		physicalStopCode = b.getString(EXTRA_PHYSICAL_STOP_CODE);
		physicalStopCrowd = b.getInt(EXTRA_PHYSICAL_STOP_CROWD);
		
		final View layout = inflater.inflate(R.layout.crowd_stop, container, false);
		
		crowdImg = (ImageView)layout.findViewById(R.id.crowd_img);
		crowdImg.setImageLevel(physicalStopCrowd);
		
		seekBar = (SeekBar)layout.findViewById(R.id.crowd_seekbar);
		seekBar.setMax(7);
		seekBar.setProgress(physicalStopCrowd);
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//DO NOTHING
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				//DO NOTHING				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				//Change image level
				//TODO: Save crowd value using physicalStopCode
				physicalStopCrowd = progress;
				crowdImg.setImageLevel(physicalStopCrowd);
			}
		});
		
		return layout;
	}

}
