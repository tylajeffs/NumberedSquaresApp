package edu.byuh.cis.cs203.numberedsquares.Interfaces;

import android.content.res.Resources;
import java.util.List;

import edu.byuh.cis.cs203.numberedsquares.Enums.GameStatus;
import edu.byuh.cis.cs203.numberedsquares.NumberedSquare;

public interface GameStyle
{
    /**
     * returns the label for the next level toast
     */
    String getNextLevelLabel(Resources res);

    /**
     * returns the string to display in the try again toast
     */
    String getTryAgainLabel(Resources res);

    /**
     * returns the labels for the squares
     * @return list of lables for the squares
     */
    List<String> getSquareLabels();

    /**
     * returns the game status (try again, level complete, continue) once a square is touched
     * @param c the numbered square that was touched
     * @return the game status (try again, level complete, continue)
     */
    GameStatus getGameStatus(NumberedSquare c);


    void resetGame();
}
