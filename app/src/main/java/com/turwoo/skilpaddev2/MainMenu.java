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
    private final int BUTTON_MARGINS = 50;
    private final int ROWS = 7;
    private final int START_ROW = 5;

    private final Bitmap START = BitmapFactory.decodeResource(getResources(), R.drawable.start);
    private final Bitmap BACKGROUND = BitmapFactory.decodeResource(getResources(), R.drawable.background);
    private final Bitmap HELP = BitmapFactory.decodeResource(getResources(), R.drawable.help);

    private Bitmap background;
    private int height;
    private int width;

    private final Runnable START_RUNNABLE = new Runnable(){
        public void run() {
            MainActivity.main.setContentView(new Game(MainActivity.main));
        }
    };

    public MainMenu(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(background==null){
            height = canvas.getHeight();
            width = canvas.getWidth();
            background = Bitmap.createScaledBitmap(BACKGROUND, width, height,false);
            initButtons();
        }

        Paint paint = new Paint(Color.BLACK);
        canvas.drawBitmap(background,0,0, paint);

        for(Button b: Button.getButtons()){
            canvas.drawBitmap(b.getBitmap(), b.getX(), b.getY(), paint);
        }

    }

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
