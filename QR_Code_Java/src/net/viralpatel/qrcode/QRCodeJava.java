package net.viralpatel.qrcode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
 
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

public class QRCodeJava {
	
	private String content;
	private String destination;
	
	public QRCodeJava(){
		
	}
	
	public QRCodeJava(String content, String destination){
		this.content = content;
		this.destination = destination;
		
	}
	
    public boolean generate() {
        ByteArrayOutputStream out = QRCode.from(this.getContent())
                                        .to(ImageType.PNG).stream();
 
        try {
            FileOutputStream fout = new FileOutputStream(new File(this.getDestination()));
 
            fout.write(out.toByteArray());
 
            fout.flush();
            fout.close();
            return true;
            
 
        } catch (FileNotFoundException e) {
            // Do Logging
        	e.printStackTrace();
        	return false;
        	
        } catch (IOException e) {
            // Do Logging
        	e.printStackTrace();
        	return false;
        }
    }
    
    private String[] convertToStringArray(String string){
		String [] stringArray = new String[2];
		stringArray = string.split(",");
		
		return stringArray;
	}
    
    public void setContent(String content){
    	this.content = content;
    }
    
    public void setDestination(String destination){
    	this.destination = destination;
    }
    
    private String getContent(){
    	return content;
    }
    
    private String getDestination(){
    	return destination;
    }
    
}
