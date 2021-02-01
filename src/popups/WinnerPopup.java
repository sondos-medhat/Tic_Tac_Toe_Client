package popups;

import Main.EntryPoint;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.GameConfig;
import util.SwitchSceneTo;

public class WinnerPopup extends AnchorPane {

    protected final Pane pane;
    protected final ImageView imageView;
    protected final ImageView imageView0;
    protected final Button button;
    protected final Button button0;
    protected final ImageView imageView1;
    protected final ImageView imageView2;
    protected final ImageView imageView3;
    protected final InnerShadow innerShadow;

    public WinnerPopup() {

        pane = new Pane();
        imageView = new ImageView();
        imageView0 = new ImageView();
        button = new Button();
        button0 = new Button();
        imageView1 = new ImageView();
        imageView2 = new ImageView();
        imageView3 = new ImageView();
        innerShadow = new InnerShadow();

        setId("AnchorPane");
        setPrefHeight(300.0);
        setPrefWidth(400.0);
        getStyleClass().add("popupanchor");
        //getStylesheets().add("/popups/../styles/Stye.css");
        pane.getStylesheets().add(String.valueOf(EntryPoint.class.getResource("/styles/Stye.css")));

        pane.setPrefHeight(300.0);
        pane.setPrefWidth(400.0);
        pane.getStyleClass().add("popuppane");
        //pane.getStylesheets().add("/popups/../styles/Stye.css");
        pane.getStylesheets().add(String.valueOf(EntryPoint.class.getResource("/styles/Stye.css")));

        imageView.setFitHeight(416.0);
        imageView.setFitWidth(423.0);
        imageView.setLayoutX(-8.0);
        imageView.setLayoutY(-58.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        //.setImage(new Image(getClass().getResource("../resources/images/neon_abstract.png").toExternalForm()));
        imageView.setImage(new Image(EntryPoint.class.getResource("/resources/images/neon_abstract.png").toExternalForm()));

        imageView0.setFitHeight(119.0);
        imageView0.setFitWidth(198.0);
        imageView0.setLayoutX(101.0);
        imageView0.setLayoutY(94.0);
        imageView0.setPickOnBounds(true);
        imageView0.setPreserveRatio(true);
        //imageView0.setImage(new Image(getClass().getResource("../resources/images/winner.gif").toExternalForm()));
        imageView0.setImage(new Image(EntryPoint.class.getResource("/resources/images/winner.gif").toExternalForm()));

        button.setLayoutX(14.0);
        button.setLayoutY(255.0);
        button.setMnemonicParsing(false);
        button.setPrefHeight(25.0);
        button.setPrefWidth(96.0);
        button.getStyleClass().add("cancelbuutton");
        button.setText("Replay");

        button0.setLayoutX(290.0);
        button0.setLayoutY(255.0);
        button0.setMnemonicParsing(false);
        button0.setPrefHeight(25.0);
        button0.setPrefWidth(96.0);
        button0.getStyleClass().add("cancelbuutton");
        button0.setText("Leave");

        imageView1.setFitHeight(279.0);
        imageView1.setFitWidth(247.0);
        imageView1.setLayoutX(77.0);
        imageView1.setLayoutY(8.0);
        imageView1.setPickOnBounds(true);
        imageView1.setPreserveRatio(true);
        //imageView1.setImage(new Image(getClass().getResource("../resources/images/fireworks.gif").toExternalForm()));
        imageView1.setImage(new Image(EntryPoint.class.getResource("/resources/images/fireworks.gif").toExternalForm()));

        imageView2.setFitHeight(150.0);
        imageView2.setFitWidth(125.0);
        imageView2.setLayoutX(-18.0);
        imageView2.setLayoutY(121.0);
        imageView2.setPickOnBounds(true);
        imageView2.setPreserveRatio(true);
        //imageView2.setImage(new Image(getClass().getResource("../resources/images/celebrating_left.gif").toExternalForm()));
        imageView2.setImage(new Image(EntryPoint.class.getResource("/resources/images/celebrating_left.gif").toExternalForm()));

        imageView3.setFitHeight(150.0);
        imageView3.setFitWidth(200.0);
        imageView3.setLayoutX(290.0);
        imageView3.setLayoutY(120.0);
        imageView3.setPickOnBounds(true);
        imageView3.setPreserveRatio(true);
        //imageView3.setImage(new Image(getClass().getResource("../resources/images/celebrating_right.gif").toExternalForm()));
        imageView3.setImage(new Image(EntryPoint.class.getResource("/resources/images/celebrating_right.gif").toExternalForm()));
        pane.setOpaqueInsets(new Insets(0.0));

        innerShadow.setBlurType(javafx.scene.effect.BlurType.GAUSSIAN);
        innerShadow.setChoke(0.19);
        innerShadow.setHeight(255.0);
        innerShadow.setOffsetX(10.0);
        innerShadow.setOffsetY(10.0);
        innerShadow.setRadius(127.0);
        innerShadow.setWidth(255.0);
        pane.setEffect(innerShadow);

        pane.getChildren().add(imageView);
        pane.getChildren().add(imageView0);
        pane.getChildren().add(button);
        pane.getChildren().add(button0);
        pane.getChildren().add(imageView1);
        pane.getChildren().add(imageView2);
        pane.getChildren().add(imageView3);
        getChildren().add(pane);

    }

    public void display(){
        // button ---> Replay
        // button0 ---> Leave
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.TRANSPARENT);

        button.setOnAction((e)->{
            if(GameConfig.getGameMode() == 1){  // Single player
                SwitchSceneTo.showScene(5);
            }else if(GameConfig.getGameMode() == 2){
                SwitchSceneTo.showScene(3);
            }

            window.close();
        });
        button0.setOnAction((e)->{
            SwitchSceneTo.showScene(1);
            window.close();
        });


        Scene scene = new Scene(this);
         scene.setFill(Color.TRANSPARENT);
        window.setScene(scene);
        window.showAndWait();
    }
}
