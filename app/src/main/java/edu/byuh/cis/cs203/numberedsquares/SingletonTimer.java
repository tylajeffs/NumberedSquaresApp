package edu.byuh.cis.cs203.numberedsquares;

import android.os.Handler;
import android.os.Message;
import java.util.ArrayList;

import edu.byuh.cis.cs203.numberedsquares.Interfaces.TimerListener;


/**
 * created by tylajeffs
 */
public class SingletonTimer extends Handler
{

    //create list of TimerListener objects (observers)
    ArrayList<TimerListener> observers;

    //create timer object
    private static SingletonTimer timer = null;



    /**
     * constructor. Just begins animation
     */
    private SingletonTimer()
    {
        observers = new ArrayList<>();

        //begin the animation
        sendMessageDelayed(obtainMessage(),0);
    }




    /**
     * singleton factory method to create the instance of the timer object
     * @return the timer object
     */
    public static SingletonTimer getTimer()
    {
        //check to see if the timer has been created yet
        if(timer == null)
        {
            //if it's null, create the timer
            timer = new SingletonTimer();
        }

        //return the timer
        return timer;
    }



    /**
     * Method to add observers to the Timer
     * @param t TimerListener to be added to the observers arraylist
     */
    public void addObserver(TimerListener t)
    {
        //add the abject to the observers arraylist
        observers.add(t);
    }




    /**
     * method to remove observers
     * @param t - the object to be removed
     *          object must implement TimerListener
     */
    public void removeObserver(TimerListener t)
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
        for(TimerListener t: observers)
        {
            t.doSomething();
        }

        //run animation again with no delay
        sendMessageDelayed(obtainMessage(),0);
    }

}
