package android.pack;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.pack.LocationDemo;

/**
 * Toll is the map activity class which displays the user a map on which toll locations and user location is displayed. GeoUpdateHandler object is called in this class whenever user's location changes.
 * 
 * @author silvian
 *
 */
public class MapViewer extends MapActivity{

	private static final int INSERT_ID = Menu.FIRST;
	private static final int INSERT_ID2 = Menu.FIRST + 1;
	private static final int HELLO_ID = 1;
	private static final long EXPIRATION = 1000;
	private MapView mapView;
	private LocationDemo myLocation;
	private LocationManager locationManager;
	private MapController mapController;
	private MyLocationOverlay myLocationOverlay;
	private int latitude;
	private int longitude;
	private static String currentToll = "No tolls so far";
	
	/**
	 * Is called if route is displayed
	 */
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Main method which calls all other objects and methods 
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapviewer);
		
		MapView mapView = (MapView) findViewById(R.id.toll);
		myLocation = new LocationDemo();
		mapController = mapView.getController();
		
		myLocationOverlay = new MyLocationOverlay(this, mapView);
        mapView.getOverlays().add(myLocationOverlay);
        //myLocationOverlay.enableCompass();
        myLocationOverlay.enableMyLocation();
        
        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                mapController.animateTo(myLocationOverlay.getMyLocation());
            }
        });
        
        
		mapController.setZoom(18);
	    mapView.setClickable(true);
	    mapView.setEnabled(true);
	    mapView.setSatellite(true);
	    mapView.setBuiltInZoomControls(true);
		mapView.setStreetView(true);
		GeoUpdateHandler geoUpdateHandler = new GeoUpdateHandler();
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new GeoUpdateHandler());
		
		 locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
         if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
             createGpsDisabledAlert();
         } else {
             locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                     0,geoUpdateHandler);

         }
         
        
         
        /*
        String ns = Context.NOTIFICATION_SERVICE;
 		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
 		String tollName = "";
 		int icon = R.drawable.icon;        // icon from resources
 		CharSequence tickerText = "Location Demo";              // ticker-text
 		long when = System.currentTimeMillis();         // notification time
 		Context context = getApplicationContext();   	// application Context
 		CharSequence contentTitle = "Location Demo Notifications";  // expanded message title
 		CharSequence contentText = "You are approacting "+tollName;      // expanded message text*/

 		/*Intent notificationIntent = new Intent(this, LocationDemo.class);
 		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

 		//the next two lines initialise the Notification, using the configurations above
 		Notification notification = new Notification(icon, tickerText, when);
 		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
 		
 		
 		mNotificationManager.notify(HELLO_ID, notification);
         
         
        locationManager.addProximityAlert(myLocation.getToll1Latitude(), myLocation.getToll1Longitude(), myLocation.getAccuracy(), EXPIRATION, contentIntent);
        locationManager.addProximityAlert(myLocation.getToll2Latitude(), myLocation.getToll2Longitude(), myLocation.getAccuracy(), EXPIRATION, contentIntent);
        locationManager.addProximityAlert(myLocation.getToll3Latitude(), myLocation.getToll3Longitude(), myLocation.getAccuracy(), EXPIRATION, contentIntent);
        
		*/
		List <Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.toll);
		MapItemizedOverlay itemizedoverlay = new MapItemizedOverlay(drawable,mapView.getContext());
		
		
		GeoPoint point = new GeoPoint(myLocation.getToll1Latitude(), myLocation.getToll1Longitude());
		OverlayItem overlayitem = new OverlayItem(point,myLocation.getToll1Name(),myLocation.getToll1Description());
		
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
		
		GeoPoint point2 = new GeoPoint(myLocation.getToll2Latitude(), myLocation.getToll2Longitude());
		OverlayItem overlayitem2 = new OverlayItem(point2, myLocation.getToll2Name(),myLocation.getToll2Description());
		
		itemizedoverlay.addOverlay(overlayitem2);
		mapOverlays.add(itemizedoverlay);
		
		GeoPoint point3 = new GeoPoint(myLocation.getToll3Latitude(), myLocation.getToll3Longitude());
		OverlayItem overlayitem3 = new OverlayItem(point3, myLocation.getToll3Name(),myLocation.getToll3Description());
		
		itemizedoverlay.addOverlay(overlayitem3);
		mapOverlays.add(itemizedoverlay);
		
	/*	if(myLocation.getLatitude() > myLocation.getToll1Latitude() && myLocation.getLatitude() < myLocation.getToll1Latitude()+10000
				   && myLocation.getLongitude() > myLocation.getToll1Longitude() && myLocation.getLongitude() < myLocation.getToll1Longitude()+10000
				   || myLocation.getLatitude() < myLocation.getToll1Latitude() && myLocation.getLatitude() > myLocation.getToll1Latitude()+10000
				   && myLocation.getLongitude() < myLocation.getToll1Longitude() && myLocation.getLongitude() > myLocation.getToll1Longitude()+10000 ){
					
			        currentToll = myLocation.getToll1Name();
					notification(currentToll);
		}
		
		if(myLocation.getLatitude() > myLocation.getToll2Latitude() && myLocation.getLatitude() < myLocation.getToll2Latitude()+10000
				   && myLocation.getLongitude() > myLocation.getToll2Longitude() && myLocation.getLongitude() < myLocation.getToll2Longitude()+10000
				   || myLocation.getLatitude() < myLocation.getToll2Latitude() && myLocation.getLatitude() > myLocation.getToll2Latitude()+10000
				   && myLocation.getLongitude() < myLocation.getToll2Longitude() && myLocation.getLongitude() > myLocation.getToll2Longitude()+10000 ){
					
					currentToll = myLocation.getToll2Name();
					notification(currentToll);
					
		}
		
		if(myLocation.getLatitude() > myLocation.getToll3Latitude() && myLocation.getLatitude() < myLocation.getToll3Latitude()+10000
				   && myLocation.getLongitude() > myLocation.getToll3Longitude() && myLocation.getLongitude() < myLocation.getToll3Longitude()+10000
				   || myLocation.getLatitude() < myLocation.getToll3Latitude() && myLocation.getLatitude() > myLocation.getToll3Latitude()+10000
				   && myLocation.getLongitude() < myLocation.getToll3Longitude() && myLocation.getLongitude() > myLocation.getToll3Longitude()+10000 ){
					
					currentToll = myLocation.getToll3Name();
					notification(currentToll);
					
		}*/
		
		currentToll = myLocation.getToll1Name();
		notification(currentToll);
		
		currentToll = myLocation.getToll2Name();
		notification(currentToll);
		
		currentToll = myLocation.getToll3Name();
		notification(currentToll);
		
		
		
		
	}
	
	/**
	 * Create Context Options Menu Item
	 */
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.showDB);
        menu.add(0, INSERT_ID2, 0,R.string.debug);
        return true;
    }
	
	@Override
	/**
	 * Execute the start of a new activity when content Menu Item is selected
	 */
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                Intent intent = new Intent(MapViewer.this,DatabaseDisplay.class);
        		startActivity(intent);
                return true;
                
            case INSERT_ID2:
            	Intent intent2 = new Intent(MapViewer.this,LocationDemo.class);
            	startActivity(intent2);
            	return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }
	
	public static String getCurrentToll()
	{
		return currentToll;
	}
	
	
	/**
	 * Notification Method allows for location notifications to be displayed
	 */
	public String notification(String tollName){
		
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
		
		tollName = myLocation.getToll1Name();
			locationManager.addProximityAlert(myLocation.getToll1Latitude(), myLocation.getToll1Longitude(), myLocation.getAccuracy(), EXPIRATION, contentIntent);
			return tollName;
	}
	
	/**
	 * GPSDisabbledAlert Method allows displays an alert message if GPS is not turned on
	 */
	 private void createGpsDisabledAlert() {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder
                 .setMessage(
                         "Your GPS is disabled! Would you like to enable it?")
                 .setCancelable(false).setPositiveButton("Enable GPS",
                         new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {
                                 showGpsOptions();
                             }
                         });
         builder.setNegativeButton("Do nothing",
                 new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                         dialog.cancel();
                     }
                 });
         AlertDialog alert = builder.create();
         alert.show();
     }
	 
	 /**
	  * GPSOption Method displays options to turn on the GPS if it's not turned on
	  */
	 private void showGpsOptions() {
         Intent gpsOptionsIntent = new Intent(
                 android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
         startActivity(gpsOptionsIntent);
     } 
	
}

