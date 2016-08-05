package com.youshangxiaozi.testweidge;

import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private SlotNumView slotNumView;
    private GiftGroove gg;
    private PathAnimatorContainer pac;
    private static final int[] IDS = new int[]{
            R.mipmap.icon_live_heart0,
            R.mipmap.icon_live_heart1,
            R.mipmap.icon_live_heart2,
            R.mipmap.icon_live_heart3,
            R.mipmap.icon_live_heart4,
            R.mipmap.icon_live_heart5,
            R.mipmap.icon_live_heart6,
            R.mipmap.icon_live_heart7,
    };
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        findViewById(R.id.start_re).setOnClickListener(this);
        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.add_188).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);


        slotNumView = (SlotNumView)findViewById(R.id.slotNumView);
        TextView v = new TextView(this);
        v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        v.setTextColor(0xffff0000);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        slotNumView.setTextView(v);

        gg = (GiftGroove)findViewById(R.id.gg);
        gg.setOnClickListener(this);

        pac = (PathAnimatorContainer) findViewById(R.id.pac);
        pac.postDelayed(new Star(), 3000);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        slotNumView.setWay(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    int count = 0;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_re:
                slotNumView.start_re();
                break;
            case R.id.add:
                slotNumView.addCount(1);
                break;
            case R.id.add_188:
                slotNumView.addCount(188);
                break;
            case R.id.pause:
                slotNumView.pause();
                break;
            case R.id.gg:
                if (count++ % 2 == 0) {
                    gg.shrink(200);
                } else {
                    gg.spread(200);
                }
                break;
        }
    }

    private boolean focusVisiable;
    @Override
    protected void onResume() {
        super.onResume();
        focusVisiable = true;
    }

    @Override
    protected void onPause() {
        focusVisiable = false;
        super.onPause();
    }

    /**
     *
     */
    class Star implements Runnable{

        @Override
        public void run() {
            ImageView iv = new ImageView(MainActivity.this);
            iv.setImageResource(IDS[random.nextInt(IDS.length)]);
            pac.addFavor(iv, new PointF(250, 900), new PointF(random.nextInt(1000), random.nextInt(200 + random.nextInt(300))),
                    new PointF(250, 0));
            pac.postDelayed(new Star(), 3000);
        }
    }
}
