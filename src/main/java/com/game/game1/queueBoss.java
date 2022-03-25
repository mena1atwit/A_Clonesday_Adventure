package com.game.game1;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class queueBoss
{
    private Scene gameScene;
    private Stage gameStage;

    private GridPane gridPane1;

    private static final int GAME_WIDTH = 1920;
    private static final int GAME_HEIGHT = 1080;

    private Stage menuStage;
    private ImageView Player;

    ColorAdjust ca = new ColorAdjust();
    ColorAdjust ca2 = new ColorAdjust();

    private AnimationTimer gameTimer;

    private final static String ROBOT_BOSS = "robot_final.png";
    private final static String QUEUE_BAR = "queueBar.png";
    private final static String BACKGROUND_IMAGE = "background_final.png";
    private final static String MISSILE = "missile_placeholder_sv2.png";
    private final static String BALLS = "metal_ball.png";
    private final static String MINI_BOT = "miniBot_v1.png";
    private final static String ROBOT_EYE = "robo_eye.png";
    private final static String PLATFORM_L = "platform_big.png";
    private final static String PLATFORM_M = "platform_med.png";
    private final static String PLATFORM_S = "platform_sml.png";
    private final static String BOOM = "boom.gif";
    private final static String SOUND_BOOM = "boom.mp3";

    private final static ImageView backgroundImage1 = new ImageView(BACKGROUND_IMAGE);

    private final static ImageView robot_eye = new ImageView(ROBOT_EYE);
    ImageView queueBar;

    private final static String ATK_RED = "atk_r_nb.png";
    private final static String ATK_GRN = "atk_g_nb.png";
    private final static String ATK_BLU = "atk_b_nb.png";
    private final static String ATK_YLW = "atk_y_nb.png";

    private final static Image R = new Image(ATK_RED);
    private final static Image G = new Image(ATK_GRN);
    private final static Image B = new Image(ATK_BLU);
    private final static Image Y = new Image(ATK_YLW);

    private boolean done = false;

    private Queue<ImageView> queue;
    private Queue<ImageView> ATKqueue;
    private Queue<ImageView> barQueue;

    private long startTime;
    private AnchorPane gamePane;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isSpaceKeyPressed;
    private boolean isDownKeyPressed;

    private static int STAR_RADIUS = 12;
    private static int PLAYER_RADIUS = 27;
    private final static int ENEMY_RADIUS = 20;

    private ImageView star;
    private InGameLabel points;
    private ImageView[] lives;
    private int playerLives = 2;
    private int playerPoints = 0;
    private final static String STAR_IMAGE = "element_red_polygon.png";

    double fallSpeed = 0;

    private List projectiles;
    private List playerAttack;

    private double currentPlayerX = 0;
    private double currentPlayerY = 0;

    private int cycleCount = 0;
    private boolean bossTarget = false;
    private double fadingTracker = 1.0;
    private boolean isTracking = false;
    private boolean PlayerTracking = false;

    private int barTracker = 0;
    private boolean isUpKeyPressed;

    public static Duration calcDeltaTime()
    {
        Duration deltaTime = Duration.ZERO;
        Instant beginTime = Instant.now();
        return deltaTime = Duration.between(beginTime, Instant.now());
    }

    private void spawnFX(ImageView boss)
    {
        AtomicInteger i = new AtomicInteger();
        AtomicReference<Double> angle = new AtomicReference<>(5.0);
        boss.setScaleX(.5);
        boss.setScaleY(.5);
        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), ev ->{
            TranslateTransition tt = new TranslateTransition(javafx.util.Duration.millis(1000), boss);
            RotateTransition rt = new RotateTransition(javafx.util.Duration.millis(1000), boss);
            ScaleTransition st = new ScaleTransition(javafx.util.Duration.millis(1500), boss);

            st.setToX(.85);
            st.setToY(.85);
            if(i.get() < 2)
            {
                rt.setToAngle(angle.get());
                angle.updateAndGet(v -> new Double((double) (v * -1)));
            }
            else
                rt.setToAngle(0);
            i.getAndIncrement();
            tt.setByY(-100);
            tt.setCycleCount(1);
            tt.play();
            rt.play();
            st.play();
        }));
        timeline.setCycleCount(3);
        timeline.play();

    }

    private void bossTimer(){
        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(10), ev ->{
            bossWait();
            barTracker = 0;
            refreshQueueBar();
            //cycleCount++;
            System.out.println(cycleCount);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }
    private void bossWait(){
        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), ev ->{
            addATK(queue);
            displayATKFX(queue, ATKqueue);
            bossATK();
        }));
        timeline.setCycleCount(4);
        timeline.play();
    }

    private void bossATK(){
        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(4), ev ->{
            executeATK(ATKqueue);
        }));
        timeline.setCycleCount(1);
        timeline.play();

    }
    private void initializeStage()
    {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
        startTime = System.currentTimeMillis();
    }

    private void initializeQueue()
    {
        this.queue = new Queue<ImageView>();
        this.ATKqueue = new Queue<ImageView>();
        this.barQueue = new Queue<ImageView>();
    }

    private void initializeEye()
    {

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

    private void spawnBoss ()
    {
        ImageView boss = new ImageView(ROBOT_BOSS);
        boss.setX(GAME_WIDTH/2 - boss.getImage().getWidth()/2);
        boss.setY(400);
        boss.toFront();
        boss.setScaleX(.85);
        boss.setScaleY(.85);
        ca.setBrightness(-.25);
        boss.setEffect(ca);
        gamePane.getChildren().add(boss);
        spawnFX(boss);
    }

    private ImageView genATK()
    {
        ImageView atk;
        int rand = (int) (Math.random() * 4);

        if(rand == 0)
        {
            atk = new ImageView(ATK_RED);
            atk.setY(0);
            return atk;
        }
        else if(rand == 1)
        {
            atk = new ImageView(ATK_GRN);
            atk.setY(-1);
            return atk;
        }
        else if(rand == 2)
        {
            atk = new ImageView(ATK_BLU);
            atk.setY(-2);
            return atk;
        }
        else
        {
            atk = new ImageView(ATK_YLW);
            atk.setY(-3);
            return atk;
        }

    }

    private void addATK(Queue queue)
    {
        ImageView atk = new ImageView();
        for(int i = 0; i < 4;i++)
        {
            atk = genATK();
            queue.add(atk);
        }
        this.queue = queue;



    }

    private void displayATKFX(Queue queue, Queue ATKqueue)
    {
        if(!queue.isEmpty())
        {
            ImageView atk = (ImageView) queue.remove();
            ATKqueue.add(atk);
            atk.setOpacity(.5);
            atk.setFitWidth(2000);
            atk.setFitHeight(1200);
            ca2.setBrightness(-.75);
            atk.setEffect(ca2);
            gamePane.getChildren().add(atk);
            FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(1000), atk);
            ft.setFromValue(.60);
            ft.setToValue(0);
            ft.play();

            ImageView toBar = new ImageView(atk.getImage());
            toBar.setFitWidth(28);
            toBar.setFitHeight(20);
            toBar.setX(GAME_WIDTH - 246 + (barTracker * (toBar.getImage().getWidth() - 4)));
            barTracker++;
            toBar.setY(154);
            barQueue.add(toBar);
            gamePane.getChildren().add(toBar);

        }
    }

    private void refreshQueueBar()
    {
        while(!barQueue.isEmpty())
        {
            ImageView temp = (ImageView) barQueue.remove();
            gamePane.getChildren().remove(temp);
        }
    }

    private void executeATK(Queue ATKqueue)
    {
        ImageView atk = (ImageView) ATKqueue.remove();
        if(atk.getY() == 0)
        {
            ImageView[] arr = new ImageView[4];
            for(int i = 0; i < arr.length; i++)
            {
                arr[i] = new ImageView(MISSILE);
                arr[i].setY(-50);
                arr[i].setX((int) (Math.random() * 1920));
                arr[i].setRotate(90);
                projectiles.add(arr[i]);
                gamePane.getChildren().add(arr[i]);
            }

            for(int i = 0; i < arr.length; i++)
            {
                int finalI = i;
                Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(4), ev ->{
                    TranslateTransition tt = new TranslateTransition(javafx.util.Duration.millis(3000), arr[finalI]);
                    tt.setToY(1250);
                    tt.setCycleCount(1);
                    tt.play();
                }));
                timeline.playFrom(javafx.util.Duration.millis(3999));
                timeline.setCycleCount(1);
                timeline.play();

                Timeline timeline2 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(4.05), ev ->{
                    FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(1000), arr[finalI]);
                    ft.setFromValue(1);
                    ft.setToValue(0);
                    ft.play();

                    projectiles.remove(arr[finalI]);
                    gamePane.getChildren().remove(arr[finalI]);
                }));
                timeline2.setCycleCount(1);
                timeline2.play();
            }
        }
        else if(atk.getY() == -1) {
            ImageView[] arr = new ImageView[5];
            int randSeed = (int) (Math.random() * 2);
            for (int i = 0; i < arr.length; i++) {
                arr[i] = new ImageView(MINI_BOT);
                //arr[i].setY((GAME_HEIGHT / 2) - arr[i].getImage().getHeight());
                //arr[i].setX((GAME_WIDTH / 2) - arr[i].getImage().getWidth());
                arr[i].setY(Math.random() * GAME_HEIGHT / 2);
                arr[i].setX(Math.random() * GAME_WIDTH);
                arr[i].setScaleX(0);
                arr[i].setScaleY(0);
                projectiles.add(arr[i]);
                gamePane.getChildren().add(arr[i]);
            }


            for (int i = 0; i < arr.length; i++) {
                int finalI = i;
                Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), ev -> {
                    TranslateTransition tt = new TranslateTransition(javafx.util.Duration.millis(1000), arr[finalI]);
                    ScaleTransition st = new ScaleTransition(javafx.util.Duration.millis(2000), arr[finalI]);
                    st.setFromX(0);
                    st.setFromY(0);
                    st.setToX(.1);
                    st.setToY(.1);
                    if (arr[finalI].getX() < 960) {
                        tt.setToX(arr[finalI].getLayoutX() - 100);
                    } else
                        tt.setToX(arr[finalI].getLayoutX() + 100);
                    tt.setToY(arr[finalI].getLayoutY() - 100);
                    st.play();
                    tt.play();
                }));

                timeline.setCycleCount(1);
                timeline.playFrom(javafx.util.Duration.millis(999));
                timeline.play();
                Timeline timeline2 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), ev -> {
                    TranslateTransition tt = new TranslateTransition(javafx.util.Duration.millis(1000), arr[finalI]);
                    if (arr[finalI].getX() < 960) {
                        tt.setToX(arr[finalI].getTranslateX() + 200);
                    } else
                        tt.setToX(arr[finalI].getLayoutX() - 200);
                    tt.setToY(arr[finalI].getTranslateY() + 200);
                    tt.play();
                }));
                timeline2.setCycleCount(1);
                timeline2.play();

                Timeline timeline3 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), ev -> {
                    Path path = new Path();
                    MoveTo mt = new MoveTo(arr[finalI].getX() + 100, arr[finalI].getY() + 200);
                    QuadCurveTo qct = new QuadCurveTo(Player.getLayoutX(), Player.getLayoutY(), GAME_WIDTH / 2, -1000);
                    PathTransition pt = new PathTransition();

                    path.getElements().add(mt);
                    path.getElements().add(qct);

                    pt.setDuration(javafx.util.Duration.millis(1000));
                    pt.setNode(arr[finalI]);
                    pt.setPath(path);
                    //pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                    pt.play();
                }));
                timeline3.setCycleCount(1);
                timeline3.setDelay(javafx.util.Duration.seconds(2));
                timeline3.play();


                Timeline timeline4 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(4), ev -> {
                    TranslateTransition tt = new TranslateTransition(javafx.util.Duration.millis(1500), arr[finalI]);
                    tt.setToX(currentPlayerX - arr[finalI].getX() + (Math.random() * 120) - 250);
                    tt.setToY(currentPlayerY + 60 - arr[finalI].getY());
                    tt.play();
                }));
                timeline4.setCycleCount(1);
                timeline4.play();
            }
        }
        else if(atk.getY() == -2)
        {
            ImageView[] arr = new ImageView[2];
            int randSeed = (int) (Math.random() * 2);
            for(int i = 0; i < arr.length; i++)
            {
                arr[i] = new ImageView(BALLS);
                if(i > (i / 2))
                    arr[i].setX(-200 - (int) (Math.random() * 100));
                else
                {
                    arr[i].setX(2000 + (int) (Math.random() * 30));
                }

                arr[i].setY(1020);
                projectiles.add(arr[i]);
                gamePane.getChildren().add(arr[i]);
            }

            for(int i = 0; i < arr.length; i++)
            {
                int finalI = i;
                Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(4), ev ->{
                    TranslateTransition tt = new TranslateTransition(javafx.util.Duration.millis(3000), arr[finalI]);
                    if(randSeed < 1) {

                        if ((int) (Math.random() * 1) < 1) {
                            arr[finalI].setY(arr[finalI].getY() + (int) (Math.random() * 10));
                            tt.setToX(3000 - (int) (Math.random() * 100));
                        } else {
                            arr[finalI].setY(arr[finalI].getY() - (int) (Math.random() * 10));
                            tt.setToX(3000 + (int) (Math.random() * 100));
                        }
                    }
                    else
                    {

                        if((int) (Math.random() * 1) < 1)
                        {
                            arr[finalI].setY(arr[finalI].getY() +  (int) (Math.random() * 10));
                            tt.setToX(-3000 + (int) (Math.random() * 100));
                        }

                        else
                        {
                            arr[finalI].setY(arr[finalI].getY() - (int) (Math.random() * 10));
                            tt.setToX(-3000 - (int) (Math.random() * 100));
                        }

                    }

                    tt.setCycleCount(1);
                    tt.play();
                }));
                timeline.playFrom(javafx.util.Duration.millis(3999));
                timeline.setCycleCount(1);
                timeline.play();

                Timeline timeline2 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(5), ev ->{
                    FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(1000), arr[finalI]);
                    ft.setFromValue(1);
                    ft.setToValue(0);
                    ft.play();
                    projectiles.remove(arr[finalI]);
                    gamePane.getChildren().remove(arr[finalI]);
                }));
                timeline2.setCycleCount(1);
                timeline2.play();

            }
        }
        else
        {
            ImageView[] arr = new ImageView[5];

            for(int i = 0; i < arr.length; i++)
            {
                arr[i] = new ImageView(BALLS);
                arr[i].setY((GAME_HEIGHT / 2 + arr[i].getImage().getHeight() / 2) - 15 + (int) (Math.random() * 40));
                arr[i].setX((GAME_WIDTH / 2 + arr[i].getImage().getHeight() / 2) - 35 + (int) (Math.random() * 40));
                projectiles.add(arr[i]);
                gamePane.getChildren().add(arr[i]);
            }

            for(int i = 0; i < arr.length; i++)
            {
                int finalI = i;
                double playerInitX = Player.getLayoutX();
                double playerInitY = Player.getLayoutY();
                Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(2), ev ->{

                    TranslateTransition tt = new TranslateTransition(javafx.util.Duration.millis(1000), arr[finalI]);
                    if((int) (Math.random() * 2) < 1)
                        tt.setToX(playerInitX - arr[finalI].getX() + (int) (Math.random() * 50));
                    else
                        tt.setToX(playerInitX - arr[finalI].getX() - (int) (Math.random() * 50));

                    tt.setToY(playerInitY - arr[finalI].getY() + 200);
                    tt.setCycleCount(1);
                    tt.play();
                }));
                timeline.playFrom(javafx.util.Duration.millis(3000 + (int) (Math.random() * 999)));
                timeline.setCycleCount(1);
                timeline.play();

                Timeline timeline2 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(5), ev ->{
                    FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(1000), arr[finalI]);
                    ft.setFromValue(1);
                    ft.setToValue(0);
                    ft.play();

                    projectiles.remove(arr[finalI]);
                    gamePane.getChildren().remove(arr[finalI]);
                }));
                timeline2.setCycleCount(1);
                timeline2.play();
            }


        }
    }

    private void bossAttackTarget(long prevTime, ImageView atk, double prevPosX, double prevPosY, int i)
    {
        {

            fadingTracker -= 0.0012;
            if(i < 10)
            {
                int attackNum = i + 1;
                bossAttackTarget(System.currentTimeMillis(), atk, Player.getLayoutX(), Player.getLayoutY(), attackNum);
            }
        }
    }


    private void displayTimer()
    {

    }



    private void createBackground()
    {
        gridPane1 = new GridPane();
        gridPane1.setLayoutX(2);

        gridPane1.getChildren().add(backgroundImage1);

        gamePane.getChildren().add(gridPane1);
    }

    private void createCharacter(DIFF chosenDiff) {
        Player = new ImageView(chosenDiff.getCharacter());
        Player.setLayoutX(GAME_WIDTH/2);
        Player.setLayoutY(GAME_HEIGHT - 97);
        gamePane.getChildren().add(Player);
        Player.toFront();
    }

    private void movePlayer() {

        if (isLeftKeyPressed && !isRightKeyPressed) {
            if (Player.getLayoutX() > 0) {
                Player.setLayoutX(Player.getLayoutX() - 6);
                Player.setScaleX(1);
            }
        }

        if (isRightKeyPressed && !isLeftKeyPressed) {
            if (Player.getLayoutX() < GAME_WIDTH - 40) {
                Player.setLayoutX(Player.getLayoutX() + 6);
                Player.setScaleX(-1);
            }
        }
        if (isUpKeyPressed) {
            if (Player.getLayoutY() > GAME_HEIGHT - 280) {
                fallSpeed += .2;
                //.println(fallSpeed);
                Player.setLayoutY(Player.getLayoutY() - 5 + fallSpeed);
                if (Player.getLayoutY() == GAME_HEIGHT - 97){
                    isUpKeyPressed = false;
                    fallSpeed = 0;
                }

            }else{
                isUpKeyPressed = false;
            }
        }
        if(isSpaceKeyPressed)
        {
            ImageView PlayerAttack = new ImageView(BALLS);
            PlayerAttack.setLayoutX(Player.getLayoutX());
            PlayerAttack.setLayoutY(Player.getLayoutY() - Player.getImage().getHeight() / 2);
            double atkSpeed = Player.getScaleX();
            PlayerAttack.setScaleX(atkSpeed * -1);
            if(playerAttack.size() < 7)
            {
                gamePane.getChildren().add(PlayerAttack);
                playerAttack.add(PlayerAttack);
            }
            isSpaceKeyPressed = false;

        }
    }


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
                else if(keyEvent.getCode() == KeyCode.UP)
                {
                    isUpKeyPressed = true;
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


    private double calculateDistance(double x1, double x2, double y1, double y2){
        return Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));

    }

    public void createGameLoop() {
        gameTimer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                long endTime = System.currentTimeMillis();
                if(endTime - startTime > 1000 && !done)
                {
                    //collision();
                    movePlayer();
                    playerAttack();
                    collisionTest();

                    currentPlayerX = Player.getLayoutX();
                    currentPlayerY = Player.getLayoutY();
                    if(cycleCount > 5)
                    {
                        gameStage.hide();
                        menuStage.show();
                    }
                    points = new InGameLabel("POINTS: " + playerPoints);
                }

            }
        };
        gameTimer.start();


    }
    private void collisionTest()
    {
        for(int i = 0; i < projectiles.size(); i++)
        {
            ImageView temp = (ImageView) projectiles.get(i);
            if(Player.getBoundsInParent().intersects(temp.getBoundsInParent()))
            {
                gamePane.getChildren().remove(temp);
                projectiles.remove(temp);

                System.out.println("True");
                removeLife();
            }
            for(int j = 0; j < playerAttack.size(); j++)
            {
                ImageView temp2 = (ImageView) playerAttack.get(j);
                if(temp2.getBoundsInParent().intersects(temp.getBoundsInParent()))
                {
                    playerPoints++;
                    gamePane.getChildren().remove(temp);
                    projectiles.remove(temp);
                }

            }
        }


    }

    private void createGameElements(DIFF chosenDiff) {
        lives = new ImageView[3];
        points = new InGameLabel("POINTS: " + playerPoints);
        points.setTextFill(Color.WHITE);
        points.setLayoutX(GAME_WIDTH - 250);
        points.setLayoutY(20);
        gamePane.getChildren().add(points);

        for (int i = 0; i < lives.length; i++) {
            lives[i] = new ImageView(chosenDiff.getLife());
            lives[i].setLayoutX((GAME_WIDTH - 250)+ (i * 50));
            lives[i].setLayoutY(80);
            gamePane.getChildren().add(lives[i]);
        }

        queueBar = new ImageView(QUEUE_BAR);
        queueBar.setLayoutX(GAME_WIDTH - 250);
        queueBar.setLayoutY(150);
        queueBar.toFront();
        gamePane.getChildren().add(queueBar);
    }

    private void removeLife(){
        try{
        gamePane.getChildren().remove(lives[playerLives]);
        playerLives--;
        if (playerLives < 0){
            gameStage.close();
            gameTimer.stop();
            menuStage.show();
        }}catch(ArrayIndexOutOfBoundsException e){

        }
    }

    public void createNewGame(Stage menuStage, DIFF chosenDiff) {

        this.menuStage = menuStage;
        this.menuStage.hide();
        initializeStage();
        initializeQueue();
        createGameLoop();
        createKeyListeners();
        bossWait();
        bossTimer();
        createBackground();

        spawnBoss();
        createCharacter(chosenDiff);
        projectiles = new ArrayList<ImageView>();
        playerAttack = new ArrayList<ImageView>();
        createGameElements(chosenDiff);
        gameStage.show();
    }
}
