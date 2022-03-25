package com.game.game1;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Random;

public class mergeSortBoss {
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
    private final static String BACKGROUND_IMAGE = "newWizard.png";
    private final static String BACKGROUND_IMAGE2 = "newWizardFlexed.png";

    private static String ENEMY_IMAGE = "eclipse-logo.png";

    private ImageView[] enemy;
    Random randomPositionGenerator;

    private ImageView star;
    private InGameLabel points;
    private ImageView[] lives;
    private int PlayerLives;
    private int PlayerPoints;
    private final static String STAR_IMAGE = "element_red_polygon.png";


    private static int STAR_RADIUS = 12;
    private static int Player_RADIUS = 27;
    private static int ENEMY_RADIUS = 20;

    private ImageView sortBossSmall;
    private static final String SMALL_BOSS_IMAGE = "pig.png";
    private static int SMALL_BOSS_IMAGE_X = 1800;
    private static int SMALL_BOSS_IMAGE_Y = GAME_HEIGHT - 60;
    private ImageView sortBossBig;
    private static final String BIG_BOSS_IMAGE = "pig.png--big.png";
    private String[] BOSS_ATTACK = {"fireball.png", "waterBall.png", "purpleBall.png", "blueBall.png","greenBall.png", "dragonBall.png"};
    private int secondsPassed = 0;
    private int bossHealth = 1000;
    private int[] numbers = new int[6];
    private ImageView[] attacks = new ImageView[6];
    private boolean position1 = false;
    private boolean arrayCreation = false;
    private boolean position2 = false;
    private boolean attackMerge = false;
    private boolean bossTarget = false;
    private int attackIteration = 0;
    private double fadingTracker = 0;
    private double playerPastLayoutX;
    private double playerPastLayoutY;
    private boolean approximationdone = false;
    private boolean quadraticCalled = false;
    private boolean leastSquaresCalled = false;


    private static String PLATFORM_IMAGE_SMALL = "platformCloudSmall.png";
    private static String PLATFORM_IMAGE_MEDIUM = "platformCloudMedium.png";
    private static String PLATFORM_IMAGE_LARGE = "platformCloudLong.png";
    private double groundHeight = GAME_HEIGHT - 90;
    private double jumpHeight = 1080;
    private ImageView[] platforms;
    double fallSpeed = 0;

    private boolean onPlat;
    private boolean falling;

    private boolean isrunning = false;





    static HashMap<String, Double> quadratic = new HashMap<String, Double>();
    static HashMap<String, Double> approximation = new HashMap<String, Double>();


    private boolean isCutScenePressed = false;
    private final static String PAUSE_IMG = "pause_resize.png";


    public mergeSortBoss() {
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
        PlayerGetPositionXPast();
        PlayerGetPositionYPast();


        gameStage.show();

    }


    private void createGameElements(DIFF chosenDiff){
        PlayerLives= 2;
        sortBossSmall = new ImageView(SMALL_BOSS_IMAGE);
        sortBossSmall.setLayoutX(SMALL_BOSS_IMAGE_X);
        sortBossSmall.setLayoutY(SMALL_BOSS_IMAGE_Y);
        points = new InGameLabel("POINTS: 0");
        points.setLayoutX(1700);
        points.setLayoutY(20);
        gamePane.getChildren().add(points);
        gamePane.getChildren().add(sortBossSmall);
        lives = new ImageView[3];

        for (int i = 0; i < lives.length; i++){
            lives[i] = new ImageView(chosenDiff.getLife());
            lives[i].setLayoutX(1700 + (i*50));
            lives[i].setLayoutY(80);
            gamePane.getChildren().add(lives[i]);
        }
        platforms = new ImageView[8];

        //setting 3 platforms at equally distant x values same height

        //manually placing third platform
        for (int i = 0; i < platforms.length; i++) {
            platforms[i] = new ImageView(PLATFORM_IMAGE_MEDIUM);
            platforms[i].setLayoutX(125 + ((i) * 300));
            platforms[i].setLayoutY(850);
            gamePane.getChildren().add(platforms[i]);
        }

    }

    private void createSortBoss(){
        Random rand = new Random();
        if (arrayCreation == false)
            for (int i = 0; i < numbers.length; i++){
                numbers[i] = rand.nextInt(6);
                System.out.println(numbers[i]);
                attacks[i] = new ImageView(BOSS_ATTACK[numbers[i]]);
                attacks[i].setLayoutX(425 + (i*200));
                attacks[i].setLayoutY(80);
                gamePane.getChildren().add(attacks[i]);

                arrayCreation = true;
            }

    }


