// Andrew Eljumaily
// 2017/12/28
// GameController
//
// The GameController class' main porpose
// is to store the two main game loops,
// one for a single player game with a
// computer player and one for a two
// player game, as well as some key
// variables. The game loops (along
// with the timeline) handle the
// actual movement of the paddles
// and the ball on the screen, as
// well as keeping score.

package andrew.fx.pong;

import andrew.fx.pong.Paddles.ComputerPaddle;
import andrew.fx.pong.Paddles.HumanPaddle;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;

public class GameController
{
    private boolean hasHitRightPaddle = false;
    private boolean hasHitLeftPaddle = false;
    private int windowX = 800;
    private int windowY = 600;
    private int scoreToPlayTo = 0;
    private boolean gameOver = false;

    public GameController(int windowX, int windowY, int scoreToPlayTo)
    {
        this.windowX = windowX;
        this.windowY = windowY;
        this.scoreToPlayTo = scoreToPlayTo;
    }

    //GAME LOOPS
    //##################################################################################################################

    public void runGameOnePlayer(ComputerPaddle leftPaddle, HumanPaddle rightPaddle, Ball ball, Timeline timeline,
                                 Label inGameText, PauseTransition pauseTransition, AudioClip hitClip,
                                 AudioClip missClip, Score rightScore, Score leftScore, Label subText)
    {
        if(rightScore.getCurrentScore() >= scoreToPlayTo || leftScore.getCurrentScore() >= scoreToPlayTo)
        {
            gameOver = true;
            timeline.pause();

            if(leftScore.getCurrentScore() > rightScore.getCurrentScore())
            {
                inGameText.setText("You Win!");
            }
            else if(leftScore.getCurrentScore() < rightScore.getCurrentScore())
            {
                inGameText.setText("You Lose!");
            }

            subText.setText("Press SPACE to restart\nor\nPress Q to quit");
        }

        //Move Right Paddle
        if(rightPaddle.isMovePaddleUp())
        {
            rightPaddle.movePaddle(rightPaddle.getHeight()/2);
        }
        else if(rightPaddle.isMovePaddleDown())
        {
            rightPaddle.movePaddle(windowY - (rightPaddle.getHeight()/2));
        }


        //Move Left Paddle
        leftPaddle.movePaddle(ball.getCenterY());


        //Check if ball hit either paddle.
        if(ball.getCenterX() >= rightPaddle.getX() && ball.getCenterX() <= (rightPaddle.getX() + rightPaddle.getWidth()))
        {
            if((ball.getCenterY() >= rightPaddle.getY() && ball.getCenterY() <= rightPaddle.getY() +
                    rightPaddle.getHeight()) && !hasHitRightPaddle)
            {
                hitClip.play();

                rightPaddle.handlePaddleHit(ball);

                hasHitRightPaddle = true;
                hasHitLeftPaddle = false;
            }
        }
        else if(ball.getCenterX() <= (leftPaddle.getX() + leftPaddle.getWidth()) && ball.getCenterX() >= leftPaddle.getX())
        {
            if((ball.getCenterY() >= leftPaddle.getY() && ball.getCenterY() <= leftPaddle.getY() +
                    leftPaddle.getHeight()) && !hasHitLeftPaddle)
            {
                hitClip.play();

                leftPaddle.handlePaddleHit(ball);

                hasHitLeftPaddle = true;
                hasHitRightPaddle = false;
            }
        }

        //Ball hit top or bottom wall.
        if(ball.getCenterY() <= ball.getRadius() || ball.getCenterY() >= (windowY - ball.getRadius()))
        {
            hitClip.play();

            ball.setMoveSpeedY(ball.getMoveSpeedY() * -1);

            hasHitLeftPaddle = false;
            hasHitRightPaddle = false;
        }

        //Ball left screen
        if(ball.getCenterX() >= windowX || ball.getCenterX() <= 0)
        {
            missClip.play();

            if(!gameOver)
            {
                timeline.pause();
                pauseTransition.play();
            }

            if(ball.getCenterX() >= windowX)
            {
                rightScore.increaseScore();
                ball.setCenterX((windowX/2) - 100);
                ball.setCenterY(windowY/2);
            }
            else if(ball.getCenterX() <= 0)
            {
                leftScore.increaseScore();
                ball.setCenterX((windowX/2) + 100);
                ball.setCenterY(windowY/2);
            }

            ball.setMoveSpeedY(1);

            hasHitLeftPaddle = false;
            hasHitRightPaddle = false;
        }

        ball.setCenterX(ball.getCenterX() + ball.getMoveSpeedX());
        ball.setCenterY(ball.getCenterY() + ball.getMoveSpeedY());
    }


    //runGameTwoPlayer
    //##################################################################################################################

