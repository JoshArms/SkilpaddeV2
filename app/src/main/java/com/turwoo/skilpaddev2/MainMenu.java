//************************************//
// MainMenu.java                      //
//      by: Josh Arms                 //
// Purpose: Handles the Main Menu     //
//          of the app                //
//************************************//
package com.turwoo.skilpaddev2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class MainMenu extends View {
    public final static int BUTTON_MARGINS = 50; //Button margins
    public final static int ROWS = 7; //Rows to divide the screen into
    public final static int START_ROW = 5; //The row the start button will appear on

    private final String TITLE = "Skilpadde V2"; //Title at the top //Will eventually be changed to a more appealing look
    private final int TEXT_SIZE = 110; //Text size of the title
    private final int TITLE_ROW = 2; //Row the title will appear on
    private final Bitmap START = BitmapFactory.decodeResource(getResources(), R.drawable.start); //Start button bitmap
    private final Bitmap BACKGROUND = BitmapFactory.decodeResource(getResources(), R.drawable.background); //background bitmap
    private final Bitmap HELP = BitmapFactory.decodeResource(getResources(), R.drawable.help); //help button bitmap

    private Bitmap background; //background bitmap
    private int height; //height of the screen
    private int width; //width of the screen

    private final Runnable START_RUNNABLE = new Runnable(){
        public void run() { //runnable for the start button
            MainActivity.main.setContentView(new Game(MainActivity.main));
        }
    };
    //Function: MainMenu(Context)
    //Purpose: Constructor
    //Parameters: Context context: the context to display to
    //returns: none
    public MainMenu(Context context){
        super(context);
    }

    //Function: onDraw(Canvas canvas)
    //Purpose: Handles the display for this class
    //Parameters: Canvas canvas: the canvas to draw to
    //Returns: none
    @Override
    protected void onDraw(Canvas canvas) {
        height = canvas.getHeight();
        width = canvas.getWidth();
        background = Bitmap.createScaledBitmap(BACKGROUND, width, height,false);
        initButtons();

        //Begin displaying
        Paint paint = new Paint(Color.BLACK);
        paint.setTextSize(TEXT_SIZE);
        canvas.drawBitmap(background,0,0, paint);
        canvas.drawText(TITLE, BUTTON_MARGINS, (height/ROWS)*TITLE_ROW, paint);

        for(Button b: Button.getButtons()){ //Display buttons
            canvas.drawBitmap(b.getBitmap(), b.getX(), b.getY(), paint);
        }

    }

    //Function: onTouchEvent(MotionEvent)
    //Purpose: handles button selections
    //Parameters: MotionEvent event: event that occurred
    //Returns: true
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int movement = event.getAction();

        switch(movement){
            case MotionEvent.ACTION_DOWN:
                int x = (int)event.getX();
                int y = (int)event.getY();
                for(Button b: Button.getButtons()){
                    if(b.isClicked(x,y)){
                        b.act();
                    }
                }
                break;
        }
        return true;
    }
    //Function: initButtons()
    //Purpose: initializes buttons
    //Parameters: none
    //Returns: none
    public void initButtons(){ //this is called after height and width of the screen have been determined
        Button.deactivateAll();
        int buttonWidth = width - (2*BUTTON_MARGINS);
        int buttonHeight = height/ROWS;
        int x = BUTTON_MARGINS;
        int y = ((width/ROWS)*START_ROW)+BUTTON_MARGINS;

        Button start = new Button(x,y,buttonWidth,buttonHeight,START_RUNNABLE, START);
        Button.activate(start);
    }
}
