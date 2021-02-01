/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import popups.ExitGamePopup;
import util.GameConfig;
import util.SwitchSceneTo;
import util.GameSound;

/**
 * FXML Controller class
 *
 * @author Hager
 */
public class AboutController implements Initializable {
    
    private final int viewIndex = 7;
    @FXML
    private Button backBtn;
    @FXML
    private FontAwesomeIconView exitGameIcon;
    @FXML
    private Label gameTitleLetter1;
    @FXML
    private Label gameTitleLetter2;
    @FXML
    private Label gameTitleLetter3;
    @FXML
    private Label gameTitleLetter4;
    @FXML
    private Label gameTitleLetter5;
    @FXML
    private Label gameTitleLetter6;
    @FXML
    private Label gameTitleLetter7;
    @FXML
    private Label gameTitleLetter8;
    @FXML
    private Label gameTitleLetter9;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GameConfig.setCurrentView(viewIndex);
        animateScreen();
        GameSound.stopMediaPlayer();
        GameSound.playAboutTrack();
    }

    @FXML
    private void handleBackBtnAction(ActionEvent event) {
       GameSound.playClickTrack();
        GameSound.getAudioClip().stop();
        SwitchSceneTo.homeScene(event);
    }

    @FXML
    private void exitGameClicked(MouseEvent event) {
        GameSound.playClickTrack();
        Platform.runLater(()->{
           
                ExitGamePopup ExitGamePopup = new ExitGamePopup();
                ExitGamePopup.display();
            });
    }

    
        private void animateScreen() {
        TranslateTransition transition1 = new TranslateTransition();
        TranslateTransition transition2 = new TranslateTransition();
        TranslateTransition transition3 = new TranslateTransition();

        TranslateTransition transition4 = new TranslateTransition();
        TranslateTransition transition5 = new TranslateTransition();
        TranslateTransition transition6 = new TranslateTransition();

        TranslateTransition transition7 = new TranslateTransition();
        TranslateTransition transition8 = new TranslateTransition();
        TranslateTransition transition9 = new TranslateTransition();

        double i = 0.3;

        transition1.setDuration(Duration.seconds(i));
        transition1.setNode(gameTitleLetter1);
        transition1.setToY(20);
        transition1.setAutoReverse(true);
        transition1.setCycleCount(TranslateTransition.INDEFINITE);
        transition1.play();

        transition2.setDuration(Duration.seconds(i));
        transition2.setNode(gameTitleLetter2);
        transition2.setToY(-20);
        transition2.setAutoReverse(true);
        transition2.setCycleCount(TranslateTransition.INDEFINITE);
        transition2.play();

        transition3.setDuration(Duration.seconds(i));
        transition3.setNode(gameTitleLetter3);
        transition1.setToX(20);
        transition3.setToY(20);
        transition3.setAutoReverse(true);
        transition3.setCycleCount(TranslateTransition.INDEFINITE);
        transition3.play();

        Duration duration = Duration.millis(6500);
        RotateTransition rotateTransition = new RotateTransition(duration, gameTitleLetter3);
        rotateTransition.setByAngle(300);
        rotateTransition.setAutoReverse(true);
        rotateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        rotateTransition.play();

        transition4.setDuration(Duration.seconds(i));
        transition4.setNode(gameTitleLetter4);
        transition4.setToY(-20);
        transition4.setAutoReverse(true);
        transition4.setCycleCount(TranslateTransition.INDEFINITE);
        transition4.play();

        transition5.setDuration(Duration.seconds(i));
        transition5.setNode(gameTitleLetter5);
        transition5.setToY(20);
        transition5.setAutoReverse(true);
        transition5.setCycleCount(TranslateTransition.INDEFINITE);
        transition5.play();

        transition6.setDuration(Duration.seconds(i));
        transition6.setNode(gameTitleLetter6);
        transition6.setToX(10);
        transition6.setToY(20);
        transition6.setAutoReverse(true);
        transition6.setCycleCount(TranslateTransition.INDEFINITE);
        transition6.play();

        transition7.setDuration(Duration.seconds(i));
        transition7.setNode(gameTitleLetter7);
        transition7.setToY(-20);
        transition7.setAutoReverse(true);
        transition7.setCycleCount(TranslateTransition.INDEFINITE);
        transition7.play();

        transition8.setDuration(Duration.seconds(i));
        transition8.setNode(gameTitleLetter8);
        transition8.setToY(20);
        transition8.setAutoReverse(true);
        transition8.setCycleCount(TranslateTransition.INDEFINITE);
        transition8.play();

        transition9.setDuration(Duration.seconds(i));
        transition9.setNode(gameTitleLetter9);
        transition9.setToY(-18);
        transition9.setAutoReverse(true);
        transition9.setCycleCount(TranslateTransition.INDEFINITE);
        transition9.play();

        Duration duration2 = Duration.millis(2500);
        RotateTransition rotateTransition2 = new RotateTransition(duration2, gameTitleLetter9);
        rotateTransition2.setCycleCount(TranslateTransition.INDEFINITE);
        rotateTransition2.setByAngle(300);
        rotateTransition2.play();

 
    }
}
