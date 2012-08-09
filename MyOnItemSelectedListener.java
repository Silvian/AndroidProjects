package navigation.haptic;


import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class MyOnItemSelectedListener implements OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent,
        View view, int pos, long id) {
    	Toast.makeText(parent.getContext(), "Show me the way to " +
    	          parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
    	utpointdestination.eloc=parent.getItemAtPosition(pos).toString();
    	
    }

    public void onNothingSelected(AdapterView parent) {
      // Do nothing.
    }
}