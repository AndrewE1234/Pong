// Andrew Eljumaily
// 2017/12/28
// Score
//
// The Score class is mostly just an
// extension of the JavaFX Label class,
// but features a few key methods and
// variables to help with the in game
// score board. This includes the
// currentScore variable which keeps
// track current score, the method
// increaseScore which increments the
// score by one and sets the text to
// the new score, and the method
// resetScore which resets both
// currentScore and the on screen
// score.
// It should be noted that
// constructor automatically sets
// the on screen score to the value
// of currentScore.
// It should also be noted that one
// Score object represents only ONE
// in game score, not both.

package andrew.fx.pong;

import javafx.scene.control.Label;

public class Score extends Label
{
    private int currentScore = 0;


    public Score(double layoutX, double layoutY, double scaleX, double scaleY, int currentScore)
    {
        this.setLayoutX(layoutX);
        this.setLayoutY(layoutY);
        this.setScaleX(scaleX);
        this.setScaleY(scaleY);

        this.currentScore = currentScore;
        this.setText(Integer.toString(currentScore));
    }

    public void increaseScore()
    {
        currentScore++;
        this.setText(Integer.toString(currentScore));
    }

    public void resetScore()
    {
        currentScore = 0;
        this.setText(Integer.toString(currentScore));
    }


    public int getCurrentScore()
    {
        return currentScore;
    }

    public void setCurrentScore(int currentScore)
    {
        this.currentScore = currentScore;
    }
}
