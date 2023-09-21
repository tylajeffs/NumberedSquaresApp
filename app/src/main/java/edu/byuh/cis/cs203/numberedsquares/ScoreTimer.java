package edu.byuh.cis.cs203.numberedsquares;

import android.os.Handler;
import android.os.Message;
import java.util.ArrayList;

import edu.byuh.cis.cs203.numberedsquares.Interfaces.ScoreTimerListener;
import edu.byuh.cis.cs203.numberedsquares.Interfaces.TimerListener;


/**
 * created by tylajeffs
 */
public class ScoreTimer extends Handler
{

    //create list of TimerListener objects (observers)
    ArrayList<ScoreTimerListener> observers;

    //create timer object
    private static ScoreTimer timer = null;



    /**
     * constructor. Just begins timer
     */
    private ScoreTimer()
    {
        observers = new ArrayList<>();

        //begin the timer with 1 second between messages
        sendMessageDelayed(obtainMessage(),1000);
    }




    /**
     * singleton factory method to create the instance of the timer object
     * @return the timer object
     */
    public static ScoreTimer getTimer()
    {
        //check to see if the timer has been created yet
        if(timer == null)
        {
            //if it's null, create the timer
            timer = new ScoreTimer();
        }

        //return the timer
        return timer;
    }



    /**
     * Method to add observers to the Timer
     * @param t TimerListener to be added to the observers arraylist
     */
    public void addObserver(ScoreTimerListener t)
    {
        //add the abject to the observers arraylist
        observers.add(t);
    }




    /**
     * method to remove observers
     * @param t - the object to be removed
     *          object must implement TimerListener
     */
    public void removeObserver(ScoreTimerListener t)
    {
        //remove the object from the observes arraylist
        observers.remove(t);
    }



    /**
     * handles the message and does animation
     * @param m message
     */
    @Override
    public void handleMessage(Message m)
    {
        //have each object in array do their action
        for(ScoreTimerListener t: observers)
        {
            t.decreaseScore();
        }

        //run animation again with no delay
        sendMessageDelayed(obtainMessage(),1000);
    }

}
