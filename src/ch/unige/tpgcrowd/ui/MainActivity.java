package ch.unige.tpgcrowd.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TextView;
import ch.unige.tpgcrowd.R;

public class MainActivity extends FragmentActivity {

	private TextView textview;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textview = (TextView) findViewById(R.id.text);
//		
//		ITPGStops stopManager = TPGManager.getStopsManager(getApplicationContext());
//		List<String> codes = new ArrayList<String>();
//		
//		codes.add("CVIN");
//		codes.add("BHET");
//		
//		stopManager.getStopsByCodes(codes, new TPGObjectListener<StopList>() {
//			
//			@Override
//			public void onSuccess(StopList results) {
//			
//				String resultText = "";
//				for (Stop stop : results.getStops()) {
//					resultText += stop.getStopCode() + " - " + stop.getStopName() + "\n";
//				}
//				
//				textview.setText(resultText);
//			}
//			
//			@Override
//			public void onFailure() {
//				textview.setText("error..");
//			}
//		});

		//Small test code - get a thermometer instance and display the stop names
//		Integer departureCode = 218161;
//		ITPGThermometer thermoManager = TPGManager.getThermometerManager(getApplicationContext());
//		
//		thermoManager.getThermometer(departureCode, new TPGObjectListener<Thermometer>() {
//			
//			@Override
//			public void onSuccess(Thermometer results) {
//				String resultText = results.getTimestamp().toString() + " - ";
//				
//				for (Step step : results.getSteps()) {
//					resultText += step.getStop().getStopName();
//				}
//				
//				textview.setText(resultText);
//			}
//			
//			@Override
//			public void onFailure() {
//				textview.setText("error..");
//			}
//		});
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
