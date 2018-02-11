// Andrew Eljumaily
// 2017/12/28
// Paddle
//
// The Paddle class is an abstract class that
// acts as a template for the ComputerPaddle
// and HumanPaddle classes, including a few
// essential variables and methods.
// The most notable are the handlePaddleHit
// method, which determines the angle the
// ball travels at once it hits the paddle,
// the abstract method movePaddle, which is
// to be implements in each child class.

package andrew.fx.pong.Paddles;

import andrew.fx.pong.Ball;
import javafx.scene.shape.Rectangle;

public abstract class Paddle extends Rectangle
{
    private double moveSpeed = 0;
    private double upperBound = 0;
    private double lowerBound = 0;

    public Paddle()
    {
    }

    public void handlePaddleHit(Ball ball)
    {
        //Determines the pitch (Y) of the ball by finding its position relative to
        //the paddle's center.
        double center = this.getHeight()/2;

        double newYDirection = ((ball.getCenterY() - (this.getY() + center)))/10;
        ball.setMoveSpeedY(newYDirection);

        ball.setMoveSpeedX(ball.getMoveSpeedX() * -1);
    }


    public abstract void movePaddle(double targetPosition);


    public double getMoveSpeed()
    {
        return moveSpeed;
    }

    public void setMoveSpeed(double moveSpeed)
    {
        this.moveSpeed = moveSpeed;
    }

    public double getUpperBound()
    {
        return upperBound;
    }

    public void setUpperBound(double upperBound)
    {
        this.upperBound = upperBound;
    }

    public double getLowerBound()
    {
        return lowerBound;
    }

    public void setLowerBound(double lowerBound)
    {
        this.lowerBound = lowerBound;
    }
}