package android.pack;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class SettingsActivity extends Activity {
	
	private CheckBox compCheckBox, satCheckBox, zoomCheckBox;
	private boolean isCompassMode = true;
	private boolean isSatelliteMode = true;
	private boolean isZoomControlsEnabled = true;
	private SettingsController settingsController = new SettingsController(isCompassMode, 
																			isSatelliteMode, 
																			isZoomControlsEnabled);

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        //set listeners for check box ticks
        addListenerOnCompCheckBox();
        addListenerOnSatCheckBox();
        addListenerOnZoomCheckBox();
        
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		
		SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
		
	    SharedPreferences.Editor editor = sharedPreferences.edit();
	    editor.putBoolean("compCheck", compCheckBox.isChecked());
	    editor.putBoolean("satCheck", satCheckBox.isChecked());
	    editor.putBoolean("zoomCheck", zoomCheckBox.isChecked());
	    
	    editor.putBoolean("compassMode", settingsController.getCompassMode());
	    editor.putBoolean("satelliteMode", settingsController.getSatelliteMode());
	    editor.putBoolean("zoomControlsEnabled", settingsController.getZoomControls());

	    editor.commit();
		
        
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
	    compCheckBox.setChecked(sharedPreferences.getBoolean("compCheck", false));
	    satCheckBox.setChecked(sharedPreferences.getBoolean("satCheck", false));
	    zoomCheckBox.setChecked(sharedPreferences.getBoolean("zoomCheck", false));
	   
	    sharedPreferences.getBoolean("compassMode", settingsController.getCompassMode());
	    sharedPreferences.getBoolean("satelliteMode", settingsController.getSatelliteMode());
	    sharedPreferences.getBoolean("zoomControlsEnabled", settingsController.getZoomControls());
	
	}
	
	private void addListenerOnCompCheckBox(){
		compCheckBox = (CheckBox) findViewById(R.id.compassModeCheck);
		
		compCheckBox.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (((CheckBox) v).isChecked())
					settingsController.setCompassMode(true);
				
				else
					settingsController.setCompassMode(false);
			}
			
		});
	}
	
	private void addListenerOnSatCheckBox(){
		satCheckBox = (CheckBox) findViewById(R.id.satelliteViewCheck);
		
		satCheckBox.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(((CheckBox) v).isChecked())
					settingsController.setSatelliteMode(true);
				
				else
					settingsController.setSatelliteMode(false);
			}
			
		});
	}
	
	private void addListenerOnZoomCheckBox(){
		zoomCheckBox = (CheckBox) findViewById(R.id.zoomControlsCheck);
		
		zoomCheckBox.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(((CheckBox) v).isChecked())
					settingsController.setZoomControls(true);
				
				else
					settingsController.setZoomControls(false);
			}
			
		});
	}
	
	private void saveState(final boolean isChecked) {
	    SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPreferences.edit();
	    editor.putBoolean("check", isChecked);
	    editor.commit();
	}

	private boolean loadState() { 
	    SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
	    return sharedPreferences.getBoolean("check", false);
	}

	
}
