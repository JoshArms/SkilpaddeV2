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
import android.view.MotionEvent;
import android.view.View;

import com.turwoo.skilpaddev2.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends View {
    //START constant game vars
    private final int ITEM_SIZE = 50; //size of falling items
    private final int STARTING_DROP_RATE = 1000; //milliseconds until next drop at the start
    private final int REFRESH_RATE = 10; //milliseconds until next draw refresh
    private final int TURT_STARTING_SPEED = 7; //turtle starting speed
    private final int ITEM_STARTING_SPEED = 5; //item starting speed;
    private final int STARTING_Y = 200; //How far off the ground the turtle starts
    private final int TURTLE_HEIGHT = 100; //turtle height
    private final int TURTLE_WIDTH = 200; //turtle width
    private final int FIRST_SPECIAL_ITEM_INDEX = 5; //index of first item that doesn't cuase you to lose if missed
    private final int POINTS_DIS_LOC_Y = 50; //Y-coordinate of the points text
    private final int POINTS_DIS_LOC_X = 10; //X-coordinate of the points text
    private final int TURT_SPEED_INCREASE = 1; //amount the turtle speed increases each time
    private final int ITEM_SPEED_INCREASE = 1; //amount the item speed increases each time
    private final int POINTS_PER_ITEM_INCREASE = 5; //number of points until item speed is increased
    private final int POINTS_PER_TURT_INCREASE = 10; //number of points until turtle speed is increased
    private final int MIN_DROP_RATE = 500;
    private final int MAX_ITEM_SPEED = 10;
    private final int DROP_RATE_CHANGE = 100;
    private final int TEXT_SIZE = 50; //text size
    private final int PAUSE_Y = 15; //y-coordinate for pause button
    private final int PAUSE_X = 15; //x-coordinate for pause button will equal (screen width - button width - PAUSE_X)
    private final int PAUSE_SIZE = 100; //width and height of the pause button
    private final int BTN_ROWS = MainMenu.ROWS;
    private final int BUTTON_MARGINS = MainMenu.BUTTON_MARGINS;
    private final int EXIT_ROW = 5;
    private final int COOKIE_SPEED_BOOST = 7;
    private final int COOKIE_EFFECT_TIME = 5000; //5 seconds
    //END constant game vars
    //START Constant Bitmaps
    private final Bitmap BACK = BitmapFactory.decodeResource(getResources(), R.drawable.background); //background img
    private final Bitmap PAUSE_BM = BitmapFactory.decodeResource(getResources(), R.drawable.pause); //pause button
    private final Bitmap START_BM = BitmapFactory.decodeResource(getResources(), R.drawable.play); //start button
    private final Bitmap REPLAY_BM = BitmapFactory.decodeResource(getResources(), R.drawable.replay); //replay button
    private final Bitmap EXIT_BM = BitmapFactory.decodeResource(getResources(), R.drawable.back); //Exit button
    //Possible bitmaps for items. This is parallel to itemActions
    private final Bitmap[] itemBms = {BitmapFactory.decodeResource(getResources(), R.drawable.red),
            BitmapFactory.decodeResource(getResources(), R.drawable.orange),
            BitmapFactory.decodeResource(getResources(), R.drawable.yellow),
            BitmapFactory.decodeResource(getResources(), R.drawable.green),
            BitmapFactory.decodeResource(getResources(), R.drawable.blue),
            BitmapFactory.decodeResource(getResources(), R.drawable.coin),
            BitmapFactory.decodeResource(getResources(), R.drawable.cookie),
            BitmapFactory.decodeResource(getResources(), R.drawable.poison)};
    //END Constant Bitmaps
    //START Runnable vars
    //Runnable for when a point is collected
    private final Runnable addPoint = new Runnable() {
        public void run() {
            g.points++;
            //update speeds
            if(g.points%POINTS_PER_ITEM_INCREASE==0){
                if(itemSpeed < MAX_ITEM_SPEED){
                    itemSpeed+=ITEM_SPEED_INCREASE;
                }
                if(dropRate > MIN_DROP_RATE){
                    dropRate-=DROP_RATE_CHANGE;
                }
            }
            if(g.points%POINTS_PER_TURT_INCREASE==0){
                turt.changeSpeed(TURT_SPEED_INCREASE);
            }
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
    //Runnable for when a cookie is collected
    private final Runnable cookieCollected = new Runnable() {
        public void run() {
            if(!powerUp){
                turt.changeSpeed(COOKIE_SPEED_BOOST);
                powerUp = true;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        turt.changeSpeed(-COOKIE_SPEED_BOOST);
                        powerUp = false;
                    }
                }, COOKIE_EFFECT_TIME);
            }
        }
    };
    //Possible actions for items. This is parallel to itemBms
    private final Runnable[] itemActions = {addPoint,
            addPoint,
            addPoint,
            addPoint,
            addPoint,
            coinCollected,
            cookieCollected,
            endGame};
    //END Runnable Vars

    //START Button Runnables
    private final Runnable PAUSE = new Runnable() {
        public void run() {
            paused = true;

            activatePauseBtns();
        }
    };
    private final Runnable START = new Runnable() {
        public void run() {
            paused = false;
            Button.deactivateAll();
            initButtons();
        }
    };
    private final Runnable EXIT = new Runnable() {
        public void run() {
            MainActivity.main.setContentView(new MainMenu(MainActivity.main));
        }
    };
    private final Runnable REPLAY = new Runnable() {
        public void run() {
            MainActivity.main.setContentView(new Game(MainActivity.main));
        }
    };
    //END Button Runnables

    //START game vars
    public static Game g;
    private Bitmap background;
    private int points; //Points collected during that game: static so runnables can access it
    private Turtle turt; //Turtle character in the game
    private ArrayList<Item> items;
    public int height; //height of screen
    public int width; //width of screen
    private int itemSpeed; //speed of falling items
    private boolean gameOver; //to know when to end the game
    private int counter;
    private boolean paused; //true if paused, else false
    private int dropRate;
    private boolean powerUp;

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
        this.counter = STARTING_DROP_RATE - REFRESH_RATE;
        Game.g = this;
        this.paused = false;
        this.dropRate = STARTING_DROP_RATE;
        this.powerUp = false;

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
        initButtons();
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
        paint.setTextSize(TEXT_SIZE);
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawText("Points: "+points, POINTS_DIS_LOC_X, POINTS_DIS_LOC_Y, paint);
        canvas.drawBitmap(turt.getBitmap(), turt.getX(), turt.getY(), null);

        for (Item it : items) {
            canvas.drawBitmap(it.getBitmap(), it.getX(), it.getY(), null);
        }

        for(Button b: Button.getButtons()){
            canvas.drawBitmap(b.getBitmap(), b.getX(), b.getY(), null);
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
            Button.deactivateAll();
            paused = true;
            int btnWidths = width - (2*BUTTON_MARGINS);
            int btnHeights = height/BTN_ROWS;
            Button.activate(new Button(BUTTON_MARGINS, ((height/BTN_ROWS)*(EXIT_ROW-1)), btnWidths, btnHeights, REPLAY, REPLAY_BM ));
            Button.activate(new Button(BUTTON_MARGINS, ((height/BTN_ROWS)*EXIT_ROW)+BUTTON_MARGINS, btnWidths, btnHeights, EXIT, EXIT_BM));
        }

        //add new item if it's time to
        counter += REFRESH_RATE;
        if (counter >= this.dropRate) {
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

    //Function: time()
    //Purpose: handle the timer
    //Parameters: none
    //Returns: nothing
    public void time() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if(!paused){
                    postInvalidate(); //tells it to draw again after finishing the last drawing
                }
            }
        }, 1000, REFRESH_RATE);
    }

    //Function: end()
    //Purpose: Ends the game
    //Parameters: none
    //returns: nothing
    public void end() { gameOver = true; }

    //Function: addCoin()
    //Purpose: adds a coin to user's bank
    //Parameters: none
    //returns: nothing
    public void addCoin() {

    }

    //Function: onTouchEvent(MotionEvent)
    //Purpose: To handle when people touch buttons
    //Parameters: MotionEvent event: the event that contains info on what happened
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
    //Purpose: initializes starting buttons
    //Parameters: none
    //Returns: none
    public void initButtons(){ //this is called after height and width of the screen have been determined
        Button.deactivateAll();
        Button pauseBtn = new Button(width - PAUSE_SIZE - PAUSE_X, PAUSE_Y, PAUSE_SIZE, PAUSE_SIZE, PAUSE, PAUSE_BM);
        Button.activate(pauseBtn);
    }
    //Function: activatePauseBtns()
    //Purpose: initializes pause buttons
    //Parameters: none
    //Returns: none
    public void activatePauseBtns(){
        Button.deactivateAll();
        Button startBtn = new Button(width - PAUSE_SIZE - PAUSE_X, PAUSE_Y, PAUSE_SIZE, PAUSE_SIZE, START, START_BM);
        Button.activate(startBtn);
        Button exitBtn = new Button(BUTTON_MARGINS, (height/BTN_ROWS)*EXIT_ROW+BUTTON_MARGINS, width-(2*BUTTON_MARGINS), height/BTN_ROWS, EXIT, EXIT_BM);
        Button.activate(exitBtn);
    }
}
