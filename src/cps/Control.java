/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cps;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author HN
 */
public class Control extends Application {

    //Gap
    private final int region_horizental_gap = 20;
    private final int region_vertical_gap = 20;    
    private final int component_title_gap = 20;
    private final int component_horizental_gap = 10;
    private final int component_vertical_gap = 10;    
    private final int control_horizental_gap = 50;
    
    
    //Size
    private final int size_icon_big = 150;
    private final int size_icon_medium = 50;
    private final int size_icon_small = 20;
    private final int size_icon_tiny = 10;
    private final int size_padding = 20;
    
    //Car selection
    private final int height_individual_car = 250;    
    private final int height_list_car = 200;
    private final int width_list_car = 100;
    
    private final int height_thumbnail = 100;
    private final int width_description_car = 250;
    
    
    private final int width_scene = 1000;
    private final int height_scene = 800;
    
    //Car selection
    private ListView<Vehicle> lv_vehicles = new ListView<>();
    private ObservableList<Vehicle> vehicles;    
    
    //Behavior Drag & Drop 
    private final ObjectProperty<TreeCell<String>> dragSource = new SimpleObjectProperty<>();
    private static final DataFormat JAVA_FORMAT = new DataFormat("application/x-java-serialized-object");
    private TreeItem<String> draggedItem;
    private ListView<String> lv_current_behaviors;
 
    @Override
    public void start(Stage stage) {
        GridPane grid = new GridPane();
        grid.setId("control-grid");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(region_horizental_gap);
        grid.setVgap(region_vertical_gap);
        grid.setPadding(new Insets(size_padding,size_padding,size_padding,size_padding));
        //grid.setGridLinesVisible(true);
        
        
        //***************************************************
        //******************** Selection ********************
        //***************************************************

        HBox hbox_selection = new HBox();
        hbox_selection.setMaxHeight(height_individual_car);
        hbox_selection.setSpacing(component_horizental_gap);
        
        //Title + Thumbnail + Description
        VBox vbox_vehicle = new VBox();
        vbox_vehicle.setAlignment(Pos.CENTER);
        vbox_vehicle.setSpacing(component_title_gap);
        
        //Title
        Text txt_vehicle_name = new Text("Select a vehicle");
        txt_vehicle_name.setId("heading1-text");
        vbox_vehicle.getChildren().add(txt_vehicle_name);
        
        ImageView thumbnail = new ImageView();
        thumbnail.setFitHeight(height_thumbnail);
        thumbnail.setPreserveRatio(true);
        thumbnail.setSmooth(true);
        thumbnail.setCache(true);
        vbox_vehicle.getChildren().add(thumbnail);
        
        Text vehicle_desc = new Text();
        vehicle_desc.setId("normal-text");
        vehicle_desc.setWrappingWidth(width_description_car);
        vbox_vehicle.getChildren().add(vehicle_desc);
                
        //List View
        vehicles = FXCollections.observableArrayList(dummyListVehicles());
        
        lv_vehicles.setId("vehicle-listview");
        lv_vehicles.setItems(vehicles);
        lv_vehicles.setPrefHeight(height_list_car);
        lv_vehicles.setPrefWidth(width_list_car);
        lv_vehicles.setCellFactory((ListView<Vehicle> l) -> new ColorRectCell());
        
        //Handle select item
        lv_vehicles.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Vehicle>() {

            @Override
            public void changed(ObservableValue<? extends Vehicle> observable, Vehicle oldValue, Vehicle newValue) {
                // Action here
                System.out.println("Selected vehicle: " + newValue.getName());
                txt_vehicle_name.setText(newValue.getName());
                vehicle_desc.setText(newValue.getDescription());
                thumbnail.setImage(new Image(newValue.getImg()));
            }
        });
        
        hbox_selection.getChildren().add(lv_vehicles);
        hbox_selection.getChildren().add(vbox_vehicle);
        grid.add(hbox_selection,0,0);
        
