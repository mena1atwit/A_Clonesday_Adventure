package com.game.game1;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class stackBoss {
    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;
//600 x 800
    private static final int GAME_WIDTH = 1920;
    private static final int GAME_HEIGHT = 1080;

    private List playerAttack;
    private Stage menuStage;
    private ImageView Player;

    private boolean isShiftPressed;
    private double bossProjSpeed = 3;
    private boolean fallingoffTopPlat = false;
    private boolean onPlat;
    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isSpaceKeyPressed;
    private boolean isDownKeyPressed;
    private AnimationTimer gameTimer;
    private double groundHeight = GAME_HEIGHT - 90;
    private double jumpHeight = 210;
    private GridPane gridPane1;
    private GridPane gridPane2;
    private static String BLUE_IMAGE = "blue.png";
    private static String BACKGROUND_IMAGE = "foodbackground.png";
    private static String PLATFORM_IMAGE = "medplat.png";
    private static String STACKBOSS_IMAGE = "burgerboss.png";
    private static String PATTY_ATTACK = "patty.png";
    private static String TOMATO_ATTACK = "tomato.png";
    private static String CHEESE_ATTACK = "cheese.png";
    private static String GREEN_PLAYER_REVERSE = "PlayerGreenReverse.png";
    private static String GREEN_PLAYER = "PlayerGreen.png";
    private static String RED_PLAYER = "PlayerRed.png";
    private static String RED_PLAYER_REVERSE = "PlayerRedReverse.png";
    private static String BLUE_PLAYER = "PlayerBlue.png";
    private static String BLUE_PLAYER_REVERSE = "PlayerBlueReverse.png";

    ViewManager manager = new ViewManager();
    private int difficulty;
    private boolean lostLife =false;
    private double projSpeed;
    Object testObject;
    private ImageView[] enemy;
    Random randomPositionGenerator;
    private boolean falling;

    private Image playerGreenReverse = new Image(GREEN_PLAYER_REVERSE);
    private Image playerGreen = new Image(GREEN_PLAYER);
    private Image playerRedReverse = new Image(RED_PLAYER_REVERSE);
    private Image playerRed = new Image(RED_PLAYER);
    private Image playerBlueReverse = new Image(BLUE_PLAYER_REVERSE);
    private Image playerBlue = new Image(BLUE_PLAYER);


    private ImageView[] blue;
    private ImageView patty;
    private ImageView tomato;
    private ImageView cheese;
    private ImageView[] stackBossAttacks;
    private ImageView stackBossImageView;
    private ImageView star;
    private ImageView[] platforms;
    private InGameLabel points;
    private ImageView[] lives;
    private int playerLives;
    private int playerPoints;
    private final static String STAR_IMAGE = "element_red_polygon.png";
    private double bossStart = 1000;
    private boolean setEnemyY = false;
    private boolean setEnemyX = false;
    private boolean canStartBoss = false;
    private boolean bossAttackedOnce = false;

    ArrayStack stackBoss = new ArrayStack(3);

    double fallSpeed = 0;
    int terminalVelocity = 5;

    boolean notHit = true;
    private int finalAttackIndex =2;
    private int JA = 0;
    private static int STAR_RADIUS = 12;
    private static int PLAYER_RADIUS = 27;
    private static int ENEMY_RADIUS = 20;




    public stackBoss() {
        initializeStage();
        createKeyListeners();
        randomPositionGenerator = new Random();
    }

    //flip the character model
    //key listeners.
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

                else if (keyEvent.getCode()== KeyCode.SHIFT){
                    isShiftPressed = true;
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
        playerAttack = new ArrayList<ImageView>();
        gameStage.show();

    }


    private void createGameElements(DIFF chosenDiff){
        playerLives= 2;
        star = new ImageView(STAR_IMAGE);
        stackBossImageView = new ImageView(STACKBOSS_IMAGE);
        patty = new ImageView(PATTY_ATTACK);
        tomato = new ImageView(TOMATO_ATTACK);
        cheese = new ImageView(CHEESE_ATTACK);
        stackBossAttacks = new ImageView[3];
//        platforms[1] = new ImageView(PLATFORM_IMAGE);
        setPosition(stackBossImageView,1450,350);
        gamePane.getChildren().add(stackBossImageView);
        points = new InGameLabel("POINTS: 0");
        points.setLayoutX(460);
        points.setLayoutY(20);
        gamePane.getChildren().add(points);
        lives = new ImageView[3];
        platforms = new ImageView[6];
        blue = new ImageView[7];

        stackBoss.push(patty);
        stackBoss.push(cheese);
        stackBoss.push(tomato);

        for (int i =0; i< blue.length;i++){
//            blue[i] = new ImageView(BLUE_IMAGE);
//            gamePane.getChildren().add(blue[i]);
        }
        for (int i =0; i< stackBossAttacks.length;i++){
            stackBossAttacks[i] = (ImageView) stackBoss.pop();
            stackBossAttacks[i].setLayoutX(1600);
            stackBossAttacks[i].setLayoutY(600+(i*50));
            gamePane.getChildren().add(stackBossAttacks[i]);
        }

        //setting 3 platforms at equally distant x values same height
        for (int i = 0; i < platforms.length-3; i++){
            platforms[i] = new ImageView(PLATFORM_IMAGE);
            platforms[i].setLayoutX(290 + (i*300));
            platforms[i].setLayoutY(900);
            gamePane.getChildren().add(platforms[i]);

        }
        for (int i = 3; i < platforms.length; i++){
            platforms[i] = new ImageView(PLATFORM_IMAGE);
            platforms[i].setLayoutX(350+((i-3)*300));
            platforms[i].setLayoutY(750);
            gamePane.getChildren().add(platforms[i]);

        }
        //manually placing third platform
//        platforms[3] = new ImageView(PLATFORM_IMAGE);
//        platforms[3].setLayoutX(200 + (0*300));
//        platforms[3].setLayoutY(750);
//        gamePane.getChildren().add(platforms[3]);
        for (int i = 0; i < lives.length; i++){
            lives[i] = new ImageView(chosenDiff.getLife());
            lives[i].setLayoutX(445 + (i*50));
            lives[i].setLayoutY(80);
            gamePane.getChildren().add(lives[i]);
        }

    }


    private void playerAttack()
    {
        for(int i = 0; i < playerAttack.size(); i++)
        {
            ImageView atk = (ImageView) playerAttack.get(i);
            atk.setX(atk.getX() + (atk.getScaleX()) * 4);
            Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(2), ev ->{
                playerAttack.remove(atk);
                gamePane.getChildren().remove(atk);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        }


    }



    private void setPosition(ImageView image,int X, int Y){
        image.setLayoutX(X);
        image.setLayoutY(Y);
    }
    private void createCharacter(DIFF chosenDiff) {

        if(chosenDiff.getCharacter() == "PlayerGreen.png"){
            difficulty = 3;
        }
        else if(chosenDiff.getCharacter() == "PlayerBlue.png"){
            difficulty = 2;
        }
        else{
            difficulty = 1;
        }
        Player = new ImageView(chosenDiff.getCharacter());
        Player.setLayoutX(GAME_WIDTH/2);
        Player.setLayoutY(GAME_HEIGHT - 90);
        gamePane.getChildren().add(Player);
    }

    // each method in this loop is called every frame
    private void createGameLoop() {
        gameTimer = new AnimationTimer() {

            @Override
            public void handle(long now) {


                collision();
                movePlayer();
                fall();
                playerAttack();
            }
        };
        gameTimer.start();

    }

    private void movePlayer() {
//set global variable for difficulty then run if statement here maybe
        if (isLeftKeyPressed && !isRightKeyPressed) {
            //rotate model when turning
            //checking which model it is
            if (difficulty == 3){
            Player.setImage(playerGreen);
            }
            else if (difficulty == 2){
                Player.setImage(playerBlue);
            }
            else{
                Player.setImage(playerRed);
            }
            if (Player.getLayoutX() >0) {
                Player.setLayoutX(Player.getLayoutX() - 9);
            }
        }

        if (isRightKeyPressed && !isLeftKeyPressed) {
            if (difficulty == 3){
                Player.setImage(playerGreenReverse);
            }
            else if (difficulty == 2){
                Player.setImage(playerBlueReverse);
            }
            else{
                Player.setImage(playerRedReverse);
            }

            if (Player.getLayoutX() < GAME_WIDTH) {
                Player.setLayoutX(Player.getLayoutX() + 9);
            }
        }

        if(isShiftPressed)
        {
//            ImageView PlayerAttack = new ImageView(BLUE_IMAGE);
//            PlayerAttack.setLayoutX(Player.getLayoutX());
//            PlayerAttack.setLayoutY(Player.getLayoutY() - Player.getImage().getHeight() / 2);
//            double atkSpeed = Player.getScaleX();
//            PlayerAttack.setScaleX(atkSpeed * 1);
//            if(playerAttack.size() < 7)
//            {
//                gamePane.getChildren().add(PlayerAttack);
//                playerAttack.add(PlayerAttack);
//            }
//            isShiftPressed = false;

        }

    }

    private void createBackground(){
        gridPane1 = new GridPane();
        gridPane2 = new GridPane();

        for (int i = 0; i < 12; i++){
            ImageView backgroundImage1 = new ImageView(BACKGROUND_IMAGE);
            ImageView backgroundImage2 = new ImageView(BACKGROUND_IMAGE);
            GridPane.setConstraints(backgroundImage1, i% 3, i/3);
            GridPane.setConstraints(backgroundImage2, i% 3, i/3);
            gridPane1.getChildren().add(backgroundImage1);
            gridPane2.getChildren().add(backgroundImage2);

        }

        gridPane2.setLayoutY(-1024);

        //gamePane.getChildren().addAll(gridPane1,gridPane2);
        gamePane.getChildren().add(gridPane1);
    }
private void fall(){

  if(isSpaceKeyPressed){
    //300
      //player is -90 so just minus jump height to it
     if (Player.getLayoutY() > (GAME_HEIGHT - (Player.getLayoutY())-jumpHeight)) {
    fallSpeed += .2;
    Player.setLayoutY(Player.getLayoutY() - 9 + fallSpeed);
    if (Player.getLayoutY() >= groundHeight ){
       //falling to ground
        Player.setLayoutY(groundHeight);
        //reach ground
        isSpaceKeyPressed = false;
        fallSpeed = 0;
    }

      }}
  else if(falling){
          fallSpeed += .5;
          Player.setLayoutY(Player.getLayoutY() - 2 + fallSpeed);

      if(Player.getLayoutY() >= groundHeight ){
          Player.setLayoutY(groundHeight);
          falling = false;
          fallSpeed = 0;
      }

  }

}
    private void BossMethod(){



        for(int i =0;i<stackBossAttacks.length;i++){

            //System.out.println(JA);
               if (!setEnemyY) {
                   setEnemyY = true;
                   stackBossAttacks[JA].setLayoutY(Player.getLayoutY());

                   //setEnemyX = true;
               }
               if (stackBossAttacks[JA].getLayoutX() > -150) {
                   //collision

                   if(Player.getBoundsInParent().intersects(stackBossAttacks[JA].getBoundsInParent())&&notHit){
                       notHit = false;
                       lostLife = true;
                   }

                   if(lostLife){
                       lostLife = false;
                       removeLife();
                   }


                   stackBossAttacks[JA].setRotate(stackBossAttacks[JA].getRotate()+9);
                   stackBossAttacks[JA].setLayoutX(stackBossAttacks[JA].getLayoutX() - bossProjSpeed);
               } else {
                   notHit = true;

                   //finished traveling
                   if(stackBossAttacks[finalAttackIndex].getLayoutX()<=-50){
                      //restart attack sequence
                       //add in if statement later if boss health --0
                   resetBoss();
                   }
                   if(!bossAttackedOnce) {
                       JA++;
                       setEnemyY = false;
                   }
                   else{
                       setEnemyY = false;
                       bossAttackedOnce = false;
                   }
               }

        }
    }

    private void resetBoss(){
        bossAttackedOnce = true;
        JA = 0;
        //tomato
        stackBossAttacks[0].setLayoutX(1600);
        stackBossAttacks[0].setLayoutY(600);
        stackBossAttacks[0].setRotate(0);
        //cheese
        stackBossAttacks[1].setLayoutX(1600);
        stackBossAttacks[1].setLayoutY(650);
        stackBossAttacks[1].setRotate(0);
        //patty
        stackBossAttacks[2].setLayoutX(1600);
        stackBossAttacks[2].setLayoutY(700);
        stackBossAttacks[2].setRotate(0);
        bossProjSpeed+=1.5;
        BossMethod();
    }
    private void collision() {
        if(Player.getLayoutX()>bossStart){
            canStartBoss = true;
        }
        if (canStartBoss){
            BossMethod();
        }
        if (PLAYER_RADIUS + STAR_RADIUS > calculateDistance(Player.getLayoutX() + 49, star.getLayoutX() + 15, Player.getLayoutY() + 37, star.getLayoutY() + 15)) {

            playerPoints++;
            String pointsText = "POINTS: ";
            if (playerPoints < 10) {
                pointsText += "0";
            }
            points.setText(pointsText + playerPoints);
        }
        //have to minus x by player width
        //if(Player.getBoundsInParent().intersects(platforms[0].getBoundsInParent())){
       if((Player.getLayoutY() <= platforms[0].getLayoutY()-70&& Player.getLayoutX()>=platforms[0].getLayoutX()-30&&Player.getLayoutX()<=platforms[0].getLayoutX()+187&& Player.getLayoutY()>=platforms[0].getLayoutY()-80)){
            //on platforms
           //platform 1
           onPlat = true;
            groundHeight =platforms[0].getLayoutY()-74;
            if (isDownKeyPressed && !isSpaceKeyPressed) {
                Player.setLayoutY(Player.getLayoutY()+70);
            }

        }
        else if((Player.getLayoutY() <= platforms[1].getLayoutY()-70&& Player.getLayoutX()>=platforms[1].getLayoutX()-30&&Player.getLayoutX()<=platforms[1].getLayoutX()+187&& Player.getLayoutY()>=platforms[1].getLayoutY()-80)){
            //platform 2
            onPlat = true;
            groundHeight =platforms[1].getLayoutY()-74;
            if (isDownKeyPressed && !isSpaceKeyPressed) {
                Player.setLayoutY(Player.getLayoutY()+70);
            }

        }
        else if((Player.getLayoutY() <= platforms[2].getLayoutY()-70&& Player.getLayoutX()>=platforms[2].getLayoutX()-30&&Player.getLayoutX()<=platforms[2].getLayoutX()+187&& Player.getLayoutY()>=platforms[2].getLayoutY()-80)){
            //platform 3
            onPlat = true;
            groundHeight =platforms[1].getLayoutY()-74;
            if (isDownKeyPressed && !isSpaceKeyPressed) {
                Player.setLayoutY(Player.getLayoutY()+70);
            }
        }
            else if((Player.getLayoutY() <= platforms[3].getLayoutY()-70&& Player.getLayoutX()>=platforms[3].getLayoutX()-30&&Player.getLayoutX()<=platforms[3].getLayoutX()+187&& Player.getLayoutY()>=platforms[3].getLayoutY()-80)){
                //platform 4
                onPlat = true;
                groundHeight =platforms[3].getLayoutY()-74;
                if (isDownKeyPressed && !isSpaceKeyPressed) {
                    Player.setLayoutY(Player.getLayoutY()+70);

                }

            }
       else if((Player.getLayoutY() <= platforms[4].getLayoutY()-70&& Player.getLayoutX()>=platforms[4].getLayoutX()-30&&Player.getLayoutX()<=platforms[4].getLayoutX()+187&& Player.getLayoutY()>=platforms[4].getLayoutY()-80)){
           //platform 5
           onPlat = true;
           groundHeight =platforms[4].getLayoutY()-74;
           if (isDownKeyPressed && !isSpaceKeyPressed) {
               Player.setLayoutY(Player.getLayoutY()+70);
           }

       }
       else if((Player.getLayoutY() <= platforms[5].getLayoutY()-70&& Player.getLayoutX()>=platforms[5].getLayoutX()-30&&Player.getLayoutX()<=platforms[5].getLayoutX()+187&& Player.getLayoutY()>=platforms[5].getLayoutY()-80)){
           //platform 6
           onPlat = true;
           groundHeight =platforms[5].getLayoutY()-74;
           if (isDownKeyPressed && !isSpaceKeyPressed) {
               Player.setLayoutY(Player.getLayoutY()+70);
           }

       }

            else{
            onPlat = false;

            //off platform
        
            if(Player.getLayoutY()<GAME_HEIGHT - 90) {
                groundHeight = GAME_HEIGHT - 90;
                falling = true;
            }
        }
        for(int j = 0; j < playerAttack.size(); j++)
        {
            ImageView temp2 = (ImageView) playerAttack.get(j);
            if(temp2.getBoundsInParent().intersects(stackBossImageView.getBoundsInParent()))
            {

                //connects attack with boss

            }

        }

    }

    private void removeLife(){
        gamePane.getChildren().remove(lives[playerLives]);
        playerLives--;
        if (playerLives <0){
            gameStage.close();
            gameTimer.stop();
            menuStage.show();
        }
    }

    private double calculateDistance(double x1, double x2, double y1, double y2){
        return Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));

    }
}

