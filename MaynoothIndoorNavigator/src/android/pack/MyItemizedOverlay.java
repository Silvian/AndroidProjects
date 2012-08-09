package android.pack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem>{

	private ArrayList<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();

	public MyItemizedOverlay(Drawable marker) {
		super(boundCenterBottom(marker));
		// TODO Auto-generated constructor stub

		populate();
	}

	public void addItem(GeoPoint p, String title, String snippet){
		OverlayItem newItem = new OverlayItem(p, title, snippet);
		overlayItemList.add(newItem);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return overlayItemList.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return overlayItemList.size();
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		super.draw(canvas, mapView, shadow);
		//boundCenterBottom(marker);
	}
	
}
