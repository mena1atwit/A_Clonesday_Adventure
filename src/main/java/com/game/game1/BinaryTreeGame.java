package com.game.game1;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class BinaryTreeGame {
    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;

    private static final int GAME_WIDTH = 1920;
    private static final int GAME_HEIGHT = 1080;

    private Stage menuStage;
    private ImageView Player;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isSpaceKeyPressed;
    private boolean isDownKeyPressed;
    private int angle;
    private AnimationTimer gameTimer;

    private GridPane gridPane1;
    private GridPane gridPane2;
    private GridPane gridPane3;
    private final static String BACKGROUND_IMAGE = "forest.png";
    private final static String BACKGROUND_IMAGE2 =  "grass.jpg";

    private static String ENEMY_IMAGE = "Guy.png";

    private ImageView[] enemy;
    Random randomPositionGenerator;

    private ImageView star;
    private InGameLabel points;
    private ImageView[] lives;
    private int playerLives;
    private int playerPoints;
    private final static String STAR_IMAGE = "element_red_polygon.png";

    double fallSpeed = 0;
    int terminalVelocity = 5;

    private static int STAR_RADIUS = 12;
    private static int PLAYER_RADIUS = 27;
    private static int ENEMY_RADIUS = 20;

    private static String BOSS_IMAGE = "tree.png";
    private ImageView Croc;
    private static String Beam_Image = "Beam.png";
    private static ImageView beam;
    private static ImageView beam2;

    private static String Preroot = "PreOrderR.png";
    private static String Preleft = "PreOrderL.png";
    private static String Preright = "PreOrderRi.png";
    private static String Inroot = "InorderR.png";
    private static String Inleft = "InorderL.png";
    private static String Inright = "InorderRi.png";
    private static String Postroot = "PostOrderR.png";
    private static String Postleft = "PostOrderL.png";
    private static String Postright = "PostOrderRi.png";


    private static ImageView inorderR;
    private static ImageView inorderL;
    private static ImageView inorderRi;
    private static ImageView preorderR;
    private static ImageView preorderL;
    private static ImageView preorderRi;
    private static ImageView postorderR;
    private static ImageView postorderL;
    private static ImageView postorderRi;

    private static myTimer time;
    public BinaryTree attack;


    public BinaryTreeGame() {
        initializeStage();
        createKeyListeners();
        randomPositionGenerator = new Random();
    }

    //flip the character model
    //key listeners. Need to add jumping and wasd keys.
    private void createKeyListeners() {

        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.LEFT) {
                    isLeftKeyPressed = true;
                } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                    isRightKeyPressed = true;
                }
                else if (keyEvent.getCode() == KeyCode.SPACE){
                    isSpaceKeyPressed = true;
                }
                else if (keyEvent.getCode() == KeyCode.DOWN){
                    isDownKeyPressed = true;
                }
            }
        });

        gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.LEFT) {
                    isLeftKeyPressed = false;
                } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                    isRightKeyPressed = false;
                }
                else if (keyEvent.getCode() == KeyCode.DOWN) {
                    isDownKeyPressed= false;
                }
            }
        });

    }

    private void initializeStage() {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
    }

    public void createNewGame(Stage menuStage, DIFF chosenDiff) {
        this.menuStage = menuStage;
        this.menuStage.hide();
        createBackground();
        createCharacter(chosenDiff);
        createGameElements(chosenDiff);
        createGameLoop();
        gameStage.show();

    }

    private void createGameElements(DIFF chosenDiff){
        attack = new BinaryTree();
        attack.aBinaryTree();
        playerLives= 2;
        star = new ImageView(STAR_IMAGE);
        //gamePane.getChildren().add(star);
        lives = new ImageView[3];

        time = new myTimer();
        beam = new ImageView(Beam_Image);
        beam2 = new ImageView(Beam_Image);
        gamePane.getChildren().add(beam);
        gamePane.getChildren().add(beam2);

        preorderR = new ImageView(Preroot);
        preorderRi = new ImageView(Preright);
        preorderL = new ImageView(Preleft);
        gamePane.getChildren().add(preorderR);
        gamePane.getChildren().add(preorderL);
        gamePane.getChildren().add(preorderRi);

        postorderR = new ImageView(Postroot);
        postorderRi = new ImageView(Postright);
        postorderL = new ImageView(Postleft);
        gamePane.getChildren().add(postorderR);
        gamePane.getChildren().add(postorderL);
        gamePane.getChildren().add(postorderRi);

        inorderR = new ImageView(Inroot);
        inorderL = new ImageView(Inleft);
        inorderRi = new ImageView(Inright);
        gamePane.getChildren().add(inorderR);
        gamePane.getChildren().add(inorderRi);
        gamePane.getChildren().add(inorderL);


        time.start();

        for (int i = 0; i < lives.length; i++){
            lives[i] = new ImageView(chosenDiff.getLife());
            lives[i].setLayoutX(445 + (i*50));
            lives[i].setLayoutY(80);
            gamePane.getChildren().add(lives[i]);
        }


        Image croc = new Image(BOSS_IMAGE);
        Croc = new ImageView(croc);
        setEnemyPosition(Croc);
        gamePane.getChildren().add(Croc);
        Player.toFront();


        enemy = new ImageView[6];

//        for (int i = 0; i < enemy.length; i++){
//            enemy[i] = new ImageView(ENEMY_IMAGE);
//            setEnemyPosition(enemy[i]);
//            gamePane.getChildren().add(enemy[i]);
//        }
    }




    //this method can be used to reuse to boss's ability. fly off the screen, but relocate to be boss when the boss is ready to fire more.



    private void setEnemyPosition(ImageView image){

        Polyline poly = new Polyline();
        poly.getPoints().addAll(new Double[]{
                960.0,-200.0 ,
                960.0, 500.0,});
        PathTransition path = new PathTransition();
        path.setNode(Croc);
        path.setDuration(Duration.seconds(3));
        path.setPath(poly);
        path.setCycleCount(1);
        path.play();


        //image.setLayoutX(randomPositionGenerator.nextInt(370));
        //image.setLayoutY(-randomPositionGenerator.nextInt(3200)+600);
    }
    private void BeamPosition(ImageView image){
        int a = attack.random(attack);
        int b = (int) (Math.random() * 3);


        Polyline poly = new Polyline();
        Polyline poly2 = new Polyline();

        postorderR.setLayoutY(-3500);
        postorderR.setLayoutX(250);
        postorderRi.setLayoutY(-3500);
        postorderRi.setLayoutX(250);
        postorderL.setLayoutY(-3500);
        postorderL.setLayoutX(250);

        inorderR.setLayoutY(350);
        inorderR.setLayoutX(-25000);
        inorderRi.setLayoutY(350);
        inorderRi.setLayoutX(-25000);
        inorderL.setLayoutY(350);
        inorderL.setLayoutX(-25000);

        preorderR.setLayoutY(-2500);
        preorderR.setLayoutX(250);
        preorderRi.setLayoutY(-2500);
        preorderRi.setLayoutX(250);
        preorderL.setLayoutY(-2500);
        preorderL.setLayoutX(250);

        if(a == 0) {
            poly.getPoints().addAll(new Double[]{
                    1000.0, -20000.0,
                    1000.0, 600.0,});

            poly2.getPoints().addAll(new Double[]{
                    1500.0, -20000.0,
                    1500.0, 600.0,});

            if(b==0) {
                FadeTransition fade = new FadeTransition(Duration.seconds(3), preorderR);

                preorderR.setLayoutY(350);
                preorderR.setLayoutX(250);

                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.play();
            }
            if(b==1){
                FadeTransition fade = new FadeTransition(Duration.seconds(3), inorderL);

                inorderL.setLayoutY(350);
                inorderL.setLayoutX(250);

                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.play();
            }
            if(b==2){
                FadeTransition fade = new FadeTransition(Duration.seconds(3), postorderL);

                postorderL.setLayoutY(350);
                postorderL.setLayoutX(250);

                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.play();
            }
        }

        if(a == 1) {
            poly.getPoints().addAll(new Double[]{
                    1500.0, -20000.0,
                    1500.0, 600.0,});

            poly2.getPoints().addAll(new Double[]{
                    500.0, -20000.0,
                    500.0, 600.0,});
            if(b==0) {
                FadeTransition fade = new FadeTransition(Duration.seconds(3), inorderR);
                inorderR.setLayoutY(350);
                inorderR.setLayoutX(250);

                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.play();
            }
            if(b==1){
                FadeTransition fade = new FadeTransition(Duration.seconds(3), preorderL);

                preorderL.setLayoutY(350);
                preorderL.setLayoutX(250);

                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.play();
            }
            if(b==2){
                FadeTransition fade = new FadeTransition(Duration.seconds(3), postorderRi);

                postorderRi.setLayoutY(350);
                postorderRi.setLayoutX(250);

                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.play();
            }
        }

        if(a == 2) {

            poly.getPoints().addAll(new Double[]{
                    500.0, -20000.0,
                    500.0, 600.0,});

            poly2.getPoints().addAll(new Double[]{
                    1000.0, -20000.0,
                    1000.0, 600.0});
            if(b==0) {
                FadeTransition fade = new FadeTransition(Duration.seconds(3), postorderR);

                postorderR.setLayoutY(350);
                postorderR.setLayoutX(250);

                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.play();
            }
            if(b==1){
                FadeTransition fade = new FadeTransition(Duration.seconds(3), inorderRi);

                inorderRi.setLayoutY(350);
                inorderRi.setLayoutX(250);

                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.play();
            }
            if(b==2){
                FadeTransition fade = new FadeTransition(Duration.seconds(3), preorderRi);

                preorderRi.setLayoutY(350);
                preorderRi.setLayoutX(250);

                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.play();
            }
        }

        PathTransition path = new PathTransition();
        PathTransition path2 = new PathTransition();
        path.setNode(beam);
        path.setDuration(Duration.seconds(5));
        path.setPath(poly);
        path.setCycleCount(1);
        path.play();
        path2.setNode(beam2);
        path2.setDuration(Duration.seconds(5));
        path2.setPath(poly2);
        path2.setCycleCount(1);
        path2.play();
    }




    private class myTimer extends AnimationTimer{
        private long prev = 0;

        @Override
        public void handle(long now) {
            long x = now - prev;
            int a = 0;


            if (x > 5e9) {

                prev = now;
                BeamPosition(beam);
                a++;
            }
        }
    }


    private void createCharacter(DIFF chosenDiff) {
        Player = new ImageView(chosenDiff.getCharacter());
        Player.setLayoutX(GAME_WIDTH/2);
        Player.setLayoutY(GAME_HEIGHT - 230);
        gamePane.getChildren().add(Player);
    }


    // each method in this loop is called every frame
    private void createGameLoop() {
        gameTimer = new AnimationTimer() {

            @Override
            public void handle(long now) {

                movePlayer();
                collisionTest();
            }
        };
        gameTimer.start();
    }

    private void movePlayer() {

        if (isLeftKeyPressed && !isRightKeyPressed) {
            if (Player.getLayoutX() > 300) {
                Player.setLayoutX(Player.getLayoutX() - 5);
            }
        }

        if (isRightKeyPressed && !isLeftKeyPressed) {
            if (Player.getLayoutX() < 1600) {
                Player.setLayoutX(Player.getLayoutX() + 5);
            }
        }
        //this will need to be changed once we add platforms
        if (isSpaceKeyPressed) {
            if (Player.getLayoutY() > GAME_HEIGHT - 200) {
                fallSpeed += .2;
                System.out.println(fallSpeed);
                Player.setLayoutY(Player.getLayoutY() - 5 + fallSpeed);
                if (Player.getLayoutY() == GAME_HEIGHT - 90){
                    isSpaceKeyPressed = false;
                    fallSpeed = 0;
                }

            }else{
                isSpaceKeyPressed = false;
            }
        }
        if (isDownKeyPressed) {
            Player.setLayoutY(GAME_HEIGHT - 90);
        }
    }

    private void createBackground(){
        gridPane1 = new GridPane();
        gridPane2 = new GridPane();
        gridPane3 = new GridPane();


        for (int i = 0; i < 12; i++){

            ImageView backgroundImage1 = new ImageView(BACKGROUND_IMAGE);
            ImageView backgroundImage2 = new ImageView(BACKGROUND_IMAGE);
            ImageView backgroundImage3 = new ImageView(BACKGROUND_IMAGE2);

            GridPane.setConstraints(backgroundImage1, i% 20, i/2);
            GridPane.setConstraints(backgroundImage2, i% 3, i/3);
            GridPane.setConstraints(backgroundImage3, i% 4, i/4);
            gridPane1.getChildren().add(backgroundImage1);
            gridPane2.getChildren().add(backgroundImage2);
            gridPane3.getChildren().add(backgroundImage3);


        }
        gridPane2.setLayoutY(-1024);

        //gamePane.getChildren().addAll(gridPane1,gridPane2);
        gamePane.getChildren().add(gridPane1);
    }


    private void collisionTest() {
        if(Player.getBoundsInParent().intersects(beam.getBoundsInParent())||Player.getBoundsInParent().intersects(beam2.getBoundsInParent()) ) {
            gamePane.getChildren().remove(beam);
            removeLife();

        }
    }


    private void removeLife(){
        gamePane.getChildren().remove(lives[playerLives]);
        playerLives--;
        if (playerLives <0){
            gameStage.close();
            time.stop();
            gameTimer.stop();
            menuStage.show();
        }
    }



}

