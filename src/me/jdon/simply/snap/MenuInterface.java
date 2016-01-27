package me.jdon.simply.snap;




import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPSClient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MenuInterface extends Application{
	
	public static String title = "Simply Snap";
	public static double version = 0.1;
	static int width = 280;
	static int height = 400;
	static Stage window;
	static Scene scene;
	Text scenetitle = null;	

	
	@Override
	public void start(Stage stage) throws Exception {
		
		
		scene = new Scene(new Group());
		Options o = new Options(true);
		ColorPicker colorPicker = new ColorPicker();
		colorPicker.setValue(Color.web(o.getColour()));
		
		window=stage;
		stage.setTitle(""/*title +" v"+version*/);
		stage.setWidth(width);
		stage.setHeight(height);
		stage.setMinHeight(height);
		stage.setMinWidth(width);
		stage.getIcons().add(new Image("IconHR.png"));
		stage.initStyle(StageStyle.UNDECORATED);
		scene.getStylesheets().add("stylesheet.css");
		BorderPane root = new BorderPane(); 
		//Background
		root.setBackground(new Background(background()));
		//Title Image
		ImageView Titleimg = new ImageView();
		Titleimg.setTranslateY(10);
		Titleimg.setImage(new Image("SimpleSnapLogo.png"));
		VBox titleimgbox = new VBox();
		titleimgbox.getChildren().add(Titleimg);
		titleimgbox.setAlignment(Pos.TOP_CENTER);
		
		

		
		// GRID STUFF
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(3, 6, 0, 5));

		scenetitle = new Text("");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		scenetitle.setTranslateY(8);
		grid.add(scenetitle, 0, 0, 2, 1);

		Label serverIP = new Label("Server IP:");
		grid.add(serverIP, 0, 1);
		
		TextField serverTextField = new TextField();
		if(!(o.getServer().isEmpty())){
			serverTextField.setText(o.getServer());
		}
		serverTextField.setPromptText("Domain or IP");
		grid.add(serverTextField, 1, 1);
		
		Label userName = new Label("User Name:");
		grid.add(userName, 0, 2);

		TextField userTextField = new TextField();
		userTextField.setPromptText("John");
		if(!(o.getUsername().isEmpty())){
			userTextField.setText(o.getUsername());
		}
		grid.add(userTextField, 1, 2);

		Label pw = new Label("Password:");
		grid.add(pw, 0, 3);

		PasswordField pwBox = new PasswordField();
		if(!(o.getPassword().isEmpty())){
			pwBox.setText(o.getPassword());
		}
		grid.add(pwBox, 1, 3);
		
		Label port = new Label("Port:");
		grid.add(port, 0, 4);

		TextField portTextField = new TextField();
		portTextField.setText("21");
		if(!(o.getPort()== 0)){
			portTextField.setText(String.valueOf(o.getPort()));
		}
		grid.add(portTextField, 1, 4);
		
		Label directory = new Label("Directory:");
		grid.add(directory, 0, 5);
		
		TextField directoryTextField = new TextField();
		directoryTextField.setPromptText("/images/cars");
		if(!(o.getDirectory().isEmpty())){
			directoryTextField.setText(o.getDirectory());
		}
		
		HBox directorybuttons = new HBox();
		directorybuttons.setSpacing(5);
		directorybuttons.getChildren().add(directoryTextField);
		Button directoryBrowser = new Button("...");
		directorybuttons.getChildren().add(directoryBrowser);
		grid.add(directorybuttons, 1, 5);
		directoryBrowser.setOnAction(e -> DirectoryBrowser());
		
		Label webAddress = new Label("Web Address:");
		grid.add(webAddress, 0, 6);
		
		TextField urlTextField = new TextField();
		urlTextField.setPromptText("yourwebsite.com");
		if(!(o.getDirectory().isEmpty())){
			urlTextField.setText(o.getWebAddress());
		}
		grid.add(urlTextField, 1, 6);
		
		Button submitbutton = new Button("Submit FTP");
		grid.add(submitbutton, 1, 7);
		submitbutton.setOnAction(e -> submit(scenetitle, serverTextField, userTextField, pwBox, portTextField, directoryTextField, urlTextField));
		//
		
		//Bottombar
		HBox bottombar = new HBox();
		bottombar.setAlignment(Pos.BOTTOM_CENTER);
		bottombar.setTranslateY(-15);
		bottombar.setSpacing(70.0);
		
        //Color picker event
		colorPicker.setOnAction(e-> updateColour(colorPicker));
		
		
		//Exit button
		Button exit = new Button("Exit");
		exit.setOnAction(e -> closeProgram());
		
		HBox error = new HBox();
		Label errortext = new Label("");
		
		error.getChildren().add(errortext);
		bottombar.getChildren().add(colorPicker);
		bottombar.getChildren().add(exit);
		
		
		//Add All
		bottombar.getChildren().add(grid);
		root.setTop(titleimgbox);
		root.setCenter(grid);
		root.setBottom(bottombar);
		addDraggableNode(root);
		//Show
		scene.setRoot(root);
		stage.setScene(scene);
		stage.show();
	}
	
	private void DirectoryBrowser(){
		DirectoryChooser d = new DirectoryChooser();
		d.showDialog(window);
	}
	
	
	private void updateColour(ColorPicker colorPicker) {
		// TODO Auto-generated method stub
		Options o = new Options(false);
		Color c = colorPicker.getValue();
		o.setColour(toRGBCode(c));
		//o.setColour(hex);
		o.save();
	}

	 public static String toRGBCode( Color color )
	    {
	        return String.format( "#%02X%02X%02X",
	            (int)( color.getRed() * 255 ),
	            (int)( color.getGreen() * 255 ),
	            (int)( color.getBlue() * 255 ) );
	    }

	public static BackgroundImage background(){
		BackgroundImage myBI= new BackgroundImage(new Image("background.png",32,32,false,true),
		        BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
		          BackgroundSize.DEFAULT);
		return myBI;
	}
	double initialY = 0;
	double initialX = 0;
	private void addDraggableNode(final Node node) {

	    node.setOnMousePressed(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent me) {
	            if (me.getButton() != MouseButton.MIDDLE) {
	                initialX = me.getSceneX();
	                initialY = me.getSceneY();
	            }
	        }
	    });

	    node.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent me) {
	            if (me.getButton() != MouseButton.MIDDLE) {
	                node.getScene().getWindow().setX(me.getScreenX() - initialX);
	                node.getScene().getWindow().setY(me.getScreenY() - initialY);
	            }
	        }
	    });
	}
	public void openMenu(){
	    Application.launch();
	}
	
	public void closeProgram(){
		Platform.exit();
	}
	
	public void submit(Text error, TextField domain, TextField username, PasswordField pass, TextField Port, TextField Directory, TextField webby){
		error.setFill(Color.RED);
		error.setText("");
		String server = domain.getText();
		String user = username.getText();
		String password = pass.getText();
		String port = Port.getText();
		String directory = Directory.getText();
		String webaddress = webby.getText();
		//Presence Checks
		if(server.isEmpty() && user.isEmpty() && password.isEmpty() && port.isEmpty() && directory.isEmpty() && webaddress.isEmpty()){
			error.setText("You must complete the boxes");
			return;
		}
		if(server.isEmpty()){
			error.setText("You must enter a server.");
			return;
		}
		if(user.isEmpty()){
			error.setText("You must enter a username.");
			return;
		}
		if(password.isEmpty()){
			error.setText("You must enter a password.");
			return;
		}
		if(port.isEmpty()){
			error.setText("You must enter a port.");
			return;
		}
		if(directory.isEmpty()){
			error.setText("You must enter a directory location.");
			return;
		}
		if(webaddress.isEmpty()){
			error.setText("You must enter a directory location.");
			return;
		}
		///
		//Other Validation
		if(server.startsWith("http")){
			error.setText("Server should not contain 'http://'");
			return;
		}
		if(server.startsWith("www")){
			error.setText("Server should not contain 'www.'");
			return;
		}
		if(webaddress.startsWith("http")){
			error.setText("Web address should not contain 'http://'");
			return;
		}
		if(webaddress.startsWith("www")){
			error.setText("Web address should not contain 'www.'");
			return;
		}
		
		if (!port.matches("[0-9]+")) {
			error.setText("Port number must only contain numbers.");
			return;
		}
		error.setText("Checking...");
		if(checkFtpDetails(server,Integer.parseInt(port), user, password, directory, webaddress)){
			Options o = new Options(true);
			o.setServer(server);
			o.setUsername(user);
			o.setPassword(password);
			o.setDirectory(directory);
			o.setPort(Integer.parseInt(port));
			o.setWebAddress(webaddress);
			o.save();
			error.setFill(Color.DARKGREEN);
			error.setText("Details Updated.");
			
			
			return;
		}else{
			return;
		}
		
	}
	
	public Boolean checkFtpDetails(String server, int port, String Username, String Password,String Directory, String webaddress){
		
		FTPSClient  ftpClient = new FTPSClient();
		try {
			ftpClient.connect(server, port);
		} catch (SocketException e) {
			//port is wrong
			e.printStackTrace();
			scenetitle.setText("Invalid Port.");
			return false;
		} catch (IOException e) {
			scenetitle.setText("Invalid Server.");
			return false;
		}
        try {
			ftpClient.login(Username, Password);
		} catch (IOException e) {
			scenetitle.setText("Wrong Username or Password.");
			return false;
		}
        ftpClient.enterLocalPassiveMode();
        try {
			ftpClient.changeWorkingDirectory(Directory);
		} catch (IOException e) {
			e.printStackTrace();
			scenetitle.setText("Invalid Directory.");
			return false;
		}finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                    return true;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
	}
		return false;
	
	}
	
	
}
