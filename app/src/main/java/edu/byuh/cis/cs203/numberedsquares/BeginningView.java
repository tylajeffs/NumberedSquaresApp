package edu.byuh.cis.cs203.numberedsquares;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

public class BeginningView extends AppCompatImageView
{


    /**
     * BeginningView constructor - just initializes a few things
     * @param context
     */
    public BeginningView(Context context)
    {
        //always call super
        super(context);
    }




    /**
     * Overridden onDraw method - takes care of all graphics
     * @param c canvas to draw on
     */
    @Override
    public void onDraw(Canvas c)
    {
        //set the background color
       // c.drawColor(Color.rgb(158, 191, 155));

    }


}
