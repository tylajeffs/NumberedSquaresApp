package edu.byuh.cis.cs203.numberedsquares.Activities;

import android.app.Activity;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;

import edu.byuh.cis.cs203.numberedsquares.GameView;

/**
 * created by tylajeffs
 */

public class MainActivity extends Activity
{

    //fields
    private GameView gv;



    /**
     * It all starts here
     * @param savedInstanceState - passed in by OS. Ignore for now.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //create new view
        gv = new GameView(this);
        setContentView(gv);


    }

    /**
     * overrides onResume, just calls the resume method in game view
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        gv.resume();

    }

    /**
     * overrides onPause, just calls the pause method in game view
     */
    @Override
    protected void onPause()
    {
        super.onPause();

        gv.pause();
    }

    /**
     * overrides onDestroy, just calls the shutdown cleanup method in game view
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        gv.shutdownCleanup();
    }

}
