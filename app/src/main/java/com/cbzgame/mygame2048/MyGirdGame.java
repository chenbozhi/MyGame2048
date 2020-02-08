package com.cbzgame.mygame2048;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//1322 7175 938
public class MyGirdGame extends GridLayout {

    //音效
    AssetManager assetManagerSlip;
    public MediaPlayer mediaPlayer_slip = new MediaPlayer(); //播放音频

    private Card[][] cardsMap = new Card[4][4];
    private List<Point> pointList = new ArrayList<>();

    //还非要写这三个构造方法
    public MyGirdGame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMyGirdGame();
    }


    public MyGirdGame(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMyGirdGame();
    }


    public MyGirdGame(Context context) {
        super(context);
        initMyGirdGame();
    }

    private void initMyGirdGame()
    {
        setColumnCount(4);
        setBackgroundColor(Color.rgb(222, 139, 97));

        setOnTouchListener(new OnTouchListener() {
            private float startX, startY;
            private float endX, endY;
            private float offsetX, offsetY;  //偏移量
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:   //按下
                    {
                        startX = event.getX();
                        startY = event.getY();
                    }
                    case MotionEvent.ACTION_UP:   //手指松开
                    {
                        endX = event.getX();
                        endY = event.getY();

                        offsetX = endX - startX;
                        offsetY = endY - startY;

                        if(Math.abs(offsetX) > Math.abs(offsetY))  //水平偏移量 大于 竖直偏移量 就说明是在 横向滑动
                        {
                            if(offsetX < -5)   //当滑动偏移量比较小的时候，不认为滑动有效，滑动比较大的时候认为有效滑动
                            {
                                moveLeft();
                                //Log.d("Gird", "left");
                                //Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
                            }
                            else if(offsetX > 5)
                            {
                                //Log.d("Gird", "right");
                                //Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
                                moveRight();
                            }
                        }
                        else
                        {
                            if(offsetY < -5)   //当滑动偏移量比较小的时候，不认为滑动有效，滑动比较大的时候认为有效滑动
                            {
                                //Log.d("Gird", "up");
                                //Toast.makeText(getContext(), "up", Toast.LENGTH_SHORT).show();  //画面坐标原点默认是在左上角，纵向往下数值变大
                                moveUp();
                            }
                            else if(offsetY > 5)
                            {
                                //Log.d("Gird", "down");
                                //Toast.makeText(getContext(), "down", Toast.LENGTH_SHORT).show();
                                moveDown();
                            }
                        }
                    }
                }
                return true;
            }
        });
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        for(int j = 0; j < 4; j++)
        {
            for(int i = 0; i < 4; i++)
            {
                Card card = new Card(getContext());
                card.setNumber(0);
                addView(card, Math.min(w, h)/4, Math.min(w, h)/4);
                cardsMap[j][i] = card;
            }
        }

        startGame();
    }

    private void addRandomNumber()
    {
        pointList.clear();

        for(int j = 0; j < 4; j++)
        {
            for(int i = 0; i < 4; i++)
            {
                if(cardsMap[j][i].getNumber() <= 0)
                {
                    pointList.add(new Point(j ,i));
                }
            }
        }
        Point point = pointList.remove((int)(Math.random() * pointList.size()));
        //Toast.makeText(getContext(), point.x + " " + point.y, Toast.LENGTH_SHORT).show();
        cardsMap[point.x][point.y].setNumber(Math.random() > 0.1 ? 2 : 4);
    }

    public void startGame()
    {
        MainActivity.getMainActivity().clearScore();
        for(int j = 0; j < 4; j++)
        {
            for(int i = 0; i < 4; i++)
            {
                cardsMap[j][i].setNumber(0);
            }
        }
        addRandomNumber();
        addRandomNumber();
    }

    private void moveLeft()
    {
        slipSoundThread();
        boolean move = false;
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                for(int y = j + 1; y < 4; y++)
                {
                    if(cardsMap[i][y].getNumber() > 0)
                    {
                        if(cardsMap[i][j].getNumber() <= 0)   //当前值为0
                        {
                            cardsMap[i][j].setNumber(cardsMap[i][y].getNumber());  //当前位置为空， 则下一位置左移
                            cardsMap[i][y].setNumber(0);
                            y = j + 1;
                            move = true;
                        }
                        else if(cardsMap[i][j].equals(cardsMap[i][y]))
                        {
                            addSoundThread();
                            cardsMap[i][j].setNumber(cardsMap[i][j].getNumber() * 2);
                            cardsMap[i][y].setNumber(0);
                            MainActivity.getMainActivity().addScore(cardsMap[i][j].getNumber());
                            updateBestScore();
                            move = true;
                        }
                    }
                }
            }
        }
        if(move)
        {
            addRandomNumber();
            checkGame();
        }
    }
    private void moveRight()
    {
        slipSoundThread();
        boolean move = false;
        for(int i = 0; i < 4; i++)
        {
            for(int j = 3; j >= 0; j --)
            {
                for(int y = j - 1; y >= 0; y--)
                {
                    if(cardsMap[i][y].getNumber() > 0)
                    {
                        if(cardsMap[i][j].getNumber() <= 0)
                        {
                            cardsMap[i][j].setNumber(cardsMap[i][y].getNumber());
                            cardsMap[i][y].setNumber(0);
                            y = j - 1;
                            move = true;
                        }
                        else if(cardsMap[i][j].equals(cardsMap[i][y]))
                        {
                            addSoundThread();
                            cardsMap[i][j].setNumber(cardsMap[i][j].getNumber() * 2);
                            cardsMap[i][y].setNumber(0);
                            MainActivity.getMainActivity().addScore(cardsMap[i][j].getNumber());
                            updateBestScore();
                            move = true;
                        }
                    }
                }
            }
        }
        if(move)
        {
            addRandomNumber();
            checkGame();
        }
    }
    private void moveUp()
    {
        slipSoundThread();
        boolean move = false;
        for(int j = 0; j < 4; j++)
        {
            for(int i = 0; i <4; i ++)
            {
                for(int x = i + 1; x < 4; x++)
                {
                    if(cardsMap[x][j].getNumber() >0)
                    {
                        if(cardsMap[i][j].getNumber() <= 0)
                        {
                            cardsMap[i][j].setNumber(cardsMap[x][j].getNumber());
                            cardsMap[x][j].setNumber(0);
                            x = i + 1;
                            move = true;
                        }
                        else if(cardsMap[i][j].equals(cardsMap[x][j]))
                        {
                            addSoundThread();
                            cardsMap[i][j].setNumber(cardsMap[i][j].getNumber() * 2);
                            cardsMap[x][j].setNumber(0);
                            MainActivity.getMainActivity().addScore(cardsMap[i][j].getNumber());
                            updateBestScore();
                            move = true;
                        }
                    }
                }
            }
        }
        if(move)
        {
            addRandomNumber();
            checkGame();
        }
    }
    private void moveDown()
    {
        slipSoundThread();
        boolean move = false;
        for(int j = 0; j < 4; j++)
        {
            for(int i = 3; i >= 0; i --)
            {
                for(int x = i - 1; x >= 0; x --)
                {
                    if(cardsMap[x][j].getNumber() > 0)
                    {
                        if(cardsMap[i][j].getNumber() <= 0)
                        {
                            cardsMap[i][j].setNumber(cardsMap[x][j].getNumber());
                            cardsMap[x][j].setNumber(0);
                            x = i - 1;
                            move = true;
                        }
                        else if(cardsMap[i][j].equals(cardsMap[x][j]))
                        {
                            addSoundThread();
                            cardsMap[i][j].setNumber(cardsMap[i][j].getNumber() * 2);
                            cardsMap[x][j].setNumber(0);
                            MainActivity.getMainActivity().addScore(cardsMap[i][j].getNumber());
                            updateBestScore();
                            move = true;
                        }
                    }
                }
            }
        }
        if(move)
        {
            addRandomNumber();
            checkGame();
        }
    }

    private void checkGame()
    {
        boolean complete = true;

        ALL:
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                if(cardsMap[i][j].getNumber() <= 0 ||
                        (i > 0 && cardsMap[i][j].equals(cardsMap[i - 1][j]))||
                        (i < 3 && cardsMap[i][j].equals(cardsMap[i + 1][j]))||
                        (j > 0 && cardsMap[i][j].equals(cardsMap[i][j - 1]))||
                        (j < 3 && cardsMap[i][j].equals(cardsMap[i][j + 1]))){
                    complete = false;
                    break ALL;
                }
            }
        }

        if(complete)
        {
            new AlertDialog
                    .Builder(getContext())
                    .setTitle("2048")
                    .setMessage("哎呀，游戏结束了~")
                    .setPositiveButton("在来一次吧！", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                        }
                    }).show();
        }
    }
    private void updateBestScore()
    {
        long bestScore, score;
        SharedPreferences sp = getContext().getSharedPreferences("game2048", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        score = MainActivity.getMainActivity().getScore();
        bestScore = sp.getLong("bestScore", 0);
        if(bestScore < score)
        {
            editor.putLong("bestScore", score);
            MainActivity.getMainActivity().setBestScore(score);
            editor.apply();
        }
    }

    //滑动音效
    private void slipSoundThread()
    {
        if(MainActivity.getMainActivity().shoundPlay)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        assetManagerSlip = getContext().getAssets();
                        AssetFileDescriptor assetFileDescriptor = assetManagerSlip.openFd("slip.wav");
                        mediaPlayer_slip.reset();
                        mediaPlayer_slip.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                        mediaPlayer_slip.prepare();
                        mediaPlayer_slip.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if (mediaPlayer_slip != null && !mediaPlayer_slip.isPlaying())
                        {
                            mediaPlayer_slip.stop();
                            mediaPlayer_slip.release();
                            mediaPlayer_slip = null;
                        }
                    }
                }
            }).start();
        }
    }

    private void addSoundThread()
    {
        if(MainActivity.getMainActivity().shoundPlay)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AssetManager assetManagerAdd;
                    MediaPlayer mediaPlayer_add = new MediaPlayer(); //播放音频
                    try {
                        assetManagerAdd = getContext().getAssets();
                        AssetFileDescriptor assetFileDescriptor = assetManagerAdd.openFd("add.wav");
                        mediaPlayer_add.reset();
                        mediaPlayer_add.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                        mediaPlayer_add.prepare();
                        mediaPlayer_add.seekTo(1000);
                        if(MainActivity.getMainActivity().shoundPlay)
                        {
                            mediaPlayer_add.start();
                        }
                        mediaPlayer_add.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if (!mediaPlayer_add.isPlaying())
                        {
                            mediaPlayer_add.stop();
                            mediaPlayer_add.release();
                        }
                    }
                }
            }).start();
        }
    }
}
/*
File file = new File("file:///android_asset/bg.mp3");
//Log.i("音乐文件路径", file.getPath());
try {
    mediaPlayer_slip.setDataSource(file.getPath());
    mediaPlayer_slip.prepare();
    mediaPlayer_slip.start();
} catch (IOException e) {
    e.printStackTrace();
}
 */