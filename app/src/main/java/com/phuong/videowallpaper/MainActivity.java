package com.phuong.videowallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Button setWallpaper;
    private Context mContext;
    private CheckBox checkVoice;
    private EditText setVideoPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        initView();
        initListener();
    }

    private void initView() {
        setWallpaper = (Button) findViewById(R.id.set_wall_pager);
        checkVoice = (CheckBox) findViewById(R.id.check_voive);
        setVideoPath = (EditText) findViewById(R.id.set_video_path);
    }

    private void initListener() {
        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(setVideoPath.getText().toString());
                VideoWallpaper videoWallpaper = new VideoWallpaper();

                videoWallpaper.setToWallPaper(mContext,setVideoPath.getText().toString());


            }
        });
        checkVoice.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            VideoWallpaper.voiceSilence(getApplicationContext());
                        } else {
                            VideoWallpaper.voiceNormal(getApplicationContext());
                        }
                    }
                });
    }
}
