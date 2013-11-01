package ch.unige.tpgcrowd.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import ch.unige.tpgcrowd.R;

public class CrowdStopFragment extends Fragment {
	
	public static final String EXTRA_PHYSICAL_STOP_CODE = "ch.unige.tpgcrowd.extra.PHYSICAL_STOP_CODE";
	public static final String EXTRA_PHYSICAL_STOP_CROWD = "ch.unige.tpgcrowd.extra.PHYSICAL_STOP_CROWD";
	private String physicalStopCode;
	private int physicalStopCrowd;
	private ImageView crowdImg;
	private SeekBar seekBar;
	private LinearLayout crowdLayout;
	private LinearLayout btnsLayout;
	
	private final OnClickListener btnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			crowdLayout.setVisibility(LinearLayout.VISIBLE);
			btnsLayout.setVisibility(LinearLayout.GONE);
			if (v.equals(btnAccept)) {
				seekBar.setVisibility(SeekBar.GONE);
				Toast.makeText(getActivity(), R.string.thx_stop_crowd, Toast.LENGTH_SHORT).show();
			}
		}
	};
	private Button btnAccept;
	private Button btnCancel;
	
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
		
		crowdLayout = (LinearLayout)layout.findViewById(R.id.crowd_image_layout);

		btnsLayout = (LinearLayout)layout.findViewById(R.id.crowd_btns_layout);
		
		btnAccept = (Button) btnsLayout.findViewById(R.id.btn_accept);
		btnCancel = (Button) btnsLayout.findViewById(R.id.btn_cancel);
		
		btnAccept.setOnClickListener(btnClickListener);
		btnCancel.setOnClickListener(btnClickListener);
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				crowdLayout.setVisibility(LinearLayout.GONE);
				btnsLayout.setVisibility(LinearLayout.VISIBLE);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {			
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
