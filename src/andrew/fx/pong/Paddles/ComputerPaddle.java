// Andrew Eljumaily
// 2017/12/28
// ComputerPaddle
//
// The HumanPaddle class acts as the
// controller for a computer controlled
// game paddle. The key part of this
// class is the movePaddle methods,
// which works by moving the paddle x
// number of pixels per frame towards a
// set target. It is similar to the
// movePaddle method in the HumanPaddle
// class, but has some key differences
// listed within the method.

package andrew.fx.pong.Paddles;

public class ComputerPaddle extends Paddle
{
    public ComputerPaddle(double x, double y, double width, double height, double moveSpeed, double upperBound,
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

        // The ComputerPaddle's target position is set at an offset of the moveSpeed to
        // prevent the paddle from jittering.
        if(this.getY() + (this.getHeight()/2) <= targetPosition + this.getMoveSpeed() &&
                this.getY() + (this.getHeight()/2) >= targetPosition - this.getMoveSpeed())
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
}