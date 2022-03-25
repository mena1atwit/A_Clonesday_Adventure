package com.game.game1;

import javafx.application.Application;
//import com.game.game1.ViewManager;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ViewManager manager = new ViewManager();
        stage = manager.getStage();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}