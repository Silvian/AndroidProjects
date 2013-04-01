package android.pack;

public class SettingsController {
	
	private boolean compassMode;
	private boolean satelliteMode;
	private boolean zoomControls;
	
	public SettingsController(){
		
	}
	
	public SettingsController(boolean compassMode, 
							   boolean satelliteMode, 
							   boolean zoomControls){
		
		this.compassMode = compassMode;
		this.satelliteMode = satelliteMode;
		this.zoomControls = zoomControls;
		
	}
	
	public void setCompassMode(boolean compassMode){
		this.compassMode = compassMode;
	}
	
	public void setSatelliteMode(boolean satelliteMode){
		this.satelliteMode = satelliteMode;
	}
	
	public void setZoomControls(boolean zoomControls){
		this.zoomControls = zoomControls;
	}
	
	public boolean getCompassMode(){
		return compassMode;
	}
	
	public boolean getSatelliteMode(){
		return satelliteMode;
	}
	
	public boolean getZoomControls(){
		return zoomControls;
	}
	
}

class ScanController{
	
	private boolean scanned;
	private String scannedContent;
	
	public ScanController(){
		
		
	}
	
	public void setScanned(boolean scanned){
		this.scanned = scanned;
	}
	
	public boolean getScanned(){
		return scanned;
	}
	
	public void setScannedContent(String scannedContent){
		this.scannedContent = scannedContent;
	}
	
	public String getScannedContent(){
		return scannedContent;
	}
	
}
