package android.pack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView;
import java.util.Random;

public class RandomGenerator extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
       TextView tv = new TextView(this);
       tv.setText("Random Number: " + randGenerate());
       setContentView(tv);
        
       Button generate = (Button) findViewById(R.id.generate_button);
        
    }
    
    public int randGenerate(){
    	
    	Random generator = new Random();
    	
    	return generator.nextInt(100);
    }
}
