package android.pack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GreenFlashLightActivity extends Activity {

	//@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.green);
		
		Button redButton = (Button)findViewById(R.id.red_button);
		redButton.setOnClickListener(new View.OnClickListener(){

			//@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(v.getContext(), "Hello!", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(GreenFlashLightActivity.this,FlashLight.class);
				startActivity(intent);
				
			}
			
		});
		
		Button blueButton = (Button) findViewById(R.id.blue_button);
        blueButton.setOnClickListener(new View.OnClickListener() {
			
			//@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GreenFlashLightActivity.this,BlueFlashLightActivity.class);
				startActivity(intent);
			}
		});
	}

}