    private void bossAttackSort() {
        if (position1 == false && arrayCreation) {
            if (attacks[0].getLayoutY() <= 300 && attacks[1].getLayoutY() <= 300) {
                attacks[0].setLayoutX(attacks[0].getLayoutX() - 10);
                attacks[0].setLayoutY(attacks[0].getLayoutY() + 10);

                attacks[1].setLayoutX(attacks[1].getLayoutX() - 10);
                attacks[1].setLayoutY(attacks[1].getLayoutY() + 10);
            }
            if (attacks[2].getLayoutY() <= 300 && attacks[3].getLayoutY() <= 300) {
                attacks[2].setLayoutY(attacks[2].getLayoutY() + 10);

                attacks[3].setLayoutY(attacks[3].getLayoutY() + 10);
            }
            if (attacks[4].getLayoutY() <= 300 && attacks[5].getLayoutY() <= 300) {
                attacks[4].setLayoutX(attacks[4].getLayoutX() + 10);
                attacks[4].setLayoutY(attacks[4].getLayoutY() + 10);

                attacks[5].setLayoutX(attacks[5].getLayoutX() + 10);
                attacks[5].setLayoutY(attacks[5].getLayoutY() + 10);
            } else {
                position1 = true;
            }
        }
        if (position1 && arrayCreation && position2 == false){
            if (attacks[0].getLayoutY() <= 500 && attacks[1].getLayoutY() <= 500) {
                attacks[0].setLayoutX(attacks[0].getLayoutX() - 2.2);
                attacks[0].setLayoutY(attacks[0].getLayoutY() + 10);

                attacks[1].setLayoutX(attacks[1].getLayoutX() + 2.2);
                attacks[1].setLayoutY(attacks[1].getLayoutY() + 10);
            }
            if (attacks[2].getLayoutY() <= 500 && attacks[3].getLayoutY() <= 500) {
                attacks[2].setLayoutX(attacks[2].getLayoutX() - 2.2);
                attacks[2].setLayoutY(attacks[2].getLayoutY() + 10);

                attacks[3].setLayoutX(attacks[3].getLayoutX() + 2.2);
                attacks[3].setLayoutY(attacks[3].getLayoutY() + 10);
            }
            if (attacks[4].getLayoutY() <= 500 && attacks[5].getLayoutY() <= 500) {
                attacks[4].setLayoutX(attacks[4].getLayoutX() - 2.2);
                attacks[4].setLayoutY(attacks[4].getLayoutY() + 10);

                attacks[5].setLayoutX(attacks[5].getLayoutX() + 2.2);
                attacks[5].setLayoutY(attacks[5].getLayoutY() + 10);
            }else {
                position2 = true;
            }
        }
    }

