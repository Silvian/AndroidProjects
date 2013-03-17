package navigation.haptic;
//feedback when pointing in direction of destination
// working for destination pointer june 13 2012
/*import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;


import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import java.util.TimerTask;

import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;


*/

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import java.util.Timer;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.util.List;

import navigation.haptic.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;



import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;

import android.widget.Button;
import android.widget.TextView;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class utpointdestination extends Activity {
private PrintWriter mCurrentFile;// file to write into
private SensorManager mSensorManager;
private double ori; //orientation from compass
private double bear;// bearing for the waypoint
private String text1;
private double dist;// value returned from php
private double lrange;
private double hrange;
private double nlat;
private double nlon;
private Integer whereami;
private String lvp = "1";
private String nwp;
private String reach;
private String text; 
private LocationManager lm;
private LocationListener locationListener;
// private TextView latitude;
// private TextView longitude;

private String beartext;
private String lat,lon, orival;
private int pointing;
private String lon1;
private String bearing;
private float accuracy;
private String accuracytext;
private String speed;
private String altitude;
private String sloc;
private int counter=0, vibratestop=0;
public static String eloc = "";
Button buttonHaptic;

private Vibrator vibrator; 
RadioGroup radiogroup1, radiogroup2;
RadioButton rt5,rt4,rt1,rt2,rt3;
Button calculate;

HttpPost httppost;

HttpClient httpclient;

// List with parameters and their values
List<NameValuePair> nameValuePairs;

String serverResponsePhrase;
int serverStatusCode;
String bytesSent,serverURL;



/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	// Capture our button from layout
   // buttonHaptic = (Button)findViewById(R.id.stop);
    

    
    // Register the onClick listener with the implementation above
 //   buttonHaptic.setOnTouchListener(mHapticButtonTouchListener);
    //buttonHaptic.setText(radio.getText());
    // Register the onClick listener with the implementation above
    //buttonHaptic.setOnClickListener(mHapticButtonListener);
    

    
    Spinner spinner = (Spinner) findViewById(R.id.spinner);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this, R.array.destination_array, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);

    spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
    
	//startSensing();
	//checkHaptic();	
final Button calculate = (Button) findViewById(R.id.calculate); //find the path
final Button vipe = (Button) findViewById(R.id.vipe); //clears all selection
//final Button accept = (Button) findViewById(R.id.accept); //says understood the direction
//final Button sbtn=(Button)findViewById(R.id.stop);

/*
radiogroup1 = (RadioGroup) findViewById(R.id.radiogroup_to);
final RadioButton rt1 = (RadioButton) findViewById(R.id.radio_pugin);
final RadioButton rt2 = (RadioButton) findViewById(R.id.radio_garda);
final RadioButton rt3 = (RadioButton) findViewById(R.id.radio_ncg);
final RadioButton rt4 = (RadioButton) findViewById(R.id.radio_cal);
final RadioButton rt5 = (RadioButton) findViewById(R.id.radio_arts);
radiogroup1.isHapticFeedbackEnabled();*/
//radiogroup2.isHapticFeedbackEnabled();

