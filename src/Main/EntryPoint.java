package Main;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.SwitchSceneTo;

/**
 *
 * @author Sondos Alagmawy
 */
public class EntryPoint extends Application {

    private static GameClient gameClient;
    private static Stage stage;
    private static ViewUpdater viewUpdater;

    @Override
    public void start(Stage stage) throws Exception {
        gameClient = new GameClient();
        EntryPoint.stage = stage;
        // To ensure program terminates when exit icon (x) is pressed
        EntryPoint.stage.setOnCloseRequest((e)-> {
            System.exit(0);
        });
        viewUpdater = new ViewUpdater();
        Parent root = FXMLLoader.load(getClass().getResource("/views/loginmain.fxml"));
        Scene scene = new Scene(root);
        SwitchSceneTo.makeMeMobile(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        stage.setResizable(false);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static GameClient getGameClient(){
        return gameClient;
    }

    public static Stage getStage(){
        return stage;
    }

    public static ViewUpdater getViewUpdater(){
        return viewUpdater;
    }
}
