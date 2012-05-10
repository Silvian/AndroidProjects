package android.pack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//import android.widget.Toast;

public class FlashLight extends Activity {
    /** Called when the activity is first created. */
   // @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button greenButton= (Button) findViewById(R.id.green_button);
        greenButton.setOnClickListener(new View.OnClickListener() {
			
			//@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(v.getContext(), "Hello!", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(FlashLight.this,GreenFlashLightActivity.class);
				startActivity(intent);
				
			}
		});
        
        Button blueButton = (Button) findViewById(R.id.blue_button);
        blueButton.setOnClickListener(new View.OnClickListener() {
			
			//@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FlashLight.this,BlueFlashLightActivity.class);
				startActivity(intent);
			}
		});
        
    }
}