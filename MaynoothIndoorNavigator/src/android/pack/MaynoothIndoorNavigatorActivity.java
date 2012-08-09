package android.pack;

import java.io.IOException;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.os.Bundle;

import com.google.android.maps.MapView.LayoutParams;  

import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MaynoothIndoorNavigatorActivity extends MapActivity {
	
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
    private GeoPoint p;
    private MyLocationOverlay myLocationOverlay;
    private LocationListener locationListener;
    private MyOverlays itemizedoverlay;
    private SettingsController settings = new SettingsController();
    private String [] coordinates = {"53.383149", "-6.602446"};
    private ScanController scanController = new ScanController();
    private GeoUpdateHandler geoUpdate = new GeoUpdateHandler();
    private static final int SETTINGS_ID = Menu.FIRST;
    private static final int SCANNER_ID = Menu.FIRST + 1;
    
	
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Configure Map
        mapView = (MapView) findViewById(R.id.mapView);
        LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.zoom);
        
        mapView.setBuiltInZoomControls(settings.getZoomControls());
        mapView.setSatellite(settings.getSatelliteMode());
        
        mapController = mapView.getController();
		mapController.setZoom(14); // Zoon 1 is world view
		
		locationListener = new GeoUpdateHandler();
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new GeoUpdateHandler());

		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);

		myLocationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				mapView.getController().animateTo(myLocationOverlay.getMyLocation());
			}
		});

		Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
		itemizedoverlay = new MyOverlays(this, drawable);
		createMarker();
        
        //Adding touch output MapOverlay
        MapOverlay myOverlay = new MapOverlay();
        mapView.getOverlays().add(myOverlay);
      
        GeoPoint callanPoint = new GeoPoint((int) (Double.parseDouble(coordinates[0])*1E6),
        								 (int) (Double.parseDouble(coordinates[1])*1E6));
        
        createMarkerOverlay(R.drawable.pin, callanPoint, "Callan Building Maynooth");
        
        //Adding Marker Overlay
        //Drawable locationMarker = getResources().getDrawable(R.drawable.location);
        ////int locationMarkerWidth = locationMarker.getIntrinsicWidth();
        //int locationMarkerHeight = locationMarker.getIntrinsicHeight();
        //locationMarker.setBounds(0, locationMarkerHeight, locationMarkerWidth, 0);
        
        //MyItemizedOverlay myItemizedLocationOverlay = new MyItemizedOverlay(locationMarker);
        //mapView.getOverlays().add(myItemizedLocationOverlay);
        
        //GeoPoint myPoint2 = new GeoPoint((int) (Double.parseDouble(coordinates[0])), 
        //								(int) (Double.parseDouble(coordinates[1])));
        
        //myItemizedOverlay.addItem(myPoint2, "myPoint2", "myPoint2");
        
        
        //Creating fixed geo location:
        mapController = mapView.getController();
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);
        
        p = new GeoPoint(
                (int) (lat * 1E6), 
                (int) (lng * 1E6));
     
        mapController.animateTo(p);
        mapController.setZoom(17);
        
        mapView.invalidate();
        
	}
    
    @Override
    protected boolean isLocationDisplayed() {
    // TODO Auto-generated method stub
    return false;
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		myLocationOverlay.enableMyLocation();
		
		mapView.setBuiltInZoomControls(settings.getZoomControls());
        mapView.setSatellite(settings.getSatelliteMode());
        
		if(settings.getCompassMode())
			myLocationOverlay.enableCompass();
		
		if(scanController.getScanned()){
			
			locationManager.removeUpdates(locationListener);
			
			mapController = mapView.getController();
	        double lat = Double.parseDouble(coordinates[0]);
	        double lng = Double.parseDouble(coordinates[1]);
	        
	        p = new GeoPoint(
	                (int) (lat * 1E6), 
	                (int) (lng * 1E6));
	     
	        mapController.animateTo(p);
	        mapController.setZoom(17);
	      
	        GeoPoint locationPoint = new GeoPoint((int) (Double.parseDouble(coordinates[0])*1E6),
	        								 (int) (Double.parseDouble(coordinates[1])*1E6));
	        
	        createMarkerOverlay(R.drawable.location, locationPoint, scanController.getScannedContent());
	        mapView.invalidate();
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		myLocationOverlay.disableMyLocation();
		myLocationOverlay.disableCompass();
		//Crazy line to stop location
        locationManager.removeUpdates(locationListener);
	}
	
	private void createMarker() {
		GeoPoint p = mapView.getMapCenter();
		OverlayItem overlayitem = new OverlayItem(p, "", "");
		itemizedoverlay.addOverlay(overlayitem);
		if (itemizedoverlay.size() > 0) {
			mapView.getOverlays().add(itemizedoverlay);
		}
	}
	
	private void createMarkerOverlay(int drawableMarker, GeoPoint point, String pointName){
		
		//Adding Marker Overlay
        Drawable marker=getResources().getDrawable(drawableMarker);
        int markerWidth = marker.getIntrinsicWidth();
        int markerHeight = marker.getIntrinsicHeight();
        marker.setBounds(0, markerHeight, markerWidth, 0);
    
        MyItemizedOverlay myItemizedOverlay = new MyItemizedOverlay(marker);
        mapView.getOverlays().add(myItemizedOverlay);
        
        myItemizedOverlay.addItem(point, pointName, pointName);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, SETTINGS_ID, 0, R.string.settings_menu);
        menu.add(0, SCANNER_ID, 1, R.string.scanner_menu);
        return true;
    }
	
	@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
        case SETTINGS_ID:	//Creates the settings intent  and starts the settings activity
            {
            	Intent i = new Intent(this, SettingsActivity.class);
            	startActivity(i);
            	return true;
            }
            
        case SCANNER_ID: //Creates the Zxing intent and starts the zxing scanner activity
			{
				Intent i = new Intent("com.google.zxing.client.android.SCAN");
				i.putExtra("SCAN_MODE", "QR_CODE_MODE");
				startActivityForResult(i, 0);
				return true;
			}
			
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		   if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {
		         String contents = intent.getStringExtra("SCAN_RESULT");
		         String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
		         // Handle successful scan
		         locationManager.removeUpdates(locationListener);
		         scanController.setScanned(true);
		         scanController.setScannedContent(contents);
		         coordinates = convertToStringArray(contents);
		         Toast.makeText(getBaseContext(), contents, Toast.LENGTH_SHORT).show();
		         
		      } else if (resultCode == RESULT_CANCELED) {
		         // Handle cancel
		    	 scanController.setScanned(false);
		    	 String cancelled = "Scan Cancelled";
		    	 Toast.makeText(getBaseContext(), cancelled, Toast.LENGTH_SHORT).show();
		      }
		   }
		}

	public String[] convertToStringArray(String string){
		String [] stringArray = new String[2];
		stringArray = string.split(",");
		
		return stringArray;
	}
	
	public class MapOverlay extends com.google.android.maps.Overlay{
	    @Override

	   public boolean onTouchEvent(MotionEvent event, MapView mapview){

	    /*	//when user lifts his/her finger
            if (event.getAction() == 1) {                
                GeoPoint p = mapView.getProjection().fromPixels(
                    (int) event.getX(),
                    (int) event.getY());
 
                Geocoder geoCoder = new Geocoder(
                    getBaseContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geoCoder.getFromLocation(
                        p.getLatitudeE6()  / 1E6, 
                        p.getLongitudeE6() / 1E6, 1);
 
                    String add = "";
                    if (addresses.size() > 0) 
                    {
                        for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
                             i++)
                           add += addresses.get(0).getAddressLine(i) + "\n";
                    }
 
                    Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
                }
                catch (IOException e) {                
                    e.printStackTrace();
                }   
                return true;
            }
            else                
                return false;
                */
	    	return false;
	    }
	}
	
	public class GeoUpdateHandler implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			
			
			//Go to scanned position
			if(scanController.getScanned()){
				
				//Creating fixed geo location:
		        mapController = mapView.getController();
		        int lat = (int) Double.parseDouble(coordinates[0]);
		        int lng = (int) Double.parseDouble(coordinates[1]);
		        
		        GeoPoint point = new GeoPoint(lat,lng);
		        point = new GeoPoint(
		                (int) (lat * 1E6), 
		                (int) (lng * 1E6));
		     
		        createMarker();
				mapController.animateTo(point);
		        mapController.setZoom(17);
		        locationManager.removeUpdates(this);
		        mapView.invalidate();
	
			}
			
			//Carry on with GPS scanned coordinates position
			else{
				int lat = (int) (location.getLatitude() * 1E6);
				int lng = (int) (location.getLongitude() * 1E6);
				GeoPoint point = new GeoPoint(lat, lng);
				createMarker();
				mapController.animateTo(point); // mapController.setCenter(point);
			}

		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
	
	
}