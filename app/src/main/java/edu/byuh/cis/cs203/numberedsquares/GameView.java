package edu.byuh.cis.cs203.numberedsquares;

import static java.lang.Math.abs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.byuh.cis.cs203.numberedsquares.Activities.SettingsActivity;
import edu.byuh.cis.cs203.numberedsquares.Enums.Side;
import edu.byuh.cis.cs203.numberedsquares.GameTypes.AlphabetGame;
import edu.byuh.cis.cs203.numberedsquares.GameTypes.CountingGame;
import edu.byuh.cis.cs203.numberedsquares.GameTypes.FlowerGame;
import edu.byuh.cis.cs203.numberedsquares.GameTypes.HawaiiGame;
import edu.byuh.cis.cs203.numberedsquares.GameTypes.PlantGame;
import edu.byuh.cis.cs203.numberedsquares.Interfaces.GameStyle;
import edu.byuh.cis.cs203.numberedsquares.Interfaces.ScoreTimerListener;
import edu.byuh.cis.cs203.numberedsquares.Interfaces.TimerListener;


/**
 * Created by tylajeffs
 */

public class GameView extends AppCompatImageView implements TimerListener, ScoreTimerListener
{

    //create fields
    private boolean calcDone;
    private float w,h;
    private SingletonTimer time;
    private ScoreTimer scoreTimer;
    private Side side;
    private int level = 1;
    private int expectedId = 1;
    private MediaPlayer music;
    private Context con;
    private GameStyle gameStyle;
    private ArrayList<String> labels;
    private List<NumberedSquare> squares;
    private int gameType;
    private int maxLevels;
    private int totalScore;
    private int speedBonusScore;
    private Paint scorePaint;
    private int highScore;




    /**
     * GameView constructor. Just initializes a few variables
     * @param context - required
     */
    public GameView(Context context) {

        super(context);

        //decide how many levels you want
        maxLevels = 4;

        //set context so we can use it outside of constructor
        con = context;

        //set the calculations done to false
        calcDone = false;

        //instantiate array lists
        squares = new ArrayList<>();
        labels = new ArrayList<String>();

        //instantiate singleton timer objects
        time = SingletonTimer.getTimer();
        scoreTimer = ScoreTimer.getTimer();

        //add GameView class to the observers using this
        time.addObserver(this);
        scoreTimer.addObserver(this);

        //instantiate game style as whatever game type the user picked in settings
        gameType = SettingsActivity.getGameType(context);
        instantiateGameType();


        //try to open the file with the high score
        try (Scanner s = new Scanner(context.openFileInput(gameStyle.toString() + ".txt"))) {
            //read the high score and store it
            highScore = s.nextInt();

        } catch (IOException e) {
            //high score is 0
            highScore = 0;
        }


        //instantiate score to be 0
        resetTotalScore();

        //instantiate paint object
        scorePaint = new Paint();

        //set score paint attributes
        scorePaint.setTextSize(50);
        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextAlign(Paint.Align.RIGHT);

        //instantiate and start music based on settings
        startMusic();
    }


    /**
     * All the rendering happens here. The first time onDraw is called, we also do some
     * one-time initialization of the graphics objects (since the width and height of
     * the screen are not known until onDraw is called).
     * @param c
     */
    @Override
    public void onDraw(Canvas c) {

        if (!calcDone) {
            //get the width and height
            w = c.getWidth();
            h = c.getHeight();

            //create the squares using the labels from the specified game
            createSquares(gameStyle.getSquareLabels());

            //set calcDone to true
            calcDone = true;
        }



        //set the background color depending on the settings
        if(SettingsActivity.backgroundIsLight(con)) {
            //light background
            // light green background c.drawColor(Color.rgb(215,228,184));
            c.drawColor(Color.rgb(214,234,255));
        } else {
            //dark background
            c.drawColor(Color.rgb(101,116,85));
        }


        //get the string for the score counter
        String scoreCounter = getResources().getString(R.string.score_counter);

        //draw the text where the current score is kept
        c.drawText(scoreCounter + " " + totalScore,w - 10,50,scorePaint);


        //draw each of the squares in the list
        for (NumberedSquare ns : squares) {
            //draw the square
            ns.draw(c);
        }
    }


    /**
     * helper method to instantiate the correct game type that the user chose
     * in the settings
     */
    public void instantiateGameType() {
        if(gameType == 1) {
            //counting game
            gameStyle = new CountingGame(maxLevels, getResources());

        } else if(gameType == 2) {
            //flower game
            gameStyle = new FlowerGame(getResources());

        } else if(gameType == 3) {
            //alphabet game
            gameStyle = new AlphabetGame(maxLevels, getResources());

        } else if(gameType == 4) {
            //hawaii game
            gameStyle = new HawaiiGame(getResources());

        } else {
            //plant game
            gameStyle = new PlantGame(getResources());
        }
    }





