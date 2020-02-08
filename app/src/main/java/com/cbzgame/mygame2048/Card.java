package com.cbzgame.mygame2048;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Card extends FrameLayout {

    private int number = 0;
    TextView tvNumber;
    public Card(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCard();
    }

    public Card(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initCard();
    }


    public Card(@NonNull Context context) {
        super(context);
        initCard();
    }

    @SuppressLint("SetTextI18n")
    private void initCard()
    {
        setBackgroundColor(Color.rgb(222, 139, 97));
        tvNumber = new TextView(getContext());
        tvNumber.setTextSize(32);
        tvNumber.setGravity(Gravity.CENTER);
        tvNumber.setBackgroundColor(Color.rgb(255, 191, 159));
        if(number > 0)
        {
            tvNumber.setText(number + "");
        }
        else {
            tvNumber.setText("");
        }
        LayoutParams params = new LayoutParams(-1, -1);
        params.setMargins(20, 20, 0, 0);
        addView(tvNumber, params);
    }

    public int getNumber() {
        return number;
    }

    @SuppressLint("SetTextI18n")
    public void setNumber(int number) {
        switch (number)
        {
            case 0:{
                tvNumber.setBackgroundColor(0x33FFFFFF);
            }break;
            case 2: {
                tvNumber.setTextColor(getResources().getColor(R.color.text2));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg2));
            }break;
            case 4:{
                tvNumber.setTextColor(getResources().getColor(R.color.text4));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg4));
            }break;
            case 8:{
                tvNumber.setTextColor(getResources().getColor(R.color.text8));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg8));
            }break;
            case 16: {
                tvNumber.setTextColor(getResources().getColor(R.color.text16));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg16));
            }break;
            case 32:{
                tvNumber.setTextColor(getResources().getColor(R.color.text32));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg32));
            }break;
            case 64:{
                tvNumber.setTextColor(getResources().getColor(R.color.text64));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg64));
            }break;
            case 128: {
                tvNumber.setTextColor(getResources().getColor(R.color.text128));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg128));
            }break;
            case 256:{
                tvNumber.setTextColor(getResources().getColor(R.color.text256));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg256));
            }break;
            case 512:{
                tvNumber.setTextColor(getResources().getColor(R.color.text512));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg512));
            }break;
            case 1024: {
                tvNumber.setTextColor(getResources().getColor(R.color.text1024));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg1024));
            }break;
            case 2048:{
                tvNumber.setTextColor(getResources().getColor(R.color.text2048));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg2048));
            }break;
            default:{
                tvNumber.setTextColor(getResources().getColor(R.color.bestsuper));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.textsupper));
            }break;
        }
        this.number = number;
        if(number > 0)
        {
            this.tvNumber.setText(number + "");
        }
        else
        {
            this.tvNumber.setText("");
        }
    }

    public boolean equals(Card card)
    {
        return this.number == card.getNumber();
    }
}