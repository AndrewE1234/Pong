// Andrew Eljumaily
// 2017/12/28
// Pong
//
// This is a recreation of the classic
// video game "Pong" done primarily with
// JavaFX. The main scenes of the
// program are a menu screen, a one player
// game, a two player game, and a help screen.
// The game also includes sounds created using
// Bfxr and Audacity. All input within the game
// is handled with the keyboard, but the menu is
// navigated by the mouse. The game's animation
// is handled by a timeline, which is given one
// of two game loops (See GameController) which,
// along with onKeyPressed/Released, are the
// primary controllers of the game. The program is
// not flawless, as there are some known bugs.
// The most notable, though not incredibly common,
// is a glitch in which the ball gets stuck between
// the paddle and the wall. This requires the entire
// program to be reset, as it may cause the sounds to
// stop playing. I thought I would point it out here
// in case you encounter it, so you can at least
// know how to "fix" it.
//
// Primary Resources and tools used for help and or assets:
//
// https://stackoverflow.com/
// https://docs.oracle.com/javase/8/javafx/api/
// https://www.bfxr.net/
// https://www.audacityteam.org/

package andrew.fx.pong;

import andrew.fx.pong.Paddles.ComputerPaddle;
import andrew.fx.pong.Paddles.HumanPaddle;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Main extends Application
{
    private final String missSoundFile = "miss.wav";
    private AudioClip missClip = new AudioClip(new File(missSoundFile).toURI().toString());

    private final String hitSoundFile = "hit.wav";
    private AudioClip hitClip = new AudioClip(new File(hitSoundFile).toURI().toString());

    private final int windowX = 800;
    private final int windowY = 600;
    private final int menuWindowX = 450;
    private final int menuWindowY = 450;
    private final String howToFile = "HowToPlayPong.txt";
    private boolean isPaused = false;
    private boolean stopPauseTransition = false;
    private final int scoreToPlayTo = 10;

    private GameController gameController = new GameController(800, 600, scoreToPlayTo);

    private final Score leftScore = new Score(windowX - 150, 20, 5, 5, 0);
    private final Score rightScore = new Score(150, 20, 5, 5, 0);
    private final Label inGameText = new Label("");
    private final Label subText = new Label("");

    private PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));

    @Override
    public void start(Stage stage) throws Exception
    {
        //Open file containing text for How To Play
        ArrayList<String> howToText = new ArrayList<>();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(howToFile));
            String line = "";
            while((line = reader.readLine()) != null)
            {
                howToText.add(line);
            }
            reader.close();
        }
        catch (Exception e)
        {
            System.err.format("Could not open file: %s", howToFile);
        }


        final Label pongTitle = new Label("PONG");
        pongTitle.setLayoutX(menuWindowX/2 - 15);
        pongTitle.setLayoutY(100);
        pongTitle.setTextFill(Color.WHITE);
        pongTitle.setScaleX(6);
        pongTitle.setScaleY(6);

        inGameText.setScaleX(5);
        inGameText.setScaleY(5);
        inGameText.setTextFill(Color.WHITE);

        subText.setScaleX(2);
        subText.setScaleY(2);
        subText.setTextFill(Color.WHITE);


        //BUTTONS
        //##############################################################################################################

        Button onePlayerGameButton = new Button("One Player Game");
        onePlayerGameButton.setPrefSize(200, 20);
        onePlayerGameButton.setStyle("-fx-font: 18 arial;");

        onePlayerGameButton.setOnAction(event ->
        {
            stopPauseTransition = false;

            final ComputerPaddle leftPaddle = new ComputerPaddle(50, windowY/2, 25, 125,
                    3, 0, windowY);
            leftPaddle.setFill(Color.WHITE);

            final HumanPaddle rightPaddle = new HumanPaddle(windowX - 75, (windowY/2), 25, 125,
                    3.5, 0, windowY);
            rightPaddle.setFill(Color.WHITE);

            final Ball ball = new Ball(400, 300, 15, 5.5, 1);
            ball.setFill(Color.WHITE);

            leftScore.setTextFill(Color.WHITE);

            rightScore.setTextFill(Color.WHITE);

            inGameText.setLayoutX(windowX/2);
            inGameText.setLayoutY((windowY/2) - 100);

            subText.setLayoutX(windowX/2 - 35);
            subText.setLayoutY(inGameText.getLayoutY()+150);

            Stage gameStage = new Stage();
            Group root = new Group(leftPaddle, rightPaddle, ball, leftScore, rightScore, inGameText, subText);
            Scene scene = new Scene(root);
            scene.setFill(Color.BLACK);
            Canvas canvas = new Canvas(windowX, windowY);

            Timeline timeLine = new Timeline();

            pauseTransition.setOnFinished( eventPause ->
                    {
                        if(!stopPauseTransition)
                        {
                            timeLine.play();
                        }
                    }
            );

            timeLine.getKeyFrames().add((new KeyFrame(Duration.millis(10), e -> gameController.runGameOnePlayer(
                    leftPaddle, rightPaddle, ball, timeLine, inGameText, pauseTransition, hitClip, missClip, rightScore,
                    leftScore, subText))));
            timeLine.setCycleCount(Timeline.INDEFINITE);

            root.getChildren().add(canvas);
            gameStage.setScene(scene);
            gameStage.setTitle("Pong");
            gameStage.initModality(Modality.WINDOW_MODAL);
            gameStage.initOwner(stage);

            gameStage.setOnCloseRequest(t ->
            {
                stopPauseTransition = true;
                resetGame(ball, leftPaddle, rightPaddle);
                timeLine.stop();
            });

            scene.setOnKeyPressed(
                    eventKeyPressed ->
                    {
                        switch (eventKeyPressed.getCode())
                        {
                            case KP_UP:
                            case UP:
                                rightPaddle.setMovePaddleUp(true);
                                break;

                            case KP_DOWN:
                            case DOWN:
                                rightPaddle.setMovePaddleDown(true);
                                break;

                            case SPACE:
                                if(gameController.isGameOver() || isPaused)
                                {
                                    resetGame(ball, leftPaddle, rightPaddle);
                                    timeLine.play();
                                }
                                break;

                            case ESCAPE:
                            case P:
                                if(!isPaused && !gameController.isGameOver())
                                {
                                    timeLine.pause();
                                    inGameText.setText("Paused");
                                    subText.setText("Press SPACE to restart\nor\nPress Q to quit");
                                    isPaused = true;
                                    stopPauseTransition = true;
                                }
                                else if(isPaused && !gameController.isGameOver())
                                {
                                    timeLine.play();
                                    inGameText.setText("");
                                    subText.setText("");
                                    isPaused = false;
                                    stopPauseTransition = false;
                                }
                                break;

                            case Q:
                                if(isPaused || gameController.isGameOver())
                                {
                                    stopPauseTransition = true;
                                    resetGame(ball, leftPaddle, rightPaddle);
                                    gameStage.close();
                                }
                                break;
                        }
                    }
            );

            scene.setOnKeyReleased(
                    eventKeyRelease ->
                    {
                        switch (eventKeyRelease.getCode())
                        {
                            case KP_UP:
                            case UP:
                                rightPaddle.setMovePaddleUp(false);
                                break;

                            case KP_DOWN:
                            case DOWN:
                                rightPaddle.setMovePaddleDown(false);
                                break;
                        }
                    }
            );

            gameStage.show();
            timeLine.play();
        });

        //##############################################################################################################

        Button twoPlayerGameButton = new Button("Two Player Game");
        twoPlayerGameButton.setPrefSize(200, 20);
        twoPlayerGameButton.setStyle("-fx-font: 18 arial;");

        twoPlayerGameButton.setOnAction(event ->
        {
            stopPauseTransition = false;

            final HumanPaddle leftPaddle = new HumanPaddle(50, windowY/2, 25, 125,
                    3.5, 0, windowY);
            leftPaddle.setFill(Color.WHITE);


            final HumanPaddle rightPaddle = new HumanPaddle(windowX - 75, (windowY/2), 25, 125,
                    3.5, 0, windowY);
            rightPaddle.setFill(Color.WHITE);


            final Ball ball = new Ball(400, 300, 15, 5.5, 1);
            ball.setFill(Color.WHITE);

            leftScore.setTextFill(Color.WHITE);

            rightScore.setTextFill(Color.WHITE);

            inGameText.setLayoutX(windowX/2);
            inGameText.setLayoutY((windowY/2) - 100);

            subText.setLayoutX(windowX/2 - 35);
            subText.setLayoutY(inGameText.getLayoutY()+150);

            Stage gameStage = new Stage();
            Group root = new Group(leftPaddle, rightPaddle, ball, leftScore, rightScore, inGameText, subText);
            Scene scene = new Scene(root);
            scene.setFill(Color.BLACK);
            Canvas canvas = new Canvas(windowX, windowY);

            Timeline timeLine = new Timeline();

            pauseTransition.setOnFinished( eventPause ->
                    {
                        if(!stopPauseTransition)
                        {
                            timeLine.play();
                        }
                    }
            );

            timeLine.getKeyFrames().add((new KeyFrame(Duration.millis(10), e -> gameController.runGameTwoPlayer(
                    leftPaddle, rightPaddle, ball, timeLine, inGameText, pauseTransition, hitClip, missClip, rightScore,
                    leftScore, subText))));
            timeLine.setCycleCount(Timeline.INDEFINITE);

            root.getChildren().add(canvas);
            gameStage.setScene(scene);
            gameStage.setTitle("Pong");
            gameStage.initModality(Modality.WINDOW_MODAL);
            gameStage.initOwner(stage);

            gameStage.setOnCloseRequest(t ->
            {
                stopPauseTransition = true;
                resetGame(ball, leftPaddle, rightPaddle);
                timeLine.stop();
            });

            scene.setOnKeyPressed(
                    eventKeyPressed ->
                    {
                        switch (eventKeyPressed.getCode())
                        {
                            case KP_UP:
                            case UP:
                                rightPaddle.setMovePaddleUp(true);
                                break;

                            case KP_DOWN:
                            case DOWN:
                                rightPaddle.setMovePaddleDown(true);
                                break;

                            case W:
                                leftPaddle.setMovePaddleUp(true);
                                break;

                            case S:
                                leftPaddle.setMovePaddleDown(true);
                                break;

                            case SPACE:
                                if(gameController.isGameOver() || isPaused)
                                {
                                    resetGame(ball, leftPaddle, rightPaddle);
                                    timeLine.play();
                                }
                                break;

                            case ESCAPE:
                            case P:
                                if(!isPaused && !gameController.isGameOver())
                                {
                                    timeLine.pause();
                                    inGameText.setText("Paused");
                                    subText.setText("Press SPACE to restart\nor\nPress Q to quit");
                                    isPaused = true;
                                    stopPauseTransition = true;
                                }
                                else if(isPaused && !gameController.isGameOver())
                                {
                                    timeLine.play();
                                    inGameText.setText("");
                                    subText.setText("");
                                    isPaused = false;
                                    stopPauseTransition = false;
                                }
                                break;

                            case Q:
                                if(isPaused || gameController.isGameOver())
                                {
                                    stopPauseTransition = true;
                                    resetGame(ball, leftPaddle, rightPaddle);
                                    gameStage.close();
                                }
                                break;
                        }
                    }
            );

            scene.setOnKeyReleased(
                    eventKeyRelease ->
                    {
                        switch (eventKeyRelease.getCode())
                        {
                            case KP_UP:
                            case UP:
                                rightPaddle.setMovePaddleUp(false);
                                break;

                            case KP_DOWN:
                            case DOWN:
                                rightPaddle.setMovePaddleDown(false);
                                break;

                            case W:
                                leftPaddle.setMovePaddleUp(false);
                                break;

                            case S:
                                leftPaddle.setMovePaddleDown(false);
                                break;
                        }
                    }
            );

            gameStage.show();
            timeLine.play();
        });

        //##############################################################################################################

        Button howToPlayButton = new Button("How To Play");
        howToPlayButton.setPrefSize(200, 20);
        howToPlayButton.setStyle("-fx-font: 18 arial;");

        howToPlayButton.setOnAction(event ->
        {
            Stage howToStage = new Stage();
            ScrollPane root = new ScrollPane();
            String getText = "";

            for(String textInList : howToText)
            {
                getText += (textInList + "\n");
            }

            Text content = new Text(getText);
            root.setContent(content);

            Scene scene = new Scene(root, menuWindowX, menuWindowY);
            howToStage.setScene(scene);
            howToStage.setTitle("How To Play");
            howToStage.initModality(Modality.WINDOW_MODAL);
            howToStage.initOwner(stage);
            howToStage.show();
        });

        //##############################################################################################################

        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(200, 20);
        exitButton.setStyle("-fx-font: 18 arial;");

        exitButton.setOnAction(event ->
        {
            Platform.exit();
        });

        //##############################################################################################################
        //END OF BUTTONS


        VBox verticalBox = new VBox(onePlayerGameButton, twoPlayerGameButton, howToPlayButton, exitButton);
        verticalBox.setSpacing(20);
        verticalBox.setLayoutX((menuWindowX/2) - 100);
        verticalBox.setLayoutY((menuWindowY/2) - (verticalBox.getHeight()/2));

        Group menuRoot = new Group(pongTitle, verticalBox);
        Scene menuScene = new Scene(menuRoot, menuWindowX, menuWindowY);
        menuScene.setFill(Color.BLACK);
        stage.setScene(menuScene);
        stage.setTitle("Pong Menu");
        stage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }


    private void resetGame(Ball ball, Rectangle leftPaddle, Rectangle rightPaddle)
    {
        ball.setCenterX((windowX/2) - 100);
        ball.setCenterY(windowY/2);

        leftPaddle.setY(ball.getCenterY() + (leftPaddle.getHeight()/2));
        rightPaddle.setY(ball.getCenterY() + (rightPaddle.getHeight()/2));

        ball.setMoveSpeedY(1);

        rightScore.resetScore();
        leftScore.resetScore();

        gameController.setHasHitLeftPaddle(false);
        gameController.setHasHitRightPaddle(false);
        gameController.setGameOver(false);

        isPaused = false;
        stopPauseTransition = false;

        inGameText.setText("");
        subText.setText("");
    }
}