//If you click "Show me the way!" button, do the following
calculate.setOnClickListener(new View.OnClickListener() {
public void onClick(View v) throws  NumberFormatException {
    if (v == calculate)
    {

/*    	rt5.setClickable(false);
    	rt4.setClickable(false);
       	rt1.setClickable(false);
    	rt2.setClickable(false);
       	rt3.setClickable(false);*/

    	
    	//private SensorManager mSensorManager;
        // Set up the orientation reading
    	mSensorManager = (SensorManager)getSystemService
    	(Context.SENSOR_SERVICE);

    	Sensor mSensor = mSensorManager.getSensorList
    	(Sensor.TYPE_ORIENTATION).get(0);
    	mSensorManager.registerListener(mSensorListener, mSensor,mSensorManager.SENSOR_DELAY_FASTEST);
    	
    	
/*        // Set up the orientation reading
    	mSensorManager = (SensorManager)getSystemService
    	(Context.SENSOR_SERVICE);
    	Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    	//Sensor mSensor = mSensorManager.getSensorList (Sensor.TYPE_ORIENTATION).get(0);
    	
    	mSensorManager.registerListener(mSensorListener, mSensor,SensorManager.SENSOR_DELAY_NORMAL);*/
    	
    	//new code
/*    	SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);

    	// Register this class as a listener for the accelerometer sensor
    	sm.registerListener(mSensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
    	                    SensorManager.SENSOR_DELAY_GAME);
    	// ...and the orientation sensor
    	sm.registerListener(mSensorListener, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
    	                    SensorManager.SENSOR_DELAY_GAME);
*/
    //end of new code..
    	//postData(); //commented this to get the save location to file working. 
      	pointing=1;    
    
    	//counter = 1;
    	vibratestop = 0;
    	reach ="n";
    startSensing(); // turn on GPS
    	
    	//postData();
    	//Location
/* 	lm = (LocationManager) 
    	    getSystemService(Context.LOCATION_SERVICE);    
    	
    	locationListener = new MyLocationListener();

    	lm.requestLocationUpdates(
    	    LocationManager.GPS_PROVIDER, 
    	    0, 
    	    0, 
    	    locationListener); 
    	*/
    	//postData();
    	//checkHaptic();

     }
}
});
//end of Show me the way! button click

vipe.setOnClickListener(new View.OnClickListener() {
public void onClick(View v) throws  NumberFormatException {
    if (v == vipe)
    {

//set buttons to clickable
   
       	lm.removeUpdates(locationListener);//stop GPS..
       //	pointing=1;

     }
}
});

/*accept.setOnClickListener(new View.OnClickListener() {
    public void onClick(View v) throws  NumberFormatException {
        if (v == accept)
        {
 vibratestop = 1;	
	pointing=1;
 //startSensing();
	vibrator.cancel();
         }
    }
});*/

/*sbtn.setOnClickListener(new View.OnClickListener() {
	public void onClick(View v) throws  NumberFormatException {
	    if (v == sbtn)
	    {
	       	lm.removeUpdates(locationListener);//stop GPS..
	     }
	}
	});*/

}

public void checkHaptic()
{

    Button btn=(Button)findViewById(R.id.info);
   
	
		btn.setText(text);
		btn.setBackgroundColor(0xffffff00);//Yellow

vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE); 
//vibrator.vibrate((long) 2000.0);	    

   if(ori >=lrange && ori<=hrange)
//if(pointing=="1")
   {
	   vibrator.vibrate((long) 100.0);
   //sbtn.setBackgroundColor(0xFF00FF00);//Green 
	long[] pattern1 = {100,100,500,100};      
long[] pattern2 = {100,500,500,500};  
long[] pattern3 = {100,1000,500,1000};  
 
 //vibrator.vibrate((long) 2000.0);
   btn.setBackgroundColor(0xFF00FF00);//Green  
   //vibrator.vibrate((long) 2000.0);	    	   
	
   if(dist > 0.0 && dist <=10.0)
   {
   
	   vibrator.vibrate((long) 2000.0);
	   btn.setBackgroundColor(0xFF00FF00);//Green  
	   sloc = ""+dist+"";
	   ((TextView) findViewById(R.id.txt_info)).setText(
	           String.valueOf(sloc));

   }
   
   else if(dist > 10.0 && dist <=100.0)
   {
 	//vibrator.vibrate(pattern3,0);
	  vibrator.vibrate((long) 500.0);
	   btn.setBackgroundColor(0xFF00FF00);//Green  
	   sloc = ""+dist+"";
	   ((TextView) findViewById(R.id.txt_info)).setText(
	           String.valueOf(sloc));
   }
   
  
   else if(dist > 100.0)
   {
	  	 //vibrator.vibrate(pattern1,0);		          
	   vibrator.vibrate((long) 100.0);
	   btn.setBackgroundColor(0xFF00FF00);//Green  
	   sloc = ""+dist+"";
	   ((TextView) findViewById(R.id.txt_info)).setText(
	           String.valueOf(sloc));
   }
  

   
   }  
   // end of main if
   else if (dist>0.0&&dist<10.0)
   {
	   btn.setText("You are there!");
	   sloc = "Reached Destination!";
	   vibrator.vibrate((long) 2000.0);
	   ((TextView) findViewById(R.id.txt_info)).setText(
	           String.valueOf(sloc));
   }
