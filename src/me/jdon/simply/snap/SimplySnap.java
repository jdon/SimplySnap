package me.jdon.simply.snap;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;

import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class SimplySnap{
	
	
	public static void main(String[] args){
		Options op = new Options(false);
		if(op.exists()){
			new Snapper();
		}else{
			MenuInterface m = new MenuInterface();
			m.openMenu();
		}
	}
	
    public static File saveImage(Rectangle rect){
    	Robot r = null;
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
    	BufferedImage img = r.createScreenCapture(rect);
    	
    	    	try {
    	    		File outputfile = File.createTempFile("screen", ".png");
			ImageIO.write(img, "png", outputfile);
  	    	 outputfile.deleteOnExit();
  	    	uploadImage(outputfile);
  	    	 return outputfile;
		} catch (IOException e) {
			e.printStackTrace();
		}
				return null;
    }
    
    public static void uploadImage(File image){
    	FTPSClient  ftpClient = new FTPSClient ();
    	Options option = new Options(false);
        String server =option.getServer();
        String webaddress = option.getWebAddress();
        int port = option.getPort();
        String user = option.getUsername();
        String pass = option.getPassword();
        String imagename = image.getName();
            try {
				ftpClient.connect(server, port);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "The socket of the FTP server is wrong");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "The server name of the FTP server is wrong");
				return;
			}
            try {
				ftpClient.login(user, pass);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "The username or password of the FTP server is wrong");
				return;
			}
            ftpClient.enterLocalPassiveMode();
 
            try {
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "The FTP server does not support passive mode");
				return;		
			}
            try {
				ftpClient.changeWorkingDirectory(option.getDirectory());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "The directory can not be found");
				return;
			}
 
            // APPROACH #1: uploads first file using an InputStream
            boolean done;
			try {
	            InputStream inputStream = new FileInputStream(image);
				done = ftpClient.storeFile(imagename, inputStream);
				inputStream.close();
	            if (done) {
	        		ClipboardHandler clipboard = new ClipboardHandler();
	        		clipboard.setClipboardContents("http://www."+webaddress+"/"+imagename);
	        		openBrowser("http://www."+webaddress+"/"+imagename);
	            }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "The file could not be uploaded to the FTP server");
				return;
			}
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Unable to disconnect from the ftp server");
				return;
            }
    }
    
    public static void openBrowser(String url){
	    try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
