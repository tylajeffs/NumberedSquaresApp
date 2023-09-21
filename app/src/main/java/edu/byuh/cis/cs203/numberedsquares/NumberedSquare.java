package edu.byuh.cis.cs203.numberedsquares;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.Random;

import edu.byuh.cis.cs203.numberedsquares.Activities.SettingsActivity;
import edu.byuh.cis.cs203.numberedsquares.Interfaces.TimerListener;


/**
 * Created by tylajeffs
 */

public class NumberedSquare implements TimerListener
{

    private float size;
    public int id;
    public static int counter = 1;
    public RectF squareBounds;
    public String label;
    private float screenWidth, screenHeight;
    private Paint textPaint;
    private Paint squarePaint;
    public PointF velocity;
    private boolean positiveX;
    private boolean positiveY;
    private Random random;
    private boolean velocityCalcDone;
    public boolean frozen;
    private Bitmap normalSquare;
    private Bitmap glitter;
    private Context con;









    /**
     * Constructor for NumberedSquare. This constructor accepts two parameters,
     * representing the width and height of the display. The constructor finds the
     * smaller of these two values (typically, width is smaller for portrait orientation;
     * height is smaller for landscape) and bases the size of the square on that.
     * @param w - the width of the screen
     * @param h - the height of the screen
     */
    public NumberedSquare(String l, float w, float h, Resources r, Context c)
    {
        //set context object
        con = c;

        //set label
        label = l;

        //set screen width and height
        screenWidth = w;
        screenHeight = h;

        //figure out the orientation of the device by seeing whether the length or width is larger
        float lesser = Math.min(screenWidth,screenHeight);

        //set the square size
        size = setSquareSize(lesser);

        //set the square id to be equal to which square it is, then increase the counter
        id = counter;
        counter++;

        //create random x and y's for the box to be drawn at
        float x = (float)(Math.random()*(screenWidth-size));
        float y = (float)(Math.random()*(screenHeight-size));


        //create a rectF object for the square using the random coordinates
        squareBounds = new RectF(x, y, x+size, y+size);

        //load normal square image
        normalSquare = BitmapFactory.decodeResource(r,R.drawable.square);
        normalSquare = Bitmap.createScaledBitmap(normalSquare,(int)squareBounds.width(),(int)squareBounds.height(),true);

        //load frozen square image
        glitter = BitmapFactory.decodeResource(r,R.drawable.silver);
        glitter = Bitmap.createScaledBitmap(glitter,(int)squareBounds.width(),(int)squareBounds.height(),true);

        //create new paint objects for the square and the text
        squarePaint = new Paint();
        textPaint = new Paint();

        //set square attributes
        squarePaint.setStyle(Paint.Style.STROKE);
        squarePaint.setStrokeWidth(lesser/100f);
        squarePaint.setColor(Color.BLACK);


        //set text attributes
        textPaint.setTextSize(65);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);

        //instantiate random object
        random = new Random();

        //instantiate velocity object
        velocity = new PointF();

        //set velocity calculation done to false
        velocityCalcDone = false;

