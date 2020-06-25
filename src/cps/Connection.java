/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cps;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author HN
 */
public class Connection extends Application {

    @Override
    public void start(Stage primaryStage) {
      
        GridPane grid = new GridPane();
        grid.setId("connection-grid");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        //grid.setGridLinesVisible(true);
        
        //Title
        Text scenetitle = new Text("Automotive CPS");
        scenetitle.setId("title-text");
        grid.add(scenetitle, 0, 0, 2, 1);
        
        //IP Address label and text field
        Label lb_ip_address = new Label("IP Address:");
        grid.add(lb_ip_address, 0, 1);

        TextField txt_ip_address = new TextField("127.0.0.1");
        grid.add(txt_ip_address, 1, 1);

        //Port label and text field
        Label lb_port = new Label("Port:");
        grid.add(lb_port, 0, 2);

        TextField txt_port = new TextField("5000");
        grid.add(txt_port, 1, 2);
        
        //Announcement text
        final Text announcement = new Text();
        announcement.setId("announcement-text");
        grid.add(announcement, 1, 6);
        
        //Connecting Button
        Button btn_connect = new Button("Connect");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn_connect);
        grid.add(hbBtn, 1, 4);
        
        btn_connect.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent e) {        
                announcement.setText("Button is pressed");
                
                Stage controlStage = new Stage();
                Control control = new Control();
                control.start(controlStage);
                controlStage.show();
                primaryStage.close();
            }
        });
        
        //Progress bar 
        ProgressIndicator pi = new ProgressIndicator();
        pi.setProgress(-1.0f);
        grid.add(pi, 1, 7);
               
        Scene scene = new Scene(grid, 600, 400);
        scene.getStylesheets().add(Connection.class.getResource("design-style.css").toExternalForm());
                
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("CPS Connection");
        primaryStage.show();
    }
    
    /*public static void main(String[] args) {
        launch(args);
    }*/
    
}
