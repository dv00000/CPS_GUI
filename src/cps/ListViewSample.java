/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cps;

/**
 *
 * @author HN
 */
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
    
public class ListViewSample extends Application {
 
    ListView<Vehicle> lv_vehicles = new ListView<>();
    ObservableList<Vehicle> vehicles;
 
    @Override
    public void start(Stage stage) {
        GridPane grid = new GridPane();
        grid.setId("control-grid");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setGridLinesVisible(true);
        
        //Title + Thumbnail + Description
        VBox box = new VBox();
        
        //Title
        Text scenetitle = new Text("Select a vehicle");
        scenetitle.setId("heading1-text");
        grid.add(scenetitle, 0, 0, 2, 1);
        box.getChildren().add(scenetitle);
        
        ImageView thumbnail = new ImageView();
        thumbnail.setFitHeight(200);
        thumbnail.setPreserveRatio(true);
        thumbnail.setSmooth(true);
        thumbnail.setCache(true);
        box.getChildren().add(thumbnail);
        
        Text vehicle_desc = new Text();
        vehicle_desc.setId("normal-text");
        box.getChildren().add(vehicle_desc);
        
        grid.add(box, 2,1,4,1);
        
        //List View
        vehicles = FXCollections.observableArrayList(dummyListVehicles());
       
        lv_vehicles.setItems(vehicles);
        lv_vehicles.setPrefWidth(100);
        lv_vehicles.setCellFactory((ListView<Vehicle> l) -> new ColorRectCell());
        //lv_vehicles.getSelectionModel().select(0);
        grid.add(lv_vehicles, 1,1,1,1);
        
        //Handle select item
        lv_vehicles.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Vehicle>() {

            @Override
            public void changed(ObservableValue<? extends Vehicle> observable, Vehicle oldValue, Vehicle newValue) {
                // Action here
                System.out.println("Selected vehicle: " + newValue.getName());
                scenetitle.setText(newValue.getName());
                vehicle_desc.setText(newValue.getDescription());
                thumbnail.setImage(new Image(newValue.getImg()));
                
                //vehicles.add(dummyVehicle());
            }
        });
        
        
        
        
        
        Scene scene = new Scene(grid, 500, 300);
        scene.getStylesheets().add(Connection.class.getResource("design-style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ListViewSample");
        stage.show();
    }
    
    static class ColorRectCell extends ListCell<Vehicle> {
        @Override
        public void updateItem(Vehicle item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null){
                String url = item.getImg();
                Image img = new Image(url);
                ImageView iv = new ImageView(img);
                iv.setFitWidth(60);
                iv.setPreserveRatio(true);
                iv.setSmooth(true);
                iv.setCache(true);
                setGraphic(iv);
            }
 
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public Vehicle dummyVehicle(){
        Vehicle v = new Vehicle();
        v.setName("Bigbang-1");
        v.setType("Bigbang");
        v.setDescription("Lorem ipsum dolor sit amet, ex etiam iriure adipisci eum");
        v.setImg("cps/img/Vehicle/"+v.getType()+".png");
        v.setSpeed(10.5);
        v.setOffset(10.2);
        v.setBattery(75);
        
        return v;
    }
    public List<Vehicle> dummyListVehicles(){
        List<Vehicle> vehicles = new ArrayList();
        
        Vehicle v1 = new Vehicle();
        v1.setName("MXT-1");
        v1.setType("MXT");
        v1.setDescription("Lorem ipsum dolor sit amet, ex etiam iriure adipisci eum");
        v1.setImg("cps/img/Vehicle/"+v1.getType()+".png");
        v1.setSpeed(10.5);
        v1.setOffset(10.2);
        v1.setBattery(75);
        vehicles.add(v1);
        
        Vehicle v2 = new Vehicle();
        v2.setName("MXT-2");
        v2.setType("MXT");
        v2.setDescription("Lorem ipsum dolor sit amet, ex etiam iriure adipisci eum");
        v2.setImg("cps/img/Vehicle/"+v2.getType()+".png");
        v2.setSpeed(10.5);
        v2.setOffset(10.2);
        v2.setBattery(72);
        vehicles.add(v2);
        
        Vehicle v3 = new Vehicle();
        v3.setName("Bigbang-1");
        v3.setType("Bigbang");
        v3.setDescription("Lorem ipsum dolor sit amet, ex etiam iriure adipisci eum");
        v3.setImg("cps/img/Vehicle/"+v3.getType()+".png");
        v3.setSpeed(10.5);
        v3.setOffset(10.2);
        v3.setBattery(70);
        vehicles.add(v3);
        
        Vehicle v4 = new Vehicle();
        v4.setName("Groundshock-1");
        v4.setType("Groundshock");
        v4.setDescription("Lorem ipsum dolor sit amet, ex etiam iriure adipisci eum");
        v4.setImg("cps/img/Vehicle/"+v4.getType()+".png");
        v4.setSpeed(10.5);
        v4.setOffset(10.2);
        v4.setBattery(75);
        vehicles.add(v4);
           
        
        return vehicles;
    }
}