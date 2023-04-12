package server;

//javafx libraries
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;


public class Server extends Application {
    // now we only have one sensor so only one parking spot
    static int numOfParking = 1;

    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Scene scene = new Scene(root, Color.BLACK);
        stage.getIcons().add(new Image("file:src/app_icon.png"));
        stage.setTitle("Server Parking Counter");
        Text text = new Text();
        text.setText("Available Parking");
        text.setX(690);
        text.setY(400);
        text.setFill(Color.rgb(51, 255, 0));
        Font font = Font.loadFont("file:src/digital-7 (mono).ttf", 70);
        text.setFont(font);
        root.getChildren().add(text);
        Text counter = new Text();
        counter.setText(Integer.toString(numOfParking));
        counter.setX(950);
        counter.setY(500);
        counter.setFill(Color.rgb(51, 255, 0));
        counter.setFont(font);
        root.getChildren().add(counter);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();

        ServerSocket serverSocket = new ServerSocket(28002);
        System.out.println("The Server is waiting for a client on port 28002");//only for the terminal
        Socket clientSocket = serverSocket.accept();// Accepts the connection for the client socket
        System.out.println("Connected");//only for the terminal
        

        new Thread(() -> {//made another thread so the main one won't clog up and freeze the app
            try (InputStreamReader ir = new InputStreamReader(clientSocket.getInputStream())) {
                BufferedReader br = new BufferedReader(ir);
                int message;
                while (true) {
                    message = br.read();
                    if (message == 1) {
                        numOfParking--;
                        System.out.println("Available Parking: " + numOfParking);//only for the terminal
                        counter.setText(Integer.toString(numOfParking));

                    } else if (message == 2) {
                        numOfParking++;
                        System.out.println("Available Parking: " + numOfParking);//only for the terminal
                        counter.setText(Integer.toString(numOfParking));

                    }
                    if (!stage.isFullScreen()) {//to close the app if the window got minimized
                        clientSocket.close();
                        serverSocket.close();
                        Platform.exit();
                        System.exit(0);
                    }
                }
            } catch (IOException e)   {
                e.printStackTrace();
            }
        }).start();
        

    }

    public static void main(String[] args) throws IOException {
        launch();
        
    }

}