//startSensing();


}
   
   
/*
   if(pointing==1)
   {
	   startSensing();
   }*/


/*if (vibratestop==1)
{
	counter=0;
}
else
{
	
}*/

	


public void startSensing()
{
	//Location
	lm = (LocationManager) 
	    getSystemService(Context.LOCATION_SERVICE);    
	
	locationListener = new MyLocationListener();

	lm.requestLocationUpdates(
	    LocationManager.GPS_PROVIDER, 
	    0, 
	    0, 
	    locationListener); 
    /*lat = "53.382750749588";
    lon = "-6.60290658473969";*/
	
	//postData();

	
}

//Start of post to database code	
	public void postData() {  

		   //New code March 21st 2011

		       
		       serverURL = "http://postgis.cs.nuim.ie/StayonPath/haptic-new/HapticPedestrian/HapticDestinationPointer.php"; // haptic + orientation + GPS + color coded buttons+ selected end location.
		//serverURL = "http://149.157.244.231/haptic-new/hapticroutedestination.php"; // haptic + orientation + GPS + color coded buttons+ selected end location.

		       httppost = new HttpPost(serverURL);  
		       httpclient = new DefaultHttpClient();
		       nameValuePairs = new ArrayList<NameValuePair>(3);  //number of variables sent

		       // Adding parameters to send to the HTTP server.
		       
		       //lat1 = findViewById(R.id.latitude).toString();
		       //lon1 = findViewById(R.id.longitude).toString();

		       //lat = "53.382750749588";
		       //lon = "-6.60290658473969";
		       
		 
		       
		       nameValuePairs.add(new BasicNameValuePair("eloc", eloc));   		       
		       nameValuePairs.add(new BasicNameValuePair("lat", lat));
		       nameValuePairs.add(new BasicNameValuePair("lon", lon));
		       nameValuePairs.add(new BasicNameValuePair("orival", orival));
		       //lm.removeUpdates(locationListener);//stop GPS.. 		
		       try {                    
		       httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));  

		       HttpResponse response = httpclient.execute(httppost);

		       InputStream is = response.getEntity().getContent();
		       BufferedInputStream bis = new BufferedInputStream(is);
		       ByteArrayBuffer baf = new ByteArrayBuffer(20);
	   		    
		
		       int current = 0;  
		       while((current = bis.read()) != -1)
		       {  
		       baf.append((byte)current);  
		       }  

		       bytesSent = new String(baf.toByteArray());
		  
		       //use this to read byte
		      //((TextView) findViewById(R.id.status)).setText(
    	      //        String.valueOf(bytesSent));
		       //end of use this to read byte   
		       
		      //bear = Double.parseDouble(bytesSent);
		       
		       // Response from the server          
		       serverResponsePhrase = response.getStatusLine().getReasonPhrase();
		       serverStatusCode = response.getStatusLine().getStatusCode();
		       
		       
		  ((TextView) findViewById(R.id.status)).setText(
    	              String.valueOf(serverStatusCode));
	
			//lm.removeUpdates(locationListener);//stop GPS..
			
	       String answerFromServer = new String(bytesSent);
	       
	       String[] partsOfResponse = answerFromServer.split(",");
	       
	       if (partsOfResponse.length > 0)
	       {
	    	   // we have a valid response
	    	   // first thing in the comma separated response is the distance
	    	   dist = new Double(partsOfResponse[0]); 
	    	   reach = new String(partsOfResponse[1]);
	    	   bear= new Double(partsOfResponse[2]); 
	    	  // nwp = new String(partsOfResponse[3]);
	    	  text = dist+"";
	    	  beartext= bear+"";
	    	   //System.out.println("RESPONSE from the Server = " + dist + ","+ bear + "," + reach + ","+ nwp);
	    	//  lm.removeUpdates(locationListener);//stop GPS..
	    	  //vibrator.vibrate((long) 2000.0);
	       }
	       
	
	       lrange = (double)(bear-5.0);
	       hrange = (double)(bear+5.0);
	       bearing = lrange+","+hrange;
	       ((TextView) findViewById(R.id.accuracy)).setText(
	               String.valueOf(bearing));
	      
   		checkHaptic();
   		
	       //Button btn=(Button)findViewById(R.id.info);
	      // Button sbtn=(Button)findViewById(R.id.stop);
	   	
			//btn.setText(text);
			//btn.setBackgroundColor(0xffffff00);//Yellow
	        //sbtn.setText(beartext);	       

	        
	        //Button xbtn=(Button)findViewById(R.id.accept);
	       // ((TextView) findViewById(R.id.accept)).setText(
		           //    String.valueOf(pointing));
	        
	        //Button btn=(Button)findViewById(R.id.info);

	    		//lm.removeUpdates(locationListener);//stop GPS..
	        
	        
	        
	        //Button btn=(Button)findViewById(R.id.info);
	        

   
	        
		        
	    	/*if(pointing ==0)
	    	{
	    		lm.removeUpdates(locationListener);//stop GPS..
	    		checkHaptic();
	    	}
*/

			//checkHaptic();
	

	/*while(pointing==0;i++)
	{
		   sloc = "You are Pointing at the Destination!";

		   ((TextView) findViewById(R.id.txt_info)).setText(
		           String.valueOf(sloc));
		//if(pointing==0)
		//{
		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE); 
	   if(ori >=lrange && ori<=hrange)
	//if(pointing=="1")
	   {

	   //sbtn.setBackgroundColor(0xFF00FF00);//Green 
		long[] pattern1 = {100,100,500,100};      
	long[] pattern2 = {100,500,500,500};  
	long[] pattern3 = {100,1000,500,1000};  
	 
	 //vibrator.vibrate((long) 2000.0);
	   btn.setBackgroundColor(0xFF00FF00);//Green  
			    	   
		
	   if(dist > 0.0 && dist <=15.0)
	   {
	   
		   vibrator.vibrate((long) 2000.0);
		   btn.setBackgroundColor(0xFF00FF00);//Green  
		   sloc = "You are Pointing at the Destination!";

		   ((TextView) findViewById(R.id.txt_info)).setText(
		           String.valueOf(sloc));

	   }
	   
	   else if(dist > 15.0 && dist <=80.0)
	   {
	 	//vibrator.vibrate(pattern3,0);
		  vibrator.vibrate((long) 500.0);
		   btn.setBackgroundColor(0xFF00FF00);//Green  
		   sloc = "You are "+dist+" metres away";

		   ((TextView) findViewById(R.id.txt_info)).setText(
		           String.valueOf(sloc));

	   }
	   
	   
	   else if(dist > 80.0 && dist <=250.0)
	   {
		  	// vibrator.vibrate(pattern2,0);
		   vibrator.vibrate((long) 300.0);
		   btn.setBackgroundColor(0xFF00FF00);//Green  
		   sloc = "You are "+dist+" metres away";

		   ((TextView) findViewById(R.id.txt_info)).setText(
		           String.valueOf(sloc));

	   }
	   
	   else if(dist > 250.0)
	   {
		  	 //vibrator.vibrate(pattern1,0);		          
		   vibrator.vibrate((long) 100.0);
		   btn.setBackgroundColor(0xFF00FF00);//Green  
		   sloc = "You are "+dist+" metres away";

		   ((TextView) findViewById(R.id.txt_info)).setText(
		           String.valueOf(sloc));

	   }
	   
	   }   		    	// end of main if
	   else if (dist>0.0&&dist<15.0)
	   {
		   btn.setText("You are there!");
		   sloc = "You are at the destination!";

		   ((TextView) findViewById(R.id.txt_info)).setText(
		           String.valueOf(sloc));
	   }
	   
		//}//end of pointing if

		if (vibratestop==1)
		{
			counter=0;
		}
		}
	*/
		
	


