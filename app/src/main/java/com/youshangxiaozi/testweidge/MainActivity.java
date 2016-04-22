package com.youshangxiaozi.testweidge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private SlotNumView slotNumView;
    private BothTransContainer bothTransContainer;
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

        bothTransContainer = (BothTransContainer)findViewById(R.id.btc);
        bothTransContainer.setOnClickListener(this);
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
            case R.id.btc:
                if (count++ % 2 == 0) {
                    bothTransContainer.shrink(200);
                } else {
                    bothTransContainer.spread(200);
                }
                break;
        }
    }
}
