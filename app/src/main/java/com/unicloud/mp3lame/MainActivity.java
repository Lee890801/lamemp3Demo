package com.unicloud.mp3lame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.unicloud.mp3lame.engine.RecordEngine;
import com.unicloud.mp3lame.inter.IRecordListener;

import org.jetbrains.annotations.Nullable;

public class MainActivity extends AppCompatActivity {


    private Button btnStartRecord;
    private Button btnStopRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartRecord = findViewById(R.id.btn_start);
        btnStopRecord = findViewById(R.id.btn_stop);

        btnStartRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordEngine.getInstance().startRecord(MainActivity.this, new IRecordListener() {
                    @Override
                    public void calculateVolume(@Nullable Integer db) {

                    }

                    @Override
                    public void onRecordComplete(@Nullable String path) {

                    }

                    @Override
                    public void onRecordError(@Nullable String error) {

                    }

                    @Override
                    public void onRecordStart() {

                    }
                });
            }
        });

        btnStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordEngine.getInstance().stopRecord();
            }
        });

    }


}