    /**
     * Helper method for creating NumberedSquare objects
     * @param labels the list of labels for all the squares you want created in that level
     */
    private void createSquares(List<String> labels) {

        //clear the list of squares
        squares.clear();

        //initialize the labels list to be equal to the labs list

        //reset the counter back to 1
        NumberedSquare.resetCounter();

        //create new numbered squares based on the size of the list of labels
        for (int i=0; i<labels.size(); i++) {

            //create new numbered square using the labels list
            NumberedSquare ns = new NumberedSquare(labels.get(i),w, h, getResources(), getContext());

            //create a boolean to see if they overlap or not - assume they don't overlap
            boolean overlaps = false;

            //go through each square in the list of squares and see if they overlap
            for(NumberedSquare square:squares) {

                if(ns.overlaps(square)) {
                    //set overlaps to true
                    overlaps = true;

                    //decrease the counter to show that it got deleted - need to keep numbers correct
                    ns.decreaseCounter();

                    //subtract 1 from i so the code will make a new square
                    //to replace the one that overlapped
                    i--;

                    //break the loop and try again
                    break;
                }
            }

            //if it doesn't overlap, add it to the squares list
            if(overlaps == false) {
                //add ns to the squares list
                squares.add(ns);

                //add squares to observers
                for(NumberedSquare square : squares) {
                    time.addObserver(square);
                }
            }
        }
    }





    /**
     * this method gets called automatically whenever a user taps the screen
     *
     * @param m - mandatory parameter, motion event
     * @return always returns true
     */

    @Override
    public boolean onTouchEvent(MotionEvent m) {

        //figure out if screen was tapped (based on release)
        if(m.getAction() == MotionEvent.ACTION_UP) {

            //send message to Logcat saying the screen was tapped
            Log.d("log", "screen tapped");

            //find the coordinates that were tapped
            float x = m.getX();
            float y = m.getY();

            for(NumberedSquare square:squares) {

                //check if the square contains the coordinates
                if(square.contains(x,y)) {

                    //send message to Logcat saying the square was tapped
                    Log.d("log", "you tapped square " + square.id);



                    switch (gameStyle.getGameStatus(square)) {
                        case LEVEL_COMPLETE:

                            //add the speed bonus points and the level points to the total score
                            //(each level gives you points equal to the # squares you tapped * 10
                            totalScore += speedBonusScore;
                            totalScore += (level * 10);

                            //increase level
                            levelUp();

                            //clear the labels list
                            labels.clear();

                            //send toast message
                            Toast t = Toast.makeText(getContext(),gameStyle.getNextLevelLabel(getResources()),Toast.LENGTH_SHORT);
                            t.show();

                            //start next level
                            startLevel(level);
                            break;

                        case CONTINUE:

                            //freeze the square
                            square.freeze();

                            //increase expected ID
                            expectedId++;
                            break;



                        case TRY_AGAIN:

                            //no speed bonus points available if they mess up
                            restartLevel();
                            break;


                        case GAME_OVER:

                            //freeze the square
                            square.freeze();

                            //instantiate the alert dialog builder
                            AlertDialog.Builder ab = new AlertDialog.Builder(getContext());

                            //compare highscore to current score
                            if(totalScore > highScore) {

                                try (FileOutputStream highScoreFile = getContext().openFileOutput(gameStyle.toString() + ".txt", Context.MODE_PRIVATE)) {

                                    //turn the current score into a string
                                    String newHighScore = Integer.toString(totalScore);

                                    //write the new high score into the file
                                    highScoreFile.write(newHighScore.getBytes());

                                } catch (IOException e) {
                                    Log.d("CS203", "error writing to filesystem");
                                }


                                //get the strings for the new high score output
                                String newHighScoreTitle = getResources().getString(R.string.new_high_score_title);
                                String newHighScoreText = getResources().getString(R.string.new_high_score_text);
                                String newHighScorePlayAgain = getResources().getString(R.string.new_high_score_play_again);


                                //set the content of the alert dialog to be about the high score
                                ab.setTitle(newHighScoreTitle);
                                ab.setMessage(newHighScoreText + totalScore + newHighScorePlayAgain);

                            } else {
                                //get the strings for the new high score output
                                String gameOverTitle = getResources().getString(R.string.game_over_title);
                                String gameOverText = getResources().getString(R.string.game_over_text);


                                //set the content of the alert dialog
                                ab.setTitle(gameOverTitle);
                                ab.setMessage(gameOverText + highScore);
                            }


                            //make sure the user has to click one of the buttons
                            ab.setCancelable(false);


                            //set button options
                            //get the string for the positive button
                            String positiveText = getResources().getString(R.string.positive_button_text);

                            //positive button using anonymous class
                            ab.setPositiveButton(positiveText, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    //reset the total score, level, and speed bonus
                                    resetTotalScore();
                                    level = 1;
                                    resetSpeedBonus();

                                    //re-instantiate the game type object to reset everything
                                    //instantiateGameType();

                                    gameStyle.resetGame();

                                    //create the squares using the labels from the specified game
                                    createSquares(gameStyle.getSquareLabels());
                                }
                            });


                            //get the string for the negative button
                            String negativeText = getResources().getString(R.string.negative_button_text);

                            //negative button with a lambda expression
                            ab.setNegativeButton(negativeText, (d,i) -> ((Activity)getContext()).finish());

                            //create the alert dialog itself
                            AlertDialog box = ab.create();

                            //show the alert dialog
                            ab.show();
                            break;
                    }
                    break;
                }
            }

