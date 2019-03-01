//************************************//
// MainMenu.java                      //
//      by: Josh Arms                 //
// Purpose: Handles the Main Menu     //
//          of the app                //
//************************************//
package com.turwoo.skilpaddev2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class MainMenu extends View {
    private Bitmap background;
    private int height;
    private int width;

    public MainMenu(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(background==null){
            height = canvas.getHeight();
            width = canvas.getWidth();
            //initVars();
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

    }
}
