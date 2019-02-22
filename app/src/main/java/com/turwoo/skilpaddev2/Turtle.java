package com.turwoo.skilpaddev2;

import android.graphics.Bitmap;

public class Turtle {
    private int speed; //speed the turtle will move at
    private Bitmap bm; //Bitmap for the Turtle
    private int x; //x-coordinate for the Turtle
    private int y; //y-coordinate for the Turtle
    private int height; //height of the Turtle
    private int length; //length of the Turtle

    //Function: Turtle(int, int, int, int, int)
    //Purpose: Constructor
    //Parameters: int x: x-coordinate
    //            int y: y-coordinate
    //            int height: Turtle height
    //            int length: Turtle length
    //            int speed: Turtle speed
    //returns: none
    public Turtle(int x, int y, int height, int length, int speed){
        this.x = x;
        this.y = y;
        this.height = height;
        this.length = length;
        this.speed = speed;
    }

    //START Accessor Methods
    public Bitmap getBitmap(){ return bm; }
    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getHeight(){ return height; }
    public int getLength(){ return length; }
    //END Accessor Methods

    //Function: move(int)
    //Purpose: moves the Turtle left and right
    //Parameters: int dir: -1 or 1 to decide direction of the movement
    //Returns: nothing
    public void move(int dir){
        this.x += dir*this.speed;
    }

    //Function: changeSpeed(int)
    //Purpose: changes the speed
    //Parameter: int change: the amount to add to speed
    //returns: nothing
    public void changeSpeed(int change){ this.speed += change; }
}
