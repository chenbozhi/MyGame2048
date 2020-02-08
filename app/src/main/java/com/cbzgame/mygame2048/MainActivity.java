package com.cbzgame.mygame2048;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public boolean shoundPlay = true;  //控制音效是否开
    public boolean bgPlay = true;

    private TextView tvScore;
    private TextView tvBestScore;
    @SuppressLint("StaticFieldLeak")
    private static MainActivity mainActivity = null;
    private long score = 0;
    private long bestScore = 0;
    public MediaPlayer mediaPlayer = new MediaPlayer(); //播放音频
    AssetManager assetManager;

    MyGirdGame myGirdGame;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvScore = findViewById(R.id.tv_score);
        tvBestScore = findViewById(R.id.tv_best_score);

        SharedPreferences sp = getSharedPreferences("game2048", MODE_PRIVATE);
        bestScore = sp.getLong("bestScore", 0);
        tvBestScore.setText(score + "");
        mainActivity = this;

        myGirdGame = findViewById(R.id.myGirdGame);

        Button menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_open_menu_ACT = new Intent(v.getContext(), MenuActivity.class);
                startActivity(intent_open_menu_ACT);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    assetManager = getAssets();
                    AssetFileDescriptor assetFileDescriptor = assetManager.openFd("bg.mp3");
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());

                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepareAsync();   //使用异步装载
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                          @Override
                                                          public void onPrepared(MediaPlayer mp) {
                                                              mediaPlayer.start();
                                                          }
                                                      });

                    mediaPlayer.setLooping(true);
                    //mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*
                 mediaPlayer = new MediaPlayer();
02.2                 mediaPlayer.setDataSource(path);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
04.4
05.5                 // 通过异步的方式装载媒体资源
06.6                 mediaPlayer.prepareAsync();
07.7                 mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
08.8                     @Override
09.9                     public void onPrepared(MediaPlayer mp) {
10.10                         // 装载完毕回调
11.11                         mediaPlayer.start();
12.12                     }
13.13                 });

                 */
            }
        }).start();

    }

    public static MainActivity getMainActivity()
    {
        return mainActivity;
    }

    public void clearScore()
    {
        score = 0;
        showScore();
    }
    @SuppressLint("SetTextI18n")
    public void showScore()
    {
        tvScore.setText(score + "");
        tvBestScore.setText(bestScore + "");
    }
    public void addScore(int s)
    {
        score += s;
        showScore();
    }
    public long getScore()
    {
        return score;
    }

    public void setBestScore(long s) {
        this.bestScore = s;
        showScore();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MainActivity.getMainActivity().bgPlay)
        {
            if(!mediaPlayer.isPlaying())
            {
                mediaPlayer.start();
            }
        }
    }
}