    public void runGameTwoPlayer(HumanPaddle leftPaddle, HumanPaddle rightPaddle, Ball ball, Timeline timeline,
                                 Label inGameText, PauseTransition pauseTransition, AudioClip hitClip,
                                 AudioClip missClip, Score rightScore, Score leftScore, Label subText)
    {
        if(rightScore.getCurrentScore() >= scoreToPlayTo || leftScore.getCurrentScore() >= scoreToPlayTo)
        {
            gameOver = true;
            timeline.pause();

            if(leftScore.getCurrentScore() > rightScore.getCurrentScore())
            {
                inGameText.setText("Right Player Wins!");
            }
            else if(leftScore.getCurrentScore() < rightScore.getCurrentScore())
            {
                inGameText.setText("Left Player Wins!");
            }

            subText.setText("Press SPACE to restart\nor\nPress Q to quit");
        }

        //Move Right Paddle
        if(rightPaddle.isMovePaddleUp())
        {
            rightPaddle.movePaddle(rightPaddle.getHeight()/2);
        }
        else if(rightPaddle.isMovePaddleDown())
        {
            rightPaddle.movePaddle(windowY - (rightPaddle.getHeight()/2));
        }

        //Move Left Paddle
        if(leftPaddle.isMovePaddleUp())
        {
            leftPaddle.movePaddle(leftPaddle.getHeight()/2);
        }
        else if(leftPaddle.isMovePaddleDown())
        {
            leftPaddle.movePaddle(windowY - (leftPaddle.getHeight()/2));
        }

        //Check if ball hit either paddle.
        if(ball.getCenterX() >= rightPaddle.getX() && ball.getCenterX() <= (rightPaddle.getX() + rightPaddle.getWidth()))
        {
            if((ball.getCenterY() >= rightPaddle.getY() && ball.getCenterY() <= rightPaddle.getY() +
                    rightPaddle.getHeight()) && !hasHitRightPaddle)
            {
                hitClip.play();

                rightPaddle.handlePaddleHit(ball);

                hasHitRightPaddle = true;
                hasHitLeftPaddle = false;
            }
        }
        else if(ball.getCenterX() <= (leftPaddle.getX() + leftPaddle.getWidth()) && ball.getCenterX() >= leftPaddle.getX())
        {
            if((ball.getCenterY() >= leftPaddle.getY() && ball.getCenterY() <= leftPaddle.getY() +
                    leftPaddle.getHeight()) && !hasHitLeftPaddle)
            {
                hitClip.play();

                leftPaddle.handlePaddleHit(ball);

                hasHitLeftPaddle = true;
                hasHitRightPaddle = false;
            }
        }

        //Ball hit top or bottom wall.
        if(ball.getCenterY() <= ball.getRadius() || ball.getCenterY() >= (windowY - ball.getRadius()))
        {
            hitClip.play();

            ball.setMoveSpeedY(ball.getMoveSpeedY() * -1);

            hasHitLeftPaddle = false;
            hasHitRightPaddle = false;
        }

        //Ball left screen
        if(ball.getCenterX() >= windowX || ball.getCenterX() <= 0)
        {
            missClip.play();

            if(!gameOver)
            {
                timeline.pause();
                pauseTransition.play();
            }

            if(ball.getCenterX() >= windowX)
            {
                rightScore.increaseScore();
                ball.setCenterX((windowX/2) - 100);
                ball.setCenterY(windowY/2);
            }
            else if(ball.getCenterX() <= 0)
            {
                leftScore.increaseScore();
                ball.setCenterX((windowX/2) + 100);
                ball.setCenterY(windowY/2);
            }

            ball.setMoveSpeedY(1);

            hasHitLeftPaddle = false;
            hasHitRightPaddle = false;
        }

        ball.setCenterX(ball.getCenterX() + ball.getMoveSpeedX());
        ball.setCenterY(ball.getCenterY() + ball.getMoveSpeedY());
    }

    //##################################################################################################################

    public boolean isHasHitRightPaddle()
    {
        return hasHitRightPaddle;
    }

    public void setHasHitRightPaddle(boolean hasHitRightPaddle)
    {
        this.hasHitRightPaddle = hasHitRightPaddle;
    }

    public boolean isHasHitLeftPaddle()
    {
        return hasHitLeftPaddle;
    }

    public void setHasHitLeftPaddle(boolean hasHitLeftPaddle)
    {
        this.hasHitLeftPaddle = hasHitLeftPaddle;
    }

    public int getWindowX()
    {
        return windowX;
    }

    public void setWindowX(int windowX)
    {
        this.windowX = windowX;
    }

    public int getWindowY()
    {
        return windowY;
    }

    public void setWindowY(int windowY)
    {
        this.windowY = windowY;
    }

    public int getScoreToPlayTo()
    {
        return scoreToPlayTo;
    }

    public void setScoreToPlayTo(int scoreToPlayTo)
    {
        this.scoreToPlayTo = scoreToPlayTo;
    }

    public boolean isGameOver()
    {
        return gameOver;
    }

    public void setGameOver(boolean gameOver)
    {
        this.gameOver = gameOver;
    }
}