    private void timerForAttackCreation(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            createSortBoss();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    private void bossAttackMerge() {
        if (position2 && arrayCreation && attackMerge == false) {
            gridPane1.setLayoutY(gridPane1.getLayoutY() + 1080);
            gridPane2.setLayoutY(gridPane2.getLayoutY() + 1080);

            if(gridPane1.getLayoutY() >= 1080){
                gridPane1.setLayoutY(-1080);
            }
            if(gridPane2.getLayoutY() >= 1080){
                gridPane2.setLayoutY(-1080);
            }

            for (int i = 0; i < numbers.length; i++) {
                gamePane.getChildren().remove(attacks[i]);
            }
            MergeSort.mergeSort(numbers);
            for (int i = 0; i < numbers.length; i++) {
                System.out.println(numbers[i]);
                attacks[i] = new ImageView(BOSS_ATTACK[numbers[i]]);
                attacks[i].setLayoutX(425 + (i * 200));
                attacks[i].setLayoutY(80);
                gamePane.getChildren().add(attacks[i]);
            }
            attackMerge = true;


        }
        else if (bossTarget == false && attackMerge == true) {
            bossAttackTarget(attackIteration);
        }
    }

    private void bossAttackTarget(int i){
        if (attackMerge && arrayCreation && bossTarget == false){
            //added two zeros
            //0.004
            fadingTracker+=.004;
            if ((approximation.isEmpty()) && approximationdone == false){
                attacks[i].setLayoutY(attacks[i].getLayoutY() + (Player.getLayoutY() - attacks[i].getLayoutY()) * fadingTracker);
                attacks[i].setLayoutX(attacks[i].getLayoutX() + (Player.getLayoutX() - attacks[i].getLayoutX()) * fadingTracker);
                playerApproximationValues();
                System.out.println(calculateDistance(attacks[i].getLayoutX(), Player.getLayoutX(), attacks[i].getLayoutY(), Player.getLayoutY()) + "SKRSEN");
                System.out.println((approximation.get("a")) + "arstar");
                if (quadratic.size() == 8 && leastSquaresCalled == false){
                    LeastSquaresApproximation();
                    leastSquaresCalled = true;
                }
            }

            else {
                approximationdone = true;
            }
            if (approximation.size() == 3 && quadraticCalled == false) {
                quadratic(i);
            }

            if (i <= 4 && approximationdone && quadraticCalled) {
                System.out.println("Add");
                fadingTracker = 0;
                approximation.clear();
                quadratic.clear();
                approximationdone = false;
                quadraticCalled = false;
                leastSquaresCalled = false;
                attackIteration++;
            }
        }
    }

    private void quadratic(int i) {

        double newX = Player.getLayoutX() *2;
        Path path = new Path();
        MoveTo mt = new MoveTo(0, 0);
        QuadCurveTo qct = new QuadCurveTo(Player.getLayoutX(), Player.getLayoutY(), newX, (approximation.get("a") * Math.pow(newX, 2) + approximation.get("b") * newX + approximation.get("c")));
        PathTransition pt = new PathTransition();
        path.getElements().add(mt);
        path.getElements().add(qct);
        pt.setDuration(Duration.seconds(.75));
        pt.setNode(attacks[i]);
        pt.setPath(path);

        pt.play();

        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(.76), ev ->{
            if (pt.getStatus().equals(Animation.Status.STOPPED)) {
                gamePane.getChildren().remove(attacks[i]);
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();
        if(isrunning == false){
            Timeline timeline2 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(4.61), ev ->{
                if (pt.getStatus().equals(Animation.Status.STOPPED)) {
                    arrayCreation = false;
                    position2 = false;
                    position1 = false;
                    attackMerge = false;
                    attackIteration = -1;
                }
            }));
            timeline2.setCycleCount(1);
            if (timeline2.getStatus().equals(Animation.Status.STOPPED)) {
                timeline2.play();
                isrunning = true;
            }
            else if(attackIteration <= 0)
                isrunning = false;
        }





        quadraticCalled = true;
        System.out.println(attacks[i].getLayoutY() + "attackY");
        System.out.println(attacks[i].getLayoutX() + "attackX");
        System.out.println(newX);
        System.out.println(newX);
    }

    private void collisionTest()
    {
        if (attacks != null)
        {

            for(int i = 0; i < attacks.length; i++)
            {
                ImageView temp = (ImageView) attacks[i];
                if(temp != null){
                    if(Player.getBoundsInParent().intersects(temp.getBoundsInParent()))
                    {
                        System.out.println("True");
                        removeLife();
                        temp.setLayoutX(5000);
                        temp.setLayoutY(5000);

                    }
                }

            }
        }
    }



    private void playerApproximationValues(){
        if (fadingTracker <= .090){
            quadratic.put("X1", Player.getLayoutX());
            quadratic.put("Y1", Player.getLayoutY());
        }
        if (fadingTracker > .090 && fadingTracker <= .120){
            quadratic.put("X2", Player.getLayoutX());
            quadratic.put("Y2", Player.getLayoutY());
        }
        if (fadingTracker > .150 && fadingTracker <= .180){
            quadratic.put("X3", Player.getLayoutX());
            quadratic.put("Y3", Player.getLayoutY());
        }
        if (fadingTracker > .210 && fadingTracker <= .240) {
            quadratic.put("X4", Player.getLayoutX());
            quadratic.put("Y4", Player.getLayoutY());
        }
    }


    private void PlayerGetPositionXPast(){

        playerPastLayoutX = Player.getLayoutX() + Player.getImage().getWidth()/2;


    }
    private void PlayerGetPositionYPast(){

        playerPastLayoutY = Player.getLayoutY()+ Player.getImage().getHeight()/2;

    }

    private static void LeastSquaresApproximation(){
        double m [][] = {
                {quadratic.get("X1")*quadratic.get("X1"), quadratic.get("X1"), 1},
                {quadratic.get("X2")*quadratic.get("X2"), quadratic.get("X2"), 1},
                {quadratic.get("X3")*quadratic.get("X3"), quadratic.get("X3"), 1},
                {quadratic.get("X4")*quadratic.get("X4"), quadratic.get("X4"), 1}
        };

        double mT[][] = new double[3][4];
        double y[] = {quadratic.get("Y1"),quadratic.get("Y2"),quadratic.get("Y3"),quadratic.get("Y4")};

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 4; j++){
                mT[i][j] = m[j][i];
            }
        }


