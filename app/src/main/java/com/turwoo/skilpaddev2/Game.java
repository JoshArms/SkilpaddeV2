//************************************//
// Game.java                          //
//      by: Josh Arms                 //
// Purpose: Handles the game portions //
//          of the app                //
//************************************//

package com.turwoo.skilpaddev2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.turwoo.skilpaddev2.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends View {
    //START constant game vars
    private final int ITEM_SIZE = 50; //size of falling items
    private final int DROP_RATE = 1500; //milliseconds until next drop at the start
    private final int REFRESH_RATE = 10; //milliseconds until next draw refresh
    private final int TURT_STARTING_SPEED = 5; //turtle starting speed
    private final int ITEM_STARTING_SPEED = 5; //item starting speed;
    private final int STARTING_Y = 100; //How far off the ground the turtle starts
    private final int TURTLE_HEIGHT = 100; //turtle height
    private final int TURTLE_WIDTH = 200; //turtle width
    private final int FIRST_SPECIAL_ITEM_INDEX = 5; //index of first item that doesn't cuase you to lose if missed
    private final int POINTS_DIS_LOC_Y = 50; //Y-coordinate of the points text
    private final int POINTS_DIS_LOC_X = 100; //X-coordinate of the points text
    //END constant game vars
    //START Constant Bitmaps
    private final Bitmap BACK = BitmapFactory.decodeResource(getResources(), R.drawable.background); //background img
    //Possible bitmaps for items. This is parallel to itemActions
    private final Bitmap[] itemBms = {BitmapFactory.decodeResource(getResources(), R.drawable.red),
            BitmapFactory.decodeResource(getResources(), R.drawable.orange),
            BitmapFactory.decodeResource(getResources(), R.drawable.yellow),
            BitmapFactory.decodeResource(getResources(), R.drawable.green),
            BitmapFactory.decodeResource(getResources(), R.drawable.blue),
            BitmapFactory.decodeResource(getResources(), R.drawable.coin),
            BitmapFactory.decodeResource(getResources(), R.drawable.poison)};
    //END Constant Bitmaps
    //START Runnable vars
    //Runnable for when a point is collected
    private final Runnable addPoint = new Runnable() {
        public void run() {
            g.points++;
        }
    };
    //Runnable for when poison is collected
    private final Runnable endGame = new Runnable() {
        public void run() {
            g.end();
        }
    };
    //Runnable for when a coin is collected
    private final Runnable coinCollected = new Runnable() {
        public void run() {
            g.addCoin();
        }
    };
    //Possible actions for items. This is parallel to itemBms
    private final Runnable[] itemActions = {addPoint,
            addPoint,
            addPoint,
            addPoint,
            addPoint,
            coinCollected,
            endGame};
    //END Runnable Vars

    //START game vars
    public static Game g;
    private Bitmap background;
    public int points; //Points collected during that game: static so runnables can access it
    private Turtle turt; //Turtle character in the game
    private ArrayList<Item> items;
    public int height; //height of screen
    public int width; //width of screen
    public int itemSpeed; //speed of falling items
    public boolean gameOver; //to know when to end the game
    private int counter;


    //Function Game(Context context)
    //Purpose: Game constructor
    //Parameters: Context context: the context of the view
    //Returns: None
    public Game(Context context) {
        super(context);
        //set vars
        this.points = 0;
        this.items = new ArrayList<>();
        this.itemSpeed = ITEM_STARTING_SPEED;
        this.gameOver = false;
        this.counter = DROP_RATE - REFRESH_RATE;
        Game.g = this;

        this.time();
    }
    //Function: initVars()
    //Purpose: To initialize variables that are dependent on the height or/and width
    //Parameters: none
    //Returns: none
    public void initVars(){
        background = Bitmap.createScaledBitmap(BACK, width, height, false);

        Bitmap turtMap = BitmapFactory.decodeResource(getResources(), R.drawable.turtle_l);
        turt = new Turtle(turtMap,TURTLE_WIDTH, height - STARTING_Y, TURTLE_HEIGHT, TURTLE_WIDTH, TURT_STARTING_SPEED);

    }

    //Function: onDraw(Canvas)
    //Purpose: Draws the game to the screen
    //Parameters: Canvas canvas: the canvas to draw to
    //Returns: none
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(background==null){
            height = canvas.getHeight();
            width = canvas.getWidth();
            initVars();
        }
        updateVars();

        //Begin drawing
        Paint paint = new Paint(Color.BLACK);
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawText("Points: "+points, POINTS_DIS_LOC_X, POINTS_DIS_LOC_Y, paint);
        canvas.drawBitmap(turt.getBitmap(), turt.getX(), turt.getY(), null);

        for (Item it : items) {
            canvas.drawBitmap(it.getBitmap(), it.getX(), it.getY(), null);
        }

    }
    //Function: updateVars()
    //Purpose: updates the object variables
    //Parameters: none
    //Returns: none
    public void updateVars(){
        if(gameOver){
            //***
            //Save scores
            //***
            View view = new Game(MainActivity.main); //this is overly simplified way to end it for current tests
            MainActivity.main.setContentView(view);
        }

        counter += REFRESH_RATE;
        if (counter % DROP_RATE == 0) {
            counter = 0;
            int randX = (int) Math.floor(Math.random() * (width - ITEM_SIZE));
            int index = (int) Math.floor(Math.random() * (itemBms.length));
            Item newItem = new Item(randX, 0 - ITEM_SIZE, ITEM_SIZE, itemSpeed, itemBms[index], itemActions[index], index < FIRST_SPECIAL_ITEM_INDEX);
            items.add(newItem);
        }

        turt.move(g,MainActivity.dir); //move turtle

        //move all items
        Iterator<Item> it = items.iterator();
        while(it.hasNext()){
            Item curItem = it.next();
            curItem.move();
            if(curItem.getY() > height){ //if the item is off the screen
                if(curItem.isLossIfMissed()){
                    this.end();
                }
            } else if (turt.collecting(curItem)){ //if the turtle got the item
                curItem.act();
                it.remove();
            }
        }

    }

    //Function: movementAndTime()
    //Purpose: move all moving objects
    //         This function also handles the timer to ensure everything keeps updating
    //Parameters: none
    //Returns: nothing
    public void time() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                postInvalidate(); //tells it to draw again after finishing the last drawing
            }
        }, 2000, REFRESH_RATE);
    }

    //Function: end()
    //Purpose: Ends the game
    //Parameters: none
    //returns: nothing
    public void end() {
        gameOver = true;
    }

    //Function: addCoin()
    //Purpose: adds a coin to user's bank
    //Parameters: none
    //returns: nothing
    public void addCoin() {

    }
}