        //***************************************************
        //*********************** Map ***********************
        //***************************************************
        /*VBox vbox_map = new VBox();
        vbox_map.setAlignment(Pos.CENTER);
        vbox_map.setStyle("-fx-background-color: white;");
        
        Text txt_map = new Text("Map");
        txt_map.setId("heading1-text");
        vbox_map.getChildren().add(txt_map);
        
        Rectangle rec_map = new Rectangle();
        rec_map.setX(50);
        rec_map.setY(50);
        rec_map.setWidth(300);
        rec_map.setHeight(200);
        rec_map.setStyle("-fx-background-color: white;");
        vbox_map.getChildren().add(rec_map);
        
        grid.add(vbox_map, 1,0);*/
        
        //***************************************************
        //#################### Control #####################
        //***************************************************
        VBox vbox_control = new VBox();
        vbox_control.setSpacing(region_vertical_gap);
        vbox_control.setAlignment(Pos.CENTER);
        
        //Parameter box
        HBox hbox_control_parameter = new HBox();
        hbox_control_parameter.setAlignment(Pos.CENTER);
        hbox_control_parameter.setSpacing(control_horizental_gap);
        
        //Offset box
        VBox vbox_control_parameter_offset = new VBox();  
        vbox_control_parameter_offset.setAlignment(Pos.CENTER);
        
        //Offset icon
        ImageView iv_control_parameter_offset= new ImageView(new Image("cps/img/Icon/offset.png"));
        iv_control_parameter_offset.setFitHeight(size_icon_medium);
        iv_control_parameter_offset.setPreserveRatio(true);
        iv_control_parameter_offset.setSmooth(true);
        iv_control_parameter_offset.setCache(true);
        vbox_control_parameter_offset.getChildren().add(iv_control_parameter_offset);
         
        //Offset label
        Text txt_control_parameter_offset = new Text("12.23");
        txt_control_parameter_offset.setId("label-text");
        vbox_control_parameter_offset.getChildren().add(txt_control_parameter_offset);
        
        hbox_control_parameter.getChildren().add(vbox_control_parameter_offset);
        
        //Speed box
        VBox vbox_control_parameter_speed = new VBox();   
        vbox_control_parameter_speed.setAlignment(Pos.CENTER);
        
        //Speed icon
        ImageView iv_control_parameter_speed= new ImageView(new Image("cps/img/Icon/speed.png"));
        iv_control_parameter_speed.setFitHeight(size_icon_medium);
        iv_control_parameter_speed.setPreserveRatio(true);
        iv_control_parameter_speed.setSmooth(true);
        iv_control_parameter_speed.setCache(true);
        vbox_control_parameter_speed.getChildren().add(iv_control_parameter_speed);
        
        //Speed label
        Text txt_control_parameter_speed = new Text("12 km/h");
        txt_control_parameter_speed.setId("label-text");
        vbox_control_parameter_speed.getChildren().add(txt_control_parameter_speed);
        
        hbox_control_parameter.getChildren().add(vbox_control_parameter_speed);
        
        //Battery box
        VBox vbox_control_parameter_battery = new VBox();     
        vbox_control_parameter_battery.setAlignment(Pos.CENTER);
        
        //Battery icon
        ImageView iv_control_parameter_battery= new ImageView(new Image("cps/img/Icon/battery.png"));
        iv_control_parameter_battery.setFitHeight(size_icon_medium);
        iv_control_parameter_battery.setPreserveRatio(true);
        iv_control_parameter_battery.setSmooth(true);
        iv_control_parameter_battery.setCache(true);
        vbox_control_parameter_battery.getChildren().add(iv_control_parameter_battery);
        
        //Batter label
        Text txt_control_parameter_battery = new Text("75%");
        txt_control_parameter_battery.setId("label-text");
        vbox_control_parameter_battery.getChildren().add(txt_control_parameter_battery);
              
        hbox_control_parameter.getChildren().add(vbox_control_parameter_battery);
        
        vbox_control.getChildren().add(hbox_control_parameter);
        
        //Vehicle box
        VBox vbox_control_vehicle = new VBox();
        vbox_control_vehicle.setAlignment(Pos.CENTER);
        
        //Vehicle icon
        ImageView iv_control_vehicle= new ImageView(new Image("cps/img/Icon/vehicle_control.png"));
        iv_control_vehicle.setFitWidth(size_icon_big);
        iv_control_vehicle.setPreserveRatio(true);
        iv_control_vehicle.setSmooth(true);
        iv_control_vehicle.setCache(true);
        vbox_control_vehicle.getChildren().add(iv_control_vehicle);
               