            //redraw the squares in the list using invalidate method
            invalidate();
        }
        //always returns true
        return true;
    }




    /**
     * Override doSomething from the TimerListener interface to redraw the screen
     */
    @Override
    public void doSomething() {
        //check to see if any squares are bumping into each other
        for(NumberedSquare a: squares) {
            for(NumberedSquare b: squares) {

                //code to make sure there is no redundancy in checking squares against each other
                if(a.id > b.id) {

                    //check to see if the two squares overlap
                    if(a.overlaps(b)) {

                        //if the sides touching are top or bottom, exchange y velocity
                        if(findSideHit(a,b) == Side.TOP || findSideHit(b,a) == Side.TOP || findSideHit(a,b) == Side.BOTTOM || findSideHit(b,a) == Side.BOTTOM) {

                            //check if either of the squares are frozen
                            if(a.frozen) {
                                //reverse b's y velocity
                                b.velocity.y *= -1;
                            } else if(b.frozen) {
                                //reverse a's y velocity
                                a.velocity.y *= -1;
                            } else {
                                //force them apart code keeps breaking!
                                //force them apart
                                //forceApart(a,b);

                                //exchange y velocities
                                exchangeYVelocity(a,b);
                            }
                        }

                        //if sides touching are left or right, exchange x velocity
                        if(findSideHit(a,b) == Side.LEFT || findSideHit(b,a) == Side.LEFT|| findSideHit(a,b) == Side.RIGHT || findSideHit(b,a) == Side.RIGHT) {

                            //check if either of the squares are frozen
                            if(a.frozen) {

                                //reverse b's x velocity
                                b.velocity.x *= -1;
                            } else if(b.frozen) {

                                //reverse a's x velocity
                                a.velocity.x *= -1;
                            } else {

                                // force them apart code keeps breaking!
                                //force them apart
                                //forceApart(a,b);

                                //exchange x velocities
                                exchangeXVelocity(a,b);
                            }
                        }
                    }
                }
            }
        }

        //make the screen redraw by calling invalidate
        invalidate();
    }






    /**
     * exchanges the Y velocities of the numbered squares passed in
     * @param a numbered square
     * @param b numbered square
     */
    public void exchangeYVelocity(NumberedSquare a, NumberedSquare b) {
        //store square a's y velocity
        float aVel = a.velocity.y;

        //exchange velocities
        a.velocity.y = b.velocity.y;
        b.velocity.y = aVel;
    }




    /**
     * exchanges the X velocities of the numbered squares passed in
     * @param a numbered square
     * @param b numbered square
     */
    public void exchangeXVelocity(NumberedSquare a, NumberedSquare b) {

        //store square a's x velocity
        float aVel = a.velocity.x;

        //exchange velocities
        a.velocity.x = b.velocity.x;
        b.velocity.x = aVel;
    }





    /**
     * method to find the side that got hit from two squares colliding
     *
     * @param a numbered square to be tested
     * @param b numbered square to be tested
     * @return an emum, which side got hit on square a
     */
    public Side findSideHit(NumberedSquare a, NumberedSquare b) {

        //initialize hitSide to empty (for debugging)
        Side hitSide = Side.EMPTY;

        //find the difference between sides to see which side is hitting (the smallest ones are hitting)
        float rightDif = abs(a.squareBounds.right - b.squareBounds.left);
        float leftDif = abs(a.squareBounds.left - b.squareBounds.right);
        float topDif = abs(a.squareBounds.top - b.squareBounds.bottom);
        float bottomDif = abs(a.squareBounds.bottom - b.squareBounds.top);

        //find the smallest one
        float smallest = Math.min(Math.min(rightDif,leftDif),Math.min(topDif,bottomDif));

        //assign enum based on which is smallest
        //right
        if(smallest == rightDif) {
            //assign right
            hitSide = Side.RIGHT;
        }
        //left
        if(smallest == leftDif) {
            //assign left
            hitSide = Side.LEFT;
        }
        //top
        if(smallest == topDif) {
            //assign top
            hitSide = Side.TOP;
        }
        //bottom
        if(smallest == bottomDif) {
            //assign bottom
            hitSide = Side.BOTTOM;
        }

        //return the side
        return hitSide;
    }


    /**
     * Method to force apart the squares when they hit each other
     * just makes sure they don't get stuck together
     *
     * @param a numbered square
     * @param b numbered square
     */