        //set isFrozen to false
        frozen = false;




    }








    /**
     * Render the square and its label into the display
     * @param c the Canvas object, representing the display area
     */
    public void draw(Canvas c)
    {
        //if the square is frozen, draw the normal square image
        if(!frozen)
        {
            //draw the normal square image
            c.drawBitmap(normalSquare,squareBounds.left,squareBounds.top,squarePaint);
        }
        else
        {
            //draw the frozen square image
            c.drawBitmap(glitter,squareBounds.left,squareBounds.top,squarePaint);
        }

        //draw the rectangle using the rectf bounds object
        //c.drawRect(squareBounds,squarePaint);



        //draw the text at the center of the square
        c.drawText(""+label, squareBounds.centerX(), squareBounds.centerY()+size/7, textPaint);
    }
















    /**
     * Move method - makes the squares move
     */
    public void move()
    {
        //if the velocity calculations aren't done, do them
        if(!velocityCalcDone)
        {
            setVelocity();

            //set calculation done to true
            velocityCalcDone = true;
        }



        //if touching a wall, reverse velocity

        //Right wall
        if(squareBounds.right + velocity.x > screenWidth)
        {
            //send message to Logcat saying it touched right
            Log.d("log", "touched right");

            //reverse x velocity
            velocity.x = -velocity.x;
        }

        //Left wall
        if(squareBounds.left + velocity.x < 0)
        {
            //send message to Logcat saying it touched left
            Log.d("log", "touched left");

            //reverse x velocity
            velocity.x = -velocity.x;
        }

        //Top wall
        if(squareBounds.top + velocity.y < 0)
        {

            //send message to Logcat saying it touched top
            Log.d("log", "touched top");

            //reverse y velocity
            velocity.y = -velocity.y;
        }

        //Bottom wall
        if(squareBounds.bottom + velocity.y > screenHeight)
        {
            //send message to Logcat saying it touched bottom
            Log.d("log", "touched bottom");

            //reverse y velocity
            velocity.y = -velocity.y;
        }


        //move the square by the velocity
        squareBounds.offset(velocity.x, velocity.y);

    }





    /**
     * Override the doSomething method from the TimerListener interface
     * make the square move
     */
    @Override
    public void doSomething()
    {
            //make the square move
            move();
    }





    /**
     * method that sets the velocity of the square to 0, freezing it
     */
    public void freeze()
    {
        //set frozen to true
        frozen = true;

        //set velocity to 0
        velocity.set(0,0);

        /*
        //change the text and square color
        squarePaint.setStyle(Paint.Style.FILL);
        squarePaint.setColor(Color.BLACK);
         */
    }




    public void setBottom(float b) {
        float dy = b - squareBounds.bottom;
        squareBounds.offset(0, dy);
    }

    public void setRight(float r) {
        float dx = r - squareBounds.right;
        squareBounds.offset(dx, 0);
    }

    public void setLeft(float lf) {
        squareBounds.offsetTo(lf, squareBounds.top);
    }

    public void setTop(float t) {
        squareBounds.offsetTo(squareBounds.left, t);
    }




    /**
     * Method to see if the squares overlap
     * @param other the numbered square object you want to test against
     */
    public boolean overlaps(NumberedSquare other)
    {
        //return true if the squares overlap, return false if they don't
        return RectF.intersects(this.squareBounds, other.squareBounds);
    }


    /**
     * @param x float of an x coordinate
     * @param y float of a y coordinate
     * @return returns true if the coordinates are inside the square
     */
    public boolean contains(float x, float y)
    {
        //test to see if the square contains the coordinates
        return (squareBounds.contains(x,y));
    }





    /**
     * method that does the velocity calculations and comes up with a random velocity
     * for each square
     */
    private void setVelocity()
    {
        //set velocity to be random x and y, positive or negative

        //calculate random velocity
        velocity.x = (float)(Math.random());
        velocity.y = (float)(Math.random());

        //get random boolean to see if they are positive or negative
        positiveX = getRandomBoolean();
        positiveY = getRandomBoolean();

        //check if x should be negative
        if(!positiveX)
        {
            //make x negative
            velocity.x *= -1;
        }

        //check if y should be negative
        if(!positiveY)
        {
            //make y negative
            velocity.y *= -1;
        }


        //multiply the velocity by the mode multiplier from settings
        velocity.x *= SettingsActivity.getSpeed(con);

    }


    /**
     * method to set the square size based on the preferences/settings
     * @param lesser - the width of the shortest orientation
     */
    private float setSquareSize(float lesser)
    {
        //set the square size to be some fraction of the shortest orientation based on settings
        return lesser/SettingsActivity.getSquareSize(con);
    }


    /**
     * Static convenience method to ensure that the ID numbers don't grow too large.
     */
    public static void resetCounter()
    {
        //reset the counter back to 1
        counter = 1;
    }

    /**
     * method to decrease the counter by 1
     */
    public void decreaseCounter()
    {
        //decrease counter by 1
        counter--;
    }


    /**
     * method to find a random boolean
     *
     * @return a random boolean
     */
    public boolean getRandomBoolean()
    {
        //return a random boolean, true or false
        return random.nextBoolean();
    }
}



