// Andrew Eljumaily
// 2017/12/28
// HumanPaddle
//
// The HumanPaddle class acts as the
// controller for a user controlled
// game paddle. Like the
// ComputerPaddle class, its
// movePaddle works by moving
// the paddle x number of pixels
// per frame towards a set target.
// The key difference is that the
// HumanPaddle class contains the
// flags movePaddleUp and
// movePaddleDown to determine
// weather or not the movePaddle
// method should be called (The
// flags are currently set by
// onKeyPress and onKeyRelease in
// the scene).

package andrew.fx.pong.Paddles;

public class HumanPaddle extends Paddle
{
    private boolean movePaddleUp = false;
    private boolean movePaddleDown = false;

    public HumanPaddle(double x, double y, double width, double height, double moveSpeed, double upperBound,
                       double lowerBound)
    {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.setMoveSpeed(moveSpeed);
        this.setUpperBound(upperBound);
        this.setLowerBound(lowerBound);
    }

    @Override
    public void movePaddle(double targetPosition)
    {
        // Reached it's bounds.
        if((this.getY() <= getUpperBound() && targetPosition <= (this.getHeight()/2))
                || (this.getY()+this.getHeight() >= getLowerBound()
                && targetPosition >= (this.getY()+(this.getHeight()/2))))
        {
            return;
        }

        if(this.getY() == targetPosition || this.getY() + this.getHeight() == targetPosition)
        {
            return;
        }
        else if(this.getY() + (this.getHeight()/2) < targetPosition)
        {
            this.setY(this.getY()+this.getMoveSpeed());
        }
        else if(this.getY() + (this.getHeight()/2) > targetPosition)
        {
            this.setY(this.getY()-this.getMoveSpeed());
        }
    }

    public boolean isMovePaddleUp()
    {
        return movePaddleUp;
    }

    public void setMovePaddleUp(boolean movePaddleUp)
    {
        this.movePaddleUp = movePaddleUp;
    }

    public boolean isMovePaddleDown()
    {
        return movePaddleDown;
    }

    public void setMovePaddleDown(boolean movePaddleDown)
    {
        this.movePaddleDown = movePaddleDown;
    }
}