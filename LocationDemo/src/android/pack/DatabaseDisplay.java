package android.pack;

import android.pack.DemoDBAdapter;

import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.ListActivity;
/**
 * This class is used to display information stored in the database for debugging purposes using the main location demo class.
 * 
 * @author silvian
 *
 */
public class DatabaseDisplay extends ListActivity{
	 
	private static final int ACTIVITY_CREATE=0;
	private static final int ACTIVITY_EDIT=1;
	
	private DemoDBAdapter mDbHelper;
    private Cursor mDataCursor;
    private ListActivity myListActivity;
    private Location location;
    
    // This is the Adapter being used to display the list's data.
    SimpleCursorAdapter mAdapter;
    
    // If non-null, this is the current filter the user has provided.
    String mCurFilter;
    
    //TODO: Make it to show database content rather than row name
    String[] fromDataBase = new String[]{MapViewer.getCurrentToll()};
    
    //String[] fromDataBaseContent = new String[]{mDbHelper.fetchAllLocationData().toString()};
	
   /* String[] fromDataBase2 = new String[]{mDbHelper.fetchDataPoint(1).toString(),
    		 mDbHelper.fetchDataPoint(2).toString(),
    		 mDbHelper.fetchDataPoint(3).toString(),
    		 mDbHelper.fetchDataPoint(4).toString(),
    		 mDbHelper.fetchDataPoint(5).toString(),
    		 mDbHelper.fetchDataPoint(6).toString(),
    		 mDbHelper.fetchDataPoint(7).toString(),
    		 mDbHelper.fetchDataPoint(8).toString()};*/
    
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dbviewer);
		
		final LocationDemo myLocation = new LocationDemo();
		
		/*mDbHelper = new DemoDBAdapter(this);
	    mDbHelper.open();    
	    fillData();
	    registerForContextMenu(myListActivity.getListView());*/
		
	//	for(int i = 0; i < fromDataBase.length; i++)
	//	{
	//		fromDataBase [i] = mAdapter.toString();
	//		System.out.println(fromDataBase[i]);
	//	}
		
		//fromDataBase [0]= mDbHelper.fetchDataPointString(location.getTime());
		
		
		
		setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, fromDataBase));
		
		//Testing Toast with ListView:
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
		   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		      // When clicked, show a toast with the TextView text 
		      Toast.makeText(getApplicationContext(), 
		    		  		(CharSequence) (MapViewer.getCurrentToll()), 
		    		  		Toast.LENGTH_SHORT).show();
		    }
		});

		
		
		//Testing Toast Idea:
		/*LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_layout,(ViewGroup) findViewById(R.id.toast_layout_root));

		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText("Hello! This is a custom toast!");

		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();*/
		
	}
	
	
			
}
