//************************************//
// Button.java                        //
//      by: Josh Arms                 //
// Purpose: Handles all the buttons   //
//                                    //
//************************************//
package com.turwoo.skilpaddev2;

import android.graphics.Bitmap;
import android.graphics.Rect;
import java.util.ArrayList;

public class Button {
    private Runnable action;
    private int x;
    private int y;
    private int width;
    private int height;
    private Bitmap bm;

    private static ArrayList<Button> buttons;

    //Function: Button(int, int, int, int, Runnable, bm)
    //Purpose: Button constructor
    //Parameters: int x: x-coordinate
    //            int y: y-coordinate
    //            int width: width of the button
    //            int height: height of the button
    //            Runnable action: action for when it is clicked
    //            Bitmap bm: bitmap for the item
    //Returns: nothing
    public Button(int x, int y, int width, int height, Runnable action, Bitmap bm){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.action = action;
        this.bm = Bitmap.createScaledBitmap(bm, width, height, false);
    }
    //BEGIN accessor methods
    public Bitmap getBitmap(){ return this.bm; }
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
    //END accessor methods

    //Function: isClicked(int, int)
    //Purpose: to determine if the button has been clicked after coordinates have been touched
    //Parameters: int x: x-coordinate that has been selected by the user
    //            int y: y-coordinate that has been selected by the user
    //Returns: true if the button has been clicked, otherwise false
    public boolean isClicked(int x, int y){
        //makes the button into a rectangle b/c rectangles have a contains method
        Rect rect = new Rect(this.x,this.y,this.x + this.width,this.y + this.height);
        return rect.contains(x, y);
    }
    //Function: act()
    //Purpose: triggers the runnable that is supposed to be triggered when it is clicked
    //Parameters: none
    //Returns: none
    public void act(){
        action.run();
    }
    //Function: getButtons()
    //Purpose: accessor method (not included with the others b/c this one is static)
    //Parameters: none
    //Returns: An ArrayList of buttons
    public static ArrayList<Button> getButtons(){
        return buttons;
    }
    //Function: activate(Button)
    //Purpose: to add a button so it has a chance to be clicked
    //Parameters: Button b: button to be added
    //Returns: none
    public static void activate(Button b){
        if(buttons.isEmpty()){
            buttons = new ArrayList<>();
        }
        buttons.add(b);
    }
    //Function: deactivateAll()
    //Purpose: makes the buttons no longer clickable
    //Parameters: none
    //Returns: none
    public static void deactivateAll(){
        buttons = new ArrayList<>();
    }
}