        double sum = 0;
        double mTm[][] = new double[3][3];
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                for (int k = 0; k < 4; k++){
                    sum = sum + mT[i][k]*m[k][j];
                }
                mTm[i][j] = sum;
                sum = 0;
            }
        }

        double sum2 = 0;
        double mTy[] = new double[3];
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 4; j++){
                sum2 = sum + mT[i][j]*y[j];
                mTy[i] += sum2;

            }
        }


        double augmentedMatrix [][] = {
                {mTm[0][0], mTm[0][1], mTm[0][2],mTy[0]},
                {mTm[1][0], mTm[1][1], mTm[1][2],mTy[1]},
                {mTm[2][0], mTm[2][1], mTm[2][2],mTy[2]}
        };

        rref(augmentedMatrix);

    }
    public static void rref(double[][] mat)
    {
        double[][] rref = new double[mat.length][mat[0].length];

        /* Copy matrix */
        for (int r = 0; r < rref.length; ++r)
        {
            for (int c = 0; c < rref[r].length; ++c)
            {
                rref[r][c] = mat[r][c];
            }
        }

        for (int p = 0; p < rref.length; ++p)
        {
            /* Make this pivot 1 */
            double pv = rref[p][p];
            if (pv != 0)
            {
                double pvInv = 1.0 / pv;
                for (int i = 0; i < rref[p].length; ++i)
                {
                    rref[p][i] *= pvInv;
                }
            }

            /* Make other rows zero */
            for (int r = 0; r < rref.length; ++r)
            {
                if (r != p)
                {
                    double f = rref[r][p];
                    for (int i = 0; i < rref[r].length; ++i)
                    {
                        rref[r][i] -= f * rref[p][i];
                    }
                }
            }
        }
        approximation.put("a", rref[0][3]);
        approximation.put("b", rref[1][3]);
        approximation.put("c", rref[2][3]);
        System.out.println(approximation.put("a", rref[0][3]) + "a");
        System.out.println(approximation.put("b", rref[0][3]) + "b");
        System.out.println(approximation.put("c", rref[0][3]) + "c");

    }


    private void moveElements(){
        star.setLayoutY(star.getLayoutY()+5);


        for(int i = 0; i < enemy.length; i++){
            enemy[i].setLayoutY(enemy[i].getLayoutY()+7);
            enemy[i].setRotate(enemy[i].getRotate()+4);
        }
    }

    //this method can be used to reuse to boss's ability. fly off the screen, but relocate to be boss when the boss is ready to fire more.
    private void checkIfElementsAreBehindAndRelocate(){
        if(star.getLayoutY() > 1200){
            setEnemyPosition(star);
        }
        for (int i = 0; i < enemy.length; i++){
            if(enemy[i].getLayoutY() > 900){
                setEnemyPosition(enemy[i]);
            }
        }
    }

    private void setEnemyPosition(ImageView image){
        image.setLayoutX(randomPositionGenerator.nextInt(370));
        image.setLayoutY(-randomPositionGenerator.nextInt(3200)+600);
    }

    private void createCharacter(DIFF chosenDiff) {
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
                collisionTest();
                fall();
                timerForAttackCreation();
                bossAttackSort();
                bossAttackMerge();
                movePlayer();
            }
        };
        gameTimer.start();

    }

    private void movePlayer() {

        if (isLeftKeyPressed && !isRightKeyPressed) {
            if (Player.getLayoutX() > -20) {
                Player.setLayoutX(Player.getLayoutX() - 20);
            }
        }

        if (isRightKeyPressed && !isLeftKeyPressed) {
            if (Player.getLayoutX() < 2000) {
                Player.setLayoutX(Player.getLayoutX() + 20);
            }
        }
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
        else if((Player.getLayoutY() <= platforms[2].getLayoutY()-70&& Player.getLayoutX()>=platforms[2].getLayoutX()-30&&Player.getLayoutX()<=platforms[2].getLayoutX()+187)){
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
            //platform 4
            onPlat = true;
            groundHeight =platforms[4].getLayoutY()-74;
            if (isDownKeyPressed && !isSpaceKeyPressed) {
                Player.setLayoutY(Player.getLayoutY()+70);

            }

        }
        else if((Player.getLayoutY() <= platforms[5].getLayoutY()-70&& Player.getLayoutX()>=platforms[5].getLayoutX()-30&&Player.getLayoutX()<=platforms[5].getLayoutX()+187&& Player.getLayoutY()>=platforms[5].getLayoutY()-80)){
            //platform 4
            onPlat = true;
            groundHeight =platforms[5].getLayoutY()-74;
            if (isDownKeyPressed && !isSpaceKeyPressed) {
                Player.setLayoutY(Player.getLayoutY()+70);

            }

        }
        else if((Player.getLayoutY() <= platforms[6].getLayoutY()-70&& Player.getLayoutX()>=platforms[6].getLayoutX()-30&&Player.getLayoutX()<=platforms[6].getLayoutX()+187&& Player.getLayoutY()>=platforms[6].getLayoutY()-80)){
            //platform 4
            onPlat = true;
            groundHeight =platforms[6].getLayoutY()-74;
            if (isDownKeyPressed && !isSpaceKeyPressed) {
                Player.setLayoutY(Player.getLayoutY()+70);

            }

        }
        else if((Player.getLayoutY() <= platforms[7].getLayoutY()-70&& Player.getLayoutX()>=platforms[7].getLayoutX()-30&&Player.getLayoutX()<=platforms[7].getLayoutX()+187&& Player.getLayoutY()>=platforms[7].getLayoutY()-80)){
            //platform 4
            onPlat = true;
            groundHeight =platforms[7].getLayoutY()-74;
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

    }
    private void fall(){

        if(isSpaceKeyPressed){
            //300
            //Player is -90 so just minus jump height to it
            if (Player.getLayoutY() > (GAME_HEIGHT - (Player.getLayoutY())-jumpHeight)) {
                fallSpeed += .3;
                Player.setLayoutY(Player.getLayoutY() - 15 + fallSpeed);
                if (Player.getLayoutY() >= groundHeight ){
                    //falling to ground
                    Player.setLayoutY(groundHeight);
                    //reach ground
                    isSpaceKeyPressed = false;
                    fallSpeed = 0;
                }
                if (isDownKeyPressed){
                    fallSpeed +=1 ;
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
    private void createBackground(){
        gridPane1 = new GridPane();
        gridPane2 = new GridPane();

        for (int i = 0; i < 12; i++){
            ImageView backgroundImage1 = new ImageView(BACKGROUND_IMAGE);
            ImageView backgroundImage2 = new ImageView(BACKGROUND_IMAGE2);
            GridPane.setConstraints(backgroundImage1, i% 3, i/3);
            GridPane.setConstraints(backgroundImage2, i% 3, i/3);
            gridPane1.getChildren().add(backgroundImage1);
            gridPane2.getChildren().add(backgroundImage2);

        }

        gridPane2.setLayoutY(-1080);
        gridPane2.setLayoutY(1080);
        gamePane.getChildren().addAll(gridPane1,gridPane2);
    }

    private void collision() {
        if (Player_RADIUS + STAR_RADIUS > calculateDistance(Player.getLayoutX() + 49, star.getLayoutX() + 15, Player.getLayoutY() + 37, star.getLayoutY() + 15)) {
            setEnemyPosition(star);

            PlayerPoints++;
            String pointsText = "POINTS: ";
            if (PlayerPoints < 10) {
                pointsText += "0";
            }
            points.setText(pointsText + PlayerPoints);
        }
        for (int i = 0; i < enemy.length; i++){
            if(ENEMY_RADIUS + Player_RADIUS > calculateDistance(Player.getLayoutX() + 49, enemy[i].getLayoutX() + 20, Player.getLayoutY() + 37, enemy[i].getLayoutY() + 20)){
                removeLife();
                setEnemyPosition(enemy[i]);
            }
        }
    }

    private void removeLife(){
        gamePane.getChildren().remove(lives[PlayerLives]);
        PlayerLives--;
        if (PlayerLives <0){
            gameStage.close();
            gameTimer.stop();
            menuStage.show();
        }
    }


    private double calculateDistance(double x1, double x2, double y1, double y2){
        return Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));

    }
}

