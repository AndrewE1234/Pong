// Andrew Eljumaily
// 2017/12/28
// Ball
//
// The Ball class acts as the template
// for the ball used in the game.
// It is mostly just an extension of
// the javaFX Circle class, with the
// main of addition of the moveSpeedX
// and moveSpeedY variables, which
// control its "speed"(Number of
// pixels to move per frame) in the
// game.

package andrew.fx.pong;

import javafx.scene.shape.Circle;

public class Ball extends Circle
{
    private double moveSpeedX = 0;
    private double moveSpeedY = 0;

    Ball(double centerX, double centerY, double radius, double moveSpeedX, double moveSpeedY)
    {
        this.setCenterX(centerX);
        this.setCenterY(centerY);
        this.setRadius(radius);
        this.moveSpeedX = moveSpeedX;
        this.moveSpeedY = moveSpeedY;
    }


    public double getMoveSpeedX()
    {
        return moveSpeedX;
    }

    public void setMoveSpeedX(double moveSpeedX)
    {
        this.moveSpeedX = moveSpeedX;
    }

    public double getMoveSpeedY()
    {
        return moveSpeedY;
    }

    public void setMoveSpeedY(double moveSpeedY)
    {
        this.moveSpeedY = moveSpeedY;
    }
}