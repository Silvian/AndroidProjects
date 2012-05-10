package android.pack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import android.pack.DemoDBAdapter;

/**
 * 
 * LocationDemo is the main debugging class which Extends Activity meaning it's an activity type class and implements LocationListener which contains the location API tools.
 * 
 * Please note that some of this class' feature are not fully implemented some of them being experimental implementations which are obsolete under different approaches. Note that the tolls position values are conserved within this class but they could be easily reimplemented to obtain the data from other sources, e.g. xml from DB.
 * @author silvian
 */

public class LocationDemo extends Activity implements LocationListener {
	private static final String TAG = "LocationDemo";
	private static final String[] S = { "Out of Service","Temporarily Unavailable", "Available" };
	private static final int INSERT_ID = Menu.FIRST;
	private static final int INSERT_ID2 = Menu.FIRST + 1;
	private static final int IDENTIFIER = 1;
	private static final int HELLO_ID = 1;
		
	//Predefined Testing Tolls
	private static final int TOLL1_LATITUDE = 53383815;
	private static final int TOLL1_LONGITUDE = -6603765;
	private static final int TOLL2_LATITUDE = 53385750;
	private static final int TOLL2_LONGITUDE = -6602000;
	private static final int TOLL3_LATITUDE = 53382431;
	private static final int TOLL3_LONGITUDE = -6602159;
	
	private static final String TOLL1_NAME = "Maynooth Toll 1";
	private static final String TOLL1_DESCRIPTION = "Testing Toll 1";
	private static final String TOLL2_NAME = "Maynooth Toll 2";
	private static final String TOLL2_DESCRIPTION = "Testing Toll 2";
	private static final String TOLL3_NAME = "Maynooth Toll 3";
	private static final String TOLL3_DESCRIPTION = "Testing Toll 3";
	private static final BasicHttpResponse BasicHttpResponse = null;
	
	private TextView output;
	private LocationManager locationManager;
	private Location location;
	private LocationListener locationListener;
	private String bestProvider;
	private DemoDBAdapter mDbHelper;
	private TextView latituteField;
	private TextView longitudeField;
	

