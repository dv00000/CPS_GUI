/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cps;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author HN
 */
public class DragDropSample extends Application{

    private final int size_icon = 20 ;
    
    
    //Drag & Drop
    private final ObjectProperty<TreeCell<String>> dragSource = new SimpleObjectProperty<>();
    private static final DataFormat JAVA_FORMAT = new DataFormat("application/x-java-serialized-object");
    private TreeItem<String> draggedItem;
    private ListView<String> lv_current_behaviors;
    
    @Override
    public void start(Stage stage) {
        GridPane grid = new GridPane();
        //grid.setId("control-grid");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setPadding(new Insets(25, 25, 25, 25));
              
        //***************************************************
        //#################### Behavior #####################
        //***************************************************
               
        //Behavior Box
        VBox vbox_behavior = new VBox();
        vbox_behavior.setAlignment(Pos.CENTER);
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
        TreeItem<String> behaviors = new TreeItem<> ("Behaviors",getImageView(size_icon,"cps/img/Icon/behavior.png"));
        behaviors.setExpanded(true);
        
        //Behavior: Light effect
        TreeItem<String> behaviors_light_effect = new TreeItem<> ("Light Effect",getImageView(size_icon,"cps/img/Icon/light-effect.png"));
        behaviors_light_effect.setExpanded(true);
        for (String item: getLightEffect()){
            behaviors_light_effect.getChildren().add(new TreeItem<> (item));
        }
        behaviors.getChildren().add(behaviors_light_effect);
        
        //Behavior: Light device
        TreeItem<String> behaviors_light_device = new TreeItem<> ("Light Device",getImageView(size_icon,"cps/img/Icon/light-device.png"));
        behaviors_light_device.setExpanded(true);
        for (String item: getLightDevice()){
            behaviors_light_device.getChildren().add(new TreeItem<> (item));
        }        
        behaviors.getChildren().add(behaviors_light_device);
        
        //Tree View of behaviors
        TreeView<String> behavior_tree = new TreeView(behaviors); 
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
        
        grid.add(vbox_behavior, 1,0);
               
        Scene scene = new Scene(grid, 1000, 900);
        scene.getStylesheets().add(Connection.class.getResource("design-style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("CPS Control");
        stage.show();

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
    
    public static void main(String[] args) {
        launch(args);
    }
}