//lm.removeUpdates(locationListener);

		       }
		       catch (Exception e) {
		       // Exception handling
		    	   System.out.println(" EXCEPTION CAUGHT " + e.toString());
		       }
		       //end of new code
		   }
		 	 

//location
private class MyLocationListener implements LocationListener 
{
/*	boolean never = true;
      String comma = new String(",");
      
      
        public void runOnce()
        {
                //Creating a file to print the data into

        String nameStr = new String("/sdcard/pointdestination4.xls");
                File outputFile = new File(nameStr);
                mCurrentFile = null;
                try {
                        mCurrentFile = new PrintWriter(new 
FileOutputStream(outputFile));
                } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

        }  	
	 
        */
        
    @Override
    public void onLocationChanged(Location loc) {

/*        if (never)
{
        never = false;
       // runOnce();//was the working code
}
      */
        if (loc != null) {  
        	((TextView) findViewById(R.id.latitude)).setText(
	                String.valueOf(loc.getLatitude()));
        	((TextView) findViewById(R.id.longitude)).setText(
	                String.valueOf(loc.getLongitude()));
            lat = loc.getLatitude()+""; 
            lon = loc.getLongitude()+""; 
            bearing=loc.getBearing()+"";
            altitude=loc.getAltitude()+"";
            speed=loc.getSpeed()+"";
            accuracy=loc.getAccuracy();
            accuracytext = loc.getAccuracy()+"";
 
            
        postData(); //commented this to get the save location to file working. 
	    
			
            
            
            
        // if(accuracy>=0.0&& accuracy<=25.0)
         // {
           
         // lm.removeUpdates(locationListener);
      // }
            
            /*myTimer = new Timer();
        	myTimer.schedule(new TimerTask() {
        	@Override
        	public void run() {
        		postData(); 

        	}

        	}, 0, 5000);*/
         
/*        StringBuffer buff = new StringBuffer();
         buff.append(String.valueOf(loc.getLongitude()));
         buff.append(comma);
         buff.append(String.valueOf(loc.getLatitude()));
         buff.append(comma);
         buff.append(String.valueOf(loc.getBearing())); //  Returns the direction of travel in degrees East of true North. If hasBearing() is false, 0.0 is returned. 
         buff.append(comma);
         buff.append(String.valueOf(loc.getAltitude())); //  Returns the altitude of this fix. If hasAltitude() is false, 0.0f is returned. 
         buff.append(comma);
         buff.append(String.valueOf(loc.getAccuracy())); // Returns the accuracy of the fix in meters. If hasAccuracy() is false, 0.0 is returned. 
         buff.append(comma);
         buff.append(String.valueOf(loc.getSpeed())); // Returns the speed of the device over ground in meters/second. If hasSpeed() is false, 0.0f is returned. 
         buff.append(comma);
         // System.currentTimeMillis() Returns the current system time in milliseconds since January 1, 1970 00:00:00 UTC
         //buff.append(String.valueOf(loc.distanceTo(dest))); // Returns the distance from current location to 'Dest' = .
         //buff.append(comma);	    	                        
         
         long time = System.currentTimeMillis();
         buff.append(time);
         
//Get Time stamp            
         SimpleTimeZone pdt = new SimpleTimeZone(-8 * 60 * 60 * 1000, "PST");
         pdt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
         pdt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);

      // Format the current time.
         SimpleDateFormat formatter = new SimpleDateFormat(
                 "yyyy.MM.dd G 'at' hh:mm:ss a zzz");
         Date currentTime_1 = new Date();
         String dateString = formatter.format(currentTime_1);
         
         // Parse the previous string back into a Date.
         ParsePosition pos = new ParsePosition(0);
         Date currentTime_2 = formatter.parse(dateString, pos);
       
         buff.append(comma);
         buff.append(dateString);// Time Stamp of the Format - 2010.09.17 AD at 04:03:05 p.m. GMT+01:00
//End of Get Time stamp
        
         mCurrentFile.println(buff.toString());*/
      
        }
       
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, 
        Bundle extras) {
        // TODO Auto-generated method stub
    }
}
//end of location