        //Road status
        Text txt_control_road_type = new Text("Power Zone");
        txt_control_road_type.setId("road-type-text");
        vbox_control_vehicle.getChildren().add(txt_control_road_type);
        
        vbox_control.getChildren().add(vbox_control_vehicle);
        
        //Adjust box
        HBox hbox_control_adjust = new HBox();
        hbox_control_adjust.setAlignment(Pos.CENTER);
        hbox_control_adjust.setSpacing(control_horizental_gap);
        
        //Adjust speed box
        VBox vbox_control_adjust_speed = new VBox();
        vbox_control_adjust_speed.setAlignment(Pos.CENTER);
        vbox_control_adjust_speed.setSpacing(component_vertical_gap);
        
        //Adjust speed arrow - UP        
        ImageView iv_control_adjust_speed_up = new ImageView(new Image("cps/img/Icon/arrow-up.png"));
        iv_control_adjust_speed_up.setFitHeight(size_icon_small);      
        iv_control_adjust_speed_up.setPreserveRatio(true);
        iv_control_adjust_speed_up.setSmooth(true);
        iv_control_adjust_speed_up.setCache(true);        
        
        Button btn_control_adjust_speed_up = new Button(); 
        btn_control_adjust_speed_up.setGraphic(iv_control_adjust_speed_up);
        btn_control_adjust_speed_up.setId("adjust-button");
        btn_control_adjust_speed_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {        
                System.out.println("Speed: Increase");            
            }
        });
        vbox_control_adjust_speed.getChildren().add(btn_control_adjust_speed_up);
        
        //Adjust speed text
        Text txt_control_adjust_speed = new Text("Speed");
        txt_control_adjust_speed.setId("label-text");   
        vbox_control_adjust_speed.getChildren().add(txt_control_adjust_speed);
        
        //Adjust speed arrow - DOWN       
        ImageView iv_control_adjust_speed_down = new ImageView(new Image("cps/img/Icon/arrow-down.png"));
        iv_control_adjust_speed_down.setFitHeight(size_icon_small);      
        iv_control_adjust_speed_down.setPreserveRatio(true);
        iv_control_adjust_speed_down.setSmooth(true);
        iv_control_adjust_speed_down.setCache(true);        
        
        Button btn_control_adjust_speed_down = new Button(); 
        btn_control_adjust_speed_down.setGraphic(iv_control_adjust_speed_down);
        btn_control_adjust_speed_down.setId("adjust-button");
        btn_control_adjust_speed_down.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {        
                System.out.println("Speed: Decrease");            
            }
        });
        vbox_control_adjust_speed.getChildren().add(btn_control_adjust_speed_down);
        
        hbox_control_adjust.getChildren().add(vbox_control_adjust_speed);
        
        //Steering wheel
        ImageView iv_control_adjust_steering_wheel= new ImageView(new Image("cps/img/Icon/steering-wheel.png"));
        iv_control_adjust_steering_wheel.setFitHeight(size_icon_medium);
        iv_control_adjust_steering_wheel.setPreserveRatio(true);
        iv_control_adjust_steering_wheel.setSmooth(true);
        iv_control_adjust_steering_wheel.setCache(true);
        
        hbox_control_adjust.getChildren().add(iv_control_adjust_steering_wheel);
        
        
        //Adjust offset box
        VBox vbox_control_adjust_offset = new VBox();
        vbox_control_adjust_offset.setAlignment(Pos.CENTER);
        vbox_control_adjust_offset.setSpacing(component_vertical_gap);
        
        //Adjust offset arrow
        HBox hbox_control_adjust_offset_arrow = new HBox();
        
        //Adjust offset arrow - LEFT
        ImageView iv_control_adjust_offset_left = new ImageView(new Image("cps/img/Icon/arrow-left.png"));
        iv_control_adjust_offset_left.setFitWidth(size_icon_tiny);      
        iv_control_adjust_offset_left.setPreserveRatio(true);
        iv_control_adjust_offset_left.setSmooth(true);
        iv_control_adjust_offset_left.setCache(true);        
        
        Button btn_control_adjust_offset_left = new Button(); 
        btn_control_adjust_offset_left.setGraphic(iv_control_adjust_offset_left);
        btn_control_adjust_offset_left.setId("adjust-button");
        btn_control_adjust_offset_left.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {        
                System.out.println("<<<<<<< Offset");
                
            }
        });
        hbox_control_adjust_offset_arrow.getChildren().add(btn_control_adjust_offset_left);
        
        //Adjust offset arrow - RIGHT
        ImageView iv_control_adjust_offset_right = new ImageView(new Image("cps/img/Icon/arrow-right.png"));
        iv_control_adjust_offset_right.setFitWidth(size_icon_tiny);             
        iv_control_adjust_offset_right.setPreserveRatio(true);
        iv_control_adjust_offset_right.setSmooth(true);
        iv_control_adjust_offset_right.setCache(true);  
        
        Button btn_control_adjust_offset_right = new Button(); 
        btn_control_adjust_offset_right.setGraphic(iv_control_adjust_offset_right);
        btn_control_adjust_offset_right.setId("adjust-button");
        btn_control_adjust_offset_right.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {        
                System.out.println("Offset >>>>>>");
            }
        });
        hbox_control_adjust_offset_arrow.getChildren().add(btn_control_adjust_offset_right);
        
        vbox_control_adjust_offset.getChildren().add(hbox_control_adjust_offset_arrow);
               
        //Adjust offset text
        Text txt_control_adjust_offset = new Text("Offset");
        txt_control_adjust_offset.setId("label-text");       
        vbox_control_adjust_offset.getChildren().add(txt_control_adjust_offset);
             
        hbox_control_adjust.getChildren().add(vbox_control_adjust_offset);
              
        vbox_control.getChildren().add(hbox_control_adjust);
                 
        grid.add(vbox_control, 0,1);
        
        //***************************************************
        //#################### Behavior #####################
        //***************************************************
               
        //Behavior Box
        VBox vbox_behavior = new VBox();
        vbox_behavior.setAlignment(Pos.CENTER);
        vbox_behavior.setSpacing(component_vertical_gap);
        vbox_behavior.setStyle("-fx-background-color: black;");
        
        //Behavior Title
        Text txt_behavior = new Text("Vehicle Behavior");
        txt_behavior.setId("heading1-text");
        vbox_behavior.getChildren().add(txt_behavior);
        
        //Drag and Drop Box
        HBox hbox_behavior_drag_drop = new HBox();
        hbox_behavior_drag_drop.setAlignment(Pos.CENTER);
        
        //Box of list of available behaviors
        VBox vbox_available_behavior = new VBox();
        vbox_available_behavior.setAlignment(Pos.CENTER);
        
        //Title of list of available behaviors
        Text txt_available_behavior = new Text("Available Behavior");
        txt_available_behavior.setId("label-text");
        vbox_available_behavior.getChildren().add(txt_available_behavior);
        
        //List of behavior
        TreeItem<String> behaviors = new TreeItem<> ("Behaviors");
        behaviors.setExpanded(true);
        
        //Behavior: Light effect
        TreeItem<String> behaviors_light_effect = new TreeItem<> ("Light Effect");
        behaviors_light_effect.setExpanded(true);
        for (String item: getLightEffect()){
            behaviors_light_effect.getChildren().add(new TreeItem<> (item));
        }
        behaviors.getChildren().add(behaviors_light_effect);
        
        //Behavior: Light device
        TreeItem<String> behaviors_light_device = new TreeItem<> ("Light Device");
        behaviors_light_device.setExpanded(true);
        for (String item: getLightDevice()){
            behaviors_light_device.getChildren().add(new TreeItem<> (item));
        }        
        behaviors.getChildren().add(behaviors_light_device);
        
        //Tree View of behaviors
        TreeView<String> behavior_tree = new TreeView(behaviors); 
        behavior_tree.setId("behavior-treeview");
        behavior_tree.setCellFactory(param -> {
            TreeCell<String> treeCell = new TreeCell<String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                }
            };
            
            treeCell.setOnDragDetected(event -> {
                draggedItem = treeCell.getTreeItem();

                // root can't be dragged
                if (draggedItem.getParent() == null) 
                    return;
                
                // categories can't be dragged
                if (draggedItem.getParent().getParent() == null) 
                    return;
                
                if (!treeCell.isEmpty()) {
                    Dragboard db = treeCell.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString(treeCell.getItem());
                    db.setContent(cc);
                    dragSource.set(treeCell);
                    db.setDragView(treeCell.snapshot(null, null));
                    event.consume();
                }
            });

            treeCell.setOnDragOver(event -> {
                if (!event.getDragboard().hasContent(JAVA_FORMAT)) 
                    return;
                
                TreeItem<String> thisItem = treeCell.getTreeItem();
                System.out.println("setOnDragOver"); 
                
                // can't drop on itself
                if (draggedItem == null || thisItem == null || thisItem == draggedItem) 
                    return;
                
                // ignore if this is the root
                if (draggedItem.getParent() == null || draggedItem.getParent().getParent() == null)
                    return;

                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                    System.out.println("setOnDragOver");
                }
                
            });

            treeCell.setOnDragDone(event -> {
                System.out.println("TreeCell: setOnDragDone");           
             });

            treeCell.setOnDragDropped(event -> {
                System.out.println("TreeCell: setOnDragDropped");  
            });

            return treeCell;
        });
        
        vbox_available_behavior.getChildren().add(behavior_tree);
        
        hbox_behavior_drag_drop.getChildren().add(vbox_available_behavior);
    
        //Box of list of available behaviors
        VBox vbox_current_behavior = new VBox();
        vbox_current_behavior.setAlignment(Pos.CENTER);
        
        //Title of list of available behaviors
        Text txt_current_behavior = new Text("Current Behavior");
        txt_current_behavior.setId("label-text");
        vbox_current_behavior.getChildren().add(txt_current_behavior);
    
        //List view of current behaviors
        lv_current_behaviors = new ListView<>();
        lv_current_behaviors.setId("behavior-listview");
        for (int i=0; i<4; i++ ) {
            lv_current_behaviors.getItems().add("Test Item "+i);
        }
        
        lv_current_behaviors.setCellFactory(lv -> {
            ListCell<String> listCell = new ListCell<String>(){
                @Override
                public void updateItem(String item , boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                }
            };

            listCell.setOnDragDetected(event -> {
                if (!listCell.isEmpty()) {
                    Dragboard db = listCell.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString(listCell.getItem());
                    db.setContent(cc);
                    System.out.println("listCell: setOnDragDetected");
                }
            });

            listCell.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                    System.out.println("listCell: setOnDragOver");
                }
            });

            listCell.setOnDragDone(event -> {
                if (lv_current_behaviors.getItems().size()>1)
                    lv_current_behaviors.getItems().remove(listCell.getItem());
                System.out.println("listCell: setOnDragDone");           
             });

            listCell.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                             
                if (db.hasString()) {
                    TreeCell<String> dragSourceCell = dragSource.get();
                    if(!lv_current_behaviors.getItems().contains(dragSourceCell.getItem()))
                        lv_current_behaviors.getItems().add(dragSourceCell.getItem());
                    event.setDropCompleted(true);
                    dragSource.set(null);
                } else {
                    event.setDropCompleted(false);
                }
                System.out.println("listCell: setOnDragDropped");   
            });

            return listCell;
        });
        vbox_current_behavior.getChildren().add(lv_current_behaviors);
              
        hbox_behavior_drag_drop.getChildren().add(vbox_current_behavior);        
        
        vbox_behavior.getChildren().add(hbox_behavior_drag_drop);
        
        grid.add(vbox_behavior, 1,1);
        
        Scene scene = new Scene(grid, width_scene, height_scene);
        scene.getStylesheets().add(Connection.class.getResource("design-style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("CPS Control");
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
        v1.setDescription("Lorem ipsum dolor sit amet, ex etiam iriure adipisci eum.Lorem ipsum dolor sit amet, ex etiam iriure adipisci eum");
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
    
    public ImageView getImageView(int size,String url){       
        ImageView imageView = new ImageView (new Image(url));
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);       
        return imageView;
    }
    
    public List<String> getLightEffect() {
        List<String> effects = new ArrayList();
        effects.add("Steady");
        effects.add("Fade");
        effects.add("Throb");
        effects.add("Strobe");
        effects.add("Flash");        
        return effects;
    }
    
    public List<String> getLightDevice() {
        List<String> devices = new ArrayList();
        devices.add("Engine Red");
        devices.add("Engine Blue");
        devices.add("Front Red");
        devices.add("Front Green");
        devices.add("Tail");        
        return devices;
    }
    
}
