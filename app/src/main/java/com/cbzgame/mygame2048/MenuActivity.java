package com.cbzgame.mygame2048;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import java.io.IOException;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    public Button soundButton;
    public Button bgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button continueButton = findViewById(R.id.continue_button_menuACT);
        Button restartGameButton = findViewById(R.id.restart_button_menuACT);
        soundButton = findViewById(R.id.sound_button_menuACT);
        bgButton = findViewById(R.id.bg_button_menuACT);

        if(!MainActivity.getMainActivity().shoundPlay)
        {
            soundButton.setText("游戏音效(关)");
        }
        if(MainActivity.getMainActivity().bgPlay)
        {
            MainActivity.getMainActivity().mediaPlayer.start();
            bgButton.setText("背景音乐(开)");
        }
        else
        {
            bgButton.setText("背景音乐(关)");
        }

        continueButton.setOnClickListener(this);
        restartGameButton.setOnClickListener(this);
        soundButton.setOnClickListener(this);
        bgButton.setOnClickListener(this);

        /**
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }
         **/
        MainActivity.getMainActivity().mediaPlayer.start();
        hitMenuThread();
    }

    @Override
    public void onClick(View v) {
        hitMenuThread();
        switch (v.getId())
        {
            case R.id.continue_button_menuACT:
            {
                finish();
            }break;
            case R.id.restart_button_menuACT:
            {
                MainActivity.getMainActivity().myGirdGame.startGame();
                finish();
            }break;
            case R.id.sound_button_menuACT:
            {
                if(MainActivity.getMainActivity().shoundPlay)
                {
                    soundButton.setText("游戏音效(关)");
                    MainActivity.getMainActivity().shoundPlay = false;
                }
                else
                {
                    soundButton.setText("游戏音效(开)");
                    MainActivity.getMainActivity().shoundPlay = true;
                }
            }break;
            case R.id.bg_button_menuACT:
            {
                if(MainActivity.getMainActivity().bgPlay)
                {
                    MainActivity.getMainActivity().bgPlay = false;
                    MainActivity.getMainActivity().mediaPlayer.pause();
                    ((MenuActivity)v.getContext()).bgButton.setText("背景音乐(关)");
                }
                else
                {
                    MainActivity.getMainActivity().bgPlay = true;
                    MainActivity.getMainActivity().mediaPlayer.start();
                    ((MenuActivity)v.getContext()).bgButton.setText("背景音乐(开)");
                }
            }break;
            default:
                break;
        }
    }

    private void hitMenuThread()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AssetManager assetManagerPause;
                MediaPlayer mediaPlayer_pause = new MediaPlayer(); //播放音频
                try {
                    assetManagerPause = getAssets();
                    AssetFileDescriptor assetFileDescriptor = assetManagerPause.openFd("pause.mp3");
                    mediaPlayer_pause.reset();
                    mediaPlayer_pause.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                    mediaPlayer_pause.prepare();
                    mediaPlayer_pause.seekTo(600);
                    if(MainActivity.getMainActivity().shoundPlay)
                    {
                        mediaPlayer_pause.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (!mediaPlayer_pause.isPlaying())
                    {
                        mediaPlayer_pause.stop();
                        mediaPlayer_pause.release();
                    }
                }
            }
        }).start();
    }
}
