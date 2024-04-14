package com.gamecodeschool.livedrawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class LiveDrawingView extends SurfaceView implements Runnable{

    // Are we debugging?
    private final boolean DEBUGGING = true;

    // These objects are needed to do the drawing
    private SurfaceHolder mOurHolder;
    private Canvas mCanvas;
    private Paint mPaint;

    // How many frames per second did we get?
    private long mFPS;
    // The number of milliseconds in a second
    private final int MILLIS_IN_SECOND = 1000;

    // Holds the resolution of the screen
    private int mScreenX;
    private int mScreenY;
    // How big will the text be?
    private int mFontSize;
    private int mFontMargin;

    // Here is the Thread and two control variables
    private Thread mThread = null;
    // This volatile variable can be accessed
    // from inside and outside the thread
    private volatile boolean mDrawing;
    private boolean mPaused = true;



    // The particle systems will be declared here later

    // These will be used to make simple buttons

    // The LiveDrawingView constructor
    // Called when this line:
    // mLiveDrawingView = new LiveDrawingView(this, size.x, size.y);
    // is executed from LiveDrawingActivity
    public LiveDrawingView(Context context, int x, int y) {
        // Super... calls the parent class
        // constructor of SurfaceView
        // provided by the Android API
        super(context);

        // Initialize these two members/fields
        // With the values passed in as parameters
        mScreenX = x;
        mScreenY = y;

        // Font is 5% (1/20th) of screen width
        mFontSize = mScreenX / 20;
        // Margin is 1.5% (1/75th) of screen width
        mFontMargin = mScreenX / 75;

        // getHolder is a method of SurfaceView
        mOurHolder = getHolder();
        mPaint = new Paint();

        // Initialize the two buttons

        // Initialize the particles and their systems

    }

    // When we start the thread with:
    // mThread.start();
    // the run method is continuously called by Android
    // because we implemented the Runnable interface
    // Calling mThread.join();
    // will stop the thread
    @Override
    public void run() {
        // mDrawing gives us finer control
        // rather than just relying on the calls to run
        // mDrawing must be true AND
        // the thread running for the main
        // loop to execute
        while (mDrawing) {

            // What time is it now at the start of the loop?
            long frameStartTime = System.currentTimeMillis();

            // Provided the app isn't paused
            // call the update method
            if(!mPaused){
                update();
                // Now the particles are in
                // their new positions

            }

            // The movement has been handled and now
            // we can draw the scene.
            draw();

            // How long did this frame/loop take?
            // Store the answer in timeThisFrame
            long timeThisFrame =
                    System.currentTimeMillis() - frameStartTime;

            // Make sure timeThisFrame is at least 1 millisecond
            // because accidentally dividing
            // by zero crashes the app
            if (timeThisFrame > 0) {
                // Store the current frame rate in mFPS
                // ready to pass to the update methods of
                // of our particles in the next frame/loop
                mFPS = MILLIS_IN_SECOND / timeThisFrame;
            }
        }
    }

    private void update() {
        // Update the particles

    }


    // Draw the particle systems and the HUD
    private void draw() {
        if (mOurHolder.getSurface().isValid()) {
            // Lock the canvas (graphics memory) ready to draw
            mCanvas = mOurHolder.lockCanvas();

            // Fill the screen with a solid color
            mCanvas.drawColor(Color.argb(255, 0, 0, 0));

            // Choose a color to paint with
            mPaint.setColor(Color.argb(255, 255, 255, 255));

            // Choose the font size
            mPaint.setTextSize(mFontSize);

            // Draw the particle systems

            // Draw the buttons

            // Draw the HUD
            if(DEBUGGING){
                printDebuggingText();
            }


            // Display the drawing on screen
            // unlockCanvasAndPost is a method of SurfaceHolder
            mOurHolder.unlockCanvasAndPost(mCanvas);
        }

    }

    private void printDebuggingText(){
        int debugSize = mFontSize / 2;
        int debugStart = 150;
        mPaint.setTextSize(debugSize);
        mCanvas.drawText("FPS: " + mFPS ,
                10, debugStart + debugSize, mPaint);

        // We will add more code here in the next chapter

    }

    // This method is called by LiveDrawingActivity
// when the user quits the app
    public void pause() {

        // Set mDrawing to false
        // Stopping the thread isn't
        // always instant
        mDrawing = false;
        try {
            // Stop the thread
            mThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }


    // This method is called by LiveDrawingActivity
    // when the player starts the app
    public void resume() {
        mDrawing = true;
        // Initialize the instance of Thread
        mThread = new Thread(this);

        // Start the thread
        mThread.start();
    }



}






