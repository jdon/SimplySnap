package me.jdon.simply.snap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Options {
	
	private String Server;
	private int port;
	private String username;
	private String password;
	private String directory;
	private String webAddress;
	private String colour;
	Path optionPath = Paths.get(System.getenv("APPDATA")+"\\SimplySnap", "options.txt");
	File optionsfile = new File(optionPath.toString());
	public Options(Boolean writedefaults){
		if(optionsfile.exists()){
			//already have options menu so load them
			try {
				List<String> fileText  = Files.readAllLines(optionPath);
				Server = fileText.get(0);
				port = Integer.valueOf(fileText.get(1));
				username = fileText.get(2);
				password = fileText.get(3);
				directory = fileText.get(4);
				webAddress = fileText.get(5);
				colour = fileText.get(6);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//open menu to create the option menu and show dialog
			try {
				Files.createDirectories(optionPath.getParent());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(writedefaults){
				FileWriter fw;
				try {
					setDirectory("/web/images");
					setServer("server");
					setUsername("username");
					setPort(21);
					setPassword("pass");
					setWebAddress("google.com");
					setColour("#46f657");
					fw = new FileWriter(optionPath.toString());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(Server);
					bw.newLine();
					bw.write(String.valueOf(port));
					bw.newLine();
					bw.write(username);
					bw.newLine();
					bw.write(password);
					bw.newLine();
					bw.write(directory);
					bw.newLine();
					bw.write(webAddress);
					bw.newLine();
					bw.write(colour);
					bw.newLine();
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
		}
	}
	
	public Boolean exists(){
		if(optionsfile.exists()){
			return true;
		}else{
			return false;
		}
		
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int Port) {
		this.port = Port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public String getServer() {
		return Server;
	}
	public void setServer(String server) {
		this.Server = server;
	}
	
	public void save(){
		try {
			FileWriter fw = new FileWriter(optionPath.toString());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(Server);
			bw.newLine();
			bw.write(String.valueOf(port));
			bw.newLine();
			bw.write(username);
			bw.newLine();
			bw.write(password);
			bw.newLine();
			bw.write(directory);
			bw.newLine();
			bw.write(webAddress);
			bw.newLine();
			bw.write(colour);
			bw.newLine();
			bw.write("firstuse");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}
}