	@Override
	/**
	 * onCreate is the main method calling the layout to be drawn. This method is called when an activity is called.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Bundle bundle = new Bundle();

		// Get the output UI NOT NEEDED ATM
		output = (TextView) findViewById(R.id.output);

		// Get the location manager
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		//TODO: Write logic on to send location from provider to database
		// List all providers:
		List<String> providers = locationManager.getAllProviders();
		
		//locationListener = new GPSLocationListener();
		
		//notification(getToll2Name());
		
		
		HttpData data = HttpRequest.get("http://postgis.cs.nuim.ie:22/");
	    output.append(data.content);
		
		if(postData())
		{
			output.append("data sent succesfully");
		}
		
		else{
			output.append("data failed");
		}
		//Create Database Storage
		
		mDbHelper = new DemoDBAdapter(this);
	    mDbHelper.open();
	     
	    mDbHelper.createLocationData(bestProvider,IDENTIFIER,132352452,121235346,123425346,1123123,11231342,123425);
	    
		//Main iteration which controls the output of the debugger and updates the DB.
		for (String provider : providers) {
			printProvider(provider);
			
			//mDbHelper.updateDataPoint(bestProvider,IDENTIFIER,132352452,121235346,123425346,1123123,11231342,123425);
			
			 data = HttpRequest.post("http://postgis.cs.nuim.ie:22/", "var1=val&var2=val2");
			 output.append(data.content);
			 Enumeration<String> keys = data.cookies.keys(); // cookies
			 while (keys.hasMoreElements()) {
			 		System.out.println(keys.nextElement() + " = " +data.cookies.get(keys.nextElement() + "rn"));
				}
			 Enumeration<String> keys2 = data.headers.keys(); // headers
			 while (keys.hasMoreElements()) {
					System.out.println(keys.nextElement() + " = " +data.headers.get(keys.nextElement() + "rn"));
				}
			
			bundle.putString(DemoDBAdapter.PROVIDER, getProvider(provider));
			bundle.putInt(DemoDBAdapter.IDENTIFIER, getIdentifier());
			bundle.putDouble(DemoDBAdapter.LATITUDE, getLatitude());
			bundle.putDouble(DemoDBAdapter.LONGITUDE, getLongitude());
			bundle.putDouble(DemoDBAdapter.ALTITUDE, getAltitude());
			bundle.putFloat(DemoDBAdapter.SPEED, getSpeed());
			bundle.putFloat(DemoDBAdapter.BEARING, getBearing());
			bundle.putFloat(DemoDBAdapter.ACCURACY, getAccuracy());
			
			
			
			//mDbHelper.open();
		/*	mDbHelper.updateDataPoint(setDbProvider(provider),
									  ""+setDbIdentifier(),
									  ""+setDbLatitude(location),
									  ""+setDbLongitude(location),
									  ""+setDbAltitude(location),
									  ""+setDbSpeed(location),
									  ""+setDbBearing(location),
									  ""+setDbAccuracy(location));*/
			//mDbHelper.close();
		}
		

		Criteria criteria = new Criteria();
		bestProvider = locationManager.getBestProvider(criteria, false);
		output.append("\n\nBEST Provider:\n");
		printProvider(bestProvider);

		output.append("\n\nLocations (starting with last known):");
		location = locationManager.getLastKnownLocation(bestProvider);
		printLocation();
		
		
		//Writing to XML File
		//writeXml(providers);
		
		File newxmlfile = new File(Environment.getExternalStorageDirectory()+"/data_output.xml");
		
		try{
            newxmlfile.createNewFile();
		}catch(IOException e){
            Log.e("IOException", "exception in createNewFile() method");
		}
		
		FileOutputStream fileos = null;        
        try{
                fileos = new FileOutputStream(newxmlfile);
        }catch(FileNotFoundException e){
                Log.e("FileNotFoundException", "can't create FileOutputStream");
        }

		//writeXml2(providers);
		
		try {
			fileos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TextView tv = (TextView)this.findViewById(R.layout.main);
		try{
		  tv.setText("file has been created on SD card");
    	} catch (Exception e) {
            Log.e("Exception","error occurred while creating xml file");
    	}
    	
    	
    	//Write to SD:
    	try {
    	    File root = Environment.getExternalStorageDirectory();
    	    if (root.canWrite()){
    	        File gpxfile = new File(root, "gpxfile.gpx");
    	        FileWriter gpxwriter = new FileWriter(gpxfile);
    	        BufferedWriter out = new BufferedWriter(gpxwriter);
    	        out.write("Hello world");
    	        out.close();
    	    }
    	} catch (IOException e) {
    	    Log.e(TAG, "Could not write file " + e.getMessage());
    	}
    	
		
		mDbHelper.close();
		
	}
	/**
	 * Create Context Options Menu Item
	 */
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.showDB);
        menu.add(0, INSERT_ID2, 0,R.string.map_viewer);
        return true;
    }
	
	@Override
	/**
	 * Execute the start of a new activity when content Menu Item is selected
	 */
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                Intent intent = new Intent(LocationDemo.this,DatabaseDisplay.class);
        		startActivity(intent);
                return true;
                
            case INSERT_ID2:
            	Intent intent2 = new Intent(LocationDemo.this,MapViewer.class);
            	startActivity(intent2);
            	return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

	/** Register for the updates when Activity is in foreground */
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(bestProvider, 20000, 1, this);
	}

	/** Stop the updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}
	
	/** Gets called automatically whenever a change in location is detected*/
	public void onLocationChanged(Location location) {
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		//printLocation();
		//int lat = (int) (location.getLatitude());
		//int lng = (int) (location.getLongitude());
		//latituteField.setText(String.valueOf(lat));
		//longitudeField.setText(String.valueOf(lng));
		
	}
	
	/** Gets called if the provider is no available*/
	public void onProviderDisabled(String provider) {
		// let okProvider be bestProvider
		// re-register for updates
		output.append("\n\nProvider Disabled: " + provider);
	}
	
	/** Gets called if the provider is available*/
	public void onProviderEnabled(String provider) {
		// is provider better than bestProvider?
		// is yes, bestProvider = provider
		output.append("\n\nProvider Enabled: " + provider);
	}

	/** Gets called when provider status is changed */
	public void onStatusChanged(String provider, int status, Bundle extras) {
		output.append("\n\nProvider Status Changed: " + provider + ", Status="
				+ S[status] + ", Extras=" + extras);
	}
	
	/** Prints provider information for debug purpose */
	public void printProvider(String provider) {
		LocationProvider info = locationManager.getProvider(provider);
		output.append(info.toString() + "\n\n");
	}
	
	/** Prints location information for debug purpose */
	public void printLocation() {
		if (location == null)
			output.append("\nLocation[unknown]\n\n");
		else
			output.append("\n\n" + location.toString());
	}
	
	/** Returns the provider */
	public String getProvider(String provider){
		LocationProvider info = locationManager.getProvider(provider);
		return info.toString();
	}
	
	/** Returns the identifier value incremented */
	public int getIdentifier(){
		return IDENTIFIER + 1;
	}
	
	/** Returns the latitude */
	public double getLatitude(){
		if(location == null)
			return 0;
		else
			return location.getLatitude();
	}
	
	/** Returns the longitude */
	public double getLongitude(){
		if(location == null)
			return 0;
		else
			return location.getLongitude();
	}
	
	/** Returns the altitude */
	public double getAltitude(){
		if(location == null || !location.hasAltitude())
			return 0;
		else
			return location.getAltitude();
	}
	
	/** Returns the speed */
	public float getSpeed(){
		if(location == null || !location.hasSpeed())
			return -1;
		else
			return location.getSpeed();
	}
	
	/** Returns the bearing */
	public float getBearing(){
		if(location == null || !location.hasBearing())
			return -1;
		else
			return location.getBearing();
	}
	
	/** Returns the accuracy */
	public float getAccuracy(){
		if(location == null || !location.hasAccuracy())
			return -1;
		else
			return location.getAccuracy();
	}
	
	
	//Return Testing Predefined Tolls Position
	public int getToll1Latitude()
	{
		return TOLL1_LATITUDE;
	}
	
	public int getToll1Longitude()
	{
		return TOLL1_LONGITUDE;
	}
	
	public int getToll2Latitude()
	{
		return TOLL2_LATITUDE;
	}
	
	public int getToll2Longitude()
	{
		return TOLL2_LONGITUDE;
	}
	
	public String getToll1Name()
	{
		return TOLL1_NAME;
	}
	
	public String getToll1Description()
	{
		return TOLL1_DESCRIPTION;
	}
	
	public String getToll2Name()
	{
		return TOLL2_NAME;
	}
	
	public String getToll2Description()
	{
		return TOLL2_DESCRIPTION;
	}
	
	public int getToll3Latitude()
	{
		return TOLL3_LATITUDE;
	}
	
	public int getToll3Longitude()
	{
		return TOLL3_LONGITUDE;
	}
	
	public String getToll3Name()
	{
		return TOLL3_NAME;
	}
	
	public String getToll3Description()
	{
		return TOLL3_DESCRIPTION;
	}
	
	
	/**
	 * XML Writer implementation 1
	 * 
	 */
	
	public String writeXml(List<String> providers){
	    XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8", true);
	        serializer.startTag("", "LocationData");
	        serializer.attribute("", "data", String.valueOf(providers.size()));
	        for (String provider : providers){
	            serializer.startTag("", "LocationPoint");
	            serializer.attribute("", "provider", locationManager.getProvider(provider).toString());
	            serializer.startTag("", "identifier");
	            serializer.text(""+IDENTIFIER +1);
	            serializer.endTag("", "identifier");
	            serializer.startTag("", "body");
	            serializer.text(mDbHelper.fetchAllLocationData().toString());
	            serializer.endTag("", "body");
	            serializer.endTag("", "LocationPoint");
	        }
	        serializer.endTag("", "LocationData");
	        serializer.endDocument();
	        return writer.toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 
	}
	
	
	
	/**
	 * XML Writer implementation 2
	 * 
	 */
	
	public String writeXml2(List<String> providers){
	    XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8", true);
	        serializer.startTag("", "LocationData");
	        serializer.attribute("", "data", String.valueOf(providers.size()));
	        for (String provider : providers){
	            serializer.startTag("", "LocationPoint");
	            serializer.attribute("", "provider", locationManager.getProvider(provider).toString());
	            serializer.startTag("", "identifier");
	            serializer.text(""+getIdentifier());
	            serializer.endTag("", "identifier");
	            serializer.startTag("", "body");
	            serializer.text("" + "\n\n" + location.toString());
	            serializer.endTag("", "body");
	            serializer.endTag("", "LocationPoint");
	        }
	        serializer.endTag("", "LocationData");
	        serializer.endDocument();
	        serializer.flush();
	        return writer.toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 
	}
	
	
	
	/**
	 *  PostData method uses a http client protocol to send data to server
	 * 
	 */
	public boolean postData() {
        // Create a new HttpClient and Post Header  
        HttpClient httpclient = new DefaultHttpClient();  
        HttpPost httppost = new HttpPost("http://postgis.cs.nuim.ie:22/");

        try {  
            // Add your data  
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);  
            nameValuePairs.add(new BasicNameValuePair("Content-Type", "application/soap+xml"));               
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
            StringEntity se = new StringEntity("text/xml","HTTP.UTF_8");

            se.setContentType("text/xml");  
            httppost.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
            httppost.setParams(null);
            
            httppost.setEntity(se);  
            BasicHttpResponse httpResponse = (BasicHttpResponse);
            
            // Execute HTTP Post Request  
            HttpResponse response = httpclient.execute(httppost);  
            httpclient.execute(httppost);
            response.addHeader("HTTPStatus", httpResponse.getStatusLine().toString());

        } catch (ClientProtocolException e) {  
            // TODO Auto-generated catch block  
        	output.append("Client Protocol Exception");
        	return false;
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
        	output.append("I/O Exception");
        	return false;
        }  
        
        return true;
    }

}