package edu.byuh.cis.cs203.numberedsquares.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import edu.byuh.cis.cs203.numberedsquares.R;

/**
 * Created by tylajeffs
 */

public class BeginningActivity extends Activity
{
    private ImageView background;

    @Override
    public void onCreate(Bundle b)
    {
        //always call super
        super.onCreate(b);

        //load the image background
        background = new ImageView(this);
        background.setImageResource(R.drawable.startscreen);
        background.setScaleType(ImageView.ScaleType.FIT_XY);

        //set the content view
        setContentView(background);

    }

    @Override
    public boolean onTouchEvent(MotionEvent m)
    {
        //if user touches screen, start game or go to settings
        if(m.getAction() == MotionEvent.ACTION_DOWN)
        {
            //find the coordinates that were tapped
            float x = m.getX();
            float y = m.getY();

            //find the width and height of the screen
            float width = background.getWidth();
            float height = background.getHeight();

            //if coordinates are on the settings button, change to settings page
            if(x > (0.75 * width) && y > (0.8 * height))
            {
                //launch settings page
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
            }
            else
            {
                //start the game (MainActivity)
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);

                //kill the beginning page/activity
                finish();
            }



        }


        //always returns true
        return true;
    }



}
