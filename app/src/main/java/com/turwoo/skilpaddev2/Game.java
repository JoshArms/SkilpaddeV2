package com.turwoo.skilpaddev2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

public class Game extends View {
    private final Bitmap BACK = BitmapFactory.decodeResource(getResources(), R.drawable.back);


    public Game(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

    }
}
