package edu.byuh.cis.cs203.numberedsquares.GameTypes;

import android.content.res.Resources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.byuh.cis.cs203.numberedsquares.Enums.GameStatus;
import edu.byuh.cis.cs203.numberedsquares.Interfaces.GameStyle;
import edu.byuh.cis.cs203.numberedsquares.NumberedSquare;
import edu.byuh.cis.cs203.numberedsquares.R;


/**
 * Created by tylajeffs
 */

public class AlphabetGame implements GameStyle
{
    //fields
    private ArrayList<String> allLabels;
    private GameStatus gameStatus;
    private String alphabet;
    private int level;
    private ArrayList<String> levelLabels;
    private String expectedLabel;
    private int numCorrectSquaresTouched;
    private Random random;
    private ArrayList<String> sortedLabels;
    private int maxLevel;
    private Resources r;



    /**
     * constructor, initializes a few things
     */
    public AlphabetGame(int maxLevels, Resources res)
    {
        //set level
        resetLevel();

        //set resources
        r = res;

        //set max level
        maxLevel = maxLevels;

        //instantiate random object
        random = new Random();

        //set number of correct squares touched to 0
        numCorrectSquaresTouched = 0;


        //get and set the alphabet labels
        String alphabetString = r.getString(R.string.alphabet);
        alphabet = alphabetString;


        //initialize labels
        levelLabels = new ArrayList<String>();
        sortedLabels = new ArrayList<String>();

        //get the labels for this level (letters in the word)
        getThisLevelsLabels();

        //set the expected label to be the first letter in the word
        resetExpectedLabel();

    }


    /**
     * Method to get the string for the next level toast message
     *
     * @param res
     * @return String for the next level toast
     */
    @Override
    public String getNextLevelLabel(Resources res)
    {
        //get the string
        String s = r.getString(R.string.alphabet_next_level_label);

        return s;
    }


    /**
     * Method to get the label for the try again toast
     *
     * @param res
     * @return String for the toast
     */
    @Override
    public String getTryAgainLabel(Resources res)
    {
        //get the string
        String s = r.getString(R.string.alphabet_try_again_label);

        return s;
    }




    /**
     * method to get the labels for the squares for that level
     * @return list of string labels
     */
    @Override
    public List<String> getSquareLabels()
    {
        //split the string of level labels into a new list of labels
        getThisLevelsLabels();

        resetExpectedLabel();

        return levelLabels;
    }


    /**
     * method to return the game status - continue, level complete, try again
     * @param ns Numbered Square that was touched
     * @return
     */
    @Override
    public GameStatus getGameStatus(NumberedSquare ns)
    {

        //check if square label is equal to expected label
        if(ns.label.equals(expectedLabel))
        {
            //if the labels match, check to see if it is the last square in the level
            if(numCorrectSquaresTouched == (level - 1))
            {
                //check to see if it's the last level
                //if it is, return game over
                if(level == maxLevel)
                {
                    //game over
                    return gameStatus.GAME_OVER;
                }
                else
                {

                    increaseLevel();
                    //return level complete if it is the last square
                    return gameStatus.LEVEL_COMPLETE;
                }

            }
            else
            {
                //it's just the right square, return continue and increase the expected label
                increaseNumCorrectSquaresTouched();
                increaseExpectedLabel();

                return gameStatus.CONTINUE;
            }

        }
        else   //wrong square
        {

            //(if it's not frozen) wrong square, try again
            if(!ns.frozen)
            {
                //reset the expected label id
                resetExpectedLabel();

                //reset the number of correct squares that were touched
                resetNumCorrectSquaresTouched();

                return gameStatus.TRY_AGAIN;
            }
            else //continue if the square is already frozen
            {
                return gameStatus.CONTINUE;
            }

        }

    }



    /**
     * helper method that increases the level by 1
     */
    private void increaseLevel()
    {
        //increase the level by one
        level++;

        //get the labels for this level
        getThisLevelsLabels();

        //reset the number of correct squares that were touched
        resetNumCorrectSquaresTouched();

        //reset the expected label with the next level's word
        resetExpectedLabel();
    }


    /**
     * helper method to set level back to 1
     */
    private void resetLevel()
    {
        //set level to 0
        level = 1;
    }


    /**
     * helper method to split the string per level and find the labels for one specific level
     */
    private void getThisLevelsLabels()
    {
        //clear the lists
        levelLabels.clear();
        sortedLabels.clear();

        //get the random letters
        getRandomLetterLabels();

        //add all elements in level labels to the sorted labels arraylist
        sortedLabels.addAll(levelLabels);

        //sort the sortedLabels in alphabetical order
        Collections.sort(sortedLabels);

    }

    /**
     * helper method to reset the the expected label to the first letter in the level's word
     */
    private void resetExpectedLabel()
    {
        //set the expected label to be the first letter in the word for this level
        expectedLabel = sortedLabels.get(0);

    }

    /**
     * helper method to increase the expected label by 1 letter in the level's word
     */
    private void increaseExpectedLabel()
    {
        //increase the expected label
        expectedLabel = sortedLabels.get(numCorrectSquaresTouched);

    }


    /**
     * helper method to increase the count of correct squares touched in the level
     */
    private void increaseNumCorrectSquaresTouched()
    {
        //increase by 1
        numCorrectSquaresTouched += 1;
    }


    /**
     * helper method to reset the amount of correct squares touched in the level
     */
    private void resetNumCorrectSquaresTouched()
    {
        //set to 0
        numCorrectSquaresTouched = 0;
    }


    /**
     * method that restarts the game, resets all variables
     */
    @Override
    public void resetGame()
    {
        //reset level to 1
        resetLevel();

        //set number of squares touched to 0
        resetNumCorrectSquaresTouched();

        //get the labels for this level
        getThisLevelsLabels();

        //reset the expected label with the 1st level's word
        resetExpectedLabel();
    }


    /**
     * method to get random letters based on the level
     */
    private void getRandomLetterLabels()
    {
        ArrayList<Integer> usedIndexes = new ArrayList<>();
        int randomIndex = 0;

        for(int i = 0; i < level; i++)
        {

            //get a random index between 0 (inclusive) and 26 (exclusive)
            randomIndex = random.nextInt(26);

            //make sure the letter hasn't been used yet
            if(!usedIndexes.contains(randomIndex))
            {

                //add the index to the used indexes
                usedIndexes.add(randomIndex);

                //get the character at the random index from the alphabet and add it to an arraylist
                levelLabels.add(Character.toString(alphabet.charAt(randomIndex)));

            }
            else
            {
                //decrease
                i--;
            }

        }
    }


    /**
     * Overridden to string class
     * @return the type of game
     */
    @Override
    public String toString()
    {
        return r.getString(R.string.alphabet_game_name);
    }

}
