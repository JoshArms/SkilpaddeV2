//************************************//
// Turtle.java                        //
//      by: Josh Arms                 //
// Purpose: Turtle object for the     //
//          Game class                //
//************************************//

package com.turwoo.skilpaddev2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

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
    public Turtle(Bitmap bm, int x, int y, int height, int length, int speed){
        this.x = x;
        this.y = y;
        this.height = height;
        this.length = length;
        this.speed = speed;
        this.bm = Bitmap.createScaledBitmap(bm, length, height, false);
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
    public void move(Game g, double dir){
        if(dir>0){ //makes the turtle face the direction it is moving
           this.bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(g.getResources(), R.drawable.turtle_r), length, height, false);
        }else{
            this.bm =Bitmap.createScaledBitmap(BitmapFactory.decodeResource(g.getResources(), R.drawable.turtle_l), length, height, false);
        }
        this.x += dir*this.speed;
    }

    //Function: changeSpeed(int)
    //Purpose: changes the speed
    //Parameter: int change: the amount to add to speed
    //returns: nothing
    public void changeSpeed(int change){ this.speed += change; }

    //Function: collecting (Item)
    //Purpose: detects if the turtle is collecting (touching) an item
    //Parameters: Item it: item to check if it is collecting
    //Returns: true if it is touching the item, else false
    public boolean collecting(Item it){
        //rectangle for the item
        Rect itR = new Rect(it.getX(), it.getY(), it.getX()+it.getHeight(), it.getY()+it.getHeight());
        //rectangle for the turtle
        Rect TurtR = new Rect(this.x,this.y,this.x+this.length,this.y+this.height);
        return TurtR.intersect(itR); //if they intersect the turtle is collecting it
    }
}
