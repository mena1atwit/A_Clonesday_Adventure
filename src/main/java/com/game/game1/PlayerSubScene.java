package com.game.game1;

import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class PlayerSubScene extends SubScene {

    private final static String FONT_PATH = "Kenney Rocket Square.ttf";
    private final static String BACKGROUND_IMAGE = "Map.png";

    private boolean isHidden;

    public PlayerSubScene() {
        super(new AnchorPane(), 706, 623);
        prefWidth(706);
        prefHeight(623);

        BackgroundImage image = new BackgroundImage(new Image(BACKGROUND_IMAGE, 706, 623, false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        AnchorPane root2 = (AnchorPane) this.getRoot();
        root2.setBackground(new Background(image));

        isHidden = true;

        setLayoutX(1024);
        setLayoutY(180);
    }

    public void moveSubScene(){
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(this);

        if(isHidden) {
            transition.setToX(-720);
            isHidden = false;
        }else{
            transition.setToX(0);
            isHidden = true;
        }

        transition.play();
    }
    public AnchorPane getPane(){
        return (AnchorPane) this.getRoot();
    }

}