//    public void forceApart(NumberedSquare a, NumberedSquare b)
//    {
//        switch (side)
//        {
//            case LEFT:
//                a.setLeft(b.squareBounds.right + 1);
//                b.setRight(b.squareBounds.left - 1);
//                break;
//            case RIGHT:
//                a.setRight(b.squareBounds.left - 1);
//                b.setLeft(a.squareBounds.right + 1);
//                break;
//            case TOP:
//                this.setTop((int)b.squareBounds.bottom + 1);
//                b.setBottom(a.squareBounds.top - 1);
//                break;
//            case BOTTOM:
//                this.setBottom((int)b.squareBounds.top - 1);
//                b.setTop(a.squareBounds.bottom + 1);
//        }
//    }


    /**
     * method to restart the level when the user touches the wrong square
     */
    public void restartLevel() {
        startLevel(level);

        //send toast message
        Toast t = Toast.makeText(getContext(),gameStyle.getTryAgainLabel(getResources()),Toast.LENGTH_SHORT);
        t.show();

        //send message to Logcat saying the level restarted
        Log.d("log", "level restarted");
    }


    /**
     * method to start the level
     *
     * @param lev the level the user is on
     */
    public void startLevel(int lev) {
        //reset expected ID
        resetExpectedId();

        //unregister previous squares
        for(NumberedSquare square : squares) {
            time.removeObserver(square);
        }

        //create the squares again - will also register them
        createSquares(gameStyle.getSquareLabels());

        //reset the potential speed bonus for that level
        resetSpeedBonus();
    }


    /**
     * helper method to reset the expected ID back to 1
     */
    public void resetExpectedId() {
        //set expected ID to 1
        expectedId = 1;
    }


    /**
     * helper method that increases the level by 1
     */
    public void levelUp() {
        //increase level
        level++;
    }




    /**
     * method for when the app gets paused. Takes care of pausing music
     */
    public void pause() {
        //pause background music
        pauseMusic();
    }

    /**
     * method for when app is resumed
     */
    public void resume() {
        //resume background music
        resumeMusic();
    }


    /**
     * method to take care of all cleanup on shutdown
     */
    public void shutdownCleanup() {
        //make sure music exists
        if(music != null) {
            //kill the background music
            music.release();
        }
    }


    /**
     * method that pauses the background music
     */
    public void pauseMusic() {
        //make sure music exists
        if(music != null) {
            //pause the music
            music.pause();
        }
    }


    /**
     * method for resuming the background music
     */
    public void resumeMusic() {
        //make sure music exists
        if(music != null) {
            //resume the music
            music.start();
        }
    }


    /**
     * helper method to start the music in the very beginning of the game
     */
    public void startMusic() {
        //check to see if the user set the music setting to on
        if(SettingsActivity.soundOn(con)) {
            music = MediaPlayer.create(con,R.raw.music);
            music.start();
            music.setLooping(true);
        }
    }




    /**
     * ScoreTimerListener method to override, decreases the speed bonus score
     * by one for each second the level is not completed
     */
    @Override
    public void decreaseScore() {
        //make sure level score doesn't go below 0
        if(speedBonusScore > 0 ) {
            //for every second that goes by in the level, decrease level score by 1
            speedBonusScore -= 1;
        }
    }

    /**
     * helper method to reset the level score
     * Each level has a certain number of points they can receive as a speed bonus,
     * increasing by 3 points for each level
     * ex.
     * level 1 = 3 points available
     * level 2 = 6 points available
     * level 3 = 9 points available
     */
    private void resetSpeedBonus() {
        //set level score to be 2 points for the first level, and add 2 more points for each
        //additional level
        speedBonusScore = 2 + ((level-0)*2);
    }


    /**
     * helper method to set the total score to 0
     */
    public void resetTotalScore() {
        //set total score to 0
        totalScore = 0;
    }
}


