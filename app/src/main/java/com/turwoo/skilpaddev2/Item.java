package com.turwoo.skilpaddev2;

import android.graphics.Bitmap;

public class Item {
    private int x; //x-coordinate of the item
    private int y; //y-coordinate of the item
    private int height; //height of the item (all of them are squares so this is also the length)
    private int speed; //speed the item falls at
    private Bitmap bm; //Bitmap for the item
    private Runnable action; //function to be called when the item is interacted with

    //Function: Item(int, int, int, int, Bitmap, Runnable)
    //Purpose: constructor
    //Parameters: int x = x-coordinate
    //            int y = y-coordinate
    //            int height = object height
    //            int speed = speed it will fall at
    //            Bitmap bm = Bitmap for the item
    //            Runnable action = function to run when the item is interacted with
    //returns: none
    public Item(int x, int y, int height, int speed, Bitmap bm, Runnable action){
        this.x = x;
        this.y = y;
        this.height = height;
        this.speed = speed;
        this.bm = bm;
        this.action = action;
    }

    //START Accessor Methods
    public int getX(){ return this.x; }
    public int getY(){ return this.y; }
    public int getHeight(){ return this.height; }
    public Bitmap getBitmap(){ return this.bm; }
    //END Accessor Methods

    //Function: act()
    //Purpose: calls the function associated with the item
    //Parameters: none
    //Returns: none
    public void act(){ action.run(); }

    //Function: move()
    //Purpose: makes an item fall down
    //Parameters: none
    //Returns none
    public void move(){ this.y += this.speed; }

}
