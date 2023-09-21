package edu.byuh.cis.cs203.numberedsquares.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.preference.ListPreference;
import android.view.MotionEvent;

//import androidx.preference.ListPreference;

import edu.byuh.cis.cs203.numberedsquares.Activities.MainActivity;
import edu.byuh.cis.cs203.numberedsquares.R;

public class SettingsActivity extends PreferenceActivity
{



    /**
     * takes care of all initialization stuff
     * @param b bundle (required)
     */
    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);

        //create preferences screen
        PreferenceScreen ps = getPreferenceManager().createPreferenceScreen(this);


        //create switch setting for music
        SwitchPreference music = new SwitchPreference(this);
        //set music title and words
        music.setTitle(getResources().getString(R.string.music_pref_title));
        music.setSummaryOn(getResources().getString(R.string.music_pref_on));
        music.setSummaryOff(getResources().getString(R.string.music_pref_off));
        //default the music to be on
        music.setDefaultValue(true);
        //set key
        music.setKey("music setting");





        //create switch setting for background color
        SwitchPreference backgroundColor = new SwitchPreference(this);
        //set music title and words
        backgroundColor.setTitle(getResources().getString(R.string.background_color_pref_title));
        backgroundColor.setSummaryOn(getResources().getString(R.string.background_color_pref_on));
        backgroundColor.setSummaryOff(getResources().getString(R.string.background_color_pref_off));
        //default the music to be on
        backgroundColor.setDefaultValue(true);
        //set key
        backgroundColor.setKey("background color");





        //create setting for square speed
        ListPreference speed = new ListPreference(this);
        //set title and words
        speed.setTitle(getResources().getString(R.string.speed_pref_title));
        speed.setSummary(getResources().getString(R.string.speed_pref_description));
        //set key
        speed.setKey("speed");
        //set the options
        speed.setEntries(R.array.speed_pref_options);
        String[] speeds = {"5", "3", "1"};
        speed.setEntryValues(speeds);
        //set default
        speed.setDefaultValue("3");




        //create setting for square size
        ListPreference squareSize = new ListPreference(this);
        //set title and words
        squareSize.setTitle(getResources().getString(R.string.square_size_pref_title));
        squareSize.setSummary(getResources().getString(R.string.square_size_pref_description));
        //set key
        squareSize.setKey("square size");
        //set the options
        squareSize.setEntries(R.array.square_size_pref_options);
        String[] sizeValues = {"5", "7", "8"};
        squareSize.setEntryValues(sizeValues);
        //set default
        squareSize.setDefaultValue("3");




        //create setting for game type
        ListPreference gameType = new ListPreference(this);
        //set title and words
        gameType.setTitle(R.string.game_type_pref_title);
        gameType.setSummary(R.string.game_type_pref_description);
        //set key
        gameType.setKey("game type");
        //set the options
        gameType.setEntries(R.array.game_type_pref_options);
        String[] typeValues = {"1", "2", "3", "4", "5"};
        gameType.setEntryValues(typeValues);
        //set default
        gameType.setDefaultValue("1");



        //add the preferences to the screen
        ps.addPreference(music);
        ps.addPreference(backgroundColor);
        ps.addPreference(speed);
        ps.addPreference(squareSize);
        ps.addPreference(gameType);



        //set the preference screen
        setPreferenceScreen(ps);




    }





    /**
     * overridden touch event method - gets called when user touches screen
     *
     * @param m - motion event (required)
     * @return always returns true
     */
    @Override
    public boolean onTouchEvent(MotionEvent m)
    {
        //if user touches screen, start game
        if(m.getAction() == MotionEvent.ACTION_DOWN)
        {

            //start the game (MainActivity)
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

            //kill the beginning page/activity
            finish();

        }

        //always returns true
        return true;
    }





    /**
     * facade method to return whether or not the music is on in settings
     * @param c context
     * @return returns true if the sound is on, false if it is off
     */
    public static boolean soundOn(Context c)
    {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("music setting",true);
    }


    /**
     * facade method to return whether or not the background should be light
     * @param c context
     * @return true if light, false if dark
     */
    public static boolean backgroundIsLight(Context c)
    {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("background color",true);
    }





    /**
     * facade method to return the speed the velocity should be multiplied by
     * @param c context
     * @return the speed
     */
    public static int getSpeed(Context c)
    {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("speed","3"));
    }


    /**
     * facade method to return the size that the square should be
     * @param c context
     * @return the int that will be the size of the square
     */
    public static int getSquareSize(Context c)
    {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("square size","8"));
    }


    /**
     * facade method to return the game type the user selected
     * @param c context
     * @return the int that corresponds to the game type
     */
    public static int getGameType(Context c)
    {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("game type","1"));
    }
}