//Orientation
private final SensorEventListener mSensorListener = new
SensorEventListener() {


      boolean never = true;

              @Override
              public void onAccuracyChanged(Sensor sensor, int accuracy) {
                      // TODO Auto-generated method stub

              }


              @Override
              public void onSensorChanged(SensorEvent event) {

            	  
                      if (never)
              {
                      never = false;
              
              }
                   
   /*                   if (event.accuracy==0)
                      {
                    	  ori=-1;
                      }
                      else
                      {*/
                      ori = Double.valueOf(event.values[0]);
              
                  	((TextView) findViewById(R.id.orientation)).setText(
        	                String.valueOf(ori));
                  	orival = ori+"";
                    
              }
};       
}// end of class

/*//Orientation
private final SensorEventListener mSensorListener = new
SensorEventListener() {


      boolean never = true;

              @Override
              public void onAccuracyChanged(Sensor sensor, int accuracy) {
                      // TODO Auto-generated method stub

              }

              float[] mGravity;
              float[] mGeomagnetic;
              @Override
              public void onSensorChanged(SensorEvent sensorEvent) {

            	  if (never)
            	  {
            	  never = false;

            	  }
            	                                        
            	  ori = Double.valueOf(event.values[0]);
if(ori<0)
	ori=ori+360;
if(ori>=360)
	ori=ori-360;
            	  ((TextView) findViewById(R.id.orientation)).setText(
            	        String.valueOf(ori));
            	  orival = ori+""; 


            	  //newer code
            	  if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
            		  mGravity=sensorEvent.values;
            	  if(sensorEvent.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD)
            		  mGeomagnetic=sensorEvent.values;
            	  if (mGravity != null && mGeomagnetic != null) {
float Ri[] = new float[9];
float In[]=new float[9];
          	        // checks that the rotation matrix is found
          	        boolean success = SensorManager.getRotationMatrix(Ri, In,
          	        		mGravity, mGeomagnetic);
          	        if (success) {
          	        	float orient[] = new float[3];
          	            SensorManager.getOrientation(Ri, orient);
          	            float azi = (float)Math.toDegrees(orient[0]);
          	            
          	         	if (azi<0)
                        	azi = 360+azi;	
          	         	
                        	//((TextView) findViewById(R.id.orientation)).setText(String.valueOf(azi));
              	             

                        	                                      
                        	ori = Double.valueOf(azi);
ori=(int)ori;
                        	((TextView) findViewById(R.id.orientation)).setText(
                        	      String.valueOf(ori));
                        	orival = ori+"";  
          	      } // en of     if (success) 

          	    } // end of if (mGravity != null && mGeomagnetic != null)
                	
                  
              }// end of onSensorChanged
              

};  //end of SensorEventListener
              
              
}//end of class
*/

/*            	  
                      if (never)
              {
                      never = false;
              
              }
                                                            
                      ori = Double.valueOf(event.values[0]);
                  	((TextView) findViewById(R.id.orientation)).setText(
        	                String.valueOf(ori));
                  	orival = ori+"";
                    
              }*/
