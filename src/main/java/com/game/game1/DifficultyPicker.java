package com.game.game1;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class DifficultyPicker extends VBox {

    private ImageView circleImage;
    private ImageView ideImage;

    private String circleEmpty = "grey_circle.png";
    private String circleFull = "green_circle.png";

    private DIFF diff;

    private boolean isFull;

    public DifficultyPicker(DIFF diff){
        circleImage = new ImageView(circleEmpty);
        ideImage = new ImageView(diff.getCharacter());
        this.diff = diff;
        isFull = false;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.getChildren().add(circleImage);
        this.getChildren().add(ideImage);
    }
    public DIFF getDiff(){
        return diff;
    }
    public boolean getIsFull(){
        return isFull;
    }

    void setIsFull(boolean isFull){
        this.isFull = isFull;
        String imageToSet = this.isFull ? circleFull : circleEmpty;
        circleImage.setImage(new Image(imageToSet));
    }
}
