package android.pack;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.pack.MapViewer;

/**
 * This class handles the update in real time of the map and gps location. It is utilized by the Toll.java
 * @author silvian
 *
 */
public class GeoUpdateHandler extends LocationDemo implements LocationListener {
	
	private static final long EXPIRATION = 1000;
	private TextView latituteField;
	private TextView longitudeField;
	private static final int HELLO_ID = 1;
	private boolean mAccuracyOverride;
	private float mLastAccuracy;
	private boolean mOverrideLocation;
	private long mTimeBetweenLocationEvents;
	private long mTimeOfLastLocationEvent;
	private LocationDemo myLocation;
	private LocationManager locationManager;
	 
	//constants
    private static final float INVALID_ACCURACY = 999999.0f;
    private static final String TAG = "GPSlocationListener";


    /**
     * This method automatically called when location changes by the main class Toll.java
     */
	public void onLocationChanged(Location location, long timeBetweenLocationEvents, boolean accuracyOverride) {
		int lat = (int) (location.getLatitude());
		int lng = (int) (location.getLongitude());
		MapView mapView;
		MapController mapController;
		mapView = (MapView) findViewById(R.id.toll);
		mapView.setBuiltInZoomControls(true);
		mapView.setStreetView(true);
		mAccuracyOverride = accuracyOverride;
	    mLastAccuracy = INVALID_ACCURACY;
	    mOverrideLocation = false;
	    mTimeBetweenLocationEvents = timeBetweenLocationEvents;
		
		
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new GeoUpdateHandler());
		
		 locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
		//locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		
		
		mapController = mapView.getController();
		mapController.setZoom(14); // Zoon 1 is world view
		
		
		GeoPoint point = new GeoPoint(lat, lng);
		mapController.animateTo(point); //	
		mapController.setCenter(point);
		
		latituteField.setText(String.valueOf(lat));
		longitudeField.setText(String.valueOf(lng));
		
		/* if (location != null)
	        {
	            //if a more accurate coordinate is available within a set of events, then use it (if enabled by programmer)
	            if (mAccuracyOverride == true)
	            {
	                //whenever the expected time period is reached invalidate the last known accuracy
	                // so that we don't just receive better and better accuracy and eventually risk receiving
	                // only minimal locations
	                if (location.getTime() - mTimeOfLastLocationEvent >= mTimeBetweenLocationEvents)
	                {
	                    mLastAccuracy = INVALID_ACCURACY;
	                }


	                if (location.hasAccuracy())
	                {
	                    final float fCurrentAccuracy = location.getAccuracy();

	                    //the '<' is important here to filter out equal accuracies !
	                    if ((fCurrentAccuracy != 0.0f) && (fCurrentAccuracy < mLastAccuracy))
	                    {
	                        mOverrideLocation = true;
	                        mLastAccuracy = fCurrentAccuracy;
	                        
	                    }
	                }
	            }*/
	            
	    
		Intent intent = new Intent(this, DatabaseDisplay.class);
		intent.setAction("PlacesProximityHandlerService");
		intent.putExtra("lat", myLocation.getToll1Latitude());
		intent.putExtra("lon", myLocation.getToll1Longitude());
		intent.putExtra("error_m", myLocation.getAccuracy()+EXPIRATION);
		PendingIntent sender=PendingIntent.getService(this, 0, intent, 0);
		
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		
		int icon = R.drawable.icon;        // icon from resources
		CharSequence tickerText = "Location Demo";              // ticker-text
		long when = System.currentTimeMillis();         // notification time
		Context context = getApplicationContext();   	// application Context
		CharSequence contentTitle = "Location Demo Notifications";  // expanded message title
		CharSequence contentText = "You are approacting "+MapViewer.getCurrentToll();      // expanded message text*/
		
		
		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText, sender);
		
		
		mNotificationManager.notify(HELLO_ID, notification);
		locationManager.addProximityAlert(myLocation.getToll1Latitude(), myLocation.getToll1Longitude(),myLocation.getAccuracy()+EXPIRATION, -1, sender);
		
		mNotificationManager.notify(HELLO_ID, notification);
		locationManager.addProximityAlert(myLocation.getToll2Latitude(), myLocation.getToll2Longitude(),myLocation.getAccuracy()+EXPIRATION, -1, sender);
		
		mNotificationManager.notify(HELLO_ID, notification);
		locationManager.addProximityAlert(myLocation.getToll3Latitude(), myLocation.getToll3Longitude(),myLocation.getAccuracy()+EXPIRATION, -1, sender);
				
		
		onLocationChanged(location);
	}
	

	public MapView findViewById(int mapview) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
	/**
	 *  This method implements the notification system in android to display notification regarding tolls that the user has passed through
	 */
	public void notification(String tollName){
		
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		
		int icon = R.drawable.icon;        // icon from resources
		CharSequence tickerText = "Location Demo";              // ticker-text
		long when = System.currentTimeMillis();         // notification time
		Context context = getApplicationContext();   	// application Context
		CharSequence contentTitle = "Location Demo Notifications";  // expanded message title
		CharSequence contentText = "You are approacting "+tollName;      // expanded message text*/

		Intent notificationIntent = new Intent(this, LocationDemo.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		//the next two lines initialise the Notification, using the configurations above
		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		
		mNotificationManager.notify(HELLO_ID, notification);
		
		if(tollName == myLocation.getToll1Name())
			locationManager.addProximityAlert(myLocation.getToll1Latitude(), myLocation.getToll1Longitude(), myLocation.getAccuracy(), EXPIRATION, contentIntent);
		
		if(tollName == myLocation.getToll2Name())
			locationManager.addProximityAlert(myLocation.getToll2Latitude(), myLocation.getToll2Longitude(), myLocation.getAccuracy(), EXPIRATION, contentIntent);
		
		if(tollName == myLocation.getToll3Name())
			locationManager.addProximityAlert(myLocation.getToll3Latitude(), myLocation.getToll3Longitude(), myLocation.getAccuracy(), EXPIRATION, contentIntent);
		
	}
	
}