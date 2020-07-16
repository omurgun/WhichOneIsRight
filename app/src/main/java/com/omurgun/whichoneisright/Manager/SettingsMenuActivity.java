package com.omurgun.whichoneisright.Manager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.omurgun.whichoneisright.R;
import com.omurgun.whichoneisright.Settings.ChangeBackgroundSettingActivity;
import com.omurgun.whichoneisright.Settings.ChangeUsernameSettingActivity;

public class SettingsMenuActivity extends AppCompatActivity {

    private Button btnChangeBackground,btnChangeUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);
        translucentStatusBarFlag();
        init();
        btnChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUsernameSetting();
            }
        });
        btnChangeBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackgroundSetting();
            }
        });

    }
    private void goBackgroundSetting() {
        Intent intentLogin = new Intent(SettingsMenuActivity.this, ChangeBackgroundSettingActivity.class);
        startActivity(intentLogin);
    }
    private void goUsernameSetting() {
        Intent intentLogin = new Intent(SettingsMenuActivity.this, ChangeUsernameSettingActivity.class);
        startActivity(intentLogin);
    }
    private void init() {
        btnChangeBackground = findViewById(R.id.btn_change_background);
        btnChangeUsername = findViewById(R.id.btn_change_username);
    }
    private void translucentStatusBarFlag() {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}