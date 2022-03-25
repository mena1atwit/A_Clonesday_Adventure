package com.game.game1;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;



public class ViewManager {

    private static final int HEIGHT = 768;
    private static final int WIDTH = 1024;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    private final static int MENU_BUTTONS_START_X = 85;
    private final static int MENU_BUTTONS_START_Y = 150;



    private boolean kPress;
    private boolean aPress;
    private boolean nPress;
    private boolean pPress;
    private PlayerSubScene creditsSubScene;
    private PlayerSubScene helpSubScene;
    private PlayerSubScene settingsSubScene;
    private PlayerSubScene difficultySubScene;

    private PlayerSubScene sceneToHide;
    private AnimationTimer gameTimer1;
    List<PlayerButton> menuButtons;
    List<DifficultyPicker> difficultyItems;
    private DIFF chosenDiff;



    public ViewManager(){
        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        createSubScenes();
        createButtons();
        createBackground();
        createGameLoop();
        createLogo();

    }
    private void showSubScene(PlayerSubScene subScene){
        //if sceneToHide has a value, and is called, whatever scene it's assigned to is moved out of sight
        if(sceneToHide != null){
            sceneToHide.moveSubScene();
        }
        //arguement subscene is then moved in and assigned to sceneToHide
        subScene.moveSubScene();
        sceneToHide = subScene;
    }

    private void createSubScenes(){
        creditsSubScene = new PlayerSubScene();
        mainPane.getChildren().add(creditsSubScene);

        helpSubScene = new PlayerSubScene();
        mainPane.getChildren().add(helpSubScene);

        settingsSubScene = new PlayerSubScene();
        mainPane.getChildren().add(settingsSubScene);

        createDifficultySubScene();

    }
    private void createGameLoop() {
        gameTimer1 = new AnimationTimer() {

            @Override
            public void handle(long now) {

                mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        if (keyEvent.getCode() == KeyCode.K) {
                            kPress = true;
                            pPress = false;
                            nPress = false;
                            aPress = false;
                        }
                        else if (keyEvent.getCode() == KeyCode.N) {
                            nPress = true;
                            kPress = false;
                            pPress = false;
                            aPress = false;
                        }
                        else if (keyEvent.getCode() == KeyCode.A) {
                            aPress = true;
                            kPress = false;
                            nPress = false;
                            pPress = false;
                        }
                        else if (keyEvent.getCode() == KeyCode.P) {
                            pPress = true;
                            kPress = false;
                            nPress = false;
                            aPress = false;
                        }
                    }
                });

            }
        };
        gameTimer1.start();

    }

    private void createDifficultySubScene() {
        difficultySubScene = new PlayerSubScene();
        mainPane.getChildren().add(difficultySubScene);

        infoLabel chooseDifficulty = new infoLabel();
        chooseDifficulty.setLayoutX(165);
        chooseDifficulty.setLayoutY(45);
        difficultySubScene.getPane().getChildren().add(chooseDifficulty);
        difficultySubScene.getPane().getChildren().add(createDifficultyToChoose());
        difficultySubScene.getPane().getChildren().add(createButtonToStart());
    }

    private HBox createDifficultyToChoose(){
        HBox box = new HBox();
        box.setSpacing(100);
        difficultyItems = new ArrayList<>();
        for(DIFF diff : DIFF.values()){
            DifficultyPicker diffToPick= new DifficultyPicker(diff);
            difficultyItems.add(diffToPick);
            box.getChildren().add(diffToPick);
            diffToPick.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    for (DifficultyPicker diff : difficultyItems){
                        diff.setIsFull(false);
                    }
                    diffToPick.setIsFull(true);
                    chosenDiff = diffToPick.getDiff();
                }
            });
        }
        box.setLayoutX(440-(118*2));
        box.setLayoutY(122);
        return box;
    }

    private PlayerButton createButtonToStart(){
        PlayerButton startButton = new PlayerButton("startButton.png");
        startButton.setLayoutX(350);
        startButton.setLayoutY(320);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(chosenDiff != null) {
                    if (pPress) {
                        //play payton game
                        stackBoss gameManager = new stackBoss();
                        gameManager.createNewGame(mainStage, chosenDiff);


                    } else if (kPress) {
                        //play kris game
                        queueBoss queueGameManager = new queueBoss();
                        queueGameManager.createNewGame(mainStage, chosenDiff);

                    } else if (aPress) {
                        //play alex game
                        mergeSortBoss alexGameviewManager = new mergeSortBoss();
                        alexGameviewManager.createNewGame(mainStage,chosenDiff);

                    } else if (nPress) {
                        //play nael game
                        BinaryTreeGame treeGameManager = new BinaryTreeGame();
                        treeGameManager.createNewGame(mainStage, chosenDiff);

                    }
                }

            }
        });


        return startButton;
    }



    public Stage getStage(){
        return mainStage;
    }

    private void addMenuButton(PlayerButton button){
        button.setLayoutX(MENU_BUTTONS_START_X);
        button.setLayoutY(MENU_BUTTONS_START_Y + menuButtons.size()*100);
        menuButtons.add(button);
        mainPane.getChildren().add(button);
    }
    private void createButtons() {
        createStartButton();
        createSettingButton();
        createHelpButton();
        createCreditsButton();
        createExitButton();
    }

    private void createStartButton(){
        PlayerButton startButton = new PlayerButton("playButton.png");
        addMenuButton(startButton);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(difficultySubScene);
            }
        });
    }
    private void createSettingButton(){
        PlayerButton settingsButton = new PlayerButton("settingsButton.png");
        addMenuButton(settingsButton);

        settingsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(settingsSubScene);
            }
        });
    }
    private void createHelpButton(){
        PlayerButton helpButton = new PlayerButton("helpButton.png");
        addMenuButton(helpButton);

        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(helpSubScene);
            }
        });
    }
    private void createCreditsButton(){
        PlayerButton creditsButton = new PlayerButton("creditsButton.png");
        addMenuButton(creditsButton);

        creditsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(creditsSubScene);
            }
        });
    }
    private void createExitButton(){
        PlayerButton exitButton = new PlayerButton("exitButton.png");
        addMenuButton(exitButton);

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.close();
            }
        });
    }

    private void createBackground(){
        Image backgroundImage = new Image("viewManagerBackground.png", 1025, 769, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        mainPane.setBackground(new Background(background));
    }

    private void createLogo(){
        ImageView logo = new ImageView("clonesLogo.png");
        logo.setLayoutX(WIDTH/2-logo.getImage().getWidth()/2);
        logo.setLayoutY(25);

        logo.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                logo.setEffect(new DropShadow());
            }
        });
        logo.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                logo.setEffect(null);
            }
        });
        mainPane.getChildren().add(logo);
    }
}
