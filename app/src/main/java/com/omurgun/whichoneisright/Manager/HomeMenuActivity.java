package com.omurgun.whichoneisright.Manager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.omurgun.whichoneisright.Game.GameActivity;
import com.omurgun.whichoneisright.Game.ShowMaxScoresActivity;
import com.omurgun.whichoneisright.R;

public class HomeMenuActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private int maxScore;
    private Button btnGameStart,btnShowTopList,btnSettings;
    public TextView highScoreText;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);
        translucentStatusBarFlag();
        init();
        sharedPreferences = this.getSharedPreferences("com.omurgun.whiconeisright", Context.MODE_PRIVATE);
        maxScore = sharedPreferences.getInt("maxScore", 0);
        highScoreText.setText("High Score : "+maxScore);
        btnGameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();
                Intent intent = new Intent(HomeMenuActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
        btnShowTopList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();
                Intent intent = new Intent(HomeMenuActivity.this, ShowMaxScoresActivity.class);
                startActivity(intent);
            }
        });
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();
                Intent intent = new Intent(HomeMenuActivity.this, SettingsMenuActivity.class);
                startActivity(intent);
            }
        });
    }
    private void init() {
        btnGameStart = findViewById(R.id.btnGameStart);
        highScoreText = findViewById(R.id.highScoreText);
        btnShowTopList = findViewById(R.id.btnShowTopList);
        btnSettings = findViewById(R.id.btnSettings);
        player = MediaPlayer.create(HomeMenuActivity.this,R.raw.song);
    }
    private void translucentStatusBarFlag() {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
